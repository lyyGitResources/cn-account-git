/* 
 * Created by sunhailin at Nov 11, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author sunhailin
 *
 */
public class TestAll extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for misc");

		suite.addTestSuite(VoteActionTest.class);
		suite.addTestSuite(CommentActionTest.class);
		suite.addTestSuite(VideoGroupActionTest.class);
		suite.addTestSuite(VideoActionTest.class);
		suite.addTestSuite(VideoFormActionTest.class);
		suite.addTestSuite(ImageActionTest.class);
		return suite;
	}
}
