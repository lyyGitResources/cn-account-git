/**
 * Created by wuyaohui at 2013-1-21 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hisupplier.commons.entity.cn.WebSite;
import com.hisupplier.commons.util.StringUtil;

/**
 * @author wuyaohui
 * 
 */
public class Talk implements java.io.Serializable {
	private static final long serialVersionUID = 2690993992954888967L;
	
	private final static Pattern BIGQQ_CODE = Pattern.compile("^(800|400)\\d{6}$");
	private final static Pattern QQ_CODE = Pattern.compile("^\\d{5,11}$");
	
	private final static Pattern OLD_QQ_FILTER = Pattern.compile("(\\d+?)\\((\\w+\\|)*qq(\\|\\w+)*\\)", Pattern.CASE_INSENSITIVE);

	public final static int
			QQ = 1,			// 普通QQ
			BIGQQ = 2;		// 企业QQ

	private String id;
	private int comId;
	private int userId;
	private String name;
	private String code;
	private int type;
	private boolean show;
	
	public static void oldData(WebSite webSite, List<Talk> talks) {
		if (webSite != null && StringUtil.isNotBlank(webSite.getChatUserId())) {
			Matcher matcher = OLD_QQ_FILTER.matcher(webSite.getChatUserId());
			Set<Integer> userId = new HashSet<Integer>();
			int tmpId = 0;
			while(matcher.find()) {
				tmpId = StringUtil.toInt(matcher.group(1), 0);
				if (tmpId > 0) userId.add(tmpId);
			}
			
			if (!userId.isEmpty()) {
				for (Talk talk : talks) {
					if (userId.contains(talk.getUserId())) {
						talk.setShow(true);
					}
				}
			}
		}
	}
	
	public String getReview() {
		StringBuilder builder = new StringBuilder();
		if (StringUtil.isNotBlank(code)) {
			switch (type) {
			case QQ:
				builder.append("<a rel=\"nofollow\" target=\"_blank\" href=\"http://wpa.qq.com/msgrd?v=3&uin=")
				.append(code).append("&site=qq&menu=yes\"><img border=\"0\" src=\"http://wpa.qq.com/pa?p=2:")
				.append(code).append(":51\" alt=\"点击这里给我发消息\" title=\"点击这里给我发消息\"></a>");
				break;
			case BIGQQ:
				builder.append("<img style=\"CURSOR: pointer\"")
				.append(" onclick=\"javascript:window.open('http://b.qq.com/webc.htm?new=0&sid=")
				.append(code).append("&o=&q=7&ref='+document.location, '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no');\"")
				.append(" border=\"0\" src=\"http://im.bizapp.qq.com:8000/zx_qq.gif\">");
				break;
			}
		}
		return builder.toString();
	}
	
	public boolean isValidate() {
		if (StringUtil.isEmpty(name) || StringUtil.isEmpty(code)
				|| StringUtil.length(name) > 20) {
			return false;
		}
		switch (type) {
		case QQ:
			if (!QQ_CODE.matcher(code).matches()) {
				return false;
			}
			break;
		case BIGQQ:
			if (!BIGQQ_CODE.matcher(code).matches()) {
				return false;
			}
			break;
		}
		return true;
	}
	
	public String generateId(int order) {
		return comId + "#" + userId + "#" + type + "#" + order;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getComId() {
		return comId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}
}
