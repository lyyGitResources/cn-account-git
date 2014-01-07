/* 
 * Created by sunhailin at Nov 5, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import com.hisupplier.commons.CN;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.WebUtil;

/**
 * @author sunhailin
 *
 */
public class Video extends com.hisupplier.commons.entity.cn.Video {

	private static final long serialVersionUID = 4713109179133627503L;
	private int videoUserCount; //视频使用次数
	private int oldGroupId;
	private String groupName;
	private String videoType;
	
	private String asdId;
	
	/**
	 * 规则 http://static.asdtv.com/uimage/视频id第一个字符/视频id前10个字符/视频id最后一个字符/85db38c02246719d16858ff36afe901b_第几张.jpg
	 * @return
	 */
	public String getImgPathShow() {
		String result = "";
		if (StringUtil.isNotBlank(asdId)) {
			String[] _tmp = asdId.split("_");
			result = "http://static.asdtv.com/uimage/7/7b3fb3c415/" + StringUtil.substring(_tmp[0], -1) + "/" + _tmp[0] + "_0.jpg";
		}
		return result;
	}
	
	public String getPlayPathFlash() {
		String result = "";
		if (StringUtil.isNotBlank(asdId)) {
			result = "http://player.asdtv.com/videos/" + asdId + ".swf";
		}
		return result;
	}
	
	public String getPlayPathHTML() {
		String result = "";
		if (StringUtil.isNotBlank(asdId)) {
			result = "<OBJECT classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" width=\"600\" height=\"337\"" +
					" id=\"asdtvplayer\">" +
					"<PARAM NAME=movie VALUE=\"http://player.asdtv.com/videos/" + asdId + ".swf\" />" +
					"<param name=\"allowscriptaccess\" value=\"always\" />" +
					"<param name=\"allowFullScreen\" value=\"true\" />" +
					"<EMBED src=\"http://player.asdtv.com/videos/" + asdId + ".swf\"" +
					" width=\"600\" height=\"337\"  TYPE=\"application/x-shockwave-flash\"" +
					" allowscriptaccess=\"always\" name=\"asdtvplayer\" allowFullScreen=\"true\" />" +
					"</OBJECT>";
		}
		return result;
	}

	public String getVideoImg() {
		if (StringUtil.isEmpty(this.getFileName())) {
			return null;
		}
		return WebUtil.getVideoImg(this.getFileName());
	}
	
	public String getStateName() {
		String name = "";
		if (this.getState() == CN.STATE_PASS) {
			name = TextUtil.getText("auditState.pass", "zh");
		} else if (this.getState() == CN.STATE_WAIT) {
			name = TextUtil.getText("auditState.wait", "zh");
		} else if (this.getState() == CN.STATE_REJECT) {
			name = TextUtil.getText("auditState.reject", "zh");
		}
		return name;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public int getOldGroupId() {
		return oldGroupId;
	}

	public void setOldGroupId(int oldGroupId) {
		this.oldGroupId = oldGroupId;
	}

	public int getVideoUserCount() {
		return videoUserCount;
	}

	public void setVideoUserCount(int videoUserCount) {
		this.videoUserCount = videoUserCount;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAsdId() {
		return asdId;
	}

	public void setAsdId(String asdId) {
		this.asdId = asdId;
	}
}
