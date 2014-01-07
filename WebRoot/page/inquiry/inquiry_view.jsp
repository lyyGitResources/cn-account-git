<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='inquiry.viewTitle'/></title>
		<script type="text/javascript">
			function clickDelete(){
				window.location.href="/inquiry/inquiry_delete.htm?inqId=<s:property value='result.inquiry.inqId'/>";
			}
			
			function clickAdd(){
				window.location.href="/inquiry/inquiry_reply_add.htm?inqId=<s:property value='result.inquiry.inqId'/>";
			}
			
			function clickNextId(){
				window.location.href="/inquiry/inquiry_view.htm?inqId=<s:property value='result.nextId'/>";
			}
			
			function clickPreId(){
				window.location.href="/inquiry/inquiry_view.htm?inqId=<s:property value='result.preId'/>";
			}
		</script>
	</head>
	<body>
		<div class="buttonLeft">
			<input type="button" value="<s:text name='inquiry.delete'/>" style="float:left;" onclick="clickDelete();"/>
			
			<input type="button" value="<s:text name='button.reply'/>" style="float:left;"  onclick="clickAdd();"/>
			<div style="float:right; margin-top:10px; margin-right:10px;">
			<s:if test="result.nextId != 0">
				<a href="javascript:clickNextId();"><s:text name='inquiry.nextInquiry'/></a>
				<%-- 
			   	<input type="button" value="<s:text name='inquiry.nextInquiry'/>" style="float:right;" onclick="clickNextId();"/>
			   	--%>
		   	</s:if>
			<s:if test="result.preId != 0">
				<a href="javascript:clickPreId();"><s:text name='inquiry.preInquiry'/></a>
				<%-- 
					<input type="button" value="<s:text name='inquiry.preInquiry'/>" style="float:right;" onclick="clickPreId();"/>
				--%>
			</s:if>
			</div>
		</div>

		<s:if test="result.replyList.size > 0">
			<strong style="font-size:12px;"><s:text name='inquiry.allReply'/></strong>
			<table cellspacing="1" class="listTable">
				<tr>
					<th width="17%"><s:text name='inquiryReply.createTime'/></th>
					<th width="43%"><s:text name='inquiryReply.subject'/></th>
					<th width="15%"><s:text name='inquiryReply.toName'/></th>
					<th width="15%"><s:text name='inquiryReply.toEmail'/></th>
				</tr>
				<s:iterator value="result.replyList" id="reply" status="st">
				<tr>
					<td><s:property value="#reply.createTime"/></td>
					<td>
						<a href="/inquiry/inquiry_reply_view.htm?id=<s:property value="#reply.id"/>">
							<s:property value="#reply.subject"/>
						</a>
					</td>
					<td><s:property value="#reply.toName"/></td>
					<td><s:property value="#reply.toEmail"/></td>
				</tr>						
				</s:iterator>
			</table>
		</s:if>
		
		<table class="formTable" cellpadding="2">
	      	<tr>
	        	<th style="width: 12%; padding-right:5px;"><s:text name="inquiry.subject" />:</th>
	        	<td style="padding:5px;padding-top;5px;*padding-top:7px;"><s:property value="result.inquiry.subject"/></td>
	      	</tr>
	      	<tr>
	        	<th style="width: 12%; padding-right:5px;"><s:text name="inquiry.createTime" />:</th>
	        	<td>
	        		<s:property value="result.inquiry.createTime"/>
	      			<s:text name="inquiry.replyDay"><s:param><s:property value="result.inquiry.replyDay"/></s:param></s:text>
	        	</td>
	      	</tr>
	    	<tr>
	    		<th style="width: 12%; padding-right:5px;"><s:text name="inquiryReply.content" />:</th>
	    		<td colspan="2" align="center" style="padding: 0; width:auto;">
	    			<center>
	    			<iframe src="/page/inquiry/content.jsp?inqId=<s:property value='result.inquiry.inqId'/>" scrolling="auto" width="600" height="1000px" frameborder="0"></iframe>
	    			</center>
	    		</td>
	    	</tr>
		</table>
	</body>
</html>
