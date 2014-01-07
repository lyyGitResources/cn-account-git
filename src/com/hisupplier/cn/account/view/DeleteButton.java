package com.hisupplier.cn.account.view;

public class DeleteButton extends Button {
	/**
	 * @param uri
	 */
	public DeleteButton(String uri) {
		super(uri);
		this.setName("button.delete");
		this.setType(Button.BUTTON);
	}

	/**
	 * @param uri
	 * @param javaScript
	 */
	public DeleteButton(String uri, boolean javaScript) {
		this(uri);
		this.setJavaScript(javaScript);
	}

}
