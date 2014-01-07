/* 
 * Created by sunhailin at Nov 2, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Menu;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.entity.UserLog;
import com.hisupplier.cn.account.entity.Video;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.util.UserLogUtil;
import com.hisupplier.cn.account.view.Button;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.entity.cn.Group;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;

/**
 * @author sunhailin
 *
 */
public class VideoService {
	private VideoDAO videoDAO;
	private CompanyDAO companyDAO;
	private final static Log log = LogFactory.getLog(VideoService.class);
	/**
	 * 查找视频分组列表
	 * @param params
	 * <pre>
	 *   pageNo		默认1
	 *   pageSize
	 * </pre>
	 * @return
	 * <pre>
	 *   groupList
	 * </pre>
	 */
	public Map<String, Object> getVideoGroupList(QueryParams params) {
		List<Group> groupList = this.videoDAO.findVideoGroupList(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("groupList", groupList);
		return result;
	}

	/**
	 * 添加/修改时取得分组信息
	 * @param params
	 * <pre>
	 *   groupId
	 * </pre>
	 * @return
	 * <pre>
	 *   formAction
	 *   groupName
	 *   groupId
	 * </pre>
	 */
	public Map<String, Object> getVideoGroup(QueryParams params) {
		String fromAction = "/video/video_group_add_submit.htm";
		String groupName = "";
		int groupId = 0;
		if (params.getGroupId() > 0) {
			Group group = this.videoDAO.findVideoGroup(params);
			if (group == null) {
				throw new PageNotFoundException();
			}
			fromAction = "/video/video_group_edit_submit.htm";
			groupName = group.getGroupName();
			groupId = group.getGroupId();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("formAction", fromAction);
		result.put("groupName", groupName);
		result.put("groupId", groupId);
		return result;
	}

	/**
	 * 添加视频分组
	 * @param params
	 * <pre>
	 *   groupName
	 * </pre>
	 * @return
	 * <pre>
	 *   addSuccess
	 *   operateFail
	 * </pre>
	 */
	public String addVideoGroup(QueryParams params) {
		params.setComId(params.getLoginUser().getComId());
		int g = videoDAO.findVideoGroupByName(params);
		if(g > 0){
			return "operateFail";
		}else{
			Group group = new Group();
			group.setGroupName(params.getGroupName());
			group.setComId(params.getLoginUser().getComId());
			group.setListOrder(0);
			group.setVideoCount(0);
			group.setVideoRejectCount(0);
			group.setState(CN.STATE_WAIT);
			String currentTime = new DateUtil().getDateTime();
			group.setCreateTime(currentTime);
			group.setModifyTime(currentTime);
			int num = this.videoDAO.addVideoGroup(group);
			if (num > 0) {
				return "addSuccess";
			} else {
				return "operateFail";
			}
		}
	}

	/**
	 * 修改视频分组
	 * @param params
	 * <pre>
	 *   groupName
	 *   groupId
	 * </pre>
	 * @return
	 * <pre>
	 *   editSuccess
	 *   operateFail
	 * </pre>
	 */
	public String updateVideoGroup(QueryParams params) {
		int ret= videoDAO.findVideoGroupByName(params);
		if(ret>0){
			return "operateFail";
		}else{
			Group group = this.videoDAO.findVideoGroup(params);
			if (group == null) {
				throw new PageNotFoundException();
			}
			UpdateMap videoGroup = new UpdateMap("VideoGroup");
			videoGroup.addField("groupName", params.getGroupName());
			videoGroup.addWhere("comId", params.getLoginUser().getComId());
			videoGroup.addWhere("groupId", params.getGroupId());
			int num = this.videoDAO.update(videoGroup);
			if (num > 0) {
				return "editSuccess";
			} else {
				return "operateFail";
			}
		}
	}

	/**
	 * 删除视频分组
	 * @param params
	 * <pre>
	 *   groupId
	 * </pre>
	 * @return
	 * <pre>
	 * 	 videoGroup.unallowed.delete	
	 *   deleteSuccess
	 *   operateFail
	 * </pre>
	 */
	public String deleteVideoGroup(QueryParams params) {
		Group group = this.videoDAO.findVideoGroup(params);
		if (group == null) {
			throw new PageNotFoundException();
		}
		if (group.getVideoCount() > 0) {
			return "videoGroup.unallowed.delete";
		}
		UpdateMap videoGroup = new UpdateMap("VideoGroup");
		videoGroup.addField("listOrder", 0);
		videoGroup.addField("videoCount", 0);
		videoGroup.addField("videoRejectCount", 0);
		videoGroup.addField("state", CN.STATE_DELETE);
		videoGroup.addField("modifyTime", new DateUtil().getDateTime());
		videoGroup.addWhere("comId", params.getLoginUser().getComId());
		videoGroup.addWhere("groupId", params.getGroupId());
		int num = this.videoDAO.update(videoGroup);
		if (num > 0) {
			return "deleteSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * 查询视频列表
	 * @param params
	 * <pre>
	 *   loginUser.comId
	 *   queryBy
	 *   queryText
	 *   videoType
	 *   groupId
	 *   pageNo
	 *   pageSize
	 * </pre>
	 * @return
	 * <pre>
	 * 	 listResult	
	 *   groupList
	 *   videoMax
	 *   videoCount
	 *   companyVideoCount
	 *   productVideoCount
	 *   menuVideoCount
	 *   rejectVideoCount
	 *   queryText
	 *   videoType
	 *   groupId
	 *   companyVideoId
	 *   productVideoIds
	 *   menuVideoIds
	 * </pre>
	 */
	public Map<String, Object> getVideoList(QueryParams params) {
		//查询当前视频数量，最大可添加的视频数量，公司、产品、菜单中使用的数量以及审核不通过的数量
		Company company = this.companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		int videoMax = company.getVideoMax();
		int videoCount = company.getVideoCount();
		int companyVideoCount = company.getVideoId() > 0 ? 1 : 0;

		int[] tmpCount = this.videoDAO.findVideoCount(params.getLoginUser().getComId());
		int productVideoCount = tmpCount[0];
		int menuVideoCount = tmpCount[1];
		int rejectVideoCount = tmpCount[2];
		//不分页查询视频分组
		List<Group> groupList = this.videoDAO.findVideoGroupList(params);
		//查询视频列表
		ListResult<Video> listResult = this.videoDAO.findVideoList(params.getLoginUser().getComId(), params.getQueryBy(), params.getQueryText(), params.getVideoType(), params.getGroupId(), params
				.getPageNo(), params.getPageSize());
		//查询视频被产品或者菜单的使用次数
		if (params.getVideoType().equals("Product") || params.getVideoType().equals("Menu")) {
			int[] videoId = new int[listResult.getList().size()];
			for (int i = 0; i < listResult.getList().size(); i++) {
				videoId[i] = listResult.getList().get(i).getVideoId();
			}
			//videoId数组不为空的时候，查询每个视频的使用次数
			if (videoId.length > 0) {
				List<HashMap> list = this.videoDAO.findUseCount(params.getLoginUser().getComId(), videoId, params.getVideoType());
				for (Video video : listResult.getList()) {
					for (HashMap map : list) {
						int id = Integer.parseInt(map.get("videoId").toString());
						if (video.getVideoId() == id) {
							video.setVideoUserCount(Integer.parseInt(map.get("count").toString()));
						}
					}
				}
			}
		}
		
		Map<String, List<Integer>> videoIds = this.videoDAO.findVideoIdByProductAndMenu(company.getComId());
		

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		result.put("groupList", groupList);
		result.put("videoMax", videoMax);
		result.put("videoCount", videoCount);
		result.put("companyVideoCount", companyVideoCount);
		result.put("productVideoCount", productVideoCount);
		result.put("menuVideoCount", menuVideoCount);
		result.put("rejectVideoCount", rejectVideoCount);
		result.put("queryText", params.getQueryText());
		result.put("videoType", params.getVideoType());
		result.put("groupId", params.getGroupId());
		result.put("companyVideoId", company.getVideoId());
		result.put("productVideoIds", videoIds.get("productVideoIds"));
		result.put("menuVideoIds", videoIds.get("menuVideoIds"));
		return result;
	}
	
	public Map<String, Object> getVideoSelect(QueryParams params){ 
		List<Group> groupList = this.videoDAO.findVideoGroupList(params);
		ListResult<Video> listResult = this.videoDAO.findVideoList(params.getLoginUser().getComId(), params.getQueryBy(), params.getQueryText(), params.getVideoType(), params.getGroupId(), params
				.getPageNo(), params.getPageSize());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		result.put("groupList", groupList);
		result.put("groupId", params.getGroupId());
		result.put("queryText", params.getQueryText());
		return result;
	}
	

	/**
	 * 取得视频上传信息
	 * @param params
	 * <pre>
	 *   loginUser.comId
	 * </pre>
	 * @return
	 * <pre>
	 * 	 allow	
	 *   videoCount
	 *   groupList
	 * </pre>
	 */
	public Map<String, Object> getVideoUpload(QueryParams params) {
		Company company = this.companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		Map<String, Object> result = new HashMap<String, Object>();
		if (company.getVideoCount() >= company.getVideoMax()) {
			result.put("allow", "false");
			result.put("videoCount", company.getVideoCount());
		} else {
			result.put("groupList", this.videoDAO.findVideoGroupList(params));
			result.put("allow", "true");
		}
		return result;
	}

	/**
	 * 取得视频修改信息
	 * @param params
	 * <pre>
	 *   loginUser.comId
	 *   videoId
	 * </pre>
	 * @return
	 * <pre>
	 * 	 video	
	 *   groupList
	 * </pre>
	 */
	public Map<String, Object> getVideoEdit(QueryParams params) {
		Video video = this.videoDAO.findVideo(params.getLoginUser().getComId(), params.getVideoId());
		if (video == null) {
			throw new PageNotFoundException();
		}
		video.setCreateTime(DateUtil.formatDateTime(video.getCreateTime()));
		video.setVideoType(params.getVideoType());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("groupList", this.videoDAO.findVideoGroupList(params));
		result.put("video", video);
		return result;
	}

	/**
	 * 更新视频
	 * @param video
	 * <pre>
	 *   state
	 *   title
	 * </pre>
	 * @return
	 * <pre>
	 * 	 editSuccess	
	 *   operateFail
	 * </pre>
	 */
	public String updateVideo(Video video, LoginUser loginUser) {
		UpdateMap updateVideo = new UpdateMap("Video");
		updateVideo.addField("title", video.getTitle());
		if (video.getOldGroupId() != video.getGroupId()) {
			updateVideo.addField("groupId", video.getGroupId());
		}
		updateVideo.addField("modifyTime", new DateUtil().getDateTime());
		updateVideo.addWhere("comId", video.getComId());
		updateVideo.addWhere("videoId", video.getVideoId());
		//修改日志
		Video oldVideo = videoDAO.findVideo(video.getComId(), video.getVideoId());
		if (videoDAO.update(updateVideo) > 0) {
			String content = "修改视频——(" + oldVideo.getTitle() + ")";
			if (!video.getTitle().equals(oldVideo.getTitle())) {
				content += "改为(" + video.getTitle() + ")";
			}
			if (video.getOldGroupId() != video.getGroupId()) {
				String newGroupName = videoDAO.findVideo(video.getComId(), video.getVideoId()).getGroupName();
				if (video.getGroupId() != 0) {
					content += "，选择分组(" + newGroupName + ")";
				} else {
					content += "，移出分组(" + oldVideo.getGroupName() + ")";
				}
			}
			if (StringUtil.isNotBlank(content)) {
				UserLog userLog = UserLogUtil.getUserLog(UserLog.VIDEO, UserLog.MODIFY, content, loginUser);
				videoDAO.addUserLog(userLog);
			}
		}

		if (video.getGroupId() != video.getOldGroupId()) {
			UpdateMap updateMap = null;
			if (video.getOldGroupId() > 0) {
				updateMap = new UpdateMap("VideoGroup");
				updateMap.addField("videoCount", "-", 1);
				updateMap.addWhere("comId", video.getComId());
				updateMap.addWhere("groupId", video.getOldGroupId());
				this.videoDAO.update(updateMap);
			}
			if (video.getGroupId() > 0) {
				updateMap = new UpdateMap("VideoGroup");
				updateMap.addField("videoCount", "+", 1);
				updateMap.addWhere("comId", video.getComId());
				updateMap.addWhere("groupId", video.getGroupId());
				this.videoDAO.update(updateMap);
			}
		}
		return "editSuccess";
	}

	/**
	 * 添加视频
	 * @param video
	 * @return
	 * <pre>
	 * 	 addSuccess	
	 *   operateFail
	 * </pre>
	 */
	public String addVideo(Video video, LoginUser loginUser) {
		if(StringUtil.isNotBlank(video.getGroupName())){
			Group group = new Group();
			group.setGroupName(video.getGroupName());
			group.setComId(loginUser.getComId());
			group.setListOrder(0);
			group.setVideoCount(0);
			group.setVideoRejectCount(0);
			group.setState(CN.STATE_WAIT);
			String currentTime = new DateUtil().getDateTime();
			group.setCreateTime(currentTime);
			group.setModifyTime(currentTime);
			video.setGroupId(this.videoDAO.addVideoGroup(group));
		}
		if (StringUtil.isBlank(video.getAsdId())) {
			return "operateFail";
		}
		
		video.setComId(loginUser.getComId());
		video.setState(CN.STATE_WAIT);
		String currentTime = new DateUtil().getDateTime();
		video.setCreateTime(currentTime);
		video.setModifyTime(currentTime);
		
		StringUtil.trimToEmpty(video, "keywords,imgPath,playPath");
		video.setImgPath(video.getImgPathShow());
		video.setPlayPath(video.getPlayPathFlash());

		int videoId = this.videoDAO.addVideo(video);
		if (videoId <= 0) {
			return "operateFail";
		}
		video.setVideoId(videoId);

		UpdateMap updateCompany = new UpdateMap("CompanyExtra");
		updateCompany.addField("videoCount", "+", 1);
		updateCompany.addWhere("comId", video.getComId());
		this.videoDAO.update(updateCompany);

		if (video.getGroupId() > 0) {
			UpdateMap videoCount = new UpdateMap("VideoGroup");
			videoCount.addField("videoCount", "+", 1);
			videoCount.addWhere("comId", video.getComId());
			videoCount.addWhere("groupId", video.getGroupId());
			this.videoDAO.update(videoCount);
		}
		
		addVideoUserLog(video.getTitle(), loginUser);

		return "addSuccess";
	}

	/**
	 * 删除视频
	 * @param params
	 * <pre>
	 *   loginUser.comId
	 *   videoId
	 * </pre>
	 * @return
	 * <pre>
	 * 	 video.used	
	 *   deleteSuccess
	 * </pre>
	 */
	public String deleteVideo(QueryParams params) {
		Video video = this.videoDAO.findVideo(params.getLoginUser().getComId(), params.getVideoId());
		if (video == null) {
			throw new PageNotFoundException();
		}

		int count = this.videoDAO.findVideoUseCount(params.getLoginUser().getComId(), params.getVideoId());
		if (count > 0) {
			return "video.used";
		}

		if (video.getGroupId() > 0) {
			UpdateMap updateVideoGroup = new UpdateMap("VideoGroup");
			updateVideoGroup.addField("videoCount", "-", 1);
			if (video.getState() == CN.STATE_REJECT) {
				updateVideoGroup.addField("videoRejectCount", "-", 1);
			}
			updateVideoGroup.addWhere("comId", video.getComId());
			updateVideoGroup.addWhere("groupId", video.getGroupId());
			updateVideoGroup.addWhere("videoCount", 0, ">");
			this.videoDAO.update(updateVideoGroup);
		}

		UpdateMap updateVideo = new UpdateMap("Video");
		updateVideo.addField("groupId", 0);
		updateVideo.addField("state", CN.STATE_DELETE);
		updateVideo.addField("modifyTime", new DateUtil().getDateTime());
		updateVideo.addWhere("videoId", video.getVideoId());
		updateVideo.addWhere("comId", video.getComId());
		//删除日志
		if (videoDAO.update(updateVideo) > 0) {
			UserLog userLog = UserLogUtil.getUserLog(UserLog.VIDEO, UserLog.DELETE,
					"删除视频——" + video.getTitle(), params.getLoginUser());
			videoDAO.addUserLog(userLog);
		}
		UpdateMap updateCompany = new UpdateMap("CompanyExtra");
		updateCompany.addField("videoCount", "-", 1);
		updateCompany.addWhere("comId", video.getComId());
		this.videoDAO.update(updateCompany);

		return "deleteSuccess";
	}

	/**
	 * 根据videoId取得菜单列表
	 * @param params
	 * <pre>
	 *   loginUser.comId
	 *   videoId
	 *   pageNo
	 *   pageSize
	 * </pre>
	 * @return
	 * <pre>
	 * 	 listResult
	 *   videoMax
	 *   videoCount
	 *   companyVideoCount
	 *   productVideoCount
	 *   menuVideoCount
	 *   rejectVideoCount
	 *   videoType
	 * </pre>
	 */
	public Map<String, Object> getVideoMenuList(QueryParams params) {
		Company company = this.companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		int videoMax = company.getVideoMax();
		int videoCount = company.getVideoCount();
		int companyVideoCount = company.getVideoId() > 0 ? 1 : 0;

		int[] tmpCount = this.videoDAO.findVideoCount(params.getLoginUser().getComId());
		int productVideoCount = tmpCount[0];
		int menuVideoCount = tmpCount[1];
		int rejectVideoCount = tmpCount[2];

		ListResult<Menu> listResult = this.videoDAO.findVideoMenuList(params.getLoginUser().getComId(), params.getVideoId(), params.getPageNo(), params.getPageSize());

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		result.put("videoMax", videoMax);
		result.put("videoCount", videoCount);
		result.put("companyVideoCount", companyVideoCount);
		result.put("productVideoCount", productVideoCount);
		result.put("menuVideoCount", menuVideoCount);
		result.put("rejectVideoCount", rejectVideoCount);
		result.put("videoType", params.getVideoType());

		return result;
	}

	/**
	 * 根据videoId取得产品列表
	 * @param params
	 * <pre>
	 *   loginUser.comId
	 *   videoId
	 *   pageNo
	 *   pageSize
	 * </pre>
	 * @return
	 * <pre>
	 * 	 listResult
	 *   videoMax
	 *   videoCount
	 *   companyVideoCount
	 *   productVideoCount
	 *   menuVideoCount
	 *   rejectVideoCount
	 *   videoType
	 * </pre>
	 */
	public Map<String, Object> getVideoProductList(QueryParams params) {
		Company company = this.companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		int videoMax = company.getVideoMax();
		int videoCount = company.getVideoCount();
		int companyVideoCount = company.getVideoId() > 0 ? 1 : 0;

		int[] tmpCount = this.videoDAO.findVideoCount(params.getLoginUser().getComId());
		int productVideoCount = tmpCount[0];
		int menuVideoCount = tmpCount[1];
		int rejectVideoCount = tmpCount[2];

		ListResult<Product> listResult = this.videoDAO.findVideoProductList(params.getLoginUser().getComId(), params.getVideoId(), params.getPageNo(), params.getPageSize());
		
		for (Product product : listResult.getList()) {
			product.addOperate(new Button("/product/product_edit.htm").appendParam("proId", product.getProId()).setName("button.editProduct").getLink());
			product.addOperate("<br />");
			product.addOperate(new Button("/trade/trade_add.htm").appendParam("proId", product.getProId()).setName("button.toTrade").getLink());
			product.addOperate("<br />");
			product.addOperate(new Button("/product/product_add.htm").appendParam("proId", product.getProId()).setName("button.addSameProduct").getLink());
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		result.put("videoMax", videoMax);
		result.put("videoCount", videoCount);
		result.put("companyVideoCount", companyVideoCount);
		result.put("productVideoCount", productVideoCount);
		result.put("menuVideoCount", menuVideoCount);
		result.put("rejectVideoCount", rejectVideoCount);
		result.put("videoType", params.getVideoType());

		return result;
	}
	
	/**
	 * 添加视频上传日志
	 * @param title 视频标题
	 * @param loginUser 当前登录用户
	 * @return
	 * <pre>
	 * 	 success
	 *   failed
	 * </pre>
	 */
	private String addVideoUserLog(String title, LoginUser loginUser) {
		String time = DateUtil.format(new Date(), "HH:mm");
		UserLog userLog = UserLogUtil.getUserLog(UserLog.VIDEO, UserLog.ADD, "上传视频——" + title + "(时间:" + time + ")", loginUser);
		String msg = null;
		try {
			int effectRow = videoDAO.addUserLog(userLog);
			if (effectRow < 0) {
				throw new Exception("添加上传视频日志失败" + loginUser.getComId());
			}
			msg = "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			msg = "failed";
		}
		return msg;
	}

	public void setVideoDAO(VideoDAO videoDAO) {
		this.videoDAO = videoDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

}
