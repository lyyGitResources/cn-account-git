package com.hisupplier.cn.account.entity;

public class Item {
	private String name;
	private String value;
	private boolean checked;

	public Item(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getChecked() {
		return checked ? "checked" : "";
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
