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
		<meta name="memoinfo" content="<s:text name='memo.info'/>"/>
		<script type="text/javascript">
			$(document).ready(function () {
				$("#uploadFormDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 480,
					width: 600,
					modal: true,
					close: function(){
						$("#uploadFormDialog").empty();
					}
				});
			});
			
			function showUploadFormDialog(url){
				$("#uploadFormDialog").load(url,{random:Math.random()});
				$("#uploadFormDialog").dialog('open');
			}
		</script>
	</head>
	<body>
		<div class="buttonLeft">
			<input name="button" type="button" onclick="document.location.href='/video/video_group_list.htm'" value="<s:text name='button.manageVideoGroup'/>"/>
			<input name="button" type="button" onclick="javasrcipt:showUploadFormDialog('/video/video_upload.do')" value="<s:text name='button.uploadVideo'/>">
		</div>
		 
		<div class="tabMenu">
			<ul>
				<li><span onclick="ListTable.url='/video/video_list.do';tabMenu(1,{videoType:'all'})" ><s:text name="video.videoCount"/>[<s:property value="result.videoCount"/>]</span></li>
				<li><span onclick="ListTable.url='/video/video_list.do';tabMenu(2,{videoType:'stateReject'})"><s:text name="video.rejectVideoCount"/>[<s:property value="result.rejectVideoCount"/>]</span></li>
				<li><span onclick="ListTable.url='/video/video_list.do';tabMenu(3,{videoType:'Company'})"><s:text name="video.companyVideoCount"/>[<s:property value="result.companyVideoCount"/>]</span></li>
				<li><span onclick="ListTable.url='/video/video_list.do';tabMenu(4,{videoType:'Product'})"><s:text name="video.productVideoCount"/>[<s:property value="result.productVideoCount"/>]</span></li>
				<li class="current"><span onclick="ListTable.url='/video/video_list.do';tabMenu(5,{videoType:'Menu'})"><s:text name="video.menuVideoCount"/>[<s:property value="result.menuVideoCount"/>]</span></li>
			</ul>
		</div>

		<div id="listTable">
			<s:include value="/page/misc/video_menu_list_inc.jsp"/>
		</div>

		<div id="uploadFormDialog" title='<s:text name="video.upload"/>'></div>
	</body>
</html>
