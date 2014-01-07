/* 
 * Created by linliuwei at 2009-11-9 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

/**
 * @author linliuwei
 */
public class QueryParams extends com.hisupplier.cn.account.dao.QueryParams {

	private boolean showReject;
	private int[] proId;
	private int tmpProdId;
	private int newProId; // 加密产品传给产品的ID
	private int copyProId; // 产品传给商情的ID
	private int userId = 0;
	private int userIdTmp; // 页面保存的userId，用于产品，商情等列表分派时需保存的userId
	private int catId;
	private int listOrder; // 用户想把产品放到的位置
	private int oldOrder; // 只展台产品使用
	private int groupId = -1;
	private int state = -1; // 大于等于0：state = #state#，-1：state in (10,15,20) -2:state in (15,20)
	private int feature = -1; // 0非展台产品 >0展台产品
	private int proType = -1; // 1供应商情 2求购商情 3过期商情
	private int optimize = -1; // 1优化产品 2普通产品

	private String newProPass;
	private String newProPassExpiry;
	private String newProMenuName;

	private int parentId;
	private String parentName;
	private String groupName;
	private boolean showNewIco;

	private int newGroupId;
	private int[] groupIds; //分组排序组ID

	private String keyword1;
	private String keyword2;
	private String keyword3;
	
	private int videoId;//产品视频
	private int comId;
	private int resultType = 0;// 0为添加     1为修改

	public boolean isShowReject() {
		return showReject;
	}

	public void setShowReject(boolean showReject) {
		this.showReject = showReject;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getOptimize() {
		return optimize;
	}

	public void setOptimize(int optimize) {
		this.optimize = optimize;
	}

	public int[] getProId() {
		return proId;
	}

	public void setProId(int[] proId) {
		this.proId = proId;
	}

	public int getListOrder() {
		return listOrder;
	}

	public void setListOrder(int listOrder) {
		this.listOrder = listOrder;
	}

	public String getNewProPass() {
		return newProPass;
	}

	public void setNewProPass(String newProPass) {
		this.newProPass = newProPass;
	}

	public String getNewProPassExpiry() {
		return newProPassExpiry;
	}

	public void setNewProPassExpiry(String newProPassExpiry) {
		this.newProPassExpiry = newProPassExpiry;
	}

	public String getNewProMenuName() {
		return newProMenuName;
	}

	public void setNewProMenuName(String newProMenuName) {
		this.newProMenuName = newProMenuName;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int[] getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(int[] groupIds) {
		this.groupIds = groupIds;
	}

	public int getFeature() {
		return feature;
	}

	public void setFeature(int feature) {
		this.feature = feature;
	}

	public int getNewGroupId() {
		return newGroupId;
	}

	public void setNewGroupId(int newGroupId) {
		this.newGroupId = newGroupId;
	}

	public int getCopyProId() {
		return copyProId;
	}

	public void setCopyProId(int copyProId) {
		this.copyProId = copyProId;
	}

	public int getProType() {
		return proType;
	}

	public void setProType(int proType) {
		this.proType = proType;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getKeyword1() {
		return keyword1;
	}

	public void setKeyword1(String keyword1) {
		this.keyword1 = keyword1;
	}

	public String getKeyword2() {
		return keyword2;
	}

	public void setKeyword2(String keyword2) {
		this.keyword2 = keyword2;
	}

	public String getKeyword3() {
		return keyword3;
	}

	public void setKeyword3(String keyword3) {
		this.keyword3 = keyword3;
	}

	public int getUserIdTmp() {
		return userIdTmp;
	}

	public void setUserIdTmp(int userIdTmp) {
		this.userIdTmp = userIdTmp;
	}

	public int getNewProId() {
		return newProId;
	}

	public void setNewProId(int newProId) {
		this.newProId = newProId;
	}

	public int getOldOrder() {
		return oldOrder;
	}

	public void setOldOrder(int oldOrder) {
		this.oldOrder = oldOrder;
	}

	public boolean isShowNewIco() {
		return showNewIco;
	}

	public void setShowNewIco(boolean showNewIco) {
		this.showNewIco = showNewIco;
	}

	public int getTmpProdId() {
		return tmpProdId;
	}

	public void setTmpProdId(int tmpProdId) {
		this.tmpProdId = tmpProdId;
	}

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	public int getComId() {
		return comId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public int getResultType() {
		return resultType;
	}

	public void setResultType(int resultType) {
		this.resultType = resultType;
	}
}
