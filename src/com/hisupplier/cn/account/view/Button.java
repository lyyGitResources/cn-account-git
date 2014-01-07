/* 
 * Created by linliuwei at 2009-6-28 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.view;

import java.util.Map;
import java.util.Map.Entry;

import com.hisupplier.commons.util.Coder;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;

/**
 * ��ť
 * @author linliuwei
 */
public class Button {

	public static final String SUBMIT = "submit";
	public static final String RESET = "reset";
	public static final String BUTTON = "button";
	public static final String LINK = "link";

	private String uri = "#"; //��ַ·��
	private String name = ""; //��ť��ʾ����
	private String type = BUTTON; // ��ť����
	private String styleClass = "button";//��ʽ��
	private String styleId = ""; //��ʽID
	private boolean javaScript = false;
	
	/**
	 * 
	 */
	public Button() {
		super();
	}

	/**
	 * @param uri
	 */
	public Button(String uri) {
		super();
		this.uri = uri;
	}

	/**
	 * @param uri
	 * @param javaScript
	 */
	public Button(String uri, boolean javaScript) {
		super();
		this.uri = uri;
		this.javaScript = javaScript;
	}

	/**
	 * @param uri
	 * @param name
	 */
	public Button(String uri, String name) {
		super();

		this.uri = uri;
		this.name = name;
	}

	/**
	 * @param uri
	 * @param name
	 * @param styleId
	 */
	public Button(String uri, String name, String styleId) {
		super();
		this.uri = uri;
		this.name = name;
		this.styleId = styleId;
	}

	@Override
	public Button clone() {
		Button button = new Button();
		button.uri = new String(this.uri);
		button.name = new String(this.name);
		button.type = new String(this.type);
		button.styleClass = new String(this.styleClass);
		button.styleId = new String(this.styleId);
		button.javaScript = this.javaScript;
		return button;
	}

	/**
	 * ׷�Ӳ���
	 * @param name
	 * @param value
	 * @return
	 */
	public Button appendParam(String name, String value) {
		if (this.uri.indexOf("?") == -1) {
			this.uri += "?" + name + "=" + Coder.encodeURL(value);
		} else {
			this.uri += "&" + name + "=" + Coder.encodeURL(value);
		}
		return this;
	}

	/**
	 * ׷�Ӳ���
	 * @param name
	 * @param value
	 * @return
	 */
	public Button appendParam(String name, int value) {
		if (this.uri.indexOf("?") == -1) {
			this.uri += "?" + name + "=" + value;
		} else {
			this.uri += "&" + name + "=" + value;
		}
		return this;
	}

	/**
	 * ����������ʽ
	 * @return
	 */
	public String getLink() {
		return getLink(null);
	}

	/**
	 * ����������ʽ
	 * @param buttonParamMap
	 * <pre>
	 * �������ɵİ�ťΪ��<button param="abc" />
	 * buttonParamMap {
	 * 	key: "param",
	 * 	value: "abc"
	 * }
	 * </pre>
	 * @return
	 */
	public String getLink(Map<String, String> buttonParamMap) {
		StringBuffer buff = new StringBuffer();
		if (StringUtil.isNotEmpty(styleId)) {
			buff.append("<a href=\"javascript:void(0);\" id=\"").append(styleId).append("\"");
		} else if (this.javaScript) {
			buff.append("<a href=\"javascript:").append(uri).append("\"");
		} else {
			buff.append("<a href=\"").append(uri).append("\"");
		}

		if (StringUtil.isNotEmpty(styleClass) && !StringUtil.equals(styleClass, "button")) {
			buff.append(" class=\"").append(styleClass).append("\"");
		}

		if (buttonParamMap != null && buttonParamMap.size() > 0) {
			for(Entry<String, String> e: buttonParamMap.entrySet()){
				buff.append(" ").append(e.getKey()).append("=\"").append(e.getValue()).append("\"");
			}
		}
		
		//		if (StringUtil.isNotEmpty(title)) {
		//			buff.append(" title=\"").append(title).append("\"");
		//		}
		buff.append(">").append(TextUtil.getText(name, "zh")).append("</a>");
		return buff.toString();
	}

	/**
	 * ���ذ�ť��ʽ
	 * @return
	 */
	public String getButton() {
		return getButton(null);
	}
	
	/**
	 * ���ذ�ť��ʽ
	 * @return
	 */
	public String getButton(Map<String, String> buttonParamMap) {

		StringBuffer buff = new StringBuffer();
		if (StringUtil.equalsIgnoreCase(type, RESET)) {
			buff.append("<input type=\"reset\"");
		} else if (StringUtil.equalsIgnoreCase(type, SUBMIT)) {
			buff.append("<input type=\"submit\"");
		} else if (StringUtil.equalsIgnoreCase(type, LINK)) {
			return this.getLink();
		} else {
			buff.append("<input type=\"button\"");
			if (StringUtil.isNotEmpty(styleId)) {
				buff.append(" id=\"").append(styleId).append("\"");
			} else if (this.javaScript) {
				buff.append(" onclick=\"").append(uri).append("\"");
			} else {
				buff.append(" onclick=\"location.href='").append(uri).append("'\"");
			}
		}
		if (StringUtil.isNotEmpty(styleClass)) {
			buff.append(" class=\"").append(styleClass).append("\"");
		}
		if (buttonParamMap != null && buttonParamMap.size() > 0) {
			for(Entry<String, String> e: buttonParamMap.entrySet()){
				buff.append(" ").append(e.getKey()).append("=\"").append(e.getValue()).append("\"");
			}
		}
		//		if (StringUtil.isNotEmpty(title)) {
		//			buff.append(" title=\"").append(getText(title)).append("\"");
		//		}
		buff.append(" value=\"").append(TextUtil.getText(name, "zh")).append("\"").append("/>");
		return buff.toString();
	}

	public Button setUri(String uri) {
		this.uri = uri;
		return this;
	}

	public Button setName(String name) {
		this.name = name;
		return this;
	}

	public Button setType(String type) {
		this.type = type;
		return this;
	}

	public Button setStyleClass(String styleClass) {
		this.styleClass = styleClass;
		return this;
	}

	public Button setStyleId(String styleId) {
		this.styleId = styleId;
		return this;
	}

	public Button setJavaScript(boolean javaScript) {
		this.javaScript = javaScript;
		return this;
	}

	public String getType() {
		return this.type;
	}

	public String getStyleClass() {
		return this.styleClass;
	}

	public String getStyleId() {
		return this.styleId;
	}

	public boolean isJavaScript() {
		return this.javaScript;
	}

	public String getUri() {
		return this.uri;
	}

	public String getName() {
		return this.name;
	}
}
