package com.hisupplier.cn.account.sms;

public class Message1 {

	private boolean isStorage = false;//�Ƿ�¼�����ݿ�
	
	private StringBuilder number = new StringBuilder();
	private String sendTime;
	
	//���²����������ƶ�mas���ŷ���
	private boolean oneToMany = false;
	
	//��ͨ
	private int id;
	private String mobile;
	private String content;
	private String sms_number;//Ҫ���͵���չ�̺ţ��Զ�����ڹ�˾���ź�����棩
	private int priority;//�������ȼ���0��ߣ�Ĭ��9
	private String plan_time;//���Ŷ�ʱ����ʱ�䣬��ʽΪ yyyy-mm-dd hh:mm:ss �������д����ʾ���Ϸ���
	// ********************************************
	//������ֶ��ɷ�������д
	// ������ֶ��ɽӿڻ���
	// ********************************************
	private String result = "0"; // ״̬��Ĭ��0��������ͨ������Ч���ƶ�����ֻ��״̬0��1
	// '0' �� �ȴ��ύ����ʾ������δ�ύ������Ⱥ��ƽ̨�ķ�����
	//'1' �� ���ύ����ʾ�����Ѿ��ύ������Ⱥ����������������δ���͵�����
	// '2' �� �ѷ��ͣ���ʾ�����Ѿ����͵����أ��ȴ��û�����
	// '3' �� �ѽ��գ���ʾ�û����ֻ��Ѿ��յ�����
	// 'a' �� �ύ���󣬶��ű�����Ⱥ���������ܾ�
	// 'b' �� ���ʹ��󣬶��ű����ؾܾ�
	//'c' �� ���մ�����Ϊ�û��ػ���ԭ�򣬶�������δ���û��յ�
	private String seq_id;//���ű��
	private String submit_time;//�ύʱ��
	private String send_time;//���ط���ʱ��
	private String recv_time;//�û�����ʱ��
	private int send_type;//����

	//��չ�ֶ�(����)
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
