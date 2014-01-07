/* 
 * Created by linliuwei at 2009-8-6 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.view;

/**
 * @author linliuwei
 */
public class ViewButton extends Button {

	/**
	 * @param uri
	 */
	public ViewButton(String uri) {
		super(uri);
		this.setName("button.view");
		this.setType(Button.BUTTON);
	}

	/**
	 * @param uri
	 * @param javaScript
	 */
	public ViewButton(String uri, boolean javaScript) {
		this(uri);
		this.setJavaScript(javaScript);
	}
}
