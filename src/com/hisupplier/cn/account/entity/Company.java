package com.hisupplier.cn.account.entity;

import java.util.Map;

import com.hisupplier.commons.CN;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.util.CategoryUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.UploadUtil;
import com.hisupplier.commons.util.WebUtil;

public class Company extends com.hisupplier.commons.entity.cn.Company {

	private static final long serialVersionUID = -4898540109909465983L;

	private boolean editMemberId; //用于表示是否可以修改会员帐号
	private boolean editComName; //用于表示是否可以修改公司名
	private String oldCatIds; //用于保存先前选择的目录
	private String videoImgPath;
	private String playPath; 
	private String regImgPath; //用于保存营业执照的路径
	private String regImgPath2; //用于保存营业执照的路径
	private int regImgType; //  执照类型  1企业 2个人
	private int oldImgType; //用于 保存先前的 执照类型  1企业 2个人
	private String oldRegImgPath2; //用于 保存先前的 第二张执照图片  删除判断
	private int videoState;
	
	
	//用于从页面接收数组型参数
	private String[] businessType;
	private String[] qualityCert;
	private String[] catId;
	private String[] website;
	private String[] keyword;
	private String[] face;
	private int crmState;//CRM绑定状态
	
	private String regNo;
	private String ceo;
	private boolean editRegNo; //用于会员信息页是否可以修改营业执照注册号
	private boolean editCeo; //用于会员信息页是否可以修改法人
	private boolean checkRegNo; //用户信息页 ，如果地区不是浙江并且不是企业的则 营业执照注册号和法人不显示为文本框
	/**
	 * 返回状态名称
	 * @return
	 */
	public String getStateName() {
		String name = "";
		if (this.getState() == CN.STATE_PASS) {
			name = "（" + TextUtil.getText("auditState.pass", "zh") + "）";
		} else if (this.getState() == CN.STATE_WAIT) {
			name = "（" + TextUtil.getText("auditState.wait", "zh") + "）";
		} else if (this.getState() == CN.STATE_REJECT) {
			name = "<span class='red'>（" + TextUtil.getText("auditState.reject", "zh") + "）</span>";
		} else if (this.getState() == CN.STATE_REJECT_WAIT) {
			name = "<span class='red'>（正在审核）</span>";
		}
		return name;
	}

	@Override
	public String getFaceImgPaths() {
		// 表单验证出错时有图片，返回选择过的图片
		if(this.face != null && this.face.length > 0 && StringUtil.isBlank(super.getFaceImgPaths())){
			StringBuffer faceImgPaths = new StringBuffer();
			for(String faceImg : face){
				faceImgPaths.append(faceImg).append(',');
			}
			faceImgPaths.deleteCharAt(faceImgPaths.length() - 1);
			
			return faceImgPaths.toString();
		}else{
			return super.getFaceImgPaths();
		}
	};
	
	/**
	 * 返回会员等级名称
	 * @return
	 */
	public String getMemberTypeName() {
		return this.getMemberType() == CN.GOLD_SITE ? TextUtil.getText("goldSite", "zh") : TextUtil.getText("freeSite", "zh");
	}

	/**
	 * 返回公司类型复选项目
	 * @return
	 */
	public Item[] getBusinessTypeItems() {

		Item[] items = new Item[CN.BUSINESS_TYPE.size()];
		for (int i = 1; i <= items.length; i++) {
			String name = (String) CN.BUSINESS_TYPE.get(i);
			items[i - 1] = new Item(name, "" + i);
		}
		if (this.getBusinessTypes() != null) {
			businessType = this.getBusinessTypes().split(",");
		}
		if (businessType != null) {
			for (String value : businessType) {
				for (Item item : items) {
					if (value.equals(item.getValue())) {
						item.setChecked(true);
					}
				}
			}
		}
		return items;
	}

	public Item[] getQualityCertItems() {
		Item[] items = new Item[CN.QUALITY_CERTS.size()];
		for (int i = 1; i <= items.length; i++) {
			String name = (String) CN.QUALITY_CERTS.get(i);
			items[i - 1] = new Item(name, "" + i);
		}
		if (this.getQualityCerts() != null) {
			qualityCert = this.getQualityCerts().split(",");
		}
		if (qualityCert != null) {
			for (String value : qualityCert) {
				for (Item item : items) {
					if (value.equals(item.getValue())) {
						item.setChecked(true);
					}
				}
			}
		}
		return items;
	}

	public Item[] getCatItems() {
		String[] cat = new String[3];
		if (this.getCatIds() != null) {
			cat = this.getCatIds().split(",");
		}
		if (this.getCatId() != null) {
			cat = this.getCatId();
		}

		Item[] catItems = new Item[cat.length];
		for (int i = 0; i < cat.length; i++) {
			String name = CategoryUtil.getNamePath(Integer.parseInt(cat[i]), ">>");
			Item item = new Item(name, cat[i]);
			catItems[i] = item;
		}
		return catItems;
	}


	public String[] getWebsiteArray1() {
		String[] websiteArray1 = new String[3];
		String[] tmp = new String[3];
		if (this.getWebsite() != null) {
			return this.getWebsite();
		}
		if (this.getWebsites() != null) {
			tmp = this.getWebsites().split(",");
		}
		if (tmp != null) {
			for (int i = 0; i < 3; i++) {
				if (tmp.length > 0 && i < tmp.length && tmp[i] != null) {
					websiteArray1[i] = tmp[i];
				} else {
					websiteArray1[i] = "";
				}
			}
		}

		return websiteArray1;
	}

	public String[] getKeywordArray1() {
		String[] keywordArray1 = new String[10];
		String[] tmp = new String[10];
		if (this.getKeyword() != null) {
			return this.getKeyword();
		}
		if (this.getKeywords() != null) {
			tmp = this.getKeywords().split(",");
		}
		if (tmp != null) {
			for (int i = 0; i < 10; i++) {
				if (tmp.length > 0 && i < tmp.length && tmp[i] != null) {
					keywordArray1[i] = tmp[i];
				} else {
					keywordArray1[i] = "";
				}
			}
		}

		return keywordArray1;
	}

	public boolean isEditMemberId() {
		return editMemberId;
	}

	public void setEditMemberId(boolean editMemberId) {
		this.editMemberId = editMemberId;
	}

	public boolean isEditComName() {
		return editComName;
	}

	public void setEditComName(boolean editComName) {
		this.editComName = editComName;
	}

	public String getOldCatIds() {
		return oldCatIds;
	}

	public void setOldCatIds(String oldCatIds) {
		this.oldCatIds = oldCatIds;
	}

	public String[] getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String[] businessType) {
		this.businessType = businessType;
	}

	public String getLogoImgSrc() {
		Map<String, String> map = UploadUtil.getImgParam(this.getLogoImgPath());

		if (Boolean.parseBoolean(map.get("isUpload"))) {
			return Config.getString("img.link") + map.get("imgPath");
		}else{
			return WebUtil.get100ImgPath(this.getLogoImgPath());
		}
	}

	public String getLogoCertImgSrc() {
		Map<String, String> map = UploadUtil.getFileParam(this.getLogoCertImg());

		if (Boolean.parseBoolean(map.get("isUpload"))) {
			return Config.getString("img.fieLink") + map.get("filePath");
		}else{
			return WebUtil.getFilePath(this.getLogoCertImg());
		}
	}

	public String getRegImgSrc() {
		Map<String, String> map = UploadUtil.getFileParam(this.getRegImgPath());

		if (Boolean.parseBoolean(map.get("isUpload"))) {
			return WebUtil.getFilePath(map.get("filePath"));
		}else{
			return WebUtil.getFilePath(this.getRegImgPath());
		}
	}
	
	public String[] getWebsite() {
		return website;
	}

	public void setWebsite(String[] website) {
		this.website = website;
	}

	public String[] getKeyword() {
		return keyword;
	}

	public void setKeyword(String[] keyword) {
		this.keyword = keyword;
	}

	public String[] getFace() {
		return face;
	}

	public void setFace(String[] face) {
		this.face = face;
	}

	public String[] getQualityCert() {
		return qualityCert;
	}

	public void setQualityCert(String[] qualityCert) {
		this.qualityCert = qualityCert;
	}

	public String[] getCatId() {
		return catId;
	}

	public void setCatId(String[] catId) {
		this.catId = catId;
	}

	public String getVideoImgPath() {
		return videoImgPath;
	}

	public void setVideoImgPath(String videoImgPath) {
		this.videoImgPath = videoImgPath;
	}

	public String getPlayPath() {
		return playPath;
	}

	public void setPlayPath(String playPath) {
		this.playPath = playPath;
	}

	public int getVideoState() {
		return videoState;
	}

	public void setVideoState(int videoState) {
		this.videoState = videoState;
	}
	public int getCrmState() {
		return crmState;
	}

	public void setCrmState(int crmState) {
		this.crmState = crmState;
	}

	public String getRegImgPath() {
		return regImgPath;
	}

	public void setRegImgPath(String regImgPath) {
		this.regImgPath = regImgPath;
	}

	public int getRegImgType() {
		return regImgType;
	}

	public void setRegImgType(int regImgType) {
		this.regImgType = regImgType;
	}

	public String getRegImgPath2() {
		return regImgPath2;
	}

	public void setRegImgPath2(String regImgPath2) {
		this.regImgPath2 = regImgPath2;
	}

	public int getOldImgType() {
		return oldImgType;
	}

	public void setOldImgType(int oldImgType) {
		this.oldImgType = oldImgType;
	}

	public String getOldRegImgPath2() {
		return oldRegImgPath2;
	}

	public void setOldRegImgPath2(String oldRegImgPath2) {
		this.oldRegImgPath2 = oldRegImgPath2;
	}

	public String getCeo() {
		return ceo;
	}

	public void setCeo(String ceo) {
		this.ceo = ceo;
	}

	public boolean isEditCeo() {
		return editCeo;
	}

	public void setEditCeo(boolean editCeo) {
		this.editCeo = editCeo;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public boolean isEditRegNo() {
		return editRegNo;
	}

	public void setEditRegNo(boolean editRegNo) {
		this.editRegNo = editRegNo;
	}

	public boolean isCheckRegNo() {
		return checkRegNo;
	}

	public void setCheckRegNo(boolean checkRegNo) {
		this.checkRegNo = checkRegNo;
	}
}
