<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.hisupplier.commons.Config" %>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.util.WebUtil"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>企业身份认证</title>
<style>
	.profile_table { width: 625px;}
	.profile_table th { width: 200px; text-align:right;}
	.profile_table th span { color: red; }
</style>
<script>
$(function() {
	$("#profile_form").validate({
		rules: {
			companyType: { min: 1 },
			regLink: { required: true },
			regAddress: { required: true, maxlength: 150 },
			<s:if test="model.editCeo">
				ceo: { required: true },
			</s:if>
			regCapital: { required: true, number: true },
			businessScope: { required: true, maxlength: 200 },
			establishDate: { required: true },
			regDate: { required: true, dateISO: true},
			regExpiry: { required: true, dateISO: true },
			regAuthority: { required: true },
			reviewTime: { required: true, dateISO: true},
			currency: { required: true },
			taxImgPath: { required: function() {
				return true;
			} },
			f1ImgPath: { 
				required: function() {
					return ($.trim($("#f2ImgPath").val()) == "" && $.trim($("#f3ImgPath").val()));
				} 
			}
		},
		messages: {
			businessScope: { maxlength: "经营范围字数不能大于200" },
			companyType: {
				min: "请选择正确的企业类型。"
			},
			f1ImgPath: {
				required: "请上传相关证件"
			},
			taxImgPath: {
				required: "请上传税务登记证"
			}
		}
	});
});
</script>
</head>
<body>
	<form id="profile_form" action="/member/profile_edit.htm" method="post">
	<table class="profile_table" cellspacing="10">
		<tr>
		<th>公司名称：</th>
		<td><s:property value="model.comName"/></td>
		</tr>
		<tr>
		<th><span>*</span>工商注册号：</th>
		<td><input name="regLink" value="<s:property value='model.regLink' />" /></td>
		</tr>
		<tr>
		<th><span>*</span>企业类型：</th>
		<td>
			<s:select list="model.comTypes" 
				headerKey="0" headerValue="请选择"
				listKey="key" listValue="value"
				value="model.companyType"
				name="companyType"
				theme="simple"
			/>
		</td>
		</tr>
		<tr>
		<th><span>*</span>住所：</th>
		<td><input name="regAddress" value="<s:property value='model.regAddress' />" /></td>
		</tr>
		
		<s:if test="model.editCeo">
			<tr>
				<th><span>*</span>法定代表人：</th>
				<td><input name="ceo" value="<s:property value='model.ceo' />" /></td>
			</tr>
		</s:if>
		<s:else>
			<tr>
				<th>法定代表人：</th>
				<td><s:property value='model.ceo' /></td>
			</tr>
		</s:else>
		
		<tr>
		<th><span>*</span>注册资金：</th>
		<td>
			<input name="regCapital" value="<s:property value='model.regCapitalShow' />" /> 万
			<select id="currency_select" name=""></select>
			<input id="currency_input" size="6" placeholder="人民币" name="currency" value="<s:property value='model.currency' />" />
			<script>
			var currencys = [ "其它", "人民币", "欧元", "美金", "日元" ];
			var currency = "<s:property value='model.currency' />";
			var $currency_select = $("#currency_select");
			var $currency_input = $("#currency_input");
			$(currencys).each(function() {
				var selected = false;
				var $option = $("<option value='" + this 
						+ "' "
						+ (currency == this ? 'selected' : "")
						+ " >" 
						+ this + "</option>")
				$currency_select.append($option)
			});
			$currency_select.change(function() {
				var $el = $(this);
				if ($el.val() == "其它") $currency_input.show();
				else {
					$currency_input.hide().val($el.val());
				}
			});
			if ($currency_select.val() == "其它") {
				$currency_input.show();
			} else {
				$currency_input.hide();
			}
			</script>			
		</td>
		</tr>
		<tr>
		<th><span>*</span>经营范围：</th>
		<td>
			<textarea id="businessScope" name="businessScope" rows="4" cols="50" style="display: block;"><s:property value='model.businessScope' /></textarea>
		</td>
		</tr>
		<tr>
		<th><span>*</span>成立日期：</th>
		<td>
			<select id="establishDate" name="establishDate"></select>
			<script>
			var $establishDate = $("#establishDate");
			for (var i = 1949; i <= new Date().getFullYear(); i++) {
				$establishDate.append("<option value='" + i + "'>" + i + "</option>");
			}
			$establishDate.val(<s:property value='model.establishDate' />);
			</script>
		</td>
		</tr>
		<tr>
		<th><span>*</span>营业期限：</th>
		<td>
			<input name="regDate" value="<s:property value='model.regDate' />" /> - 
			<input name="regExpiry" value="<s:property value='model.regExpiry' />" />
		</td>
		</tr>
		<tr>
		<th><span>*</span>登记机关：</th>
		<td><input name="regAuthority" value="<s:property value='model.regAuthority' />" /></td>
		</tr>
		<tr>
		<th><span>*</span>年检时间：</th>
		<td><input name="reviewTime" value="<s:property value='model.reviewTime' />" /></td>
		</tr>
		<s:if test="model.foodCompany">
		<tr>
		<th style="vertical-align: top"><span>*</span>税务登记证：</th>
		<td>
		<s:set name="type" value="'tax'" />
		<s:set name="imgPath" value="model.taxImgPath" />
		<s:set name="imgSrc" value="model.taxImgSrc" />
		<%@ include file="/page/inc/file_bar.jsp" %>
		</td>
		</tr>
		
		<tr>
		<th style="vertical-align: top"><span>*</span>选择上传相关食品证件：</th>
		<td>
		<%@ include file="inc/food_img_bar.jsp" %>
		</td>
		</tr>
		
		</s:if>
		<tr>
		<td class="buttonCenter" colspan="2" >
			<input type="submit" value="提交" />
			<input type="reset" value="重置" />
		</td>
		</tr>
	</table>
	</form>
</body>
</html>