/* 
 * Created by taofeng at Nov 2, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.sms;

import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisupplier.commons.jdbc.JdbcUtil;
import com.hisupplier.commons.util.StringUtil;

/**
 * @author taofeng
 *
 */
public class MessageUtil {

	private static final Log log = LogFactory.getLog(MessageUtil.class);
	private static Pattern sentitiveWordPattern;
	private static DataSource sms;

	/**
	 * <p>敏感词汇、短信发送数据源初始化,中英文版共用
	 * <p>PS：要先初始化JdbcUtilFactory
	 */
	public static void init() {
		StringBuilder regex = new StringBuilder();
		JdbcUtil jdbc = null;
		try {
			sms = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/hisupplier_sms");
			jdbc = new JdbcUtil(sms);
			jdbc.query("select word from MessageSensitive");
			while (jdbc.resultNext()) {
				String word = StringUtil.replaceAll(jdbc.getString("word"), Pattern.compile("\\|{1,}", Pattern.CASE_INSENSITIVE), "\\\\\\\\|");
				regex.append(word).append("|");
			}
			if (regex.length() > 0) {
				regex.deleteCharAt(regex.length() - 1);
				sentitiveWordPattern = Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE);
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if(jdbc != null){
				jdbc.close();
			}
		}
	}

	/**
	 * <p>过滤短信中的敏感词汇
	 * @param content
	 * @return String
	 */
	public static String filterSentitiveWord(String content) {
		if (sentitiveWordPattern == null) {
			return content;
		}
		return StringUtil.replaceAll(content, sentitiveWordPattern, "...");
	}

	public static DataSource getSms() {
		return sms;
	}

	public static void setSms(DataSource sms) {
		MessageUtil.sms = sms;
	}

}
