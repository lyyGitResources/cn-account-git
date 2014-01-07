/* 
 * Created by baozhimin at Nov 30, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author baozhimin
 */
public class TestAll extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for product");

		suite.addTestSuite(NewProductActionTest.class);
		suite.addTestSuite(NewProductFormActionTest.class);
		suite.addTestSuite(ProductActionTest.class);
		suite.addTestSuite(ProductFormActionTest.class);
		suite.addTestSuite(TradeActionTest.class);
		suite.addTestSuite(TradeFormActionTest.class);
		return suite;
	}
}
