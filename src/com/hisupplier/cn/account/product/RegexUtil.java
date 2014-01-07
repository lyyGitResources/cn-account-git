package com.hisupplier.cn.account.product;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hisupplier.commons.Config;
/**
 * �ж�ժҪ���������Ƿ��������ַ����email����
 * @param text
 * @return
 */
public class RegexUtil {
	private static Pattern regURL = Pattern.compile("((http[s]?:\\/\\/(www)?)|www\\.)([ \\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?", Pattern.CASE_INSENSITIVE);
	private static Pattern regEmail = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private static String HI_DOMAIN=Config.getString("sys.domain");
	
	public static boolean RegexUrl(String text) {
		Matcher m = regURL.matcher(text);
		while (m.find()) {
			if (m.group().indexOf(HI_DOMAIN) < 0) {
				return false;
			}
		}
		return true;
	}
	public static boolean RegexEmail(String text) {
		Matcher m = regEmail.matcher(text);
		while (m.find()) {
			if (m.group().indexOf(HI_DOMAIN) < 0) {
				return false;
			}
		}
		return true;
	}
}
