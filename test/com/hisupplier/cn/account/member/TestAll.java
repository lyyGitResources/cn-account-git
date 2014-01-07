package com.hisupplier.cn.account.member;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestAll extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for member");

		//���й�˾�µ����в���
		suite.addTestSuite(CompanyFormActionTest.class);
		suite.addTestSuite(CompanyActionTest.class);
		suite.addTestSuite(UserFormActionTest.class);
		suite.addTestSuite(UserActionTest.class);
		suite.addTestSuite(ForgetPasswdActionTest.class);
		suite.addTestSuite(JoinActionTest.class);
		return suite;
	}
}
