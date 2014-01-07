package com.hisupplier.cn.account.inquiry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.PermissionDeniedDataAccessException;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Inquiry;
import com.hisupplier.cn.account.entity.InquiryReply;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.DateUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

public class InquiryDAO extends DAO {

	/**
	 * 按comId,read查询Inquiry数量
	 * @param comId
	 * @param userId
	 * @param read 是否已读，-1忽略条件
	 * @param recommend 是否推荐存盘，-1忽略条件
	 * @param state
	 * @return
	 */
	public int findInquiryCount(int comId, int userId, int read, int recommend, int state) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("userId", userId);
		map.put("read", read);
		map.put("recommend", recommend);
		map.put("state", state);
		return (Integer) this.getSqlMapClientTemplate().queryForObject("inquiry.findInquiryCount", map);
	}

	/**
	 * 返回询盘列表
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
	 */
	@SuppressWarnings("unchecked")
	public ListResult<Inquiry> findInquiryList(QueryParams params) {
		params.setDefaultSort(new String[] { "inqId", "fromName", "fromProvince", "Users.userId" }, "inqId", "desc");

		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("inquiry.findInquiryListCount", params);
		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());

		List<Inquiry> list = getSqlMapClientTemplate().queryForList("inquiry.findInquiryList", params);
		for (int i = 0; i < list.size(); i++) {
			Inquiry inquiry = list.get(i);
			inquiry.setCreateTime(DateUtil.formatDateTime(inquiry.getCreateTime()));
		}
		return new ListResult<Inquiry>(list, page);
	}

	/**
	 * 返回单条询盘数据
	 * @param comId
	 * @param inqId
	 * @return
	 */
	public Inquiry findInquiry(int comId, int inqId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("inqId", inqId);

		Inquiry inquiry = (Inquiry) this.getSqlMapClientTemplate().queryForObject("inquiry.findInquiry", map);
		return inquiry;
	}

	/**
	 * 查找上一条询盘ID
	 * @param comId
	 * @param userId
	 * @param inqId 本条询盘ID
	 * @param admin
	 * @return
	 */
	public int findInquiryPreId(int comId, int userId, int inqId, int admin) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("userId", userId);
		map.put("inqId", inqId);
		map.put("admin", admin);
		Object preInqId = this.getSqlMapClientTemplate().queryForObject("inquiry.findInquiryPreId", map);
		if (preInqId == null) {
			return 0;
		} else {
			return (Integer) preInqId;
		}
	}

	/**
	 * 查找下一条询盘ID
	 * @param comId
	 * @param userId
	 * @param inqId
	 * @param admin
	 * @return
	 */
	public int findInquiryNextId(int comId, int userId, int inqId, int admin) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("userId", userId);
		map.put("inqId", inqId);
		map.put("admin", admin);
		Object nextInqId = this.getSqlMapClientTemplate().queryForObject("inquiry.findInquiryNextId", map);
		if (nextInqId == null) {
			return 0;
		} else {
			return (Integer) nextInqId;
		}
	}

	/**
	 * 返回询盘报表数据
	 * @param params
	 * <pre>
	 *   userId             默认-1
	 *   state              默认-1
	 *   queryText          默认null
	 *   countryCode        默认null
	 *   loginUser.comId    默认-1			
	 * </pre>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Inquiry> findInquiryChart(QueryParams params) {
		return this.getSqlMapClientTemplate().queryForList("inquiry.findInquiryChart", params);
	}

	/**
	 * 返回询盘信息，用于询盘下载
	 * @param params
	 * <pre>
	 *   userId    默认-1
	 *   month     默认null			
	 * </pre>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Inquiry> findInquiryDownload(QueryParams params) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> monthList = new ArrayList<String>();
		String monthId[] = params.getMonth();
		for (int i = 0; i < params.getMonth().length; i++) {
			monthList.add(monthId[i]);
		}
		map.put("comId", params.getLoginUser().getComId());
		map.put("userId", params.getUserId());
		map.put("monthList", monthList);
		return (ArrayList<Inquiry>) this.getSqlMapClientTemplate().queryForList("inquiry.findInquiryDownload", map);
	}

	/**
	 * 返回询盘回复列表
	 * @param comId
	 * @param inqId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InquiryReply> findInquiryReplyList(int comId, int inqId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("inqId", inqId);

		List<InquiryReply> list = getSqlMapClientTemplate().queryForList("inquiry.findInquiryReplyList", map);
		for (int i = 0; i < list.size(); i++) {
			InquiryReply inquiryReply = list.get(i);
			inquiryReply.setCreateTime(DateUtil.formatDateTime(inquiryReply.getCreateTime()));
		}
		return list;
	}

	/**
	 * 返回询盘回复数据
	 * @param comId
	 * @param id
	 * @return
	 */
	public InquiryReply findInquiryReply(int comId, int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("id", id);

		InquiryReply inquiryReply = (InquiryReply) this.getSqlMapClientTemplate().queryForObject("inquiry.findInquiryReply", map);
		return inquiryReply;
	}

	/**
	 * 添加询盘
	 * @param inquiryList
	 * @return
	 */
	public int addInquiry(final List<Inquiry> inquiryList) {
		int num = 0;
		try {
			SqlMapClient sqlMapClient = this.getSqlMapClientTemplate().getSqlMapClient();
			sqlMapClient.startBatch();
			for (Inquiry inquiry : inquiryList) {
				int inqId = (Integer) this.getSqlMapClientTemplate().insert("inquiry.addInquiry", inquiry);
				inquiry.setInqId(inqId);
				this.getSqlMapClientTemplate().insert("inquiry.addInquiryExtra", inquiry);
				num++;
			}
			sqlMapClient.executeBatch();
		} catch (Exception e) {
			throw new PermissionDeniedDataAccessException(e.getMessage(), e);
		}
		return num;
		/*		return (Integer) this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
					public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
						executor.startBatch();
						for (Inquiry inquiry : inquiryList) {
							int inqId = (Integer) executor.insert("inquiry.addInquiry", inquiry);
							inquiry.setInqId(inqId);
							executor.insert("inquiry.addInquiryExtra", inquiry);
							inquiry.setInqId(0);
						}
						return executor.executeBatch();
					}
				});*/
	}

	/**
	 * 添加询盘回复数据
	 * @param inquiryReply
	 * @return
	 */
	public int addInquiryReply(InquiryReply inquiryReply) {
		return (Integer) this.getSqlMapClientTemplate().insert("inquiry.addInquiryReply", inquiryReply);
	}

}
