<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$(function (){
		$("#uploadVideoDialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 350,
			width: 580,
			modal: true,
			close: function(){
				$("#uploadVideoDialog").empty();
			}
		});
	});
	function uploadVideo(){
		$("#uploadVideoDialog").html(AJAX_LOADING_CODE);
		$("#uploadVideoDialog").load('/video/video_upload.do');
		$("#uploadVideoDialog").dialog('open');
	}
</script>
<s:i18n name="com.hisupplier.cn.account.misc.package_zh">
	<div id="uploadVideoDialog" title='<s:text name="video.upload"/>'></div>
</s:i18n>
