/* 
 * Created by taofeng at 2010-7-6 
 * Copyright HiSupplier.com ÁªÍ¨
 */

package com.hisupplier.cn.account.sms;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisupplier.commons.Global;
import com.hisupplier.commons.jdbc.JdbcUtil;

public class SMSUnicomSender {
	private static Log log = LogFactory.getLog(SMSUnicomSender.class);
	private List<Message1> messageList = new ArrayList<Message1>();

	public void add(Message1 message1) {
		this.messageList.add(message1);
	}

	public void run() {
		int total = this.messageList.size();
		int count = 0;
		if (total <= 0) {
			if (log.isDebugEnabled()) {
				log.debug("message size " + total);
			}
			return;
		}
		boolean debug = Boolean.getBoolean(Global.DEBUG);
		String debugMobile = System.getProperty(Global.DEBUG + ".mobile");
		
		JdbcUtil jw = null;
		try {
			jw = new JdbcUtil(MessageUtil.getSms());
			jw.begin();
			jw.createPreparedStatement("insert into sms_send (comId,mobile,content,result,sms_number,priority,plan_time) values(?,?,?,?,?,?,?)");
			for (Message1 msg : this.messageList) {
				count++;
				jw.setInt(1, msg.getComId());
				jw.setString(2, debug ? debugMobile : msg.getMobile());
				jw.setString(3, msg.getContent());
				jw.setString(4, msg.getResult());
				jw.setString(5, "1234");
				jw.setInt(6, 9);
				jw.setString(7, "");
				jw.preAddBatch();
				if (count % 25 == 0) {
					jw.preExecuteBatch();
				}
			}
			jw.preExecuteBatch();
			jw.commit();
		} catch (Exception e) {
			log.error("", e);
			jw.rollback();
		} finally {
			if (jw != null) {
				jw.close();
			}
			if (log.isInfoEnabled()) {
				log.info("total: " + total + ", send: " + count);
			}
		}
	}
}
