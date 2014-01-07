/* 
 * Created by taofeng at Feb 4, 2010 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hisupplier.commons.util.StringUtil;

/**
 * @author taofeng
 *
 */
public class ChatItem {

	private int userId;
	private boolean tq;
	private boolean qq;
	private boolean msn;

	/**
	 * 组合聊天项目格式为userId(tq|qq)
	 * @param array 格式userId_tq,userId_qq,userId_msn
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public static String toCompose(String[] array) {
		String text = "";
		Map<String, StringBuilder> tempMap = new HashMap<String, StringBuilder>();
		if (array.length > 0) {
			for (String str : array) {
				String[] temp = StringUtil.toArray(str.trim(), "_");
				if (temp.length == 2) {
					if (tempMap.get(temp[0]) == null) {
						tempMap.put(temp[0], new StringBuilder(temp[1].trim()+"|"));
					} else {
						StringBuilder value = tempMap.get(temp[0]);
						if (temp[1].trim().toLowerCase().equals("tq")) {
							value.append("tq|");
						} else if (temp[1].trim().toLowerCase().equals("qq")) {
							value.append("qq|");
						} else if (temp[1].trim().toLowerCase().equals("msn")) {
							value.append("msn|");
						}
					}
				}
			}
			Iterator<?> iter = tempMap.entrySet().iterator();
			StringBuilder chat = new StringBuilder();
			while (iter.hasNext()) {
			    Map.Entry<String,StringBuilder> entry = (Map.Entry<String,StringBuilder>) iter.next();
			    StringBuilder value = entry.getValue();
			    value.deleteCharAt(value.length()-1);
			    chat.append(entry.getKey()).append("(").append(value).append("),");
			} 
			if(chat.length()>0){
				chat.deleteCharAt(chat.length()-1);
				text = chat.toString();
			}
		}
		return text;
	}

	/**
	 * 拆分
	 * @param str 格式userId(tq|qq)
	 */
	public void toSplit(String str) {
		if (StringUtil.isNotEmpty(str)) {
			Pattern pattern = Pattern.compile("^([1-9]\\d*)\\((.+)\\)|([1-9]\\d*)$", Pattern.CASE_INSENSITIVE);
			Matcher matched = pattern.matcher(str);
			if (matched.matches()) {
				if (matched.group(3) != null) {
					this.userId = Integer.parseInt(matched.group(3));
				} else {
					this.userId = Integer.parseInt(matched.group(1));
					String[] tmp = StringUtil.toArray(matched.group(2), "|");
					for (String t : tmp) {
						if (t.toLowerCase().equals("tq")) {
							this.tq = true;
						} else if (t.toLowerCase().equals("qq")) {
							this.qq = true;
						} else if (t.toLowerCase().equals("msn")) {
							this.msn = true;
						}
					}
				}
			}
		}
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean isTq() {
		return tq;
	}

	public void setTq(boolean tq) {
		this.tq = tq;
	}

	public boolean isQq() {
		return qq;
	}

	public void setQq(boolean qq) {
		this.qq = qq;
	}

	public boolean isMsn() {
		return msn;
	}

	public void setMsn(boolean msn) {
		this.msn = msn;
	}

}
