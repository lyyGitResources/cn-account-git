/* 
 * Created by taofeng at Dec 25, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import java.io.Serializable;

/**
 * @author taofeng
 *
 */
public class WebSiteModule implements Serializable {

	private static final long serialVersionUID = 1255961867747573600L;

	private int modId;
	private String modName;
	private int comId;
	private int containerNo;
	private int orderNo;
	private String createTime;

	public int getModId() {
		return modId;
	}

	public void setModId(int modId) {
		this.modId = modId;
	}

	public String getModName() {
		return modName;
	}

	public void setModName(String modName) {
		this.modName = modName;
	}

	public int getComId() {
		return comId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public int getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(int containerNo) {
		this.containerNo = containerNo;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
