<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="group.listOrderTitle"/></title>
	    <script type="text/javascript" src="/js/drag/core.js"></script>
	    <script type="text/javascript" src="/js/drag/events.js"></script>
	    <script type="text/javascript" src="/js/drag/css.js"></script>
	    <script type="text/javascript" src="/js/drag/coordinates.js"></script>
	    <script type="text/javascript" src="/js/drag/drag.js"></script>
	    <script type="text/javascript" src="/js/drag/dragsort.js"></script>
	    <script type="text/javascript" src="/js/drag/cookies.js"></script>
	    <script type="text/javascript" src="/js/drag/drag.extend.js"></script>
		<script type="text/javascript">
			var param =	{
				isList:true,
				background:"#ffffff",
				listId: "listOrder",	// 排序列表父标签ID
				buttonName:"buttons",	// 完成排序和OK按钮名称
				form:"#groupOrderForm",		// 表单
				numberName:"[name='number']", // 序号位置
				total: <s:property value="result.groupList.size"/>, // 判断输入序号是否太大
				pageNo:0,				// 0表示不分页
				pageSize: 0,			
				url: null				// 产品排序使用
			}
			
		</script>
	</head>
	<body>

		<div class="pageTips">
			<span><s:text name="group.listOrder.notice0"/>:</span>
			<ul>
				<li>
					<s:text name="group.listOrder.notice1"/>：
					<ul>
						<li><s:text name="group.listOrder.notice2"/></li>
						<li><s:text name="group.listOrder.notice3"/></li>
					</ul>
				</li>
				<li>
					<s:text name="group.listOrder.notice4"/>
				</li>
			</ul>
		</div>
		
		<form action="/group/group_order_submit.htm" id="groupOrderForm">
			<input type="hidden" id="_oldOrder"/>
			<input type="hidden" name="parentId" value='<s:property value="result.parentId"/>'/>
			
			<p><input type="submit" name="buttons" value='<s:text name="button.saveOrder"/>' disabled/></p>
			
			<div class="orderList">

				<div class="title">
					<div style="width:80px;"><s:text name="serialNumber"/></div>
					
						<div style="width:300px;"><s:text name="group.groupName"/>（<s:text name="group.productCount"/>/<s:text name="group.tradeCount"/>）</div>
					
					<div style="width:80px;"><s:text name="group.child"/></div>
					<div style="width:125px;"><s:text name="modifyTime"/></div>
					
						<div style="width:149px;"><s:text name="operate"/></div>
					
				</div>
			
				<ul id="listOrder">
					<s:iterator value="result.groupList" id="group" status="st">
					<li itemid='<s:property value="#st.count"/>' title="按鼠标左键拖拽选中项">
						<input type="hidden" name="groupIds" value='<s:property value="#group.groupId"/>'>
						
						<div style="padding-top: 0;position: relative;top: 12px;width: 80px;" name="serialDiv" id="<s:property value="#group.groupId"/>" >
							<span id="_newOrderDiv<s:property value="#group.groupId"/>" name="_newOrderDiv" style="text-align:center;">
								<input style="width:20px;" id="_newOrder<s:property value="#group.groupId"/>" />
								<a href="#position" name="buttons" onclick="ToolMan._extend.setSortValue($('#_newOrder<s:property value="#group.groupId"/>').val())">
									<s:text name='button.confirm' />
								</a>
							</span>
							<span id="_number<s:property value="#group.groupId"/>" name="number" ></span>
						</div>
						
							<div style="width:300px;">
						
							<s:property value="#group.groupName"/>(<s:property value="#group.productCount"/>&nbsp;/&nbsp;<s:property value="#group.tradeCount"/>)
						</div>	
						<div style="width:80px;">
							<s:property value="#group.child"/>
						</div>	
						<div style="width:125px;">
							<s:property value="#group.modifyTime"/>
						</div>
						
							<div style="width:149px;">
								<s:if test="#group.child == 0">
									<s:if test="#group.productCount == 0 || #group.productCount == 1"><p style="color: gray;">组内产品排序</p></s:if>
									<s:else>
									<a href="/product/product_order.htm?groupId=<s:property value="#group.groupId"/>"><s:text name="button.orderInGroup"/></a>&nbsp;
									</s:else>
								</s:if>
								<s:else>
									<a href="/group/group_order.htm?parentId=<s:property value="#group.groupId"/>&pageSize=50"><s:text name="button.selectChildGroup"/></a>&nbsp;
								</s:else>
								
								<%-- 
								<s:if test="#group.child > 0">
									<a href="javascript:location.href = '/group/group_order.htm?parentId=<s:property value="#group.groupId"/>'" ><s:text name="button.orderChildGroup"/></a>
								</s:if>
								--%>
							</div>
						
					</li>
					</s:iterator>
				</ul>
				
			</div>
			<p><input type="submit" name="buttons" value='<s:text name="button.saveOrder"/>' disabled/></p>
		</form>
	
	</body>
</html>

