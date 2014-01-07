/* 
 * Created by sunhailin at Nov 9, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import java.io.File;
import java.util.Map;

import com.hisupplier.commons.CN;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.UploadUtil;
import com.hisupplier.commons.util.WebUtil;

/**
 * @author sunhailin
 */
public class Menu extends com.hisupplier.commons.entity.cn.Menu {

	private static final long serialVersionUID = -4466808374749030800L;
	private String groupName;// 用于栏目表单
	private boolean fix;
	private int listStyle;
	private boolean show;
	private boolean fold;
	private boolean showDate;
	private String videoImgPath;
	private String playPath;
	private int videoState;
	private File attachment;
	private String attachmentContentType;
	private String attachmentFileName;

	public String getImgPath75() {
		Map<String, String> map = UploadUtil.getImgParam(this.getImgPath());

		if (Boolean.parseBoolean(map.get("isUpload"))) {
			return Config.getString("img.link") + map.get("imgPath");
		} else {
			return WebUtil.get75ImgPath(this.getImgPath());
		}
	}

	public String getStateName() {
		String name = "";
		if (this.getState() == CN.STATE_PASS) {
			name = TextUtil.getText("auditState.pass", "zh");
		} else if (this.getState() == CN.STATE_REJECT_WAIT) {
			name = TextUtil.getText("auditState.auditing", "zh");
		} else if (this.getState() == CN.STATE_WAIT) {
			name = TextUtil.getText("auditState.wait", "zh");
		} else if (this.getState() == CN.STATE_REJECT) {
			name = TextUtil.getText("auditState.reject", "zh");
		}
		return name;
	}

	public String getShortTitle() {
		return StringUtil.substring(this.getTitle(), 18, "...", false);
	}

	public String getFilePathUrl() {
		return WebUtil.getFilePath(this.getFilePath());
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getListStyle() {
		return listStyle;
	}

	public void setListStyle(int listStyle) {
		this.listStyle = listStyle;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isShowDate() {
		return showDate;
	}

	public void setShowDate(boolean showDate) {
		this.showDate = showDate;
	}

	public boolean isFix() {
		return fix;
	}

	public void setFix(boolean fix) {
		this.fix = fix;
	}

	public File getAttachment() {
		return attachment;
	}

	public void setAttachment(File attachment) {
		this.attachment = attachment;
	}

	public String getAttachmentContentType() {
		return attachmentContentType;
	}

	public void setAttachmentContentType(String attachmentContentType) {
		this.attachmentContentType = attachmentContentType;
	}

	public String getAttachmentFileName() {
		return attachmentFileName;
	}

	public void setAttachmentFileName(String attachmentFileName) {
		this.attachmentFileName = attachmentFileName;
	}

	public boolean isFold() {
		return fold;
	}

	public void setFold(boolean fold) {
		this.fold = fold;
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

}
