<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.util.WebUtil"%>
<script type="text/javascript">
	$(document).ready(function () {	
		//图片预加载处理
		$("img.imgLoadError").error(function(){
			var width = $(this).parent().css("width");
			if(width == "100px"){
				$(this).attr("src", "<%=WebUtil.getDefaultImgPath(100) %>");
			}else if(width == "240px"){
				$(this).attr("src", "<%=WebUtil.getDefaultImgPath(240) %>");
			}else{
				$(this).attr("src", "<%=WebUtil.getDefaultImgPath(75) %>");
			}
		});
	});
</script>
