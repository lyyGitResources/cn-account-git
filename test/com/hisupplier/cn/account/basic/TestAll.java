

package com.hisupplier.cn.account.basic;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestAll extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for basic");

		//����basic�����в���
		suite.addTestSuite(HomeActionTest.class);
		suite.addTestSuite(ServirceFormActionTest.class);
		suite.addTestSuite(UserSuggestFomActionTest.class);
		return suite;
	}

}
