<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="vote.viewTitle"/></title>
		<script type="text/javascript">
		
			//$.validator.addMethod("tmp", function(value) {return value == "tmp";}, '<s:text name="voteComment.content.maxlength"/>');

 			$(function (){
				$("#commentForm").validate({
					//debug: true,
					errorPlacement: function(error, element) {
						element.before(error);
					},
					submitHandler: function(form){ 
						$(form).ajaxSubmit($.extend(ajaxSubmitOptions,{
							success: function (response, status){
								$("#dialogLoading").hide(); 
								if(response.tip == "addSuccess"){
									ListTable.reload({"msg": response.msg},true);
								}else{
									$("#dialogMessage").show().html(response.msg);
								}
							},
							resetForm: true
						})); 
					},
					rules: {
						content: {required:true, maxlength:3000}
					},
					messages: {
						content: "<s:text name='voteComment.content.maxlength'/>"
					}
				});
			});

			function checkOption(optionId){
				var voteType = <s:property value="result.vote.voteType"/>;
				var itemIds = document.voteForm.optionId;
				var tip="";
				var logList;
				if(voteType == 1){
					<s:if test="result.logListSize > 0">
						logList = <s:property value="result.logListSize"/>;
					</s:if>
					tip = '<s:text name="vote.radio.tip"/>';
				}else if(voteType == 2){
					tip = '<s:text name="vote.checkBox.tip"/>';
					<s:iterator value="result.vote.optionList" id="voteOption" status="st">
						if(<s:property value="#voteOption.optionId"/> == optionId){
							logList = <s:property value="#voteOption.logListSize"/>;
						}
					</s:iterator>
				}
				if(logList > 0){
					alert(tip);
					for(var i=0;i<itemIds.length;i++){
						if(itemIds[i].value == optionId){
							itemIds[i].checked = false;
							break;
						}
					}
					return false;
				}
				return true;
			}
			
			function voteSubmit(){
			
				<s:if test="!result.vote.enableVote">
					alert('<s:text name="vote.timeout"/>');
					return false;
				</s:if>
				var form = document.voteForm;
				var voteType = <s:property value="result.vote.voteType"/>;
				if(voteType == 1){
			        if(Util.isChecked("optionId")){
			        	form.action = "/vote/vote_option_add.htm";
			        	form.submit();
			        }else{
			        	window.alert('<s:text name="vote.noselect.option"/>');
			        	return false;
			        }
				}else if(voteType == 2){
					if(Util.isChecked("optionId")){
						form.action = "/vote/vote_option_add.htm";
						form.submit();
					}else{
						window.alert('<s:text name="vote.noselect.option"/>');
						return false;
					}
				}
			}
			
		</script>
	</head>
	<body>
		<div class="voteBox">
			<div class="voteList2">
				<ul>
				  <li><s:text name="vote.createTime"/>：<s:property value="result.vote.createTime"/></li>
				  <li class="li"><s:text name="vote.count"/>：<s:property value="result.vote.voteCount"/></li>
				  <li><s:text name="vote.endTime"/>：<s:property value="result.vote.endTime"/></li>
				</ul>
			</div>
			
			<form id="voteForm" name="voteForm" method="post" action="">
				<table cellspacing="0" class="voteTable">
					<tr>
						<th colspan="4"><s:property value="result.vote.title"/></th>
					</tr>
					<tr>
						<td colspan="4"><s:property value="result.vote.content"/></td>
					</tr>
					<s:iterator value="result.vote.optionList" id="voteOption" status="st">
						<s:if test="result.vote.voteType == 1">
							<tr>
								<td style="width:30px; text-align:right;"><input type="radio" id="optionId<s:property value="#voteOption.optionId"/>" name="optionId" value="<s:property value="#voteOption.optionId"/>" onclick="checkOption(<s:property value="#voteOption.optionId"/>);" <s:if test="!result.vote.enableVote">disabled</s:if>/></td>
								<td class="name"><s:property value="#voteOption.content"/>：</td>
								<td class="article"><div class="article<s:property value="#st.count"/>" style="width:<s:property value="#voteOption.percent"/>"></div></td>
								<td style="width:60px;"><s:property value="#voteOption.voteCount"/>(<s:property value="#voteOption.percent"/>)</td>
							</tr>
						</s:if>
						<s:elseif test="result.vote.voteType == 2">
							<tr>
								<td><input type="checkbox" id="optionId<s:property value="#voteOption.optionId"/>" name="optionId" value="<s:property value="#voteOption.optionId"/>" onclick="checkOption(<s:property value="#voteOption.optionId"/>);" <s:if test="!result.vote.enableVote">disabled</s:if>/></td>
								<td class="name"><s:property value="#voteOption.content"/>：</td>
								<td class="article"><div class="article<s:property value="#st.count"/>" style="width:<s:property value="#voteOption.percent"/>"></div></td>
								<td><s:property value="#voteOption.voteCount"/>(<s:property value="#voteOption.percent"/>)</td>
							</tr>
						</s:elseif>
					</s:iterator>
					<input type="hidden" name="voteId" value="<s:property value="result.vote.voteId"/>"/>
					<input type="hidden" name="voteType" value="<s:property value="result.vote.voteType"/>"/>
				</table>
				<div class="voteButton"><a href="#position" onclick="voteSubmit()"><img src="/img/vote.gif" width="116" height="31"/></a></div>
			</form>
			
			<div class="voteCommentTable">
				<form action="/vote/vote_comment_add.htm" id="commentForm" name="commentForm" method="post">
					<div class="title"><s:text name="vote.say"/></div>
					<textarea name="content" rows="4" cols="70" style="width:420px"></textarea><br />
					<input type="hidden" name="voteId" value="<s:property value="result.vote.voteId"/>"/>
					<input type="hidden" name="pageNo" id="pageNo" value="<s:property value='result.listResult.page.pageNo'/>">
					<div class="published">
						<input type="submit" value='<s:text name="button.deliver"/>'/>
					</div>
				</form>
			</div>
			<div class="voteCommentList" id="listTable">	
				<s:include value="/page/misc/vote_comment_list_inc.jsp"/>
			</div>	
		</div>
	</body>
</html>
