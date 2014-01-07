<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<style type="text/css">
a:link {
	color: #3366BD;
	text-decoration: none;
}

a:visited {
	text-decoration: none;
	color: #3366BD;
}

a:hover {
	text-decoration: underline;
	color: #F26616;
}

a:active {
	text-decoration: none;
	color: #3366BD;
}
</style>
<script type="text/javascript">
	function setVideo(videoId, videoImg, videoPlayPath, state){
		window.parent.$("#videoId").val(videoId);
		window.parent.$("#_tmpVideoImg").attr("src",videoImg);

		// 处理图片的超链接
		if(state == 20){
			if(window.parent.$("#_tmpVideoPlayPath").length > 0){
				window.parent.$("#_tmpVideoPlayPath").attr("href", videoPlayPath);
			}else{
				var objHref = $("<a/>")
				objHref.attr("id", "_tmpVideoPlayPath");
				objHref.attr("href", "javascript:playVideo('" + videoPlayPath + "');");
				window.parent.$("#_tmpVideoImg").wrap(objHref); 
			}
		}else{
			var objImg = window.parent.$("#_tmpVideoImg");
			$("#_tmpVideoPlayPath").replaceWith(objImg);
		}

		window.parent.$("#videoNotice").hide();
		window.parent.$("#selectVideoDialog").dialog('close');
	}
</script>
<div class="selectVideoList">
	<s:include value="/page/inc/pagination.jsp"/>
</div>
<table cellspacing="1" class="listTable" style="width:580px;">
	<tr>
		<th width="10%"><s:text name="video.fileName"/></th>
		<th width="55%"><s:text name="video.title"/></th>
		<th width="15%"><s:text name="video.state"/></th>
		<th width="10%"><s:text name="video.viewCount"/></th>
		<th width="10%"><s:text name="operate"/></th>
	</tr>
	<s:iterator value="result.listResult.list" id="video">
	<tr>
		<td class="img75">
			<s:if test="#video.state == 20">
				<a href="#position" onclick="playVideo('<s:property value="#video.playPath"/>');">
					<img src="<s:property value="#video.imgPath"/>" onload="Util.setImgWH(this, 75, 75)" onerror="$(this).attr('src', '/img/video_process.gif')"/>
				</a>
			</s:if>
			<s:else>
				<img src="<s:property value="#video.imgPath"/>" onload="Util.setImgWH(this, 75, 75)" onerror="$(this).attr('src', '/img/video_process.gif')"/>
			</s:else>
		</td>
		<td><s:property value="#video.title"/></td>
		<td>
			<s:property value="#video.stateName"/>
		</td>
		<td><s:property value="#video.viewCount"/></td>
		<td>
			<s:if test="#video.state > 10">
				<a href="#position" onclick="setVideo(<s:property value="#video.videoId"/>,'<s:property value="#video.imgPath"/>','<s:property value="#video.playPath"/>', <s:property value="#video.state"/>)"><s:text name="button.select"/></a>
			</s:if>
		</td>
	</tr>
	</s:iterator>
</table>
<div class="selectVideoList">
	<s:include value="/page/inc/pagination.jsp"/>
</div>
<%@ include file="/page/inc/image_error.jsp" %>