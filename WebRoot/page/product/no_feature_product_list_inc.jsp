<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:include value="/page/inc/image_error.jsp"></s:include>
<div class="noFeatureProductList">
	<%@ include file="/page/inc/pagination.jsp"%>
</div>
<table cellspacing="1" class="noFeatureProductList">
	<tbody>
		<tr>
			<th style="width:80px;"><s:i18n name="com.hisupplier.cn.account.product.package"><s:text name="product.imgPath" /></s:i18n></th>
			<th style="width:380px;"><s:i18n name="com.hisupplier.cn.account.product.package"><s:text name="product.proName" /></s:i18n></th>
			<th style="width:100px;"><s:i18n name="com.hisupplier.cn.account.product.package"><s:text name="product.createAndModifyTime" /></s:i18n></th>
			<th style="width:100px;"><s:text name="operate" /></th>
		</tr>
		<s:iterator value="result.listResult.list" id="product">
			<tr>
				<td>
					<div class="img75">
						<a href="<s:property value='#product.imgPathS' />" target="_blank">
							<img src="<s:property value='#product.imgPath75' />" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)"/>
						</a>
					</div>
				</td>
				<td>
					<s:property value="#product.proName" />
					<br/>
					<s:property value="#product.model" />
				</td>
				<td>
					<s:property value="#product.createTime" /><br/>
					<s:property value="#product.modifyTime" />
				</td>
				<td><a href="/product/feature_product_add.htm?proId=<s:property value="#product.proId" />"><s:text name="button.featureProduct"/></a></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
<div class="noFeatureProductList">
	<%@ include file="/page/inc/pagination.jsp"%>
</div>
