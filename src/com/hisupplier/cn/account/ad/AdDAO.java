package com.hisupplier.cn.account.ad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.AdOrder;
import com.hisupplier.cn.account.entity.Top;
import com.hisupplier.cn.account.entity.TopOrder;
import com.hisupplier.cn.account.entity.UpgradeMail;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.DateUtil;

public class AdDAO extends DAO {

	/**
	 * ���ع�������б�
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ListResult<AdOrder> findAdOrderList(QueryParams params) {
		params.setDefaultSort(new String[] { "id" }, "id", "desc");

		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("ad.findAdOrderListCount", params);
		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());

		List<AdOrder> list = getSqlMapClientTemplate().queryForList("ad.findAdOrderList", params);
		for (int i = 0; i < list.size(); i++) {
			AdOrder adOrder = list.get(i);
			adOrder.setCreateTime(DateUtil.formatDateTime(adOrder.getCreateTime()));
		}
		return new ListResult<AdOrder>(list, page);
	}

	/**
	 * ����Topsite�����б�
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ListResult<Top> findTopList(QueryParams params) {
		params.setDefaultSort(new String[] { "topId" }, "topId", "desc");

		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("ad.findTopCount", params);
		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());

		List<Top> list = getSqlMapClientTemplate().queryForList("ad.findTopList", params);
		for (int i = 0; i < list.size(); i++) {
			Top top = list.get(i);
			top.setCreateTime(DateUtil.formatDateTime(top.getCreateTime()));
		}
		return new ListResult<Top>(list, page);
	}

	/**
	 * ����Topsite�����б�
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ListResult<TopOrder> findTopOrderList(QueryParams params) {
		params.setDefaultSort(new String[] { "id" }, "id", "desc");

		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("ad.findTopOrderCount", params);
		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());

		List<TopOrder> list = getSqlMapClientTemplate().queryForList("ad.findTopOrderList", params);
		for (int i = 0; i < list.size(); i++) {
			TopOrder topOrder = list.get(i);
			topOrder.setCreateTime(DateUtil.formatDateTime(topOrder.getCreateTime()));
		}
		return new ListResult<TopOrder>(list, page);
	}

	/**
	 * ��topIds��ѯTop�ؼ����б�
	 * @param topIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Top> findTopKeywordList(String topIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("topIds", topIds);
		List<Top> list = getSqlMapClientTemplate().queryForList("ad.findTopKeywordList", map);
		return list;
	}

	/**
	 * ��ѯTopsite�޸�ҳ�������
	 * @param comId
	 * @param topId
	 * @return
	 */
	public Top findTop(int comId, int topId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("topId", topId);
		Top top = (Top) this.getSqlMapClientTemplate().queryForObject("ad.findTop", map);
		return top;
	}

	/**
	 * ������Ա����
	 * @param upgradeMail
	 * @return
	 */
	public int addUpgradeMail(UpgradeMail upgradeMail) {
		return (Integer) this.getSqlMapClientTemplate().insert("ad.addUpgradeMail", upgradeMail);
	}

	/**
	 * �����������
	 * @param adOrder
	 * @return
	 */
	public int addAdOrder(AdOrder adOrder) {
		return (Integer) this.getSqlMapClientTemplate().insert("ad.addAdOrder", adOrder);
	}

	/**
	 * ����Topsite����
	 * @param topOrder
	 * @return
	 */
	public int addTopOrder(TopOrder topOrder) {
		return (Integer) this.getSqlMapClientTemplate().insert("ad.addTopOrder", topOrder);
	}
}
