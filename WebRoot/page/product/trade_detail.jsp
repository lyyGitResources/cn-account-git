<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ page import="com.hisupplier.commons.CN"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			查看商情
		</title>
		<meta name="memoinfo" content="<s:property value='memoInfo'/>"/>
	</head>
	<body>
		<div class="buttonLeft">
			<input type="button" onclick="window.location.href='/trade/trade_edit.htm?proId=<s:property value="result.product.proId" />'" value="修改商情" />
			<input type="button" onclick="window.location.href='/trade/trade_delete.do?proId=<s:property value="result.product.proId" />'" value="删除商情" />
			<input type="button" onclick="window.location.href='/trade/trade_add.htm?proId=<s:property value="result.product.proId" />'" value="添加同类商情" />
			
		</div>
			<table class="formTable">
				<tbody>
					<tr>
						<th>
							商情图片：
						</th>
						<td>
						<div id="tradeImgtooltip" style="position:absolute; margin-top:61px;margin-left:210px; width:16px; height:16px;"></div> 
						<div><img src="${result.product.imgPath75}" /></div>
						</td>
					</tr>
					
					<tr>
						<th>商情类型：</th>
						<td>
							<s:if test="result.product.proType == 1">供应商情</s:if>
							<s:if test="result.product.proType == 2">采购商情</s:if>
						</td>
					</tr>

					<tr>
						<th>商品主题：</th>
						<td>
							${result.product.proName }
						</td>
					</tr>
					
					<tr>
						<th>产品关键字：</th>
						<td>
							${result.product.keyword1 }
						</td>
					</tr>
					
					<tr>
						<th>普通分组：</th>
						<td >
							${result.product.groupName }
						</td>
					</tr>
					
					<tr>
						<th>行业目录：</th>
						<td>
						${result.product.catName }
						</td>
					</tr>
					<tr>
						<th>商情摘要：</th>
						<td>
						${result.product.brief }
						</td>
					</tr>
					<tr>
						<th>有效期：</th>
						<td>
						<s:if test="result.product.validDay==0">无期限</s:if>
						<s:else>${result.product.validDay}&nbsp;天</s:else>
						</td>
					</tr>
					<tr>
						<th>详细描述：</th>
						<td width="627"><div style="width:610px;overflow-x:auto;">${result.product.description }</div></td>
					</tr>			
				</tbody>
			</table>
		<%@ include file="/page/inc/image_error.jsp" %>
	</body>
</html>