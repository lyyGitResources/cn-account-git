package com.hisupplier.cn.account.user;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.util.MailFactory;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.mail.Mail;
import com.hisupplier.commons.mail.MailSender;
import com.hisupplier.commons.mail.MailSenderCNFactory;
import com.hisupplier.commons.task.TaskExecutor;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.Validator;
import com.opensymphony.xwork2.ActionSupport;

public class ProductServiceApplyAction extends ActionSupport {

	private static final long serialVersionUID = -725340904672437998L;
	private String contact;
	private int sex;
	private String comName;
	private String address;
	private String tel;
	private String email;
	private String website = "http://";
	private String supplement;

	public String product_apply() {
		return SUCCESS;
	}

	public String product_apply_submit() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("subject", "申请认证供应商服务");
		map.put("sex", sex == 1 ? "先生" : sex ==2 ? "女士" : "小姐");
		map.put("base", Config.getString("sys.base"));
		map.put("imgBase", Config.getString("img.base"));
		map.put("date", new DateUtil().getDateTime());

		map.put("contact", contact);
		map.put("comName", comName);
		map.put("address", address);
		map.put("tel", tel);
		map.put("email", email);
		map.put("website", website);
		map.put("supplement", supplement);

		map.put("contactMenu", "姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：");
		map.put("comNameMenu", "公司名称：");
		map.put("addressMenu", "详细地址：");
		map.put("telMenu", "联系电话：");
		map.put("emailMenu", "电子邮件：");
		map.put("websiteMenu", "公司网站：");
		map.put("supplementMenu", "补充说明：");

		Mail mail = MailFactory.getExporterApply(map);
		MailSender emailSender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		emailSender.add(mail);
		TaskExecutor.execute(emailSender);

		this.addActionMessage("发送成功");
		return SUCCESS;
	}

	public void validateProduct_apply_submit() {
		if (StringUtil.isEmpty(contact)) {
			this.addActionError("请输入您的姓名！");
		}
		if (sex <=0) {
			this.addActionError("请输入您的性别！");
		}
		if (StringUtil.isEmpty(comName)) {
			this.addActionError("请输入公司名称！");
		}

		if (StringUtil.isEmpty(address)) {
			this.addActionError("请输入详细地址！");
		}

		if (StringUtil.isEmpty(tel)) {
			this.addActionError("请输入联系电话！");
		}

		if (StringUtil.isEmpty(email)) {
			this.addActionError("请输入电子邮箱！");
		} else if (!Validator.isEmail(email)) {
			this.addActionError("电子邮箱格式不正确！");
		}

		if ("http://".equalsIgnoreCase(website)) {
			website = "";
		} else if (StringUtil.isNotBlank(website) && !Validator.isUrl(website)) {
			this.addActionError("公司网站格式不正确！");
		}
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getSupplement() {
		return supplement;
	}

	public void setSupplement(String supplement) {
		this.supplement = supplement;
	}

}