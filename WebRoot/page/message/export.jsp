<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>导入到电话薄</title>
		<link href="/css/pop.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.js"></script>
		<script type="text/javascript">
	 		var tip ='<s:property value="tip" />';
	 		$(function(){
	 			if (tip != '') {
	 				alert(tip);
	 				
	 				parent.$('#exportDialog').dialog('close');
	 				parent.location.href = "/message/phoneBook.htm";
	 			}
	 		});
			
			function checkFrom(){
				var filePath = $("#upload").val();
				if(filePath == ""){
					alert("请选择要导入的文件");
					return false;
				}
				
				var ext = filePath.substring(filePath.lastIndexOf(".") + 1);
				var format = $("[name='format']:checked").val();
				if((ext.toLowerCase() == "xls" && format != 1)
				 		|| (ext.toLowerCase() == "txt" && format != 2)){
					alert("文件与选定的格式不匹配,请重新选择文件!");
					return false;
				}

				return true;
			}	
		</script>
	</head>
	<body>
    	<div class="popupBox">
    		<div style="color: red"><s:actionerror /></div>
    		<div class="Box">		
 	 			<form name="exportForm" id="exportForm" action="/message/read_file.do" method="post" enctype="multipart/form-data" onsubmit="return checkFrom();">
					<table border="0" cellspacing="1" cellpadding="0" class="table">
						<tr>
							<td>导入到分组</td>
							<td>
			    				<select name="groupId" style="width:205px; height:20px;" id="group">
 									<s:iterator value="result.groupList" id="group">
 										<option value="<s:property value='#group.groupId' />"><s:property value='#group.groupName' /></option>
 									</s:iterator>
					          </select>
							</td>
						</tr>
						<tr>
							<td>文件格式</td>
							<td style="color:#055FAB;">
								<input type="radio" value="1" name="format" id="format1" checked><label for="format1">标准EXCEL文件</label>
								<input type="radio" value="2" name="format" id="format2"><label for="format2">逗号分隔的TXT文件</label>
							</td>
						</tr>
						<tr>
							<td>选择文件</td>
							<td><input type="file" name="upload" id="upload" ></td>
						</tr>
					</table>
					<table border="0" cellspacing="1" cellpadding="0" class="buttonBox">
					 	<tr>
                            <td>
								<input type="submit" value="下一步" class="button"/>
								<input type="button" value="取消" class="button" onclick="parent.$('#exportDialog').dialog('close');"/>
							</td>
                        </tr>
					</table>
				</form>	
			</div>
    	</div>
 	</body>   	
</html>    	