package com.hisupplier.cn.account.entity;

import java.util.ArrayList;
import java.util.List;

import com.hisupplier.commons.CN;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.util.StringUtil;

public class PatentDeblocked {
	public static final int IMG_MAX = 15; // 营业执照 、 凭证 允许上传的最大数量 
	private int id;
	private int comId;
	private String keywords; // 违规词 多个违规词用"，"分隔
	private String remark; // 备注
	private int state; // 0标记删除，10审核失败，15等待审核，20审核成功
	private String cause; // 审核失败的原因'
	private String regImgPath; // 营业执照和有效证件图片 最多3张图片 用逗号分隔
	private String createTime;
	private String modifyTime;
	private String[] patentImgs; //表单提交时 存储多个图片的地址   处理后以逗号分隔存在 regImgPath中
	private String imgIds; //对应Image表里的imgId 多个用逗号分隔   最多3个
	private String[] imgIdsTag; //违规词解禁页面 隐藏域里存放的 图片的imgId 用于向数据插入数据时 使用

	public String getStateName() {
		String showTypeName = "";
		if (this.getState() == CN.STATE_PASS) {
			showTypeName = "审核通过";
		} else if (this.getState() == CN.STATE_WAIT) {
			showTypeName = "等待审核";
		} else if (this.getState() == CN.STATE_REJECT) {
			StringBuilder st = new StringBuilder();
			st.append("审核不通过 <a href=\"JavaScript:causeShow('").append(this.cause).append("')\" >(原因)").append("</a>&nbsp;&nbsp;");
			showTypeName = st.toString();
		}
		return showTypeName;
	}

	public String getLicense() { //显示营业执照或凭证 并加入事件 单击后可查看图片
		String[] patentImages = this.getRegImgPath().split(",");
		StringBuilder showText = new StringBuilder();
		int index = 1;
		String imgLink = Config.getString("img.link");
		for(String imgPath : patentImages) { //注意 <a href=\" 双引号转义  
			showText.append("<a href=\"JavaScript:licenseShow('").append(imgLink).append(imgPath).append("')\" >凭证").append(index)
			.append("</a>&nbsp;&nbsp;");
			index++;
		}
		return showText.toString();
	}

	public List<String> getLicenseArray() {
		List<String> list = new ArrayList<String>();
		String[] patentImages = this.getRegImgPath().split(",");
		for(String imgPath : patentImages) {
			list.add(imgPath);
		}
		return list;
	}

	public String getFormImgs() {
		if (this.patentImgs != null) {
			StringBuilder path = new StringBuilder();
			for (String imgPath : this.patentImgs) {
				path.append(imgPath).append(",");
			}
			return StringUtil.trimComma(path.toString());
		}
		return null;
	}
	
	public String getFormImgIds() {
		if (this.imgIdsTag != null) {
			StringBuilder path = new StringBuilder();
			for (String imgId : this.imgIdsTag) {
				path.append(imgId).append(",");
			}
			return StringUtil.trimComma(path.toString());
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getComId() {
		return comId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getRegImgPath() {
		return regImgPath;
	}

	public void setRegImgPath(String regImgPath) {
		this.regImgPath = regImgPath;
	}

	public String[] getPatentImgs() {
		return patentImgs;
	}

	public void setPatentImgs(String[] patentImgs) {
		this.patentImgs = patentImgs;
	}

	public String getImgIds() {
		return imgIds;
	}

	public void setImgIds(String imgIds) {
		this.imgIds = imgIds;
	}

	public String[] getImgIdsTag() {
		return imgIdsTag;
	}

	public void setImgIdsTag(String[] imgIdsTag) {
		this.imgIdsTag = imgIdsTag;
	}

}
