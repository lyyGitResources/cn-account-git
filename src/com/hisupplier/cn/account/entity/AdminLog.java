package com.hisupplier.cn.account.entity;


public class AdminLog  {

	private int id;
	private int adminId;
	private int operType;
	private int tableId;
	private String tableName;
	private String remark;
	private String createTime;


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAdminId() {
		return this.adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public int getTableId() {
		return this.tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public int getOperType() {
		return this.operType;
	}

	public void setOperType(int operType) {
		this.operType = operType;
	}

}
