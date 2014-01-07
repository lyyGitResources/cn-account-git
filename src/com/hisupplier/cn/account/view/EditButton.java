/* 
 * Created by linliuwei at 2009-8-6 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.view;

/**
 * @author linliuwei
 */
public class EditButton extends Button {

	/**
	 * @param uri
	 */
	public EditButton(String uri) {
		super(uri);
		this.setName("button.edit");
		this.setType(Button.BUTTON);
	}

	/**
	 * @param uri
	 * @param javaScript
	 */
	public EditButton(String uri, boolean javaScript) {
		this(uri);
		this.setJavaScript(javaScript);
	}
}
