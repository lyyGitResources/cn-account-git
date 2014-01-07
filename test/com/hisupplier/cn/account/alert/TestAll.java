package com.hisupplier.cn.account.alert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAll extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for alert");
		//运行alert下所有测试
		suite.addTestSuite(AlertTradeActionTest.class);
		suite.addTestSuite(AlertTradeFormActionTest.class);
		suite.addTestSuite(AlertTradeStepActionTest.class);		
		return suite;
	}
}
