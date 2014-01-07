<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='inquiryReply.viewTitle'/></title>
		<script type="text/javascript" src="/js/fckeditor/fckeditor.js"></script>
		<script type="text/javascript" src="/js/fckeditor/fckeditor.extend.js"></script>		
		<script type="text/javascript">			
			function clickPreId(){
				window.location.href="/inquiry/inquiry_view.htm?inqId=<s:property value='result.reply.inqId'/>";
			}
		</script>
	</head>
	
	<body>
		<div class="buttonLeft">
			<input type="button" value="<s:text name='inquiry.viewTitle'/>" style="float:left;" onclick="clickPreId();"/>
		</div>

		<table class="formTable">
	      	<tr>
	        	<th><s:text name="inquiryReply.toName" />:</th>
	        	<td><s:property value="result.reply.toName"/></td>
	      	</tr>
	      	<tr>
	        	<th><s:text name="inquiryReply.toEmail" />:</th>
	        	<td><s:property value="result.reply.toEmail"/></td>
	      	</tr>
	      	<tr>
	        	<th><s:text name="inquiryReply.createTime" />:</th>
	        	<td><s:property value="result.reply.createTime"/></td>
	      	</tr>
	      	<tr>
	        	<th><s:text name="inquiryReply.subject" />:</th>
	        	<td><s:property value="result.reply.subject"/></td>
	      	</tr>
	      	<tr>
	        	<th><s:text name="inquiryReply.content" />:</th>
        		<td>
					<s:property value="result.reply.content" escape="false"/>
				</td>
	      	</tr>
	      	<s:if test="result.reply.filePathUrl != null">
		      	<tr>
		        	<th><s:text name="attachment" />:</th>
		        	<td>
		        	<s:iterator value="result.reply.filePathUrl" id="filePath">
		        		<a href="<s:property value="#filePath"/>" target="_blank" ><s:property value="#filePath"/></a><br />
		        	</s:iterator>
		        	
		      	</tr>
	      	</s:if>
		</table>
	</body>
</html>
