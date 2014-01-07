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
	 * ����ͼƬ�б�
	 * @param params
	 * <pre>
	 *   imgName	Ĭ��null
	 *   imgType	Ĭ��-1
	 *   ajax		Ĭ��false
	 *   select     Ĭ��false
	 *   sortBy		Ĭ��imgId desc
	 *   pageNo		Ĭ��1
	 * </pre>
	 * @return
	 * <pre>
	 *   listResult	
	 *   numMap<����,����>	ͼƬ��������
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
		// ͼƬ����
		if (company.getImgCount() >= company.getImgMax()) {
			//ͼƬ����
			result.put("isImgFull", true);
		}
		
		ListResult<Image> listResult = this.imageDAO.findImageList(params);
		result.put("company", company);
		result.put("listResult", listResult);
		for(Image image : listResult.getList()){
			if(params.isSelect()){
				String element = "";
				if(params.isBatch()){//�������ϴ���Ʒ����ҪͼƬ������excel�б�ѡ����ʹ��
					//element = "<img src='/img/checkmark.jpg' title='" + TextUtil.getText("button.select", "zh") + "' onclick=\"selectImage("+image.getImgId()+",'"+image.getImgPath()+"','','"+image.getImgName()+"');\" style='cursor:pointer;'/>";					
				}else{
					element = "<img src='/img/checkmark.jpg' title='" + TextUtil.getText("button.select", "zh") + "' onclick=\"selectImage("+image.getImgId()+",'"+image.getImgPath()+"','','');\" style='cursor:pointer;'/>";
				}
				image.addOperate(element);
				result.put("isSelect", true);//��ͼ��ѡ��ҳ����������������ʾ���鰴ť�õĿ���
				result.put("isFck", params.getImgSrcTag().indexOf("txtUrl") != -1 ? true : false);
			}else{
				Map<String, String> param = new HashMap<String, String>();
				param.put("name", "imageDelete");
				param.put("imgId", image.getImgId() + "");
				param.put("pageNo", params.getPageNo() + "");
				param.put("imgType", image.getImgType() + "");
				String element = "<img src='/img/delete.jpg' title='" + TextUtil.getText("button.delete", "zh") + "' name='imageDelete' imgid='"+image.getImgId()+"' pageNo='"+params.getPageNo()+"' imgType='"+image.getImgType()+"' style='cursor:pointer;'/>";
				image.addOperate(element);
				result.put("isSelect", false);//������ͼ���б�ҳ��
				result.put("isFck",false);
			}
		}
		
		if(StringUtil.isEmpty(params.getImgSrcTag())){
			params.setImgSrcTag("");
		}
		
		// ͼƬ�б�ĵ�һ����ʾ��ѯ
		if(!params.isAjax() && !params.isSelect()){
			result.put("numMap", this.imageDAO.findNumByImgType(params.getLoginUser().getComId()));
		}
		return result;
	}
	
	/**
	 * ͼƬ�ϴ�
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
	 * �ϴ�ͼƬ
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
	 * ɾ��ͼƬ
	 * @param params
	 * <pre>
	 *   loginUser.comId
	 *   imgId
	 * </pre>
	 * @return 
	 * <pre>
	 *   usedNum 	ͼƬʹ�õ�����
	 *   model 		ģ����ʾ
	 *   delNum 	ɾ���ɹ�����
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
				model = "��˾�̱�ͼƬ";
				break;
			case Image.FACE:
				Company company = this.companyDAO.findCompany(params.getLoginUser().getComId());
				if(company.getFaceImgPaths().indexOf(image.getImgPath()) != -1){
					usedNum ++;
				}
				model = "��˾����ͼƬ";
				break;
			case Image.PRODUCT:
				params.setTable("Product");
				usedNum += this.imageDAO.findNumByUsed(params);
				params.setTable("Trade");
				usedNum += this.imageDAO.findNumByUsed(params);
				params.setTable("NewProduct");
				usedNum += this.imageDAO.findNumByUsed(params);
				model = "��Ʒ�����ܲ�Ʒ��������Ϣ";
				break;
			case Image.MENU:
				params.setTable("Menu");
				usedNum = this.imageDAO.findNumByUsed(params);
				model = "�Զ���˵���Ϣ";
				break;
			case Image.BANNER:
				params.setTable("WebSite");
				params.setField("bannerPath");
				usedNum = this.imageDAO.findNumByUsed(params);
				model = "�����վģ��BannerͼƬ";
				break;
			case Image.HEAD:
				params.setTable("Users");
				params.setField("headImgPath");
				usedNum = this.imageDAO.findNumByUsed(params);
				model = "��ϵ��ͷ��ͼƬ";
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
				
				model = "��Чƾ֤ͼƬ";
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
	
	public Map<String, Object> getLicense(QueryParams params) {//��ǰ��¼�û��� Ӫҵִ��/��Чƾ֤ ͼƬ��������
		Company company = companyDAO.findCompany(params.getLoginUser().getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}

		Map<String, Object> result = new HashMap<String, Object>();
		int imgCount = imageDAO.findLicenseCount(params.getLoginUser().getComId());
		result.put("isImgFull", false);
		// ͼƬ����
		if (imgCount >= PatentDeblocked.IMG_MAX) {
			// ͼƬ����
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
