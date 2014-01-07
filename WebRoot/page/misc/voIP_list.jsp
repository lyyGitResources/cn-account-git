<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>通话明细查询</title>
		<script type="text/javascript">
			$(function () {
				// 刷新查询当前月份详单
				$("[name='hideMonth']").hide();
				$("#year").val(<s:property value="year"/>);
				$("#month").val(<s:property value="month"/>);
				$("#userId").val(-1);

				$("#year").change(function(){
					if($("#year").val() == <s:property value="year"/>){
						$("[name='hideMonth']").hide();
						$("#month").val(<s:property value="month"/>);
					}else{
						$("[name='hideMonth']").show();
					}
				});
				
				$("#searchForm").validate({
					submitHandler: function(){ 
						ListTable.reload({
							userId: $("#userId").val(),
							year: $("#year").val(),
							month: $("#month").val(),
							pageNo:1
						}, true);
					}
				});
				
				$("#voIPForm").validate({
					errorPlacement: function(error, element) {
						element.parent().next("td").after(error);
					},
					submitHandler: function(form){
						$(form).ajaxSubmit({ 
						    success: function(response){
						    	alert(response.msg);
						    },
						    dataType: 'json'
						});
					},
					rules: {
						telephone: {digits:true, rangelength: [10, 15]}
					},
					messages: {
						telephone: '请输入一个有效的电话/手机号码'
					}
				});
			});
		</script>
	</head>
	<body>
		<div style="float:right;">
			余额：<font class="red"><s:property value="result.balance"/></font>元
		</div>
		<div style="clear:both;"></div>
		<form id="searchForm" class="searchForm">
			<s:if test="result.userList != null && result.userList.size > 0">
				<td>
					<select id="userId">
						<option value="-1">所有帐号</option>
						<option value="0">管理员</option>
						<s:iterator value="result.userList" id="user">
							<option value="<s:property value='#user.userId' />">
								<s:property value='#user.email' />
							</option>
						</s:iterator>
					</select>
				</td>
			</s:if>
			<select id="year">
				<option value="<s:property value="year - 2"/>"><s:property value="year - 2"/>年</option>
				<option value="<s:property value="year - 1"/>"><s:property value="year - 1"/>年</option>
				<option selected="selected" value="<s:property value="year"/>"><s:property value="year"/>年</option>
			</select>
			<select id="month">
				<s:iterator value="new int[]{1,2,3,4,5,6,7,8,9,10,11,12}" id="month">
					<s:if test="month >= #month">
						<option value="<s:property value="#month"/>"><s:property value="#month"/>月</option>
					</s:if>
					<s:else>
						<option value="<s:property value="#month"/>" style="display:none;" name="hideMonth"><s:property value="#month"/>月</option>
					</s:else>
				</s:iterator>
			</select>
			<input type="submit" value="查询"/>
		</form>
		
		<div id="listTable">
			<s:include value="/page/misc/voIP_list_inc.jsp"/>
		</div>
		
		<div class="TelBox">
			<table cellspacing="0" cellpadding="3" border="0" width="90%">
				<tbody>
					<tr>
						<td align="left" width="50%">
							修改绑定的电话号码
						</td>
						<td align="left" width="50%">
							充值请联系客服 <b>Kitty</b>
						</td>
					</tr>
					<tr>
						<td align="left">
							<form id="voIPForm" method="post" action="/voIP/voIP_reBinding.do">
								<strong>号码：</strong><input type="text" value="<s:property value="result.telephone"/>" maxlength="20" name="telephone"/>
								<input type="submit" class="button10" value="确定"/>
							</form>
						</td>
						<td align="left">
							<strong>电话：</strong> 0574-27901060        <strong>邮箱：</strong> <a target="blank" href="mailto:service10@hi.cc">service10@hi.cc</a>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</body>
</html>