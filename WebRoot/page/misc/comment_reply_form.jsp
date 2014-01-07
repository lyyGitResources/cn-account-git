<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$(document).ready(function () {
		$("#replyForm").validate({
			//debug: true,
			errorPlacement: function(error, element) {
				element.before(error);
			},
			submitHandler: function(form){ 
				$(form).ajaxSubmit($.extend(ajaxSubmitOptions,{
					success: function (response, status){
						$("#dialogLoading").hide(); 
						if(response.tip == "addSuccess"){
							$('#replyFormDialog').dialog('close');
							ListTable.reload({'msg':response.msg},true);
						}else{
							$("#dialogMessage").show().html(response.msg);
						}
					},
					type: 'post',        		
				    dataType: 'json'  
				})); 
			},
			rules: {
				content: {required:true, maxlength:2000}
			},
			messages: {
				content: "<s:text name='commentReply.content.maxlength'/>"
			}
		});
	});
</script>
<form id="replyForm" name="replyForm" action="/comment/comment_reply_submit.htm" method="post">
	<textarea id="content" name="content" rows="4" cols="26" style="width:485px;" ></textarea>
	<input type="hidden" id="commentId" name="commentId" value='<s:property value="result.commentId"/>'/>
	<input type="submit" value="<s:text name='button.submit'/>" class="popButton">
</form>