<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	var param =	{
		isList:false,
		background:"#ffffff",
		listId: "listOrder",	// 排序列表父标签ID
		buttonName:"buttons",	// 完成排序和OK按钮名称
		form:"#orderForm",		// 表单
		numberName:"[name='number']", // 序号位置
		total: <s:property value="result.listResult.page.resultTotal"/>, // 判断输入序号是否太大
		pageNo:<s:property value="pageNo"/>,
		pageSize:<s:property value="pageSize"/>,			
		url: "/product/product_single_order_submit.do?groupId=<s:property value='groupId'/>&"
	}
</script>
<script type="text/javascript" src="/js/drag/drag.extend.js"></script>
<s:include value="/page/inc/image_error.jsp"/>
<s:if test="result.listResult.list.size > 0">
	<div style="float: left;">
		<%@ include file="/page/inc/pagination.jsp"%>
	</div>
</s:if>
<input type="hidden" name="pageNo" value="<s:property value='pageNo'/>">
<div class="productOrderList" <s:if test="result.listResult.list.size == 0">style="display:none;"</s:if>>
	<ul id="listOrder">
		<s:iterator value="result.listResult.list" id="product" status="st">
			<li itemid='<s:property value="#st.count"/>' title="按鼠标左键拖拽选中项">
				<center>
					<input type="hidden" name="proId" value='<s:property value="#product.proId"/>'>
					<div class="img75">
						<img src="<s:property value="#product.imgPath75"/>" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)"/>
					</div>
					<div name="serialDiv" id="<s:property value="#product.proId"/>" style="height:20px;">
						<span id="_newOrderDiv<s:property value="#product.proId"/>" name="_newOrderDiv">
							<input style="width:20px;" id="_newOrder<s:property value="#product.proId"/>" />
							<a href="#position" name="buttons" onclick="ToolMan._extend.setSortValue($('#_newOrder<s:property value="#product.proId"/>').val())">
								<s:text name='button.confirm' />
							</a>
						</span>		
						<span id="_number<s:property value="#product.proId"/>" name="number" class="number">
						</span>
					</div>
					<span title="<s:property value="#product.proName"/>"><s:property value="#product.shortProName"/></span><br/>
					<span title="<s:property value="#product.model"/>"><s:property value="#product.shortModel"/></span>
				</center>
			</li>
<%-- 			<s:if test="#st.count % 5 == 0"> --%>
<!-- 				<li style="clear:both; overflow:hidden; width:100%; float:none; font-size:0; height:0; padding:0; margin:0;"></li> -->
<%-- 			</s:if> --%>
		</s:iterator>
	</ul>
</div>
<s:if test="result.listResult.list.size > 0">
	<div style="float: left;margin-bottom: 5px;">
		<%@ include file="/page/inc/pagination.jsp"%>
	</div>
</s:if>