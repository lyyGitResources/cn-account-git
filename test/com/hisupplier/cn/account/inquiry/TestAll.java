package com.hisupplier.cn.account.inquiry;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAll extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for inquiry");

		//����ѯ���µ����в���
		suite.addTestSuite(InquiryActionTest.class);
		suite.addTestSuite(InquiryReplyActionTest.class);
		suite.addTestSuite(InquirySendActionTest.class);

		return suite;
	}
}
