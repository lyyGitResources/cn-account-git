/* 
 * Created by linliuwei at 2009-11-9 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.product.ProductAction;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.entity.cn.Tag;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.UploadUtil;
import com.hisupplier.commons.util.WebUtil;
import com.hisupplier.commons.util.cn.LocaleUtil;

/**
 * @author linliuwei
 */
public class Product extends com.hisupplier.commons.entity.cn.Product {

	private static final long serialVersionUID = 2665323782201617054L;

	public final static List<String> MINORDER_UNIT_LIST = new ArrayList<String>();
	static {
		MINORDER_UNIT_LIST.add("袋");
		MINORDER_UNIT_LIST.add("桶");
		MINORDER_UNIT_LIST.add("打");
		MINORDER_UNIT_LIST.add("克");
		MINORDER_UNIT_LIST.add("千克");
		MINORDER_UNIT_LIST.add("吨");
		MINORDER_UNIT_LIST.add("米");
		MINORDER_UNIT_LIST.add("千米");
		MINORDER_UNIT_LIST.add("平方米");
		MINORDER_UNIT_LIST.add("立方米");
		MINORDER_UNIT_LIST.add("盎司");
		MINORDER_UNIT_LIST.add("对/双");
		MINORDER_UNIT_LIST.add("包/捆");
		MINORDER_UNIT_LIST.add("张/片/块");
		MINORDER_UNIT_LIST.add("套/副");
	}

	private StringBuffer operate = new StringBuffer();
	private int oldGroupId;
	private int[] oldSpecialGroupId;
	private int oldCatId;
	private String oldKeyword;
	private String[] proAddImg;
	private String[] proImgArray;
	private String groupName;
	private String catName;
	private String videoImgPath;
	private String playPath;
	private int videoState;
	private Map<Integer, String> specialGroupMap;
	private int[] specialGroupId;
	private boolean addTrade = true;
	private boolean optimizeProduct = true;
	private boolean showSetpOne;//批量上传产品时图片是否隐藏
	private String keyword1;
	private String keyword2;
	private String keyword3;
	private String minOrderUnitSelect;
	private String minOrderUnitOther;
	private String[] tagName1;
	private String[] tagName2;
	private String[] tagName3;
	private String[] tagValue1;
	private String[] tagValue2;
	private String[] tagValue3;
	private List<Tag> tagList;
	private boolean foodPact;
	
	//商情添加修改时，记录当前商情数量和最大可添加的数量
	private int tradeCount;
	private int tradeMax;

	private Image image;

	private File attachment;
	private String attachmentContentType;
	private String attachmentFileName;
	
	public String getProvinceShow() {
		StringBuilder area = new StringBuilder();
		if (StringUtil.isNotEmpty(this.getProvince())) {
			String province = LocaleUtil.getProvince(this.getProvince());
			if (province.indexOf("香港") != -1) {
				area.append("香港");
			} else if (province.indexOf("澳门") != -1) {
				area.append("澳门");
			} else if (province.indexOf("台湾") != -1) {
				area.append("台湾");
			} else {
				area.append(LocaleUtil.getProvince(this.getProvince()));
				if (StringUtil.isNotEmpty(this.getCity())) {
					area.append(" ").append(LocaleUtil.getCity(this.getCity()));
				}
				if (StringUtil.isNotEmpty(this.getTown())) {
					area.append(" ").append(LocaleUtil.getCounty(this.getTown()));
				}
			}
		} else if (StringUtil.isNotEmpty(this.getPlace())) {
			area.append(this.getPlace());
		}
		return area.toString();
		
	}
	
	private boolean formValidate = true;//是否是表单验证出错时提交过来的
	
	public String getImgPath75() {
		Map<String, String> map = UploadUtil.getImgParam(this.getImgPath());

		if (Boolean.parseBoolean(map.get("isUpload"))) {
			return Config.getString("img.link") + map.get("imgPath");
		}else{
			return WebUtil.get75ImgPath(this.getImgPath());
		}
	}

	@Override
	public String getImgPaths() {
		// 表单验证出错时有附加图片，返回选择过的图片
		if(this.proAddImg != null && this.proAddImg.length > 0 && StringUtil.isBlank(super.getImgPaths()) && isFormValidate()){
			StringBuffer imgPaths = new StringBuffer();
			for(String addImg : proAddImg){
				imgPaths.append(addImg).append(',');
			}
			imgPaths.deleteCharAt(imgPaths.length() - 1);
			
			return imgPaths.toString();
		}else{
			return super.getImgPaths();
		}
	}
	
	public String getImgPathS() {
		return WebUtil.getImgPath(this.getImgPath());
	}

	public String getProTypeText() {
		String keywords = StringUtil.trimComma(super.getKeywords());
		if (StringUtil.isBlank(keywords) || keywords.length() == 0) {
			return TextUtil.getText(ProductAction.class, "product.general", "zh");
		} else {
			return TextUtil.getText(ProductAction.class, "product.optimize", "zh");
		}
	}
	
	/**
	 * 判断商情是否过期
	 * @return
	 */
	public boolean isDated() {
		return this.getValidDay() > 0 && 
				DateUtil.getDays(DateUtil.parseDate(this.getModifyTime()), new Date()) > this.getValidDay();
	}

	public String getProTypeTrade() {
		if (isDated()) {
			return "过期商情";
		} else if (this.getProType() == CN.TRADE_SELL) {
			return TextUtil.getText(ProductAction.class, "trade.sell", "zh");
		} else {
			return TextUtil.getText(ProductAction.class, "trade.buy", "zh");
		}
	}

	public String getStateName() {
		String name = "";
		if (this.getState() == CN.STATE_PASS) {
			name = TextUtil.getText("auditState.pass", "zh");
		} else if (this.getState() == CN.STATE_WAIT) {
			name = TextUtil.getText("auditState.wait", "zh");
		} else if (this.getState() == CN.STATE_REJECT_WAIT) {
			name = TextUtil.getText("auditState.auditing", "zh");
		} else if (this.getState() == CN.STATE_REJECT) {
			name = TextUtil.getText("auditState.rejectRemark", "zh");
		}
		return name;
	}

	public String getPrice1Text() {
		if (super.getPrice1() > 0) {
			DecimalFormat df = new DecimalFormat("0.00");
			return df.format(this.getPrice1());
		} else {
			return "";
		}
	}

	public String getPrice2Text() {
		if (super.getPrice2() > 0) {
			DecimalFormat df = new DecimalFormat("0.00");
			return df.format(this.getPrice2());
		} else {
			return "";
		}
	}
	
	/**
	 * 是否是用户自己填写的单位
	 * @return
	 */
	public String getMinOrderUnitOthers() {
		if (StringUtil.isNotEmpty(this.getMinOrderUnit()) && !MINORDER_UNIT_LIST.contains(this.getMinOrderUnit())) {
			return true + "";
		} else {
			return false + "";
		}
	}

	public int getTrade() {
		int num = 0;
		if (StringUtil.isNotBlank(this.getPrice1Text())) {
			num++;
		}
		if (StringUtil.isNotBlank(this.getPrice2Text())) {
			num++;
		}
		if (StringUtil.isNotBlank(this.getPaymentType())) {
			num++;
		}
		if (StringUtil.isNotBlank(this.getMinOrder())) {
			num++;
		}
		if (StringUtil.isNotBlank(this.getTransportation())) {
			num++;
		}
		return num;
	}

	public int getSuppliable() {
		int num = 0;
		if (StringUtil.isNotBlank(this.getProductivity())) {
			num++;
		}
		if (StringUtil.isNotBlank(this.getPacking())) {
			num++;
		}
		if (StringUtil.isNotBlank(this.getDeliveryDate())) {
			num++;
		}
		return num;
	}
	
	// 根据3个关键词，组合为一个关键词
	public String getInitKeywords(){
		StringBuffer keywords = new StringBuffer();
		if (StringUtil.isNotBlank(this.getKeyword1())) {
			keywords.append(StringUtil.replace(this.getKeyword1(), ",", ""));
		}
		return StringUtil.trimComma(keywords.toString());
	}
	
	public String getShortProName() {
		return StringUtil.substring(this.getProName(), 9, "...", false);
	}

	public String getShortModel() {
		return StringUtil.substring(this.getModel(), 12, "...", false);
	}

	public String getShortFeatureProName() {
		return StringUtil.substring(this.getProName(), 6, "...", false);
	}

	public String getShortFeatureModel() {
		return StringUtil.substring(this.getModel(), 3, "...", false);
	}

	public String getFilePathUrl() {
		if (StringUtil.isEmpty(this.getFilePath())) return "";
		return WebUtil.getFilePath(this.getFilePath());
	}

	public String getCountryCode() {
		if (Register.isRigthLocal(this.getTown())) {
			return this.getTown();
		} else if (Register.isRigthLocal(this.getCity())) {
			return this.getCity();
		} else if (Register.isRigthLocal(this.getProvince())) {
			return this.getProvince();
		} else {
			return "";
		}
	}

	public String getOperate() {
		return this.operate.toString();
	}

	public void addOperate(String operate) {
		this.operate.append(operate);
	}

	public int getOldGroupId() {
		return oldGroupId;
	}

	public void setOldGroupId(int oldGroupId) {
		this.oldGroupId = oldGroupId;
	}

	public int getOldCatId() {
		return oldCatId;
	}

	public void setOldCatId(int oldCatId) {
		this.oldCatId = oldCatId;
	}

	public String[] getProAddImg() {
		return proAddImg;
	}

	public void setProAddImg(String[] proAddImg) {
		this.proAddImg = proAddImg;
	}

	public String[] getProImgArray() {
		return proImgArray;
	}

	public void setProImgArray(String[] proImgArray) {
		this.proImgArray = proImgArray;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public boolean isAddTrade() {
		return addTrade;
	}

	public void setAddTrade(boolean addTrade) {
		this.addTrade = addTrade;
	}

	public boolean isOptimizeProduct() {
		return optimizeProduct;
	}

	public void setOptimizeProduct(boolean optimizeProduct) {
		this.optimizeProduct = optimizeProduct;
	}

	public boolean isShowSetpOne() {
		return showSetpOne;
	}

	public void setShowSetpOne(boolean showSetpOne) {
		this.showSetpOne = showSetpOne;
	}

	public String getKeyword1() {
		return keyword1;
	}

	public void setKeyword1(String keyword1) {
		this.keyword1 = keyword1;
	}

	public String getKeyword2() {
		return keyword2;
	}

	public void setKeyword2(String keyword2) {
		this.keyword2 = keyword2;
	}

	public String getKeyword3() {
		return keyword3;
	}

	public void setKeyword3(String keyword3) {
		this.keyword3 = keyword3;
	}

	public String[] getTagName1() {
		return tagName1;
	}

	public void setTagName1(String[] tagName1) {
		this.tagName1 = tagName1;
	}

	public String[] getTagName2() {
		return tagName2;
	}

	public void setTagName2(String[] tagName2) {
		this.tagName2 = tagName2;
	}

	public String[] getTagName3() {
		return tagName3;
	}

	public void setTagName3(String[] tagName3) {
		this.tagName3 = tagName3;
	}

	public String[] getTagValue1() {
		return tagValue1;
	}

	public void setTagValue1(String[] tagValue1) {
		this.tagValue1 = tagValue1;
	}

	public String[] getTagValue2() {
		return tagValue2;
	}

	public void setTagValue2(String[] tagValue2) {
		this.tagValue2 = tagValue2;
	}

	public String[] getTagValue3() {
		return tagValue3;
	}

	public void setTagValue3(String[] tagValue3) {
		this.tagValue3 = tagValue3;
	}

	public String getOldKeyword() {
		return oldKeyword;
	}

	public void setOldKeyword(String oldKeyword) {
		this.oldKeyword = oldKeyword;
	}

	public int[] getOldSpecialGroupId() {
		return oldSpecialGroupId;
	}

	public void setOldSpecialGroupId(int[] oldSpecialGroupId) {
		this.oldSpecialGroupId = oldSpecialGroupId;
	}

	public int[] getSpecialGroupId() {
		return specialGroupId;
	}

	public void setSpecialGroupId(int[] specialGroupId) {
		this.specialGroupId = specialGroupId;
	}

	public List<Tag> getTagList() {
		return tagList;
	}

	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}

	public Map<Integer, String> getSpecialGroupMap() {
		return specialGroupMap;
	}

	public void setSpecialGroupMap(Map<Integer, String> specialGroupMap) {
		this.specialGroupMap = specialGroupMap;
	}

	public List<String> getMinOrderUnitList() {
		return MINORDER_UNIT_LIST;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public int getTradeCount() {
		return tradeCount;
	}

	public void setTradeCount(int tradeCount) {
		this.tradeCount = tradeCount;
	}

	public int getTradeMax() {
		return tradeMax;
	}

	public void setTradeMax(int tradeMax) {
		this.tradeMax = tradeMax;
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

	public String getMinOrderUnitSelect() {
		return minOrderUnitSelect;
	}

	public void setMinOrderUnitSelect(String minOrderUnitSelect) {
		this.minOrderUnitSelect = minOrderUnitSelect;
	}

	public void setMinOrderUnitOther(String minOrderUnitOther) {
		this.minOrderUnitOther = minOrderUnitOther;
	}

	public String getMinOrderUnitOther() {
		return minOrderUnitOther;
	}
	
	@Override
	public float getPrice1() {
		if(super.getPrice1() >= 100000000.0){
			return 99999995.99f;
		}else{
			return super.getPrice1();
		}
	}
	
	@Override
	public float getPrice2() {
		if(super.getPrice2() >= 100000000.0){
			return 99999995.99f;
		}else{
			return super.getPrice2();
		}
	}

	public String getPlayPath() {
		return playPath;
	}

	public void setPlayPath(String playPath) {
		this.playPath = playPath;
	}

	public String getVideoImgPath() {
		return videoImgPath;
	}

	public void setVideoImgPath(String videoImgPath) {
		this.videoImgPath = videoImgPath;
	}

	public int getVideoState() {
		return videoState;
	}

	public void setVideoState(int videoState) {
		this.videoState = videoState;
	}

	public boolean isFormValidate() {
		return formValidate;
	}

	public void setFormValidate(boolean formValidate) {
		this.formValidate = formValidate;
	}

	public boolean isFoodPact() {
		return foodPact;
	}

	public void setFoodPact(boolean foodPact) {
		this.foodPact = foodPact;
	}
}
