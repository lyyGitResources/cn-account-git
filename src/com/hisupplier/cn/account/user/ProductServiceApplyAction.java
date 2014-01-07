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
		map.put("subject", "������֤��Ӧ�̷���");
		map.put("sex", sex == 1 ? "����" : sex ==2 ? "Ůʿ" : "С��");
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

		map.put("contactMenu", "��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;����");
		map.put("comNameMenu", "��˾���ƣ�");
		map.put("addressMenu", "��ϸ��ַ��");
		map.put("telMenu", "��ϵ�绰��");
		map.put("emailMenu", "�����ʼ���");
		map.put("websiteMenu", "��˾��վ��");
		map.put("supplementMenu", "����˵����");

		Mail mail = MailFactory.getExporterApply(map);
		MailSender emailSender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		emailSender.add(mail);
		TaskExecutor.execute(emailSender);

		this.addActionMessage("���ͳɹ�");
		return SUCCESS;
	}

	public void validateProduct_apply_submit() {
		if (StringUtil.isEmpty(contact)) {
			this.addActionError("����������������");
		}
		if (sex <=0) {
			this.addActionError("�����������Ա�");
		}
		if (StringUtil.isEmpty(comName)) {
			this.addActionError("�����빫˾���ƣ�");
		}

		if (StringUtil.isEmpty(address)) {
			this.addActionError("��������ϸ��ַ��");
		}

		if (StringUtil.isEmpty(tel)) {
			this.addActionError("��������ϵ�绰��");
		}

		if (StringUtil.isEmpty(email)) {
			this.addActionError("������������䣡");
		} else if (!Validator.isEmail(email)) {
			this.addActionError("���������ʽ����ȷ��");
		}

		if ("http://".equalsIgnoreCase(website)) {
			website = "";
		} else if (StringUtil.isNotBlank(website) && !Validator.isUrl(website)) {
			this.addActionError("��˾��վ��ʽ����ȷ��");
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