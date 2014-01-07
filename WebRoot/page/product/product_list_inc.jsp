<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$(document).ready(function () {
		$("#adminLogDialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 200,
			width: 360,
			modal: true,
			close: function(){
				$("#adminLogDialog").empty();
			}
		});
	
		$("[name='adminLog']").each(function(){
			$(this).click(function(){
				$("#adminLogDialog").load("/basic/admin_log.do?tableName=Product&tableId=" + $(this).attr("proId"),{random: Math.random()});
				$("#adminLogDialog").dialog('option', 'title', '<s:text name="auditState.rejectRemark"/>');
				$("#adminLogDialog").dialog('open');
			});
		});
		
		Util.check_toggle("ckAll", "proId");
	});
</script>
<s:include value="/page/inc/image_error.jsp"/>
<%@ include file="/page/inc/pagination.jsp"%>
<table class="listTable" cellspacing="1">
	<tbody>
		<tr>
			<th width="5%"><input type="checkbox" name="ckAll"/></th>
			<th width="10%"><s:i18n name="com.hisupplier.cn.account.product.package"><s:text name="product.imgPath" /></s:i18n></th>
			<th width="35%"><s:i18n name="com.hisupplier.cn.account.product.package"><s:text name="product.proName" /></s:i18n></th>
			<%--
			<th width="15%"><s:i18n name="com.hisupplier.cn.account.product.package"><s:text name="product.model" /></s:i18n></th>
			 --%>
			<s:if test="state != 5">
				<th width="10%"><s:i18n name="com.hisupplier.cn.account.product.package"><s:text name="product.typeAndState" /></s:i18n></th>
			</s:if>
			<th width="9%" class="sort" id="sort_viewCount" onclick="ListTable.sort('viewCount');"><s:text name="viewCount" /></th>
			<th width="6%" class="sort" id="sort_likeRecordCount" onclick="ListTable.sort('likeRecordCount')"><span class="like">赞</span></th>
			<th width="12%" id="sort_modifyTime"><s:if test="state == 5"><s:text name="deleteTime" /></s:if><s:else>提交/修改时间</s:else></th>
			<th width="12%"><s:text name="operate" /></th>
		</tr>
		<s:iterator value="result.listResult.list" id="product">
			<tr>
				<%-- 子帐号能操作回收站，但不能操作不是自己产品的产品列表 --%>
				<td>
					<input type="checkbox" name="proId" value="<s:property value='#product.proId' />" <s:if test="!loginUser.admin && loginUser.userId != #product.userId && #product.state != 5">disabled</s:if> />
				</td>
				<td>
					<div class="imgBox75">
					<s:if test="#product.state == 10 || #product.state == 15">
						<a target="_blank" href="<s:property value='#product.imgPath75' />">
					</s:if>
					<s:elseif test="#product.state == 5">
						<a href="/product/product_detail.htm?proId=${product.proId }">
					</s:elseif>
					<s:else>
						<a target="_blank" href="<s:property value='previewUrl.showroomUrl' escape='false'/>/product_detail.htm?type=Product&proId=${product.proId }">					
					</s:else>
							<img src="<s:property value='#product.imgPath75' />" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)"/>
						</a>
					</div>
				</td>
				<td>
					<s:if test="#product.state == 10">
						<a href="/product/product_edit.htm?proId=<s:property value='#product.proId' />">${product.proName}</a>
					</s:if>
					<s:elseif test="#product.state == 5">
						<a href="/product/product_detail.htm?proId=${product.proId }">${product.proName }</a>
					</s:elseif>
					<s:elseif test="#product.state == 15">
						<a href="/product/product_detail.htm?proId=<s:property value='#product.proId' />">${product.proName}</a>
					</s:elseif>
					<s:else>
						<a target="_blank" href="<s:property value='previewUrl.showroomUrl' escape='false'/>/product_detail.htm?type=Product&proId=${product.proId }">${product.proName}</a>			
					</s:else>
					<span id="productListNewIco<s:property value='#product.proId' />">
						<s:if test="#product.showNewIco == true"><img src='<%=Config.getString("img.base") %>/img/ico/group_new.gif'/></s:if>
					</span>
					<br/>
					<s:property value='#product.model' />
				</td>
				<%-- 
				<td><s:property value='#product.model' /></td>
				--%>
				<s:if test="state != 5">
					<td>
						<s:property value='#product.proTypeText' /><br />
						<s:if test="#product.state == 10">
							<a href="#position" proId="<s:property value='#product.proId' />" name="adminLog">
								<s:property value='#product.stateName' />
							</a>
						</s:if>
						<s:else>
							<s:property value='#product.stateName' />
						</s:else>
					</td>
				</s:if>
				<td><s:property value='#product.viewCount' /></td>
				<td>${product.likeRecordCount }</td>
				<td>${product.createTime }<br/>${product.modifyTime}</td>
				<td><a href="/product/product_detail.htm?proId=<s:property value='#product.proId' />">查看产品</a><br/><s:property value='#product.operate' escape='false' /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
<%@ include file="/page/inc/pagination.jsp"%>
<div id="adminLogDialog"></div>