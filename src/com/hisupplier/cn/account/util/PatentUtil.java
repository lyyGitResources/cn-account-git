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
	private static final String regEx="[\\pP+$`~^=|<>~�� |]"; 
	
	/**
	 * ע�⣺����ǹ�˾�������е�keyword�ֶ������飩 �͵��������� 
	 * @param obj
	 * @param map
	 * @param comId
	 * @return
	 */
	public static String checkKeyword(Object obj, Map<String, String> map, int comId) {
		List<String> keywords = clearRepeatList(comId); // ���˺��Υ����б�
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

								// ����ǹ�˾�������е�keyword�ֶ�������
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
									String objValue = BeanUtils.getProperty(obj, entry.getKey());// ����ȡֵ
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
			result.setTitle(menu.getTitle().replaceAll(regEx, "").replaceAll(" ", "").replaceAll("��", ""));
		}
		return result;
	}

	/**
	 * ��������������ַ�  product
	 * @param str�ؼ���
	 * @return
	 * @throws PatternSyntaxException
	 */
	private static Product ProFilter(Product pro) throws PatternSyntaxException {
		Product product = new Product();
		if (pro != null) {
			StringUtil.trimToEmpty(pro, "brief,description");
			if (StringUtil.isNotEmpty(pro.getKeyword1())) {
				product.setKeyword1(pro.getKeyword1().replaceAll(regEx, "").replaceAll(" ", "").replaceAll("��", ""));
			}
			if (StringUtil.isNotEmpty(pro.getProName())) {
				product.setProName(pro.getProName().replaceAll(regEx, "").replaceAll(" ", "").replaceAll("��", ""));
			}
			product.setBrief(pro.getBrief().replaceAll(" ", "").replaceAll("��", ""));
			product.setDescription(pro.getDescription().replaceAll(" ", "").replaceAll("��", "").replaceAll("&nbsp;", ""));
		}
		return product;
	}
	
	/**
	 * @param ��������������ַ� buyLead
	 * @return
	 * @throws PatternSyntaxException
	 */
	private static BuyLead BlFilter(BuyLead bl) throws PatternSyntaxException {
		BuyLead buyLead = new BuyLead();
		if (bl != null) {
			if (StringUtil.isNotEmpty(bl.getKeywords())) {
				buyLead.setKeywords(bl.getKeywords().replaceAll(regEx, "").replaceAll(" ", "").replaceAll("��", ""));
			}
			if (StringUtil.isNotEmpty(bl.getProName())) {
				buyLead.setProName(bl.getProName().replaceAll(regEx, "").replaceAll(" ", "").replaceAll("��", ""));
			}
			buyLead.setBrief(bl.getBrief().replaceAll(" ", "").replaceAll("��", ""));
		}
		return buyLead;
	}

	/**
	 * ��������������ַ�  company
	 * @param str�ؼ���
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
					keyword[i] = com.getKeyword()[i].replaceAll(regEx, "").replaceAll(" ", "").replaceAll("��", "");
				}
				company.setKeyword(keyword);
			}
			company.setComName(com.getComName().replaceAll(" ", "").replaceAll("��", ""));
			company.setDescription(StringUtil.toEmpty(com.getDescription()));
		}
		return company;
	}
	
	/** 
	 * Υ��ʽ������ʱ�õ��ã���֤Ҫ��ӵĽ��Υ����Ƿ���Υ����б��д���
	 * @param addKeyword
	 * @return boolean
	 */
	public static boolean checkAddKeywords(String addKeyword) {
		List<String> keywords = getPatentCached(); //Υ����б�
		return keywords.contains(addKeyword);
	}
	
	/**
	 * ͨ��Υ��ʽ������ͨ���ļ���  ���˵� ��Ӧ��˾�е�Υ��ʼ���
	 * @param comId
	 * @return
	 */
	private static List<String> clearRepeatList (int comId) {
		List<String> keywords = getPatentCached(); //Υ����б�
		if(comId == 0) {
			return keywords;
		}else {
			List<String> patentDeblocked = PatentDeblockedUtil.getPatentDeblockedList(comId); // ���ͨ����Υ��ʽ������
			keywords.removeAll(patentDeblocked);
			return keywords;
		}
	}
}
