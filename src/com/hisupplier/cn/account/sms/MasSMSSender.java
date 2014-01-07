/* 
 * Created by taofeng at 2010-6-22 
 * Copyright HiSupplier.com �ƶ�
 */

package com.hisupplier.cn.account.sms;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamobile.openmas.client.Sms;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.Global;
import com.hisupplier.commons.util.StringUtil;

/**
 * mas���ŷ���(�ƶ��ӿ�)
 * PS����ͬ������Ⱥ��������Message��������number��ʽΪ�ֻ���1,�ֻ���2...
 *     ������oneToManyΪtrue
 * @author taofeng
 */
public class MasSMSSender {

	private static Log log = LogFactory.getLog(MasSMSSender.class);
	private static final String extendCode = "0101"; //�Զ�����չ���루ģ�飩��δ����
	private static final String applicationId = "DefaultApplication"; // Ӧ��ID������������ã�
	private static final String password = "DefaultApplication";      // Ӧ������
	
	private List<Message1> messageList = new ArrayList<Message1>();

	public void add(Message1 message) {
		this.messageList.add(message);
	}

	public void run() {
		int total = this.messageList.size();
		boolean debug = Boolean.getBoolean(Global.DEBUG);
		String debugMobile = System.getProperty(Global.DEBUG + ".mobile");

		if (total <= 0) {
			if (log.isDebugEnabled()) {
				log.debug("message size " + total);
			}
			return;
		}
		try {
			Sms sms = new Sms(getWebServiceURL());
			for (Message1 msg : this.messageList) {
				if (debug && StringUtil.isNotEmpty(debugMobile)) {
					sms.SendMessage(new String[]{debugMobile}, msg.getContent(), extendCode, applicationId, password);
					break;
				}
				if (msg.isOneToMany()) {
					sms.SendMessage(StringUtil.toArray(msg.getNumber(), ","), msg.getContent(), extendCode, applicationId, password);
				} else {
					sms.SendMessage(new String[]{msg.getNumber()}, msg.getContent(), extendCode, applicationId, password);
				}
			}
		} catch (AxisFault e) {
			log.error(e.getMessage(), e);
		} catch (RemoteException e) {
			log.error(e.getMessage(), e);
		} 
	}

	private String getWebServiceURL() {
		String url = Config.getString("sms.url");
		if(StringUtil.isBlank(url) || "sms.url".equalsIgnoreCase(url)){
			url = "http://122.227.163.86:9080/OpenMasService";
		}
		return url;
	}
	
}
