/* 
 * Created by linliuwei at 2009-10-29 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.ad.AdAction;
import com.hisupplier.cn.account.entity.AdOrder;
import com.hisupplier.cn.account.entity.Inquiry;
import com.hisupplier.cn.account.entity.InquiryReply;
import com.hisupplier.cn.account.entity.ServiceMail;
import com.hisupplier.cn.account.entity.TopOrder;
import com.hisupplier.cn.account.entity.UpgradeMail;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.basket.BasketItem;
import com.hisupplier.commons.mail.Mail;
import com.hisupplier.commons.mail.MailTemplate;
import com.hisupplier.commons.util.CategoryUtil;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.WebUtil;

/**
 * <pre>
 * ÿ���ʼ�ģ�嶼����������
 * ����ʱ��		${date} 
 * ��վ·��		${b2bBasePath} 		
 * �û���̨·��	${accountBasePath} �磺http://accountcn.jiaming.com
 * </pre>
 * @author linliuwei
 */
public class MailFactory {
	public static final String SERVICE_EMAIL = "service@hisupplier.com";
	public static final String FROM_NAME = "������";

	public static Map<String, Object> getCommonMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("date", new DateUtil().getDateTime());
		map.put("b2bBasePath", Config.getString("sys.base"));
		map.put("accountBasePath", Config.getString("account.base"));
		return map;
	}

	/**
	 * ע��ɹ��ʼ�

	 * @param params
	 * <pre>
	 * String welcome ��ϵ����
	 * String service_email ��������
	 * String memberId ��Ա�ʺ�		
	 * String contact ��ϵ������		
	 * String email
	 * String passwd
	 * </pre>
	 * @return
	 */
	public static Mail getRegister(Map<String, Object> params) {
		Map<String, Object> map = getCommonMap();
		map.putAll(params);
		map.put("service_email", SERVICE_EMAIL);
		Mail mail = new Mail();
		mail.setToName((String) map.get("contact"));
		mail.setToEmail((String) map.get("email"));
		mail.setReplyTo(SERVICE_EMAIL);
		mail.setFromName(FROM_NAME);
		mail.setSubject("��ӭ���뺣����");
		mail.setContent(MailTemplate.toString("register.ftl", map));
		return mail;
	}

	/**
	 * ������������ȡ���ʼ�

	 * @param params
	 * <pre>
	 * String service_contact ��ϵ����
	 * String service_email ��������
	 * String memberId ��Ա�ʺ�		
	 * String contact ��ϵ������		
	 * String email
	 * String passwd
	 * </pre>
	 * @return
	 */
	public static Mail getForgetPasswd(Map<String, Object> params) {
		Map<String, Object> map = getCommonMap();
		map.putAll(params);
		map.put("service_email", SERVICE_EMAIL);
		Mail mail = new Mail();
		mail.setToName((String) map.get("contact"));
		mail.setToEmail((String) map.get("email"));
		mail.setReplyTo(SERVICE_EMAIL);
		mail.setFromName(FROM_NAME);
		mail.setSubject("������ - �һ�����");
		mail.setContent(MailTemplate.toString("forget_passwd.ftl", map));
		return mail;
	}

	/**
	 * ���ؿͻ������ʼ�
	 * @param serviceMail
	 * @return
	 */
	public static Mail getServiceMail(ServiceMail serviceMail) {
		Map<String, Object> map = getCommonMap();
		map.put("serviceMail", serviceMail);
		Mail mail = new Mail();
		mail.setToName(serviceMail.getToName());
		mail.setToEmail(serviceMail.getToEmail());
		mail.setReplyTo(serviceMail.getEmail());
		mail.setFromName(serviceMail.getContact());
		mail.setSubject("��Ա" + serviceMail.getContact() + "��Ҫ" + serviceMail.getReason());
		mail.setContent(MailTemplate.toString("service_mail.ftl", map));
		return mail;
	}

	/**
	 * ������ϵ�����ʼ�
	 * @param serviceMail
	 * @return
	 */
	public static Mail getContactUsMail(ServiceMail serviceMail) {
		Map<String, Object> map = getCommonMap();
		map.put("serviceMail", serviceMail);
		Mail mail = new Mail();
		mail.setToName("������������Ա");
		if (serviceMail.getReason() == "չ�����") {
			mail.setToEmail("fair5@hi.cc");
		}else if(serviceMail.getReason() == "��ѵ���"){
			mail.setToEmail("Channel6@hi.cc");
		} 
		else {
			mail.setToEmail("service@hisupplier.com");
		}
		mail.setReplyTo(serviceMail.getEmail());
		mail.setFromName(serviceMail.getContact());
		mail.setSubject(serviceMail.getReason());
		mail.setContent(MailTemplate.toString("contact_us.ftl", map));
		return mail;
	}

	public static Mail getInquiryRely(InquiryReply inquiryReply) {
		Map<String, Object> map = getCommonMap();
		map.put("inquiryReply", inquiryReply);
		String[] tmps = {};
		if (StringUtil.isNotEmpty(inquiryReply.getFilePath())) {
			tmps = inquiryReply.getFilePath().split(",");
			for (int i = 0; i < tmps.length; i++) {
				tmps[i] = WebUtil.getFilePath(tmps[i]);
			}
		}
		map.put("inquiryReplyFilePaths", tmps);
		Mail mail = new Mail();
		mail.setToName(inquiryReply.getToName());
		mail.setToEmail(inquiryReply.getToEmail());
		mail.setReplyTo(inquiryReply.getFromEmail());
		mail.setFromName(inquiryReply.getFromName());
		mail.setSubject(inquiryReply.getSubject());
		mail.setContent(MailTemplate.toString("inquiry_reply.ftl", map));
		return mail;
	}

	/**
	 * ��������ѯ���ʼ�
	 * @param inquiry
	 * @param basketItem
	 * @return
	 */
	public static Mail getInquiry(Inquiry inquiry, BasketItem basketItem) {
		Map<String, Object> map = getCommonMap();
		map.put("fromSite", "http://" + basketItem.getMemberId() + ".cn." + Config.getString("sys.domain"));
		map.put("inquiry", inquiry);
		map.put("inquiryContent", StringUtil.formatText(inquiry.getContent()));
		String[] tmps = {};
		if (StringUtil.isNotEmpty(inquiry.getFilePath())) {
			tmps = inquiry.getFilePath().split(",");
			for (int i = 0; i < tmps.length; i++) {
				tmps[i] = WebUtil.getFilePath(tmps[i]);
			}
		}
		map.put("inquiryFilePaths", tmps);
		map.put("basketItem", basketItem);

		//String[] fromWebsites = inquiry.getFromWebsite().split(",");
		List<String> fromWebsites = StringUtil.toArrayList(inquiry.getFromWebsite(), ",");
		map.put("inquiryFromWebsites", fromWebsites);

		Mail mail = new Mail();
		mail.setToEmail(inquiry.getToEmail());
		mail.setToName(inquiry.getToName());
		mail.setReplyTo(inquiry.getFromEmail());
		mail.setFromName(FROM_NAME);
		mail.setSubject(inquiry.getSubject());
		mail.setContent(MailTemplate.toString("inquiry.ftl", map));
		return mail;
	}

	/**
	 * ����ѯ���Ƽ��ʼ�
	 * @param inquiry
	 * @param company
	 * @return
	 */
	public static Mail getInquiryRecommend(Inquiry inquiry, User user) {
		Map<String, Object> map = getCommonMap();

		String domain = Config.getString("sys.domain");
		StringBuffer catName = new StringBuffer();
		String[] catId = StringUtil.split(user.getCatIds(), ",");
		if (catId != null) {
			for (String id : catId) {
				int categoryId = 0;
				try {
					categoryId = Integer.parseInt(id);
				} catch (NumberFormatException e) {
					continue;
				}
				catName.append(CategoryUtil.getNamePath(categoryId, ">>")).append(",");
			}
		}
		map.put("fromSite", "http://" + user.getMemberId() + ".cn." + domain);
		map.put("inquiry", inquiry);
		map.put("catName", StringUtil.trimComma(catName.toString()));
		List<String> fromWebsites = StringUtil.toArrayList(inquiry.getFromWebsite(), ",");
		map.put("inquiryFromWebsites", fromWebsites);

		Mail mail = new Mail();
		mail.setToEmail(inquiry.getToEmail());
		mail.setToName(inquiry.getToName());
		mail.setReplyTo(inquiry.getFromEmail());
		mail.setFromName(inquiry.getFromName());
		mail.setSubject(inquiry.getSubject());
		mail.setContent(MailTemplate.toString("inquiry_recommend.ftl", map));
		return mail;
	}

	/**
	 * ��Ա����
	 * @param upgradeMail
	 * @return
	 */
	public static Mail getUpgrade(UpgradeMail upgradeMail) {
		Map<String, Object> map = getCommonMap();
		switch (upgradeMail.getUpType()) {
			case 1: case 2: case 3:
				upgradeMail.setUpTypeName(TextUtil.getText(
						AdAction.class, 
						"upgrade.upType" + upgradeMail.getUpType(), 
						"zh"));
				break;
		}
		map.put("upgradeMail", upgradeMail);
		Mail mail = new Mail();
		mail.setToName("������������Ա");
		mail.setToEmail("service@hisupplier.com");
		mail.setReplyTo(upgradeMail.getEmail());
		mail.setFromName(upgradeMail.getContact());
		mail.setSubject("��Ա" + upgradeMail.getContact() + "��Ҫ����");
		mail.setContent(MailTemplate.toString("upgrade.ftl", map));
		return mail;
	}

	/**
	 * �������
	 * @param adOrder
	 * @return
	 */
	public static Mail getAdOrder(AdOrder adOrder) {
		Map<String, Object> map = getCommonMap();
		map.put("adOrder", adOrder);
		Mail mail = new Mail();
		mail.setToName("������������Ա");
		mail.setToEmail("service@hisupplier.com");
		mail.setReplyTo(adOrder.getEmail());
		mail.setFromName(adOrder.getContact());
		mail.setSubject("��Ա" + adOrder.getContact() + "��Ҫ" + adOrder.getCatName());
		mail.setContent(MailTemplate.toString("adOrder.ftl", map));
		return mail;
	}

	public static Mail getTopOrder(TopOrder topOrder) {
		Map<String, Object> map = getCommonMap();

		map.put("topOrder", topOrder);
		Mail mail = new Mail();
		mail.setToName("������������Ա");
		mail.setToEmail("service@hisupplier.com");
		mail.setReplyTo(topOrder.getEmail());
		mail.setFromName(topOrder.getContact());
		mail.setSubject("��Ա"+topOrder.getContact()+"��Ҫ����Topsite�ؼ���");
		mail.setContent(MailTemplate.toString("topOrder.ftl", map));
		return mail;
	}
	public static Mail getExporterApply(Map<String, Object> map){
		map.put("date", new DateUtil().getDateTime());
		map.put("b2bBasePath", Config.getString("sys.base"));
		map.put("accountBasePath", Config.getString("account.base"));
		Mail mail = new Mail();
		mail.setToEmail(SERVICE_EMAIL);
		mail.setFromName((String)map.get("contact"));
		mail.setReplyTo((String)map.get("email"));
		mail.setSubject("���뺣��������վ��Ʒ����");
		mail.setContent(MailTemplate.toString("product_apply.ftl", map));
		return mail;
	}
}
