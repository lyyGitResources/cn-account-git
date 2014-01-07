/* 
 * Created by linliuwei at 2009-10-27 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.menu;

/**
 * @author linliuwei
 */
public class QueryParams extends com.hisupplier.cn.account.dao.QueryParams {

	private int state = -1;//状态
	private int groupId;
	private int menuId;
	private int[] groupIds;//栏目排序的id
	private int[] menuIds;//信息排序的id
	private int videoId;
	

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int[] getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(int[] groupIds) {
		this.groupIds = groupIds;
	}

	public int[] getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(int[] menuIds) {
		this.menuIds = menuIds;
	}

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}


	

}
