package com.hisupplier.cn.account.entity;

import java.util.HashMap;
import java.util.Map;

public class MessageTemplate extends com.hisupplier.commons.entity.MessageTemplate {
	private static final long serialVersionUID = 8050098307269161044L;

	public static Map<Integer, String> tempalteType = new HashMap<Integer, String>();

	static {
		tempalteType.put(1, "����ף��");
		tempalteType.put(2, "����ף��");
		tempalteType.put(3, "����ף��");
		tempalteType.put(4, "�����ʺ�");
		tempalteType.put(10, "�ҵĶ���");
	}

	public String getFullType() {
		return tempalteType.get(this.getTypes()) == null ? "" : tempalteType.get(this.getTypes());
	}
}
