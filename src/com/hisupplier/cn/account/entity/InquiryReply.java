package com.hisupplier.cn.account.entity;

import java.io.File;

import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.WebUtil;

public class InquiryReply extends com.hisupplier.commons.entity.cn.InquiryReply {

	private static final long serialVersionUID = -630213765056181447L;

	private File[] upload;
	private String[] uploadContentType;
	private String[] uploadFileName;

	private String fromName;
	private String fromEmail;
	private String fromContent;

	public String[] getFilePathUrl() {
		String[] path = null;
		if(StringUtil.isNotEmpty(this.getFilePath())){
			path = new String[3];
			path = this.getFilePath().split(",");
			for(int i = 0; i < path.length;i++){
				path[i] = WebUtil.getFilePath(path[i]);
			}
		}
		return path;
	}
	


	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getFromContent() {
		return fromContent;
	}

	public void setFromContent(String fromContent) {
		this.fromContent = fromContent;
	}



	public File[] getUpload() {
		return upload;
	}



	public void setUpload(File[] upload) {
		this.upload = upload;
	}



	public String[] getUploadContentType() {
		return uploadContentType;
	}



	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentType = uploadContentType;
	}



	public String[] getUploadFileName() {
		return uploadFileName;
	}



	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

}
