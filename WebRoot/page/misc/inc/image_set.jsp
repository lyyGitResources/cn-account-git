<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.js"></script>
<script type="text/javascript">
	<s:if test="imgSrcTag != null && imgSrcTag != ''">
		<s:if test="imgSrcTag == 'txtUrl'">
			var fckIframeId = opener.$("iframe[id$='___Frame']").attr("id");
			opener.FCKeditorAPI.GetInstance(fckIframeId.replace("___Frame","")).InsertHtml("<img src='<%=Config.getString("img.link") %><s:property value='ImgPath'/>' align='absmiddle'>");
			// window.opener.TinyMCE_insertImage("<s:property value='ImgPath75'/>");
		</s:if>
		<s:else>
			window.opener.$("#<s:property value='imgSrcTag'/>").attr("src", "<s:property value='ImgPath75'/>");
			window.opener.$("#<s:property value='imgIdTag'/>").val("<s:property value='ImgId'/>");
			window.opener.$("#<s:property value='imgPathTag'/>").val("<s:property value='ImgPath'/>");
		</s:else>
	</s:if>
	<s:else>
		var imgtype = "<%= request.getParameter("imgType") %>";
		var imgidtag = "<%= request.getParameter("imgIdTag") %>";
		var imgpathtag = "<%= request.getParameter("imgPathTag") %>";
	
		if(imgtype = 2 && imgidtag == 6 && imgpathtag == 'face' ){
			window.opener.$("#selectImgDialog").load("/image/image_select.do?imgType=<%= request.getParameter("imgType") %>&imgSrcTag=&imgIdTag=" + imgidtag + "&imgPathTag=" + imgpathtag);
			window.opener.$("#selectImgDialog").dialog('option', 'title', '<s:text name="button.selectImage" />');
			window.opener.$("#selectImgDialog").dialog('open');
		} else if(imgtype = 3 && imgidtag == 5 && imgpathtag == 'proAddImg' ){
			window.opener.$("#selectImgDialog").load("/image/image_select.do?imgType=<%= request.getParameter("imgType") %>&imgSrcTag=&imgIdTag=" + imgidtag + "&imgPathTag=" + imgpathtag);
			window.opener.$("#selectImgDialog").dialog('option', 'title', '<s:text name="button.selectImage" />');
			window.opener.$("#selectImgDialog").dialog('open');
		} else{
			window.opener.location.href="/image/image_list.htm?imgType=<%= request.getParameter("imgType") %>";
		}
	</s:else>
 	window.close();
</script>
