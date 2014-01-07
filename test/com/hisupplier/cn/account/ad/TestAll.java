package com.hisupplier.cn.account.ad;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAll extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for ad");
		
		//运行增值服务下的所有测试
		suite.addTestSuite(AdActionTest.class);
		suite.addTestSuite(UpgradeActionTest.class);
		suite.addTestSuite(AdOrderActionTest.class);
		suite.addTestSuite(TopActionTest.class);
		suite.addTestSuite(TopOrderActionTest.class);

		return suite;
	}
}
