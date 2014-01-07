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
	 * <p>����˾ID��ͼƬID��ѯͼƬ
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
	 * <p>����˾ID���鿴ͼƬ������
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
	 * <p>��������ϲ�ѯͼƬ��Ϣ
	 * @param params
	 * <pre>
	 *   imgName 	Ĭ��null
	 *   imgType 	Ĭ��-1
	 *   sortBy 	Ĭ��imgId desc
	 *   pageNo		Ĭ��1
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
	 * ��ѯ��ͬͼƬ���͵�����
	 * @param comId
	 * @return
	 * <pre>
	 *   sum 			ͼƬ����
	 *   imgType[1-9] 	ÿ�����͵�����
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Integer> findNumByImgType(int comId) {
		Map<String, Integer> numMap = new HashMap<String, Integer>();
		// ��ʼ��
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
	 * ��ѯһ��ͼƬʹ�õĴ���
	 * @param params
	 * <pre>
	 *   table ��Ҫ��ѯ�ı�
	 *   filed Ĭ��null ���д洢ͼƬ���ֶ���
	 *   imgId 
	 * </pre>
	 * @return 
	 */
	public int findNumByUsed(QueryParams params) {
		Integer num = (Integer) this.getSqlMapClientTemplate().queryForObject("image.findNumByUsed", params);
		return num == null ? 0 : num;
	}
	
	/**
	 * ͨ��comId ��ѯ��Ӧ��˾�� Ӫҵִ�� /��Чƾ֤ ͼƬ����
	 * @return
	 */
	public int findLicenseCount(int comId) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("image.findLicenseCount", comId);
	}
}
