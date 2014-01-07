<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>目录建议</title>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.form.js"></script>
		<script type="text/javascript">
			jQuery.extend(jQuery.validator.methods, {
				// 不是所有的字段为空
				notAllEmpty: function(value, element, param) {
					var result = false;
					if($.trim(value) != ""){
						result = true;
					}else{
						$(param).each(function(){
							if($.trim($(this).val()) != ""){
								result = true;
							}
						});
					}
					return result;
				}
			});

			$(function (){
				$("#suggestForm").validateForm({
					submitHandler: function(form){
						$(form).ajaxSubmit({ 
						    success: function(response){
						    	alert(response.msg);
						    },
						    type: 'post',
						    dataType: 'json',
						    resetForm: true 
						});
					},
					rules: {
						content1: {maxlength:3000, notAllEmpty: "#content3,#content2"},
						content2: {maxlength:3000},
						content3: {maxlength:3000}
					},
					messages: {
						content1: '<s:text name="categorySuggest.content.required" />',
						content2: '<s:text name="categorySuggest.content.required" />',
						content3: '<s:text name="categorySuggest.content.required" />'

					}
				});
			});
		</script>
	</head>
	<body>
		<div class="main">
			<div style="color: rgb(255, 103, 2); font-size: 14px; margin-top: 10px; margin-bottom: 10px;">欢迎帮助我们改进目录列表！</div>
			<p>我们将不断更新目录 以满足您的需求，如果找不到合适的目录，请填写以下表单告诉我们。</p>
			<form id="suggestForm" method="post" action="/user/category_suggest_submit.do">
				<div class="formBox">
					<b>您要推广或购买的产品：</b>
					<div>
						<textarea style="width: 690px; height: 100px;" name="content1" id="content1"></textarea>
						<div id="content1Tip"></div>
					</div>
					<b>您期望的目录名称：</b>
					<div>
						<textarea style="width: 690px; height: 100px;" name="content2" id="content2"></textarea>
						<div id="content2Tip"></div>
					</div>
					<b>补充说明：</b>
					<div>
						<textarea style="width: 690px; height: 100px;" name="content3" id="content3"></textarea>
						<div id="content3Tip"></div>
					</div>
				</div> 
				<div style="text-align: center; padding-top: 10px;">
					<input type="submit" class="submit3" value="提交审请" />
				</div>
			</form>
		</div>
	</body>
</html>