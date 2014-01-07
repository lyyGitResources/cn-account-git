/* 
 * Created by taofeng at 2010-7-6 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.sms;

import java.util.ArrayList;
import java.util.List;

/**
 * @author taofeng
 */
public class SMSFactory {

	private static final String MOBILE_PATTERN = "(13[4-9]|15[0-27-9]|18[278])\\d{8}";// 移动
	public static final String SMS_SIGNATURE = "[中国海商网]";	// 短信签名
	
	/**
	 * 添加单条短信
	 * @param msg
	 */
	public static void addMessage(Message1 msg) {
		List<Message1> msgList = new ArrayList<Message1>(1);
		msgList.add(msg);
		addMessage(msgList);
	}

	/**
	 * 添加短信列表(多对多)
	 * @param messageList
	 * @param oneToMany
	 */
	public static void addMessage(List<Message1> messageList) {
		if (messageList != null && messageList.size() > 0) {
			MasSMSSender smsSender = new MasSMSSender();
			SMSUnicomSender unSmsSender = new SMSUnicomSender();
			for (Message1 msg : messageList) {
				// TODO: 移送短信正常后，修改过来
				if (false && msg.getNumber().matches(MOBILE_PATTERN)) {
					// 移动接口发送带自己的签名，去掉我们加的签名
					if(msg.getContent() != null){
						msg.setContent(msg.getContent().replace(SMS_SIGNATURE, ""));
					}
					smsSender.add(msg);
				} else {
					if(msg.getContent() != null && msg.getContent().indexOf(SMS_SIGNATURE) == -1){
						msg.setContent(msg.getContent() + SMS_SIGNATURE);
					}
					unSmsSender.add(msg);
				}
			}
			smsSender.run();
			unSmsSender.run();
		}
	}

	/**
	 * 短信群发(一对多)
	 * @param comId
	 * @param content
	 * @param mobiles
	 * 
	 */
	public static void addMessage(int comId, String content, List<String> mobiles) {
		if (mobiles.size() == 0)
			return;
		content = MessageUtil.filterSentitiveWord(content);
		Message1 masMessage = new Message1();//移动群发对象
		masMessage.setContent(content);
		masMessage.setOneToMany(true);
		SMSUnicomSender unSmsSender = new SMSUnicomSender();
		for (String mobile : mobiles) {
			Message1 unMessage = new Message1();
			unMessage.setComId(comId);
			
			//区分移动联通号码
			// TODO: 移送短信正常后，修改过来
			if (false && mobile.matches(MOBILE_PATTERN)) {
				masMessage.appendNumber(mobile + ",");
				unMessage.setResult("3");
			} else {
				unMessage.setResult("0");
			}
			unMessage.setMobile(mobile);
			unMessage.setContent(content);
			unSmsSender.add(unMessage);
		}
		unSmsSender.run();
		
		if (masMessage.getNumber().length() > 0) {
			MasSMSSender smsSender = new MasSMSSender();
			smsSender.add(masMessage);
			smsSender.run();
		}
	}
}
