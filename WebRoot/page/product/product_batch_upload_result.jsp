<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@page import="com.hisupplier.commons.util.WebUtil"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>批量上传产品结果</title>
	</head>
	<body>
		<div class="operateTips">
			<s:if test="result.error != ''">
				<div class="operateFail"><s:property value="result.error"/></div>
			</s:if>
			<s:else>
				<s:if test="result.sizeLimit > 0">
					只允许一次上传<span style="color:red;"><s:property value='result.sizeLimit'/></span>个产品，修改后请再次<a href="#" onclick="javascript:$('#uploadExcelForm').show();">上传</a>
				</s:if>
				<s:elseif test="result.errorCount > 0">
					<div class="operateFail">
						操作失败，只成功添加了<span style="color:red;"><s:property value='result.successCount'/></span>个产品<br />
						<span style="font-weight:normal;font-size:12px;">请再次<a href="<s:property value='result.errorFilePaht'/>">下载表格</a>,错误的信息已用红色标识，错误提示信息在最后一列，修改后请再次<a href="#" onclick="javascript:$('#uploadExcelForm').show();">上传</a></span>
					</div>
				</s:elseif>
				<s:elseif test="result.successCount > 0">
					<div class="operateSuccess">
						成功批量上传<s:property value="result.successCount"/>个产品！<br />
						<span style="font-weight:normal;font-size:12px;">点击<a href="/product/product_list.htm">此处</a>查看已提交的批量产品</span>
					</div>
				</s:elseif>
			</s:else>
		</div>
		<form style="display:none;" id="uploadExcelForm" method="post" enctype="multipart/form-data" action="/product/batch_add_submit.htm" >
			<span style="margin-left:80px;margin-right:30px;">上传填写后的表格：</span>
			<input type="file" name="attachment" value="" onchange="this.nextSibling.nextSibling.disabled='';"/>
			<input type="submit" value="上传" disabled="disabled"/>
		</form>
	</body>
</html>