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
		// ͼƬ����
		if (imgCount >= PatentDeblocked.IMG_MAX) {
			// ͼƬ����
			result.put("isImgFull", true);
		}

		result.put("licensePage", true);
		result.put("image", new Image());
		result.put("userInfo", user);
		result.put("company", company);
		//jsp from�ύ��ַ���������ı�   
		result.put("submitURL", "/patent/patentDeblocked_submit.htm"); 
		result.put("titleText", "�������");
		result.put("patent", patentDeblocked);
		return result;
	}
	
	public Map<String, Object> getPatentDeblockedError(LoginUser loginUser, PatentDeblocked patentDeblocked) {
		Map<String, Object> result = this.getPatentDeblocked(loginUser, patentDeblocked);
		patentDeblocked.setRegImgPath(patentDeblocked.getFormImgs());// ��֤ʧ�ܱ���ͼƬ
		patentDeblocked.setImgIds(patentDeblocked.getFormImgIds());// ��֤ʧ�ܱ���ͼƬid
		result.put("patent", patentDeblocked);
		return result;
	}
	
	public Map<String, Object> getPatentDeblockedEditError(LoginUser loginUser, PatentDeblocked patentDeblocked) {
		Map<String, Object> result = getPatentDeblocked(loginUser, patentDeblocked);
		patentDeblocked.setRegImgPath(patentDeblocked.getFormImgs());
		patentDeblocked.setImgIds(patentDeblocked.getFormImgIds());// ��֤ʧ�ܱ���ͼƬid
		//Ӫҵִ��/��Чƾ֤��ͼƬ��ʾ��JSPҳ��  ͨ��set images��������
		result.put("submitURL", "/patent/patentDeblocked_edit_submit.htm");
		result.put("titleText", "��������޸�");
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
		//Ӫҵִ��/��Чƾ֤��ͼƬ��ʾ��JSPҳ��  ͨ��set images��������
		result.put("patent", patentDeblocked);
		result.put("submitURL", "/patent/patentDeblocked_edit_submit.htm");
		result.put("titleText", "��������޸�");
		return result;
	}
	
	public String addPatentDeblocked(PatentDeblocked patentDeblocked, LoginUser loginUser, boolean isUpdate) {
		StringUtil.trimToEmpty(patentDeblocked, "remark,cause,keywords");
		if (StringUtil.isBlank(patentDeblocked.getKeywords())) {
			return "����дΥ���";
		}
		if (patentDeblocked.getKeywords().length() > 120) {
			return "�����Υ��ʹ���";
		}
		if (patentDeblocked.getRemark().length() > 500) {
			return "����ı�ע����";
		}
		if (patentDeblocked.getPatentImgs() == null) { // ��ӵ�ͼƬ
			patentDeblocked.setRegImgPath("");
			return "����Ҫ�ϴ�һ�� ����Чƾ֤��";
		} else {
			
			if (!PatentUtil.checkAddKeywords(patentDeblocked.getKeywords())) {
				return "��Ҫ����Ľ��Υ��ʲ������ڣ�����������";
			}
			if (patentDeblocked.getPatentImgs().length > 3) {
				return "��Чƾ֤�������ϴ�3��"; 
			}
			if (StringUtil.isNotEmpty(patentDeblocked.getFormImgIds())) {
				patentDeblocked.setImgIds(patentDeblocked.getFormImgIds());
			}
			patentDeblocked.setComId(loginUser.getComId());
			
			if (isUpdate) {
				List<PatentDeblocked> isSubmit = patentDeblockedDAO.findPatentDeblockedIsUpdate(patentDeblocked);
				if (isSubmit.size() > 0) {
					return "��Υ����Ѿ����룬�����ظ�����";
				}
			}else {
				List<String> keywordsList = patentDeblockedDAO.findPatentDeblockedKeywords(patentDeblocked);
				if (keywordsList.size() > 0) {
						return "���Ѿ�������˽��Υ��ʣ������ظ�����";
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
							return "ͼƬ����";
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
								return "ͼƬ�ϴ�ʧ��";
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
		
		if (isUpdate) { //����
			
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
				UserLog userLog = UserLogUtil.getUserLog(UserLog.PATENT, UserLog.MODIFY, "�޸Ľ��Υ��ʡ���" + patentDeblocked.getKeywords(), loginUser);
				patentDeblockedDAO.addUserLog(userLog);
				return "addSuccess";
			} else {
				return "operateFail";
			}
			
		}else { // ���Υ���
			
			String currentTime = new DateUtil().getDateTime();
			patentDeblocked.setCreateTime(currentTime);
			patentDeblocked.setModifyTime(currentTime);
			if (patentDeblockedDAO.addPatentDeblocked(patentDeblocked) > 0) {
				UserLog userLog = UserLogUtil.getUserLog(UserLog.PATENT, UserLog.ADD, "��ӽ��Υ��ʡ���" + patentDeblocked.getKeywords(), loginUser);
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
			UserLog userLog = UserLogUtil.getUserLog(UserLog.PATENT, UserLog.DELETE, "ɾ�����Υ��ʡ���" + patentDeblocked.getKeywords(), params.getLoginUser());
			patentDeblockedDAO.addUserLog(userLog);
		}
		return num;
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
