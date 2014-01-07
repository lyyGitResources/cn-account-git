package com.hisupplier.cn.account.basic;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.hisupplier.about.cn.About;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.AdminLog;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.ServiceMail;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.entity.UserLog;
import com.hisupplier.cn.account.inquiry.InquiryDAO;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.member.UserDAO;
import com.hisupplier.cn.account.misc.CommentDAO;
import com.hisupplier.cn.account.misc.VoteDAO;
import com.hisupplier.cn.account.product.TradeDAO;
import com.hisupplier.cn.account.util.QrCode;
import com.hisupplier.cn.account.website.WebsiteDAO;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.entity.Attachment;
import com.hisupplier.commons.entity.cn.Service;
import com.hisupplier.commons.entity.cn.UserSuggest;
import com.hisupplier.commons.entity.cn.WebSite;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.PasswordCheck;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;

public class BasicService {

	private CompanyDAO companyDAO;
	private UserDAO userDAO;
	private UserLogDAO userLogDAO;
	private ServiceDAO serviceDAO;
	private UserSuggestDAO userSuggestDAO;
	private AdminLogDAO adminLogDAO;
	private InquiryDAO inquiryDAO;
	private BulletinDAO bulletinDAO;
	private VoteDAO voteDAO;
	private CommentDAO commentDAO;
	private TradeDAO tradeDAO;
	private WebsiteDAO websiteDAO;
	private final static Log log = LogFactory.getLog(BasicService.class);
	
	public void setCommentDAO(CommentDAO commentDAO) {
		this.commentDAO = commentDAO;
	}

	/**
	 * ������ҳ��Ϣ
	 * @param params
	 * <pre>
	 * comId  
	 * userId
	 * </pre>
	 * @param request 
	 * @return
	 * <pre>
	 * videoCount	��Ƶʣ������
	 * productCount	��Ʒʣ������
	 * optimizeProCount	�Ż���Ʒʣ������
	 * newProCount	���ܲ�Ʒʣ������
	 * featureProCount	��ҳչ̨��Ʒʣ������
	 * tradeCount	����ʣ������
	 * menuCount	�Զ���˵�ʣ������
	 * </pre>
	 * <pre>
	 * memberType	��Ա�ȼ�
	 * availabilityDays	��Ч����
	 * regDate	ע������
	 * jionYear	�������
	 * loginTimes	��¼����
	 * preLoginTime �ϴε�¼ʱ��
	 * preLoginIP	�ϴε�¼��ַ
	 * userLogList	�û���־�б�
	 * company		��˾
	 * </pre>
	 */
	public Map<String, Object> getHome(QueryParams params, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		int comId = params.getLoginUser().getComId();
		int userId = params.getLoginUser().getUserId();
		Company company = companyDAO.findCompanyMemberType(comId);
		if (company == null) {
			throw new PageNotFoundException();
		}
		User user = userDAO.findUser(userId, comId);
		if (user == null) {
			throw new PageNotFoundException();
		}
		// TODO ����ǿ�� 2012-08-10
		if (StringUtil.isNotEmpty(request.getParameter("ticket")) 
				&& PasswordCheck.check(user.getPasswd()) == 1) {
			result.put("msg", "password");
			return result;
		}

		//ʣ������
		int videoCount = company.getVideoMax() - company.getVideoCount();
		int productCount = company.getProductMax() - company.getProductCount();
		int optimizeProCount = company.getOptimizeProMax() - company.getOptimizeProCount();
		int newProCount = company.getNewProMax() - company.getNewProCount();
		int featureProCount = company.getFeatureProMax() - company.getFeatureProCount();
		int tradeCount = company.getTradeMax() - company.getTradeCount();
		int menuCount = company.getMenuGroupMax() - company.getMenuGroupCount() + 2;

		//����ʣ����Ч�ڣ�������
		long availabilityDays = -1;
		String goldExpiry = company.getGoldEndDate();
		if (company.getMemberType() == CN.GOLD_SITE && StringUtil.isNotEmpty(goldExpiry)) {
			availabilityDays = new DateUtil().getDaysFromNow(DateUtil.parseDate(goldExpiry));
		}
		if (availabilityDays < 0) {
			availabilityDays = 0;
		}

		//����������ޣ�����1��Ҳ��ʾ1��
		int jionYear = 1;
		String jionDate = DateUtil.formatDate(company.getCreateTime());
		String currentDate = new DateUtil().getDate();
		jionYear = Integer.parseInt(currentDate.substring(0, 4)) - Integer.parseInt(jionDate.substring(0, 4));
		if (Integer.parseInt(StringUtil.remove(jionDate, '-').substring(4)) < Integer.parseInt(StringUtil.remove(currentDate, '-').substring(4))) {
			jionYear += 1;//��ǰ�����մ���ע������գ�������޼�1��
		}

		//��ά��·������
		buildQRCodePath(params);
		
		About about = new About();
		result.put("news", about.getNews(5));
		result.put("exhii", about.getExhii(2));
		// ��ȡ���̲���
		result.put("videoCount", videoCount);
		result.put("productCount", productCount);
		result.put("optimizeProCount", optimizeProCount);
		result.put("newProCount", newProCount);
		result.put("featureProCount", featureProCount);
		result.put("tradeCount", tradeCount);
		result.put("menuCount", menuCount);
		result.put("tradeDatedCount", tradeDAO.findTradeDatedCount(comId));

		result.put("memberType", company.getMemberType());
		result.put("availabilityDays", availabilityDays);
		result.put("regDate", DateUtil.formatDate(company.getCreateTime()));
		result.put("jionYear", jionYear);
		result.put("loginTimes", user.getLoginTimes());
		result.put("preLoginTime", DateUtil.formatDateTime(user.getPreLoginTime()));
		result.put("preLoginIP", user.getPreLoginIP());
		result.put("userLogList", userLogDAO.findUserLogList(params));
		result.put("bulletinList", bulletinDAO.findBulletinList(params));
		result.put("company", company);
		result.put("vote", voteDAO.findVoteLog());
		return result;
	}

	/**
	 * ���ؿͷ��б�
	 * @param params
	 * <pre>
	 * comId
	 * </pre>
	 * @return
	 * <pre>
	 * serviceList	�ͷ��б�
	 * tqIds		�ͷ���ԱTQid
	 * </pre>
	 */
	public Map<String, Object> getServiceList(QueryParams params) {
		List<Service> serviceList = serviceDAO.findCompanyServiceList(params.getLoginUser().getComId());
		if (serviceList.size() <= 0) {
			serviceList = serviceDAO.findServiceList(CN.FREE_SITE);
		}
		String tqIds = "";
		int i = 0;
		for (Service service : serviceList) {
			if (i == serviceList.size() - 1) {
				tqIds = service.getTq();
			} else {
				tqIds += service.getTq() + "|";
			}
			i++;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("serviceList", serviceList);
		result.put("tqIds", tqIds);
		return result;
	}

	/**
	 * ���ؿͷ�����
	 * @param params
	 * <pre>
	 * comId
	 * userId
	 * </pre>
	 * @return 
	 * <pre>
	 *   serviceList	�ͷ��б�
	 *   serviceMail	�ͷ��ʼ����Ͷ���
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getServiceMail(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int userId = params.getLoginUser().getUserId();
		Company company = companyDAO.findCompany(comId);
		if (company == null) {
			throw new PageNotFoundException();
		}
		User user = userDAO.findUser(userId, comId);
		if (user == null) {
			throw new PageNotFoundException();
		}
		Map<String, Object> result = this.getServiceList(params);
		List<Service> serviceList = (List<Service>) result.get("serviceList");
		ServiceMail serviceMail = new ServiceMail();
		serviceMail.setComName(company.getComName());
		serviceMail.setContact(user.getContact());
		serviceMail.setEmail(user.getEmail());
		serviceMail.setTel(user.getTel());
		serviceMail.setFax(user.getFax());
		serviceMail.setMemberId(company.getMemberId());
		serviceMail.setMobile(user.getMobile());
		serviceMail.setToEmail(serviceList.get(0).getEmail());
		serviceMail.setToName(serviceList.get(0).getContact());
		result.put("serviceMail", serviceMail);
		return result;
	}

	/**
	 * �����û��������
	 * @param params
	 * <pre>
	 * comId
	 * userId
	 * </pre>
	 * @return
	 * <pre>
	 *listResult �û���������б�
	 *comId		��˾Id
	 * </pre>
	 */
	public Map<String, Object> getUserSuggestList(QueryParams params) {
		ListResult<UserSuggest> suggestList = userSuggestDAO.findUserSuggestList(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", suggestList);
		result.put("comId", params.getLoginUser().getComId());
		return result;
	}

	/**
	 * ����û��������
	 * @param userSuggest
	 * @return
	 * <pre>
	 * addSuccess	��ӳɹ�
	 * operateFail	����ʧ��
	 * </pre>
	 */
	public String addUserSuggest(UserSuggest userSuggest) {
		userSuggest.setCreateTime(new DateUtil().getDateTime());
		if (userSuggestDAO.addUserSugggest(userSuggest) > 0) {
			return "addSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * ���ز�����־
	 * @param params
	 * @return
	 * <pre>
	 * listResult	�û���־�б�
	 * userList		�����ʺ�
	 * </pre>
	 */
	public Map<String, Object> getUserLogList(QueryParams params) {
		params.setPaging(true);
		ListResult<UserLog> userLogList = userLogDAO.findUserLogList(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", userLogList);

		// ���ƻ�Ա���Ҵ������ʺ�
		Company company = this.companyDAO.findCompany(params.getLoginUser().getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}
		if (company.getMemberType() == CN.GOLD_SITE && company.getUserCount() > 0 && params.getLoginUser().isAdmin()) {
			result.put("userList", this.userDAO.findUserList(params.getLoginUser().getComId()));
		}
		return result;
	}

	/**
	 * �������δͨ��ԭ��
	 * @param params
	 * @return
	 * <pre>
	 * adminLog �����־
	 * </pre>
	 */
	public Map<String, Object> getAdminLog(QueryParams params) {
		AdminLog adminLog = (AdminLog) adminLogDAO.findAdminLog(params.getTableId(), params.getTableName());
		if (adminLog == null) {
			adminLog = new AdminLog();
			adminLog.setRemark("");
			adminLog.setCreateTime(new DateUtil().getDateTime());
		} else {
			adminLog.setCreateTime(DateUtil.formatDateTime(adminLog.getCreateTime()));
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("adminLog", adminLog);
		return result;
	}

	/**
	 * ����δ��ѯ��������ѯ�̻���վ��������Ʒ����վ�������������վ����
	 * @param params
	 * @return
	 */
	public Map<String, Object> getInquiryUnreadCount(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int userId = params.getLoginUser().getUserId();
		int unreadCount = inquiryDAO.findInquiryCount(comId, userId, 0, -1, CN.STATE_PASS);
		Company company = companyDAO.findCompany(comId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("inquiryUnreadCount", unreadCount);
		result.put("inquiryDelCount", company.getInquiryDelCount());
		result.put("productDelCount", company.getProductDelCount());
		result.put("tradeDelCount", company.getTradeDelCount());
		return result;
	}

	/**
	 * �ж��Ƿ����ɶ�ά��·��
	 * @param params
	 */
	public void buildQRCodePath(QueryParams params){
		int comId = params.getLoginUser().getComId();
		WebSite webSite = websiteDAO.findWebsite(comId);
		Company company = companyDAO.findCompany(comId);
		
		if (StringUtil.isBlank(webSite.getQrPath()) && StringUtil.isNotBlank(webSite.getDomain())) {
			try{
				UpdateMap updateMap_webSite = new UpdateMap("WebSite");
				//�����վĬ����ʾ��ά��
				updateMap_webSite.addField("isShowQR", 1);
				updateMap_webSite.addField("qrPath", qrcode(comId, "http://" + webSite.getDomain()) + "|" + webSite.getDomain());
				updateMap_webSite.addWhere("comId", comId);
				int effectRow=websiteDAO.update(updateMap_webSite);
				if (effectRow<=0) {
					throw new Exception("����website����ʧ��" + comId);
				}
			}catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		} 
		
		if (StringUtil.isBlank(company.getQrPath()) && company.getMemberType() == 2) {
			try{
				UpdateMap updateMap_companyExtra = new UpdateMap("CompanyExtra");
				updateMap_companyExtra.addField("qrPath", qrcode(comId, "http://" + company.getMemberId() + ".cn." + Config.getString("sys.domain")));
				updateMap_companyExtra.addWhere("comId", comId);
				int effectRow=companyDAO.update(updateMap_companyExtra);
				if (effectRow<=0) {
					throw new Exception("����companyExtra����ʧ��" + comId);
				}
			}catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		
	}

	/**
	 * ���ɶ�ά��QRCode���ϴ�ͼƬվ
	 * @param comId
	 * @param context ��ά��ͼƬ����
	 * @return
	 */
	public String qrcode(int comId, String context) {
		//���ɶ�ά��ͼƬ������
		QrCode qrCode = new QrCode();
		String fileName = UploadUtil.getNewFileName(new DateUtil(), "jpg");
		String imgPath = ServletActionContext.getServletContext().getRealPath("contactTmp/") + fileName;
		String filePath = null;
		File file = null;
		try {
			file = new File(imgPath);
			if (!file.exists()) {
				file.mkdir();
			}
			qrCode.createQRCode(context, imgPath);
			///�ϴ���ά��ͼƬ��ͼƬվ
			Attachment attachment = UploadUtil.uploadFileStream(comId, 0, fileName, file);
			if (attachment != null) {
				filePath = attachment.getFilePath().replace("\\", "/");
			} else {
				throw new Exception("�ϴ� QR ʧ��" + comId);
			}
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		///ɾ�������ϵĶ�ά��ͼƬ
		if (file.exists()) {
			file.delete();
		}
		return filePath;
	}

	/**
	 * ͨ�� ��ǰ��¼���û� comId  ��ѯ ���û�����������
	 * @param loginUser
	 * @return
	 */
	public int getCommentCount(LoginUser loginUser) {
		int commentCount = commentDAO.findCommentCount(loginUser.getComId());
		return commentCount;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setUserLogDAO(UserLogDAO userLogDAO) {
		this.userLogDAO = userLogDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setServiceDAO(ServiceDAO serviceDAO) {
		this.serviceDAO = serviceDAO;
	}

	public void setAdminLogDAO(AdminLogDAO adminLogDAO) {
		this.adminLogDAO = adminLogDAO;
	}

	public void setUserSuggestDAO(UserSuggestDAO userSuggestDAO) {
		this.userSuggestDAO = userSuggestDAO;
	}

	public void setInquiryDAO(InquiryDAO inquiryDAO) {
		this.inquiryDAO = inquiryDAO;
	}

	public void setBulletinDAO(BulletinDAO bulletinDAO) {
		this.bulletinDAO = bulletinDAO;
	}

	public void setVoteDAO(VoteDAO voteDAO) {
		this.voteDAO = voteDAO;
	}

	public void setTradeDAO(TradeDAO tradeDAO) {
		this.tradeDAO = tradeDAO;
	}

	public void setWebsiteDAO(WebsiteDAO websiteDAO) {
		this.websiteDAO = websiteDAO;
	}
}
