<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>查看加密产品
		</title>
		<meta name="memoinfo" content="<s:property value='memoInfo'/>"/>
	</head>
	<body>
		<div class="buttonLeft">
			<input type="button" value="修改加密产品" onclick="window.location.href='/newproduct/new_product_edit.htm?newProId=${result.product.proId}'" />
			<input type="button" value="转为普通产品" onclick="window.location.href='/product/product_add.htm?newProId=${result.product.proId}'" />
		</div>
			
			<table class="formTable">
				<tr>
					<th>
						产品图片：
					</th>
					<td>
					<div id="phototooltip" style="position:absolute; margin-top:61px;margin-left:210px; width:16px; height:16px;"></div>
						<img src="${result.product.imgPath75}" alt="" />
					</td>
				</tr>
				<tr>
					<th>产品名称：</th>
					<td>
						${result.product.proName }
					</td>
				</tr>
				<tr>
					<th>产品类型：</th>
					<td>
						${result.product.model }
					</td>
				</tr>
				<tr>
					<th>产品产地：</th>
					<td>
						${result.product.provinceShow }
					</td>
				</tr>
				<tr>
					<th>产品摘要：</th>
					<td class="fieldTips">
						${result.product.brief }
					</td>
				</tr>
				<tr>
					<th>详细描述：</th>
				<td width="627">
					<div style="width:610px;overflow-x:auto;">${result.product.description }</div>
				</td>
				</tr>
			</table>
		<%@ include file="/page/inc/image_error.jsp" %>
	</body>
</html>