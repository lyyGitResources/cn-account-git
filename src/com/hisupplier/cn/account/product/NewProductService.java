/* 
 * Created by baozhimin at Nov 18, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.entity.UserLog;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.member.UserDAO;
import com.hisupplier.cn.account.util.UserLogUtil;
import com.hisupplier.cn.account.view.Button;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;

/**
 * @author baozhimin
 */
public class NewProductService {

	private NewProductDAO newProductDAO;
	private CompanyDAO companyDAO;
	private UserDAO userDAO;

	/**
	 * 返回加密产品管理页面
	 * @param params
	 * @return
	 * <pre>
	 * listResult
	 *   Product{
	 *      proId,
	 *      comId,
	 *      proName,
	 *      model,
	 *      imgId,
	 *      imgPath,
	 *      viewCount,
	 *      state,
	 *      modifyTime
	 *   }
	 * </pre>
	 */
	public Map<String, Object> getNewProductList(QueryParams params) {

		Map<String, Object> result = new HashMap<String, Object>();
		ListResult<Product> listResult = this.newProductDAO.findNewProductList(params);
		result.put("listResult", listResult);
		for (Product product : listResult.getList()) {
			if(params.getLoginUser().isAdmin() || params.getLoginUser().getUserId() == product.getUserId()){
				product.addOperate(new Button("/newproduct/new_product_edit.htm").appendParam("newProId", product.getProId()).setName("button.editNewProduct").getLink());
				product.addOperate("<br />");
			}
			
			product.addOperate(new Button("/product/product_add.htm").appendParam("newProId", product.getProId()).setName("button.toProduct").getLink());
			product.addOperate("<br />");
		}
		if (params.isAjax()) {
			return result;
		}
		return result;
	}

	/**
	 * 返回加密产品添加页面
	 * @param request
	 * @param params 
	 * <pre>
	 * loginUser.comId
	 * </pre>
	 * @return
	 * <pre>
	 * full: 表示加密产品已满
	 * num: 加密产品数量
	 * newProCount: 加密产品数量
	 * newProMax：加密产品允许的最大数量
	 * </pre>
	 */
	public Map<String, Object> getNewProductAdd(HttpServletRequest request, QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Company company = this.companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		if (company.getNewProCount() >= company.getNewProMax()) {
			result.put("full", "newproduct.full");
			result.put("num", company.getNewProCount());
			return result;
		}
		result.put("isImgFull", false);
		// 图片已满
		if (company.getImgCount() >= company.getImgMax()) {
			//图片已满
			result.put("isImgFull", true);
		}
		Product product = new Product();
		// 默认原产地
		if (StringUtil.isEmpty(product.getTown())) {
			User user = this.userDAO.findUser(params.getLoginUser().getUserId(), params.getLoginUser().getComId());
			product.setTown(user.getTown());
		}
		Image image = new Image();
		image.getWatermark(request, company.getMemberId());

		result.put("product", product);
		result.put("image", image);
		result.put("newProCount", company.getNewProCount());
		result.put("newProMax", company.getNewProMax());
		return result;
	}

	/**
	 * 加密产品设置
	 * @param params
	 * @return
	 * <pre>
	 * company{
	 * 		newProPass
	 * 		newProPassExpiry
	 * 		newProMenuName
	 * }
	 * </pre>
	 */
	public Map<String, Object> getNewProductSet(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("company", this.companyDAO.findCompany(params.getLoginUser().getComId()));
		return result;
	}

	/**
	 * 返回加密产品修改页面
	 * @param request
	 * @param params
	 * @return
	 * <pre>
	 * Product{
	 *    proId,
	 *    comId,
	 *    proName,
	 *    model,
	 *    imgId,
	 *    imgPath,
	 *    viewCount,
	 *    state,
	 *    modifyTime
	 * }
	 * </pre>
	 */
	public Map<String, Object> getNewProductEdit(HttpServletRequest request, QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Company company = this.companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}
		result.put("isImgFull", false);
		// 图片已满
		if (company.getImgCount() >= company.getImgMax()) {
			//图片已满
			result.put("isImgFull", true);
		}
		Image image = new Image();
		image.getWatermark(request, company.getMemberId());

		result.put("image", image);
		result.put("product", this.newProductDAO.findNewProduct(params.getLoginUser().getComId(), params.getNewProId()));
		return result;
	}

	/**
	 * 加密产品添加提交
	 * @param response
	 * @param product
	 * @param loginUser
	 * @return
	 * <pre>
	 * product.uploadFail
	 * operateFail
	 * addSuccess
	 * </pre>
	 */
	public String addNewProduct(HttpServletResponse response, Product product, LoginUser loginUser) {
		StringUtil.trimToEmpty(product, "proName,brief,description,model,province,city,town,place,imgPath");
		// 上传产品图片
		if (StringUtil.isNotEmpty(product.getImgPath())) {
			Map<String, String> map = UploadUtil.getImgParam(product.getImgPath());
			if (Boolean.parseBoolean(map.get("isUpload"))) {
				Image image = product.getImage();
				image.saveWatermark(response);
				image.setComId(product.getComId());
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				image.setImgType(Integer.parseInt(map.get("imgType")));
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "product.uploadFail";
				}
				product.setImgId(img.getImgId());
				product.setImgPath(img.getImgPath());
			}
		}
		product.setState(CN.STATE_WAIT);
		String currentTime = new DateUtil().getDateTime();
		product.setCreateTime(currentTime);
		product.setModifyTime(currentTime);
		if (this.newProductDAO.addNewProduct(product) <= 0) {
			return "operateFail";
		}

		UpdateMap updateMap = new UpdateMap("CompanyExtra");
		updateMap.addField("newProCount", "+", 1);
		updateMap.addWhere("comId", product.getComId());
		this.newProductDAO.update(updateMap);

		UserLog userLog = UserLogUtil.getUserLog(UserLog.PRODUCT, UserLog.ADD, "添加加密产品——" + product.getProName(), loginUser);
		newProductDAO.addUserLog(userLog);
		return "addSuccess";
	}

	/**
	 * 加密产品批量删除
	 * @param params proId[]
	 * @return
	 * <pre>
	 * operateFail
	 * deleteSuccess
	 * </pre>
	 */
	public String deleteNewProduct(QueryParams params) {
		ListResult<Product> newProductList = this.newProductDAO.findNewProductList(params);
		int rejectNum = 0;
		for (Product newProduct : newProductList.getList()) {
			if (newProduct.getState() == CN.STATE_REJECT) {
				rejectNum++;
			}
		}
		UpdateMap updateMap = new UpdateMap("NewProduct");
		updateMap.addWhere("comId", params.getLoginUser().getComId());
		updateMap.addWhere("proId", StringUtil.toString(StringUtil.toStringArray(params.getProId()), ","), "in");
		int deleteNum = this.newProductDAO.delete(updateMap);
		if (deleteNum <= 0) {
			return "operateFail";
		}

		updateMap = new UpdateMap("CompanyExtra");
		if (rejectNum > 0) {
			updateMap.addField("newProRejectCount", "-", rejectNum);
		}
		updateMap.addField("newProCount", "-", deleteNum);
		updateMap.addWhere("comId", params.getLoginUser().getComId());
		this.newProductDAO.update(updateMap);
		
		UserLog userLog = UserLogUtil.getUserLog(UserLog.PRODUCT, UserLog.DELETE, "批量删除加密产品", params.getLoginUser());
		newProductDAO.addUserLog(userLog);
		return "deleteSuccess";
	}

	/**
	 * 加密产品修改提交
	 * <pre>
	 * @param response
	 * @param product
	 * PS:comId,userId在表单中取到
	 * @param loginUser
	 * </pre>
	 * @return
	 * <pre>
	 * product.uploadFail: 表示文件或图片上传失败
	 * operateFail
	 * editSuccess
	 * </pre>
	 */
	public String updateNewProduct(HttpServletResponse response, Product product, LoginUser loginUser) {
		StringUtil.trimToEmpty(product, "proName,brief,description,model,province,city,town,place,imgPath");
		// 上传产品图片
		if (StringUtil.isNotEmpty(product.getImgPath())) {
			Map<String, String> map = UploadUtil.getImgParam(product.getImgPath());
			if (Boolean.parseBoolean(map.get("isUpload"))) {
				Image image = product.getImage();
				image.saveWatermark(response);
				image.setComId(product.getComId());
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				image.setImgType(Integer.parseInt(map.get("imgType")));
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "product.uploadFail";
				}
				product.setImgId(img.getImgId());
				product.setImgPath(img.getImgPath());
			}
		}
		UpdateMap updateMap = new UpdateMap("NewProduct");
		updateMap.addField("proName", product.getProName());
		updateMap.addField("brief", product.getBrief());
		updateMap.addField("description", product.getDescription());
		updateMap.addField("model", product.getModel());
		updateMap.addField("province", product.getProvince());
		updateMap.addField("city", product.getCity());
		updateMap.addField("town", product.getTown());
		updateMap.addField("place", product.getPlace());
		updateMap.addField("imgId", product.getImgId());
		updateMap.addField("imgPath", product.getImgPath());
		updateMap.addField("listOrder", this.newProductDAO.findMaxListOrder(product.getComId()) + 1);
		
		Product pro = newProductDAO.findNewProduct(loginUser.getComId(), product.getProId());
		int oldState = pro.getState(); //修改之前的状态
		int newState = 0;
		
		if (loginUser.getMemberType() == 2) {
			if (oldState == CN.STATE_PASS) {
				newState = CN.STATE_REJECT_WAIT; 
			} else if (oldState == CN.STATE_REJECT_WAIT) { 
				newState = CN.STATE_REJECT_WAIT;
			} else {
				newState = CN.STATE_WAIT;
			}
		} else {
			newState= CN.STATE_WAIT;
		}
		updateMap.addField("state", newState);
		
		updateMap.addField("modifyTime", new DateUtil().getDateTime());
		updateMap.addWhere("comId", product.getComId());
		updateMap.addWhere("proId", product.getProId());
		if (this.newProductDAO.update(updateMap) <= 0) {
			return "operateFail";
		}

		if (product.getState() == CN.STATE_REJECT) {
			updateMap = new UpdateMap("CompanyExtra");
			updateMap.addField("newProRejectCount", "-", 1);
			updateMap.addWhere("comId", product.getComId());
			this.newProductDAO.update(updateMap);
		}

		UserLog userLog = UserLogUtil.getUserLog(UserLog.PRODUCT, UserLog.MODIFY, "修改加密产品", loginUser);
		newProductDAO.addUserLog(userLog);
		return "editSuccess";
	}

	/**
	 * 加密产品设置提交
	 * @param params 
	 * <pre>
	 * newProPass
	 * newProPassExpiry
	 * newProMenuName
	 * </pre>
	 * @return
	 * <pre>
	 * operateFail
	 * editSuccess
	 * </pre>
	 */
	public String updateNewProductSet(QueryParams params) {
		UpdateMap updateMap = new UpdateMap("CompanyExtra");
		updateMap.addField("newProPass", params.getNewProPass());
		updateMap.addField("newProMenuName", params.getNewProMenuName());
		updateMap.addWhere("comId", params.getLoginUser().getComId());
		if (this.newProductDAO.update(updateMap) <= 0) {
			return "operateFail";
		}
		
		UserLog userLog = UserLogUtil.getUserLog(UserLog.PRODUCT, UserLog.MODIFY, "修改加密产品设置", params.getLoginUser());
		newProductDAO.addUserLog(userLog);
		return "editSuccess";
	}

	public void setNewProductDAO(NewProductDAO newProductDAO) {
		this.newProductDAO = newProductDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
