/* 
 * Created by linliuwei at 2009-11-12 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.webservice;

import javax.jws.WebService;

import com.hisupplier.cn.account.entity.TradeAlert;

/**
 * ���Ľӿ�
 * @author linliuwei
 */
@WebService
public interface SubscibeService {

	/**
	 * <p>���鶩��Զ�̵��ýӿڣ����������
	 * <p>1) ������ע�ᣬֱ��������鶩��
	 * <p>2) δע�ᣬ��ע����������鶩��
	 * @param alert
	 * @return 
	 * <pre>
	 *   alert.limit           ������������
	 *   alert.keyword.used    �ؼ����Ѵ���
	 *   addSuccess            ���ĳɹ�
	 *   operateFail           ����ʧ��
	 * </pre>
	 */
	String subscibe(TradeAlert alert);
}
