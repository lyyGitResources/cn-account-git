<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gb2big5-converter" prefix="gb2big5"%>
<gb2big5:Gb2Big5JspWrapper>
<ul>
	<s:iterator value="result.serviceList" id="service" status="st">
		<li>
			客服专员：<s:property value="#service.contact"/><br />
			工号：<s:property value="#service.workId"/><br />
			直线：<span class="phone" style=""><s:property value="#service.tel"/></span><br />
			<%--
			<span style="margin-left: 35px;">按1键，拨分机</span><span class="phone" style=""><s:property value="#service.tel"/><br /></span>
			 --%>
			<%-- 直线/热线：<span class="phone" style=""><s:property value="#service.tel"/></span><br />--%>
<%-- 			分机：<s:property value="#service.tel"/><br /> --%>
			<s:if test="#service.fax != null && #service.fax != ''">
				传真：<s:property value="#service.fax"/><br />
			</s:if>
			E-mail：<a href='mailto:<s:property value="#service.email"/>' target='blank'><s:property value="#service.email"/></a>
			<div>
				<a href='http://wpa.qq.com/msgrd?V=5&Uin=<s:property value="#service.qq" />' target="_blank">
					<img border='0' src='http://wpa.qq.com/pa?p=5:<s:property value="#service.qq" />:5'>
				</a>
			</div>
		</li>
	</s:iterator>
</ul>
<br />
服务时间：9:00-17:00<br /> 
(周一~周五，法定节假日除外)
<s:if test="loginUser.memberType == 1">
	<div style="text-align:center; padding-bottom:5px; margin:0;">
		<a href="/ad/upgrade.htm"><img src="/img/ico/Topsite.gif" border="0" /></a>
	</div>
</s:if>
</gb2big5:Gb2Big5JspWrapper>