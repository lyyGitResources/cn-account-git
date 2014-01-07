<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="comment.listTitle"/></title>
		<script type="text/javascript">
			$(function (){
				$("#replyFormDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 220,
					width: 520,
					modal: true,
					close: function(){
						$("#replyFormDialog").empty();
					}
				});
			});
			function showReplyFormDialog (commentId,type) {
				$("#replyFormDialog").load('/comment/comment_reply.do?commentId='+commentId);
				$("#replyFormDialog").dialog('open');
			}
		</script>
	</head>
	<body>
		<div class="pageTips">
			提示：海商网有权利删除垃圾信息，该操作将不再通知发布用户。
		</div>
		<div class="commentTabMenu">
			<ul>
				<li class="current"><span onclick="commentTabMenu(1,{commentType:'company'})"><s:text name='comment.company'/></span></li>
				<li><span onclick="commentTabMenu(2,{commentType:'product'})"><s:text name='comment.product'/></span></li>
			</ul>
		</div>
		<div id="listTable">
			<s:include value="/page/misc/comment_list_inc.jsp"/>
		</div>
		<div id="replyFormDialog" title='<s:text name="commentReply.title"/>'></div>
	</body>
</html>
