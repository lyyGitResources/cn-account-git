<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			<s:text name="video.listTitle"/>
			<span class="gray">
				<s:text name="video.exist">
					<s:param><s:property value="result.videoCount"/></s:param>
					<s:param><s:property value="result.videoMax"/></s:param>
				</s:text>
			</span>
		</title>
		<meta name="memoinfo" content="<s:text name='memo.info2'/>"/>
		<script type="text/javascript">
			$(document).ready(function () {
				$("#editFormDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 380,
					width: 550,
					modal: true,
					close: function(){
						$("#editFormDialog").empty();
					}
				});
				//绑定表单验证
				$("#searchForm").validate({
					submitHandler: function(){ 
						ListTable.reload({
							queryBy: $("#queryBy").val(),
							queryText: $("#queryText").val(),
							groupId:$("#groupId").val(),
							pageNo:1
						}, true);
					}
				});
			});
			function showEditFormDialog (url) {
				$("#editFormDialog").load(url,{random:Math.random()});
				$("#editFormDialog").dialog('open');
			}
		</script>
	</head>
	<body>
		<%@ include file="/page/inc/upload_video.jsp"%>
		<div id="editFormDialog" title='<s:text name="video.editTitle"/>'></div>
		<div class="pageTips">
			 视频格式<br/>
			1. 支持avi、flv、mp4、mpg、mov、asf、3gp、f4v、wmv、rmvb、rm、ts格式<br/>
			2. 不超过400M <br/>
		</div>
		<div class="tabMenu">
			<ul>
				<li class="current"><span onclick="tabMenu(1,{videoType:'all'})"><s:text name="video.videoCount"/>[<s:property value="result.videoCount"/>]</span></li>
				<li><span onclick="tabMenu(2,{videoType:'stateReject'})"><s:text name="video.rejectVideoCount"/>[<s:property value="result.rejectVideoCount"/>]</span></li>
				<li><span onclick="tabMenu(3,{videoType:'Company'})"><s:text name="video.companyVideoCount"/>[<s:property value="result.companyVideoCount"/>]</span></li>
				<li><span onclick="tabMenu(4,{videoType:'Product'})"><s:text name="video.productVideoCount"/>[<s:property value="result.productVideoCount"/>]</span></li>
				<li><span onclick="tabMenu(5,{videoType:'Menu'})"><s:text name="video.menuVideoCount"/>[<s:property value="result.menuVideoCount"/>]</span></li>
			</ul>
		</div>

		<div class="buttonLeft">
			<input type="button" onclick="document.location.href='/video/video_group_list.htm'" value="<s:text name='button.manageVideoGroup'/>"/>
			<s:if test="result.videoMax > result.videoCount">
				<input type="button" onclick="javasrcipt:uploadVideo()" value="<s:text name='button.upVideo'/>">
			</s:if>
		</div>

		<form class="searchForm" id="searchForm">
			<table>
				<tr>
					<td><input type="hidden" name="queryBy" id="queryBy" value="title"/></td>
					<td>
						<input name="queryText" id="queryText" value="<s:text name="video.title.required"/>" style="width:200px;" oriValue="<s:text name="video.title.required"/>"/>
					</td>
					<td>
						<select name="groupId" id="groupId">
							<option value="-1" <s:if test="result.groupId !> 0">selected</s:if> ><s:text name='videoGroup.AllGroup'/></option>
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
			<s:include value="/page/misc/video_list_inc.jsp"/>
		</div>
	</body>
</html>
