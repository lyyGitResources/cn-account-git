/* 
 * Created by sunhailin at Oct 26, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

/**
 * @author sunhailin
 *
 */
public class VoteOption extends com.hisupplier.commons.entity.cn.VoteOption {

	private static final long serialVersionUID = -4886171287603571492L;
	private String percent;
	private int logListSize;

	//	private List<VoteLog> logList;

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public int getLogListSize() {
		return logListSize;
	}

	public void setLogListSize(int logListSize) {
		this.logListSize = logListSize;
	}

}
