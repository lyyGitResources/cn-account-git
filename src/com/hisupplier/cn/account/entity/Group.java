/* 
 * Created by linliuwei at 2009-11-9 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import com.hisupplier.commons.util.StringUtil;

/**
 * @author linliuwei
 */
public class Group extends com.hisupplier.commons.entity.cn.Group {

	private static final long serialVersionUID = 8026114014116192564L;
	private StringBuffer operate;
	private String option; // 每个分组对应的选项
	private String parentName;
	private int itemMaxCount;	// 最大信息数
		
	public String getShowGroupName(){
		return StringUtil.substring(this.getGroupName(),4,"...",false);
	}
	public String getOperate() {
		if(this.operate == null){
			return "";
		}
		return operate.toString();
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}


	public void setOperate(StringBuffer operate) {
		this.operate = operate;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}
	public int getItemMaxCount() {
		return itemMaxCount;
	}
	public void setItemMaxCount(int itemMaxCount) {
		this.itemMaxCount = itemMaxCount;
	}

}
