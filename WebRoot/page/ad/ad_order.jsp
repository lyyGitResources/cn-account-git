<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='adOrder.formTitle'/></title>
		<meta http-equiv="X-UA-Compatible" content="IE=7" />
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.data_zh.js"></script>
		<script type="text/javascript">
			$(function(){
				$("#adOrderForm").validateForm({
					rules: {
						adType:{required:true},
						remark:{maxlength:500}
					},
					messages: {
						adType: {required:'<s:text name="adOrder.adType.required"/>'},
						remark: '<s:text name="upgrade.remark.required"/>'
					}
				});
				
				$("#_catList").category({
					contId: "catName",
					contType: "text"
				});
				
				if(document.getElementById("keyword").value != ""){
					document.getElementById("keywordDiv").style.display = "";
					document.getElementById("type2").checked = true;
				}else{
					showKeywordText();	
				}
			});
			
			function showKeywordText() {
				var isCheck = document.getElementById("type2");
				if(isCheck.checked == true){
					document.getElementById("keywordDiv").style.display = "";
					$("#keyword").rules("add", {required: true, maxlength:60,
		 				messages: {required:'<s:text name='adOrder.keyword.required'/>'}});
		 			
				}else{
					document.getElementById("keywordDiv").style.display = "none";
					document.getElementById("keyword").value = "";
					$("#keyword").rules("remove");
				}
			}
			
			function showCat() {
				var isCheck = document.getElementById("type1");
				if(isCheck.checked == true){
					document.getElementById("catDiv").style.display = "";
					$("#catName").rules("add", {required: true,
		 				messages: {required:'<s:text name='adOrder.catId.required'/>'}});
				}else{
					document.getElementById("catDiv").style.display = "none";
					document.getElementById("catId").value = "";
					document.getElementById("catName").value = "";
					$("#catName").rules("remove");
				}
			}
		</script>
	</head>
	<body>
		<form id="adOrderForm" name="adOrderForm" method="post" action="/ad/ad_order_submit.htm" enctype="multipart/form-data" >
			<input type="hidden" name="comId" value="<s:property value="result.company.comId"/>"/>
			<input type="hidden" name="userId" value="<s:property value="result.user.userId"/>"/>
			<input type="hidden" name="memberId" value="<s:property value="result.company.memberId"/>"/>
			<input type="hidden" name="comName" value="<s:property value="result.company.comName"/>"/>
			<input type="hidden" name="contact" value="<s:property value="result.user.contact"/>"/>
			<input type="hidden" name="email" value="<s:property value="result.user.email"/>"/>
			<input type="hidden" name="tel" value="<s:property value="result.user.tel"/>"/>
			<input type="hidden" name="fax" value="<s:property value="result.user.fax"/>"/>
			<input type="hidden" name="mobile" value="<s:property value="result.user.mobile"/>"/>
			<s:token />
			<table class="formTable">
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name='adOrder.adType'/>：</th>
					<td>
						<div id="adTypeTip"></div>
						<input type="checkbox" id="type1" name="adType" value="1" onclick="showCat()"/> &nbsp;<s:text name='adOrder.adType1'/>&nbsp;<span class="red"><s:text name='adOrder.money'/></span><br />
							<div id="catDiv" style="display:none;">
								<input type="hidden" id="catId" name="catId" value="<s:property value='result.product.catId'/>" />
								<input type="text" id="catName" name="catName" value="<s:property value='result.product.catName'/>" style="width:320px" readonly />
								<input type="button" id="_cat" value="<s:text name="button.selectCat" />" />
								<br />
								<div id="_catList" ></div>
								<div id="catNameTip" ></div>
							</div>
						<input type="checkbox" id="type2" name="adType" value="2" onclick="showKeywordText()"/> &nbsp;<s:text name='adOrder.adType2'/>&nbsp;<span class="red"><s:text name='adOrder.money'/></span><br />
							<div id="keywordDiv" style="display:none;">
								请输入关键词：
								<input id="keyword" type="text" name="keyword" value="<s:property value="order.keyword"/>" style="width:260px;" class="form_text">
								<div id="keywordTip"></div>
							</div>
					</td>
				</tr>
				<tr>
					<th><s:text name='upgrade.remark'/>：</th>
					<td>
						<textarea id="remark" name="remark" style="width: 450px; height: 70px;"></textarea>
						<div id="remarkTip"></div>
					</td>
				</tr>
				<tr>
					<th><s:text name='memberId'/>：</th>
					<td>
						<s:property value="result.company.memberId"/>
					</td>
				</tr>
				<tr>
					<th><s:text name='comName'/>：</th>
					<td>
						<s:property value="result.company.comName"/>
					</td>
				</tr>
				<tr>
					<th><s:text name='upgrade.contact'/>：</th>
					<td>
						<s:property value="result.user.contact"/>
					</td>
				</tr>
				<tr>
					<th><s:text name='email'/>：</th>
					<td>
						<s:property value="result.user.email"/>
					</td>
				</tr>
				<tr>
					<th><s:text name='upgrade.tel'/>：</th>
					<td>
						<s:property value="result.user.tel"/>
					</td>
				</tr>
				<tr>
					<th><s:text name='upgrade.fax'/>：</th>
					<td>
						<s:property value="result.user.fax"/>
					</td>
				</tr>
				<tr>
					<th><s:text name='upgrade.mobile'/>：</th>
					<td>
						<s:property value="result.user.mobile"/>
					</td>
				</tr>
			</table>
			<div class="buttonCenter">
				<input type="submit"  value="<s:text name='button.submit' />"/> &nbsp;&nbsp;
				<input type="reset"  value="<s:text name='button.reset' />"/> 
			</div>
		</form>
	</body>
</html>
