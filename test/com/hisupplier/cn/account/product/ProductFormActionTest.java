/* 
 * Created by baozhimin at Nov 26, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import org.apache.commons.beanutils.PropertyUtils;

import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.product.ProductFormAction;
import com.hisupplier.cn.account.test.ActionTestSupport;

/**
 * @author baozhimin
 */
public class ProductFormActionTest extends ActionTestSupport {

	private String namespace = "/product";
	private Product product = null;
	private ProductFormAction action = null;
	
	public void test_product_add_submit() throws Exception {
		String method = "product_add_submit";
		
		Group group = this.getProductGroup(true, false);
		if (group == null) {
			assertTrue(false);
			return;
		}
		action = createAction(ProductFormAction.class, namespace, method);
		this.setValidateToken();
		product = action.getModel();
		product.setComId(442);
		product.setUserId(758);
		product.setCatId(41);
		product.setProName("test");
		product.setBrief("testtest");
		product.setGroupId(group.getGroupId());
		product.setGroupIdBak(0);
		product.setImgPath("imgPath:/upload/200911/30/131307140791(s).jpg;imgType:3;imgName:4115105710_a6b7ebc9aa.jpg");
		product.setVideoId(0);
		product.setModel("test");
		product.setProvince("");
		product.setCity("");
		product.setTown("");
		product.setPlace("test");
		product.setKeyword1("11");
		product.setKeyword2("12");
		product.setKeyword3("13");
		product.setFeatureGroupId(0);
		product.setFeatureOrder(0);
		product.setViewCount(0);
		product.setCommentCount(0);
		product.setPrice1(10);
		product.setPrice2(30);
		product.setMinOrderNum(1);
		product.setMinOrderUnitOther("元");
		product.setPaymentType("test");
		product.setProductivity("test");
		product.setPacking("test");
		product.setTransportation("test");
		product.setDeliveryDate("test");
		product.setTagName1(new String[]{"4861"});
		product.setTagValue1(new String[]{"0"});
		product.setTagName2(new String[]{"287"});
		product.setTagValue2(new String[]{"ad"});
		product.setTagName3(new String[]{"品牌"});
		product.setTagValue3(new String[]{"贵友"});
		product.setProAddImg(new String[]{"imgPath:/upload/200911/30/131307140791(s).jpg;imgType:3;imgName:4115105710_a6b7ebc9aa.jpg"});
		product.setFilePath("");
		product.setDescription("testtesttesttesttest");
		Image image = new Image();
		product.setImage(image);
		
		this.execute(method, "success");
	}
	
	public void test_product_edit_submit() throws Exception {
		String method = "product_edit_submit";
		
		Product p = this.getProduct(false);
		if (p == null) {
			assertTrue(false);
			return;
		}
		Group group = this.getProductGroup(true, false);
		if (group == null) {
			assertTrue(false);
			return;
		}
		action = createAction(ProductFormAction.class, namespace, method);
		this.setValidateToken();
		product = action.getModel();
		PropertyUtils.copyProperties(product, p);

		product.setCatId(41);
		product.setProName("test");
		product.setBrief("testtest");
		product.setGroupId(group.getGroupId());
		product.setImgPath("imgPath:/upload/200911/30/131307140791(s).jpg;imgType:3;imgName:4115105710_a6b7ebc9aa.jpg");
		product.setVideoId(0);
		product.setModel("test");
		product.setProvince("");
		product.setCity("");
		product.setTown("");
		product.setPlace("test");
		product.setKeyword1("11");
		product.setKeyword2("12");
		product.setKeyword3("13");
		product.setFeatureGroupId(0);
		product.setFeatureOrder(0);
		product.setViewCount(0);
		product.setCommentCount(0);
		product.setPrice1(10);
		product.setPrice2(30);
		product.setMinOrderNum(1);
		product.setMinOrderUnitOther("元");
		product.setPaymentType("test");
		product.setProductivity("test");
		product.setPacking("test");
		product.setTransportation("test");
		product.setDeliveryDate("test");
		product.setTagName1(new String[]{"4861"});
		product.setTagValue1(new String[]{"0"});
		product.setTagName2(new String[]{"287"});
		product.setTagValue2(new String[]{"ad"});
		product.setTagName3(new String[]{"品牌"});
		product.setTagValue3(new String[]{"贵友"});
		product.setProAddImg(new String[]{"imgPath:/upload/200911/30/131307140791(s).jpg;imgType:3;imgName:4115105710_a6b7ebc9aa.jpg"});
		product.setFilePath("");
		product.setDescription("testtesttesttesttest");
		product.setOldCatId(p.getCatId());
		product.setOldGroupId(p.getGroupId());
		product.setOldKeyword(p.getKeywords());
		
		Image image = new Image();
		product.setImage(image);
		
		this.execute(method, "success");
	}
}
