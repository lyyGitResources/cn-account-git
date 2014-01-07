<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$(document).ready(function () {
		$("#adminLogDialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 200,
			width: 350,
			modal: true,
			close: function(){
				$("#adminLogDialog").empty();
			}
		});
	
		$("[name='adminLog']").each(function(){
			$(this).click(function(){
				$("#adminLogDialog").load("/basic/admin_log.do?tableName=NewProduct&tableId=" + $(this).attr("proId"),{random: Math.random()});
				$("#adminLogDialog").dialog('option', 'title', '<s:text name="auditState.rejectRemark"/>');
				$("#adminLogDialog").dialog('open');
			});
		});
		
		Util.check_toggle("ckAll", "proId");
	});
</script>
<s:include value="/page/inc/image_error.jsp"></s:include>
<%@ include file="/page/inc/pagination.jsp"%>
<table class="listTable" cellspacing="1">
	<tbody>
		<tr>
			<th width="5%"><input type="checkbox"  name="ckAll"/></th>
			<th width="10%"><s:text name="product.imgPath" /></th>
			<th width="35%"><s:text name="product.proName" /></th>
			<%-- 
			<th width="10%"><s:text name="product.model" /></th>
			--%>
			<th width="13%"><s:text name="auditState" /></th>
			<th width="9%" class="sort" id="sort_viewCount" onclick="ListTable.sort('viewCount');"><s:text name="viewCount" /></th>
			<th width="10%" class="sort" id="sort_modifyTime" onclick="ListTable.sort('modifyTime');"><s:text name="modifyTime" /></th>
			<th width="18%"><s:text name="operate" /></th>
		</tr>
		<s:iterator value="result.listResult.list" id="product">
			<tr>
				<td><input type="checkbox" name="proId" value="<s:property value='#product.proId' />" <s:if test="!loginUser.admin && loginUser.userId != #product.userId">disabled</s:if>/></td>
				<td>
					<div class="imgBox75"> 
						<a target="_blank" href="<s:property value='#product.imgPathS' />">
							<img src="<s:property value='#product.imgPath75' />" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)"/>
						</a>
					</div>
				</td>
				<td>
					<a href="/newproduct/new_product_edit.htm?newProId=<s:property value='#product.proId' />"><s:property value='#product.proName' /></a>
					<br/>
					<s:property value='#product.model' />
				</td>
				<%-- 
				<td><s:property value='#product.model' /></td>
				--%>
				<td>
					<s:if test="#product.state == 10">
						<a href="#position" proId="<s:property value='#product.proId' />" name="adminLog">
							<s:property value='#product.stateName' />
						</a>
					</s:if>
					<s:else>
						<s:property value='#product.stateName' />
					</s:else>
				</td>
				<td><s:property value='#product.viewCount' /></td>
				<td><s:property value='#product.modifyTime' /></td>
				<td><a href="/newproduct/new_product_detail.htm?newProId=<s:property value='#product.proId' />">查看加密产品</a><br/><s:property value='#product.operate' escape='false' /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
<%@ include file="/page/inc/pagination.jsp"%>
<div id="adminLogDialog"></div>