/* 
 * Created by linliuwei at 2009-8-6 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.view;

/**
 * @author linliuwei
 */
public class ResetButton extends Button {

	public static final ResetButton RESET_BUTTON = new ResetButton();

	public ResetButton() {
		this.setName("button.reset");
		this.setType(Button.RESET);
	}

}
