package com.hisupplier.cn.account.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.struts2.ServletActionContext;

import com.hisupplier.cn.account.entity.Contact;
import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.FileUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;
import com.opensymphony.xwork2.ModelDriven;

public class ImportAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = -7536541457724363003L;
	private QueryParams params = new QueryParams();
	private Map<String, Object> result;
	private MessageService messageService;
	private List<Contact> contactList;
	private String fileName;
	private String dir;

	public InputStream getTargetFile() throws Exception {
		return new FileInputStream(new File(dir + fileName));
	}

	public String import_submit() throws Exception {
		contactList = this.messageService.getContactList(this.getLoginUser().getComId(), params.getGroupId());
		fileName = this.createFile(contactList, ServletActionContext.getServletContext().getRealPath(""));
		return SUCCESS;
	}

	private String createFile(List<Contact> contactList, String rootPath) {
		// 基本参数
		dir = rootPath + "contactTmp" + File.separator;
		this.deleteDirectory(dir);
		String ext = params.getFormat() == 1 ? "xls" : "txt";
		fileName = UploadUtil.getNewFileName(new DateUtil(), ext);
		this.write(params.getFormat(), fileName, dir, contactList);
		return fileName;
	}

	private void write(int format, String fileName, String dirPath, List<Contact> contactList) {
		if (format == 1) {
			try {
				WorkbookSettings set = new WorkbookSettings();
				set.setLocale(new Locale("zh", "CN"));
				set.setEncoding("ISO-8859-1");
				WritableWorkbook workbook = Workbook.createWorkbook(new File(dirPath + fileName), set);
				WritableSheet ws = workbook.createSheet("Contact", 0);
				workbook.setColourRGB(Colour.LIME, 0xff, 0, 0);
				ws.getSettings().setProtected(false);
				ws.getSettings().setDefaultRowHeight(20 * 20);
				ws.getSettings().setDefaultColumnWidth(15);
				WritableFont wf = new WritableFont(WritableFont.ARIAL, 12);
				wf.setItalic(true);
				String[] ranges = StringUtil.split(params.getRange(), ",");
				int count = 0;
				for (Contact contact : contactList) {
					for (int i = 0; i < ranges.length; i++) {
						if ("1".equals(StringUtil.trim(ranges[i]))) {
							ws.addCell(new Label(i, count, contact.getContactName()));
						}
						if ("2".equals(StringUtil.trim(ranges[i]))) {
							ws.addCell(new Label(i, count, contact.getMobile()));
						}
					}
					count++;
				}
				workbook.write();
				workbook.close();
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			PrintWriter pw = null;
			String[] Ranges = StringUtil.split(params.getRange(), ",");
			try {
				File file = FileUtil.createNewFile(dirPath, fileName);
				pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "gb2312"));
				StringBuffer buffer = new StringBuffer();
				for (Contact contact : contactList) {
					for (int i = 0; i < Ranges.length; i++) {
						if ("1".equals(StringUtil.trim(Ranges[i]))) {
							buffer.append(contact.getContactName());
						}
						if ("2".equals(StringUtil.trim(Ranges[i]))) {
							if (i > 0) {
								buffer.append(",");
							}
							buffer.append(contact.getMobile());
						}
					}
					buffer.append("\r\n");
				}
				pw.print(buffer.toString());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				pw.close();
			}
		}
	}

	/**
	 * 删除目录中的所有文件
	 * 
	 * @param filepath
	 */
	private void deleteDirectory(String filepath) {
		try {
			File f = new File(filepath);// 定义文件路径
			if (f.exists() && f.isDirectory()) { // 判断是文件还是目录
				if (f.listFiles().length != 0) {
					File delFile[] = f.listFiles();
					int i = f.listFiles().length;
					for (int j = 0; j < i; j++) {
						if (delFile[j].isDirectory()) {
							deleteDirectory(delFile[j].getAbsolutePath()); // 递归调用del方法并取得子目录路径
						}
						delFile[j].delete();// 删除文件
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}
}
