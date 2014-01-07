package com.hisupplier.cn.account.inquiry;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jws.WebService;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisupplier.cas.CASClient;
import com.hisupplier.cas.Ticket;
import com.hisupplier.cn.account.alert.TradeAlertService;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Inquiry;
import com.hisupplier.cn.account.entity.TradeAlert;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.member.UserDAO;
import com.hisupplier.cn.account.sms.Message1;
import com.hisupplier.cn.account.sms.SMSFactory;
import com.hisupplier.cn.account.util.MailFactory;
import com.hisupplier.cn.account.webservice.InquirySendService;
import com.hisupplier.cn.account.webservice.RegisterService;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.basket.BasketItem;
import com.hisupplier.commons.basket.BasketItem.Product;
import com.hisupplier.commons.basket.BasketItem.Trade;
import com.hisupplier.commons.entity.Attachment;
import com.hisupplier.commons.entity.Category;
import com.hisupplier.commons.mail.InquiryMailSenderFactory;
import com.hisupplier.commons.mail.Mail;
import com.hisupplier.commons.mail.MailSender;
import com.hisupplier.commons.task.TaskExecutor;
import com.hisupplier.commons.util.CategoryUtil;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.MemcachedUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;
import com.hisupplier.commons.util.cn.LocaleUtil;

@WebService(endpointInterface = "com.hisupplier.cn.account.webservice.InquirySendService")
public class InquirySendServiceImpl implements InquirySendService {

	private static final Log log = LogFactory.getLog(InquirySendServiceImpl.class);
	private static final ConcurrentMap<String, IP> ipCache = new ConcurrentHashMap<String, IP>(300);
	private static final List<String> allowIP = new ArrayList<String>(); // ��������Ⱥ��ѯ���ʼ���IP
	private static final Pattern patternMobile = Pattern.compile("^(\\+?86-)?((13|15|18)[0-9]{9})$", Pattern.CASE_INSENSITIVE);

	private CompanyDAO companyDAO;
	private UserDAO userDAO;
	private InquiryDAO inquiryDAO;
	private TradeAlertService tradeAlertService;
	private RegisterService registerService;

	static {
		allowIP.add("210.83.71.238"); // ��˾��ͨIP
		allowIP.add("122.227.163.86");// ��˾����IP
		allowIP.add("127.0.0.1");

		long delay = 10 * CN.UNIT_MINUTE;
		TaskExecutor.scheduleAtFixedRate(new Runnable() {
			public void run() {
				if (log.isDebugEnabled()) {
					log.debug("cleaning ip cache");
				}
				long now = System.currentTimeMillis();
				Iterator<IP> iter = ipCache.values().iterator();
				while (iter.hasNext()) {
					IP ip = iter.next();
					if (now >= (ip.time + CN.UNIT_DAY)) {
						if (log.isDebugEnabled()) {
							log.debug("remove ip " + ip.key);
						}
						iter.remove();
					}
				}
			}
		}, delay, delay);
	}

	private class IP {
		public String key;
		public long time;
		public int count;

		public IP(String key, long time, int count) {
			super();
			this.key = key;
			this.time = time;
			this.count = count;
		}
	}

	@SuppressWarnings("unchecked")
	public String send(Inquiry inquiry) {
		List<BasketItem> basketItemList = inquiry.getBasketItemList();
		// ���IP
		if (!this.checkIP(inquiry.getFromIP(), basketItemList.size())) {
			return "ipLimit";
		}

		Ticket loginUser = inquiry.getLoginUser();
		Ticket currentMember = null;
		// Register register = null;
		String memberId = null;

		// �ѵ�¼
		if (loginUser != null) {
			// ���15�������Ƿ�����ͬ����
			if (isRepeatInquiry(loginUser.getMemberId(), inquiry)) {
				return "inquiry.repeat";
			}
			memberId = loginUser.getMemberId();
			Company company = this.companyDAO.findCompany(loginUser.getComId());
			User user = this.userDAO.findUser(loginUser.getUserId(), loginUser.getComId());
			inquiry.setFromUserId(loginUser.getUserId());
			inquiry.setFromComId(loginUser.getComId());
			inquiry.setFromProvince(user.getProvince());
			inquiry.setFromCity(user.getCity());
			inquiry.setFromTown(user.getTown());
			inquiry.setFromCompany(company.getComName());
			inquiry.setFromName(user.getContact());
			inquiry.setFromEmail(user.getEmail());
			inquiry.setFromTel(user.getTel());
			inquiry.setFromFax(user.getFax());
			inquiry.setFromStreet(user.getStreet());
			inquiry.setFromWebsite(company.getWebsites());
		} else {
			// �ǻ�Ա
			if (!inquiry.isNewUser()) {
				currentMember = CASClient.getInstance().validatePasswd(inquiry.getEmail(), inquiry.getPasswd());
				memberId = currentMember.getMemberId();
				if (currentMember.getMessage().equals("notfound") || currentMember.getMessage().equals("error")) {
					return "passwdError";
				}
				// ���15�������Ƿ�����ͬ����
				if (isRepeatInquiry(currentMember.getMemberId(), inquiry)) {
					return "inquiry.repeat";
				}
				Company company = this.companyDAO.findCompany(currentMember.getComId());
				User user = this.userDAO.findUser(currentMember.getUserId(), currentMember.getComId());
				inquiry.setFromUserId(currentMember.getUserId());
				inquiry.setFromComId(currentMember.getComId());
				inquiry.setFromProvince(user.getProvince());
				inquiry.setFromCity(user.getCity());
				inquiry.setFromTown(user.getTown());
				inquiry.setFromCompany(company.getComName());
				inquiry.setFromName(user.getContact());
				inquiry.setFromEmail(user.getEmail());
				inquiry.setFromTel(user.getTel());
				inquiry.setFromFax(user.getFax());
				inquiry.setFromStreet(user.getStreet());
				inquiry.setFromWebsite(company.getWebsites());
			} 
			/* ȡ������ѯ��ע�� jira: http://192.168.1.240:8080/browse/CN-245
			else {
				// ���û�
				inquiry.setPasswd("123456");// ����Ĭ������
				register = new Register();
				register.setRegMode(6);// ��վѯ�̶���ע��
				register.setEmail(inquiry.getFromEmail());
				register.setPasswd(inquiry.getPasswd());
				register.setComName(inquiry.getFromCompany());
				register.setContact(inquiry.getFromName());
				register.setSex(inquiry.getSex());
				register.setProvince(inquiry.getFromProvince());
				register.setCity(inquiry.getFromCity());
				register.setTown(inquiry.getFromTown());
				register.setTel(inquiry.getFromTel());
				register.setFax(inquiry.getFromFax());
				register.setStreet(inquiry.getFromStreet());
				register.setWebsites(inquiry.getFromWebsite());

				String result = registerService.join(register);
				// ֻ��"��վ"��"��������"��ѯ���ڻ�Աע��ʧ�ܺ���Ҫ������ʾ
				if (!result.equals("addSuccess") && inquiry.getFromSite() <= 2) {
					return result;
				}
				memberId = register.getMemberId();
				inquiry.setFromUserId(register.getUserId());
				inquiry.setFromComId(register.getComId());
			}
			*/
		}

		// ����ѯ��Ĭ��ֵ
		inquiry.setRead(false);
		inquiry.setRecommend(false);
		inquiry.setState(CN.STATE_PASS); // ����״̬
		if (inquiry.getFromSite() <= 0) {
			inquiry.setFromSite(1); // Ĭ����վ
		}
		inquiry.setCreateTime(new DateUtil().getDateTime());

		// �ϴ�ѯ�̸�����ͼƬ������
		if (inquiry.getUpload() != null) {
			StringBuffer buff = new StringBuffer();
			for (int i = 0; i < inquiry.getUpload().length; i++) {
				Attachment attachment = null;
				try {
					attachment = UploadUtil.uploadFileStream(0, 1, inquiry.getUploadFileName()[i], new FileInputStream(inquiry.getUpload()[i]));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				if (attachment != null) {
					buff.append(attachment.getFilePath()).append(",");
				}
			}
			if (buff.length() > 0) {
				buff.deleteCharAt(buff.length() - 1);
			}
			inquiry.setFilePath(buff.toString());
		} else {
			inquiry.setFilePath("");
		}

		// ����ѯ���ʼ������̣߳�ÿ���3�뷢��һ���ʼ�
//		MailSender mailSender = MailSenderCNFactory.createSender(MailSenderCNFactory.INQUIRY);
		MailSender mailSender = InquiryMailSenderFactory.createSender();
		mailSender.setDelay(CN.UNIT_SECOND * 3);

		// ѯ�̺��ʼ��б�
		Map<String, Object> mailAndInquiryMap = this.getInquiryList(inquiry, basketItemList);
		List<Inquiry> inquiryList = (List<Inquiry>) mailAndInquiryMap.get("inquiryList");
		List<Mail> mailList = (List<Mail>) mailAndInquiryMap.get("mailList");

		// �����Ƽ�ѯ��
		this.getRecommend(inquiry, basketItemList, mailList, inquiryList);

		// ����ѯ�̺Ͷ���
		if (inquiryList.size() > 0) {
			this.inquiryDAO.addInquiry(inquiryList);
			mailSender.add(mailList);
			TaskExecutor.execute(mailSender);
			this.sendSms(inquiry, basketItemList);
		}

		if (StringUtil.isEmpty(inquiry.getFromCompanyType())) { 
			// ���鶩��
			TradeAlert tradeAlert = this.getTradeAlert(inquiry);
			if (tradeAlert != null) {
				// �ѵ�¼
				if (loginUser != null) {
					tradeAlert.setComId(loginUser.getComId());
					tradeAlert.setUserId(loginUser.getUserId());
				} else {
					// �ǻ�Ա
					if (!inquiry.isNewUser() && currentMember != null) {
						tradeAlert.setComId(currentMember.getComId());
						tradeAlert.setUserId(currentMember.getUserId());
					} 
					/*
					else {
						// ���û�
						if (register != null && register.getComId() > 0 && register.getUserId() > 0) {
							tradeAlert.setComId(register.getComId());
							tradeAlert.setUserId(register.getUserId());
							if (StringUtil.isEmpty(tradeAlert.getCatIds())) {
								tradeAlert.setCatIds("");
							}
						}
					}
					*/
				}
				String result = tradeAlertService.addTradeAlert(tradeAlert);
				if("alert.limit".equals(result)){
					return result;
				}
			}
		}
		// ���� ѯ�̷��ͼ�¼��Memcache
		if (memberId != null) inquiryCacheIn(memberId, inquiry);
		return "addSuccess";
	}
	
	/**
	 * �Ƚ��Ƿ��ظ�����
	 * @param memberId
	 * @param inquiry
	 * 	<pre>
	 * 	inquiry.content
	 * </pre>
	 * @return
	 */
	private boolean isRepeatInquiry(String memberId, Inquiry inquiry) {
		Object temp = null;
		if ((temp = MemcachedUtil.get("inquiry_sendId_" + memberId))  != null) {
			if (inquiry.getContent().equals(String.valueOf(temp))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ��ѯ��д�뵽Memcached�У�����15����
	 * @author wuyaohui
	 * @param memberId
	 * @param inquiry
	 * <pre>
	 * 	inquiry.content
	 * </pre>
	 */
	private void inquiryCacheIn(String memberId, Inquiry inquiry) {
		MemcachedUtil.set("cn_inquiry_send_" + memberId, inquiry.getContent(), new Date(15 * CN.UNIT_MINUTE));
	}

	/**
	 * ���IP��ÿIPÿ24Сʱ֮��ֻ��Ⱥ�����15���ʼ�������15���ÿ�ξ�ֻ�ܷ���1��
	 * @param clientIP
	 * @param size
	 * @return
	 */
	private boolean checkIP(String clientIP, int size) {
		boolean token = false;
		if (StringUtil.isEmpty(clientIP)) {
			return false;
		}
		if (allowIP.contains(clientIP)) {
			return true;
		}
		long now = System.currentTimeMillis();
		if (StringUtil.isNotEmpty(clientIP)) {
			IP ip = ipCache.get(clientIP);
			if (ip != null) {
				if (size > 1 && ip.count >= 15) {
					token = false;
				} else {
					token = true;
				}
				ip.count += size;
			} else {
				ip = new IP(clientIP, now, 0);
				ipCache.put(clientIP, ip);
				token = true;
			}
			log.debug("ip:" + ip.key + ", count:" + ip.count + ", time:" + DateUtil.formatDateTime(new Date(now)));
		}
		return token;
	}

	/**
	 * ���Ͷ��ţ���վѯ��(ֻ�й���Ա���ܹ����ܵ�����)
	 * @param inquiry
	 * @param basketItemList
	 */
	private void sendSms(Inquiry inquiry, List<BasketItem> basketItemList) {
		Message1 message = null;
		List<Message1> messageList = new ArrayList<Message1>();
		for (BasketItem basketItem : basketItemList) {
			StringBuffer content = new StringBuffer();
			String contact = basketItem.getContact();
			String email = basketItem.getEmail();
			String mobile = basketItem.getMobile();
			if (StringUtil.isNotEmpty(contact) && StringUtil.isNotEmpty(email) && StringUtil.isNotEmpty(mobile)) {
				Matcher matched = patternMobile.matcher(mobile);
				if (basketItem.isSms() && matched.matches()) {
					content.append("�װ���");
					content.append(contact);
					if (basketItem.getMemberType() != CN.FREE_SITE && basketItem.getInquiryReceive() == CN.INQUIRY_ACCEPTER_USER) {// ����goldsite�����ʺŵ�����½�Ϊfreesite
						content.append("����˾��һ������").append(LocaleUtil.getProvince(inquiry.getFromProvince()));// ���ء��й�����½�����Ķ��Żᱻ����
						content.append("��ѯ�̣������HiSupplier��̨����!");
					} else {
						content.append("������һ������").append(LocaleUtil.getProvince(inquiry.getFromProvince()));
						content.append("��ѯ�̣��뼰ʱ��").append(email).append("����!");
					}
					String content_ = StringUtil.replaceAll(content.toString(), Pattern.compile("(cnd|av){1,}", Pattern.CASE_INSENSITIVE), "...");
					message = new Message1(matched.group(2), content_);
					message.setMobile(matched.group(2));
					messageList.add(message);
					log.info("�������ţ���Ա�ʺ�" + basketItem.getMemberId() + "���ֻ����룺" + mobile + "�����ݣ�" + content_);
				}
			}
		}
		SMSFactory.addMessage(messageList);
	}

	/**
	 * ����GoldSite�Ƽ�ѯ��
	 * @param inquiry
	 * @param basketItemList
	 * @param mailList
	 * @param inquiryList
	 */
	private void getRecommend(Inquiry inquiry, List<BasketItem> basketItemList, List<Mail> mailList, List<Inquiry> inquiryList) {
		StringBuffer catIds = new StringBuffer();
		for (BasketItem item : basketItemList) {
			if (item.getMemberType() == CN.FREE_SITE && item.getProductList().size() > 0) {
				int catId = item.getProductList().get(0).getCatId();
				Category cat = CategoryUtil.getById(catId);
				if (cat != null && cat.getCatName(true) != null && cat.getCatName(true).indexOf("����") == -1) {
					catIds.append(catId).append(",");
				}
			}
		}
		if (catIds.length() > 0) {
			catIds.deleteCharAt(catIds.length() - 1);
		}
		if (catIds.length() > 0) {
			List<User> userlist = this.userDAO.findUserListByProCatId(catIds.toString());
			if (userlist != null && userlist.size() > 0) {
				for (User user : userlist) {// ���Ƽ���ͬһĿ¼�µĲ�Ʒ�����ˣ�ÿ�˷�һ���ʼ�
					Inquiry recommendInq = new Inquiry();
					try {
						PropertyUtils.copyProperties(recommendInq, inquiry);
					} catch (Exception e) {
						continue;
					}
					recommendInq.setToEmail(user.getEmail());
					recommendInq.setToName(user.getContact());
					user.setCatIds(catIds.toString());
					Mail emailRecommend = MailFactory.getInquiryRecommend(recommendInq, user);
					recommendInq.setContent(emailRecommend.getContent());
					recommendInq.setComId(user.getComId());
					recommendInq.setUserId(user.getUserId());
					recommendInq.setRecommend(true);
					mailList.add(emailRecommend);
					inquiryList.add(recommendInq);
				}
			}
		}

	}

	/**
	 * �������鶩�Ķ��󣬷���null��ʾ������
	 * @param inquiry
	 * @return
	 */
	private TradeAlert getTradeAlert(Inquiry inquiry) {
		TradeAlert tradeAlert = null;
		if (inquiry.isTradeAlert() && StringUtil.isNotEmpty(inquiry.getTradeAlertKeyword())) {
			tradeAlert = new TradeAlert();
			tradeAlert.setEnable(true);
			tradeAlert.setCreateTime(inquiry.getCreateTime());
			tradeAlert.setKeyword(inquiry.getTradeAlertKeyword());
			if (StringUtil.equalsIgnoreCase(inquiry.getTradeAlertInfoType(), "company")) {
				tradeAlert.setCompany(true);
				tradeAlert.setProduct(false);
				tradeAlert.setSell(false);
				tradeAlert.setBuy(false);
			} else if (StringUtil.equalsIgnoreCase(inquiry.getTradeAlertInfoType(), "product")) {
				tradeAlert.setCompany(false);
				tradeAlert.setProduct(true);
				tradeAlert.setSell(false);
				tradeAlert.setBuy(false);
			} else if (StringUtil.equalsIgnoreCase(inquiry.getTradeAlertInfoType(), "buy")) {
				tradeAlert.setCompany(false);
				tradeAlert.setProduct(false);
				tradeAlert.setSell(false);
				tradeAlert.setBuy(true);
			}
		}
		return tradeAlert;
	}

	/**
	 * ����ѯ���б���ʼ��б�
	 * @param inquiry
	 * @param basketItemList
	 * @return
	 */
	private Map<String, Object> getInquiryList(Inquiry inquiry2, List<BasketItem> basketItemList) {
		// 1.����Ա���գ����е�ѯ�̶��ɹ���Աͳһ���գ�Ĭ��ѡ�
		// 2.���ʺŽ��գ���ѡ��������3�����
		// 2.1 ���ѯ�̵������а���������˺ŵĲ�Ʒ����ô��Щ���˺Ŷ����յ�һ��������ѯ�̡�
		// 2.2 ���ѯ�̵�������ֻ��һ�����˺ŵĲ�Ʒ����ôֻ��������˺����յ�ѯ�̡�
		// 2.3 ���ѯ�̵�������һ�����˺ŵĲ�Ʒ��û�У���ô���ɹ���Ա���ա�
		// 3.ͬʱ���գ����˺Ž������ͬ�ϣ�����Ա��ͬʱ�յ�һ��������ѯ�̡�
		Map<String, Object> result = new HashMap<String, Object>(2);
		List<Mail> mailList = new ArrayList<Mail>();
		List<Inquiry> inquiryList = new ArrayList<Inquiry>();
		Mail mail = null;
		int type = 0;
		for (BasketItem basketItem : basketItemList) {
			if (basketItem.getMemberType() == CN.FREE_SITE) {// ���������û�ֻ�й���Ա�˺Ž���ѯ��
				type = CN.INQUIRY_ACCEPTER_ADMIN;
			} else {
				type = basketItem.getInquiryReceive();
			}
			Inquiry inquiryCopy = new Inquiry();
			try {
				PropertyUtils.copyProperties(inquiryCopy, inquiry2);
			} catch (Exception e) {
				continue;
			}
			switch (type) {
				case CN.INQUIRY_ACCEPTER_ADMIN:
					inquiryCopy.setToEmail(basketItem.getEmail());
					inquiryCopy.setToName(basketItem.getContact());
					inquiryCopy.setUserId(basketItem.getUserId());
					inquiryCopy.setComId(basketItem.getComId());

					mail = MailFactory.getInquiry(inquiryCopy, basketItem);
					inquiryCopy.setContent(mail.getContent());
					inquiryList.add(inquiryCopy);
					mailList.add(mail);
					break;
				case CN.INQUIRY_ACCEPTER_USER:
					String[] tmpUser = this.getEmails(basketItem, type);
					if (!StringUtil.isEmpty(tmpUser[0])) {
						inquiryCopy.setToEmail(tmpUser[0]);
						inquiryCopy.setToName(tmpUser[2]);

						mail = MailFactory.getInquiry(inquiryCopy, basketItem);
						inquiryCopy.setContent(mail.getContent());
						inquiryList.addAll(this.getInquiryList(inquiryCopy, basketItem, type));

						mail.setCarbonCopyTo(tmpUser[1]);
						mailList.add(mail);
					}
					break;
				case CN.INQUIRY_ACCEPTER_BOTH:
					String[] tmpBoth = this.getEmails(basketItem, type);
					if (!StringUtil.isEmpty(tmpBoth[0])) {
						inquiryCopy.setToEmail(tmpBoth[0]);
						inquiryCopy.setToName(tmpBoth[2]);

						mail = MailFactory.getInquiry(inquiryCopy, basketItem);
						inquiryCopy.setContent(mail.getContent());
						inquiryList.addAll(this.getInquiryList(inquiryCopy, basketItem, type));

						mail.setCarbonCopyTo(tmpBoth[1]);
						mailList.add(mail);
					}
					break;
				default:
					break;
			}
		}
		result.put("mailList", mailList);
		result.put("inquiryList", inquiryList);
		return result;
	}

	/**
	 * @param inquiry
	 * @param basketItem
	 * @param type
	 * @return
	 */
	private List<Inquiry> getInquiryList(Inquiry inquiry, BasketItem basketItem, int type) {
		List<Inquiry> inquiryList = new ArrayList<Inquiry>();
		Map<Integer, Integer> userIdMap = new LinkedHashMap<Integer, Integer>();
		if (type == CN.INQUIRY_ACCEPTER_BOTH) {
			userIdMap.put(basketItem.getUserId(), basketItem.getUserId());
		}
		for (Product p : basketItem.getProductList()) {
			userIdMap.put(p.getUserId(), p.getUserId());
		}
		for (Trade t : basketItem.getTradeList()) {
			userIdMap.put(t.getUserId(), t.getUserId());
		}
		if (userIdMap.size() == 0) {
			userIdMap.put(basketItem.getUserId(), basketItem.getUserId());
		}
		Iterator<Integer> its = userIdMap.values().iterator();
		while (its.hasNext()) {
			Inquiry inquiryCopy = new Inquiry();
			try {
				PropertyUtils.copyProperties(inquiryCopy, inquiry);
			} catch (Exception e) {
				continue;
			}
			int userId = its.next();
			inquiryCopy.setComId(basketItem.getComId());
			inquiryCopy.setUserId(userId);
			inquiryList.add(inquiryCopy);
		}
		return inquiryList;
	}

	/**
	 * ���ؽ�������ѡ�ʼ��������ʼ��ͽ���������
	 * @param basketItem
	 * @param type ���˺Ž���/ͬʱ����
	 * @return String[] String[0]=��ѡ�ʼ�;String[1]=�����ʼ�;String[2]=����������;
	 */
	private String[] getEmails(BasketItem basketItem, int type) {
		String[] tmp = new String[3];
		Map<Integer, String> toEmail = new LinkedHashMap<Integer, String>(10);
		Map<Integer, String> toName = new LinkedHashMap<Integer, String>(10);
		for (BasketItem.Product product : basketItem.getProductList()) {
			toEmail.put(product.getUserId(), product.getEmail());
			toName.put(product.getUserId(), product.getContact());
		}
		for (BasketItem.Trade trade : basketItem.getTradeList()) {
			toEmail.put(trade.getUserId(), trade.getEmail());
			toName.put(trade.getUserId(), trade.getContact());
		}
		if (toEmail.size() > 0) {
			if (type == CN.INQUIRY_ACCEPTER_USER) {// 2�����˺Ž���
				Iterator<Integer> its = toEmail.keySet().iterator();
				if (its.hasNext()) {
					tmp[0] = toEmail.get(its.next());// �ʼ��е���ѡemailΪ��һ����Ʒ�����������˺ŵ�
					its.remove();// �Ƴ���ѡemail
				}
			} else if (type == CN.INQUIRY_ACCEPTER_BOTH) {// 3��ͬʱ����
				tmp[0] = basketItem.getEmail();// �ʼ��е���ѡemail�ǹ���Ա��
				toEmail.remove(basketItem.getUserId());// �Ƴ���ѡemail
			}
			Iterator<String> itEmail = toEmail.values().iterator();
			Iterator<String> itName = toName.values().iterator();
			StringBuffer emails = new StringBuffer();
			StringBuffer names = new StringBuffer();
			while (itEmail.hasNext()) {
				emails.append(itEmail.next()).append(",");// ����email
			}
			while (itName.hasNext()) {
				names.append(itName.next()).append(",");
			}
			tmp[1] = StringUtil.trimComma(emails.toString());
			tmp[2] = StringUtil.trimComma(names.toString());

		} else if (toEmail.size() == 0) {// ֻ�Թ�˾ѯ��
			tmp[0] = basketItem.getEmail();
			tmp[1] = "";
			tmp[2] = basketItem.getContact();
		}
		return tmp;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setInquiryDAO(InquiryDAO inquiryDAO) {
		this.inquiryDAO = inquiryDAO;
	}

	public void setTradeAlertService(TradeAlertService tradeAlertService) {
		this.tradeAlertService = tradeAlertService;
	}

	public void setRegisterService(RegisterService registerService) {
		this.registerService = registerService;
	}

 
}
