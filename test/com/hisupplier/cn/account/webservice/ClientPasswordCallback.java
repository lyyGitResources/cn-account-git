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
public class ClientPasswordCallback implements CallbackHandler {


    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
        //���ô�����Ϣ�������룬�û�����client-beans.xml������
        pc.setPassword("hisupplier_showroom");
    }

}
