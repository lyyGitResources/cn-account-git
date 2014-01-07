package com.hisupplier.cn.account.inquiry;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Inquiry;
import com.hisupplier.cn.account.entity.InquiryReply;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.member.UserDAO;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.entity.Attachment;
import com.hisupplier.commons.entity.cn.Company;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.UploadUtil;

public class InquiryService {

	private InquiryDAO inquiryDAO;
	private UserDAO userDAO;
	private CompanyDAO companyDAO;
	
	
	/**
	 * 询盘管理
	 * @param params
	 * <pre>
	 *   loginUser.comId    默认-1
	 *   userId             默认-1
	 *   state              默认-1
	 *   sortBy             默认inqId desc
	 *   pageNo             默认1
	 *   queryBy            默认null
	 *   queryText          默认null
	 *   countryCode        默认null
	 *   read               默认-1
	 * </pre>
	 * @return
	 * <pre>
	 *   userList
	 *   readCount
	 *   unreadCount
	 *   inquiryCount
	 *   listResult
	 * </pre>
	 */
	public Map<String, Object> getInquiryList(QueryParams params) {
		params.setState(CN.STATE_PASS);
		if(!params.getLoginUser().isAdmin()){
			params.setUserId(params.getLoginUser().getUserId());
		}
		int comId = params.getLoginUser().getComId();

		List<User> userList = null;
		if (params.getLoginUser().isAdmin() && params.getLoginUser().getMemberType() == CN.GOLD_SITE) {
			userList = userDAO.findUserList(comId);
			if (params.getUserId() == -1) {
				params.setUserId(params.getLoginUser().getUserId());
			}
		}

		int readCount = inquiryDAO.findInquiryCount(comId, params.getUserId(), 1, -1, CN.STATE_PASS);
		int unreadCount = inquiryDAO.findInquiryCount(comId, params.getUserId(), 0, -1, CN.STATE_PASS);
		int recommendCount = inquiryDAO.findInquiryCount(comId, params.getUserId(), -1, 1, CN.STATE_PASS);
		int unRecommendCount = inquiryDAO.findInquiryCount(comId, params.getUserId(), -1, 0, CN.STATE_PASS);

		ListResult<Inquiry> listResult = inquiryDAO.findInquiryList(params);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("userList", userList);

		result.put("readCount", readCount);
		result.put("unreadCount", unreadCount);
		result.put("recommendCount", recommendCount);
		result.put("unRecommendCount", unRecommendCount);
		result.put("inquiryCount", readCount + unreadCount);

		result.put("listResult", listResult);
		return result;
	}

	/**
	 * 询盘查看
	 * @param params
	 * <pre>
	 *   inqId[]            默认-1
	 *   loginUser.userId   默认-1
	 *   loginUser.comId    默认-1			
	 * </pre>
	 * @return
	 * <pre>
	 *   inquiry
	 *   replyList
	 *   preId
	 *   nextId
	 * </pre>
	 */
	public Map<String, Object> getInquiryView(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int userId = params.getLoginUser().getUserId();
		int inqId[] = params.getInqId();

		Inquiry inquiry = inquiryDAO.findInquiry(comId, inqId[0]);
		if (inquiry == null) {
			throw new PageNotFoundException();
		}
		inquiry.setCreateTime(DateUtil.formatDateTime(inquiry.getCreateTime()));

		List<InquiryReply> replyList = inquiryDAO.findInquiryReplyList(comId, inqId[0]);

		int admin = params.getLoginUser().isAdmin() ? 1 : 0;
		int preId = inquiryDAO.findInquiryPreId(comId, userId, inqId[0], admin);
		int nextId = inquiryDAO.findInquiryNextId(comId, userId, inqId[0], admin);

		if (!inquiry.isRead()) {
			UpdateMap Map = new UpdateMap("Inquiry");
			Map.addField("isRead", 1);
			Map.addWhere("comId", comId);
			Map.addWhere("inqId", inqId[0]);
			inquiryDAO.update(Map);
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("inquiry", inquiry);
		result.put("replyList", replyList);
		result.put("preId", preId);
		result.put("nextId", nextId);
		return result;
	}

	/**
	 * 询盘查看回复
	 * @param params
	 * <pre>
	 *   inqId[]            默认-1
	 *   loginUser.comId    默认-1			
	 * </pre>
	 * @return
	 * <pre>
	 *   reply
	 * </pre>
	 */
	public Map<String, Object> getInquiryReplyView(QueryParams params) {
		int comId = params.getLoginUser().getComId();

		InquiryReply inquiryReply = inquiryDAO.findInquiryReply(comId, params.getId());
		if (inquiryReply == null) {
			throw new PageNotFoundException();
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("reply", inquiryReply);
		return result;
	}

	/**
	 * 询盘回复
	 * @param params
	 * <pre>
	 *   inqId[]            默认-1
	 *   loginUser.comId    默认-1			
	 * </pre>
	 * @return
	 * <pre>
	 *   inquiry
	 *   fromName
	 *   fromEmail
	 * </pre>
	 */
	public Map<String, Object> getInquiryReplyAdd(QueryParams params) {
		int inqId[] = params.getInqId();
		Inquiry inquiry = inquiryDAO.findInquiry(params.getLoginUser().getComId(), inqId[0]);
		if (inquiry == null) {
			throw new PageNotFoundException();
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("inquiry", inquiry);
		result.put("fromName", params.getLoginUser().getContact());
		result.put("fromEmail", params.getLoginUser().getEmail());
		return result;
	}

	/**
	 * 返回询盘报表页面
	 * @param params
	 * <pre>
	 *   userId             默认-1
	 *   state              默认-1
	 *   queryText          默认null
	 *   countryCode        默认null
	 *   loginUser.comId    默认-1			
	 * </pre>
	 * @return
	 * <pre>
	 *   userList
	 *   chartCode
	 * </pre>
	 */
	public Map<String, Object> getInquiryChart(QueryParams params) {
		if(!params.getLoginUser().isAdmin()){
			params.setUserId(params.getLoginUser().getUserId());
		}
		int comId = params.getLoginUser().getComId();

		List<User> userList = null;
		if (params.getLoginUser().isAdmin() && params.getLoginUser().getMemberType() == CN.GOLD_SITE) {
			userList = userDAO.findUserList(comId);
		}
		Calendar beginTime = new DateUtil().getCalendar();
		beginTime.add(Calendar.MONTH, -4);
		List<Inquiry> list = inquiryDAO.findInquiryChart(params);
		if (list.size() < 6) {
			int month = new DateUtil().getCalendar().get(Calendar.MONTH) + 1;
			int year = new DateUtil().getCalendar().get(Calendar.YEAR);
			
			Set<Integer> temp = new HashSet<Integer>(6);
			for (Inquiry tep : list) {
				String[] data = tep.getYearMonth().split("-");
				int period = 0;
				if (data[0].equals(year + ""))
					period = month - Integer.parseInt(data[1]);
				else
					period = month + (12 - Integer.parseInt(data[1]));
				tep.setPeriod(period);
				temp.add(tep.getPeriod());
			}
			for (int i = 5; i >= 0; i--) {
				if (!temp.contains(i)) {
					Inquiry a = new Inquiry();
					a.setNumber(0);
					int tmp_year = i < month ? year : year - 1;
					int tmp_month = i > month ? month + 12 - i : month - i; 
					a.setYearMonth(tmp_year + "-" + (tmp_month < 10 ? "0" + tmp_month : tmp_month));
					a.setPeriod(i);
					list.add(5 - i, a);
				}
			}
			temp = null;
		}
		
		String title = TextUtil.getText(InquiryService.class, "inquiry.charTitle", "zh");
		//得到flash代码
		StringBuilder strXML = new StringBuilder("<chart palette='2' caption='")
			.append(title).append("' showValues='1' baseFont='Arial' baseFontSize='16' decimalPrecision='0'");
		StringBuilder xml = new StringBuilder();
		int max=0;
		for (int i = 0; i < list.size(); i++) {
			xml.append("<set label='").append(list.get(i).getYearMonth()).append("' value='").append(list.get(i).getNumber()).append("' />");
			if(max < list.get(i).getNumber())
				max = list.get(i).getNumber();
			//strXML = strXML + xml;
		}
		if(max < 5)
			strXML.append("yAxisMaxValue='").append(max+1).append("' numDivLines='").append(max).append("'>");
		else 
			strXML.append(">");
		strXML.append(xml).append("</chart>");
		String chartCode = FusionChartsCreator.createChart("/img/swf/Column3D.swf", "", strXML.toString(), "CatCum", 750, 250, false, false);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("userList", userList);
		result.put("chartCode", chartCode);
		return result;
	}

	/**
	 * 返回询盘下载页面
	 * @param params
	 * <pre>
	 *   userId             默认-1
	 *   state              默认-1
	 *   queryText          默认null
	 *   countryCode        默认null
	 *   loginUser.comId    默认-1			
	 * </pre>
	 * @return
	 * <pre>
	 *   userList
	 *   inquiryList
	 * </pre>
	 */
	public Map<String, Object> getInquiryDownloadView(QueryParams params) {
		if(!params.getLoginUser().isAdmin()){
			params.setUserId(params.getLoginUser().getUserId());
		}
		int comId = params.getLoginUser().getComId();

		List<User> userList = null;
		if (params.getLoginUser().isAdmin() && params.getLoginUser().getMemberType() == CN.GOLD_SITE) {
			userList = userDAO.findUserList(comId);
		}

		List<Inquiry> inquiryList = inquiryDAO.findInquiryChart(params);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("userList", userList);
		result.put("inquiryList", inquiryList);
		return result;
	}

	/**
	 * 返回询盘下载zip压缩数据
	 * @param params
	 * <pre>
	 *   userId    默认-1
	 *   month     默认null			
	 * </pre>
	 * @return
	 * <pre>
	 *   inquiryList
	 * </pre>
	 */
	public Map<String, Object> getInquiryDownload(QueryParams params) {

		List<Inquiry> inquiryList = inquiryDAO.findInquiryDownload(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("inquiryList", inquiryList);
		return result;
	}

	/**
	 * 询盘设置
	 * @param params
	 * <pre>
	 *   comId  默认-1		
	 * </pre>
	 * @return
	 * <pre>
	 *   company
	 * </pre>
	 */
	public Map<String, Object> getInquirySet(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		Company company = companyDAO.findCompany(comId);
		if (company == null) {
			throw new PageNotFoundException();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("company", company);
		return result;
	}

	/**
	 * 询盘回收站
	 * @param params
	 * <pre>
	 *   loginUser.comId    默认-1
	 *   userId             默认-1
	 *   state              默认-1
	 *   sortBy             默认inqId desc
	 *   pageNo             默认1
	 *   queryBy            默认null
	 *   queryText          默认null
	 *   countryCode        默认null
	 *   read               默认-1
	 * </pre>
	 * @return
	 * <pre>
	 *   userList
	 *   listResult
	 * </pre>
	 */
	public Map<String, Object> getInquiryRecycleList(QueryParams params) {
		params.setState(CN.STATE_RECYCLE);
		int comId = params.getLoginUser().getComId();
		List<User> userList = null;

		if (params.getLoginUser().getMemberType() == CN.GOLD_SITE) {
			userList = userDAO.findUserList(comId);
		}
		ListResult<Inquiry> inquiryList = inquiryDAO.findInquiryList(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("userList", userList);
		result.put("listResult", inquiryList);
		return result;
	}

	/**
	 * 新增询盘回复
	 * @param inquiryReply
	 * @return
	 */
	public String addInquiryReply(InquiryReply inquiryReply) {
		String currentTime = new DateUtil().getDateTime();
		inquiryReply.setCreateTime(currentTime);
		// 上传询盘附件到图片服务器
		if (inquiryReply.getUpload() != null) {
			StringBuffer buff = new StringBuffer();
			for (int i = 0; i < inquiryReply.getUpload().length; i++) {
				Attachment attachment = null;
				try {
					attachment = UploadUtil.uploadFileStream(0, 1, inquiryReply.getUploadFileName()[i], new FileInputStream(inquiryReply.getUpload()[i]));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				if (attachment != null) {
					buff.append(attachment.getFilePath()).append(",");
				}
			}
			if (buff.length() > 0) {
				buff.deleteCharAt(buff.length() - 1);
			}
			inquiryReply.setFilePath(buff.toString());
		} else {
			inquiryReply.setFilePath("");
		}
		
		int num = inquiryDAO.addInquiryReply(inquiryReply);
		if (num > 0) {
			UpdateMap Map = new UpdateMap("Inquiry");
			Map.addField("replyCount", "+", 1);
			Map.addWhere("comId", inquiryReply.getComId());
			Map.addWhere("inqId", inquiryReply.getInqId());
			inquiryDAO.update(Map);
		}
		return "replaySuccess";
	}

	/**
	 * 询盘回收站批量还原
	 * @param params
	 * <pre>
	 *   LoginUser.comId   默认-1
	 *   inqIds            默认-1
	 * </pre>
	 * @return
	 */
	public String updateInquiryRecycleReuse(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int num = 0;

		int inqIds[] = params.getInqId();
		for (int i = 0; i < inqIds.length; i++) {
			UpdateMap Map = new UpdateMap("Inquiry");
			Map.addField("state", CN.STATE_PASS);
			Map.addWhere("comId", comId);
			Map.addWhere("inqId", inqIds[i]);
			num = inquiryDAO.update(Map);
		}
		if (num > 0) {
			UpdateMap updateMap = new UpdateMap("CompanyExtra");
			updateMap.addField("inquiryDelCount", "-", inqIds.length);
			updateMap.addWhere("comId", comId);
			inquiryDAO.update(updateMap);
		}
		if (num > 0) {
			return "inquiry.reuseSuccess";
		}
		return "operateFail";
	}

	/**
	 * 询盘回收站批量删除
	 * @param params
	 * <pre>
	 *   LoginUser.comId   默认-1
	 *   inqIds            默认-1
	 * </pre>
	 * @return
	 */
	public String updateInquiryRecycleDelete(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int num = 0;

		int inqIds[] = params.getInqId();
		for (int i = 0; i < inqIds.length; i++) {
			UpdateMap Map = new UpdateMap("Inquiry");
			Map.addField("state", CN.STATE_DELETE);
			Map.addWhere("comId", comId);
			Map.addWhere("inqId", inqIds[i]);
			num = inquiryDAO.update(Map);
		}
		if (num > 0) {
			UpdateMap updateMap = new UpdateMap("CompanyExtra");
			updateMap.addField("inquiryDelCount", "-", inqIds.length);
			updateMap.addWhere("comId", comId);
			inquiryDAO.update(updateMap);
		}
		if (num > 0) {
			return "deleteSuccess";
		}
		return "operateFail";
	}

	/**
	 * 询盘回收站批量清空
	 * @param params
	 * <pre>
	 *   LoginUser.comId   默认-1
	 *   inqIds            默认-1
	 * </pre>
	 * @return
	 */
	public String updateInquiryRecycleEmpty(QueryParams params) {
		int comId = params.getLoginUser().getComId();

		UpdateMap Map = new UpdateMap("Inquiry");
		Map.addField("state", CN.STATE_DELETE);
		Map.addWhere("comId", comId);
		Map.addWhere("state", CN.STATE_RECYCLE);
		int num = inquiryDAO.update(Map);

		if (num > 0) {
			UpdateMap updateMap = new UpdateMap("CompanyExtra");
			updateMap.addField("inquiryDelCount", 0);
			updateMap.addWhere("comId", comId);
			inquiryDAO.update(updateMap);
		}
		if (num > 0) {
			return "deleteSuccess";
		}
		return "operateFail";
	}

	/**
	 * 更新询盘设置
	 * @param params
	 * <pre>
	 *   LoginUser.comId     默认-1
	 *   inquiryReceive      默认-1
	 *   receiveRecommend    默认-1
	 * </pre>
	 * @return
	 */
	public String updateInquirySet(QueryParams params) {
		int num = 0;
		if (params.getInquiryReceive() != -1 && params.getReceiveRecommend() != -1) {
			UpdateMap Map = new UpdateMap("CompanyExtra");
			Map.addField("inquiryReceive", params.getInquiryReceive());
			Map.addField("isReceiveRecommend", params.getReceiveRecommend());
			Map.addWhere("comId", params.getLoginUser().getComId());
			num = companyDAO.update(Map);
		}
		if (num > 0) {
			return "editSuccess";
		}
		return "operateFail";
	}

	/**
	 * 询盘批量删除
	 * @param params
	 * <pre>
	 *   LoginUser.comId    默认-1
	 *   InqId              默认-1
	 * </pre>
	 * @return
	 */
	public String deleteInquiry(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int num = 0;

		Company company = companyDAO.findCompany(comId);
		if (company == null) {
			throw new PageNotFoundException();
		}

		int InquiryRecycleNum = company.getInquiryDelCount();
		int deleteNum = params.getInqId().length;
		if (InquiryRecycleNum + deleteNum > 200) {
			return "inquiry.deleteLimit";
		} else {
			int inqIds[] = params.getInqId();
			for (int i = 0; i < inqIds.length; i++) {
				UpdateMap Map = new UpdateMap("Inquiry");
				Map.addField("state", CN.STATE_RECYCLE);
				Map.addWhere("comId", comId);
				Map.addWhere("inqId", inqIds[i]);
				num = inquiryDAO.update(Map);
			}

			if (num > 0) {
				UpdateMap Map = new UpdateMap("CompanyExtra");
				Map.addField("inquiryDelCount", "+", deleteNum);
				Map.addWhere("comId", comId);
				inquiryDAO.update(Map);
			}
		}
		if (num > 0) {
			return "deleteSuccess";
		}
		return "operateFail";
	}

	public void setInquiryDAO(InquiryDAO inquiryDAO) {
		this.inquiryDAO = inquiryDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}
}
