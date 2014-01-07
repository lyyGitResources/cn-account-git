package com.hisupplier.cn.account.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hisupplier.commons.Config;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;
import com.hisupplier.commons.util.WebUtil;
import com.hisupplier.commons.util.cn.LocaleUtil;

public class User extends com.hisupplier.commons.entity.cn.User {

	private static final long serialVersionUID = 8742829689082612087L;
	private String tel1;
	private String tel2;
	private String fax1;
	private String fax2;
	private String oldPasswd;
	private String confirmPasswd;
	private String memberId;//忘记密码取回，推荐询盘用

	private String comName; //用于TQ注册时使用
	private String websites;//用于TQ注册时使用
	

	// 目前用于发送推荐询盘
	private int memberType;
	private String catIds;

	//用于从主帐号获取
	private String adminStreet;
	private String adminTel;
	private String adminFax;
	private String adminZip;

	private ChatItem chatItem;

	private String googleLocal;
	
	private int crmState;//crm绑定状态
	
	private int state;
	
	// Talks
	private List<Talk> talks = new ArrayList<Talk>();
	private boolean qq_type;
	private String[] qq_name;
	private String[] qq_id;
	private String bigqq_name;
	private String bigqq_id;
	

	public String getProvinceShow() {
		return LocaleUtil.getProvince(this.getProvince());
	}
	public String getCityShow() {
		return LocaleUtil.getCity(this.getCity());
	}
	public String getTownShow() {
		return LocaleUtil.getCounty(this.getTown());
	}

	public String getHeadImgSrc() {
		Map<String, String> map = UploadUtil.getImgParam(this.getHeadImgPath());

		if (Boolean.parseBoolean(map.get("isUpload"))) {
			return Config.getString("img.link") + map.get("imgPath");
		} else {
			return WebUtil.get75ImgPath(this.getHeadImgPath());
		}
	}

	public String getSexString() {
		if (this.getSex() == 1) {
			return "男";
		}
		return "女";
	}

	public String getShowString() {
		if (this.isShow()) {
			return "是";
		}
		return "否";
	}

	public String getCountryCode() {
		if (Register.isRigthLocal(this.getTown())) {
			return this.getTown();
		} else if (Register.isRigthLocal(this.getCity())) {
			return this.getCity();
		} else if (Register.isRigthLocal(this.getProvince())) {
			return this.getProvince();
		} else {
			return "";
		}
	}

//	public static boolean isRigthLocal(String local) {
//		if(StringUtil.isNotBlank(local) && !"0".equals(local) && !"000000".equals(local) && !"000000000".equals(local) && !"000000000000".equals(local)){
//			return true;
//		}else{
//			return false;
//		}
//	}
	
	/**
	 * 返回公司类型复选项目
	 * @return
	 */
	public Item[] getPrivilegeItems() {

		Item[] items = new Item[6];
		items[0] = new Item("修改公司信息", "/member/company_edit_submit");
		items[1] = new Item("分组", "/group/");
		items[2] = new Item("自定义菜单", "/menu/");
		items[3] = new Item("增值服务", "/ad/");
		items[4] = new Item("订阅", "/alert/");
		items[5] = new Item("网站设计", "/website/");

		List<String> privilegeList = this.getPrivilegeList();
		for (String value : privilegeList) {
			for (Item item : items) {
				if (value.equals(item.getValue())) {
					item.setChecked(true);
				}
			}
		}
		return items;
	}

	/**
	 * 已拥有的权限
	 * @return
	 */
	public List<String> getPrivilegeList() {
		List<String> list = new ArrayList<String>();
		for (String pri : StringUtil.toArrayList(this.getPrivilege(), ",")) {
			list.add(pri);
		}
		return list;
	}

	public String getLinkIdText() {
		if (StringUtil.isNotBlank(this.getLinkId())) {
			return "已绑定";
		} else {
			return "未绑定";
		}
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getTel1() {
		return tel1;
	}

	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getFax1() {
		return fax1;
	}

	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}

	public String getFax2() {
		return fax2;
	}

	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}

	public String getOldPasswd() {
		return oldPasswd;
	}

	public void setOldPasswd(String oldPasswd) {
		this.oldPasswd = oldPasswd;
	}

	public String getConfirmPasswd() {
		return confirmPasswd;
	}

	public void setConfirmPasswd(String confirmPasswd) {
		this.confirmPasswd = confirmPasswd;
	}

	public int getMemberType() {
		return memberType;
	}

	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}

	public String getCatIds() {
		return catIds;
	}

	public void setCatIds(String catIds) {
		this.catIds = catIds;
	}

	public String getAdminStreet() {
		return this.adminStreet;
	}

	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public void setAdminStreet(String adminStreet) {
		this.adminStreet = adminStreet;
	}

	public String getAdminTel() {
		return this.adminTel;
	}

	public void setAdminTel(String adminTel) {
		this.adminTel = adminTel;
	}

	public String getAdminFax() {
		return this.adminFax;
	}

	public void setAdminFax(String adminFax) {
		this.adminFax = adminFax;
	}

	public String getAdminZip() {
		return this.adminZip;
	}

	public void setAdminZip(String adminZip) {
		this.adminZip = adminZip;
	}

	public String getGoogleLocal() {
		return googleLocal;
	}

	public void setGoogleLocal(String googleLocal) {
		this.googleLocal = googleLocal;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getWebsites() {
		return websites;
	}

	public void setWebsites(String websites) {
		this.websites = websites;
	}

	public ChatItem getChatItem() {
		return chatItem;
	}

	public void setChatItem(ChatItem chatItem) {
		this.chatItem = chatItem;
	}

	public int getCrmState() {
		return crmState;
	}

	public void setCrmState(int crmState) {
		this.crmState = crmState;
	}
	public String[] getQq_name() {
		return qq_name;
	}
	public void setQq_name(String[] qq_name) {
		this.qq_name = qq_name;
	}
	public String[] getQq_id() {
		return qq_id;
	}
	public void setQq_id(String[] qq_id) {
		this.qq_id = qq_id;
	}
	public boolean isQq_type() {
		return qq_type;
	}
	public void setQq_type(boolean qq_type) {
		this.qq_type = qq_type;
	}
	public String getBigqq_name() {
		return bigqq_name;
	}
	public void setBigqq_name(String bigqq_name) {
		this.bigqq_name = bigqq_name;
	}
	public String getBigqq_id() {
		return bigqq_id;
	}
	public void setBigqq_id(String bigqq_id) {
		this.bigqq_id = bigqq_id;
	}
	public List<Talk> getTalks() {
		return talks;
	}
	public void setTalks(List<Talk> talks) {
		this.talks = talks;
	}
}
