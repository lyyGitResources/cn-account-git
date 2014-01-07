<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$(document).ready(function () {
		$("#videoEditForm").validate({
			rules: {
				title: {required:true, maxlength:120}
			},
			messages: {
				title: "<s:text name='video.title.maxlength'/>"
			}
		});
	});
</script>
<s:include value="/page/inc/image_error.jsp"></s:include>
<form id="videoEditForm" name="videoEditForm" action="/video/video_edit_submit.do" method="post">
	<table class="formTable" style="width:520px;">
		<tr>
			<th><s:text name="video.fileName"/>：</th>
			<td class="img75">
				<s:if test="result.video.state == 20">
					<a href="#position" onclick="javascript:playVideo('<s:property value="result.video.playPath"/>');">
						<img src="<s:property value="result.video.imgPath"/>" onload="Util.setImgWH(this, 75, 75)" onerror="$(this).attr('src', '/img/video_process.gif')"/>
					</a>
				</s:if>
				<s:else>
					<img src="<s:property value="result.video.imgPath"/>" onload="Util.setImgWH(this, 75, 75)" onerror="$(this).attr('src', '/img/video_process.gif')"/>
				</s:else>
			</td>
		</tr>
		<tr>
			<th><span class="red">*</span>&nbsp;<s:text name="video.title"/>:</th>
			<td><input type="text" id="title" name="title" value='<s:property value="result.video.title"/>' style="width:220px;"/></td>
		</tr>
		<tr>
			<th><s:text name="video.state"/>：</th>
			<td>
				 <s:property value="result.video.stateName"/>
			</td>
		</tr>
		<tr>
			<th><s:text name="video.groupId"/>：</th>
			<td>
				<select name="groupId" id="groupId">
					<option value="0" selected>请选择</option>
					<s:iterator value="result.groupList" id="group">
						<option value="<s:property value='#group.groupId'/>" <s:if test="#group.groupId == result.video.groupId">selected</s:if> ><s:property value='#group.groupName'/></option>
					</s:iterator>
				</select>
			</td>
		</tr>
		<tr>
			<th><s:text name="video.viewCount"/>：</th>
			<td><s:property value="result.video.viewCount"/></td>
		</tr>
		<tr>
			<th><s:text name="createTime"/>：</th>
			<td><s:property value="result.video.createTime"/></td>
		</tr>
		<input type="hidden" name="oldGroupId" value='<s:property value="result.video.groupId"/>'/>
		<input type="hidden" name="state" value='<s:property value="result.video.state"/>'/>
		<input type="hidden" name="comId" value='<s:property value="result.video.comId"/>'/>
		<input type="hidden" name="videoId" value='<s:property value="result.video.videoId"/>'/>
		<input type="hidden" name="videoType" value='<s:property value="result.video.videoType"/>'/>
	</table>
	<div class="buttonCenter" style="width:500px;">
	<input type="submit" value="<s:text name='button.submit'/>" >
	</div>
</form>