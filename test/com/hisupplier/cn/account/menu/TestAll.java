package com.hisupplier.cn.account.menu;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAll extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for menu");
		suite.addTestSuite(MenuActionTest.class);
		suite.addTestSuite(MenuFormActionTest.class);
		return suite;
	}
}
