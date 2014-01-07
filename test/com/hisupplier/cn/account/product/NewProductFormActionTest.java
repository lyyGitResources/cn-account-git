/* 
 * Created by baozhimin at Nov 19, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import org.apache.commons.beanutils.PropertyUtils;

import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.product.NewProductAction;
import com.hisupplier.cn.account.product.NewProductFormAction;
import com.hisupplier.cn.account.product.QueryParams;
import com.hisupplier.cn.account.test.ActionTestSupport;

/**
 * @author baozhimin
 */
public class NewProductFormActionTest extends ActionTestSupport {

	private String namespace = "/newproduct";
	private NewProductFormAction action = null;

	public void test_new_product_add_submit() throws Exception {
		String method = "new_product_add_submit";
		Company company = this.getCompanyEdit();
		action = createAction(NewProductFormAction.class, namespace, method);
		this.setValidateToken();
		Product product = action.getModel();
		product.setComId(company.getComId());
		product.setImgPath("aa.jpg");
		product.setProName("�²�Ʒ����");
		product.setModel("�²�Ʒmodel");
		product.setProvince("101101");
		product.setCity("101101101");
		product.setTown("101101101101");
		product.setBrief("�²�Ʒ��Ҫ����");
		product.setDescription("�²�Ʒ��ϸ����");
		
		this.execute(method, "success");
	}
	public void test_new_product_edit_submit() throws Exception {
		String method = "new_product_edit_submit";
		Product p = this.getNewProduct();
		action = createAction(NewProductFormAction.class, namespace, method);
		this.setValidateToken();
		Product product = action.getModel();
		PropertyUtils.copyProperties(product, p);
		product.setImgPath("�޸ĺ��aa.jpg");
		product.setProName("�޸ĺ���²�Ʒ����");
		product.setModel("�޸ĺ���²�Ʒmodel");
		product.setProvince("101101");
		product.setCity("101101101");
		product.setTown("101101101101");
		product.setBrief("�޸ĺ���²�Ʒ��Ҫ����");
		product.setDescription("�޸ĺ���²�Ʒ��ϸ����");
		
		this.execute(method, "success");
	}
	
	public void test_new_product_delete() throws Exception {
		String method = "new_product_delete";
		int[]proId = this.getNewProductId();
		if (proId == null) {
			assertTrue(false);
			return;
		}
		NewProductAction action = createAction(NewProductAction.class, namespace, method);
		QueryParams params = action.getModel();
		params.setProId(proId);
		this.execute(method, "success");
	}
	public static void main(String[] args) {
		for(int i = 0; i<32;i++){
			try {
				new NewProductFormActionTest().test_new_product_add_submit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
