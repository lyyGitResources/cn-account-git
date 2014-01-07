<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$(function (){
		$("#uploadVideoDialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 350,
			width: 590,
			modal: true,
			close: function(){
				$("#uploadVideoDialog").empty();
			}
		});
		
		$("#selectVideoDialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 600,
			width: 620,
			modal: true,
			close: function(){
				$("#selectVideoDialog").empty();
			}
		});
	});
	function uploadVideo(){
		$("#uploadVideoDialog").html(AJAX_LOADING_CODE);
		$("#uploadVideoDialog").load('/video/video_upload.do');
		$("#uploadVideoDialog").dialog('open');
	}
	
	function selectVideo(){
		$("#selectVideoDialog").html(AJAX_LOADING_CODE);
		$("#selectVideoDialog").load('/video/video_select.do');
		$("#selectVideoDialog").dialog('open');
	}
	
	function playVideo(playPath) {
		window.open(playPath, "", "height=400px,width=500px,top=300px,left=400px,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no, status=no");
	}
	
	function removeVideo() {
		$("#videoNotice").hide();
		$("#videoId").val(0);
		$("#_tmpVideoPlayPath").replaceWith($("#_tmpVideoImg"));
		$("#_tmpVideoImg").attr("src","<%=Config.getString("video.default") %>");
	}
	
</script>
<div style="margin-bottom:10px;">
	<div class="imgBox75">
		<s:if test="#videoState == 20">
			<a href="javascript:playVideo('<s:property value="#playPath"/>');" id="_tmpVideoPlayPath">
				<img src="<s:property value="#videoImgPath"/>" onload="Util.setImgWH(this, 75, 75)" onerror="$(this).attr('src', '/img/video_process.gif')" id="_tmpVideoImg" />
			</a>
		</s:if>
		<s:elseif test="#videoState > 0">
			<img src="<s:property value="#videoImgPath"/>" onload="Util.setImgWH(this, 75, 75)" onerror="$(this).attr('src', '/img/video_process.gif')" id="_tmpVideoImg"/>
		</s:elseif>
		<s:else>
			<img src="<%=Config.getString("video.default") %>" onload="Util.setImgWH(this, 75, 75)" id="_tmpVideoImg"/>
		</s:else><br/>
		
		
	</div>
	<div class="fieldTips" style="float:left;margin-left:85px; margin-top:-80px;*margin-left:85px !important; *margin-left:42px; line-height:16px;">
		<b>视频格式</b><br />
		1. 支持flv、wmv、wm、asf、asx、rm、rmvb、mpg、<br />
		mpeg、mpe、vod、dat、mov、mp4、avi、3gp、ram格式<br />
		2. 不超过400M <br />
		3.上传的视频标题必须含有中文。
	</div>
		<s:if test="#videoState == 15"><p style="color: red">视频未审核，无法播放，请等待本公司人员对其审核或者重新选择已通过审核的视频</p></s:if>
		
</div>
<s:if test="#videoState == 10">
<div id="videoNotice" style="color:red">
	<s:text name="video.reject" />
</div>
</s:if>
<input type="button" onclick="uploadVideo();" value="<s:text name='button.uploadVideo'/>" />
<input type="button" onclick="selectVideo();" value="<s:text name='button.selectVideo'/>" />
<input type="button" onclick="removeVideo();" value="<s:text name='button.remove'/>" />

<s:i18n name="com.hisupplier.cn.account.misc.package_zh">
	<div id="uploadVideoDialog" title='<s:text name="video.upload"/>'></div>
	<div id="selectVideoDialog" title='<s:text name="video.selectTitle"/>'></div>
</s:i18n>
