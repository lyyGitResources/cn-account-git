<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.hisupplier.cn.account.entity.MessageTemplate"%>
<%@ page import="com.hisupplier.commons.page.Page"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ page import="com.hisupplier.commons.util.Coder"%>
<%@ page import="com.opensymphony.xwork2.util.*"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.hisupplier.commons.page.ListResult"%>
<%@page import="com.hisupplier.cn.account.message.QueryParams"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	ValueStack vs = (ValueStack) request.getAttribute("struts.valueStack");
	Map<String,Object> result = (Map<String,Object>)vs.findValue("result");
	ListResult listResult=(ListResult)result.get("listResult");
	QueryParams queryParams=(QueryParams)vs.findValue("model");
	int type = queryParams.getType();
	Page p = listResult.getPage();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>短信群发</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">   
		<script type="text/javascript">
			$(document).ready(function(){
				$("#templateModifyDiv").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 230,
					width: 462,
					modal: true,
					closeOnEscape: true
				});
				$("#templateForm").submit(function(){
					if($.trim($("#content").val()) == "" || $.trim($("#content").val()).length < 5){
						alert("请输入短信内容，最少5个字符！");
						return false;
					}
				});
				
				countNum(60,$("#content"),$("#count"));
			});
			function setType(id){
				window.location.href="/message/template.htm?type="+id;
			}
			function countNum(num, obj, showObj) {
				var spare = num - obj.val().length;
				if(spare >= 0){
					showObj.html(spare);
				}else{
					obj.val(obj.val().substring(0, num));
					showObj.html(0);
				}
			}
			function setDeleteUrl(id){
				if(confirm("确定要删除该短语吗")){
					window.location.href="/message/deleteTemplate.do?templateId="+id+"&type=<%=type%>&pageNo=<%=p.getPageNo()%>";
				}
			}
			function modifyTemplate(id){
				var form = document.forms['modifyTemplateForm'];
				form.templateId.value = id;
				form.content.value=$("#template_"+id).attr("content");
				var length = form.content.value.length;
				$("#count02").html(60-length);
				$("#templateModifyDiv").dialog("open");
			}
		</script>
	</head>
	<body>
				<div class="SMSbox">
				     <div class="tabMenu">
					<ul>
						<li>
							<span onclick="location.href='/message/form.htm'">发送短消息</span>
						</li>
						<li>
							<span onclick="location.href='/message/messageLog.htm'">已发短信</span>
						</li>
						<li>
							<span onclick="location.href='/message/phoneBook.htm'">电话簿</span>
						</li>
						<li class="current">
							<span onclick="location.href='/message/template.htm'">常用短语</span>
						</li>
						<li>
							<span onclick="location.href='/message/chargeLog.htm'">充值记录</span>
						</li>
						<li>
							<span onclick="location.href='/message/charge.htm'">充值</span>
						</li>
					</ul>
				</div>
		      		 <div class="SMSboxLine"></div>
		      		 <div style="overflow:hidden;">
				         <form name="form2" id="templateForm" method="post" action="/message/addTemplate.htm" class="form2">
					         <table border="0" cellspacing="0" cellpadding="0">
					            <tr>
					              <th>短语内容：</th>
					              <td>
					              <textarea id="content" onkeyup="countNum(60,$(this),$('#count'));" name="content" style=" width:354px;height:83px;"><s:property value="content"/></textarea>
					                <div class="gray">(剩余<span class="star" id="count">60</span>个字符)</div></td>
					            </tr>
					            <tr>
					              <td>&nbsp;</td>
					              <td><input type="submit" value="添加" class="button" /></td>
					            </tr>
					         </table>
				         </form>
		      		</div>
			  		<div class="buttonGroup" style="width:100%">
			  			<input type="button"  class="searchButton <%=type==-1?"b":"" %>" value="&nbsp;全部&nbsp;" onclick="setType(-1);"/>&nbsp;
			  			<input type="button"  class="searchButton <%=type==1?"b":"" %>" value="&nbsp;春节祝福&nbsp;" onclick="setType(1);"/>&nbsp;
			  			<input type="button"  class="searchButton <%=type==2?"b":"" %>" value="&nbsp;中秋祝福&nbsp;" onclick="setType(2);"/>&nbsp;
			  			<input type="button"  class="searchButton <%=type==3?"b":"" %>" value="&nbsp;生日祝福&nbsp;" onclick="setType(3);"/>&nbsp;
			  			<input type="button"  class="searchButton <%=type==4?"b":"" %>" value="&nbsp;健康问候&nbsp;" onclick="setType(4);"/>
			  			<input type="button"  class="searchButton <%=type==10?"b":"" %>" value="&nbsp;我的短语&nbsp;" onclick="setType(10);"/>
						<table width="100%" border="0" cellpadding="2" cellspacing="1" class="tableRank" style="margin-top:10px;">
					        <tr>
					          <th width="20%">分类</th>
					          <th>内容</th>
					          <th width="20%">选项</th>
					        </tr>
					        <%
					        int i = 0;
					        List<MessageTemplate> templateList=listResult.getList();
					        for(MessageTemplate template : templateList ){ %>
						        <tr>
						          <td <%=i%2==0?"class='td1'":"class='td2 tdBg'" %>><%=template.getFullType() %></td>
						          <td <%=i%2==0?"class='tdText'":"class='tdBg tdText'" %>><%=template.getContent() %></td>
						          <td <%=i%2==0?"":"class='tdBg'" %>><%if(template.getTypes()==10){ %><a href="javascript:setDeleteUrl(<%=template.getId() %>);">删除</a>&nbsp;&nbsp;<a href="javascript:modifyTemplate(<%=template.getId() %>);">修改</a><%} %>
						          <input type="hidden" id="template_<%=template.getId() %>" content="<%=Coder.encodeXHTML(template.getContent()) %>">
						          </td>
						        </tr>
					        <%i++;} %>
					    </table>
			  		</div>
	        		<%
					String url = "/message/template.htm?type="+type+"&pageNo="+Page.PAGE_NO;
					p.setPagUrl(url);
					%>
					<%@include file="/page/message/phoneBookPageBar.jsp" %>
		    	</div>
		    	<div id="templateModifyDiv" style="display:none;">
			    	<form name="modifyTemplateForm" action="/message/updateTemplate.do" enctype="multipart/form-data" method="post">
			    		<table border="0" cellspacing="0" cellpadding="0">
				            <tr>
				              <th width="20%">短语内容：</th>
				              <td>
				              <textarea id="content" onkeyup="countNum(60,$(this),$('#count02'));" name="content" style="width:300px;height:83px;"></textarea>
				                <div class="gray">(剩余<span class="star" id="count02">60</span>个字符)</div></td>
				            </tr>
					    </table>
			    		<input type="hidden" name="templateId" value="">
			    		<input type="hidden" name="type" value="<%=type %>">
			    		<input type="hidden" name="pageNo" value="<%=p.getPageNo() %>">
			    		<input type="button"  value="取消" onclick="$('#templateModifyDiv').dialog('close');">
			    		<input type="submit"  value="修改">
			    	</form>
		    	</div>
	</body>
</html>