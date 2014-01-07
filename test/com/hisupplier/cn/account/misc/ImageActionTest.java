/* 
 * Created by baozhimin at Oct 28, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.cn.account.misc.ImageAction;
import com.hisupplier.cn.account.misc.QueryParams;
import com.hisupplier.cn.account.test.ActionTestSupport;

/**
 * @author baozhimin
 */
public class ImageActionTest extends ActionTestSupport {
	
	private String namespace = "/image";
	private QueryParams params = null;
	private ImageAction action = null;
	
	public void test_image_list() throws Exception {
		String method = "image_list";
		
		
		
		//所有记录查询［默认］
		action = createAction(ImageAction.class, namespace, method);
		params = action.getModel();
		this.execute(method, "success");
		
		// 按图片类型查询
		action = createAction(ImageAction.class, namespace, method);
		params = action.getModel();
		params.setImgType(3);
		this.execute(method, "success");
		
		// 按关键词查询
		action = createAction(ImageAction.class, namespace, method);
		params = action.getModel();
		params.setQueryBy("imgName");
		params.setQueryText("1");
		this.execute(method, "success");
		
		// 按所有条件查询
		action = this.createAction(ImageAction.class, namespace, method);
		params = action.getModel();
		params.setImgType(3);
		params.setQueryBy("imgName");
		params.setQueryText("1");
		this.execute(method, "success");
	}
	
	public void test_image_delete() throws Exception {
		Image image = this.getImage();
		if (image == null) {
			assertTrue(false);
			return;
		}
		
		String method = "image_delete";
		action = this.createAction(ImageAction.class, namespace, method);
		params = action.getModel();
		params.setImgId(image.getImgId());
		this.execute(method, "success");
	}
	
}
