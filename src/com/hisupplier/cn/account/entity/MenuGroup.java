package com.hisupplier.cn.account.entity;

import com.hisupplier.commons.entity.Entity;

public class MenuGroup extends Entity {
	private static final long serialVersionUID = -8353840079040821606L;
	private int groupId;
	private String groupName;
	private int comId;
	private boolean fix;
	private boolean show;
	private boolean showDate;
	private int listStyle; //排列方式，1：竖排，2：横排',
	private int listOrder;
	private int menuCount;
	private int menuRejectCount;
	private int state;// '0标记删除，15等待审核，20审核通过',
	private String createTime;
	private String modifyTime;
	
	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getComId() {
		return comId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public boolean isFix() {
		return fix;
	}

	public void setFix(boolean fix) {
		this.fix = fix;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isShowDate() {
		return showDate;
	}

	public void setShowDate(boolean showDate) {
		this.showDate = showDate;
	}

	public int getListStyle() {
		return listStyle;
	}

	public void setListStyle(int listStyle) {
		this.listStyle = listStyle;
	}

	public int getListOrder() {
		return listOrder;
	}

	public void setListOrder(int listOrder) {
		this.listOrder = listOrder;
	}

	public int getMenuCount() {
		return menuCount;
	}

	public void setMenuCount(int menuCount) {
		this.menuCount = menuCount;
	}

	public int getMenuRejectCount() {
		return menuRejectCount;
	}

	public void setMenuRejectCount(int menuRejectCount) {
		this.menuRejectCount = menuRejectCount;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
}
