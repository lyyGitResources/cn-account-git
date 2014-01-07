package com.hisupplier.cn.account.entity;

import java.util.ArrayList;
import java.util.List;

import com.hisupplier.commons.CN;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.util.StringUtil;

public class PatentDeblocked {
	public static final int IMG_MAX = 15; // Ӫҵִ�� �� ƾ֤ �����ϴ���������� 
	private int id;
	private int comId;
	private String keywords; // Υ��� ���Υ�����"��"�ָ�
	private String remark; // ��ע
	private int state; // 0���ɾ����10���ʧ�ܣ�15�ȴ���ˣ�20��˳ɹ�
	private String cause; // ���ʧ�ܵ�ԭ��'
	private String regImgPath; // Ӫҵִ�պ���Ч֤��ͼƬ ���3��ͼƬ �ö��ŷָ�
	private String createTime;
	private String modifyTime;
	private String[] patentImgs; //���ύʱ �洢���ͼƬ�ĵ�ַ   ������Զ��ŷָ����� regImgPath��
	private String imgIds; //��ӦImage�����imgId ����ö��ŷָ�   ���3��
	private String[] imgIdsTag; //Υ��ʽ��ҳ�� ���������ŵ� ͼƬ��imgId ���������ݲ�������ʱ ʹ��

	public String getStateName() {
		String showTypeName = "";
		if (this.getState() == CN.STATE_PASS) {
			showTypeName = "���ͨ��";
		} else if (this.getState() == CN.STATE_WAIT) {
			showTypeName = "�ȴ����";
		} else if (this.getState() == CN.STATE_REJECT) {
			StringBuilder st = new StringBuilder();
			st.append("��˲�ͨ�� <a href=\"JavaScript:causeShow('").append(this.cause).append("')\" >(ԭ��)").append("</a>&nbsp;&nbsp;");
			showTypeName = st.toString();
		}
		return showTypeName;
	}

	public String getLicense() { //��ʾӪҵִ�ջ�ƾ֤ �������¼� ������ɲ鿴ͼƬ
		String[] patentImages = this.getRegImgPath().split(",");
		StringBuilder showText = new StringBuilder();
		int index = 1;
		String imgLink = Config.getString("img.link");
		for(String imgPath : patentImages) { //ע�� <a href=\" ˫����ת��  
			showText.append("<a href=\"JavaScript:licenseShow('").append(imgLink).append(imgPath).append("')\" >ƾ֤").append(index)
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
