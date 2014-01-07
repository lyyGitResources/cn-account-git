package com.hisupplier.cn.account.basic;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.xml.DOMConfigurator;

import com.hisupplier.cn.account.sms.MessageUtil;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.character.Gb2Big5;
import com.hisupplier.commons.jdbc.JdbcUtilFactory;
import com.hisupplier.commons.mail.InquiryMailSenderFactory;
import com.hisupplier.commons.mail.MailSenderCNFactory;
import com.hisupplier.commons.mail.MailTemplate;
import com.hisupplier.commons.task.TaskExecutor;
import com.hisupplier.commons.util.CategoryUtil;
import com.hisupplier.commons.util.StringUtil;

/**
 * 用后后台启动初始化
 *
 */
public class BootListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		TaskExecutor.destroy();
	}

	static {
		DOMConfigurator.configure(BootListener.class.getResource("/log4j.xml"));
	}

	public void contextInitialized(ServletContextEvent event) {
		try {
			JdbcUtilFactory.init(CN.getDataSource());
			CN.initMemberType();
			TaskExecutor.init("cn-account");
			
			MailSenderCNFactory.init();
			InquiryMailSenderFactory.init();
			
			Config.init("account");
			CategoryUtil.init();
//			DomainAreaUtil.init();
			MessageUtil.init();
			String isBig5 = event.getServletContext().getInitParameter("isBig5");
			Config.setString("isBig5", isBig5);
			if(Boolean.getBoolean(CN.DEBUG)){
				Config.setString("memcache.server", "192.168.1.8:11211");
			}
			Config.setString("memcache2.server", Config.getString("memcache.server"));
			if(StringUtil.equalsIgnoreCase("true", isBig5)){
				String path = event.getServletContext().getRealPath("WEB-INF\\classes");
				Gb2Big5.init(path+"/gb2312.txt",path+"/big5.txt");
				Config.setString("cas.base", "http://my."+Config.getString("sys.domain")+"/big5");
			}
			MailTemplate.init(event.getServletContext().getRealPath("/page/template/mail"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
