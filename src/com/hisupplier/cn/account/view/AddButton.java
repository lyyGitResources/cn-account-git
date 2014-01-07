/* 
 * Created by linliuwei at 2009-8-6 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.view;

/**
 * @author linliuwei
 */
public class AddButton extends Button {

	/**
	 * @param uri
	 */
	public AddButton(String uri) {
		super(uri);
		this.setName("button.add");
		this.setType(Button.BUTTON);
	}

	/**
	 * @param uri
	 * @param javaScript
	 */
	public AddButton(String uri, boolean javaScript) {
		this(uri);
		this.setJavaScript(javaScript);
	}
}
