package com.hisupplier.cn.account.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAll extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test All");

		//运行所有测试
		suite.addTest(com.hisupplier.cn.account.ad.TestAll.suite());
		suite.addTest(com.hisupplier.cn.account.alert.TestAll.suite());
		suite.addTest(com.hisupplier.cn.account.basic.TestAll.suite());
		suite.addTest(com.hisupplier.cn.account.group.TestAll.suite());
		suite.addTest(com.hisupplier.cn.account.inquiry.TestAll.suite());
		suite.addTest(com.hisupplier.cn.account.member.TestAll.suite());
		suite.addTest(com.hisupplier.cn.account.menu.TestAll.suite());

		suite.addTest(com.hisupplier.cn.account.misc.TestAll.suite());
		suite.addTest(com.hisupplier.cn.account.product.TestAll.suite());
		//		suite.addTest(com.hisupplier.cn.account.website.TestAll.suite());
		suite.addTest(com.hisupplier.cn.account.util.TestAll.suite());
		suite.addTest(com.hisupplier.cn.account.user.TestAll.suite());
		return suite;
	}
}
