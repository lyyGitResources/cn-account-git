package com.hisupplier.cn.account.inquiry;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.hisupplier.cas.CASClient;
import com.hisupplier.cn.account.entity.Inquiry;
import com.hisupplier.cn.account.webservice.InquirySendService;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.basket.BasketFactory;
import com.hisupplier.commons.basket.BasketItem;
import com.hisupplier.commons.util.Coder;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.ValidateCode;
import com.hisupplier.commons.util.Validator;
import com.hisupplier.commons.util.WebUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class InquirySendAction extends ActionSupport implements ModelDriven<Inquiry> {

	private static final long serialVersionUID = 2271954573642365431L;
	public static final String[] ALLOW_MIME_TYPE = { "image/jpeg", "image/pjpeg", "image/gif", "image/png", "text/plain", "application/pdf", "application/msword", "application/vnd.ms-excel","application/x-msexcel",
			"application/zip" };
	private InquirySendService inquirySendService;
	private Inquiry inquiry = new Inquiry();
	private String autoLoginURL;

	public String inquiry() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String fromPage = request.getHeader("referer");
		if (StringUtil.isEmpty(fromPage)) {
			fromPage = Config.getString("sys.base");
		}
		inquiry.setFromPage(fromPage);
		inquiry.setFromWebsite("http://");
		inquiry.setLoginUser(CASClient.getInstance().getUser(request));
		List<BasketItem> basketItemList = BasketFactory.getBasket(request, "cn").getItems(request, response);
		for (int i = 0; i < basketItemList.size(); i++) {
			List<BasketItem.Product> productList = basketItemList.get(i).getProductList();
			String showroomUrl = "http://" + basketItemList.get(i).getMemberId() + ".cn." + Config.getString("sys.domain");
			for (int j = 0; j < productList.size(); j++) {
				productList.get(j).setProUrl(showroomUrl + "/product/detail-" + productList.get(j).getProId() + ".html");
			}

			List<BasketItem.Trade> tradeList = basketItemList.get(i).getTradeList();
			for (int k = 0; k < tradeList.size(); k++) {
				tradeList.get(k).setProUrl(showroomUrl + "/offer/detail-" + tradeList.get(k).getTradeId() + ".html");
			}
		}
		inquiry.setBasketItemList(basketItemList);
		if (inquiry.getBasketItemList().size() <= 0) {
			return "basket";
		}

		// ����ѯ������
		String subject = "";
		if (inquiry.getBasketItemList().size() == 1) {
			BasketItem item = inquiry.getBasketItemList().get(0);
			if (item.getProductList().size() == 1 && item.getTradeList().size() == 0) {
				subject = "����" + item.getProductList().get(0).getProName() + "��ѯ��";
			} else if (subject.equals("") && item.getTradeList().size() == 1 && item.getProductList().size() == 0) {
				subject = "����" + item.getTradeList().get(0).getTradeName() + "��ѯ��";
			} else {
				subject = "���ڲ�Ʒ��ѯ��";
			}
		} else {
			subject = "���ڲ�Ʒ��ѯ��";
		}
		inquiry.setSubject(subject);

		//���ö����������
		if (StringUtil.isEmpty(inquiry.getTradeAlertKeyword()) && inquiry.getBasketItemList().size() > 0) {
			String tradeAlertKeyword = "";
			if (inquiry.getBasketItemList().get(0).getProductList().size() > 0) {
				tradeAlertKeyword = inquiry.getBasketItemList().get(0).getProductList().get(0).getProName();
			} else if (inquiry.getBasketItemList().get(0).getTradeList().size() > 0) {
				tradeAlertKeyword = inquiry.getBasketItemList().get(0).getTradeList().get(0).getTradeName();
			}
			inquiry.setTradeAlertKeyword(tradeAlertKeyword);
			inquiry.setTradeAlertInfoType("product");
		}
		return SUCCESS;
	}

	public String inquiry_send() throws Exception {
		/*
		//ҳ����fromEmail ��email��ֵ��������󷵻ص�ҳ����Ҫ�໥ȡ���ݣ����������ֵ
		if (StringUtil.isNotEmpty(inquiry.getEmail())) {
			inquiry.setFromEmail(inquiry.getEmail());
		} else if (StringUtil.isNotEmpty(inquiry.getFromEmail())) {
			inquiry.setEmail(inquiry.getFromEmail());
		}
		*/
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		if (inquiry.getBasketItemList() == null || inquiry.getBasketItemList().size() <= 0) {
			return "basket";
		}
		String result = inquirySendService.send(inquiry);
		if (!result.equals("addSuccess")) {
			if (result.equals("ipLimit")) {
				this.addActionError("ÿIPÿ24Сʱ��ֻ��Ⱥ�����15���ʼ�������15���ÿ��ֻ�ܷ���1��");
			} else if (result.equals("passwdError")) {
				this.addActionError("��Ա�ʺţ����䲻���ڻ����������");
			} else if (result.equals("email.used")) {
				this.addActionError("�����ַ�Ѵ��ڣ�����������");
			} else if (result.equals("comName.used")) {
				this.addActionError("��˾�����Ѵ��ڣ�����������");
			} else if (result.equals("operateFail")) {
				this.addActionError("ѯ�̷���ʧ�ܣ��뷵������");
			} else if (result.equals("inquiry.repeat")) {
				this.addActionError("�Բ��𣬶�ʱ���ڲ��������ύ��ͬ���ݵ�ѯ�̣����Ժ����ԡ�");
			} else if (result.equals("alert.limit")){
				this.addActionError("���˺ź����˺Ź��ܶ���10������,���Ѷ���10������");
			}
			return INPUT;
		}
		BasketFactory.removeBasket(request, response);
		// �ѵ�¼
		if (inquiry.getLoginUser() != null) {
			autoLoginURL = "/user/inquiry_success.htm?fromPage=" + Coder.encodeURL(inquiry.getFromPage());
		} else {
			// �ǻ�Ա
			String returnUrl = "";
			if (!inquiry.isNewUser()) {
				if (Config.getString("isBig5").equalsIgnoreCase("true")) {
					returnUrl = "http://account.big5." + Config.getString("sys.domain") + "/user/inquiry_success.htm?fromPage=" + Coder.encodeURL(inquiry.getFromPage());
				} else {
					returnUrl = "http://account.cn." + Config.getString("sys.domain") + "/user/inquiry_success.htm?fromPage=" + Coder.encodeURL(inquiry.getFromPage());
				}
				autoLoginURL = CASClient.getInstance()
						.getAutoLoginURL(returnUrl, inquiry.getEmail(), inquiry.getPasswd());
			} else {
				// ���û�
				String account_domain = "http://account.cn." + Config.getString("sys.domain");
				if (Config.getString("isBig5").equalsIgnoreCase("true")) {
					account_domain = "http://account.big5." + Config.getString("sys.domain");
				}
				returnUrl = account_domain 
							+ "/user/inquiry_success.htm?fromPage="
							+ Coder.encodeURL(inquiry.getFromPage());
				autoLoginURL = returnUrl;
			}
		}
		return SUCCESS;
	}

	public String inquiry_success() throws Exception {
		return SUCCESS;
	}

	public void validateInquiry_send() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		inquiry.setFromIP(WebUtil.getRemoteIP(request));
		inquiry.setLoginUser(CASClient.getInstance().getUser(request));
		inquiry.setBasketItemList(BasketFactory.getBasket(request, "cn").getItems(request, response));

		StringUtil.trimToEmpty(inquiry, "subject,fromCompany,fromName,fromEmail,fromProvince,fromCity,fromTown,fromStreet,fromWebsite,email,passwd");

		if (StringUtil.isEmpty(inquiry.getSubject()) || inquiry.getSubject().length() > 120) {
			this.addActionError("������ѯ�����⣬����120���ַ���");
		}
		if (StringUtil.isEmpty(inquiry.getContent()) || inquiry.getContent().length() > 3000) {
			this.addActionError("������ѯ�����ݣ�����3000���ַ���");
		}
		if (inquiry.getUpload() != null) {
			for (int i = 0; i < inquiry.getUpload().length; i++) {
				if (!StringUtil.containsValue(ALLOW_MIME_TYPE, inquiry.getUploadContentType()[i]) || inquiry.getUpload()[i].length() / 1024 > 500) {
					this.addActionError(TextUtil.getText("attachment.invalid", "zh"));
				}
			}
		}
		
		// δ��¼
		if (inquiry.getLoginUser() == null) {
			// �ǻ�Ա
			if (!inquiry.isNewUser()) {
				if (!"ok".equals(Validator.isMemberId(inquiry.getEmail())) && !Validator.isEmail(inquiry.getEmail())) {
					this.addActionError("�������Ա�ʺŻ�����");
				}
				if (!Validator.isPassword(inquiry.getPasswd())) {
					this.addActionError("�������¼����");
				}
			} else {
				// ���û�
				if (!Validator.isEmail(inquiry.getFromEmail())) {
					this.addActionError("��������Ч�ĵ������䣬�����һ�����");
				}
				if ("company".equals(inquiry.getFromCompanyType())) {
					if (StringUtil.isEmpty(inquiry.getFromCompany()) 
							|| inquiry.getFromCompany().length() > 120) {
						this.addActionError("�����빫˾���ƣ�����120���ַ���");
					} else {
						inquiry.setFromCompany(
								inquiry.getFromCompany().replace(" ", ""));
					}
					/*
					if (StringUtil.isEmpty(inquiry.getFromProvince())) {
						this.addActionError("��ѡ��ʡ��");
					}
					if (StringUtil.isEmpty(inquiry.getFromCity())) {
						this.addActionError("��ѡ�����");
					}
					if (StringUtil.isEmpty(inquiry.getFromStreet()) || inquiry.getFromStreet().length() > 100) {
						this.addActionError("������ֵ���ַ��������120���ַ���");
					}
					*/
				}

				if (StringUtil.isEmpty(inquiry.getFromName()) 
						|| inquiry.getFromName().length() > 20) {
					this.addActionError("��������ϵ������������20���ַ���");
				}

				if (Validator.isTel(inquiry.getTel1(), inquiry.getTel2())) {
					inquiry.setFromTel(inquiry.getTel1() + "-" + inquiry.getTel2());
				} else {
					this.addActionError("���������ź͵绰���룬���ų���3-5λ���֣��绰���볤����7-26���ַ��ڣ�����绰��\",\"��\"/\"�ָ����ֻ���������\"-\"�ָ�");
				}

				if (StringUtil.isNotEmpty(inquiry.getFax1()) && StringUtil.isNotEmpty(inquiry.getFax2())) {
					if (Validator.isTel(inquiry.getFax1(), inquiry.getFax2())) {
						inquiry.setFromFax(inquiry.getFax1() + "-" + inquiry.getFax2());
					} else {
						this.addActionError("���������źʹ�����룬���ų���3-5λ���֣�������볤����7-26���ַ��ڣ����������','��\"/\"�ָ����ֻ���������\"-\"�ָ�");
					}
				} else {
					inquiry.setFromFax("");
				}

				if (StringUtil.isNotEmpty(inquiry.getFromWebsite())) {
					if (!Validator.isUrl(inquiry.getFromWebsite()) || inquiry.getFromWebsite().length() > 100) {
						this.addActionError("��������Ч�Ĺ�˾��ַ��������100���ַ���");
					} else {
						String website = inquiry.getFromWebsite().toLowerCase();
						if (!website.startsWith("http://") && !website.startsWith("https://")) {
							inquiry.setFromWebsite("http://" + website);
						}
					}
				}
			}
		}

		if (!ValidateCode.isValid(request, inquiry.getValidateCodeKey(), inquiry.getValidateCode())) {
			this.addActionError(TextUtil.getText("validateCode.error", "zh"));
		}

		if (this.hasActionErrors()) {
			inquiry.setPasswd("");
		}
	}

	public Inquiry getModel() {
		return inquiry;
	}

	public Inquiry getInquiry() {
		return inquiry;
	}

	public String getAutoLoginURL() {
		return autoLoginURL;
	}

	public void setInquirySendService(InquirySendService inquirySendService) {
		this.inquirySendService = inquirySendService;
	}

}
