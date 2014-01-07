/* 
 * Created by linliuwei at 2009-10-27 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.inquiry;

/**
 * @author linliuwei
 */
public class QueryParams extends com.hisupplier.cn.account.dao.QueryParams {

	private int[] inqId;//ѯ��ID
	private int userId = -1;//��������
	private int state = -1;
	private String countryCode; //ʡ�ݴ���
	private int read = -1;
	private int recommend = -1;
	
	private int id = -1;//ѯ�̻ظ�ID

	private String[] month;//ѯ�������·�

	private int inquiryReceive = -1;//ѯ�̽��շ�ʽ
	private int receiveRecommend = -1;//�Ƿ�����Ƽ�ѯ��

	public int[] getInqId() {
		return inqId;
	}

	public void setInqId(int[] inqId) {
		this.inqId = inqId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInquiryReceive() {
		return inquiryReceive;
	}

	public void setInquiryReceive(int inquiryReceive) {
		this.inquiryReceive = inquiryReceive;
	}

	public int getReceiveRecommend() {
		return receiveRecommend;
	}

	public void setReceiveRecommend(int receiveRecommend) {
		this.receiveRecommend = receiveRecommend;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String[] getMonth() {
		return month;
	}

	public void setMonth(String[] month) {
		this.month = month;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

}
