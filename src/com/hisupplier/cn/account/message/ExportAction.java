package com.hisupplier.cn.account.message;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;
import com.opensymphony.xwork2.ModelDriven;

public class ExportAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = -759494787232020280L;
	private String fileName;
	private QueryParams params = new QueryParams();
	private Map<String, Object> result;
	private MessageService messageService;
	public static final String[] ALLOW_MIME_TYPE = { 
		"text/plain", 
		"application/vnd.ms-excel", 
		"application/x-msexcel",
		"application/octet-stream"
	};

	public String export() throws Exception {
		result = this.messageService.getContactGroup(this.getLoginUser().getComId());
		return SUCCESS;
	}

	public String read_file() throws Exception {
		if (!StringUtil.containsValue(ALLOW_MIME_TYPE, params.getUploadContentType())) {
			result = this.messageService.getContactGroup(this.getLoginUser().getComId());
			this.addActionError("��ѡ����ȷ���ļ�����");
			return "input";
		}

		if (params.getFormat() == 2) {
			HashMap<String, Object> logSet = this.messageService.exportTXT(params, this.getLoginUser().getComId());
			if (logSet.size() == 0) {
				tip = "��¼��ʽ������鿴������";
				return "txtSuccess";
			}
			int count = (Integer) logSet.get("count");
			int samecount = (Integer) logSet.get("samecount");
			int wrongcount = (Integer) logSet.get("wrongcount");
			tip = "";
			if (samecount > 0) {
				tip += "��" + samecount + "����¼�Ѵ��ڣ������ظ��ύ��";
			}
			if (wrongcount > 0) {
				tip += "��" + wrongcount + "����¼��ʽ������鿴������";
			}
			tip += "�ɹ�����" + count + "����¼��";
			//tip=Coder.encodeURL(tip);
			return "txtSuccess";
		} else {
			fileName = params.getUploadFileName();
			if (fileName.lastIndexOf(".") != -1) {
				fileName = UploadUtil.getNewFileName(new DateUtil(), fileName.substring(fileName.lastIndexOf(".") + 1));
			}

			UploadUtil.write(new FileInputStream(params.getUpload()), ServletActionContext.getServletContext().getRealPath("") + "contactTmp", fileName);
			result = this.messageService.getFileCount(params);
			return SUCCESS;
		}
	}

	public String export_submit() throws Exception {
		HashMap<String, Object> logSet = this.messageService.exportXLS(this.params,
				ServletActionContext.getServletContext().getRealPath("") + "contactTmp" + File.separator + fileName,
				this.getLoginUser().getComId());
		if (logSet.size() == 0) {
			tip = "��¼��ʽ������鿴����������ʧ�ܣ�������";
			return SUCCESS;
		}
		int count = (Integer) logSet.get("count");
		int samecount = (Integer) logSet.get("samecount");
		int wrongcount = (Integer) logSet.get("wrongcount");
		tip = "";
		if (samecount > 0) {
			tip += "��" + samecount + "����¼�Ѵ��ڣ������ظ��ύ��";
		}
		if (wrongcount > 0) {
			tip += "��" + wrongcount + "����¼��ʽ������鿴������";
		}
		tip += "�ɹ�����" + count + "����¼��";
		return SUCCESS;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public QueryParams getModel() {
		return params;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
