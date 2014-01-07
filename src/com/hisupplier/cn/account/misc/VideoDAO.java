/* 
 * Created by sunhailin at Nov 2, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Menu;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.entity.Video;
import com.hisupplier.commons.entity.cn.Group;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.DateUtil;

/**
 * @author sunhailin
 *
 */
public class VideoDAO extends DAO {

	/**
	 * 查找视频分组列表
	 * @param params
	 * <pre>
	 *   pageNo
	 *   pageSize
	 * </pre>
	 * @return
	 * <pre>
	 *   list
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public List<Group> findVideoGroupList(QueryParams params) {
		List<Group> list = (ArrayList<Group>) this.getSqlMapClientTemplate().queryForList("video.findVideoGroupList", params);
		for (Group group : list) {
			group.setCreateTime(DateUtil.formatDateTime(group.getCreateTime()));
			group.setModifyTime(DateUtil.formatDateTime(group.getModifyTime()));
		}
		return list;
	}

	/**
	 * 查询单个视频分组
	 * @param params
	 * <pre>
	 *   groupId
	 * </pre>
	 * @return
	 * <pre>
	 *   Group
	 * </pre>
	 */
	public Group findVideoGroup(QueryParams params) {
		return (Group) this.getSqlMapClientTemplate().queryForObject("video.findVideoGroup", params);
	}
	
	/**
	 * 通过名称查找组
	 * @param params
	 * @return
	 */
	public int findVideoGroupByName(QueryParams params){
		params.setComId(params.getLoginUser().getComId());
		return (Integer)this.getSqlMapClientTemplate().queryForObject("video.findVideoGroupByName",params);
		
	}
	

	/**
	 * 添加视频分组
	 * @param group
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *   int
	 * </pre>
	 */
	public int addVideoGroup(Group group) {
		return (Integer) this.getSqlMapClientTemplate().insert("video.addVideoGroup", group);
	}

	/**
	 * 添加视频
	 * @param video
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *   int
	 * </pre>
	 */
	public int addVideo(Video video) {
		return (Integer) this.getSqlMapClientTemplate().insert("video.addVideo", video);
	}

	/**
	 * 查找视频使用数量
	 * @param comId
	 * @return
	 * <pre>
	 * int[]{
	 * 	int[0] = productVideoCount 产品中使用的数量
	 * 	int[1] = menuVideoCount 菜单中使用的数量
	 *  int[2] = rejectVideoCount 审核未通过的数量
	 *  }
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public int[] findVideoCount(int comId) {
		int[] videoCount = new int[3];
		Map<String, Integer> map = (HashMap<String, Integer>) this.getSqlMapClientTemplate().queryForObject("video.findVideoCount", comId);
		videoCount[0] = map.get("productVideoCount");
		videoCount[1] = map.get("menuVideoCount");
		videoCount[2] = map.get("rejectVideoCount");
		return videoCount;
	}

	/**
	 * 删除视频时，查询视频是否被使用
	 * @param comId
	 * @param videoId
	 * @return
	 * <pre>
	 * count 被使用的次数，count = 0 表示未被使用
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public int findVideoUseCount(int comId, int videoId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("comId", comId);
		params.put("videoId", videoId);
		Map map = (HashMap) this.getSqlMapClientTemplate().queryForObject("video.findVideoUseCount", params);

		int count = Integer.parseInt(map.get("useInCompanyCount").toString());
		count += Integer.parseInt(map.get("useInProductCount").toString());
		count += Integer.parseInt(map.get("useInMenuCount").toString());
		return count;
	}

	/**
	 * 查询视频列表
	 * @param comId
	 * @param userId
	 * @param queryBy
	 * @param queryText
	 * @param videoType
	 * @param groupId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * <pre>
	 * ListResult
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public ListResult<Video> findVideoList(int comId, String queryBy, String queryText, String videoType, int groupId, int pageNo, int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("queryBy", queryBy);
		map.put("queryText", queryText);
		map.put("videoType", videoType);
		map.put("groupId", groupId);
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("video.findVideoListCount", map);

		Page page = PageFactory.createPage(resultTotal, pageNo, pageSize);
		map.put("startRow", page.getStartRow());
		map.put("pageSize", pageSize);
		List<Video> list = (ArrayList<Video>) this.getSqlMapClientTemplate().queryForList("video.findVideoList", map);

		for (Video video : list) {
			video.setCreateTime(DateUtil.formatDateTime(video.getCreateTime()));
			video.setModifyTime(DateUtil.formatDateTime(video.getModifyTime()));
		}
		return new ListResult<Video>(list, page);
	}

	/**
	 * 查询视频在产品或菜单中的使用次数
	 * @param comId
	 * @param videoId
	 * @param tableType
	 * @return
	 * <pre>
	 * list<HashMap> 每个HashMap里边存放以videoId，count为键的键值对
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> findUseCount(int comId, int[] videoId, String tableType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("videoId", videoId);
		map.put("tableType", tableType);
		return (ArrayList<HashMap>) this.getSqlMapClientTemplate().queryForList("video.findUseCount", map);
	}

	/**
	 * 查找视频
	 * @param comId
	 * @param videoId
	 * @return Video
	 */
	public Video findVideo(int comId, int videoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("videoId", videoId);
		return (Video) this.getSqlMapClientTemplate().queryForObject("video.findVideo", map);
	}

	/**
	 * 查找使用视频的菜单列表
	 * @param comId
	 * @param videoId
	 * @param pageNo
	 * @param pageSize
	 * @return ListResult
	 */
	@SuppressWarnings("unchecked")
	public ListResult<Menu> findVideoMenuList(int comId, int videoId, int pageNo, int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("videoId", videoId);
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("video.findVideoMenuListCount", map);

		Page page = PageFactory.createPage(resultTotal, pageNo, pageSize);
		map.put("startRow", page.getStartRow());
		map.put("pageSize", pageSize);
		List<Menu> list = (ArrayList<Menu>) this.getSqlMapClientTemplate().queryForList("video.findVideoMenuList", map);

		for (Menu menu : list) {
			menu.setCreateTime(DateUtil.formatDateTime(menu.getCreateTime()));
			menu.setModifyTime(DateUtil.formatDateTime(menu.getModifyTime()));
		}
		return new ListResult<Menu>(list, page);
	}

	/**
	 * 查询使用视频的产品列表
	 * @param comId
	 * @param videoId
	 * @param pageNo
	 * @param pageSize
	 * @return ListResult
	 */
	@SuppressWarnings("unchecked")
	public ListResult<Product> findVideoProductList(int comId, int videoId, int pageNo, int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("videoId", videoId);
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("video.findVideoProductListCount", map);

		Page page = PageFactory.createPage(resultTotal, pageNo, pageSize);
		map.put("startRow", page.getStartRow());
		map.put("pageSize", pageSize);
		List<Product> list = (ArrayList<Product>) this.getSqlMapClientTemplate().queryForList("video.findVideoProductList", map);

		for (Product product : list) {
			product.setCreateTime(DateUtil.formatDate(product.getCreateTime()));
			product.setModifyTime(DateUtil.formatDate(product.getModifyTime()));
		}
		return new ListResult<Product>(list, page);
	}
	

	/**
	 * 
	 * @author wuyaohui
	 * @param comId
	 * @return
	 * Map {
	 * 		productVideoIds
	 * 		menuVideoIds
	 * }
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<Integer>> findVideoIdByProductAndMenu(int comId) {
		Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
		result.put("productVideoIds", this.getSqlMapClientTemplate().queryForList("video.findVideoIdByProduct",comId));
		result.put("menuVideoIds", this.getSqlMapClientTemplate().queryForList("video.findVideoIdByMenu",comId));
		return result;
	}

}
