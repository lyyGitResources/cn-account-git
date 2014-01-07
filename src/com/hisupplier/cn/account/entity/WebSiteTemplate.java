package com.hisupplier.cn.account.entity;


public class WebSiteTemplate extends com.hisupplier.commons.entity.cn.WebSiteTemplate {
	private static final long serialVersionUID = 1L;
	
	private int pageSize = 5;
	private int bannerNum;
	/**
	 * ���㵱ǰģ�����ڵ�ҳ��
	 * @param index ��1��ʼ
	 * @return int
	 */
	public int getPageNo(int index) {
		int tmp = (Integer) index / pageSize;
		int pageNo = 0;
		if (index % pageSize == 0) {
			pageNo = tmp;
		} else {
			pageNo = tmp + 1;
		}
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getBannerNum() {
		return bannerNum;
	}

	public void setBannerNum(int bannerNum) {
		this.bannerNum = bannerNum;
	}

}
