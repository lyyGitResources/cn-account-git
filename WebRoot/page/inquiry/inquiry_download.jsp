<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/page/inquiry/functions.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='inquiry.downloadTitle'/></title>
		<script type="text/javascript">
			function download(){
				if(!Util.isChecked('month')){
					alert('<s:text name="inquiry.selectLimit" />');
					return false;
				}
			}

			$(function() {
				/*$(":input[name='month']").bind("click",function(){
					if($(":input[name='month']").length == $(":input[name='month']:checked").length) {
						$("#ckAll").attr("checked",true);
					} else {
						$("#ckAll").attr("checked",false);
					}
				});*/
				Util.check_toggle("ckAll", "month");
			});
		
		</script>
	</head>
	<body>
		<div id="chartCode"></div>
		<s:if test="result.userList != null && result.userList.size > 0">
			<s:set name="userId" value="userId" />
			<form class="searchForm" id="inquiry_seach" action="/inquiry/inquiry_download.htm">
				<table>
					<tr>
						<td align="left">
							<input type="checkbox" name="ckAll" />
							全选	
						</td>
						<td>
							<select name="userId" id="userId">
							<option value="-1" <s:if test="userId==-1">selected</s:if>>所有用户</option>
							<s:iterator value="result.userList" id="user">
								<option value="${user.userId}" ${user.userId == userId ? "selected" : "" } >
									<s:if test="#user.admin"><s:text name="search.userAdmin"/></s:if><s:else>${user.email}</s:else>
								</option>
							</s:iterator>
							</select>
						</td>
						<td><input type="submit" class="searchButton" value="<s:text name='button.search'/>"/></td>
					</tr>
				</table>
			</form>
		</s:if>
		<form id="inquiry_download_zip" method="post" action="/inquiry/inquiry_download_zip.htm" onsubmit="return download();" >
			<table width="25%" style="margin:0 auto; line-height:30px; margin-top:10px; margin-left: 5px;">
				<s:iterator value="result.inquiryList" id="list" status="st">
		          	<tr>
		            	<td align="left">
			            	<s:if test="#list.number > 0">
			            		<input type="checkbox" name="month" value="<s:property value='#list.yearMonth' />"/>
			            	</s:if>
			            	<s:else>
			            		<input type="checkbox" name="disabled" disabled="disabled"/>
			            	</s:else>
		            	</td>
		            	<td align="left"> <s:property value='#list.yearMonth' />&nbsp;(<s:property value='#list.number' />) </td>
		          	</tr>
				</s:iterator>
			</table>
			<div class="searchForm"><input type="submit" class="searchButton" value="<s:text name="button.download" /> "  style="margin-top: 3px;"/></div>
		</form>
	</body>
</html>
