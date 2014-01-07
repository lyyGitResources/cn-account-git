/* 
 * Created by sunhailin at Oct 29, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import java.util.List;

import com.hisupplier.commons.entity.cn.CommentReply;



/**
 * @author sunhailin
 *
 */
public class Comment extends com.hisupplier.commons.entity.cn.Comment {

	private static final long serialVersionUID = -8778626185506567387L;
	private String proName;
	private String model;
	private String contact;

	private List<CommentReply> replyList;

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public List<CommentReply> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<CommentReply> replyList) {
		this.replyList = replyList;
	}
	
	
}
