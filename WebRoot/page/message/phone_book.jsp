<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ page import="com.hisupplier.cn.account.entity.Contact"%>
<%@ page import="com.hisupplier.cn.account.entity.ContactGroup"%>
<%@ page import="com.hisupplier.commons.util.Coder"%>
<%@ page import="com.hisupplier.commons.page.Page"%>
<%@ page import="com.opensymphony.xwork2.util.*"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.hisupplier.commons.page.ListResult"%>
<%@page import="com.hisupplier.cn.account.message.QueryParams"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	ValueStack vs = (ValueStack) request.getAttribute("struts.valueStack");
	Map<String,Object> result = (Map<String,Object>)vs.findValue("result");
	QueryParams queryParams=(QueryParams)vs.findValue("model");
	ListResult listResult=(ListResult)result.get("listResult");
	List<ContactGroup> groupList=(List<ContactGroup>)result.get("contactgroup");
	Page p = listResult.getPage();
	int type = queryParams.getType();
	int groupId = queryParams.getGroupId();
	String keyword = queryParams.getKeyword();
%>
<s:set name="groupList" value="result.contactgroup" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>短信群发</title>
    <script src="<%=Config.getString("img.base")%>/js/lib/jquery.checkbox.min.js"></script>
    <link href="/css/pop.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
		$(document).ready(function(){
			
			$("#cb_all").checkbox(".cb_ch");
			
			$("#contactModifyDiv,#groupModifyDiv").dialog({
				bgiframe: true,
				autoOpen: false,
				height: 230,
				width: 405,
				modal: true
			});
				
			$("#importDiv").dialog({
				bgiframe: true,
				autoOpen: false,
				height: 250,
				width: 660,
				modal: true
			});
				
			$("#exportDialog").dialog({
				bgiframe: true,
				autoOpen: false,
				height: 300,
				width: 700,
				modal: true
			});
			
			$("#contactAdd").submit(function(){
				if($.trim($("#contactName").val()) == "" || $.trim($("#mobile").val()) == ""){
					alert("请填写姓名和手机号！");
					return false;
				}
			});
			$("#contactGroupAdd, #modifyContactGroupForm").submit(function(){
				if($.trim($("#groupName").val()) == ""){
					alert("请填写组名！");
					return false;
				}
			});
			$("#contactGroupAdd").submit(function(){
				if($.trim($("#groupName_add").val()) == ""){
					alert("请填写组名！");
					return false;
				}
			});
			$("#phonebookTip").tooltip({
				title:'导入电话簿',
				detail:'标准Excel格式导入，在sheet1的A列和B列分别填入姓名和号码，如：张三 13800000000；TXT文本导入，每行填写一对姓名和号码，并用英文逗号分隔开，如：张三,138000000。',
				toolTipwidth:300
			});
			
			$("#batch_delete").click(function() {
				var $cb_ch = $(".cb_ch:checked");
				if ($cb_ch.length <= 0) {
					alert("请至少选择一个联系人。");
					return false;
				}
				var values = [];
				$cb_ch.each(function() {
					values.push(this.value);
				});
				if (confirm("确定要删除已选中的联系人吗？")) {
					window.location.href = "/message/batchDeleteContact.do?contactIds=" + values.join(',') + "&type=<s:property value='type' />&keyword=<s:property value='keyword' />&groupId=<s:property value='groupId'/>";
					return false;
				}
			});
		});
			
		function modifyContact(id){
			var form = document.forms['modifyContactForm'];
			var groupId = $("#contact_"+id).attr("groupId");
			form.contactId.value = id;
			form.contactName.value = $("#contact_"+id).attr("contactName");
			form.mobile.value = $("#contact_"+id).attr("mobile");
			for(var i=0;i<form.groupId.options.length;i++){
				if(form.groupId.options[i].value==groupId){
					form.groupId.options[i].selected=true;
					break;
				}
			}
			$("#contactModifyDiv").dialog('option', 'title', "修改联系人信息");
			$("#contactModifyDiv").dialog("open");
		}
			
		function modifyContactGroup(id){
			var form = document.forms['modifyContactGroupForm'];
			form.groupId.value = id;
			form.groupName.value = $("#group_"+id).attr("groupName");
			$("#groupModifyDiv").dialog('option', 'title', "修改人组名");
			$("#groupModifyDiv").dialog("open");
		}
			
		function showImport(){
			$("#importDiv").dialog('option', 'title', "从电话薄导出");
			$("#importDiv").dialog("open");				
		}
		function setDeleteGroupUrl(id){
			if(confirm("确定要删除该组吗")){
				window.location.href="/message/deleteContactGroup.htm?groupId="+id;
			}
		}
		function setDeleteContactUrl(id){
			if(confirm("确定要删除该联系人吗")){
				window.location.href="/message/deleteContact.htm?contactId="+id+"&type=<%=type%>&groupId=<%=groupId%>&keyword=<%=keyword%>&pageNo=<%=p.getPageNo()%>";
			}
		}
		function showExport(){
			$("#iframe").attr("src", "/message/export.do");
			$("#exportDialog").dialog("open");
		}
	</script>

	</head>

	<body>
	<div class="SMSbox" id="SMSbox">
		<div class="tabMenu">
			<ul>
				<li>
					<span onclick="location.href='/message/form.htm'">发送短消息</span>
				</li>
				<li>
					<span onclick="location.href='/message/messageLog.htm'">已发短信</span>
				</li>
				<li class="current">
					<span onclick="location.href='/message/phoneBook.htm'">电话簿</span>
				</li>
				<li>
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
				<form class="tab2" id="contactAdd" action="/message/addContact.htm" enctype="multipart/form-data" method="post">
					<label for="contactName">姓名：</label>
					<input type="text" name="contactName" id="contactName" autocomplete=off maxlength=40 />
					<label for="mobile">手机号：</label>
					<input type="text" name="mobile" id="mobile" autocomplete=off maxlength=15 />
					<label for="addGroupId">分组：</label>
					<s:select id="addGroupId" name="groupId" list="#groupList" headerKey="0" headerValue="未分组" listKey="groupId" listValue="groupName"/>
					<input type="submit"  value="添加" class="button" />
				</form>
				<table style="overflow:hidden;"><tr>
					<td style="vertical-align: top;">
						<form class="searchBox5" action="/message/phoneBook.htm" method="get">
						<table>
						<tr>
							<th>
								<select id="searchType" name="type" style="width:93px;">
								<option value="1" <%=type==1?"selected":"" %>>姓名</option>
								<option value="2" <%=type==2?"selected":"" %>>手机号</option>
								</select>
							</th>
							<th>
								<input type="text" name="keyword" autocomplete=off style="width:163px;" value='<s:property value="keyword"/>' />
							</th>
							<td>
								<select id="searchGroupId" name="groupId" style="width:93px;">
								<option value="-1">所有分组</option>
								<%for(ContactGroup group : groupList){ %>
								<option value="<%=group.getGroupId() %>" <%=groupId==group.getGroupId()?"selected":"" %>><%=group.getGroupName() %></option>
								<%} %>
								<option value="0" <%=groupId==0?"selected":"" %>>未分组</option>
								</select>
							</td>
							<td><input type="submit" value="查询" class="button" /></td>
						</tr>
						</table>
						</form>
						<div style="margin-bottom: 15px;">
							<a id="batch_delete" href="javascript:void(0)">删除联系人</a>&nbsp;
							<a href="javascript:showExport();">导入到电话簿</a>&nbsp;<span id="phonebookTip"></span>&nbsp;
							<a href="javascript:showImport();">从电话簿导出</a>
						</div>
						<div class="tableList">
						<table width="100%" border="0" cellpadding="2" cellspacing="1" class="tableRank">
							<tr>
							<th><input id="cb_all" type="checkbox" /></th>
							<th>序号</th>
							<th>姓名</th>
							<th>分组</th>
							<th>手机号</th>
							<th>选项</th>
							</tr>
							<s:iterator value="result.listResult.list" id="contact" status="st">
							<tr>
								<td><input class="cb_ch" type="checkbox" value="<s:property value='#contact.id' />" /></td>
								<td class="td1"><s:property value="#st.index + 1"/></td>
								<td><s:property value="#contact.contactName"/></td>
								<td><s:if test="#contact.groupName==null">未分组</s:if><s:else><s:property value="#contact.groupName"/></s:else></td>
								<td><s:property value="#contact.mobile"/></td>
								<td>
									<a href="javascript:setDeleteContactUrl(<s:property value='#contact.id' />);">删除</a>
									&nbsp;&nbsp;<a href="javascript:modifyContact(<s:property value='#contact.id' />);">修改</a>
									<input type="hidden" id="contact_<s:property value='#contact.id' />" 
										contactName="<s:property value='#contact.contactName' />" 
										groupId="<s:property value='#contact.groupId' />" 
										mobile="<s:property value='#contact.mobile' />"/>
								</td>
							</tr>
							</s:iterator>
						</table>
			        	</div>
			        	</td>
			        	<td style="vertical-align: top;">
			        	<div class="manage" style="margin-top: 0px;" >
			          		<div class="top"></div>
			          		<div class="text"> 
			          			<form id="contactGroupAdd" action="/message/addContactGroup.htm" method="get">
				          			<span>管理组：
				            			<input name="groupName" id="groupName_add" type="text" autocomplete=off maxlength=30 style="width:107px;" />&nbsp;
				            			<span class="button5Span">
				            				<input type="submit" value="&nbsp;添加&nbsp;" class="button5" />
				            			</span>
				            		</span>
			            		</form>
					            <table border="0" cellspacing="0" cellpadding="0" width="100%">
					              <tr>
					                <th>组名</th>
					                <th>选项</th>
					              </tr>
								<%for(ContactGroup group : groupList){ %>
					              <tr>
					                <td><%=group.getGroupName() %></td>
					                <td><a href="javascript:setDeleteGroupUrl(<%=group.getGroupId() %>);">删除</a>&nbsp;&nbsp; <a href="javascript:modifyContactGroup(<%=group.getGroupId() %>);">修改</a></td>
					              	<input type="hidden" id="group_<%=group.getGroupId() %>" groupName="<%=Coder.encodeXHTML(group.getGroupName()) %>">
					              </tr>
					              <%} %>
					            </table>
			          		</div>
			          		<div class="bottom"></div>
			        	</div>
			        	</td>
			        	</tr>
			      	</table>
			    <%
				String url = "/message/phoneBook.htm?keyword="+keyword+"&type="+type+"&groupId="+groupId+"&pageNo="+Page.PAGE_NO;
				p.setPagUrl(url);
				%>
	        	<%@include file="/page/message/phoneBookPageBar.jsp" %>
		    	</div>
		    	<div id="contactModifyDiv" style="display:none;">
			    	<form name="modifyContactForm" action="/message/updateContact.do" enctype="multipart/form-data" method="post">
			    		<table>
			    			<tr><td>姓名：</td><td><input type="text" name="contactName" value="" maxlength=40/></td></tr>
			    			<tr><td>手机号：</td><td><input type="text" name="mobile" value="" maxlength=11/></td></tr>
			    			<tr><td>分组：</td><td>
				    				<select name="groupId" style="width:93px;">
						          	<option value="0">未分组</option>
						          	<%for(ContactGroup group : groupList){ %>
						            	<option value="<%=group.getGroupId() %>" ><%=group.getGroupName() %></option>
							        <%} %>
						          </select>
					          	</td>
					        </tr>
			    		</table>
			    		<input type="hidden" name="contactId" value="">
			    		<input type="hidden" name="pageNo" value="<%=p.getPageNo() %>">
			    		<input type="submit" value="修改">
			    		<input type="button" value="取消" onclick="$('#contactModifyDiv').dialog('close');">
			    	</form>
		    	</div>
		    	<div id="groupModifyDiv" style="display:none;">
			    	<form name="modifyContactGroupForm" id="modifyContactGroupForm" action="/message/updateContactGroup.do" enctype="multipart/form-data" method="post">
			    		<table>
			    			<tr><td>组名：</td><td><input type="text" name="groupName" id="groupName" maxlength=30/></td></tr>
			    		</table>
			    		<input type="hidden" name="groupId">
			    		<input type="submit"  value="修改">
			    		<input type="button"  value="取消" onclick="$('#groupModifyDiv').dialog('close');">
			    	</form>
		    	</div>
		    </div>
		    
		    <div id="exportDialog" title="导入到电话簿">
				<iframe id="iframe" src="" frameborder="no" scrolling="no" style="width:660px;height:250px;"></iframe>
			</div>
		
		    <div id="importDiv" style="display:none;">
		    	<div class="popupBox">
        			<div class="Box">
				    	<form name="importForm" action="/message/import_submit.do" method="post">
				    		 <table border="0" cellspacing="1" cellpadding="0" class="table">
				    			<tr>
				    				<td>需要导出的组</td>
				    				<td>
					    				<select name="groupId" style="width:205px; height:20px;">
							          	<option value="-1" >所有分组</option>
							          	<option value="0">未分组</option>
							          	<%for(ContactGroup group : groupList){ %>
							            	<option value="<%=group.getGroupId() %>" ><%=group.getGroupName() %></option>
								        <%} %>
							          </select>
				    				</td>
				    			</tr>
				    			<tr>
				    				<td>导出的文件格式</td>
				    				<td style="color:#055FAB;">
										<input type="radio" value="1" name="format" id="format1" checked><label for="format1" style="margin:0 10px">标准EXCEL文件</label>
										<input type="radio" value="2" name="format" id="format2"><label for="format2" style="margin:0 10px">逗号分隔的TXT文件</label>			    					
				    				</td>
				    			</tr>
				    			<tr>
				    				<td>导出范围</td>
				    				<td>
										<input type="checkbox" value="1" name="range" id="range1" checked><label for="range1" style="margin:0 10px">姓名</label>
										<input type="checkbox" value="2" name="range" id="range2" checked><label for="range2" style="margin:0 10px">手机号码</label>			    					
				    				</td>
				    			</tr>
				    		</table>
				    		 <table border="0" cellspacing="1" cellpadding="0" class="buttonBox">
				    		 	<tr>
				    		 		<td>
							    		<input type="hidden" name="groupId" value="">
							    		<input type="submit"  value="导出">
							    		<input type="button"  value="取消" onclick="$('#importDiv').dialog('close');">		    		 		
				    		 		</td>
				    		 	</tr>
				    		 </table>
				    		</form>
			    		</div>
			    	</div>
		    	</div>
  </body>
</html>
