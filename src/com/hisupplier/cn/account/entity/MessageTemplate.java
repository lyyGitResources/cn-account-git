package com.hisupplier.cn.account.entity;

import java.util.HashMap;
import java.util.Map;

public class MessageTemplate extends com.hisupplier.commons.entity.MessageTemplate {
	private static final long serialVersionUID = 8050098307269161044L;

	public static Map<Integer, String> tempalteType = new HashMap<Integer, String>();

	static {
		tempalteType.put(1, "´º½Ú×£¸£");
		tempalteType.put(2, "ÖĞÇï×£¸£");
		tempalteType.put(3, "ÉúÈÕ×£¸£");
		tempalteType.put(4, "½¡¿µÎÊºò");
		tempalteType.put(10, "ÎÒµÄ¶ÌÓï");
	}

	public String getFullType() {
		return tempalteType.get(this.getTypes()) == null ? "" : tempalteType.get(this.getTypes());
	}
}
