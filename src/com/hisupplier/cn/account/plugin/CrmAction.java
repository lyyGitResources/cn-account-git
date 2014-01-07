package com.hisupplier.cn.account.plugin;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.commons.util.Coder;
import com.hisupplier.commons.util.StringUtil;

public class CrmAction extends BasicAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8848956284304707724L;
	private Map<String, Object> result;
	private CrmService crmService;

	public String gotoCRM() throws Exception {
		String message = "0";
		tip = "success";
		int comCrmState = crmService.getCompany(this.getLoginUser().getComId()).getCrmState();//公司同步状态
		int userCrmState = crmService.getUser(this.getLoginUser().getUserId(), this.getLoginUser().getComId()).getCrmState();//用户同步状态
		if (comCrmState == 0 && userCrmState == 0) {//公司和用户都未同步
			URL url = new URL(this.getURL("company"));
			URLConnection uc = url.openConnection();
			DocumentBuilderFactory db = DocumentBuilderFactory.newInstance();
			DocumentBuilder dombuilder = db.newDocumentBuilder();
			Document doc = (Document) dombuilder.parse(uc.getInputStream());
			Element root = doc.getDocumentElement();
			message = root.getChildNodes().item(1).getChildNodes().item(0).getNodeValue();
			if (StringUtil.equals(message, "0")) {
				url = new URL(this.getURL("User"));
				uc = url.openConnection();
				dombuilder = db.newDocumentBuilder();
				doc = (Document) dombuilder.parse(uc.getInputStream());
				root = doc.getDocumentElement();
				message = root.getChildNodes().item(1).getChildNodes().item(0).getNodeValue();
				tip = crmService.updateCRMState(this.getLoginUser(), "Company");
				tip = crmService.updateCRMState(this.getLoginUser(), "Users");
			}
		} else if (userCrmState == 0) {//公司已同步，用户未同步
			URL url = new URL(this.getURL("User"));
			URLConnection uc = url.openConnection();
			DocumentBuilderFactory db = DocumentBuilderFactory.newInstance();
			DocumentBuilder dombuilder = db.newDocumentBuilder();
			Document doc = (Document) dombuilder.parse(uc.getInputStream());
			Element root = doc.getDocumentElement();
			message = root.getChildNodes().item(1).getChildNodes().item(0).getNodeValue();
			tip = crmService.updateCRMState(this.getLoginUser(), "Users");
		}
		if (!StringUtil.equals("0", message) || !StringUtil.equals("success", tip)) {
			tip = "operateFail";
			msg = this.getText(tip);
		}
		result = new HashMap<String, Object>();
		result.put("loginUrl", this.getURL("login"));
		return SUCCESS;
	}

	/**
	 * 取得接口的url(同步公司信息，同步用户信息，登陆)
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private String getURL(String type) throws Exception {
		//String url = "http://outtest.youshang.com:8080/kdagent/federation/commonservice/servicePortal.do?partnerId=21&alg=1&cipherCode=";
		String url ="http://agent.youshang.com/federation/commonservice/servicePortal.do?partnerId=8194&alg=1&cipherCode=";
		String uuid = UUID.randomUUID().toString();
		uuid = Coder.encodeURL(uuid).substring(0, 16);
		String cipherCode = "";
		if (StringUtil.equals("company", type)) {
			cipherCode = Coder.encodeURL(AESEncrypt(this.getLoginUser().getCompanyCrmXML(), uuid, "VD393CE-D162-5817-3DB1-1DG8BDC1E888"));
			return url + cipherCode + "&iv=" + uuid;
		} else if (StringUtil.equals("User", type)) {
			cipherCode = Coder.encodeURL(AESEncrypt(this.getLoginUser().getUserCrmXML(), uuid, "VD393CE-D162-5817-3DB1-1DG8BDC1E888"));
			return url + cipherCode + "&iv=" + uuid;
		} else {
			cipherCode = Coder.encodeURL(AESEncrypt(this.getLoginUser().getLoginCrmXML(), uuid, "VD393CE-D162-5817-3DB1-1DG8BDC1E888"));
			return url + cipherCode + "&iv=" + uuid;
		}
	}

	/**
	 * AES加密
	 * @param text
	 * @param iv
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static String AESEncrypt(String text, String iv, String password) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] keyBytes = new byte[16];
		byte[] b = password.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length)
			len = keyBytes.length;
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
		results = new Base64().encode(results);
		return new String(results, "UTF-8");
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public void setCrmService(CrmService crmService) {
		this.crmService = crmService;
	}

}
