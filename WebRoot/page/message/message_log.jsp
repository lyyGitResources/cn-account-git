<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>短信群发</title>
    <script type="text/javascript">
  		$(document).ready(function () {
  			$("#searchForm").validate({
					submitHandler: function(){ 
						ListTable.reload({
							type: $("#type").val(),
							keyword: $("#keyword").val(),
							resultType: $("#resultType").val(),
							pageNo:1
						}, true);
					}
				});
  		})
		function setDeleteUrl(id){
			if(confirm("确定要删除该短信记录吗")){
				$("#msgIdTmp").val(id);
				if(ListTable.params["type"]){
					$("#typeTmp").val(ListTable.params["type"]);
				}
				if(ListTable.params["keyword"]){
					$("#keywordTmp").val(ListTable.params["keyword"]);
				}
				if(ListTable.params["resultType"]){
					$("#resultTypeTmp").val(ListTable.params["resultTypeId"]);
				}
				if(ListTable.params["pageNo"]){
					$("#pageNoTmp").val(ListTable.params["pageNo"]);
				}
				$("#form").attr("action", "/message/message_delete.do");
				$("#form").submit();
			}
		}
		function setRepeatUrl(id){
			if(confirm("确定要重发该短信记录吗")){
				$("#msgIdTmp").val(id);
				if(ListTable.params["type"]){
					$("#typeTmp").val(ListTable.params["type"]);
				}
				if(ListTable.params["keyword"]){
					$("#keywordTmp").val(ListTable.params["keyword"]);
				}
				if(ListTable.params["resultType"]){
					$("#resultTypeTmp").val(ListTable.params["resultTypeId"]);
				}
				if(ListTable.params["pageNo"]){
					$("#pageNoTmp").val(ListTable.params["pageNo"]);
				}
				$("#form").attr("action", "/message/repeatMessageLog.do");
				$("#form").submit();
			}
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
						<li  class="current">
							<span onclick="location.href='/message/messageLog.htm'">已发短信</span>
						</li>
						<li>
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
				    <form id="searchForm" class="searchBox5">
				        <table>
				          <tr>
				            <th><select id="type" name="type" style="width:93px;">
				                <option value="1" selected>手机号</option>
				                <option value="2">短信内容</option>
				                <option value="3">发送时间</option>
				              </select></th>
				            <th><input id="keyword" type="text" name="keyword"/></th>
				            <td><select id="resultType" name="resultType" style="width:93px;">
				                <option value="1" selected>成功</option>
				                <option value="-2" >失败</option>
				              </select></td>
				            <td><input type="submit" value="查询" class="button" /></td>
				          </tr>
				        </table>
				    </form>
			<div id="listTable">
			<%@include file="/page/message/message_log_inc.jsp" %>
			</div>
			<form id="form" method="post">
			<input type="hidden" name="msgId" id="msgIdTmp" />
			<input type="hidden" name="type" id="typeTmp" />
			<input type="hidden" name="keyword" id="keywordTmp" />
			<input type="hidden" name="resultType" id="resultTypeTmp" />
			<input type="hidden" name="pageNo" id="pageNoTmp" />
			</form>
		</div>
  </body>
</html>
