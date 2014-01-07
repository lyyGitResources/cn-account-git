/* 
 * Created by baozhimin at Dec 28, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.basic;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

/**
 * @author baozhimin
 */
public class ClientPasswordCallback implements CallbackHandler {

	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
        pc.setPassword("hisupplier_b2b");
	}

}
