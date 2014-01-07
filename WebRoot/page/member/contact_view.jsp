<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:if test="loginUser.admin"><s:text name="user.contactTitle" /></s:if><s:else><s:text name="user.myUserTitle" /></s:else></title>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js"></script>
		<script type="text/javascript" src="/js/user.js"></script>
		<script type="text/javascript">
			$(function(){
				$("#adminLogDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 200,
					width: 360,
					modal: true,
					close: function(){
						$("#adminLogDialog").empty();
					}
				});
				
				$("#adminLog").click(function(){
					$("#adminLogDialog").load("/basic/admin_log.do?tableName=Users&tableId=<s:property value='result.more.userId' />",{random: Math.random()});
					$("#adminLogDialog").dialog('option', 'title', '<s:text name="auditState.rejectRemark"/>');
					$("#adminLogDialog").dialog('open');
				});

				})
			function delectCheck(moreId){
				deleteConfirm("/member/contact_more_delete.htm?userId="+moreId);
			}
			function openWindow(url) {
				var param = "height=700px,width=800px,left=250px,top=150px,resizable=yes,scrollbars=yes,status=no,toolbar=no,menubar=no,location=no";
				window.open(url, "", param);
			}
		</script>
	</head>
	<body>
	<s:if test="result.moreId !=0 && result.more.state == 10">
		<div style="color:red;padding:10px 20px;">
				<ul>
						<li>
						<span>第二联系人信息审核未通过，请根据<a id='adminLog' href="javascript:void(0);">未通过原因</a>修改</span>
					</li>
				</ul>
			</div>
	</s:if>
	<div id="contactDiv">
		<h2>
			<s:text name="input.notice" />
		</h2>
			<s:token />
			<table class="formTable">
				<tr>
					<th><s:text name="modifyTime" />：</th>
					<td><s:property value="result.user.modifyTime" /></td>
				</tr>
				<tr>
					<th><s:text name="user.headImg" />：</th>
					<td>
						<div class="imgBox75"><img src="<s:property value="result.user.headImgSrc" />" /></div>
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="user.contact" />：</th>
					<td>
						<s:property value='result.user.contact' />
						<s:property value='result.user.titleShow' />
						<div id="contactTip"></div>
					</td>
				</tr>
				<tr>
					<th>联系地址：</th>
					<td>
					<s:property value="result.user.provinceShow"/>
					<s:property value="result.user.cityShow"/>
					<s:property value="result.user.townShow"/>
					<s:property value='result.user.street' />
					</td>
				</tr>
				<tr>
					<th>
						&nbsp;电子地图坐标：
					</th>
					<td>
						<span id="googleLocalSpan"><s:property value='result.user.googleLocal' /></span>
						<a style="text-decoration: none;" href="javascript:openWindow('/page/member/inc/select_map.jsp')">
							<img border="0" align="absmiddle" src="/img/map.gif"/>
						</a>
					</td>
				</tr>
				<tr>
					<th><s:text name="user.zip" />：</th>
					<td><s:property value='result.user.zip' /></td>
				</tr>
				<tr>
					<th><s:text name="user.department" />：</th>
					<td><s:property value='result.user.department' /></td>
				</tr>
				<tr>
					<th><s:text name="user.job" />：</th>
					<td>
						<s:property value='result.user.job' /></td>
				</tr>
				<tr>
					<th><span class="red">*</span><s:text name="email" />：</th>
					<td>
						<s:property value='result.user.email' />
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span><s:text name="user.tel" />：</th>
					<td>
						<s:property value='result.user.tel1' />
						&nbsp;&nbsp;-&nbsp;&nbsp;
						<s:property value='result.user.tel2' />
					</td>
				</tr>
				<tr>
					<th><s:text name="user.fax" />：</th>
					<td>
						<s:property value='result.user.fax1' />
						&nbsp;&nbsp;-&nbsp;&nbsp;
						<s:property value='result.user.fax2' />
					</td>
				</tr>
				<tr>
					<th><s:text name="user.mobile" />：</th>
					<td>
						<s:property value='result.user.mobile' />
						<%--<input id="bindingMobile" type="button" value="绑定号码"/>--%>
					</td>
				</tr>
				<s:if test="loginUser.admin && loginUser.memberType == 2">
					<tr>
						<th><s:text name="user.sms" />：</th>
						<td>
							<s:if test="result.user.sms == true">
								<s:text name="yes" />
								
							</s:if>
							<s:else>
								<s:text name="no" />
							</s:else>
							
						</td>
					</tr>
				</s:if>
				<s:else>
					<input type="hidden" id="sms" name="sms" value="<s:property value="user.sms"/>" />
				</s:else>
				<tr>
					<th><s:text name="user.showMobile" />：</th>
					<td>
						<s:if test="result.user.showMobile == true">
							<s:text name="yes" />
						</s:if>
						<s:else>
							<s:text name="no" />
						</s:else>
					</td>
				</tr>
				<tr>
					<th><s:text name="user.qq" />：</th>
					<td>
						<table>
						<s:iterator value="result.user.talks" id="talk">
						<tr>
							<td style="width:50px; text-align: right;"><s:property value="#talk.name" /></td>
							<td><s:property value="#talk.review" escape="false" /></td>
						</tr>
						</s:iterator>
						</table>
					</td>
				</tr>
				
				<%--
				<tr>
					<th><s:text name="user.msn" />：</th>
					<td>
						<s:property value='result.user.msn' />
					</td>
				</tr>
				 --%>
				<%-- 
				<tr>
					<th><s:text name="user.skype" />：</th>
					<td>
						<input type="text" name="skype" style="width: 160px;margin-right: 5px;" value="<s:property value='result.user.skype' />" />
					</td>
				</tr>
				--%>
				<tr>
					<th><s:text name="user.preLoginTime" />：</th>
					<td>
						<s:property value="result.user.preLoginTime" />
					</td>
				</tr>
				<tr>
					<th><s:text name="user.preLoginIP" />：</th>
					<td>
						<s:property value="result.user.preLoginIP" />
					</td>
				</tr>
				<tr>
					<th><s:text name="user.loginTimes" />：</th>
					<td>
						<s:property value="result.user.loginTimes" />
					</td>
				</tr>
			</table>
			<div class="buttonLeft" style="margin-top: 0">
				<s:if test="result.user.admin==true">
					<input type="button" value="修改联系人信息" onclick="location.href='/member/contact_edit.htm'" />
					<s:if test="result.moreId ==0">
					<input type="button" onclick="location.href='/member/contact_more.htm?userId=<s:property value='result.user.userId' />'" value="<s:text name="user.moreContactAdd" />" />
					</s:if>
				</s:if>
				<s:else>
					<input type="button" value="修改我的信息" onclick="location.href='/member/child_user_edit.htm?userId=${result.user.userId}'" />
				</s:else>
			</div>
			<s:if test="result.moreId !=0">
			<table class="formTable">
				<tr>
					<th align="right"><span class="red">*</span>姓名：</th>
					<td width="81%">
						<s:property value="result.more.contact"/>&nbsp;<s:property value="result.more.titleShow" />
					</td>
				</tr>
				<tr>
					<th align="right">部门：</th>
					<td>
						<div style="float:left "><s:property value="result.more.department"/> </div>
					</td>
				</tr>
				<tr>
					<th align="right">职务：</th>
					<td>
						<div style="float:left "><s:property value="result.more.job" /></div>
					</td>
				</tr>
				<tr>
					<th align="right"><span class="red">*</span>电话：</th>
					<td>
						<s:property value="result.more.tel"/>
					</td>
				</tr>
				<tr>
					<th align="right">传真：</th>
					<td><s:property value="result.more.fax"/></td>
				</tr>
				<tr>
					<th align="right">手机：</th>
					<td><s:property value="result.more.mobile"/></td>
				</tr>
				<tr>
					<th align="right"><span class="red">*</span>邮箱：</th>
					<td><s:property value="result.more.email"/></td>
				</tr>
				<tr>
					<th align="right"><span class="red">*</span>地址：</th>
					<td><s:property value="result.more.street"/></td>
				</tr>
				<tr>
					<th align="right">邮编：</th>
					<td><s:property value="result.more.zip"/></td>
				</tr>
			</table>
			<s:if test="result.user.admin == true">
				<div class="buttonLeft">
				<s:if test="result.moreId == 0">
					<input type="button" onclick="location.href='/member/contact_more.htm?userId=<s:property value='result.user.userId' />'" value="<s:text name="user.moreContactAdd" />" />
				</s:if>
				<s:else>
					<input type="button" onclick="location.href='/member/contact_more.htm?userId=<s:property value='result.user.userId' />'" value="<s:text name="user.moreContactModify" />" />
					<input type="button" onclick="delectCheck(<s:property value='result.moreId' />)" value="<s:text name="user.moreContactRemove" />" />
				</s:else>
				</div>
			</s:if>
			</s:if>
	</div> 
	<div id="adminLogDialog"></div>
		<%@ include file="/page/inc/image_error.jsp" %>
	</body>
</html>