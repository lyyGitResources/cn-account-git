package com.hisupplier.cn.account.entity;

import java.util.Map;

import com.hisupplier.commons.Config;
import com.hisupplier.commons.util.Coder;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.WebUtil;
import com.hisupplier.commons.util.cn.CompanyType;

public class CompanyProfile 
		extends com.hisupplier.commons.entity.cn.CompanyProfile {
	private static final long serialVersionUID = 4419966682056393765L;
	
	private String comName;
	private String currency;
	private boolean foodCompany;
	
	// 食品相关证件
	private String f1ImgPath;
	private String f2ImgPath;
	private String f3ImgPath;
	
	private String regImgPath2;
	private int regImgType; // 1企业  2个人
	private boolean editCeo;
	
	private String[] foodImgArray;
	
	public String getFoodImg() {
		StringBuilder builder = new StringBuilder();
		builder.append(StringUtil.defaultIfEmpty(f1ImgPath, " ")).append(";")
			.append(StringUtil.defaultIfEmpty(f2ImgPath, " ")).append(";")
			.append(StringUtil.defaultIfEmpty(f3ImgPath, " "));
		return Coder.decodeURL(builder.toString());
	}
	
	private void parseFoodImgPath() {
		if (foodImgArray == null && StringUtil.isNotEmpty(getFoodImgPath())) {
			foodImgArray = StringUtil.split(getFoodImgPath(), ";");
			f1ImgPath = foodImgArray[0];
			f2ImgPath = foodImgArray[1];
			f3ImgPath = foodImgArray[2];
		}
		if (foodImgArray == null) { foodImgArray = new String[3]; }
	}
	
	public String getTaxImgSrc() {
		return WebUtil.getFilePath(getTaxImgPath());
	}
	
	public String getF1ImgPath() {
		parseFoodImgPath();
		return f1ImgPath;
	}
	
	public String getF2ImgPath() {
		parseFoodImgPath();
		return f2ImgPath;
	}
	
	public String getF3ImgPath() {
		parseFoodImgPath();
		return f3ImgPath;
	}
	
	public String getF1ImgSrc() {
		parseFoodImgPath();
		return getFilePath(f1ImgPath);
	}
	
	public String getF2ImgSrc() {
		parseFoodImgPath();
		return getFilePath(f2ImgPath); 
	}
	
	public String getF3ImgSrc() {
		parseFoodImgPath();
		return getFilePath(f3ImgPath); 
	}
	
	private String getFilePath(String path) {
		if (StringUtil.isBlank(path)) {
			return Config.getString("img.default");
		}
		return Config.getString("img.fileLink") + path;
	}

	public String getRegCapitalShow() {
		if (StringUtil.isNotBlank(getRegCapital()) && StringUtil.isBlank(currency)) {
			String[] _split = StringUtil.split(getRegCapital(), ":");
			int _size = _split.length;
			setRegCapital(_size > 0 ? _split[0] : "");
			setCurrency(_size > 1 ? _split[1] : "");
		}
		return getRegCapital();
	}
	
	public Map<Integer, String> getComTypes() {
		return CompanyType.INSTANCE.map();
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getCurrency() {
		if (StringUtil.isBlank(currency)) {
			currency = "人民币";
		}
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public boolean isFoodCompany() {
		return foodCompany;
	}

	public void setFoodCompany(boolean foodCompany) {
		this.foodCompany = foodCompany;
	}

	public void setF1ImgPath(String f1ImgPath) {
		this.f1ImgPath = f1ImgPath;
	}

	public void setF2ImgPath(String f2ImgPath) {
		this.f2ImgPath = f2ImgPath;
	}

	public void setF3ImgPath(String f3ImgPath) {
		this.f3ImgPath = f3ImgPath;
	}

	public String getRegImgPath2() {
		return regImgPath2;
	}

	public void setRegImgPath2(String regImgPath2) {
		this.regImgPath2 = regImgPath2;
	}

	public int getRegImgType() {
		return regImgType;
	}

	public void setRegImgType(int regImgType) {
		this.regImgType = regImgType;
	}

	public boolean isEditCeo() {
		return editCeo;
	}

	public void setEditCeo(boolean editCeo) {
		this.editCeo = editCeo;
	}
}
