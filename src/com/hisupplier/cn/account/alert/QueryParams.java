/* 
 * Created by linliuwei at 2009-10-27 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.alert;

/**
 * @author linliuwei
 */
public class QueryParams extends com.hisupplier.cn.account.dao.QueryParams {

	private int id; //����ҳ�洫ֵ
	private boolean enable;//����ҳ�洫ֵ

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
