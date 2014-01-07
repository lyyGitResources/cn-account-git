<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='inquiry.listTitle'/></title>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js"></script>
		<script type="text/javascript">
			$(document).ready(function () {
				getProvince("province","city","country","code","countryCode");
				
				//绑定表单验证
				$("#searchForm").validate({
					submitHandler: function(){ 
						ListTable.params["queryBy"] = $("#queryBy").val();
						ListTable.params["queryText"] = $("#queryText").val();
						if($("#userId").size() > 0){
							ListTable.params["userId"] = $("#userId").val();
						}			
						ListTable.params["countryCode"] = $("#countryCode").val();
						ListTable.params["pageNo"] = 1;
						ListTable.reload();
					}
				});
			});
			
			function checkSelect(){
				if(!Util.isChecked('inqId')){
					alert('<s:text name="inquiry.selectLimit" />');
					return false;
				}
			}
			
			function tabMenu(index, params){
				$("#imgType").val(params.imgType);
				$(".tabMenu li").each(function(){
					$(this).removeClass("current");
				});
				if ($("#userId").length) params.userId = $("#userId").val(index === 1 ? -1 : this.value);
				params.queryBy = $("#queryBy").val();
				params.queryText = $("#queryText").val(index === 1 ? "" : this.value);
				params.countryCode = $("#province").val(index === 1 ? -1 : this.value);
				$(".tabMenu li:nth-child("+index+")").eq(0).addClass("current");
				ListTable.reload(params); 
			}
		</script>
	</head>
	<body>
		<div class="tabMenu">
			<ul>
				<li>
					<span onclick="tabMenu(1,{empty:true})">
					<s:if test="loginUser.admin"><s:text name='inquiry.inquiryTotal'/></s:if>
					<s:else><s:text name='inquiry.myInquiryTotal'/></s:else>
					[<s:property value="result.inquiryCount"/>]</span>
				</li>
				<li class="current"><span onclick="tabMenu(2,{recommend:0})">公司询盘[<s:property value="result.unRecommendCount"/>]</span></li>
				<li><span onclick="tabMenu(3,{recommend:1})">推荐询盘[<s:property value="result.recommendCount"/>]</span></li>
				<li><span onclick="tabMenu(4,{read:0})"><s:text name='inquiry.unreadCount'/>[<s:property value="result.unreadCount"/>]</span></li>
				<li><span onclick="tabMenu(5,{read:1})"><s:text name='inquiry.readCount'/>[<s:property value="result.readCount"/>]</span></li>
			</ul>
		</div>	
		
		<form class="searchForm" id="searchForm">
			<table>
				<tr>
					<td>
						<s:if test="result.userList != null && result.userList.size > 0">
							<select name="userId" id="userId" style="width: 115px;">
								<option value="-1"><s:text name='inquiry.byAllUserId'/></option>
								<s:iterator value="result.userList" id="user" status="st">
									<option value="<s:property value='#user.userId' />" <s:if test="userId == #user.userId">selected</s:if>>
										<s:if test="#user.admin"><s:text name="search.userAdmin"/></s:if><s:else><s:property value='#user.email' /></s:else>
									
								</s:iterator>
							</select>
						</s:if>
					</td>
					<td>
						<select name="queryBy" id="queryBy">
							<option value="subject">
								<s:text name='inquiry.bySubject' />
							</option>
							<option value="fromName">
								<s:text name='inquiry.byFromName' />
							</option>
						</select>
					</td>
					<td>
						<input name="queryText" id="queryText" style="width:200px;" />
					</td>
					<td>
						<select id="province"></select>
						<input type="hidden" name="countryCode" id="countryCode" value="">	
					</td>		
								
					<td><input type="submit" class="searchButton" value="<s:text name='button.search'/>"/></td>
				</tr>
			</table>
		</form>
		
		<form method="post" action="/inquiry/inquiry_delete.htm" onsubmit="return checkSelect();">
			<s:if test="loginUser.admin">
				<div class="buttonLeft">
					<input type="submit" value="<s:text name='inquiry.delete'/>"/>
				</div>
			</s:if>
	
			<div id="listTable">
				<%@ include file="/page/inquiry/inquiry_list_inc.jsp" %>
			</div>
		</form>
	</body>
</html>