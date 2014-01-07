/* 
 * Created by linliuwei at 2009-8-6 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.view;

/**
 * @author linliuwei
 */
public class SubmitButton extends Button {

	/**
	 * 
	 */
	public SubmitButton() {
		super();
		this.setName("button.submit");
		this.setType(Button.SUBMIT);
	}

	/**
	 * @param uri
	 */
	public SubmitButton(String uri) {
		super(uri);
		this.setName("button.submit");
		this.setType(Button.SUBMIT);
	}

	/**
	 * @param uri
	 * @param javaScript
	 */
	public SubmitButton(String uri, boolean javaScript) {
		this(uri);
		this.setJavaScript(javaScript);
	}
}
