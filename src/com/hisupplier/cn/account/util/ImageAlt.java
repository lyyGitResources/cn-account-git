/**
 * Created by wuyaohui at 2013-4-15 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hisupplier.commons.util.StringUtil;

/**
 * @author wuyaohui
 *
 */
public class ImageAlt {
	private final static Pattern IMG_TAG = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>", Pattern.CASE_INSENSITIVE);

	public static String path(String text, String alt) {
		String result = "";
		if (StringUtil.isNotBlank(text)) {
			
			Matcher imgMatcher = IMG_TAG.matcher(text);
			StringBuffer sb = new StringBuffer();
			String path = "";
			while (imgMatcher.find()) {
				path = imgMatcher.group(1);
				if (StringUtil.isNotBlank(path)) {
					// 过滤地址中的 $
					path = Matcher.quoteReplacement(path);
					path = "<img align=\"absmiddle\" src=\"" + path + "\" alt=\"" + alt + "\">";
					imgMatcher.appendReplacement(sb, path);
				}
			}
			imgMatcher.appendTail(sb);
			if (sb.length() > 0) {
				result = StringUtil.filterHTML(sb.toString());
			} else {
				result = text;
			}
		}
		return result;
	}
}
