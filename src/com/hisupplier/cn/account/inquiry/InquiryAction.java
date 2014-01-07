package com.hisupplier.cn.account.inquiry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Inquiry;
import com.hisupplier.commons.util.FileUtil;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

public class InquiryAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = 8416000403688890665L;

	private InquiryService inquiryService;
	private QueryParams params = new QueryParams();
	private Map<String, Object> result;

	private String zipFilename;
	private String dir;

	public String inquiry_list() throws Exception {
		result = inquiryService.getInquiryList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String inquiry_view() throws Exception {
		result = inquiryService.getInquiryView(params);
		return SUCCESS;
	}

	public String inquiry_reply_view() throws Exception {
		result = inquiryService.getInquiryReplyView(params);
		return SUCCESS;
	}

	public String inquiry_reply_add() throws Exception {
		result = inquiryService.getInquiryReplyAdd(params);
		return SUCCESS;
	}

	public String inquiry_delete() throws Exception {
		tip = inquiryService.deleteInquiry(params);
		this.addMessage(getText(tip));
		return SUCCESS;
	}

	public String inquiry_chart() throws Exception {
		result = inquiryService.getInquiryChart(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}
 
	public String inquiry_chart_stat() throws Exception {
		result = inquiryService.getInquiryChart(params);
		return SUCCESS;
	}

	public String inquiry_download() throws Exception {
		result = inquiryService.getInquiryDownloadView(params);
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String inquiry_download_zip() throws Exception {
		String memberId = this.getLoginUser().getMemberId();

		result = inquiryService.getInquiryDownload(params);

		String zipFilePath = this.makeZip(memberId, (List<Inquiry>) result.get("inquiryList"), ServletActionContext.getServletContext().getRealPath(""));
		if (zipFilePath == null) {
			return "error";
		}
		return SUCCESS;
	}

	public String inquiry_set() throws Exception {
		result = inquiryService.getInquirySet(params);
		this.addMessage(getText("addSuccess"));
		return SUCCESS;
	}

	public String inquiry_set_submit() throws Exception {
		tip = inquiryService.updateInquirySet(params);
		this.addMessage(getText(tip));
		return SUCCESS;
	}

	public String inquiry_recycle_list() throws Exception {
		result = inquiryService.getInquiryRecycleList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String inquiry_recycle_reuse() throws Exception {
		tip = inquiryService.updateInquiryRecycleReuse(params);
		this.addMessage(getText(tip));
		return SUCCESS;
	}

	public String inquiry_recycle_delete() throws Exception {
		tip = inquiryService.updateInquiryRecycleDelete(params);
		this.addMessage(getText(tip));
		return SUCCESS;
	}

	public String inquiry_recycle_empty() throws Exception {
		tip = inquiryService.updateInquiryRecycleEmpty(params);
		this.addMessage(getText(tip));
		return SUCCESS;
	}

	public InputStream getTargetFile() throws Exception {
		return ServletActionContext.getServletContext().getResourceAsStream(dir + File.separator + zipFilename);
	}

	// 创建zip压缩文件
	private String makeZip(String memberId, List<Inquiry> list, String rootPath) {
		Pattern pattern_filename = Pattern.compile("(\\|/|:|\\*|\\?|\"|\\<|\\>|\\|){1,}", Pattern.CASE_INSENSITIVE);
		// 基本参数
		dir = File.separator + "var" + File.separator + "tmp" + File.separator + memberId;
		zipFilename = "inquiry_" + memberId + ".zip";
		String zipOutputDir = rootPath + dir;
		String zipInputDir = rootPath + dir + File.separator + "tmp";
		String result = dir + File.separator + zipFilename;
		try {
			for (int i = 0; i < list.size(); i++) {
				// 使用 subject 和 createTime 做文件名
				String subject = list.get(i).getSubject();
				String filename = StringUtil.replaceAll(subject, pattern_filename, "") + "[" + list.get(i).getCreateTime().substring(0, 9) + "]" + (i + 1) + ".eml";
				// 使用yyyy-mm格式的日期做文件目录
				String dirPath = zipInputDir + File.separator + list.get(i).getCreateTime().substring(0, 7);
				// 写入文件
				this.write(dirPath, filename, list.get(i), memberId);
			}
			// 打包压缩
			FileUtil.zip(zipOutputDir, zipFilename, zipInputDir, "GBK");
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} finally {
			// 删除该目录下的所有目录或文件
			this.delete(zipInputDir);
		}
		return result;
	}

	// 写入htm文件
	private void write(String dirPath, String fileName, Inquiry inquiry, String memberId) {
		PrintWriter pw = null;
		try {
			File file = FileUtil.createNewFile(dirPath, fileName);
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

			pw.println("Date: " + inquiry.getCreateTime());
			pw.println("From: \"" + inquiry.getFromName() + "\"<" + inquiry.getFromEmail() + ">");
			pw.println("To: " + memberId);
			pw.println("Subject: " + inquiry.getSubject());
			pw.println("Content-Type: text/html;charset=\"UTF-8\"");
			pw.println("\n");
			pw.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
			pw.println("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head>");
			pw.println("<META http-equiv=Content-Type content=\"text/html; charset=UTF-8\">");
			pw.println("</head>");
			pw.println("<body><div>");
			pw.println("Subject: " + inquiry.getSubject());
			pw.println("<br />");
			pw.println("Date: " + inquiry.getCreateTime());
			pw.println("<br />");
			pw.println("From: " + inquiry.getFromName());
			pw.println("<br />");
			pw.println("From Email: " + inquiry.getFromEmail());
			pw.println("<br />");
			pw.println("<br />");
			pw.println(inquiry.getContent());
			pw.println("</div>");
			pw.print("</body></html>");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	// 删除path路径下的所有目录或文件
	private void delete(String path) {
		try {
			File file = new File(path);
			if (file.isDirectory()) {
				File[] list = file.listFiles();
				for (File tmp : list) {
					delete(tmp.getAbsolutePath());
				}
				file.delete();
			} else {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public QueryParams getModel() {
		return params;
	}

	public void setInquiryService(InquiryService inquiryService) {
		this.inquiryService = inquiryService;
	}

	public String getZipFilename() {
		return zipFilename;
	}

	public void setZipFilename(String zipFilename) {
		this.zipFilename = zipFilename;
	}
}
