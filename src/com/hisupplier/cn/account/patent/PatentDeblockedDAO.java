package com.hisupplier.cn.account.patent;

import java.util.List;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.PatentDeblocked;
import com.hisupplier.cn.account.misc.QueryParams;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;

public class PatentDeblockedDAO extends DAO {
	
	public int addPatentDeblocked(PatentDeblocked patentDeblocked) {
		return (Integer) this.getSqlMapClientTemplate().insert("patentDeblocked.addPatentDeblocked", patentDeblocked);
	}
	
	@SuppressWarnings("unchecked")
	public ListResult<PatentDeblocked> findPatentDeblockedList(QueryParams params) {
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("patentDeblocked.findPatentDeblockedListCount", params);
		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());
		List<PatentDeblocked> list = this.getSqlMapClientTemplate().queryForList("patentDeblocked.findPatentDeblockedList", params);
		return new ListResult<PatentDeblocked>(list, page);
	}
	
	public PatentDeblocked findPatentDeblockedById(QueryParams params) {
		return (PatentDeblocked) this.getSqlMapClientTemplate().queryForObject("patentDeblocked.findPatentDeblockedById", params);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findPatentDeblockedKeywords(PatentDeblocked patentDeblocked) {
		return this.getSqlMapClientTemplate().queryForList("patentDeblocked.findPatentDeblockedKeywords", patentDeblocked);
	}
	
	@SuppressWarnings("unchecked")
	public List<PatentDeblocked> findPatentDeblockedIsUpdate(PatentDeblocked patentDeblocked) {
		return (List<PatentDeblocked>) this.getSqlMapClientTemplate().queryForList("patentDeblocked.findPatentDeblockedIsUpdate", patentDeblocked);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findPatentDeblockedImgIds(int comId) {
		return this.getSqlMapClientTemplate().queryForList("patentDeblocked.findPatentDeblockedImgIds", comId);
	}
}
