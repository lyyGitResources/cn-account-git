/* 
 * Created by wuyaohui at 2011-8-2
 * Copyright HiSupplier.com 
 */
package com.hisupplier.cn.account.entity;

/**
 * @author wuyaohui
 *
 */
public class LikeRecord extends com.hisupplier.commons.entity.cn.LikeRecord {
	private int count; // ×ÜÔŞÊı
	private boolean liked; // ÊÇ·ñÔŞ

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean getLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

}
