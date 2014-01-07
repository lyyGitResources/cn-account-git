/* 
 * Created by linliuwei at 2009-10-27 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.alert;

/**
 * @author linliuwei
 */
public class QueryParams extends com.hisupplier.cn.account.dao.QueryParams {

	private int id; //用于页面传值
	private boolean enable;//用于页面传值

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	
}
