<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:if test="result.user.userId > 0">
	<s:set name="pageTitle" value="%{getText('user.editTitle')}" />
	<s:set name="formAction" value="'/member/user_edit_submit.htm'" />
	<s:set name="showGetButton" value="'false'" />
</s:if>
<s:else>
	<s:set name="pageTitle" value="%{getText('user.addTitle')}"></s:set>
	<s:set name="formAction" value="'/member/user_add_submit.htm'" />
	<s:set name="showGetButton" value="'true'" />
</s:else>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>查看子账号</title>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js"></script>
		<script type="text/javascript" src="/js/user.js"></script>
	</head>
	<body>
		<div class="buttonLeft">
			<input value="修改子账号" onclick="window.location.href='/member/user_edit.htm?userId=${result.user.userId}'" />
		</div>
			<table class="formTable">
				<tr>
					<th>头像：</th>
					<td>
					<div class="imgBox75">
							<img src="${result.user.headImgSrc}" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)"/>
						</div>
					</td>
				</tr>
				<tr>
					<th>姓名：</th>
					<td>${result.user.contact } <s:property value="result.user.titleShow" /> </td>
				</tr>
				<tr>
					<th>密码：</th>
					<td>${result.user.passwd }</td>
				</tr>
				<tr>
					<th>联系地址：</th>
					<td>
						<s:property value="result.user.provinceShow"/>
						<s:property value="result.user.cityShow"/>
						<s:property value="result.user.townShow"/>
						<s:property value="result.user.street"/>
					</td>
				</tr>
				<tr>
					<th>邮编：</th>
					<td>${result.user.zip }</td>
				</tr>
				<tr>
					<th>部门：</th>
					<td>${result.user.department }</td>
				</tr>
				<tr>
					<th>职务：</th>
					<td>${result.user.job }</td>
				</tr>
				<tr>
					<th>电子邮箱：</th>
					<td>${result.user.email }</td>
				</tr>
				<tr>
					<th>电话：</th>
					<td>${result.user.tel }</td>
				</tr>
				<tr>
					<th>传真：</th>
					<td>${result.user.fax }</td>
				</tr>
				<tr>
					<th>移动电话：</th>
					<td>${result.user.mobile }</td>
				</tr>
			
				<tr>
					<th>公开联系方式：</th>
					<td><s:if test="result.user.showMobile == false">否</s:if><s:else>是</s:else></td>
				</tr>
				<tr>
					<th>QQ：</th>
					<td>
						<table cellpadding="0" cellspacing="0">
						<s:iterator value="result.user.talks" id="talk">
							<tr>
								<td style="width: 50px; text-align: right;"><s:property value="#talk.name" /></td>
								<td><s:property value="#talk.review" escape="false" /></td>
							</tr>
						</s:iterator>
						</table>
					</td>
				</tr>
				<tr>
					<th>上一次登录时间：</th>
					<td>${result.user.preLoginTime }</td>
				</tr>
				<tr>
					<th>上一次登录地址：</th>
					<td>${result.user.preLoginIP }</td>
				</tr>
				<tr>
					<th>登录次数：</th>
					<td>${result.user.loginTimes }</td>
				</tr>
			</table>
			<div class="buttonLeft">
			<input value="修改子账号" onclick="window.location.href='/member/user_edit.htm?userId=${result.user.userId}'" />
		</div>
		<%@ include file="/page/inc/image_error.jsp" %>
	</body>
</html>