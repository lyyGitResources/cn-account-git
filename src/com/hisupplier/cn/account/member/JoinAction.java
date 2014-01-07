package com.hisupplier.cn.account.member;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cas.CASClient;
import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Register;
import com.hisupplier.cn.account.util.PatentUtil;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.util.Coder;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.ValidateCode;
import com.hisupplier.commons.util.Validator;
import com.opensymphony.xwork2.ModelDriven;

public class JoinAction extends BasicAction implements ModelDriven<Register> {
	private static final long serialVersionUID = -7536541457724363003L;
	private CompanyService companyService;
	private Register register = new Register();
	private Map<String, Object> result;
	private String autoLoginURL;

	private String regToken = "";//ע���� ����ͬ��ע��ģʽ

	public String join() throws Exception {
		result = new HashMap<String, Object>();
		result.put("profileImagePath", Config.getString("img.base") + "/img/no_photo.gif" );//Ӫҵִ��ͼƬ
		result.put("headDescription", "���������ע���Ա����Աע�ᣬ���߽��ף�������Ϣ��ó�׻��ᣬ��ҵ��Ϣ��������Ϣ");
		result.put("navbar", "���ע���Ա");
		result.put("register", register);
		return SUCCESS;
	}

	public String join_submit() throws Exception {
		result = new HashMap<String, Object>();
		result.put("headDescription", "���������ע���Ա����Աע�ᣬ���߽��ף�������Ϣ��ó�׻��ᣬ��ҵ��Ϣ��������Ϣ");
		if (StringUtil.isNotEmpty(regToken)) {
			if (Coder.encodeX2("jindie").equals(regToken)) {
				register.setRegMode(13);//ע��ģʽΪ����ƹ� ����Ϊa2hvY2pk
			}
		}
		tip = this.companyService.join(register);
		if ("addSuccess".equals(tip)) {
			String returnUrl = Config.getString("account.base") + "/member/company_edit.htm?reg=true";
			this.autoLoginURL = CASClient.getInstance().getAutoLoginURL(returnUrl, register.getMemberId(), register.getPasswd());

		} else {
			this.addActionError(TextUtil.getText(tip,"zh"));
			// result = new HashMap<String, Object>();
			//����Ӫҵִ��·����������ҳ����ʾ
			//filePath:/upload/local201008/12/173710679722.jpg;fileType:6;fileName:500x200.jpg
			if (StringUtil.isNotEmpty(register.getRegImgPath())) {
				getProfileImagePath();
			}
			result.put("register", register);
			return "input";
		}
		return SUCCESS;
	}

	public void validateJoin_submit() {
		Company company = new  Company();
		company.setComName(register.getComName());
		
		Map<String, String> map = new HashMap<String, String>(); 
		map.put("comName", "��˾��,");
		map.put("keyword", "��ҵ�ؼ���,");
		String patentKeyword = PatentUtil.checkKeyword(company, map, 0);
		if (StringUtil.isNotEmpty(patentKeyword)) {
			this.addActionError("������Ĺ�˾���� �а���Υ����:"+ patentKeyword.split(",") +"");
		}
		
		StringUtil.trimToEmpty(register, "email,memberId,passwd,confirmPasswd,comName,contact,tel1,tel2,validateCode");
		String email = register.getEmail();
		if (StringUtil.isEmpty(register.getCity())) {
			this.addActionError("��ѡ��˾���ڵ���");
		}

		if (!Validator.isEmail(email)) {
			this.addActionError("��������Ч�ĵ������䣬�����һ�����");
		}

		tip = Validator.isMemberId(register.getMemberId());
		if (tip.equals("invalid")) {//����Ƿ���Ч
			this.addActionError(TextUtil.getText("memberId.required","zh"));
		} else if (tip.equals("used")) {//����Ƿ�ʹ��
			this.addActionError(TextUtil.getText("memberId.used","zh"));
		}
		if (!Validator.isPassword(register.getPasswd())) {
			this.addActionError(TextUtil.getText("passwd.required","zh"));
		} else if (!StringUtil.equalsIgnoreCase(register.getPasswd(), register.getConfirmPasswd())) {
			this.addActionError("�������벻ƥ��");
		}
		if (StringUtil.isEmpty(register.getComName()) || register.getComName().length() > 120) {
			this.addActionError(TextUtil.getText("comName.required","zh"));
		} else {
			register.setComName(register.getComName().replace(" ", ""));
		}
		if (StringUtil.isEmpty(register.getContact()) || register.getContact().length() > 20) {
			this.addActionError("������������������20���ַ���");
		}

		if (Validator.isTel(register.getTel1(), register.getTel2())) {
			register.setTel(register.getTel1() + "-" + register.getTel2());
		} else {
			this.addActionError("���������ź͵绰���룬���ų���3-5λ���֣��绰���볤����7-26���ַ��ڣ�����绰��\",\"��\"/\"�ָ����ֻ���������\"-\"�ָ�");
		}

		if(StringUtil.isBlank(register.getRegImgPath()) && StringUtil.isBlank(register.getRegImgPath2())){
			result = new HashMap<String, Object>();
			result.put("profileImagePath", Config.getString("img.base") + "/img/no_photo.gif" );//Ӫҵִ��ͼƬ
			this.addActionError("Ӫҵִ�ջ���Ч֤������Ϊ��");
		}
		
		if (!ValidateCode.isValid(request, register.getValidateCodeKey(), register.getValidateCode())) {
			this.addActionError(TextUtil.getText("validateCode.error", "zh"));
		}
		if (register.getSex() != 1 && register.getSex() != 2 && register.getSex() != 3) {
			this.addActionError("�������Ա�");
		}
		
		if (register.getRegImgType() == 1 && register.getProvince().equals("103103")) { //�㽭����     ��ҵ
			if (StringUtil.isEmpty(register.getCeo())) {
				addFieldError("ceo", "���������˲���Ϊ�ա�");
			}
			
			if (StringUtil.isEmpty(register.getRegNo())) {
				addFieldError("regNo", "Ӫҵִ��ע��Ų���Ϊ�ա�");
			}
		}
		
		if (this.hasActionErrors()) {
			if(result == null){
				result = new HashMap<String, Object>();
			}
			//����Ӫҵִ��·����������ҳ����ʾ
			//filePath:/upload/local201008/12/173710679722.jpg;fileType:6;fileName:500x200.jpg
			if (StringUtil.isNotEmpty(register.getRegImgPath())) {
				getProfileImagePath();
			}
			result.put("register", register);
			result.put("headDescription", "���������ע���Ա����Աע�ᣬ���߽��ף�������Ϣ��ó�׻��ᣬ��ҵ��Ϣ��������Ϣ");
		}
	}

	private void getProfileImagePath() {
		String path = register.getRegImgPath();
		int beginPos = path.indexOf("filePath") + "filePath".length() + 1;
		int endPos = path.indexOf(";");
		if(beginPos != -1 && endPos != -1){
			result.put("profileImagePath", Config.getString("img.fileLink") + path.substring(beginPos, endPos));
		}
	}
	
	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public Register getModel() {
		return register;
	}

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

	public String getAutoLoginURL() {
		return autoLoginURL;
	}

	public void setAutoLoginURL(String autoLoginURL) {
		this.autoLoginURL = autoLoginURL;
	}

	public String getRegToken() {
		return this.regToken;
	}

	public void setRegToken(String regToken) {
		this.regToken = regToken;
	}
}
