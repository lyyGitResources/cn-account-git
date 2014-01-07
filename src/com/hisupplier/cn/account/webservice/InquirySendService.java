/* 
 * Created by linliuwei at 2009-11-12 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.webservice;

import javax.jws.WebService;

import com.hisupplier.cn.account.entity.Inquiry;

/**
 * ѯ�̷��ͽӿ�
 * @author linliuwei
 */
@WebService
public interface InquirySendService {

	/**
	 * <p>ѯ�̷���Զ�̵��ýӿ�
	 * <p>�˽ӿ����������� hisupplier-cas.jar, hisupplier-commons.jar
	 * @param inquiry
	 * <pre>
	 *   //��ѡ�ֶ�
	 *   subject             ����    	
	 *   content  			 ����
	 *   fromIP              ѯ�̷�����IP
	 *   fromSite            ѯ������վ��
	 *   basketItemList      
	 *   loginUser           �Ƿ��¼��nullΪδ��¼��δ��¼ʱ�����û����͡��ǻ�Ա�����ֶζ�����Ҫ
	 *   newUser             �Ƿ����û�
	 * </pre>
	 * @return 
	 * <pre>
	 *   ipLimit       IP����(ÿIPÿ24Сʱ��ֻ��Ⱥ�����15���ʼ�������15���ÿ��ֻ�ܷ���1��)
	 *   passwdError   �ǻ�Ա���ʺŻ��������
	 *   email.used    ���û��������Ѵ���
	 *   comName.used  ���û�����˾���Ѵ���
	 *   addSuccess    ���ͳɹ�
	 *   operateFail   ����ʧ��
	 *   inquiry.repeat 15�����ڲ��ܷ�����ͬ���ݵ�ѯ��
	 * </pre>
	 */
	String send(Inquiry inquiry);
}
