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
	 * ��comId,read��ѯInquiry����
	 * @param comId
	 * @param userId
	 * @param read �Ƿ��Ѷ���-1��������
	 * @param recommend �Ƿ��Ƽ����̣�-1��������
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
	 * ����ѯ���б�
	 * @param params
	 * <pre>
	 *   loginUser.comId    Ĭ��-1
	 *   userId             Ĭ��-1
	 *   state              Ĭ��-1
	 *   sortBy             Ĭ��inqId desc
	 *   pageNo             Ĭ��1
	 *   queryBy            Ĭ��null
	 *   queryText          Ĭ��null
	 *   countryCode        Ĭ��null
	 *   read               Ĭ��-1
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
	 * ���ص���ѯ������
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
	 * ������һ��ѯ��ID
	 * @param comId
	 * @param userId
	 * @param inqId ����ѯ��ID
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
	 * ������һ��ѯ��ID
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
	 * ����ѯ�̱�������
	 * @param params
	 * <pre>
	 *   userId             Ĭ��-1
	 *   state              Ĭ��-1
	 *   queryText          Ĭ��null
	 *   countryCode        Ĭ��null
	 *   loginUser.comId    Ĭ��-1			
	 * </pre>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Inquiry> findInquiryChart(QueryParams params) {
		return this.getSqlMapClientTemplate().queryForList("inquiry.findInquiryChart", params);
	}

	/**
	 * ����ѯ����Ϣ������ѯ������
	 * @param params
	 * <pre>
	 *   userId    Ĭ��-1
	 *   month     Ĭ��null			
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
	 * ����ѯ�̻ظ��б�
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
	 * ����ѯ�̻ظ�����
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
	 * ���ѯ��
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
	 * ���ѯ�̻ظ�����
	 * @param inquiryReply
	 * @return
	 */
	public int addInquiryReply(InquiryReply inquiryReply) {
		return (Integer) this.getSqlMapClientTemplate().insert("inquiry.addInquiryReply", inquiryReply);
	}

}
