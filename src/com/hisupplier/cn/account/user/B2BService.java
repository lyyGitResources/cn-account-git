/* 
 * Created by baozhimin at Dec 7, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hisupplier.cas.CASClient;
import com.hisupplier.cas.Ticket;
import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.entity.BuyLead;
import com.hisupplier.cn.account.entity.Friend;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.entity.Register;
import com.hisupplier.cn.account.member.CompanyService;
import com.hisupplier.cn.account.product.TradeService;
import com.hisupplier.cn.account.util.PatentUtil;
import com.hisupplier.cn.search.webservice.Search;
import com.hisupplier.commons.EN;
import com.hisupplier.commons.entity.cn.CategorySuggest;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;

/**
 * @author baozhimin
 */
public class B2BService {
	
	private B2BDAO b2BDAO;
	private TradeService tradeService;
	private CompanyService companyService;
	private Search search;
	
	/**
	 * 目录建议添加
	 * @param request
	 * @param categorySuggest
	 * @return
	 * <pre>
	 * operateFail
	 * categorySuggest.addSuccess
	 * </pre>
	 */
	public String addCategorySuggest(HttpServletRequest request, CategorySuggest categorySuggest){
		categorySuggest.setCreateTime(new DateUtil().getDateTime());

		if(this.b2BDAO.addCategorySuggest(categorySuggest) > 0){
			return "categorySuggest.addSuccess";
		}else{
			return "operateFail";
		}
		
	}
	
	/**
	 * 友情链接添加
	 * @param request
	 * @param friend
	 * @return
	 * <pre>
	 * operateFail
	 * friend.addSuccess
	 * </pre>
	 */
	public String addFriend(HttpServletRequest request, Friend friend){
		String currentTime = new DateUtil().getDateTime();
		friend.setState(15);
		friend.setListOrder(this.b2BDAO.findFriendMaxListOrder() + 1);
		friend.setCreateTime(currentTime);
		friend.setModifyTime(currentTime);
		if (friend.getLinkType() != 6) {
			friend.setCatId(0);
			friend.setImgPath("");
		}
		if(this.b2BDAO.addFriend(friend) > 0){
			return "friend.addSuccess";
		}else{
			return "operateFail";
		}
		
	}
	
	/**
	 * 求购信息添加
	 * @param request
	 * @param buyLead
	 * @return
	 * <pre>
	 * buyLead.user.wrong	// 账号不存在
	 * buyLead.passwd.wrong	// 密码错误
	 * comName.used		   	// 从注册中返回
	 * email.used			// 从注册中返回
	 * addSuccess
	 * </pre>
	 */
	public String addBuyLead (HttpServletRequest request, BuyLead buyLead){
		// 已登录
		Product product = new Product();
		product.setImgPath("");
		product.setProName(buyLead.getProName());
		product.setBrief(buyLead.getBrief());
		product.setCatId(buyLead.getCatId());
		product.setValidDay(buyLead.getValidDay());
		product.setKeywords(this.getKeywords(buyLead));
		product.setProType(EN.TRADE_BUY);
		product.setDescription(buyLead.getDescription());
		
		if(CASClient.getInstance().isLogin(request)){//已登陆
			LoginUser loginUser = (LoginUser) CASClient.getInstance().getUser(request);
			
			Map<String, String> map = new HashMap<String, String>(); 
			map.put("keywords", "关键词,");
			map.put("proName", "求购主题,");
			map.put("brief", "摘要,");
			String patentKeyword = PatentUtil.checkKeyword(buyLead, map, loginUser.getComId());
			if (StringUtil.isNotEmpty(patentKeyword)) {
				return "提交失败！您提交的   " + patentKeyword.split(",")[0] +  "  包含违禁词：" + patentKeyword.split(",")[1];
			}
			
			this.tradeService.addTrade(null, product, loginUser);
		}else{
			if(buyLead.isNewUser()){
				
				Map<String, String> map = new HashMap<String, String>(); 
				map.put("keywords", "关键词,");
				map.put("proName", "求购主题,");
				map.put("brief", "摘要,");
				String patentKeyword = PatentUtil.checkKeyword(buyLead, map, 0);
				if (StringUtil.isNotEmpty(patentKeyword)) {
					return "提交失败！您提交的   " + patentKeyword.split(",")[0] +  "  包含违禁词：" + patentKeyword.split(",")[1];
				}
				
				Register register = new Register();
				register.setProvince(buyLead.getProvince());
				register.setCity(buyLead.getCity());
				register.setTown(buyLead.getTown());
				register.setStreet("");
				register.setFax("");
				register.setComName(buyLead.getComName());
				register.setRegMode(EN.REG_BUY_LEADING);
				register.setContact(buyLead.getContact());
				register.setEmail(buyLead.getNewUserEmail());
				register.setPasswd(buyLead.getNewUserPasswd());
				register.setTel(buyLead.getTel());
				register.setSex(buyLead.getSex());
				String result = this.companyService.join(register);
				
				if(StringUtil.equalsIgnoreCase(result, "addSuccess")){
					product.setUserId(register.getUserId());
					product.setComId(register.getComId());
					this.tradeService.addTrade(null, product, null, register.getComId(), register.getUserId());
				}else{
					return result;
				}
			}else{
				Ticket ticket = CASClient.getInstance().validatePasswd(buyLead.getEmail(), buyLead.getPasswd());
				if ("notfound".equals(ticket.getMessage())) {
					return "buyLead.user.wrong";
				}else if("error".equals(ticket.getMessage())){
					return "buyLead.passwd.wrong";
				}
				
				//登陆票据正确后根据comId检测违规词
				Map<String, String> map = new HashMap<String, String>(); 
				map.put("keywords", "关键词,");
				map.put("proName", "求购主题,");
				map.put("brief", "摘要,");
				String patentKeyword = PatentUtil.checkKeyword(buyLead, map, ticket.getComId());
				if (StringUtil.isNotEmpty(patentKeyword)) {
					return "提交失败！您提交的   " + patentKeyword.split(",")[0] +  "  包含违禁词：" + patentKeyword.split(",")[1];
				}
				
				this.tradeService.addTrade(null, product, null, ticket.getComId(), ticket.getUserId());
			}
		}
		
		return "addSuccess";
	}
	
	private String getKeywords(BuyLead buyLead){
		/*
		String keywords = "";
		for(String keyword : buyLead.getKeywordArray()){
			if(StringUtil.isNotBlank(keyword)){
				keywords += "," + keyword;
			}
		}
		return StringUtil.trimComma(keywords);
		*/
		return buyLead.getKeywords();
	}
	
	public void setB2BDAO(B2BDAO b2bdao) {
		b2BDAO = b2bdao;
	}

	public void setTradeService(TradeService tradeService) {
		this.tradeService = tradeService;
	}

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

	public Search getSearch() {
		return search;
	}
}
