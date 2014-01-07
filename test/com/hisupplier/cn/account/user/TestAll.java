/* 
 * Created by baozhimin at Dec 11, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.user;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author baozhimin
 */
public class TestAll extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for user");

		suite.addTestSuite(CategorySuggestActionTest.class);
		suite.addTestSuite(FriendActionTest.class);
		suite.addTestSuite(PostBuyLeadActionTest.class);
		return suite;
	}
}
