<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	/**
	 * 删除确认
	 * @param deleteUrl 删除地址
	 * @param message 提示信息
	 */
	function deleteConfirm(deleteUrl, message){
		if(message == null){
			message = "<s:text name='deleteConfirm' />";
		}
		$('#dialogMessage').show().html("<br />"+message);
		$("#dialogLoading").hide();
		$('#dialog').dialog('option', 'buttons', { 
			"<s:text name='button.cancel' />" : function() {
				$(this).next().hide();	 	// 隐藏按钮
				$(this).dialog('close');
				$("#dialogLoading").show();	// 显示正在加载图标
			},
			"<s:text name='button.confirm' />": function() {
				document.location.href = deleteUrl;
			}
		});
		$("#dialog").dialog('open');
	}
</script>
<div id="dialog" style="display: none;" title="<s:text name="systemTips" />">
	<p id="dialogLoading">
		<img src="/img/loading1.gif" />
		<br />
		<br />
		Loading...
	</p>
	<p id="dialogMessage"></p>
</div>
