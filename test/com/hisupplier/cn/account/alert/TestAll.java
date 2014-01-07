package com.hisupplier.cn.account.alert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAll extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for alert");
		//����alert�����в���
		suite.addTestSuite(AlertTradeActionTest.class);
		suite.addTestSuite(AlertTradeFormActionTest.class);
		suite.addTestSuite(AlertTradeStepActionTest.class);		
		return suite;
	}
}
