/* 
 * Created by linliuwei at 2009-11-12 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import java.util.Map;

import com.hisupplier.commons.Config;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;
import com.hisupplier.commons.util.WebUtil;

public class Top extends com.hisupplier.commons.entity.cn.Top {

	private static final long serialVersionUID = -9084333962720408543L;

	private String keyword;

	private Image image;//用于水印
	
	private String memberId;
	private String link2;//用于修改页面 链接地址的其他网址

	//二级域名首页,用于修改页面
	public String getWebBase() {
		return "http://" + this.memberId + ".cn." + Config.getString("sys.domain");
	}

	//产品列表
	public String getWebProList() {
		return "http://" + this.memberId + ".cn." + Config.getString("sys.domain") + "/product/list.html";
	}

	//About us
	public String getWebAboutUs() {
		return "http://" + this.memberId + ".cn." + Config.getString("sys.domain") + "/about-us/";
	}

	public String getLink1() {
		String link1 = "";
		if (StringUtil.isNotEmpty(this.getLink())) {
			if (this.getLink().equals(this.getWebBase()) || this.getLink().equals(this.getWebProList()) || this.getLink().equals(this.getWebAboutUs())) {
				link1 = this.getLink();
			} else {
				link1 = "";
				this.setLink2(this.getLink());
			}
		} else {
			link1 = this.getWebBase();
		}
		return link1;
	}

	public String getImgPath75() {
		Map<String, String> map = UploadUtil.getImgParam(this.getImgPath());

		if (Boolean.parseBoolean(map.get("isUpload"))) {
			return Config.getString("img.link") + map.get("imgPath");
		}else{
			return WebUtil.get75ImgPath(this.getImgPath());
		}
	}

	
	public String getShortProName() {
		return StringUtil.substring(this.getProName(), 10, "...", false);
	}

	public String getTopTypeName() {
		String topTypeName = "";
		if (this.getPriority() == 1) {
			topTypeName = "第1页第1位";
		} else if (this.getPriority() == 2) {
			topTypeName = "第1页第2位以下随机排序";
		}
		return topTypeName;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getLink2() {
		return link2;
	}

	public void setLink2(String link2) {
		this.link2 = link2;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
