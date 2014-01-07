package com.hisupplier.cn.account.patent;

import java.io.FileNotFoundException;
import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.PatentDeblocked;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author liuyuyang
 */
public class PatentDeblockedFormAction extends BasicAction implements ModelDriven<PatentDeblocked> {

	private static final long serialVersionUID = 5054023815698763396L;
	private PatentDeblockedService patentDeblockedService;
	private Map<String, Object> result;
	private PatentDeblocked patentDeblocked = new PatentDeblocked();
	private int licenseMulti = 1;//image_multi.jsp页控制显示
	
	public String patentDeblocked_submit() throws Exception {
		super.currentMenu = "patentDeblocked";
		tip = patentDeblockedService.addPatentDeblocked(patentDeblocked, getLoginUser(), false);
		if (!StringUtil.equalsIgnoreCase(tip, "addSuccess")) {
			result = patentDeblockedService.getPatentDeblockedError(getLoginUser(), patentDeblocked);
			this.addActionError(getText(tip));
			return INPUT;
		}
		addMessage("成功提交，请等待审核!");
		return SUCCESS;
	}
	
	public String patentDeblocked_edit_submit() throws Exception {
		super.currentMenu = "patentDeblocked";
		tip = patentDeblockedService.addPatentDeblocked(patentDeblocked, getLoginUser(), true);
		if (!StringUtil.equalsIgnoreCase(tip, "addSuccess")) {
			result = patentDeblockedService.getPatentDeblockedEditError(getLoginUser(), patentDeblocked);
			this.addActionError(getText(tip));
			return INPUT;
		}
		addMessage("修改成功提交，请等待审核!");
		return SUCCESS;
	}
	
	public void validatePatentDeblocked_submit() throws FileNotFoundException {}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public PatentDeblocked getModel() {
		return patentDeblocked;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public void setPatentDeblockedService(PatentDeblockedService patentDeblockedService) {
		this.patentDeblockedService = patentDeblockedService;
	}

	public int getLicenseMulti() {
		return licenseMulti;
	}

	public void setLicenseMulti(int licenseMulti) {
		this.licenseMulti = licenseMulti;
	}
	
}
