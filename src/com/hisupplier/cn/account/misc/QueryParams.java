/* 
 * Created by sunhailin at Oct 27, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

/**
 * @author sunhailin
 *
 */
public class QueryParams extends com.hisupplier.cn.account.dao.QueryParams {

	private int voteId;//ͶƱ����id
	private int voteType;//ͶƱ���ͣ�1��ѡ��2��ѡ'
	private String[] optionId;//ͶƱѡ��id
	private String content;//ͶƱ��������

	private int imgId;
	private String imgName;//�޸�ͼƬ����ʱ��
	private int imgType = -1;
	private String table;
	private String field;
	private String imgSrcTag;
	private String imgPathTag;
	private String imgIdTag;
	private boolean select;  // ��ͼƬ�б���ͼƬѡ��
	private boolean batch;//�Ƿ��������ϴ���Ʒ
	
	private int[] commentId;//����id
	private String commentType = "company"; //company, product �������� 

	private String groupName;//��Ƶ��������
	private int groupId = -1;//��Ƶ��id

	private String videoType = "all";//1��"all" ��ʾ���� 2��stateReject,3��Company,4��Product,5��Menu,
	private int videoId;//��Ƶid

	private String telephone;
	private int year;
	private int month;
	private int userId;  // -1��ʾ��ѯ���� 0��ʾ��ѯ�Լ� >0��ʾ�����ѯĳ���˺ŵ��˵�
	private int comId;
	
	private boolean chooseTel;
	private String tel1;
	private String tel2;
	private String mobile;
	
	//PatentDeblocked
	private String tableName;
	private int tableId = -1;
	private int state = -1;

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	public int[] getCommentId() {
		return commentId;
	}

	public void setCommentId(int[] commentId) {
		this.commentId = commentId;
	}

	public int getVoteId() {
		return voteId;
	}

	public void setVoteId(int voteId) {
		this.voteId = voteId;
	}

	public String[] getOptionId() {
		return optionId;
	}

	public void setOptionId(String[] optionId) {
		this.optionId = optionId;
	}

	public int getVoteType() {
		return voteType;
	}

	public void setVoteType(int voteType) {
		this.voteType = voteType;
	}

	public int getImgType() {
		return imgType;
	}

	public void setImgType(int imgType) {
		this.imgType = imgType;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCommentType() {
		return commentType;
	}

	public String getImgSrcTag() {
		return imgSrcTag;
	}

	public void setImgSrcTag(String imgSrcTag) {
		this.imgSrcTag = imgSrcTag;
	}

	public String getImgPathTag() {
		return imgPathTag;
	}

	public void setImgPathTag(String imgPathTag) {
		this.imgPathTag = imgPathTag;
	}

	public String getImgIdTag() {
		return imgIdTag;
	}

	public void setImgIdTag(String imgIdTag) {
		this.imgIdTag = imgIdTag;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public boolean isBatch() {
		return batch;
	}

	public void setBatch(boolean batch) {
		this.batch = batch;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getComId() {
		return comId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public boolean isChooseTel() {
		return chooseTel;
	}

	public void setChooseTel(boolean chooseTel) {
		this.chooseTel = chooseTel;
	}

	public String getTel1() {
		return tel1;
	}

	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
