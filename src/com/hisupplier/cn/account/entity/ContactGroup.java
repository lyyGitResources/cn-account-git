package com.hisupplier.cn.account.entity;

import java.util.List;


public class ContactGroup extends com.hisupplier.commons.entity.ContactGroup {
	private static final long serialVersionUID = -355480989841180101L;
	
	private List<Contact> contactList;

	public ContactGroup clone(){
		ContactGroup group = new ContactGroup();
		group.setComId(this.getComId());
		group.setGroupId(this.getGroupId());
		group.setGroupName(this.getGroupName());
		group.setCreateTime(this.getCreateTime());
		group.setModifyTime(this.getModifyTime());
		return group;
	}
	
	public List<Contact> getContactList() {
		return contactList;
	}

	public void setContactList(List<Contact> contactList) {
		this.contactList = contactList;
	}

}
