<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<script type="text/javascript">
			$(function () {
				ListTable.url = "/video/video_select.do";
				var queryText = $("#queryText").attr("oriValue");
				$("#queryText").blur(function(){
					if($(this).val() == ""){
						$(this).val(queryText);
					}
				}).focus(function(){
					if($(this).val() == queryText){
						$(this).val("");
					}
				});
				
				$("#searchForm").submit(function(){
					if($("#queryText").val() == queryText){
						$("#queryText").val("");
					}
				});
		
				$("#searchForm").validate({
					submitHandler: function(){ 
						ListTable.params["queryBy"] = $("#queryBy").val();
						ListTable.params["queryText"] = $("#queryText").val();
						ListTable.params["groupId"] = $("#groupIdSearch").val();
						ListTable.params["pageNo"] = 1;
						ListTable.reload({}, true);
					}
				});
			});
		</script>
	</head>
	<body>
	
		<form class="searchForm" id="searchForm" style="width:574px;">
			<table>
				<tr>
					<td><input type="hidden" name="queryBy" id="queryBy" value="title"/></td>
					<td>
						<input name="queryText" id="queryText" value="<s:text name="video.title.required"/>" style="width:200px;" oriValue="<s:text name="video.title.required"/>"/>
					</td>
					<td>
						<select name="groupId" id="groupIdSearch">
							<option value="-1"><s:text name='videoGroup.AllGroup'/></option>
							<s:iterator value="result.groupList" id="group" status="st">
								<option value="<s:property value='#group.groupId'/>" <s:if test="result.groupId == #group.groupId">selected</s:if> ><s:property value='#group.groupName'/></option>
							</s:iterator>
						</select>
					</td>	
					<td><input type="submit" class="searchButton" value="<s:text name='button.search'/>"/></td>
				</tr>
			</table>
		</form>
		<div id="listTable">
			<s:include value="/page/misc/video_select_inc.jsp"/>
		</div>

	</body>
</html>
