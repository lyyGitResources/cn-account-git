/* 
 * Created by baozhimin at Nov 19, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import com.hisupplier.cn.account.product.NewProductAction;
import com.hisupplier.cn.account.product.QueryParams;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.util.DateUtil;

/**
 * @author baozhimin
 */
public class NewProductActionTest extends ActionTestSupport {

	private String namespace = "/newproduct";
	private QueryParams params = null;
	private NewProductAction action = null;

	public void test_new_product_list() throws Exception {
		String method = "new_product_list";

		//���м�¼��ѯ��Ĭ�ϣ�
		action = createAction(NewProductAction.class, namespace, method);
		params = action.getModel();
		this.execute(method, "success");

		// ����Ʒ����ѯ
		action = createAction(NewProductAction.class, namespace, method);
		params = action.getModel();
		params.setQueryText("�������");
		this.execute(method, "success");

		// ���ͺŲ�ѯ
		action = createAction(NewProductAction.class, namespace, method);
		params = action.getModel();
		params.setQueryText("L-L06201");
		this.execute(method, "success");
	}

	public void test_new_product_add() throws Exception {
		String method = "new_product_add";
		action = createAction(NewProductAction.class, namespace, method);
		params = action.getModel();
		this.execute(method, "success");
	}

	public void test_new_product_edit() throws Exception {
		String method = "new_product_edit";
		int newProId = this.getNewProduct().getProId();
		action = createAction(NewProductAction.class, namespace, method);
		params = action.getModel();
		params.setNewProId(newProId);
		this.execute(method, "success");
	}

	public void test_new_product_set() throws Exception {
		String method = "new_product_set";
		action = createAction(NewProductAction.class, namespace, method);
		params = action.getModel();
		this.execute(method, "success");
	}
	public void test_new_product_set_submint() throws Exception {
		String method = "new_product_set_submit";
		action = createAction(NewProductAction.class, namespace, method);
		params = action.getModel();
		params.setNewProPass("123123");
		params.setNewProPassExpiry(new DateUtil().getDateTime());
		params.setNewProMenuName("newproMenuName");
		this.execute(method, "success");
	}
}
