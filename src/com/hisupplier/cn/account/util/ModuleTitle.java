/* 
 * Created by wuyaohui at 2011-11-8
 * Copyright HiSupplier.com 
 */
package com.hisupplier.cn.account.util;

import com.hisupplier.commons.util.StringUtil;


/**
 * @author wuyaohui
 *
 */
public enum ModuleTitle {
	M_PRODUCT_GROUP("m_product_group"), 
	M_SPECIAL_GROUP("m_special_group"), 
	M_TRADE_GROUP("m_trade_group"), 
	M_COMMENT("m_comment"), 
	M_PROFILE("m_profile"), 
	M_ONLINE_SERVICE("m_online_service"), 
	M_MENU_GROUP("m_menu_group"), 
	M_VIDEO("m_video"), 
	M_PRODUCT_NEWLIST("m_product_newlist"), 
	M_COMPANY_INFO("m_company_info"), 
	M_FEATURE_PRODUCT("m_feature_product"), 
	M_CONTACT("m_contact"), 
	M_PRODUCT_HOT("m_product_hot");

	private String title;

	ModuleTitle(String title) {
		this.title = title;
	}
	
	public static ModuleTitle[] L1_C1 = { 
		M_PRODUCT_GROUP, 
		M_SPECIAL_GROUP,
		M_TRADE_GROUP, 
		M_COMMENT, 
		M_PROFILE, 
		M_ONLINE_SERVICE,
		M_MENU_GROUP, M_VIDEO,
		M_PRODUCT_HOT
	};
	
	public static ModuleTitle[] L1_C5 = { 
		M_PRODUCT_NEWLIST, 
		M_COMPANY_INFO,
		M_FEATURE_PRODUCT, 
		M_CONTACT 
	};
	
	public static ModuleTitle[] L2_C1 = L1_C5;
	public static ModuleTitle[] L2_C5 = L1_C1;
	public static ModuleTitle[] L3_C1 = { 
		M_PRODUCT_GROUP, 
		M_SPECIAL_GROUP,
		M_VIDEO, 
		M_TRADE_GROUP, 
		M_COMMENT, 
		M_PROFILE, 
		M_ONLINE_SERVICE,
		M_PRODUCT_HOT
	};
	public static ModuleTitle[] L3_C3 = L1_C5;
	public static ModuleTitle[] L3_C5 = { 
		M_MENU_GROUP  
	};

	public static boolean in(ModuleTitle[] moduleTitles, String title) {
		if (StringUtil.isEmpty(title)) return false;
		for (ModuleTitle mt : moduleTitles) {
			if (mt.title.equals(title)) return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return this.title;
	}
	
	public static void main(String[] args) {
		System.out.println(ModuleTitle.in(L1_C1, "m_comment"));
	}
	
}