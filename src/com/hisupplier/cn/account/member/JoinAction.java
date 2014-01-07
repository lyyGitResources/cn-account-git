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

	private String regToken = "";//注册标记 代表不同的注册模式

	public String join() throws Exception {
		result = new HashMap<String, Object>();
		result.put("profileImagePath", Config.getString("img.base") + "/img/no_photo.gif" );//营业执照图片
		result.put("headDescription", "海商网免费注册会员，会员注册，在线交易，买卖信息，贸易机会，商业信息，供求信息");
		result.put("navbar", "免费注册会员");
		result.put("register", register);
		return SUCCESS;
	}

	public String join_submit() throws Exception {
		result = new HashMap<String, Object>();
		result.put("headDescription", "海商网免费注册会员，会员注册，在线交易，买卖信息，贸易机会，商业信息，供求信息");
		if (StringUtil.isNotEmpty(regToken)) {
			if (Coder.encodeX2("jindie").equals(regToken)) {
				register.setRegMode(13);//注册模式为金蝶推广 编码为a2hvY2pk
			}
		}
		tip = this.companyService.join(register);
		if ("addSuccess".equals(tip)) {
			String returnUrl = Config.getString("account.base") + "/member/company_edit.htm?reg=true";
			this.autoLoginURL = CASClient.getInstance().getAutoLoginURL(returnUrl, register.getMemberId(), register.getPasswd());

		} else {
			this.addActionError(TextUtil.getText(tip,"zh"));
			// result = new HashMap<String, Object>();
			//处理营业执照路径，方便在页面显示
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
		map.put("comName", "公司名,");
		map.put("keyword", "行业关键词,");
		String patentKeyword = PatentUtil.checkKeyword(company, map, 0);
		if (StringUtil.isNotEmpty(patentKeyword)) {
			this.addActionError("您输入的公司名称 中包含违禁词:"+ patentKeyword.split(",") +"");
		}
		
		StringUtil.trimToEmpty(register, "email,memberId,passwd,confirmPasswd,comName,contact,tel1,tel2,validateCode");
		String email = register.getEmail();
		if (StringUtil.isEmpty(register.getCity())) {
			this.addActionError("请选择公司所在地区");
		}

		if (!Validator.isEmail(email)) {
			this.addActionError("请输入有效的电子邮箱，便于找回密码");
		}

		tip = Validator.isMemberId(register.getMemberId());
		if (tip.equals("invalid")) {//检测是否无效
			this.addActionError(TextUtil.getText("memberId.required","zh"));
		} else if (tip.equals("used")) {//检测是否使用
			this.addActionError(TextUtil.getText("memberId.used","zh"));
		}
		if (!Validator.isPassword(register.getPasswd())) {
			this.addActionError(TextUtil.getText("passwd.required","zh"));
		} else if (!StringUtil.equalsIgnoreCase(register.getPasswd(), register.getConfirmPasswd())) {
			this.addActionError("两次密码不匹配");
		}
		if (StringUtil.isEmpty(register.getComName()) || register.getComName().length() > 120) {
			this.addActionError(TextUtil.getText("comName.required","zh"));
		} else {
			register.setComName(register.getComName().replace(" ", ""));
		}
		if (StringUtil.isEmpty(register.getContact()) || register.getContact().length() > 20) {
			this.addActionError("请输入姓名，长度在20个字符内");
		}

		if (Validator.isTel(register.getTel1(), register.getTel2())) {
			register.setTel(register.getTel1() + "-" + register.getTel2());
		} else {
			this.addActionError("请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用\",\"或\"/\"分隔，分机号码请用\"-\"分隔");
		}

		if(StringUtil.isBlank(register.getRegImgPath()) && StringUtil.isBlank(register.getRegImgPath2())){
			result = new HashMap<String, Object>();
			result.put("profileImagePath", Config.getString("img.base") + "/img/no_photo.gif" );//营业执照图片
			this.addActionError("营业执照或有效证件不能为空");
		}
		
		if (!ValidateCode.isValid(request, register.getValidateCodeKey(), register.getValidateCode())) {
			this.addActionError(TextUtil.getText("validateCode.error", "zh"));
		}
		if (register.getSex() != 1 && register.getSex() != 2 && register.getSex() != 3) {
			this.addActionError("请输入性别");
		}
		
		if (register.getRegImgType() == 1 && register.getProvince().equals("103103")) { //浙江地区     企业
			if (StringUtil.isEmpty(register.getCeo())) {
				addFieldError("ceo", "法定代表人不能为空。");
			}
			
			if (StringUtil.isEmpty(register.getRegNo())) {
				addFieldError("regNo", "营业执照注册号不能为空。");
			}
		}
		
		if (this.hasActionErrors()) {
			if(result == null){
				result = new HashMap<String, Object>();
			}
			//处理营业执照路径，方便在页面显示
			//filePath:/upload/local201008/12/173710679722.jpg;fileType:6;fileName:500x200.jpg
			if (StringUtil.isNotEmpty(register.getRegImgPath())) {
				getProfileImagePath();
			}
			result.put("register", register);
			result.put("headDescription", "海商网免费注册会员，会员注册，在线交易，买卖信息，贸易机会，商业信息，供求信息");
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
