<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			查看产品
		</title>
		<meta name="memoinfo" content="<s:property value='memoInfo'/>"/>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.data_zh.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js"></script>
		<script type="text/javascript" src="/js/fckeditor/fckeditor.extend.js" ></script>
		<script type="text/javascript" src="/js/fckeditor/fckeditor.js"></script>
		<script type="text/javascript" src="/js/datePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="/js/product.js"></script>
		
	</head>
	<body>
		<div class="buttonLeft">
		<input type="button" onclick="window.location.href='/product/product_edit.htm?proId=<s:property value="result.product.proId" />'" value="修改产品" />
			<s:if test="result.company.memberType > 1">
			<input type="button" onclick="window.location.href='/product/product_batch_add.htm'" value="批量上传产品" />
				<input type="button" onclick="window.location.href='/newproduct/new_product_add.htm'" value="<s:text name="button.addNewProduct" />" />
			</s:if>
			<s:if test="result.product.proId > 0">
				<input type="button" onclick="window.location.href='/product/product_add.htm?proId=<s:property value="result.product.proId" />'" value="<s:text name="button.addSameProduct" />" />
				<s:if test="result.copyProId != null && result.copyProId > 0">
					<input onclick='isModifyTrade(<s:property value="result.copyProId" />)' type='button' value='<s:text name="button.toTrade" />' />
				</s:if>
				<s:else>
					<input type="button" onclick="window.location.href='/trade/trade_add.htm?copyProId=<s:property value="result.product.proId" />'" value="<s:text name="button.toTrade" />" />
				</s:else>
				<input type="button" onclick="window.location.href='/product/product_delete.do?proId=<s:property value="result.product.proId" />'" value="<s:text name="button.deleteProduct" />" />
				
			</s:if>
		</div>
		
		<div>
			<table  class="formTable">
				<tr>
					<th>所属目录：</th>
					<td>${result.product.catName }</td>
				</tr>
				<tr>
					<th>产品属性：</th>
					<td><s:iterator value="result.product.tagList" id="tag">
							<s:property value='#tag.tagValueName'/>
						</s:iterator>
					</td>
				</tr>
				<tr>
					<th>型号：</th>
					<td>${result.product.model }</td>
				</tr>
				<tr>
					<th>产地：</th>
					<td>${result.product.provinceShow }</td>
				</tr>
				<tr>
					<th>产品名称：</th>
					<td>${result.product.proName }</td>
				</tr>
				<tr>
					<th>产品关键字：</th>
					<td>${result.product.keywords}</td>
				</tr>
				<tr>
					<th>产品图片：</th>
					<td>
						<div class="imgBox75">
							<img src="<s:property value='result.product.imgPath75' />" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)"/>
						</div>
					</td>
				</tr>
				<tr>
					<th>产品视频：</th>
					<td>
					<div class="imgBox75">
						<s:if test="result.product.videoId < 1">
							<img src="<%=Config.getString("video.default") %>" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)" />
						</s:if>
						<s:else>
							<img src="${result.product.videoImgPath }" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)" />
						</s:else>
					</div>
					</td>
				</tr>
				<tr>
					<th>产品摘要：</th>
					<td>${result.product.brief }</td>
				</tr>
				<tr>
					<th>普通分组：</th>
					<td>${result.product.groupName }</td>
				</tr>
				<tr>
					<th>特殊分组：</th>
					<td>
						<s:iterator value="result.product.specialGroupMap" id="SpecialGroup">
							${SpecialGroup.value }<br/>
						</s:iterator>
					</td>
				</tr>
				<tr>
					<th>附件：</th>
					<td><a id="filePathA" href="<s:property value='result.product.filePathUrl'/>" target="_blank" >${result.product.filePathUrl }</a></td>
				</tr>
				<tr>
					<th>价格：</th>
					<td>${result.product.price1 }</td>
				</tr>
				<tr>
					<th>付款方式：</th>
					<td>${result.product.paymentType }</td>
				</tr>
				<tr>
					<th>最小起订量：</th>
					<td>${result.product.minOrderNum } ${result.product.minOrderUnit }</td>
				</tr>
				<tr>
					<th>运输方式：</th>
					<td>${result.product.transportation }</td>
				</tr>
				<tr>
					<th>产量：</th>
					<td>${result.product.productivity }</td>
				</tr>
				<tr>
					<th>交货期：</th>
					<td>${result.product.deliveryDate }</td>
				</tr>
				
				<tr>
					<th>包装：</th>
					<td>${result.product.packing }</td>
				</tr>
				
				<tr>
					<th>提交时间：</th>
					<td>${result.product.createTime }</td>
				</tr>
				<tr>
					<th>修改时间：</th>
					<td>${result.product.updateTime }</td>
				</tr>
				<tr>
					<th>详细描述：</th>
					<td width="627"><div style="width:610px;overflow-x:auto;">${result.product.description }</div></td>
				</tr>
			</table>
		
		</div>
		
	
		<%@ include file="/page/inc/image_error.jsp" %>
	</body>
</html>