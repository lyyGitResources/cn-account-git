<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="menu.orderTitle" /></title>
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
				isList: true,
				background: "#ffffff",
				listId: "listOrder",	// 排序列表父标签ID
				buttonName: "buttons",	// 完成排序和OK按钮名称
				form: "#orderForm",		// 表单
				numberName: "[name='number']", // 序号位置
				total: <s:property value="result.listResult.list.size"/>, // 判断输入序号是否太大
				pageNo: 0,				// 0表示不分页
				pageSize: 0,			
				url: null				// 产品排序使用
			}
	
		</script>
	</head>
	<body>
		<div class="pageTips">
			<s:text name="order.tip" />：<br/>
			♦  <s:text name="order.notice" ><s:param>菜单信息</s:param></s:text>：<br/>
			    1、<s:text name="order.notice1" ><s:param>菜单信息</s:param></s:text><br/>
			    2、<s:text name="order.notice2" ><s:param>菜单信息</s:param></s:text>
		</div>
		
		<form action="/menu/menu_order_submit.htm" id="orderForm" method="post">
			<input type="hidden" id="_oldOrder"/>
			<input type="hidden" name="groupId" value="<s:property value="groupId"/>" />
			
			<p>
				<input type="submit" name="buttons" value='<s:text name="button.saveOrder"/>' disabled/>
				<input type="button" onclick="window.location.href='/menu/menu_list.htm?groupId=<s:property value="groupId"/>'" value="<s:text name='menu.list' />"/>
			</p>
			
			<div class="orderList">
				<div class="title">
					<div style="width:100px;"><s:text name="serialNumber"/></div>
					<div style="width:503px;"><s:text name="menu.title" /></div>
					<div style="width:130px;"><s:text name="modifyTime"/></div>
				</div>
			
				<ul id="listOrder">
					<s:iterator value="result.listResult.list" id="menu" status="st">
						<li itemid='<s:property value="#st.count"/>' title="按鼠标左键拖拽选中项">
							<input type="hidden" name="menuIds" value='<s:property value="#menu.menuId"/>'>
							
							<div style="width:100px;" name="serialDiv" id="<s:property value="#menu.menuId"/>" >
								<span id="_newOrderDiv<s:property value="#menu.menuId"/>" name="_newOrderDiv">
									<input style="width:20px;" id="_newOrder<s:property value="#menu.menuId"/>" />
									<a href="#position" name="buttons" onclick="ToolMan._extend.setSortValue($('#_newOrder<s:property value="#menu.menuId"/>').val())">
										<s:text name='button.confirm' />
									</a>
								</span>
							
								<span id="_number<s:property value="#menu.menuId"/>" name="number" >
								</span>
							</div>
							<div style="width:503px;">
								<a href="/menu/menu_edit.htm?menuId=<s:property value='#menu.menuId' />">
									<s:property value="#menu.title" />
								</a>
							</div>
							<div style="width:130px;">
								<s:property value="#menu.modifyTime"/>
							</div>
						</li>
					</s:iterator>
				</ul>
			</div>
		</form>
		
	</body>
</html>	