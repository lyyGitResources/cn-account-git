/* 
 * Created by wuyaohui at 2011-8-2
 * Copyright HiSupplier.com 
 */
package com.hisupplier.cn.account.product;

import org.apache.struts2.ServletActionContext;

import com.hisupplier.cn.account.entity.LikeRecord;
import com.hisupplier.commons.Config;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author wuyaohui
 *
 */
public class LikeRecordAction extends ActionSupport implements ModelDriven<LikeRecord> {

	private static final long serialVersionUID = 1746296020528229116L;

	private LikeRecord likeRecord = new LikeRecord();
	private LikeRecordService likeRecordService;
	private String msg;

	public String likeRecord_add() throws Exception {
		likeRecord = likeRecordService.addLikeRecord(ServletActionContext.getRequest(), likeRecord);
		return "json";
	}

	public String login() throws Exception {
		if ("showRoom".equals(msg)) {
			msg = new StringBuilder("http://detail.cn.")
						.append(Config.getString("sys.domain"))
						.append("/product_detail.htm?type=Product&proId=")
						.append(likeRecord.getProId()).toString();
			//msg = "http://detail.cn.hisupplier.com/product_detail.htm?type=Product&proId=" + likeRecord.getProId();
		} else {
			msg = new StringBuilder("http://cn.").append(Config.getString("sys.domain"))
						.append("/detail/product_detail.htm?proId=").append(likeRecord.getProId()).toString();
			//msg = "http://cn.hisupplier.com/detail/product_detail.htm?proId=" + likeRecord.getProId();
		}
		return "redirect";
	}

	public String likeRecord_remove() throws Exception {
		likeRecord = likeRecordService.deleteLikeRecord(ServletActionContext.getRequest(), likeRecord);
		return "json";
	}

	public String likeRecord_show() throws Exception {
		likeRecord = likeRecordService.getLikeRecord(ServletActionContext.getRequest(), likeRecord);
		return "json";
	}

	@Override
	public LikeRecord getModel() {
		return likeRecord;
	}

	public void setLikeRecordService(LikeRecordService likeRecordService) {
		this.likeRecordService = likeRecordService;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
