package com.hisupplier.cn.account.sms;

public class Message1 {

	private boolean isStorage = false;//是否录入数据库
	
	private StringBuilder number = new StringBuilder();
	private String sendTime;
	
	//以下参数仅用于移动mas短信发送
	private boolean oneToMany = false;
	
	//联通
	private int id;
	private String mobile;
	private String content;
	private String sms_number;//要发送的扩展短号（自动添加在公司短信号码后面）
	private int priority;//发送优先级，0最高，默认9
	private String plan_time;//短信定时发送时间，格式为 yyyy-mm-dd hh:mm:ss 如果不填写，表示马上发送
	// ********************************************
	//上面的字段由发送者填写
	// 下面的字段由接口回填
	// ********************************************
	private String result = "0"; // 状态，默认0，仅对联通号码有效，移动号码只有状态0和1
	// '0' — 等待提交，表示短信尚未提交到短信群发平台的服务器
	//'1' — 已提交，表示短信已经提交到短信群发服务器，但是尚未发送到网关
	// '2' — 已发送，表示短信已经发送到网关，等待用户接收
	// '3' — 已接收，表示用户的手机已经收到短信
	// 'a' — 提交错误，短信被短信群发服务器拒绝
	// 'b' — 发送错误，短信被网关拒绝
	//'c' — 接收错误，因为用户关机等原因，短信最终未被用户收到
	private String seq_id;//短信编号
	private String submit_time;//提交时间
	private String send_time;//网关发送时间
	private String recv_time;//用户接收时间
	private int send_type;//保留

	//扩展字段(必填)
	private int comId;
	private int contactId;
	private int state;
	
	
	public Message1() {
		
	}

	/**
	 * @param number
	 * @param content
	 */
	public Message1(String number, String content) {
		this.appendNumber(number);
		this.content = content;
	}

	/**
	 * @param number
	 * @param content
	 * @param sendTime
	 */
	public Message1(String number, String content, String sendTime) {
		this.appendNumber(number);
		this.content = content;
		this.sendTime = sendTime;
	}
	
	public void appendNumber(String number) {
		this.number.append(number);
	}

	public String getNumber() {
		return this.number.toString();
	}

	public void setNumber(String number) {
		this.appendNumber(number);
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public boolean isOneToMany() {
		return oneToMany;
	}
	public void setOneToMany(boolean oneToMany) {
		this.oneToMany = oneToMany;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSms_number() {
		return sms_number;
	}
	public void setSms_number(String sms_number) {
		this.sms_number = sms_number;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getPlan_time() {
		return plan_time;
	}
	public void setPlan_time(String plan_time) {
		this.plan_time = plan_time;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}
	public String getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
	}
	public String getSend_time() {
		return send_time;
	}
	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
	public String getRecv_time() {
		return recv_time;
	}
	public void setRecv_time(String recv_time) {
		this.recv_time = recv_time;
	}
	public int getSend_type() {
		return send_type;
	}
	public void setSend_type(int send_type) {
		this.send_type = send_type;
	}
	public int getComId() {
		return comId;
	}
	public void setComId(int comId) {
		this.comId = comId;
	}
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	public boolean isStorage() {
		return isStorage;
	}

	public void setStorage(boolean isStorage) {
		this.isStorage = isStorage;
	}
}
