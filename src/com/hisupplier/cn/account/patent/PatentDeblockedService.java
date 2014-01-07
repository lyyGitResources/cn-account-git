/**
 * Created by liuyuyang at 2013-3-29 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.patent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.cn.account.entity.PatentDeblocked;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.entity.UserLog;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.member.UserDAO;
import com.hisupplier.cn.account.misc.ImageDAO;
import com.hisupplier.cn.account.misc.QueryParams;
import com.hisupplier.cn.account.util.PatentUtil;
import com.hisupplier.cn.account.util.UserLogUtil;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;

/**
 * @author liuyuyang
 * 
 */
public class PatentDeblockedService {
	private PatentDeblockedDAO patentDeblockedDAO;
	private CompanyDAO companyDAO;
	private UserDAO userDAO;
	private ImageDAO imageDAO;

	public Map<String, Object> getPatentDeblocked(LoginUser loginUser, PatentDeblocked patentDeblocked) {
		Company company = companyDAO.findCompany(loginUser.getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}
		User user = userDAO.findUser(loginUser.getUserId(), loginUser.getComId());
		if (user == null) {
			throw new PageNotFoundException();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		int imgCount = imageDAO.findLicenseCount(company.getComId());
		result.put("isImgFull", false);
		// 图片已满
		if (imgCount >= PatentDeblocked.IMG_MAX) {
			// 图片已满
			result.put("isImgFull", true);
		}

		result.put("licensePage", true);
		result.put("image", new Image());
		result.put("userInfo", user);
		result.put("company", company);
		//jsp from提交地址会根据这个改变   
		result.put("submitURL", "/patent/patentDeblocked_submit.htm"); 
		result.put("titleText", "解禁申请");
		result.put("patent", patentDeblocked);
		return result;
	}
	
	public Map<String, Object> getPatentDeblockedError(LoginUser loginUser, PatentDeblocked patentDeblocked) {
		Map<String, Object> result = this.getPatentDeblocked(loginUser, patentDeblocked);
		patentDeblocked.setRegImgPath(patentDeblocked.getFormImgs());// 验证失败保存图片
		patentDeblocked.setImgIds(patentDeblocked.getFormImgIds());// 验证失败保存图片id
		result.put("patent", patentDeblocked);
		return result;
	}
	
	public Map<String, Object> getPatentDeblockedEditError(LoginUser loginUser, PatentDeblocked patentDeblocked) {
		Map<String, Object> result = getPatentDeblocked(loginUser, patentDeblocked);
		patentDeblocked.setRegImgPath(patentDeblocked.getFormImgs());
		patentDeblocked.setImgIds(patentDeblocked.getFormImgIds());// 验证失败保存图片id
		//营业执照/有效凭证的图片显示在JSP页面  通过set images参数设置
		result.put("submitURL", "/patent/patentDeblocked_edit_submit.htm");
		result.put("titleText", "解禁申请修改");
		result.put("patent", patentDeblocked);
		return result;
	}
	
	public Map<String, Object> getPatentDeblockedEdit(LoginUser loginUser , QueryParams params) {
		params.setComId(loginUser.getComId());
		PatentDeblocked patentDeblocked = patentDeblockedDAO.findPatentDeblockedById(params);
		if(patentDeblocked == null) {
			throw new PageNotFoundException();
		}
		Map<String, Object> result = getPatentDeblocked(loginUser, patentDeblocked);
		//营业执照/有效凭证的图片显示在JSP页面  通过set images参数设置
		result.put("patent", patentDeblocked);
		result.put("submitURL", "/patent/patentDeblocked_edit_submit.htm");
		result.put("titleText", "解禁申请修改");
		return result;
	}
	
	public String addPatentDeblocked(PatentDeblocked patentDeblocked, LoginUser loginUser, boolean isUpdate) {
		StringUtil.trimToEmpty(patentDeblocked, "remark,cause,keywords");
		if (StringUtil.isBlank(patentDeblocked.getKeywords())) {
			return "请填写违规词";
		}
		if (patentDeblocked.getKeywords().length() > 120) {
			return "输入的违规词过长";
		}
		if (patentDeblocked.getRemark().length() > 500) {
			return "输入的备注过长";
		}
		if (patentDeblocked.getPatentImgs() == null) { // 添加的图片
			patentDeblocked.setRegImgPath("");
			return "至少要上传一份 （有效凭证）";
		} else {
			
			if (!PatentUtil.checkAddKeywords(patentDeblocked.getKeywords())) {
				return "您要申请的解禁违规词并不存在，不可以申请";
			}
			if (patentDeblocked.getPatentImgs().length > 3) {
				return "有效凭证最多可以上传3张"; 
			}
			if (StringUtil.isNotEmpty(patentDeblocked.getFormImgIds())) {
				patentDeblocked.setImgIds(patentDeblocked.getFormImgIds());
			}
			patentDeblocked.setComId(loginUser.getComId());
			
			if (isUpdate) {
				List<PatentDeblocked> isSubmit = patentDeblockedDAO.findPatentDeblockedIsUpdate(patentDeblocked);
				if (isSubmit.size() > 0) {
					return "此违规词已经申请，不能重复申请";
				}
			}else {
				List<String> keywordsList = patentDeblockedDAO.findPatentDeblockedKeywords(patentDeblocked);
				if (keywordsList.size() > 0) {
						return "您已经申请过此解禁违规词，不能重复申请";
				}
			}
			
			if (patentDeblocked.getPatentImgs().length > 0) {
				Company company = this.companyDAO.findCompanyMemberType(loginUser.getComId());
				if (company == null) {
					throw new PageNotFoundException();
				}

				int tmpImgCount = 0;
				int uploadImgCount = 0;
				String proAddImgs = "";
				String addImgIds ="";
				Map<String, String> map = new HashMap<String, String>();
				Pattern number_pattern = Pattern.compile("^\\d{1}$");
				int imgCount = imageDAO.findLicenseCount(loginUser.getComId());
				
				for (String img : patentDeblocked.getPatentImgs()) {

					map = UploadUtil.getImgParam(img);
					if (Boolean.parseBoolean(map.get("isUpload"))) {
						if (imgCount + uploadImgCount >= PatentDeblocked.IMG_MAX) {
							return "图片已满";
						}
						if (company.getImgCount() + tmpImgCount++ < company.getImgMax()) {
							Image image = new Image();
							image.setComId(company.getComId());
							image.setImgName(map.get("imgName"));
							image.setImgPath(map.get("imgPath"));
							if (!number_pattern.matcher(map.get("imgType")).matches() || !"9".equals(map.get("imgType"))) {
								map.put("imgType", ""+ Image.LICENSE +"");
							}
							image.setImgType(Integer.parseInt(map.get("imgType")));
							com.hisupplier.commons.entity.Image img_ = UploadUtil.swfUploadImage(image);
							if (img_.getImgId() <= 0) {
								return "图片上传失败";
							}else {
								uploadImgCount++;
								proAddImgs += "," + img_.getImgPath();
								addImgIds += "," + img_.getImgId();
							}
						}
					} else {
						proAddImgs += "," + img;
					}

				}
				patentDeblocked.setImgIds(StringUtil.trimComma((patentDeblocked.getImgIds() == null ? "": patentDeblocked.getImgIds()) + addImgIds));
				patentDeblocked.setRegImgPath(StringUtil.trimComma(proAddImgs));
			}
		}
		
		if (isUpdate) { //更新
			
			String currentTime = new DateUtil().getDateTime();
			patentDeblocked.setModifyTime(currentTime);
			patentDeblocked.setState(15);
			UpdateMap patentMap = new UpdateMap("PatentDeblocked");
			patentMap.addField("keywords", patentDeblocked.getKeywords());
			patentMap.addField("remark", patentDeblocked.getRemark());
			patentMap.addField("state", patentDeblocked.getState());
			patentMap.addField("cause", patentDeblocked.getCause());
			patentMap.addField("regImgPath", patentDeblocked.getRegImgPath());
			patentMap.addField("modifyTime", patentDeblocked.getModifyTime());
			patentMap.addField("imgIds", patentDeblocked.getImgIds());
			patentMap.addWhere("id", patentDeblocked.getId());
			patentMap.addWhere("comId", loginUser.getComId());
			
			if (patentDeblockedDAO.update(patentMap) > 0) {
				UserLog userLog = UserLogUtil.getUserLog(UserLog.PATENT, UserLog.MODIFY, "修改解禁违规词——" + patentDeblocked.getKeywords(), loginUser);
				patentDeblockedDAO.addUserLog(userLog);
				return "addSuccess";
			} else {
				return "operateFail";
			}
			
		}else { // 添加违规词
			
			String currentTime = new DateUtil().getDateTime();
			patentDeblocked.setCreateTime(currentTime);
			patentDeblocked.setModifyTime(currentTime);
			if (patentDeblockedDAO.addPatentDeblocked(patentDeblocked) > 0) {
				UserLog userLog = UserLogUtil.getUserLog(UserLog.PATENT, UserLog.ADD, "添加解禁违规词——" + patentDeblocked.getKeywords(), loginUser);
				patentDeblockedDAO.addUserLog(userLog);
				return "addSuccess";
			} else {
				return "operateFail";
			}
			
		}
		
	}

	public Map<String, Object> getPatentDeblockedList(QueryParams params) {
		Company company = companyDAO.findCompany(params.getLoginUser().getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}

		ListResult<PatentDeblocked> listResult = patentDeblockedDAO.findPatentDeblockedList(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		return result;
	}

	public int deletePatentDeblocked(QueryParams params) {
		params.setComId(params.getLoginUser().getComId());
		PatentDeblocked patentDeblocked = this.patentDeblockedDAO.findPatentDeblockedById(params);
		UpdateMap updateMap = new UpdateMap("PatentDeblocked");
		updateMap.addField("state", 0);
		updateMap.addWhere("id", params.getTableId());
		updateMap.addWhere("comId", params.getComId());
		int num = patentDeblockedDAO.update(updateMap);
		if (num > 0) {
			UserLog userLog = UserLogUtil.getUserLog(UserLog.PATENT, UserLog.DELETE, "删除解禁违规词——" + patentDeblocked.getKeywords(), params.getLoginUser());
			patentDeblockedDAO.addUserLog(userLog);
		}
		return num;
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

	public void setPatentDeblockedDAO(PatentDeblockedDAO patentDeblockedDAO) {
		this.patentDeblockedDAO = patentDeblockedDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setImageDAO(ImageDAO imageDAO) {
		this.imageDAO = imageDAO;
	}
}
