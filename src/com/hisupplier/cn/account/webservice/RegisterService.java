/* 
 * Created by linliuwei at 2009-11-12 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.webservice;

import javax.jws.WebService;

import com.hisupplier.cn.account.entity.Register;

/**
 * ע���Ա�ӿ�
 * @author linliuwei
 */
@WebService
public interface RegisterService {

	/**
	 * ����ע��Զ�̵��ýӿ�
	 * @param register
	 * <pre>
	 *   //��˾��Ϣ
	 *   regMode        ע�᷽ʽ��Ĭ��ֵ��վ����ע��
	 *   comName        ��˾��
	 *   memberId       (��ѡ)��Ա�ʺţ�Ĭ��ֵ17λ��������(��ǰʱ�侫ȷ������)
	 *   catIds         (��ѡ)��ҵĿ¼��Ĭ��ֵ��(��ײ�Ŀ¼ID������ö��ŷָ�)
	 *   keywords       (��ѡ)��Ʒ�ؼ��ʣ�Ĭ��ֵ��
	 *   description    (��ѡ)��ϸ������Ĭ��ֵ��
	 *   domId          (��ѡ)��ҵ����Ĭ��ֵ��
	 *   economyArea    (��ѡ)��������Ĭ��ֵ��������
	 *   businessTypes  (��ѡ)��˾���ͣ�Ĭ��ֵ��
	 *   mainExports    (��ѡ)��Ҫ���۵�����Ĭ��ֵ��
	 *   websites       (��ѡ)��˾��ַ��Ĭ��ֵ��
	 *   
	 *   //����Ա�ʺ���Ϣ
	 *   email          ����
	 *   passwd         ����
	 *   province       ʡ�ݵ����ִ���
	 *   city           ���е����ִ���
	 *   town           ���������ִ��ţ����û�о���12��0����
	 *   contact        ��ϵ������
	 *   sex            �Ա�
	 *   tel            �绰
	 *   fax            (��ѡ)���棬Ĭ��ֵ��
	 *   street         (��ѡ)�ֵ���ַ��Ĭ��ֵ��
	 * </pre>
	 * @return 
	 * <pre>
	 *   memberId.used     ��Ա�ʺ��Ѵ���
	 *   email.used        �����Ѵ���
	 *   comName.used      ��˾���Ѵ���
	 *   addSuccess        ע��ɹ�
	 *   operateFail       ע��ʧ��
	 * </pre>
	 */
	String join(Register register);
}
