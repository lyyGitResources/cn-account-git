<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.util.WebUtil"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>
违规词解禁申请（<s:property value="result.titleText" />）<span class="sapn1" style="margin:0;margin-top:12px;*margin-top:0px;">"<font class="red">*</font>" 为必填项</span>
</title>
<meta name="memoinfo" content="温馨提示：请勿随意删除审核通过的解禁违规词，删除后对应的解禁违规词将无法再公司、产品、商情等信息中继续添加"/>
<script type="text/javascript">

	$(document).ready(function() {
		$("#selectImgDialog").dialog({
			bgiframe : true,
			autoOpen : false,
			height : 600,
			width : 780,
			modal : true,
			close : function() {
				$("#selectImgDialog").empty();
			}
		});

		$("#wordForm").submit(function() {
			if($("[name='patentImgs']").length > 0){
				return true;
			}else{
				$('#phototooltip').html('至少要上传一个有效凭证');
				return false;
			}
		});

		$("#wordForm").validateForm({
			rules : {
				keywords : {
					required : true,
					maxlength : 120
				},
				remark : {
					maxlength : 500
				}
			},
			messages : {
				keywords : {
					required : '请填写解禁违规词',
					maxlength : '您所填写的解禁违规词长度超过了120个字符'
				},
				remark : {
					maxlength : '您所填写的备注长度超过了500个字符'
				}
			}
		});
	});
</script>
</head>
<body>
	<s:property value="com.hisupplier.cn.account.message" />
	<div class="tabMenu" id="tabMenu" style="margin-bottom: 20px;">
		<ul>
			<li class="current">
				<span><a style="text-decoration: none; color: #000;" href="/patent/patentDeblocked.htm">解禁申请</a></span>
			</li>
			<li>
				<span><a style="text-decoration: none; color: #000;" href="/patent/patentDeblocked_list.htm">申请列表</a></span>
			</li>
			<li>
				<span><a style="text-decoration: none; color: #000;" href="/patent/patentDeblocked_license.htm">有效凭证</a></span>
			</li>
		</ul>
	</div>
	<form id="wordForm" name="wordForm" method="post" action="<s:property value="result.submitURL" />" 
		<s:token />
		<input type="hidden" name="id" value="<s:property value="result.patent.id"/>" />
		<input type="hidden" name="state" value="<s:property value="result.patent.state"/>" />
		<table class="formTable">
			<tr>
				<th><span class="red">*</span>违规词：</th>
				<td>
					<input name="keywords" type="text" value="<s:property value="result.patent.keywords"/>" style="width: 320px;"/>
				</td>
			</tr>
			<tr>
				<th><span class="red">*</span>有效凭证：</th>
				<td class="fieldTips">
					<%
						int imgType = Image.LICENSE;
						String imgPathTag = "patentImgs";
						int maxNum = 3; //最大上传数
					%>
					<span id="proAddImgDiv">
						<s:set name="imgExts" value="'*.jpg;*.jpeg;*.gif'" /> 
						<s:set name="patentImgSize" value="'500'" />
						<s:set name="images" value="result.patent.regImgPath" />
						<s:set name="imagIds" value="result.patent.imgIds" />
					 	<%@ include file="/page/inc/image_multi.jsp"%>
					</span>
					<span style="margin-left:4px; color:red;" id="phototooltip"></span>
				</td>
			</tr>
			<tr>
				<th>备注：</th>
				<td>
					<textarea name="remark" style="width: 350px; height: 100px;"><s:property value="result.patent.remark"/></textarea>
				</td>
			</tr>
			<tr>
				<th><s:text name="memberId" />：</th>
				<td><s:property value="result.company.memberId" /></td>
			</tr>
			<tr>
				<th><s:text name="comName" />：</th>
				<td><s:property value="result.company.comName" /></td>
			</tr>
			<tr>
				<th><s:text name="serviceMail.contact" />：</th>
				<td><s:property value="result.userInfo.contact" /></td>
			</tr>
			<tr>
				<th><s:text name="email" />：</th>
				<td><s:property value="result.userInfo.email" /></td>
			</tr>
			<tr>
				<th>电话：</th>
				<td><s:property value="result.userInfo.tel" /></td>
			</tr>
			<tr>
				<th><s:text name="serviceMail.mobile" />：</th>
				<td><s:property value="result.userInfo.mobile" /></td>
			</tr>
			<tr style="height: 60px;"></tr>
		</table>
		<div class="buttonCenter">
			<input type="submit" value="<s:text name='button.submit' />" /> <input type="reset" value="<s:text name='button.reset' />" />
		</div>
	</form>
	<div id="selectImgDialog"></div>
</body>
</html>