/* 
 * Created by baozhimin at Nov 19, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.product.ProductAction;
import com.hisupplier.cn.account.product.QueryParams;
import com.hisupplier.cn.account.test.ActionTestSupport;

/**
 * @author baozhimin
 */
public class ProductActionTest extends ActionTestSupport {

	private String namespace = "/product";
	private QueryParams params = null;
	private ProductAction action = null;

	public void test_product_list() throws Exception {
		String method = "product_list";

		//所有记录查询［默认］
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		this.execute(method, "success");

		// 按关键词查询
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setQueryText("笔");
		this.execute(method, "success");

		// 按子帐号查询
		User user = this.getUserEdit();
		if (user == null) {
			assertTrue(false);
			return;
		}
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setUserId(user.getUserId());
		this.execute(method, "success");

		// 按优化产品查询
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setOptimize(1);
		this.execute(method, "success");

		// 按状态查询
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setState(20);
		this.execute(method, "success");

		// 按组内产品查询
		Group group = this.getProductGroup(true, false);
		if (group == null) {
			assertTrue(false);
			return;
		}
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setGroupId(group.getGroupId());
		this.execute(method, "success");

		// 按展台产品查询
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setFeature(1);
		this.execute(method, "success");

		// 按产品ID查询
		int[] proId = this.getProductId(false);
		if (proId == null) {
			assertTrue(false);
			return;
		}
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setProId(proId);
		this.execute(method, "success");
	}

	public void test_product_repost() throws Exception {
		String method = "product_repost";
		int[] proId = this.getProductId(false);
		if (proId == null) {
			assertTrue(false);
			return;
		}

		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setProId(proId);
		this.execute(method, "success");
	}

	public void test_product_change_user() throws Exception {
		String method = "product_change_user";
		int[] proId = this.getProductId(false);
		if (proId == null) {
			assertTrue(false);
			return;
		}

		User user = this.getUserEdit();
		if (user == null) {
			assertTrue(false);
			return;
		}

		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setProId(proId);
		params.setUserId(user.getUserId());
		this.execute(method, "success");
	}

	public void test_product_delete() throws Exception {
		String method = "product_delete";

		int[] proId = this.getProductId(false);
		if (proId == null) {
			assertTrue(false);
			return;
		}

		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setProId(proId);
		this.execute(method, "success");
	}

	public void test_similar_category() throws Exception {
		String method = "similar_category";

		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		this.execute(method, "success");
	}

	public void test_product_tags() throws Exception {
		String method = "product_tags";

		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		this.execute(method, "success");
	}

	public void test_check_product_keyword() throws Exception {
		String method = "check_product_keyword";

		Product product = this.getProduct(false);
		if (product == null) {
			assertTrue(false);
			return;
		}

		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setProId(new int[] { product.getProId() });
		params.setKeyword1("test1");
		this.execute(method, "success");
	}

	public void test_product_add() throws Exception {
		String method = "product_add";

		// 默认条件
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		this.execute(method, "success");

		// 添加同类产品
		Product product = this.getProduct(false);
		if (product == null) {
			assertTrue(false);
			return;
		}
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setProId(new int[] { product.getProId() });
		this.execute(method, "success");

		// 加密产品转为普通产品
		product = this.getNewProduct();
		if (product == null) {
			assertTrue(false);
			return;
		}
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setNewProId(product.getProId());
		this.execute(method, "success");
	}

	public void test_product_edit() throws Exception {
		String method = "product_edit";

		Product product = this.getProduct(false);
		if (product == null) {
			assertTrue(false);
			return;
		}
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setProId(new int[] { product.getProId() });
		this.execute(method, "success");

	}

	public void test_feature_product_list() throws Exception {
		String method = "feature_product_list";
		action = createAction(ProductAction.class, namespace, method);
		this.execute(method, "success");
	}

	public void test_no_feature_product_list() throws Exception {
		String method = "no_feature_product_list";
		action = createAction(ProductAction.class, namespace, method);
		this.execute(method, "success");
	}

	public void test_feature_product_add() throws Exception {
		String method = "feature_product_add";
		Product product = this.getNoFeatureProduct();

		if (product == null) {
			assertTrue(false);
			return;
		}
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setProId(new int[] { product.getProId() });
		this.execute(method, "success");
	}

	public void test_feature_product_remove() throws Exception {
		String method = "feature_product_remove";
		Product product = this.getFeatureProduct();
		if (product == null) {
			assertTrue(false);
			return;
		}
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setProId(new int[] { product.getProId() });
		this.execute(method, "success");
	}

	public void test_feature_product_set_to_product() throws Exception {
		String method = "feature_product_set";
		Product product = this.getFeatureProduct();
		if (product != null) {
			action = createAction(ProductAction.class, namespace, method);
			params = action.getModel();
			params.setGroupId(0);
			params.setProId(new int[] { product.getProId() });
			this.execute(method, "success");
		}
	}

	public void test_feature_product_set_to_group() throws Exception {
		String method = "feature_product_set";
		Product product = this.getFeatureProduct();
		com.hisupplier.commons.entity.cn.Group group = this.getGroup();
		if (product != null && group != null) {
			action = createAction(ProductAction.class, namespace, method);
			params = action.getModel();
			params.setGroupId(group.getGroupId());
			params.setProId(new int[] { product.getProId() });
			this.execute(method, "success");
		}
	}
	
	public void test_product_order() throws Exception {
		String method = "product_order";
		
		// 所有产品排序
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		this.execute(method, "success");
		
		// 组内产品排序
		Group group = this.getProductGroup(false, true);
		if (group == null) {
			assertTrue(false);
			return;
		}
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setGroupId(group.getGroupId());
		this.execute(method, "success");
	}
	
	public void test_product_single_order_submit() throws Exception {
		String method = "product_single_order_submit";
		
		// 所有产品排序
		Product product = this.getProduct(true);
		if (product == null) {
			assertTrue(false);
			return;
		}
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setProId(new int[]{product.getProId()});
		params.setListOrder(1);
		this.execute(method, "success");
		
		// 组内产品排序
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setProId(new int[]{product.getProId()});
		params.setGroupId(product.getGroupId());
		this.execute(method, "success");
	}
	
	public void test_product_order_submit1() throws Exception {
		String method = "product_order_submit";
		
		// 所有产品排序
		int[] proId = this.getProductId(true);
		if (proId == null) {
			assertTrue(false);
			return;
		}
		
		// 组内产品排序
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setProId(proId);
		params.setGroupId(1); // >0表示组内排序
		this.execute(method, "success");
	}
	
	public void test_product_order_submit2() throws Exception {
		String method = "product_order_submit";
		
		// 所有产品排序
		int[] proId = this.getProductId(true);
		if (proId == null) {
			assertTrue(false);
			return;
		}
		action = createAction(ProductAction.class, namespace, method);
		params = action.getModel();
		params.setProId(proId);
		this.execute(method, "success");
	}
}
