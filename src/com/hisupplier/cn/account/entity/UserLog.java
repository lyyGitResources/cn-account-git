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
	 * 返回信息类型名称
	 * @return
	 */
	public String getLogTypeName(){
		String name = "";
		if(StringUtil.equals(this.getLogType(), HOME)){
			name = "后台首页";
		}else if(StringUtil.equals(this.getLogType(), MEMBER)){
			name = "会员信息";
		}else if(StringUtil.equals(this.getLogType(), GROUP)){
			name = "分组";
		}else if(StringUtil.equals(this.getLogType(), PRODUCT)){
			name = "产品";
		}else if(StringUtil.equals(this.getLogType(), TRADE)){
			name = "商情";
		}else if(StringUtil.equals(this.getLogType(), MENU)){
			name = "自定义菜单";
		}else if(StringUtil.equals(this.getLogType(), AD)){
			name = "增值服务";
		}else if(StringUtil.equals(this.getLogType(), WEBSITE)){
			name = "网站设计";
		}else if(StringUtil.equals(this.getLogType(), INQUIRY)){
			name = "询盘";
		}else if(StringUtil.equals(this.getLogType(), ALERT)){
			name = "订阅";
		}else if(StringUtil.equals(this.getLogType(), PATENT)){
			name = "违规词";
		}else if(StringUtil.equals(this.getLogType(), VIDEO)){
			name = "视频";
		}
		return name;
	}
	
	public String getOperateName(){
		String name = "";
		if(this.getOperate() == ADD){
			name ="添加";
		}else if(this.getOperate() == MODIFY){
			name = "修改";
		}else if(this.getOperate() == RECYCLE){
			name = "回收";
		}else if(this.getOperate() == REUSE){
			name = "还原";
		}else if(this.getOperate() == DELETE){
			name = "删除";
		}
		return name;
	}

}
