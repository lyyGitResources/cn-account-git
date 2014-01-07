<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			<s:text name="product.featureListTitle"/>
			<span class="gray">
				<s:text name="product.featureExist">
					<s:param><s:property value="result.featureProCount"/></s:param>
					<s:param><s:property value="result.featureProMax"/></s:param>
				</s:text>
			</span>
		</title>
		<script type="text/javascript">
			$(document).ready(function () {
				$("#listDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 530,
					width: 730,
					modal: true
				});
				
				$("#normalGroupList").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 500,
					width: 520,
					modal: true,
					close:function() {
						(document.getElementsByName(tempFeatureName)[0]).checked = true;
					}
				});
				
				$("[name='serialDiv']").each(function(){
					var $this = $(this);
					
					$(this).hover(
						function(){
							hideNewOrderDiv();
							
							var order = $.trim($("#_number"+ $this.attr("id")).text());
							order = order.substring(3);
							
							$("#_newOrder" + $this.attr("id")).val(order);
							$("#_oldOrder").val(order);
							$("#_newOrderDiv" + $this.attr("id")).show();
							$("#_newOrder" + $this.attr("id")).select();
							
							$("#_number"+ $this.attr("id")).text("");
						},
						function(){
							$("#_number"+ $this.attr("id")).text("序号：" + $("#_oldOrder").val());
							hideNewOrderDiv();
						}
					);
				});
				
			});
			
			function hideNewOrderDiv(){
				$("[name='_newOrderDiv']").each(function(){
					$(this).hide();
				});
			}
	
			var tempFeatureName;
			function showGroupList (el, proId) {
				tempFeatureName = el.name;
				$("#tmpProId").val(proId);
				$("#normalGroupList").dialog('open');
			}

			function showProductList (url) {
				$("#listDialog").html(AJAX_LOADING_CODE);
				$("#listDialog").load(url,{random: Math.random()});
				$("#listDialog").dialog('open');
			}
			
			function selectGroup(groupId,groupName,groupNamePath) {
				window.location.href = "/product/feature_product_set.htm?groupId="+groupId+"&proId="+$("#tmpProId").val();
			}
			
			function setSortValue(newOrder){
				if($("#_oldOrder").val() == newOrder){
					return;
				}
				if(isNaN(newOrder * 1) || newOrder.indexOf(".") != -1 || newOrder * 1 <= 0 || newOrder * 1 > <s:property value="result.listResult.list.size"/>){
					alert("请输入一个正确的排序值！");
					return;
				}
				
				$("#_newOrder").val(newOrder);
				$("#orderForm").submit();
			}
			
			function remove_feature_product(proId){
				$("#dialog").dialog('open');
				window.location.href = "/product/feature_product_remove.htm?proId="+proId;
			}
			
		</script>
	
	</head>
	
	<body>
		<%@ include file="/page/inc/image_error.jsp" %>
		<div class="pageTips">
			<span><s:text name="product.feature.notice0"/>:</span>
			<ul>
				<li><s:text name="product.feature.notice1"/></li>
				<li><s:text name="product.feature.notice2"/></li>
				<li><s:text name="product.feature.notice3"/></li>
				<li><s:text name="product.feature.notice4"/></li>
			</ul>
		</div>
		
		<form id="orderForm" action="/product/feature_product_order.htm" method="post">
			<input type="hidden" id="_oldOrder" name="oldOrder"/>
			<input type="hidden" id="_newOrder" name="listOrder"/>
			<input type="hidden" id="tmpProId"/>
		
			<div class="featureList">
				<ul>
					<s:iterator value="result.listResult.list" id="product" status="st">
							<li><div style="width: 150px ; height: 120px; margin: 10px auto">
								<div class="imgBox75" style="float:left;">
									<img src="<s:property value='#product.imgPath75' />" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)"/>
								</div>
								<div class="text"  style="width: 60px">
									<span title="<s:property value="#product.proName"/>">
										<a title="${product.proName }" href="/product/product_edit.htm?proId=<s:property value="#product.proId"/>">
												${product.shortFeatureProName.length() > 3 ? product.shortFeatureProName.substring(0,3).concat("...") : product.shortFeatureProName}
										</a> 
									</span><br />
									<span title="<s:property value="#product.model"/>">
										<s:property value="#product.shortFeatureModel"/>
									</span><br />
									<input type="hidden" name="proId" value="<s:property value="#product.proId"/>"/>
									<div name="serialDiv" id="<s:property value="#product.proId"/>" style="height:20px;">
										<span id="_newOrderDiv<s:property value="#product.proId"/>" name="_newOrderDiv" style="display:none;">
											<input style="width:20px;" id="_newOrder<s:property value="#product.proId"/>" />
											<a href="#position" name="buttons" onclick="setSortValue($('#_newOrder<s:property value="#product.proId"/>').val())">
												<s:text name='button.confirm' />
											</a>
										</span>
										<span id="_number<s:property value="#product.proId"/>" class="number">
											序号：<s:property value="#st.count"/>
										</span>
									</div>
									<s:if test="loginUser.admin">
										<a href="javascript:remove_feature_product(<s:property value="#product.proId"/>)"><s:text name="button.cancelFeature"/></a>
									</s:if>
								</div>
								<s:if test="loginUser.admin">
									<div class="form"  style="width: 140px">
										<div class="span_inline">
											<input value="0" name="feature<s:property value="#st.count"/>" type="radio" onclick="window.location.href = '/product/feature_product_set.htm?groupId=0&proId=<s:property value="#product.proId"/>'" <s:if test="#product.featureGroupId == 0">checked</s:if>  />
											<label for="feature${st.count }"><s:text name="product.linkToProduct"/></label>
										</div>
										<div class="span_inline">
											<input value="1" name="feature<s:property value="#st.count"/>" type="radio" onclick="showGroupList (this, <s:property value="#product.proId"/>)" <s:if test="#product.featureGroupId > 0">checked</s:if> />
											<lable for="feature${st.count }"><s:text name="product.linkToGroup"/></lable>&nbsp;<s:if test="#product.featureGroupId > 0"><a style="text-decoration: none; color: black" title="${product.groupName }">(${product.groupName.length() > 2 ? product.groupName.substring(0,2).concat("..."): product.groupName})</a></s:if>
										</div>
									</div>
								</s:if>
							</div></li>
						<s:if test="#st.count%4==0">
							<li class="line"></li>
						</s:if>
				    </s:iterator>
	
			    	<s:iterator value="new int[result.featureProMax - result.listResult.list.size]" status="st">
						<li>
							<center style="margin-top: 20px;">
								<div class="imgBox75" >
									<img  src="<%=Config.getString("img.default") %>" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)"/>
								</div>
								<div>
									<a href="#position;" onclick="javascript:showProductList('/product/no_feature_product_list.do');"><s:text name="button.selectFeature"/></a>
								</div>
							</center>
						</li>
						<s:if test="(#st.count + result.listResult.list.size)%4==0">
							<li class="line"></li>
						</s:if>
					</s:iterator>
				</ul>
			</div>
		</form>
		
		<div id="listDialog" title='<s:text name="product.selectFeatureTitle"/>'></div>
		
		<div id="normalGroupList" title='<s:i18n name="com.hisupplier.cn.account.group.package"><s:text name="group.listTitle"/></s:i18n>'>
			<iframe src='/group/select_group_list.do?feature=1000' frameborder="no" scrolling="yes" style="width:100%; height:420px;*height:450px !important;*height:450px;"></iframe>
		</div>
	</body>
</html>
