/* 
 * Created by baozhimin at Oct 28, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.StringUtil;

/**
 * @author baozhimin
 */
public class ImageDAO extends DAO {

	/**
	 * <p>按公司ID，图片ID查询图片
	 * @param params
	 * <pre>
	 *   loginUser.comId
	 *   imgId
	 * </pre>
	 * @return
	 */
	public Image findImage(QueryParams params) {
		return (Image) this.getSqlMapClientTemplate().queryForObject("image.findImage", params);
	}
	/**
	 * <p>按公司ID，查看图片总数量
	 * @param params
	 * <pre>
	 *   loginUser.comId
	 * </pre>
	 * @return
	 */
	public int findImageCount(QueryParams params) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("image.findImageListCount", params);
	}
	/**
	 * <p>按条件组合查询图片信息
	 * @param params
	 * <pre>
	 *   imgName 	默认null
	 *   imgType 	默认-1
	 *   sortBy 	默认imgId desc
	 *   pageNo		默认1
	 * </pre>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ListResult<Image> findImageList(QueryParams params) {
		params.setDefaultSort(new String[] { "imgId" }, "imgId", "desc");
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("image.findImageListCount", params);

		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());
		List<Image> list = getSqlMapClientTemplate().queryForList("image.findImageList", params);
		for (int i = 0; i < list.size(); i++) {
			Image image = list.get(i);
			image.setFormat(StringUtil.substring(image.getImgPath(), image.getImgPath().lastIndexOf(".") + 1).toUpperCase());
		}
		return new ListResult<Image>(list, page);
	}

	/**
	 * 查询不同图片类型的数量
	 * @param comId
	 * @return
	 * <pre>
	 *   sum 			图片总数
	 *   imgType[1-9] 	每种类型的数量
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Integer> findNumByImgType(int comId) {
		Map<String, Integer> numMap = new HashMap<String, Integer>();
		// 初始化
		for (int i = 1; i < 10; i++) {
			numMap.put("imgType" + i, 0);
		}

		int count = 0;
		List<Map<Integer, Integer>> imageGroupList = (ArrayList<Map<Integer, Integer>>) this.getSqlMapClientTemplate().queryForList("image.findNumByImgType", comId);
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		if (imageGroupList.size() > 0) {
			for (int i = 0; i < imageGroupList.size(); i++) {
				map = (Map<Integer, Integer>) imageGroupList.get(i);
				numMap.remove("imgType" + map.get("imgType"));
				numMap.put("imgType" + map.get("imgType"), map.get("count") == null ? 0 : map.get("count"));
				if (map.get("count") != null) {
					count += map.get("count");
				}
			}
		}
		numMap.put("sum", count);
		return numMap;
	}

	/**
	 * 查询一张图片使用的次数
	 * @param params
	 * <pre>
	 *   table 需要查询的表
	 *   filed 默认null 表中存储图片的字段名
	 *   imgId 
	 * </pre>
	 * @return 
	 */
	public int findNumByUsed(QueryParams params) {
		Integer num = (Integer) this.getSqlMapClientTemplate().queryForObject("image.findNumByUsed", params);
		return num == null ? 0 : num;
	}
	
	/**
	 * 通过comId 查询相应公司的 营业执照 /有效凭证 图片数量
	 * @return
	 */
	public int findLicenseCount(int comId) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("image.findLicenseCount", comId);
	}
}
