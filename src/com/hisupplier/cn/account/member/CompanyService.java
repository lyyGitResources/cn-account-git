package com.hisupplier.cn.account.member;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.hisupplier.cas.CASClient;
import com.hisupplier.cas.Ticket;
import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.CompanyProfile;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.cn.account.entity.Menu;
import com.hisupplier.cn.account.entity.Register;
import com.hisupplier.cn.account.entity.Talk;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.entity.UserLog;
import com.hisupplier.cn.account.entity.Video;
import com.hisupplier.cn.account.menu.MenuDAO;
import com.hisupplier.cn.account.misc.VideoDAO;
import com.hisupplier.cn.account.util.MailFactory;
import com.hisupplier.cn.account.util.UserLogUtil;
import com.hisupplier.cn.account.webservice.RegisterService;
import com.hisupplier.cn.account.website.WebsiteDAO;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.Global;
import com.hisupplier.commons.entity.Attachment;
import com.hisupplier.commons.entity.cn.WebSite;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.mail.MailSender;
import com.hisupplier.commons.mail.MailSenderCNFactory;
import com.hisupplier.commons.task.TaskExecutor;
import com.hisupplier.commons.tq.CNTQWebService;
import com.hisupplier.commons.util.CategoryUtil;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.PasswordCheck;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;
import com.hisupplier.commons.util.Validator;

@WebService(endpointInterface = "com.hisupplier.cn.account.webservice.RegisterService")
public class CompanyService implements RegisterService {

	private static Pattern pattern_memberId = Pattern.compile("\\d{17}");
	private static Pattern number_pattern = Pattern.compile("^\\d{1}$");
	
	private CompanyDAO companyDAO;
	private UserDAO userDAO;
	private VideoDAO videoDAO;
	private MenuDAO menuDAO;
	private WebsiteDAO websiteDAO;
	private TalkService talkService;
	
	private static ConcurrentMap<String, Forget> forgetCache = new ConcurrentHashMap<String, Forget>(100);

	class Forget {
		public Forget(long time, int count) {
			this.time = time;
			this.count = count;
		}

		public long time;
		public int count;
	}

	/**
	 * <p>����ԱID���Ѵ��ڷ���used�����򷵻�unused
	 * @param params
	 * <pre>
	 *   comId
	 *   memberId
	 * </pre>
	 * @return
	 * <pre>
	 *   used
	 *   unused
	 * </pre>
	 */
	public String checkMemberId(QueryParams params) {
		int comId = params.getLoginUser() == null ? -1 : params.getLoginUser().getComId();
		return this.companyDAO.findMemberId(comId, params.getMemberId()) > 0 ? "used" : "unused";
	}

	/**
	 * <p>��⹫˾���ƣ��Ѵ��ڷ���used�����򷵻�unused
	 * @param params
	 * <pre>
	 *   comId
	 *   comName
	 * </pre>
	 * @return
	 * <pre>
	 *   used
	 *   unused
	 * </pre>
	 */
	public String checkComName(QueryParams params) {
		int comId = params.getLoginUser() == null ? -1 : params.getLoginUser().getComId();
		return this.companyDAO.findComName(comId, params.getComName()) > 0 ? "used" : "unused";
	}

	/**
	 * <p>������䣬�Ѵ��ڷ���used�����򷵻�unused
	 * @param params
	 * <pre>
	 *   email
	 *   userId
	 * </pre>
	 * @return 
	 * <pre>
	 *   used
	 *   unused
	 * </pre>
	 */
	public String checkEmail(QueryParams params) {
		return this.userDAO.findEmail(params.getUserId(), params.getEmail()) > 0 ? "used" : "unused";
	}
	
	/**
	 * ��������Ƿ���ȷ
	 * @param request
	 * @param oldPasswd ���������
	 * @return Boolean
	 */
	public boolean checkUserPassword(HttpServletRequest request, String oldPasswd) {
		Ticket loginUser = CASClient.getInstance().getUser(request);
		boolean result = false;
		if (loginUser != null) {
			result = this.userDAO.checkPassword(loginUser.getUserId(), oldPasswd);
		}
		return result;
	}
	
	/**
	 * ��Ա��Ϣ�޸�ʱ��֤��ʱ����Ĳ�ѯ��˾��Ϣ
	 * @param comId
	 * @return
	 */
	public Company getValidateCompany(int comId) {
		return companyDAO.findCompany(comId);
	}
	/**
	 * ��Ա��Ϣ�޸�ʱ��֤��ʱ����Ĳ�ѯ�û���Ϣ
	 * @param comId
	 * @return
	 */
	public User getValidateUser(String memberId) {
		return userDAO.findUserByMemberId(memberId);
	}
	/**
	 * <p>��ѯ��˾�޸���Ҫ������
	 * @param comId
	 * @return company
	 */
	public Map<String, Object> getCompanyEdit(int comId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Company company = this.companyDAO.findCompanyMemberType(comId);
		if (company == null) {
			throw new PageNotFoundException();
		}
		String province = "";
		province = userDAO.findProvince(comId);
		CompanyProfile companyProfile = this.companyDAO.findCompanyProfile(company.getComId());
		if(companyProfile != null){
			company.setRegImgPath(companyProfile.getRegImgPath());
			company.setRegImgType(companyProfile.getRegImgType());
			company.setRegImgPath2(companyProfile.getRegImgPath2());
			
			company.setRegNo(companyProfile.getRegNo());
			company.setCeo(companyProfile.getCeo());
			if (companyProfile.getRegImgType() == 1) {
				if (province.equals("103103")) {// �㽭ʡ
					company.setCheckRegNo(true);
				}
			}
		} else {
			company.setRegImgType(1);
		}
		
		if (company.getState() == CN.STATE_REJECT || StringUtil.isEmpty(company.getRegNo())) {
			company.setEditRegNo(true);
		} else {
			company.setEditRegNo(false);
		}
		if (company.getState() == CN.STATE_REJECT || StringUtil.isEmpty(company.getCeo())) {
			company.setEditCeo(true);
		} else {
			company.setEditCeo(false);
		}
		
		if (pattern_memberId.matcher(company.getMemberId()).matches()) {
			company.setEditMemberId(true);
			company.setEditComName(true);
		} else {
			company.setEditMemberId(false);
			if (company.getState() == CN.STATE_REJECT) {
				company.setEditComName(true);
			} else {
				company.setEditComName(false);
			}
		}
		company.setOldCatIds(company.getCatIds());
		
		if (company.getVideoId() > 0) {
			Video video = this.videoDAO.findVideo(company.getComId(), company.getVideoId());
			if (video != null) {
				company.setVideoImgPath(video.getImgPath());
				company.setPlayPath(video.getPlayPath());
				company.setVideoState(video.getState());
			}
		}
		result.put("isImgFull", false);
		result.put("isRegImgEmpty",false);
		
		// ͼƬ����
		if (company.getImgCount() >= company.getImgMax()) {
			//ͼƬ����
			result.put("isImgFull", true);
		}
		result.put("company", company);
		result.put("province", province);
		return result;
	}

	/**
	 * <p>��ѯ��˾�޸���Ҫ������
	 * @param params
	 * <pre>
	 *   comId
	 *   videoId
	 * </pre>
	 * @return company
	 */
	public Map<String, Object> getCompanyEdit(QueryParams params) {
		if (params.getLoginUser() != null) {
			return getCompanyEdit(params.getLoginUser().getComId());
		} else {
			throw new PageNotFoundException();
		}
	}

	/**
	 * <p>��ѯ��ϵ���޸���Ҫ������
	 * @param params
	 * <pre>
	 *   userId
	 *   comId
	 * </pre>
	 * @param type 0����ϵ���޸ĵ�ʱ���ѯ��Ϣ��1��ҳ��Ҫ��ʾ�ڶ���ϵ�˵���Ϣ
	 * @return user
	 */
	public Map<String, Object> getContactEdit(QueryParams params, int type) {
		Map<String, Object> result = new HashMap<String, Object>();
		int userId = params.getLoginUser().getUserId();
		int comId = params.getLoginUser().getComId();
		
		User user = this.userDAO.findUser(userId, comId);
		if (user == null) {
			throw new PageNotFoundException();
		}
		Company company = this.companyDAO.findCompanyMemberType(comId);
		if (company == null) {
			throw new PageNotFoundException();
		}
		// TODO ��ʱ�޸�
		if (user.getTown().length() == 9) {
			user.setTown("");
		}
		
		user.setGoogleLocal(company.getGoogleLocal());
		result.put("isImgFull", false);
		// ͼƬ����
		if (company.getImgCount() >= company.getImgMax()) {
			//ͼƬ����
			result.put("isImgFull", true);
		}
		this.parseTel(user);
		this.parseFax(user);
		result.put("user", user);
		
		User more = this.userDAO.findUserByParentId(userId, comId);
		if (more == null){
			result.put("moreId", 0);
		}else{
			result.put("moreId", more.getUserId());
		}
		if(type==1){
			if (more != null) {
				this.parseTel(more);
				this.parseFax(more);
			}
			result.put("more", more);
		}
		
		// ���� Talk
		// �Ƿ���ҵQQ 
		List<Talk> talks = talkService.getTalks(user.getUserId());
		if (talks.isEmpty()) {
			talks = new ArrayList<Talk>();
			Talk t = new Talk();
			t.setName(StringUtil.isBlank(user.getQq()) ? "" : user.getContact());
			t.setCode(user.getQq());
			t.setType(Talk.QQ);
			talks.add(t);
		} else {
			if (talks.size() == 1) {
				user.setQq_type(talks.get(0).getType() == Talk.BIGQQ);
			}
		}
		user.setTalks(talks);
		return result;
	}

	/**
	 * <p>��ѯ��ϵ���޸���Ҫ������
	 * @param params
	 * <pre>
	 *   userId
	 *   comId
	 * </pre>
	 * @return user
	 */
	public Map<String, Object> getMoreContactEdit(QueryParams params) {
		User user = this.userDAO.findUserByParentId(params.getUserId(), params.getLoginUser().getComId());
		if (user != null) {
			this.parseTel(user);
			this.parseFax(user);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("user", user);
		return result;
	}

	/**
	 * <p>��ѯ�����޸���Ҫ������
	 * @param params
	 * <pre>
	 *   userId
	 *   comId
	 * </pre>
	 * @param request 
	 * @return user
	 */
	public Map<String, Object> getPasswdEdit(QueryParams params, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		// TODO ����ǿ�� 2012-08-10
		LoginUser loginUser = params.getLoginUser();
		if (StringUtil.equals("home", (String) request.getParameter("rel")) &&
				PasswordCheck.check(userDAO.findUser(loginUser.getUserId(), loginUser.getComId()).getPasswd()) == 1) {
			result.put("alert", true);
		} else {
			result.put("alert", false);
		}
		return result;
	}

	/**
	 * <p>��������
	 * @param email
	 * @param limit ���Ʒ����ʼ��Ĵ���
	 * @return
	 * <pre>
	 *   memberId
	 *   email
	 *   tip	��ʾ��Ϣ
	 * </pre> 
	 */
	public Map<String, Object> getForgetPasswd(String email, boolean limit) {

		Map<String, Object> result = new HashMap<String, Object>();
		Forget forget = null;
		// ������ʱ��Ҫ�ж��Ƿ��Ѿ���3���ˣ�������򷵻أ��������ٷ����ʼ�
		if (limit) {
			long now = new Date().getTime();
			if (forgetCache.size() >= 100) {
				forgetCache.clear();
			}
			forget = forgetCache.get(email);
			if (forget != null) {
				if (now > (forget.time + Global.UNIT_DAY)) {
					forgetCache.remove(email);
				} else {
					if (forget.count >= 3) {
						result.put("tip", "�����Ѵﵽ����ȡ���ʼ����������");
						return result;
					} else {
						forget.count = forget.count + 1;
					}
				}
			} else {
				forget = new Forget(now, 0);
				forgetCache.put(email, forget);
			}
		}

		// ����Email��ѯ�ʻ��Ļ�Ա�ʺź����� ����ݻ�Ա�ʺŲ��������Email
		User user = null;
		if (Validator.isMemberId(email).equals("ok")) {
			user = this.userDAO.findUserByMemberId(email);

		} else if (Validator.isEmail(email)) {
			user = this.userDAO.findUserByEmail(email);
		}

		if (user == null) {
			result.put("tip", "��Ա�ʺŻ����䲻����");
			return result;
		}
		if (pattern_memberId.matcher(user.getMemberId()).matches()) {
			result.put("editMemberId", "true");//����17λ����ʱ���ʼ�����"����"����
		} else {
			result.put("editMemberId", "false");
		}
		result.put("memberId", user.getMemberId());
		result.put("contact", user.getContact());
		result.put("email", user.getEmail());
		result.put("passwd", user.getPasswd());
		result.put("service_contact", CASClient.getInstance().getAutoLoginURL(Config.getString("account.base") + "/basic/service_mail.htm", user.getMemberId(), user.getPasswd()));
		result.put("tip", "success");

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(MailFactory.getForgetPasswd(result));
		TaskExecutor.execute(sender);
		return result;
	}

	/**
	 * <p>���ʺŹ���
	 * @param params
	 *   comId
	 * @return
	 *   userList
	 */
	public Map<String, Object> getUserList(QueryParams params) {
		Company company = this.companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}

		List<User> userList = this.userDAO.findUserList(params.getLoginUser().getComId());

		//�ų�����Ա�ʺ�
		Iterator<User> u = userList.iterator();
		while (u.hasNext()) {
			if (u.next().isAdmin()) {
				u.remove();
				break;
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("userList", userList);
		result.put("company", company);
		return result;
	}

	/**
	 * <p>��ѯ������ʺ���Ҫ������
	 * @param params
	 *   comId
	 *   userId
	 * @return ����null��ʾ���ʺ��ѳ������������������
	 *   adminUser
	 */
	public Map<String, Object> getUserAdd(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		Company company = this.companyDAO.findCompanyMemberType(comId);
		if (company == null) {
			throw new PageNotFoundException();
		}
		if (company.getUserCount() >= company.getUserMax()) {
			return null;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("isImgFull", false);
		// ͼƬ����
		if (company.getImgCount() >= company.getImgMax()) {
			//ͼƬ����
			result.put("isImgFull", true);
		}
		User adminUser = this.userDAO.findUser(params.getLoginUser().getUserId(), comId);
		User user = new User();
		user.setAdminStreet(adminUser.getStreet());
		user.setAdminTel(adminUser.getTel());
		user.setAdminFax(adminUser.getFax());
		user.setAdminZip(adminUser.getZip());

		result.put("user", user);
		return result;
	}

	/**
	 * <p>��ѯ�༭���ʺ���Ҫ������
	 * @param params
	 * <pre>
	 *   comId
	 *   userId
	 * </pre>
	 * @return 
	 *   user
	 */
	public Map<String, Object> getUserEdit(QueryParams params) {
		if (params.getUserId() == -1) {
			throw new PageNotFoundException();
		}
		User user = this.userDAO.findUser(params.getUserId(), params.getLoginUser().getComId());
		if (user == null) {
			throw new PageNotFoundException();
		}

		this.parseTel(user);
		this.parseFax(user);

		Company company = this.companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("isImgFull", false);
		// ͼƬ����
		if (company.getImgCount() >= company.getImgMax()) {
			//ͼƬ����
			result.put("isImgFull", true);
		}
		
		List<Talk> talks = talkService.getTalks(user.getUserId());
		if (talks.isEmpty()) {
			talks = new ArrayList<Talk>();
			Talk t = new Talk();
			t.setName(StringUtil.isBlank(user.getQq()) ? "" : user.getContact());
			t.setCode(user.getQq());
			t.setType(Talk.QQ);
			talks.add(t);
		} else {
			if (talks.size() == 1) {
				user.setQq_type(talks.get(0).getType() == Talk.BIGQQ);
			}
		}
		user.setTalks(talks);
		
		result.put("user", user);
		return result;
	}

	/**
	 * <p>������ʺ�
	 * @param user
	 * @param loginUser
	 * @return
	 * <pre>
	 *   email.used		�ʼ��ظ�
	 *   user.headImg.error	�ϴ�ͼ�����
	 *   addSuccess				
	 *   operateFail			
	 * </pre>
	 */
	public String addUser(User user, LoginUser loginUser) {
		int comId = loginUser.getComId();
		if (this.userDAO.findEmail(user.getEmail()) > 0) {
			return "email.used";
		}
		
		if (StringUtil.isNotEmpty(user.getHeadImgPath())) {
			Map<String, String> map = UploadUtil.getImgParam(user.getHeadImgPath());
			if (Boolean.parseBoolean(map.get("isUpload"))) {
				Company company = this.companyDAO.findCompanyMemberType(loginUser.getComId());
				if (company.getImgCount() < company.getImgMax()) {
					Image image = new Image();
					image.setComId(loginUser.getComId());
					image.setImgName(map.get("imgName"));
					image.setImgPath(map.get("imgPath"));
					if(!number_pattern.matcher(map.get("imgType")).matches() || !"6".equals(map.get("imgType"))){//�жϲ���һλ���־����ó�6��6Ϊ��˾��ϵ������ͼƬ
						map.put("imgType", "6");
					}
					image.setImgType(Integer.parseInt(map.get("imgType")));
					com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
					if (img.getImgId() <= 0) {
						return "user.headImg.error";
					}
					user.setHeadImgPath(img.getImgPath());
				}
			} else {
				user.setHeadImgPath(user.getHeadImgPath());
			}
		}

		DateUtil dateUtil = new DateUtil();

		user.setNotLoginAlertDate(dateUtil.getDate());
		user.setPreLoginTime(dateUtil.getDateTime());
		user.setLastLoginTime(dateUtil.getDateTime());
		user.setCreateTime(dateUtil.getDateTime());
		user.setModifyTime(dateUtil.getDateTime());
		user.setLoginTimes(1);
		user.setAdmin(false);

		int userId = 0;
		if ((userId = userDAO.addUser(user)) > 0) {
			user.setUserId(userId);
			talkService.addTalks(user, comId);
			
			UpdateMap companyMap = new UpdateMap("CompanyExtra");
			companyMap.addField("userCount", "+", 1);
			companyMap.addWhere("comId", user.getComId());
			companyDAO.update(companyMap);

			UserLog userLog = UserLogUtil.getUserLog(UserLog.MEMBER, UserLog.ADD, "������ʺ�", loginUser);
			companyDAO.addUserLog(userLog);
			return "addSuccess";
		}
		return "operateFail";
	}

	/**
	 * �޸ı�����ϵ��
	 * @param user 
	 * @return 1����ӳɹ� 2���޸ĳɹ� 0������ʧ��
	 */
	public int updateContactMore(User user) {
		DateUtil dateUtil = new DateUtil();
		user.setNotLoginAlertDate(dateUtil.getDate());  
		user.setModifyTime(dateUtil.getDateTime());
		user.setLastLoginTime(dateUtil.getDateTime());
		user.setPreLoginTime(dateUtil.getDateTime());
		user.setCreateTime(dateUtil.getDateTime());
		user.setState(CN.STATE_WAIT);
		user.setShow(true);
		user.setAdmin(false);
		if (user.getUserId() <= 0) {
			return userDAO.addUser(user) > 0 ? 1 : 0;
		} else {
			return userDAO.updateUser(user) == 1 ? 2 : 0;
		}
	}

	/**
	 * ɾ��������ϵ��
	 * @param params 
	 * @return �ɹ�ʧ��
	 */
	public boolean deleteContactMore(QueryParams params) {
		UpdateMap userMap = new UpdateMap("Users");
		userMap.addWhere("comId", params.getLoginUser().getComId());
		userMap.addWhere("userId", params.getUserId());
		userMap.addWhere("isAdmin", 0);
		return userDAO.delete(userMap) == 1;
	}
	

	/**
	 * <p>���¹�˾��Ϣ
	 * @param company
	 * @param loginUser
	 * @return
	 * <pre>
	 *   memberId.used
	 *   comName.used
	 *   company.logoCertImg.error
	 *   company.logoImg.error
	 *   company.faceImg.error
	 *   editSuccess
	 *   operateFail
	 * </pre> 
	 */
	public String updateCompany(Company company, LoginUser loginUser) {
		if (company.isEditMemberId()) {
			if (this.companyDAO.findMemberId(company.getComId(), company.getMemberId()) > 0) {
				return "memberId.used";
			}
		}
		if (company.isEditComName()) {
			if (this.companyDAO.findComName(company.getComId(), company.getComName()) > 0) {
				return "comName.used";
			}
		}
		
		Attachment regAtt = null;
		Attachment regAtt2 = null;
		Map<String, String> regMap = UploadUtil.getFileParam(company.getRegImgPath());
		Map<String, String> regMap2 = UploadUtil.getFileParam(company.getRegImgPath2());
		
		
		UpdateMap companyProfileMap = new UpdateMap("CompanyProfile");
		boolean uploadType = false; // �Ƿ�ִ���ϴ� 
		
		String regImgPath = company.getRegImgPath(), regImgPath2 = company.getRegImgPath2();
		
		if (Boolean.parseBoolean(regMap.get("isUpload"))) {
			if("undefined".equals(regMap.get("fileType"))){
				return "company.profileImg.error";
			}
			regAtt = UploadUtil.swfuploadFile(company.getComId(), Integer.parseInt(regMap.get("fileType")), regMap.get("fileName"), regMap.get("filePath"));
			if (regAtt == null) {
				return "company.profileImg.error";
			}
			regImgPath = regAtt.getFilePath();
			uploadType = true;
		}
		if (Boolean.parseBoolean(regMap2.get("isUpload"))) {
			if("undefined".equals(regMap2.get("fileType"))){
				return "company.profileImg.error";
			}
			regAtt2 = UploadUtil.swfuploadFile(company.getComId(), Integer.parseInt(regMap2.get("fileType")), regMap2.get("fileName"), regMap2.get("filePath"));
			if (regAtt2 == null) {
				return "company.profileImg.error";
			}
			regImgPath2 = regAtt2.getFilePath();
			uploadType = true;
		}
		
		//ִ�����͸ı�
		if (company.getOldImgType() != company.getRegImgType()) {
			// ���޸��У�����Ǵ��ϴ������޸�Ϊ�ϴ���ҵƾ֤������ֻ�ϴ���һ�� ������ԭ�����ϴ���2�Ÿ���֤������ʱҪ���ԭ�����Ǹ��ڶ���ͼƬ��
			if (company.getRegImgType() == 1) {
				if (StringUtil.isBlank(regImgPath) && StringUtil.isNotBlank(regImgPath2)) {
					regImgPath = regImgPath2;
				}
				regImgPath2 = "";
			}
			uploadType = true;
		}
		
		//�ж��Ƿ�ɾ���˵ڶ���ͼƬ
		if (StringUtil.isNotBlank(company.getOldRegImgPath2()) && StringUtil.isBlank(regImgPath2)) {
			regImgPath2 = "";
			uploadType = true;
		}
		
		if (StringUtil.isBlank(regImgPath) && StringUtil.isNotBlank(regImgPath2)) {
			regImgPath = regImgPath2;
			regImgPath2 = "";
			uploadType = true;
		}
		
		companyProfileMap.addField("regImgPath", regImgPath);
		companyProfileMap.addField("regImgPath2", regImgPath2);
		
		if (uploadType) {
			CompanyProfile companyProfile = this.companyDAO.findCompanyProfile(company.getComId());
			if (companyProfile != null) {
				companyProfileMap.addField("modifyTime", new DateUtil().getDateTime());
				companyProfileMap.addField("regImgType", company.getRegImgType());
				companyProfileMap.addWhere("comId", company.getComId());
				this.companyDAO.update(companyProfileMap);
			} else {
				companyProfile = new CompanyProfile();
				companyProfile.setComId( company.getComId());
				if (StringUtil.isNotBlank(regImgPath)) {
					companyProfile.setRegImgPath(regImgPath);
				}
				if (StringUtil.isNotBlank(regImgPath2)) {
					companyProfile.setRegImgPath2(regImgPath2);
				}
				companyProfile.setRegImgType(company.getRegImgType());
				companyProfile.setLegalForm("");
				companyProfile.setCeo("");
				companyProfile.setRegAddress("");
				companyProfile.setRegCapital("");
				companyProfile.setRegNo("");
				companyProfile.setRegAuthority("");
				companyProfile.setRegDate("");
				companyProfile.setRegExpiry("");
				companyProfile.setRegLink("");
				companyProfile.setCustomer("");
				companyProfile.setFacLocation("");
				companyProfile.setFacSize("");
				companyProfile.setEmployee("");
				companyProfile.setRDStaff("");
				companyProfile.setAnnualSale("");
				companyProfile.setExpPercent("");
				companyProfile.setEstablishDate("");
				companyProfile.setCreateTime(new DateUtil().getDateTime());
				companyProfile.setModifyTime(new DateUtil().getDateTime());
				this.companyDAO.addCompanyProfile(companyProfile);
			}
		}
		
		if (company.isCheckRegNo()) {
			UpdateMap regNoMap = new UpdateMap("CompanyProfile");
			regNoMap.addWhere("comId", company.getComId());
			if (company.isEditRegNo()) {
				regNoMap.addField("regNo", company.getRegNo());
				this.companyDAO.update(regNoMap);
			}
			if (company.isEditCeo()) {
				regNoMap.addField("ceo", company.getCeo());
				this.companyDAO.update(regNoMap);
			}
		}
		
		company.setCatIds(StringUtil.toString(company.getCatId(), ","));
		UpdateMap companyMap = new UpdateMap("Company");
		String businessType = "";
		for (String item : company.getBusinessType()) {
			businessType += item + ",";
		}

		String qualityCert = "";
		if (company.getQualityCert() != null) {
			for (String item : company.getQualityCert()) {
				qualityCert += item + ",";
			}
		}

		businessType = StringUtil.trimComma(businessType);

		qualityCert = StringUtil.trimComma(qualityCert);

		String websites = StringUtil.toString(company.getWebsite(), ",");
		String keywords = StringUtil.toString(company.getKeyword(), ",");

		String catId = "";
		for (String item : company.getCatId()) {
			catId += item + ",";
		}
		catId = StringUtil.trimComma(catId);
		if (company.isEditMemberId()) {
			companyMap.addField("memberId", company.getMemberId());
		}
		if (company.isEditComName()) {
			companyMap.addField("comName", company.getComName());
		}
		companyMap.addField("businessTypes", businessType);
		companyMap.addField("qualityCerts", qualityCert);

		companyMap.addField("domId", company.getDomId());
		companyMap.addField("websites", websites);
		companyMap.addField("keywords", keywords);
		companyMap.addField("catIds", catId);
		int currentState = companyDAO.findCompanyState(company.getComId()); //Ϊִ�и���ǰ�Ĺ�˾״̬
		
		int state = CN.STATE_WAIT;
		// �ƽ��Ա���ͨ�����޸ĺ� ��Ϊ"�������"
		if (loginUser.getMemberType() == 2) {
			if (currentState == 20) {
				state = CN.STATE_REJECT_WAIT; // �������
			} else if (currentState == CN.STATE_REJECT_WAIT) { // ������� �޸ĺ��� 
				state = CN.STATE_REJECT_WAIT;
			} else {
				state = CN.STATE_WAIT;
			}
		} else {
			state = CN.STATE_WAIT;
		}
		companyMap.addField("state", state);
		
		companyMap.addField("modifyTime", new DateUtil().getDateTime());
		companyMap.addField("updateTime", new DateUtil().getDateTime());
		companyMap.addField("description", company.getDescription());
		companyMap.addField("comNameEN", company.getComNameEN() == null ? "" : company.getComNameEN());
		// �ϴ��̱�ע��֤��
		Map<String, String> map = UploadUtil.getFileParam(company.getLogoCertImg());
		if (Boolean.parseBoolean(map.get("isUpload"))) {
			Attachment att = UploadUtil.swfuploadFile(company.getComId(), Integer.parseInt(map.get("fileType")), map.get("fileName"), map.get("filePath"));
			if (att == null) {
				return "company.logoCertImg.error";
			}
			companyMap.addField("logoCertImg", att.getFilePath());
		} else {
			companyMap.addField("logoCertImg", company.getLogoCertImg() == null ? "" : company.getLogoCertImg());
		}
		int tmpImgCount = 0;
		Company memberType = this.companyDAO.findCompanyMemberType(company.getComId());
		
		// �ϴ��̱�ͼ
		map = UploadUtil.getImgParam(company.getLogoImgPath());
		if (Boolean.parseBoolean(map.get("isUpload"))) {
			// ͼƬδ�������ϴ�
			if (company.getImgCount() + tmpImgCount++ < memberType.getImgMax()) {
				Image image = new Image();
				image.setComId(company.getComId());
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				if(!number_pattern.matcher(map.get("imgType")).matches()){//�жϲ���һλ���־����ó�1��1Ϊ��˾logo����ͼƬ
					map.put("imgType", "1");
				}
				image.setImgType(Integer.parseInt(map.get("imgType")));
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "company.logoImg.error";
				}
				companyMap.addField("logoImgPath", img.getImgPath());
			}
		} else {
			companyMap.addField("logoImgPath", company.getLogoImgPath() == null ? "" : company.getLogoImgPath());
		}

		// �ϴ�����򳧷�ͼƬ
		if (company.getFace() != null && company.getFace().length > 0) {
			String faceImgPaths = "";
			for (String faceImgPath : company.getFace()) {
				map = UploadUtil.getImgParam(faceImgPath);
				if (Boolean.parseBoolean(map.get("isUpload"))) {
					// ͼƬδ�������ϴ�
					if (company.getImgCount() + tmpImgCount++ < memberType.getImgMax()) {
						Image image = new Image();
						image.setComId(company.getComId());
						image.setImgName(map.get("imgName"));
						image.setImgPath(map.get("imgPath"));
						if(!number_pattern.matcher(map.get("imgType")).matches() || !"2".equals(map.get("imgType"))){//�жϲ���һλ���־����ó�2��2Ϊ��˾face����ͼƬ
							map.put("imgType", "2");
						}
						image.setImgType(Integer.parseInt(map.get("imgType")));
						com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
						if (img.getImgId() <= 0) {
							return "company.faceImg.error";
						}
						faceImgPaths += "," + img.getImgPath();
					}
				} else {
					faceImgPaths += "," + faceImgPath;
				}
			}
			companyMap.addField("faceImgPaths", StringUtil.trimComma(faceImgPaths));
		} else {
			companyMap.addField("faceImgPaths", "");
		}

		companyMap.addField("videoId", company.getVideoId());
		companyMap.addWhere("comId", company.getComId());

		if (companyDAO.update(companyMap) <= 0) {
			return "operateFail";
		}

		if (company.isEditMemberId()) {
			loginUser.setMemberId(company.getMemberId());
		}
		if (company.isEditComName()) {
			loginUser.setComName(company.getComName());
		}

		//����Ŀ¼ID·��
		String[] oldCatIds = StringUtil.toArray(company.getOldCatIds(), ",");
		String[] newCatIds = company.getCatId();
		// ����ɵĺ���ѡ�Ĳ�һ��
		if (!StringUtil.equalsValues(oldCatIds, newCatIds)) {
			// ɾ���ɵ�CompanyCategory��ϵ
			UpdateMap updateMap = new UpdateMap("CompanyCategory");
			updateMap.addWhere("comId", company.getComId());
			companyDAO.delete(updateMap);
			// ����µ�CompanyCategory��ϵ
			companyDAO.addCompanyCategory(company.getComId(), CategoryUtil.getIds(newCatIds));
		}

		UserLog userLog = UserLogUtil.getUserLog(UserLog.MEMBER, UserLog.MODIFY, "�޸Ĺ�˾��Ϣ", loginUser);
		companyDAO.addUserLog(userLog);
		
		// loginUser�д���Keywords����ʾ��˾��Ϣ����
		if(StringUtil.isBlank(loginUser.getKeywords())){
			loginUser.setKeywords(keywords);
		}
		this.updateLoginUser();
		return "editSuccess";
	}

	/**
	 * <p>������ϵ��
	 * @param user
	 * @param loginUser
	 * @return
	 * <pre>
	 *   email.used
	 *   user.headImg.error
	 *   editSuccess
	 *   operateFail
	 * </pre>
	 */
	public String updateContact(User user, LoginUser loginUser) {
		if (this.userDAO.findEmail(user.getUserId(), user.getEmail()) > 0) {
			return "email.used";
		}
		if (StringUtil.isEmpty(user.getProvince())) {
			return "user.province.required";
		}
		if (StringUtil.isEmpty(user.getCity())) {
			return "user.city.required";
		}
		
		if (!talkService.addTalks(user, loginUser.getComId())) {
			return "����д��ȷ��QQ��Ϣ";
		}
		
		UpdateMap userMap = new UpdateMap("Users");
		// �ϴ���ϵ��ͼƬ
		Map<String, String> map = UploadUtil.getImgParam(user.getHeadImgPath());
		if (Boolean.parseBoolean(map.get("isUpload"))) {
			Company company = this.companyDAO.findCompanyMemberType(loginUser.getComId());
			// ͼƬδ�������ϴ�
			if (company.getImgCount() < company.getImgMax()) {
				Image image = new Image();
				image.setComId(loginUser.getComId());
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				if(!number_pattern.matcher(map.get("imgType")).matches() || !"6".equals(map.get("imgType"))){//�жϲ���һλ���־����ó�6��6Ϊ��˾��ϵ������ͼƬ
					map.put("imgType", "6");
				}
				image.setImgType(Integer.parseInt(map.get("imgType")));
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "user.headImg.error";
				}
				userMap.addField("headImgPath", img.getImgPath());
			}
		} else {
			userMap.addField("headImgPath", user.getHeadImgPath());
		}

		userMap.addField("contact", user.getContact());
		userMap.addField("sex", user.getSex());
		userMap.addField("title", user.getTitle());
		userMap.addField("province", user.getProvince());
		userMap.addField("city", user.getCity());
		userMap.addField("town", user.getTown());
		userMap.addField("street", user.getStreet());
		userMap.addField("zip", user.getZip());
		userMap.addField("department", user.getDepartment());
		userMap.addField("job", user.getJob());
		userMap.addField("email", user.getEmail());
		userMap.addField("tel", user.getTel());
		userMap.addField("fax", user.getFax());
		userMap.addField("mobile", user.getMobile());
		userMap.addField("isSms", user.isSms() ? 1 : 0);
		userMap.addField("isShowMobile", user.isShowMobile() ? 1 : 0);
		userMap.addField("qq", user.getQq());
		userMap.addField("qqcode", "");
		userMap.addField("msn", user.getMsn());
		userMap.addField("msncode", user.getMsncode());
		userMap.addField("skype", user.getSkype());
		userMap.addField("state", 15);
		userMap.addField("modifyTime", new DateUtil().getDateTime());
		userMap.addWhere("userId", user.getUserId());

		if (userDAO.update(userMap) > 0) {
			// ����Google����
			userMap = new UpdateMap("CompanyExtra");
			userMap.addField("googleLocal", user.getGoogleLocal() == null ? "" : user.getGoogleLocal());
			userMap.addWhere("comId", loginUser.getComId());
			userDAO.update(userMap);
			
			UserLog userLog = UserLogUtil.getUserLog(UserLog.MEMBER, UserLog.MODIFY, "�޸Ĺ�˾��ϵ��", loginUser);
			companyDAO.addUserLog(userLog);

			this.updateLoginUser();
			return "editSuccess";
		}
		return "operateFail";
	}

	/**
	 * <p>�������ʺ�
	 * @param user
	 * @param loginUser
	 * @return
	 * <pre>
	 *   email.used
	 *   user.headImg.error
	 *   editSuccess
	 *   operateFail
	 * </pre>
	 */
	public String updateUser(User user, LoginUser loginUser) {
		int comId = loginUser.getComId();
		if (this.userDAO.findEmail(user.getUserId(), user.getEmail()) > 0) {
			return "email.used";
		}
		
		UpdateMap userMap = new UpdateMap("Users");
		
		if (!talkService.addTalks(user, comId)) {
			return "����д��ȷ�� QQ �˺���Ϣ";
		}

		// �ϴ���ϵ��ͼƬ
		Map<String, String> map = UploadUtil.getImgParam(user.getHeadImgPath());
		if (Boolean.parseBoolean(map.get("isUpload"))) {
			Company company = this.companyDAO.findCompanyMemberType(loginUser.getComId());
			// ͼƬδ�������ϴ�
			if (company.getImgCount() < company.getImgMax()) {
				Image image = new Image();
				image.setComId(loginUser.getComId());
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				if(!number_pattern.matcher(map.get("imgType")).matches() || !"6".equals(map.get("imgType"))){//�жϲ���һλ���־����ó�6��6Ϊ��˾��ϵ������ͼƬ
					map.put("imgType", "6");
				}
				image.setImgType(Integer.parseInt(map.get("imgType")));
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "user.headImg.error";
				}
				userMap.addField("headImgPath", img.getImgPath());
			}
		} else {
			userMap.addField("headImgPath", user.getHeadImgPath());
		}

		userMap.addField("contact", user.getContact());
		userMap.addField("passwd", user.getPasswd());
		if (loginUser.isAdmin()) {
			userMap.addField("privilege", user.getPrivilege());
		}
		userMap.addField("sex", user.getSex());
		userMap.addField("title", user.getTitle());
		userMap.addField("province", user.getProvince());
		userMap.addField("city", user.getCity());
		userMap.addField("town", user.getTown());
		userMap.addField("street", user.getStreet());
		userMap.addField("zip", user.getZip());
		userMap.addField("department", user.getDepartment());
		userMap.addField("job", user.getJob());
		userMap.addField("email", user.getEmail());
		userMap.addField("tel", user.getTel());
		userMap.addField("fax", user.getFax());
		userMap.addField("mobile", user.getMobile());
		userMap.addField("isSms", user.isSms() ? 1 : 0);
		userMap.addField("isShowMobile", user.isShowMobile() ? 1 : 0);
		userMap.addField("qq", user.getQq());
		userMap.addField("qqcode", user.getQqcode());
		userMap.addField("msn", user.getMsn());
		userMap.addField("msncode", user.getMsncode());
		userMap.addField("skype", user.getSkype());
		userMap.addField("isShow", user.isShow() ? 1 : 0);
		userMap.addField("modifyTime", new DateUtil().getDateTime());
		userMap.addField("state", CN.STATE_WAIT);
		userMap.addWhere("comId", user.getComId());
		userMap.addWhere("userId", user.getUserId()); 
		if (userDAO.update(userMap) > 0) {
			UserLog userLog = UserLogUtil.getUserLog(UserLog.MEMBER, UserLog.MODIFY, "�޸����ʺ�", loginUser);
			companyDAO.addUserLog(userLog);

			return "editSuccess";
		}
		return "operateFail";
	}

	/**
	 * <p>��������
	 * @param user
	 * @param loginUser
	 * @return
	 * <pre>
	 *   editSuccess
	 *   operateFail
	 *   passwdError
	 * </pre>
	 */
	public String updatePasswd(User user, LoginUser loginUser) {
		User u = this.userDAO.findUser(loginUser.getUserId(), loginUser.getComId());
		if (!StringUtil.equalsIgnoreCase(user.getOldPasswd(), u.getPasswd())) {
			return "passwdError";
		}
		UpdateMap userMap = new UpdateMap("Users");
		userMap.addField("passwd", user.getPasswd());
		userMap.addWhere("userId", loginUser.getUserId());
		userMap.addWhere("passwd", user.getOldPasswd());
		if (userDAO.update(userMap) > 0) {
			UserLog userLog = UserLogUtil.getUserLog(UserLog.MEMBER, UserLog.MODIFY, "�޸�����", loginUser);
			companyDAO.addUserLog(userLog);

			return "editSuccess";
		}
		return "operateFail";
	}

	/**
	 * <p>ɾ�����ʺ�
	 * @param params
	 * @return
	 * <pre>
	 *   deleteSuccess
	 * </pre>
	 */
	public String deleteUser(QueryParams params) {

		int comId = params.getLoginUser().getComId();
		int adminUserId = params.getLoginUser().getUserId();

		User user = this.userDAO.findUser(params.getUserId(), comId);

		UpdateMap userLogMap = new UpdateMap("UserLog");
		userLogMap.addWhere("comId", comId);
		userLogMap.addWhere("userId", params.getUserId());
		userDAO.delete(userLogMap);

		UpdateMap userMap = new UpdateMap("Users");
		userMap.addWhere("comId", comId);
		userMap.addWhere("userId", params.getUserId());
		userMap.addWhere("isAdmin", 0);
		userDAO.delete(userMap);

		UpdateMap companyMap = new UpdateMap("CompanyExtra");
		companyMap.addField("userCount", "-", 1);
		companyMap.addWhere("comId", comId);
		companyDAO.update(companyMap);

		UpdateMap productMap = new UpdateMap("Product");
		productMap.addField("userId", adminUserId);
		productMap.addWhere("comId", comId);
		productMap.addWhere("userId", params.getUserId());
		companyDAO.update(productMap);

		UpdateMap tradeMap = new UpdateMap("Trade");
		tradeMap.addField("userId", adminUserId);
		tradeMap.addWhere("comId", comId);
		tradeMap.addWhere("userId", params.getUserId());
		companyDAO.update(tradeMap);

		UpdateMap inquiryMap = new UpdateMap("Inquiry");
		inquiryMap.addField("userId", adminUserId);
		inquiryMap.addWhere("comId", comId);
		inquiryMap.addWhere("userId", params.getUserId());
		companyDAO.update(inquiryMap);

		UpdateMap tradeAlertMap = new UpdateMap("TradeAlert");
		tradeAlertMap.addWhere("comId", comId);
		tradeAlertMap.addWhere("userId", params.getUserId());
		userDAO.delete(tradeAlertMap);
		
		talkService.deleteTalksByUserId(params.getUserId());

		UserLog userLog = UserLogUtil.getUserLog(UserLog.MEMBER, UserLog.DELETE, "ɾ�����ʺ�", params.getLoginUser());
		companyDAO.addUserLog(userLog);

		if (user.getTqId() >= 5070001 && user.getTqId() <= 5080000) {
			CNTQWebService.delete(user.getTqId());
		}

		return "deleteSuccess";
	}
	
	public CompanyProfile getProfileByLoginUser(LoginUser loginUser) {
		int comId = loginUser.getComId();
		CompanyProfile profile = getProfileByComId(comId);
		Company company = companyDAO.findCompany(comId);
		if (profile == null) {
			profile = new CompanyProfile();
		}
		profile.setComName(company.getComName());
		company.setCeo(profile.getCeo());
		if (StringUtil.isBlank(company.getCeo())) {//�����Ƿ���޸�
			profile.setEditCeo(true);
		}
		
		String[] ids = CategoryUtil.getIds(StringUtil.split(company.getCatIds(), ","));
		for (String id : ids) {
			if ("1".equals(id)) {
				profile.setFoodCompany(true);
			}
		}
		
		return profile;
	}
	
	private CompanyProfile getProfileByComId(int comId) {
		return companyDAO.findCompanyProfile(comId);
	}
	
	public boolean updateProfile(CompanyProfile profile, LoginUser loginUser) {
		int comId = loginUser.getComId();
		profile.setRegCapital(profile.getRegCapital() + ":" + profile.getCurrency());
		
		Map<String, String> taxImgMap = UploadUtil.getFileParam(profile.getTaxImgPath());
		if (Boolean.parseBoolean(taxImgMap.get("isUpload"))) {
			if("undefined".equals(taxImgMap.get("fileType"))){
				return false;
			}
			Attachment att = UploadUtil.swfuploadFile(comId, 
					Integer.parseInt(taxImgMap.get("fileType")), 
					taxImgMap.get("fileName"), taxImgMap.get("filePath"));
			if (att != null) {
				profile.setTaxImgPath(att.getFilePath());
			}
		}
		
		profile.setFoodImgPath(profile.getFoodImg());
		if (getProfileByComId(comId) == null && addProfile(profile, comId)) {
			return true;
		} else {
			UpdateMap update = new UpdateMap("CompanyProfile");
			update.addField("regLink", profile.getRegLink());
			update.addField("companyType", profile.getCompanyType());
			update.addField("regAddress", profile.getRegAddress());
			if (profile.isEditCeo()) {
				update.addField("ceo", profile.getCeo());
			}
			update.addField("regCapital", profile.getRegCapital());
			update.addField("businessScope", profile.getBusinessScope());
			update.addField("establishDate", profile.getEstablishDate());
			update.addField("regDate", profile.getRegDate());
			update.addField("regExpiry", profile.getRegExpiry());
			update.addField("regAuthority", profile.getRegAuthority());
			update.addField("reviewTime", profile.getReviewTime());
			if (StringUtil.isNotBlank(profile.getTaxImgPath())) {
				update.addField("taxImgPath", profile.getTaxImgPath());
				update.addField("foodImgPath", profile.getFoodImgPath());
			}
			
			update.addWhere("comId", comId);
			if (companyDAO.update(update) > 0) return true; 
		}
		return false;
	}
	
	private boolean addProfile(CompanyProfile companyProfile, int comId) {
		String currentTime = new DateUtil().getDateTime();
		StringUtil.trimToEmpty(companyProfile, "regImgPath,regImgPath2,legalForm,ceo,regAddress,regCapital" +
				",regNo,regAuthority,regDate,regExpiry,regLink,customer,facLocation,facSize" +
				",employee,annualSale,expPercent,establishDate,RDStaff,taxImgPath" +
				",foodImgPath");
		companyProfile.setCreateTime(currentTime);
		companyProfile.setModifyTime(currentTime);
		companyProfile.setComId(comId);
		return this.companyDAO.addCompanyProfile(companyProfile) > 0;
	}

	public String join(Register register) {
		DateUtil dateUtil = new DateUtil();
		String currentDate = dateUtil.getDate();
		String currentTime = dateUtil.getDateTime();
		int comId = 0;
		int userId = 0;
		if (StringUtil.isNotEmpty(register.getMemberId()) 
				&& this.companyDAO.findMemberId(register.getMemberId()) > 0) {
			return "memberId.used";
		}
		if (this.userDAO.findEmail(register.getEmail()) > 0) {
			return "email.used";
		}
		if (register.getJoinUserType() == 0) {
			if (this.companyDAO.findComName(register.getComName()) > 0) {
				return "comName.used";
			}
		}
		
		//������˾
		Company company = new Company();
		company.setRegMode(register.getRegMode());
		company.setComName(register.getComName());
		company.setMemberId(register.getMemberId());
		company.setCatIds(register.getCatIds());
		company.setKeywords(register.getKeywords());
		company.setDescription(register.getDescription());
		company.setDomId(register.getDomId());
		company.setBusinessTypes(register.getBusinessTypes());
		company.setWebsites(register.getWebsites());
		StringUtil.trimToEmpty(company, "catIds,keywords,description,businessTypes,websites");
		if (company.getRegMode() <= 0) {
			company.setRegMode(1);
		}
		if (StringUtil.isEmpty(company.getMemberId())) {
			company.setMemberId(dateUtil.getDate2() + dateUtil.getTime4());
		}

		company.setMemberType(CN.FREE_SITE);
		company.setState(CN.STATE_WAIT);
		company.setGoldBeginDate(currentDate);
		company.setGoldEndDate(currentDate);
		company.setGoldIndexLastTime(currentTime);
		company.setMenuGroupCount(2);

		company.setCreateTime(currentTime);
		company.setModifyTime(currentTime);
		company.setUpdateTime(currentTime);

		if (StringUtil.isNotEmpty(register.getRegImgPath())) {
			company.setProfile(true);
		} else {
			company.setProfile(false);
		}
		comId = this.companyDAO.addCompany(company);
		if (comId > 0) {
			if (StringUtil.isNotEmpty(company.getCatIds())) {
				// ����µ�CompanyCategory��ϵ
				companyDAO.addCompanyCategory(company.getComId(), 
						CategoryUtil.getIds(company.getCatIds().split(",")));
			}
			register.setComId(comId);
			//����Users
			User user = new User();
			user.setComId(comId);
			user.setMemberId(company.getMemberId());
			user.setEmail(register.getEmail());
			user.setPasswd(register.getPasswd());
			user.setProvince(register.getProvince());
			user.setCity(register.getCity());
			user.setTown(register.getTown());
			user.setContact(register.getContact());
			user.setSex(register.getSex());
			user.setTel(register.getTel());
			user.setFax(register.getFax());
			user.setStreet(register.getStreet());
			user.setAdmin(true);
			user.setShow(true);
			user.setNotLoginAlertDate(currentDate);
			user.setPreLoginTime(currentTime);
			user.setLastLoginTime(currentTime);
			user.setLoginTimes(0);
			user.setPrivilege("");
			user.setCreateTime(currentTime);
			user.setModifyTime(currentTime);
			StringUtil.trimToEmpty(user, "fax,street,preLoginIP,lastLoginIP" +
					",headImgPath,zip,department,job,mobile,linkId,qq" +
					",qqcode,msn,msncode,skype,town,title");
			userId = this.userDAO.addUser(user);

			register.setUserId(userId);

			//����WebSite
			WebSite website = new WebSite();
			website.setComId(comId);
			website.setLayoutNo(1);
			// templateNo ��Ϊ�����ȡ 1 ~ 66
			// website.setTemplateNo(34);
			website.setTemplateNo(Math.abs(new Random().nextInt()) % 66 + 1);
			
			website.setBannerNo(1);
			website.setBannerType(1);
			website.setCreateTime(currentTime);
			website.setModifyTime(currentTime);
			StringUtil.trimToEmpty(website, "domain,domainEN,chatMsg,chatUserId" +
					",bannerPath,statSite,statScript,googleSitemapVerify,ICPNo" +
					",ICPScript,siteName,siteLink,serviceSiteLink");
			this.websiteDAO.addWebSite(website);

			//���������̶��˵���
			Menu menuGroup1 = new Menu();
			menuGroup1.setComId(comId);
			menuGroup1.setGroupName("��˾����");
			menuGroup1.setListOrder(1);
			menuGroup1.setListStyle(CN.DISPLAY_LIST);
			menuGroup1.setFix(true);
			menuGroup1.setShow(true);
			menuGroup1.setState(CN.STATE_PASS);
			menuGroup1.setCreateTime(currentTime);
			menuGroup1.setModifyTime(currentTime);

			Menu menuGroup2 = new Menu();
			menuGroup2.setComId(comId);
			menuGroup2.setGroupName("����֤��");
			menuGroup2.setListOrder(2);
			menuGroup2.setListStyle(CN.DISPLAY_GALLERY);
			menuGroup2.setFix(true);
			menuGroup2.setShow(true);
			menuGroup2.setState(CN.STATE_PASS);
			menuGroup2.setCreateTime(currentTime);
			menuGroup2.setModifyTime(currentTime);
			this.menuDAO.addMenuGroup(menuGroup1);
			this.menuDAO.addMenuGroup(menuGroup2);

			// �ϴ�Ӫҵִ��
			CompanyProfile companyProfile = new CompanyProfile();
			//�����ݿⱣ�渽��
			Attachment joinAtt = null;
			Attachment joinAtt2 = null;
			String joinImgPath = register.getRegImgPath(), joinImgPath2 = register.getRegImgPath2();
			Map<String, String> uploadImgMap = UploadUtil.getFileParam(joinImgPath);
			Map<String, String> uploadImgMap2 = UploadUtil.getFileParam(joinImgPath2);
						
			if (Boolean.parseBoolean(uploadImgMap.get("isUpload"))) {
				joinAtt = UploadUtil.swfuploadFile(comId, Integer.parseInt(uploadImgMap.get("fileType")), uploadImgMap.get("fileName"), uploadImgMap.get("filePath"));
				joinImgPath = joinAtt == null ? "" : joinAtt.getFilePath();
			}
			
			if (Boolean.parseBoolean(uploadImgMap2.get("isUpload"))) {
				joinAtt2 = UploadUtil.swfuploadFile(comId, Integer.parseInt(uploadImgMap2.get("fileType")), uploadImgMap2.get("fileName"), uploadImgMap2.get("filePath"));
				joinImgPath2 = joinAtt2 == null ? "" : joinAtt2.getFilePath();
			}
			if (StringUtil.isBlank(joinImgPath) && StringUtil.isNotBlank(joinImgPath2)) {
				joinImgPath = joinImgPath2;
				joinImgPath2 = "";
			}
			companyProfile.setRegImgPath(joinImgPath);
			companyProfile.setRegImgPath2(joinImgPath2);
			
			companyProfile.setCeo(register.getCeo());
			companyProfile.setRegNo(register.getRegNo());
			companyProfile.setRegImgType(register.getRegImgType());
			addProfile(companyProfile, comId);

			Map<String, Object> map = new HashMap<String, Object>();
			if (company.getRegMode() == 5) {
				if (StringUtil.isNotEmpty(register.getTradeAlertKeyword())) {
					map.put("tradeAlertKeyword", register.getTradeAlertKeyword());
					map.put("tradeAlertCatName", "");
				} else if (StringUtil.isNotEmpty(register.getCatIds())) {
					map.put("tradeAlertKeyword", "");
					map.put("tradeAlertCatName", CategoryUtil.getNamePath(Integer.parseInt(register.getCatIds()), " >> "));
				}
			}
			if (pattern_memberId.matcher(user.getMemberId()).matches()) {
				map.put("editMemberId", "true");//����17λ����ʱ���ʼ�����"����"����
			} else {
				map.put("editMemberId", "false");
			}
			map.put("regMode", company.getRegMode());
			map.put("memberId", user.getMemberId());
			map.put("contact", user.getContact());
			map.put("email", user.getEmail());
			
			String passWord = user.getPasswd();
			StringBuilder psw = new StringBuilder();
			for (int p = 2; p < passWord.length(); p++) {
				psw.append("*");
			}
			map.put("passwd", StringUtil.substring(passWord, 2, psw.toString(), false));
			
			map.put("service_contact", CASClient.getInstance().getAutoLoginURL(Config.getString("account.base") + "/basic/service_mail.htm", user.getMemberId(), user.getPasswd()));

			MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
			sender.add(MailFactory.getRegister(map));
			TaskExecutor.execute(sender);
			return "addSuccess";
		} else {
			return "operateFail";
		}
	}

	private void parseTel(User user) {
		if (StringUtil.isNotBlank(user.getTel())) {
			String[] tels = user.getTel().split("-");
			if (tels.length == 1) {
				user.setTel2(tels[0]);
			} else if (tels.length == 2) {
				user.setTel1(tels[0]);
				user.setTel2(tels[1]);
			} else if (tels.length == 3) {
				user.setTel1(tels[0]);
				user.setTel2(tels[1] + "-" + tels[2]);
			}
		}
	}

	private void parseFax(User user) {
		if (StringUtil.isNotBlank(user.getFax())) {
			String[] tels = user.getFax().split("-");
			if (tels.length == 1) {
				user.setFax2(tels[0]);
			} else if (tels.length == 2) {
				user.setFax1(tels[0]);
				user.setFax2(tels[1]);
			} else if (tels.length == 3) {
				user.setFax1(tels[0]);
				user.setFax2(tels[1] + "-" + tels[2]);
			}
		}
	}
	
	/**
	 * ����session
	 */
	private void updateLoginUser(){
		HttpServletRequest request = ServletActionContext.getRequest();
		LoginUser l=(LoginUser) request.getSession().getAttribute("com.hisupplier.cas.user");
		l.setState(15);
		l.setUserState(15);
		request.setAttribute("com.hisupplier.cas.user", l);
	}
	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setVideoDAO(VideoDAO videoDAO) {
		this.videoDAO = videoDAO;
	}

	public void setMenuDAO(MenuDAO menuDAO) {
		this.menuDAO = menuDAO;
	}

	public void setWebsiteDAO(WebsiteDAO websiteDAO) {
		this.websiteDAO = websiteDAO;
	}

	public void setTalkService(TalkService talkService) {
		this.talkService = talkService;
	}

	
}
