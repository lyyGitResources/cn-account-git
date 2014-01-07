<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			<s:text name="videoGroup.listTitle"/>
			<span class="gray">
				<s:text name="videoGroup.exist">
					<s:param><s:property value="result.groupList.size"/></s:param>
				</s:text>
			</span>
		</title>
		<script type="text/javascript">
			$(document).ready(function (){
				$("#formDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 150,
					width: 560,
					modal: true,
					close: function(){
						$("#formDialog").empty();
					}
				});
			});
			
			function showFormDialog (url,title) {
				$("#formDialog").load(url,{random:Math.random()});
				$("#formDialog").dialog('option', 'title', title);
				$("#formDialog").dialog('open');
			}
		</script>
	</head>
	<body>	<div class="buttonLeft">
				<input type="button" value="<s:text name='button.addVideoGroup'/>" onclick="javaScript:showFormDialog('/video/video_group_add.do','<s:text name="videoGroup.addTitle"/>')"/>
			</div>
			<div id="listTable">
				<s:include value="/page/misc/video_group_list_inc.jsp"/>
			</div>
			<div id="formDialog"></div>

	</body>
</html>
