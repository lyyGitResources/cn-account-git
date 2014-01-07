/* 
 * Created by taofeng at 2010-7-14 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.webservice;

import java.util.Map;

import javax.jws.WebService;

import com.hisupplier.commons.entity.cn.Company;
import com.hisupplier.commons.entity.cn.User;

/**
 * @author taofeng
 */
@WebService
public interface MemberService {

	/**
	 * ����˾ID��ȡ��˾��ϸ��Ϣ
	 * @param comId
	 * @return Company
	 */
	public Company getCompany(int comId);

	/**
	 * <pre>
	 * ����˾ID���û�ID��ȡ�û���Ϣ
	 * ps:���û�ID>0�ҹ�˾ID=0ʱ���û�ID��ȡ�û���Ϣ
	 *    ����˾ID>0���û�ID=0ʱ����˾ID��ȡ����Ա�û���Ϣ
	 * </pre>
	 * @param userId
	 * @param comId
	 * @return User
	 */
	public User getUser(int userId, int comId);

	/**
	 * <pre>
	 * ���¹�˾��չ��ָ���ֶ�
	 * ps:��ԭ��ֵ��+value
	 * </pre>
	 * @param comId
	 * @param column key=�ֶ�����value=����ֵ
	 * @return String suc=�ɹ� err=ʧ��
	 */
	public String updateComExtra(int comId, Map<String, Integer> column);

}
