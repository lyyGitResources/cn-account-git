<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
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
		listId: "listOrder",	// 排序列表父标签ID
		buttonName:"buttons",	// 完成排序和OK按钮名称
		form:"#groupOrderForm",		// 表单
		newOrderDiv:"#newOrderDiv",
		numberName:"[name='number']", // 序号位置
		total: <s:property value="result.groupList.size"/>, // 判断输入序号是否太大
		pageNo:0,				// 0表示不分页
		pageSize:0,			
		url:null,				// 产品排序使用
		background:"#ffffff"	
	}
</script>
<form action="/specialGroup/group_order_submit.htm" id="groupOrderForm">
	<input type="hidden" id="_oldOrder"/>
	<div class="orderList">
		<div class="title">
			<div style="width:50px;"><s:text name="serialNumber"/></div>
			<div style="width:300px;"><s:text name="specialGroup.groupName"/></div>
			<div style="width:84px;"><s:text name="specialGroup.productCount"/></div>
			<div style="width:300px;"><s:text name="operate"/></div>
		</div>
	
		<ul id="listOrder">
			<s:iterator value="result.groupList" id="group" status="st">
			<li itemid='<s:property value="#st.count"/>' title="按鼠标左键拖拽选中项">
				<input name="groupIds" value='<s:property value="#group.groupId"/>' style="display:none;">
	
				<div style="width:80px;" name="serialDiv" id="<s:property value="#group.groupId"/>" >
					<span id="_newOrderDiv<s:property value="#group.groupId"/>" name="_newOrderDiv">
						<input style="width:20px;" id="_newOrder<s:property value="#group.groupId"/>" />
						<a href="#position" name="buttons" onclick="ToolMan._extend.setSortValue($('#_newOrder<s:property value="#group.groupId"/>').val())">
							<s:text name='button.confirm' />
						</a>
					</span>
					
					<span id="_number<s:property value="#group.groupId"/>" name="number" >
					</span>
				</div>
						
				<div style="width:270px;">
					<s:property value="#group.groupName"/>
				</div>	
				<div style="width:84px;">
					<s:property value="#group.productCount"/>
				</div>	
				<div style="width:300px;">
					<a href="#position" onclick="javascript:showFormDialog_special_group('/specialGroup/group_edit.do?groupId=<s:property value="#group.groupId"/>','<s:text name="specialGroup.modifyGroupTitle"/>')"><s:text name="button.edit"/></a>&nbsp;
					<a href="#position" onclick='javascript:deleteGroup(<s:property value="#group.groupId"/>)'><s:text name="button.delete"/></a>&nbsp;
					<s:if test="#group.productCount > 0">
						<a href="/specialGroup/group_product_list.htm?groupId=<s:property value="#group.groupId"/>"><s:text name="button.groupProduct"/></a>&nbsp;
					</s:if>
				</div>	
			</li>
			</s:iterator>
		</ul>
	</div>
	<p><input type="submit" name="buttons" value='<s:text name="button.saveOrder"/>' disabled/></p>
</form>