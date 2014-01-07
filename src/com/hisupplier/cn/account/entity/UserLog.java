package com.hisupplier.cn.account.entity;

import com.hisupplier.commons.util.StringUtil;

public class UserLog  extends com.hisupplier.commons.entity.cn.UserLog{


	private static final long serialVersionUID = -3378701692472016100L;
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getContentSub() {
		return StringUtil.substring(this.getContent(), 12, "...", false);
	};
	
	/**
	 * ������Ϣ��������
	 * @return
	 */
	public String getLogTypeName(){
		String name = "";
		if(StringUtil.equals(this.getLogType(), HOME)){
			name = "��̨��ҳ";
		}else if(StringUtil.equals(this.getLogType(), MEMBER)){
			name = "��Ա��Ϣ";
		}else if(StringUtil.equals(this.getLogType(), GROUP)){
			name = "����";
		}else if(StringUtil.equals(this.getLogType(), PRODUCT)){
			name = "��Ʒ";
		}else if(StringUtil.equals(this.getLogType(), TRADE)){
			name = "����";
		}else if(StringUtil.equals(this.getLogType(), MENU)){
			name = "�Զ���˵�";
		}else if(StringUtil.equals(this.getLogType(), AD)){
			name = "��ֵ����";
		}else if(StringUtil.equals(this.getLogType(), WEBSITE)){
			name = "��վ���";
		}else if(StringUtil.equals(this.getLogType(), INQUIRY)){
			name = "ѯ��";
		}else if(StringUtil.equals(this.getLogType(), ALERT)){
			name = "����";
		}else if(StringUtil.equals(this.getLogType(), PATENT)){
			name = "Υ���";
		}else if(StringUtil.equals(this.getLogType(), VIDEO)){
			name = "��Ƶ";
		}
		return name;
	}
	
	public String getOperateName(){
		String name = "";
		if(this.getOperate() == ADD){
			name ="���";
		}else if(this.getOperate() == MODIFY){
			name = "�޸�";
		}else if(this.getOperate() == RECYCLE){
			name = "����";
		}else if(this.getOperate() == REUSE){
			name = "��ԭ";
		}else if(this.getOperate() == DELETE){
			name = "ɾ��";
		}
		return name;
	}

}
