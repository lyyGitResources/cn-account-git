/* 
 * Created by baozhimin at Oct 28, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hisupplier.commons.Config;
import com.hisupplier.commons.util.Coder;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.WebUtil;

/**
 * @author baozhimin
 */
public class Image extends com.hisupplier.commons.entity.Image {
	private static final long serialVersionUID = -6299538088734810443L;
	private String format;
	private StringBuffer operate = new StringBuffer();
	private File attachment;
	private String attachmentContentType;
	private String attachmentFileName;
	private String imgSrcTag;
	private String imgPathTag;
	private String imgIdTag;
	
	/**
	 * 图片媒体类型
	 * @return
	 */
	public String getMimeType() {
		return Config.getString("img." + getImgType() + ".mimeType");
	}
	
	/**
	 * 图片格式
	 * @return
	 */
	public String getType() {
		String type = Config.getString("img." + getImgType() + ".type");
		if(StringUtil.isEmpty(type)){
			return "JPG、JPEG、GIF";
		}
		
		return type.toUpperCase().replace(",", "、");
	}
	
	/**
	 * 图片最大大小
	 * @return
	 */
	public int getMaxSize() {
		int maxSize = 100;
		
		String size = Config.getString("img." + getImgType() + ".size");
		if(StringUtil.isEmpty(size)){
			return maxSize;
		}

		try {
			maxSize = Integer.parseInt(size);
		} catch (Exception e) {
		}

		return maxSize;
	}
	
	/**
	 * 图片最佳尺寸
	 * @return
	 */
	public String getBestScale() {
		String scale = Config.getString("img." + getImgType() + ".scale");
		if(scale == null){
			scale = "";
		}
		return scale;
	}
	
	/**
	 * 取得存储在Cookie中的水印信息
	 * @param request
	 */
	public void getWatermark(HttpServletRequest request, String memberId) {
		String cookieValue = WebUtil.getCookieValue(request, "hs_image_watermark");
		String[] tmps = StringUtil.toArray(Coder.decodeBase64(cookieValue), ";");
		if (tmps.length >= 5) {
			this.setWatermark(Boolean.parseBoolean(tmps[0]));
			this.setWatermarkText(StringUtil.trimToEmpty(tmps[1]));
			this.setWatermarkTextColor(StringUtil.trimToEmpty(tmps[2]));
			
			int textFontSize = 0;
			try{
				textFontSize = Integer.parseInt(tmps[3]);
			}catch (NumberFormatException e) {
			}

			this.setTextFontSize(textFontSize);
			this.setWatermarkRight(Boolean.parseBoolean(tmps[4]));
		} else {
			this.setWatermark(true);
			this.setWatermarkText(memberId + ".cn." + Config.getString("sys.domain"));
			this.setWatermarkTextColor("#cccccc");
			this.setTextFontSize(0);
			this.setWatermarkRight(true);
		}
	}
	
	/**
	 * 保存水印设置在Cookie中
	 * @param response
	 */
	public void saveWatermark(HttpServletResponse response) {
		// 保存水印信息前，进行验证水印文字和颜色
		if(this.isWatermark()){
			if(StringUtil.isBlank(this.getWatermarkText()) || "null".equals(this.getWatermarkText())){
				this.setWatermarkText("memberId.cn." + Config.getString("sys.domain"));
			}
			
			if(StringUtil.isBlank(this.getWatermarkTextColor()) || this.getWatermarkTextColor().length() != 7){
				this.setWatermarkTextColor("#cccccc");
			}else{
				try {
					int R = Integer.parseInt(super.getWatermarkTextColor().substring(1, 3), 16);
					int G = Integer.parseInt(super.getWatermarkTextColor().substring(3, 5), 16);
					int B = Integer.parseInt(super.getWatermarkTextColor().substring(5, 7), 16);
					if ((R < 0 || R > 255) || (G < 0 || G > 255) || (B < 0 || B > 255)) {
						this.setWatermarkTextColor("#cccccc");
					}
				} catch (Exception e) {
					this.setWatermarkTextColor("#cccccc");
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append(this.isWatermark()).append(";");
		sb.append(this.getWatermarkText()).append(";");
		sb.append(this.getWatermarkTextColor()).append(";");
		sb.append(this.getTextFontSize()).append(";");
		sb.append(this.isWatermarkRight());

		// 保存时间1年
		WebUtil.addCookie(response, "hs_image_watermark", Coder.encodeBase64(sb.toString()), 60 * 24 * 365);
	}
	
	public String getWatermarkCheck() {
		if(this.isWatermark()){
			return "checked";
		}else{
			return "";
		}
	}
	
	public String getWatermarkRightCheck() {
		if(this.isWatermarkRight()){
			return "checked";
		}else{
			return "";
		}
	}
	
	public String getTextOptionCheck() {
		if(this.isTextOption()){
			return "checked";
		}else{
			return "";
		}
	}
	
	public String getWatermarkTextReal() {
		if(StringUtil.isEmpty(this.getWatermarkText())){
			if(StringUtil.isEmpty(this.getMemberId())){
				return "cn." + Config.getString("sys.domain");
			}else{
				return this.getMemberId() + ".cn." + Config.getString("sys.domain");
			}
		}
		return this.getWatermarkText();
	}
	
	public String getWatermarkTextColorReal() {
		if(StringUtil.isEmpty(this.getWatermarkTextColor())){
			return "#cccccc";
		}
		return this.getWatermarkTextColor();
	}
	
	
	public String getImageSizeText() {
		return this.getImgSize() + "K";
	}
	
	public List<Integer> getTextFontSizeList() {
		List<Integer> fontSizeList = new ArrayList<Integer>();
		for (int i = 50; i >= 10; i -= 2) { 
			fontSizeList.add(i);
		}
		return fontSizeList;
	}
	public List<String> getScaleList(){
		List<String> list = new ArrayList<String>();
		String link = Config.getString("img.link");
		String ext = this.getImgPath().substring(this.getImgPath().lastIndexOf("."));// 截取图片后缀
		String name = this.getImgPath().substring(0, this.getImgPath().lastIndexOf("."));// 截图图片名

		if(name.endsWith("(s)")){
			name = name.substring(0, name.length() - 3);
			String[] array = Image.this.getImgScale().split(";");
			for (String s : array) {
				String[] tmp = s.split(":");
				if (tmp.length == 2 && tmp[0].equals("s75")) {
					list.add(tmp[1] + "=" + link + name + "(75)" + ext);
				} else if (tmp.length == 2 && tmp[0].equals("s100")) {
					list.add(tmp[1] + "=" + link + name + "(100)" + ext);
				} else if (tmp.length == 2 && tmp[0].equals("s240")) {
					list.add(tmp[1] + "=" + link + name + "(240)" + ext);
				} else if (tmp.length == 2 && tmp[0].equals("ss")) {
					list.add(tmp[1] + "=" + link + this.getImgPath());
				}
			}
		}else if(name.endsWith("_s")){
			name = name.substring(0, name.length() - 2);
			String[] array = Image.this.getImgScale().split(";");
			for (String s : array) {
				String[] tmp = s.split(":");
				if (tmp.length == 2 && tmp[0].equals("s75")) {
					list.add(tmp[1] + "=" +  link + name + "_75" + ext);
				} else if (tmp.length == 2 && tmp[0].equals("s100")) {
					list.add(tmp[1] + "=" +  link + name + "_100" + ext);
				} else if (tmp.length == 2 && tmp[0].equals("s240")) {
					list.add(tmp[1] + "=" +  link + name + "_240" + ext);
				} else if (tmp.length == 2 && tmp[0].equals("ss")) {
					list.add(tmp[1] + "=" +  link + this.getImgPath());
				}
			}
		}else{
			list.add("(-1 X -1)=" +  link + this.getImgPath());
		}
	
		return list;
	}
	
	public String getImgPath75() {
		if(this.getImgType() == Image.BANNER){
			return WebUtil.get100ImgPath(this.getImgPath());
		}else if(this.getImgType() == Image.LOGO){
			return WebUtil.get100ImgPath(this.getImgPath());
		}else{
			return WebUtil.get75ImgPath(this.getImgPath());
		}
	}
	public String getImgPath100() {
		return WebUtil.get100ImgPath(this.getImgPath());
	}
	public String getImgPathS() {
		return WebUtil.getImgPath(this.getImgPath());
	}
	public String getNewImage() {
		if (StringUtil.isEmpty(this.getCreateTime())) {
			return "";
		}
		
		String tmp = DateUtil.formatDate(this.getCreateTime());
		if(new DateUtil().getDate().equals(tmp)){
			return "<img src='/img/new.gif' border='0'/>";
		}
		return "";
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getOperate() {
		return this.operate.toString();
	}

	public void addOperate(String operate) {
		this.operate.append(operate);
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

	public String getImgSrcTag() {
		return imgSrcTag;
	}

	public void setImgSrcTag(String imgSrcTag) {
		this.imgSrcTag = imgSrcTag;
	}

	public String getImgPathTag() {
		return imgPathTag;
	}

	public void setImgPathTag(String imgPathTag) {
		this.imgPathTag = imgPathTag;
	}

	public String getImgIdTag() {
		return imgIdTag;
	}

	public void setImgIdTag(String imgIdTag) {
		this.imgIdTag = imgIdTag;
	}
}
