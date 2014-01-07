/* 
 * Created by taofeng at Nov 17, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.webservice;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

/**
 * @author taofeng
 *
 */
public class ServerPasswordCallback implements CallbackHandler {
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		if (pc.getIdentifier().equalsIgnoreCase("hisupplier_showroom")) {
			
			pc.setPassword("hisupplier_showroom");
		} else if(pc.getIdentifier().equalsIgnoreCase("hisupplier_b2b")){
			
			pc.setPassword("hisupplier_b2b");
		}else if(pc.getIdentifier().equalsIgnoreCase("hisupplier_admin")){
			
			pc.setPassword("hisupplier_admin");
		}
		else {
			throw new IOException("ª·‘±’ ∫≈¥ÌŒÛ");
		}
	}
}
