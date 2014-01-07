package com.hisupplier.cn.account.alert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import com.hisupplier.cas.CASClient;
import com.hisupplier.cas.Ticket;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Register;
import com.hisupplier.cn.account.entity.TradeAlert;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.member.CompanyService;
import com.hisupplier.cn.account.member.UserDAO;
import com.hisupplier.cn.account.webservice.SubscibeService;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;

@WebService(endpointInterface = "com.hisupplier.cn.account.webservice.SubscibeService")
public class TradeAlertService implements SubscibeService {
	private TradeAlertDAO tradeAlertDAO;
	private UserDAO userDAO;
	private CompanyService companyService;

	/**
	 * �������鶩���б�
	 * @param params
	 * <pre>
	 *    loginUser.comId
	 *    loginUser.userId
	 * </pre>
	 * @return alertList
	 */
	public Map<String, Object> getTradeAlertList(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<TradeAlert> alertList = (List<TradeAlert>) tradeAlertDAO.findTradeAlertList(params);
		result.put("alertList", alertList);
		return result;
	}

	/**
	 * �������鶩��
	 * @param params
	 * <pre>
	 *    loginUser.comId
	 *    loginUser.userId
	 *    id
	 * </pre>
	 * @return tradeAlert
	 */
	public Map<String, Object> getTradeAlert(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		TradeAlert tradeAlert = tradeAlertDAO.findTradeAlert(params);
		if (tradeAlert == null) {
			throw new PageNotFoundException();
		}

		// �ж�ģ��
		if (StringUtil.isNotBlank(tradeAlert.getKeyword())) {
			tradeAlert.setMode("keyword");
		} else {
			tradeAlert.setMode("category");
		}
		result.put("tradeAlert", tradeAlert);
		return result;
	}

	/**
	 * �������鶩��
	 * @param alert
	 * @return
	 * <pre>
	 *   alert.limit			������������
	 *   alert.keyword.used		�ؼ����Ѵ���
	 *   addSuccess				���ĳɹ�
	 *   operateFail			����ʧ��
	 * </pre>
	 */
	public String addTradeAlert(TradeAlert alert) {
		int total = tradeAlertDAO.findTradeAlertCount(alert.getComId());
		if (total >= 10) {
			return "alert.limit";
		}
		if (StringUtil.isNotEmpty(alert.getKeyword())) {
			if (tradeAlertDAO.findKeyword(alert.getComId(), alert.getKeyword()) > 0) {
				return "alert.keyword.used";
			}
		} else {
			alert.setKeyword("");
		}
		if (StringUtil.isEmpty(alert.getCatIds())) {
			alert.setCatIds("");
			// ����ҳ������ajax��ѯ
			//			if (StringUtil.isNotEmpty(alert.getKeyword())) {
			//				List<Category> catList = searchService.findRelateCatList(alert.getKeyword(), 3);
			//				StringBuffer catIds = new StringBuffer();
			//				if (catList != null && catList.size() > 0) {
			//					for (Category category : catList) {
			//						catIds.append(category.getCatId()).append(',');
			//					}
			//				}
			//				if (catIds.length() > 0) {
			//					catIds.deleteCharAt(catIds.length() - 1);
			//				}
			//				alert.setCatIds(catIds.toString());
			//			}
		}
		// �ͻ���ӳ���ڴ������Ʒ��ͬ�Ĺ�Ӧ��Ϣ ����� 2010-1-26
		//		if (alert.isProduct()) {
		//			alert.setSell(true);
		//		}
		alert.setEnable(true);
		alert.setCreateTime(new DateUtil().getDateTime());
		if (tradeAlertDAO.addTradeAlert(alert) > 0) {
			return "addSuccess";
		} else {
			return "operateFail";
		}

	}

	/**
	 * �������鶩�ĵ�һ��
	 * @param alert
	 * @return
	 * <pre>
	 * 	 email.error			���䲻����
	 * 	 email.used				�����Ѵ��ڣ�����������
	 *   passwd.error			�����Ѵ��ڣ��������
	 *   alert.limit			�����Ѵ��ڣ�������������
	 *   alert.keyword.used		�����Ѵ��ڣ��ؼ����Ѵ��ڣ�����������
	 *   addSuccess				�����Ѵ��ڣ����ĳɹ�
	 *   operateFail			�����Ѵ��ڣ�����ʧ��
	 *   toStep2 				���䲻���ڣ�ת���ڶ�����
	 * </pre>
	 */
	public String addTradeAlertStep1(TradeAlert alert) {
		String result = "";
		if(StringUtil.isNotEmpty(alert.getPasswd()) || userDAO.findEmail(alert.getEmail()) > 0){
			if (userDAO.findEmail(alert.getEmail()) > 0) {
				if (StringUtil.isEmpty(alert.getPasswd())) {
					result = "email.used";
				} else {
					Ticket ticket = CASClient.getInstance().validatePasswd(alert.getEmail(), alert.getPasswd());
					if (ticket.getMessage().equals("notfound") || ticket.getMessage().equals("err")) {
						result = "passwd.error";
					} else {
						alert.setComId(ticket.getComId());
						alert.setUserId(ticket.getUserId());
						return this.addTradeAlert(alert);
					}
				}
			}else{
				result = "email.error";
			}
		} else {
			result = "toStep2";
		}
		return result;
	}

	/**
	 * �������鶩�ĵڶ�������ע���ٶ���
	 * @param alert
	 * @return
	 * <pre>
	 *   email.used		
	 *   comName.used 	
	 *   alert.keyword.used �ؼ����Ѵ���
	 *   addSuccess 	���ĳɹ�
	 *   operateFail	����ʧ��
	 * </pre>
	 */
	public String addTradeAlertStep2(TradeAlert alert) {
		//ע��
		Register register = new Register();
		register.setRegMode(5);
		if (StringUtil.isNotEmpty(alert.getCatIds())) {
			register.setCatIds(alert.getCatIds());
		} else {
			register.setTradeAlertKeyword(alert.getKeyword());
		}
		register.setComName(alert.getComName());
		register.setEmail(alert.getEmail());
		register.setPasswd(alert.getPasswd());
		register.setProvince(alert.getProvince());
		register.setCity(alert.getCity());
		register.setTown(alert.getTown());
		register.setContact(alert.getContact());
		register.setSex(alert.getSex());
		register.setTel(alert.getTel());
		String result = companyService.join(register);

		//ע��ɹ�����
		if (result.equals("addSuccess")) {
			alert.setComId(register.getComId());
			alert.setUserId(register.getUserId());
			alert.setEnable(true);
			alert.setCreateTime(new DateUtil().getDateTime());
			if (tradeAlertDAO.addTradeAlert(alert) > 0) {
				return "addSuccess";
			} else {
				return "operateFail";
			}
		}
		return result;
	}

	/**
	 * �������鶩��
	 * @param alert
	 * @return editSuccess or operateFail
	 */
	public String updateTradeAlert(TradeAlert alert) {
		if (StringUtil.isNotEmpty(alert.getKeyword())) {
			if (tradeAlertDAO.findKeyword(alert.getComId(), alert.getKeyword(), alert.getId()) > 0) {
				return "alert.keyword.used";
			}
		}
		if (tradeAlertDAO.updateTradeAlert(alert) > 0) {
			return "editSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * ȡ��/�ָ����鶩��
	 * @param alert
	 * <pre>
	 *    loginUser.comId
	 *    loginUser.userId
	 *    id
	 * </pre>
	 @return deleteSuccess or operateFail
	 */
	public String updateTradeAlertEnable(QueryParams params) {
		UpdateMap updateMap = new UpdateMap("TradeAlert");
		updateMap.addField("isEnable", params.isEnable() ? 1 : 0);
		updateMap.addWhere("comId", params.getLoginUser().getComId());
		updateMap.addWhere("userId", params.getLoginUser().getUserId());
		updateMap.addWhere("id", params.getId());
		if (tradeAlertDAO.update(updateMap) > 0) {
			return "editSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * ɾ�����鶩��
	 * @param params
	 * <pre>
	 *    loginUser.comId
	 *    loginUser.userId
	 *    id
	 * </pre>
	 * @return deleteSuccess or operateFail
	 */
	public String deleteTradeAlert(QueryParams params) {
		UpdateMap updateMap = new UpdateMap("TradeAlert");
		updateMap.addWhere("comId", params.getLoginUser().getComId());
		updateMap.addWhere("userId", params.getLoginUser().getUserId());
		updateMap.addWhere("id", params.getId());
		if (tradeAlertDAO.delete(updateMap) > 0) {
			return "deleteSuccess";
		} else {
			return "operateFail";
		}
	}

	public String subscibe(TradeAlert alert) {
		String result = "";
		User user = userDAO.findUserByEmail(alert.getEmail());
		//������ע��
		if (user != null) {
			alert.setComId(user.getComId());
			alert.setUserId(user.getUserId());
			result = this.addTradeAlert(alert);

			//δע��
		} else {
			Register register = new Register();
			register.setRegMode(7);
			register.setComName(alert.getComName());
			register.setEmail(alert.getEmail());
			register.setMemberId(new DateUtil().getDate2() + new DateUtil().getTime4());
			register.setPasswd("123456");
			register.setProvince(alert.getProvince());
			register.setCity(alert.getCity());
			register.setTown(alert.getTown());
			register.setContact(alert.getContact());
			register.setSex(alert.getSex());
			register.setTel(alert.getTel());
			register.setCatIds(alert.getCatIds());
			register.setJoinUserType(alert.getJoinUserType());
			result = companyService.join(register);
			if (result.equals("addSuccess")) {
				alert.setComId(register.getComId());
				alert.setUserId(register.getUserId());
				alert.setEnable(true);
				alert.setCreateTime(new DateUtil().getDateTime());
				if (tradeAlertDAO.addTradeAlert(alert) > 0) {
					return "addSuccess";
				} else {
					return "operateFail";
				}
			}
		}
		return result;
	}

	public void setTradeAlertDAO(TradeAlertDAO tradeAlertDAO) {
		this.tradeAlertDAO = tradeAlertDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

}
