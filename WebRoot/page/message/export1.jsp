<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/error/500.jsp"%> 
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<title>从电话簿导出</title>
	<link href="/css/pop.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.js"></script>
	<script type="text/javascript">  
	 	function checkForm(){
	 		if($("#name").val() == ""){
	 			alert("请选择姓名列");
	 			return false;
	 		}
	 		if($("#mobile").val() == ""){
	 			alert("请选择手机号列");
	 			return false;
	 		}
	 		 if($("#name").val() == $("#mobile").val()){
	 			alert("请不要重复选择");
	 			return false;
	 		}
	 		
	 		return true;
	 	}
	 </script> 
	</head>
	<body>
		<div class="popupBox">
			<div class="Box">
				<form name="form" action="/message/export_submit.do"  method="post" onsubmit="return checkForm();">
					<s:token />
					<input type="hidden" name="fileName" id="fileName" value="<s:property value='fileName' />" />
					<input type="hidden" name="format" id="format" value="<s:property value='model.format' />" />
					<input type="hidden" name="groupId" id="groupId" value="<s:property value='model.groupId' />" />
					<table border="0" cellspacing="1" cellpadding="0" class="table">
						<tr>
							<td>姓名</td>
							<td>
								<select id="name" name="name">
								   	<option value="">请选择列</option>
								   			<s:iterator value="result.list" id="list" status="st">
												<option value="<s:property value='#st.index' />"><s:property value='#list' /></option>
											</s:iterator>
								</select>
							</td>
						</tr>
						<tr>
							<td>手机号</td>
							<td>
								<select id="mobile" name="mobile">
								   	<option value="">请选择列</option>
							   			<s:iterator value="result.list" id="list" status="st">
											<option value="<s:property value='#st.index' />"><s:property value='#list' /></option>
										</s:iterator>
								</select>						
							</td>
						</tr>															
					</table>
					<table border="0" cellspacing="1" cellpadding="0" class="buttonBox">
						<tr>
							<td>
								<input type="button" value="上一步" class="button" onclick="javascript:window.location.href='/message/export.do'"/>
								<input type="submit" value="导入" class="button"/>
								<input type="button" value="取消" class="button" onclick="parent.$('#exportDialog').dialog('close');"/>							
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</body>	
</html>