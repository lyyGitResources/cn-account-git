/* 
 * Created by baozhimin at Oct 28, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.cn.account.entity.PatentDeblocked;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.patent.PatentDeblockedDAO;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.UploadUtil;

/**
 * @author baozhimin
 */
public class ImageService {
	private ImageDAO imageDAO;
	private CompanyDAO companyDAO;
	private PatentDeblockedDAO patentDeblockedDAO;
	/**
	 * 返回图片列表
	 * @param params
	 * <pre>
	 *   imgName	默认null
	 *   imgType	默认-1
	 *   ajax		默认false
	 *   select     默认false
	 *   sortBy		默认imgId desc
	 *   pageNo		默认1
	 * </pre>
	 * @return
	 * <pre>
	 *   listResult	
	 *   numMap<类型,数量>	图片类型数量
	 * </pre>
	 */
	public Map<String, Object> getImageList(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Company company = null;
		if (params.getLoginUser() != null) {
			company = this.companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		}
		if (company == null) {
			throw new PageNotFoundException();
		}
		result.put("isImgFull", false);
		// 图片已满
		if (company.getImgCount() >= company.getImgMax()) {
			//图片已满
			result.put("isImgFull", true);
		}
		
		ListResult<Image> listResult = this.imageDAO.findImageList(params);
		result.put("company", company);
		result.put("listResult", listResult);
		for(Image image : listResult.getList()){
			if(params.isSelect()){
				String element = "";
				if(params.isBatch()){//是批量上传产品，需要图片名称在excel列表选择中使用
					//element = "<img src='/img/checkmark.jpg' title='" + TextUtil.getText("button.select", "zh") + "' onclick=\"selectImage("+image.getImgId()+",'"+image.getImgPath()+"','','"+image.getImgName()+"');\" style='cursor:pointer;'/>";					
				}else{
					element = "<img src='/img/checkmark.jpg' title='" + TextUtil.getText("button.select", "zh") + "' onclick=\"selectImage("+image.getImgId()+",'"+image.getImgPath()+"','','');\" style='cursor:pointer;'/>";
				}
				image.addOperate(element);
				result.put("isSelect", true);//从图库选择页面点击过来，不用显示详情按钮用的开关
				result.put("isFck", params.getImgSrcTag().indexOf("txtUrl") != -1 ? true : false);
			}else{
				Map<String, String> param = new HashMap<String, String>();
				param.put("name", "imageDelete");
				param.put("imgId", image.getImgId() + "");
				param.put("pageNo", params.getPageNo() + "");
				param.put("imgType", image.getImgType() + "");
				String element = "<img src='/img/delete.jpg' title='" + TextUtil.getText("button.delete", "zh") + "' name='imageDelete' imgid='"+image.getImgId()+"' pageNo='"+params.getPageNo()+"' imgType='"+image.getImgType()+"' style='cursor:pointer;'/>";
				image.addOperate(element);
				result.put("isSelect", false);//正常的图库列表页面
				result.put("isFck",false);
			}
		}
		
		if(StringUtil.isEmpty(params.getImgSrcTag())){
			params.setImgSrcTag("");
		}
		
		// 图片列表的第一次显示查询
		if(!params.isAjax() && !params.isSelect()){
			result.put("numMap", this.imageDAO.findNumByImgType(params.getLoginUser().getComId()));
		}
		return result;
	}
	
	/**
	 * 图片上传
	 * @param request
	 * @param params
	 * @return
	 */
	public Map<String, Object> getImageUpload(HttpServletRequest request, QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Image image = new Image();
		image.setImgType(params.getImgType());
		if(image.getImgType() == 3 || image.getImgType() == 4){
			image.getWatermark(request, params.getLoginUser().getMemberId());
		}
		result.put("image", image);
		return result;
	}
	
	/**
	 * 上传图片
	 * @param response
	 * @param image
	 */
	public void uploadImage(HttpServletResponse response, Image image){
		if(image.getImgType() == 3 || image.getImgType() == 4){
			image.saveWatermark(response);
		}
		com.hisupplier.commons.entity.Image img = UploadUtil.uploadImgStream(image, image.getAttachmentFileName(), image.getAttachment());
		image.setImgId(img.getImgId());
		image.setImgPath(img.getImgPath());
	}
	
	/**
	 * 删除图片
	 * @param params
	 * <pre>
	 *   loginUser.comId
	 *   imgId
	 * </pre>
	 * @return 
	 * <pre>
	 *   usedNum 	图片使用的数量
	 *   model 		模块提示
	 *   delNum 	删除成功数量
	 * </pre>
	 */
	public Map<String, Object> deleteImage(QueryParams params){
		Map<String, Object> result = new HashMap<String, Object>();
		Image image = this.imageDAO.findImage(params);
		if(image == null){
			throw new PageNotFoundException();
		}
		int usedNum = 0;
		String model = "";
		switch(image.getImgType()){
			case Image.LOGO:
				params.setTable("Company");
				params.setField("logoImgPath");
				usedNum = this.imageDAO.findNumByUsed(params);
				model = "公司商标图片";
				break;
			case Image.FACE:
				Company company = this.companyDAO.findCompany(params.getLoginUser().getComId());
				if(company.getFaceImgPaths().indexOf(image.getImgPath()) != -1){
					usedNum ++;
				}
				model = "公司形象图片";
				break;
			case Image.PRODUCT:
				params.setTable("Product");
				usedNum += this.imageDAO.findNumByUsed(params);
				params.setTable("Trade");
				usedNum += this.imageDAO.findNumByUsed(params);
				params.setTable("NewProduct");
				usedNum += this.imageDAO.findNumByUsed(params);
				model = "产品、加密产品或商情信息";
				break;
			case Image.MENU:
				params.setTable("Menu");
				usedNum = this.imageDAO.findNumByUsed(params);
				model = "自定义菜单信息";
				break;
			case Image.BANNER:
				params.setTable("WebSite");
				params.setField("bannerPath");
				usedNum = this.imageDAO.findNumByUsed(params);
				model = "风格网站模板Banner图片";
				break;
			case Image.HEAD:
				params.setTable("Users");
				params.setField("headImgPath");
				usedNum = this.imageDAO.findNumByUsed(params);
				model = "联系人头像图片";
				break;
			case Image.LICENSE:
				List<String> imgIds = this.patentDeblockedDAO.findPatentDeblockedImgIds(params.getLoginUser().getComId());
				
				if (imgIds.size() > 0) {
					List<String> imgIdsList = new ArrayList<String> ();
					for (String imgId : imgIds) {
						String[] imgId_ = imgId.split(",");
						if (!imgId.isEmpty()){
							for (String img : imgId_) {
								imgIdsList.add(img);
							}
						}
					}
					
					if (imgIdsList.size() > 0) {
						if (imgIdsList.contains(params.getImgId()+"")) {
							usedNum = 1;
						}
					}
				}
				
				model = "有效凭证图片";
				break;
		}
		result.put("usedNum", usedNum);
		result.put("model", model);
		if(usedNum > 0){
			return result;
		}
		UpdateMap updateMap = new UpdateMap("Image");
		updateMap.addField("state", 0);
		updateMap.addWhere("comId", params.getLoginUser().getComId());
		updateMap.addWhere("imgId", params.getImgId());
		int delNum = this.imageDAO.update(updateMap);
		if( delNum > 0){
			updateMap = new UpdateMap("CompanyExtra");
			updateMap.addField("imgCount", "-", 1);
			updateMap.addWhere("comId", params.getLoginUser().getComId());
			this.imageDAO.update(updateMap);
		}
		result.put("delNum", delNum);
		return result;
	}
	
	public Map<String, Object> getLicense(QueryParams params) {//当前登录用户的 营业执照/有效凭证 图片类型总数
		Company company = companyDAO.findCompany(params.getLoginUser().getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}

		Map<String, Object> result = new HashMap<String, Object>();
		int imgCount = imageDAO.findLicenseCount(params.getLoginUser().getComId());
		result.put("isImgFull", false);
		// 图片已满
		if (imgCount >= PatentDeblocked.IMG_MAX) {
			// 图片已满
			result.put("isImgFull", true);
		}
		result.put("licenseCount", imgCount);
		return result;
	}
	
	public String updateImageName(QueryParams params){
		Image image = this.imageDAO.findImage(params);
		if(image == null){
			throw new PageNotFoundException();
		}
		UpdateMap updateMap = new UpdateMap("Image");
		updateMap.addField("imgName", params.getImgName());
		updateMap.addWhere("comId", params.getLoginUser().getComId());
		updateMap.addWhere("imgId", params.getImgId());
		int num = this.imageDAO.update(updateMap);
		if(num > 0){
			return "editSuccess";
		}
		return "operateFail";
	}
	
	public void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setPatentDeblockedDAO(PatentDeblockedDAO patentDeblockedDAO) {
		this.patentDeblockedDAO = patentDeblockedDAO;
	}
}
