/* 
 * Created by sunhailin at Nov 11, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author sunhailin
 *
 */
public class TestAll extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for util");

		suite.addTestSuite(MailFactoryTest.class);
		return suite;
	}
}
