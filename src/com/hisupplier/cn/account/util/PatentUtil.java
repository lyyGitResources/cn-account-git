package com.hisupplier.cn.account.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.beanutils.BeanUtils;

import com.hisupplier.cn.account.entity.BuyLead;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Menu;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.commons.util.StringUtil;

public class PatentUtil extends com.hisupplier.commons.util.cn.PatentUtil{
	private static final String regEx="[\\pP+$`~^=|<>~￥ |]"; 
	
	/**
	 * 注意：如果是公司对象（其中的keyword字段是数组） 就单独处理下 
	 * @param obj
	 * @param map
	 * @param comId
	 * @return
	 */
	public static String checkKeyword(Object obj, Map<String, String> map, int comId) {
		List<String> keywords = clearRepeatList(comId); // 过滤后的违规词列表
		if (obj instanceof Product) {
			obj = ProFilter((Product) obj);
		} else if (obj instanceof Company) {
			obj = ComFilter((Company) obj);
		} else if (obj instanceof Menu) {
			obj = menuTileFilter((Menu) obj);
		} else if (obj instanceof BuyLead) {
			obj = BlFilter((BuyLead) obj);
		}

		if (obj != null && map.size() > 0) {

			if (keywords != null && keywords.size() > 0) {
				for (String keyword : keywords) {
					if (StringUtil.isNotBlank(keyword)) {
						keyword = keyword.replace(" ", "");

						for (Map.Entry<String, String> entry : map.entrySet()) {
							try {

								// 如果是公司对象，其中的keyword字段是数组
								if (entry.getKey().equals("keyword")) {
									String[] objValues = BeanUtils.getArrayProperty(obj, entry.getKey());
									if (objValues != null && objValues.length > 0) {
										for (String objValue : objValues) {
											if (StringUtil.isNotEmpty(objValue) && objValue.indexOf(keyword) > -1) {
												return entry.getValue() + keyword;
											}
										}
									}
								} else {
									String objValue = BeanUtils.getProperty(obj, entry.getKey());// 反射取值
									if (StringUtil.isNotEmpty(objValue) && objValue.indexOf(keyword) > -1) {
										return entry.getValue() + keyword;
									}
								}

							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							}
						}

					}

				}
			}

		}
		return null;
	}

	private static Menu menuTileFilter(Menu menu) {
		Menu result = new Menu();
		if (menu != null) {
			result.setTitle(menu.getTitle().replaceAll(regEx, "").replaceAll(" ", "").replaceAll("　", ""));
		}
		return result;
	}

	/**
	 * 清除掉所有特殊字符  product
	 * @param str关键字
	 * @return
	 * @throws PatternSyntaxException
	 */
	private static Product ProFilter(Product pro) throws PatternSyntaxException {
		Product product = new Product();
		if (pro != null) {
			StringUtil.trimToEmpty(pro, "brief,description");
			if (StringUtil.isNotEmpty(pro.getKeyword1())) {
				product.setKeyword1(pro.getKeyword1().replaceAll(regEx, "").replaceAll(" ", "").replaceAll("　", ""));
			}
			if (StringUtil.isNotEmpty(pro.getProName())) {
				product.setProName(pro.getProName().replaceAll(regEx, "").replaceAll(" ", "").replaceAll("　", ""));
			}
			product.setBrief(pro.getBrief().replaceAll(" ", "").replaceAll("　", ""));
			product.setDescription(pro.getDescription().replaceAll(" ", "").replaceAll("　", "").replaceAll("&nbsp;", ""));
		}
		return product;
	}
	
	/**
	 * @param 清除掉所有特殊字符 buyLead
	 * @return
	 * @throws PatternSyntaxException
	 */
	private static BuyLead BlFilter(BuyLead bl) throws PatternSyntaxException {
		BuyLead buyLead = new BuyLead();
		if (bl != null) {
			if (StringUtil.isNotEmpty(bl.getKeywords())) {
				buyLead.setKeywords(bl.getKeywords().replaceAll(regEx, "").replaceAll(" ", "").replaceAll("　", ""));
			}
			if (StringUtil.isNotEmpty(bl.getProName())) {
				buyLead.setProName(bl.getProName().replaceAll(regEx, "").replaceAll(" ", "").replaceAll("　", ""));
			}
			buyLead.setBrief(bl.getBrief().replaceAll(" ", "").replaceAll("　", ""));
		}
		return buyLead;
	}

	/**
	 * 清除掉所有特殊字符  company
	 * @param str关键字
	 * @return
	 * @throws PatternSyntaxException
	 */
	private static Company ComFilter(Company com) throws PatternSyntaxException {
		Company company = new Company();
		String[] keyword = null;
		if (com != null) {
			if (com.getKeyword() != null && com.getKeyword().length > 0) {
				keyword = new String[com.getKeyword().length];
				for (int i = 0; i < com.getKeyword().length; i++) {
					keyword[i] = com.getKeyword()[i].replaceAll(regEx, "").replaceAll(" ", "").replaceAll("　", "");
				}
				company.setKeyword(keyword);
			}
			company.setComName(com.getComName().replaceAll(" ", "").replaceAll("　", ""));
			company.setDescription(StringUtil.toEmpty(com.getDescription()));
		}
		return company;
	}
	
	/** 
	 * 违规词解禁申请时用到得：验证要添加的解禁违规词是否在违规词列表中存在
	 * @param addKeyword
	 * @return boolean
	 */
	public static boolean checkAddKeywords(String addKeyword) {
		List<String> keywords = getPatentCached(); //违规词列表
		return keywords.contains(addKeyword);
	}
	
	/**
	 * 通过违规词解禁申请通过的集合  过滤掉 相应公司中的违规词集合
	 * @param comId
	 * @return
	 */
	private static List<String> clearRepeatList (int comId) {
		List<String> keywords = getPatentCached(); //违规词列表
		if(comId == 0) {
			return keywords;
		}else {
			List<String> patentDeblocked = PatentDeblockedUtil.getPatentDeblockedList(comId); // 审核通过的违规词解禁数组
			keywords.removeAll(patentDeblocked);
			return keywords;
		}
	}
}
