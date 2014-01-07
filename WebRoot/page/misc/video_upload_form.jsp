<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.hisupplier.commons.Global" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<style>
	.videoSelect  {
		line-height:40px;
		padding-left:5px;
		text-align:left;
		font:12px/15px Arial,"宋体";
	}
	#up_btn {padding-left: 100px;}
	label { display: block;margin-bottom: 10px; }
	.form {
		width:311px;
		margin:0 auto;
		padding-top: 40px;
		text-align: center;
	}
</style>
<script type="text/javascript" src="/js/jquery/jquery-swfobject-1.1.1.js"></script>
<script type="text/javascript">
	function insert(vid, title){
		var params = {comId:<s:property value='loginUser.comId'/>};
		params.title = title;
		params.asdId = vid
	
		$.ajax({
		   	type: "post",
		    url: "/video/video_upload_submit.do",
    		timeout: 20000,
		    data: params,
		    dataType: "json",
		    success:function(response){
		    	if(response == null){
		    		alert("上传视频失败！");
		    		return;
		    	}
		    	
		    	if(response.tip != "addSuccess"){
		    		alert(response.msg);
		    		return;
		    	}
		    	
		    	alert(response.msg);
				
				// 具体的选择页面
		    	if(window.parent.$("#videoId").length > 0){
		    		window.parent.$("#videoId").val(response.model.videoId);
		    		
		    		var objImg = window.parent.$("#_tmpVideoImg");
					$("#_tmpVideoPlayPath").replaceWith(objImg);
					
		    		window.parent.$("#_tmpVideoImg").attr("src", "/img/video_process.gif");
		    		window.parent.$("#videoNotice").hide();
		    	}else{
		    		window.parent.location.reload();
		    	}
		    	
		    	$('#uploadVideoDialog').dialog('close');
		    	
		    }
		});	
	}
</script>
<s:if test="result.allow == 'false'">
	<div class="pageTips" style="width:540px;">
		<s:text name="video.full">
			<s:param><s:property value="result.videoCount"/></s:param>
		</s:text>
	</div>
</s:if>
<s:elseif test="result.allow == 'true'">
	<div class="form">
		<label for="video_file">文件：<input type="text" id="video_file" readonly="readonly" ></label>
		<label for="video_name">标题：<input type="text" id="video_name" name="videoName"></label>
		<p id="up_btn">上传视频</p>
		<script>
		$("#up_btn").flash({
			swf: '/js/asdtv.swf?callback=jQuery.asdtv.successful',
			width: 200,
			height: 40,
			allowScriptAccess: "always",
			wmode: 'transparent'
		});
		jQuery.asdtv = {
			getTitle: function() {
				return $("#video_name").val();
			},
			putTitle: function(title) {
				$("#video_file,#video_name").val(title);
			},
			successful: function(vid) {
				insert(vid, $("#video_name").val());
			},
			start: function(videoName) {
			}
		}
		</script>
		<%-- 
		<object codeBase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,28,0" height="260" width="320">
			<param name="movie" value="http://union.bokecc.com/flash/loader.swf?userID=191728&type=normal&address=%27http%3A//www.hisupplier.com/%27"/>
			<param name="allowScriptAccess" value="always"/>
			<embed src="http://union.bokecc.com/flash/loader.swf?userID=191728&type=normal&address=%27http%3A//www.hisupplier.com/%27" 
				pluginspage="http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash" 
				type="application/x-shockwave-flash" allowScriptAccess="always" width="320" height="260"/>
		</object>
		 --%>
		<div class="videoSelect">
			<p><span style="color: red">*</span> 我们支持的格式：</p>
			<p style="padding-left: 12px;">avi、flv、mp4、mpg、mov、asf、3gp、f4v、wmv、rmvb、rm、ts</p>
			<br />
			<p><span style="color: red">*</span> 上传视频大小不能超过 <span style="color: red">400M</span></p> 
			<%-- CC
			flv、wmv、wm、asf、asx、rm、rmvb、mpg、mpeg
			mpe、vod、dat、mov、mp4、avi、3gp、ram
			 --%>
		</div>
	</div>
</s:elseif>
