package com.hisupplier.cn.account.alert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.TradeAlert;
import com.hisupplier.commons.util.DateUtil;

public class TradeAlertDAO extends DAO {

	/**
	 * ��comId��userId��ѯTradeAlertͳ������
	 * @param comId
	 * @param userId
	 * @return
	 */
	public int findTradeAlertCount(int comId) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("alert.findTradeAlertCount", comId);
	}

	/**
	 * @param params
	 * <pre>
	 *    loginUser.comId
	 *    loginUser.userId
	 * </pre>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TradeAlert> findTradeAlertList(QueryParams params) {
		List<TradeAlert> alertList = (List<TradeAlert>) this.getSqlMapClientTemplate().queryForList("alert.findTradeAlertList", params);
		if (alertList.size() > 0) {
			for (TradeAlert alert : alertList) {
				alert.setCreateTime(DateUtil.formatDateTime(alert.getCreateTime()));
			}
		}
		return alertList;
	}

	/**
	 * ��ѯTradeAlert
	 * @param params
	 * @return
	 */
	public TradeAlert findTradeAlert(QueryParams params) {
		return (TradeAlert) this.getSqlMapClientTemplate().queryForObject("alert.findTradeAlert", params);
	}

	/**
	 * ��ⶩ�Ĺؼ��ʣ�����ֵ����0��ʾ����
	 * @param comId
	 * @param keyword
	 * @return 
	 */
	public int findKeyword(int comId, String keyword) {
		return this.findKeyword(comId, keyword, -1);
	}
	
	/**
	 * ��ⶩ�Ĺؼ��ʣ�����ֵ����0��ʾ����
	 * @param comId
	 * @param keyword
	 * @param id �ų��Ķ���ID
	 * @return
	 */
	public int findKeyword(int comId, String keyword, int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("keyword", keyword);
		map.put("id", id);
		return (Integer) this.getSqlMapClientTemplate().queryForObject("alert.findKeyword", map);

	}

	/**
	 * ����TradeAlert
	 * @param alert
	 * @return
	 */
	public int addTradeAlert(TradeAlert alert) {
		return (Integer) this.getSqlMapClientTemplate().insert("alert.addTradeAlert", alert);
	}

	/**
	 * ����TradeAlert
	 * @param alert
	 * @return
	 */
	public int updateTradeAlert(TradeAlert alert) {
		return (Integer) this.getSqlMapClientTemplate().update("alert.updateTradeAlert", alert);
	}
}
