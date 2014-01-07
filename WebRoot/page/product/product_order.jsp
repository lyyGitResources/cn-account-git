<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			<s:text name="product.orderTitle" />
			<span class="red">
				（<s:if test="groupId > 0"><s:text name='button.orderInGroup' /></s:if><s:else><s:text name='button.orderAll' /></s:else>）
			</span>
		</title>
		<script type="text/javascript" src="/js/drag/core.js"></script>
	    <script type="text/javascript" src="/js/drag/events.js"></script>
	    <script type="text/javascript" src="/js/drag/css.js"></script>
	    <script type="text/javascript" src="/js/drag/coordinates.js"></script>
	    <script type="text/javascript" src="/js/drag/drag.js"></script>
	    <script type="text/javascript" src="/js/drag/dragsort.js"></script>
	    <script type="text/javascript" src="/js/drag/cookies.js"></script>
	    <script type="text/javascript">
	    	function changepageSize(){
	    		var pageSize = $("#pageSize").val();
	    		if(!Util.isInt(pageSize) || pageSize <= 0 && pageSize % 5 != 0){
	    			alert("请输入是5倍数的正整数！");
	    		}else{
	    			var groupId = $("#groupId").val();
	    			if(groupId > 0){
	    				window.location.href = "/product/product_order.htm?groupId=" + groupId + "&pageSize=" + pageSize;
	    			}else{
	    				window.location.href = "/product/product_order.htm?pageSize=" + pageSize;
	    			}
	    		}
	    	}
	    </script>
	</head>
	<body>
		<div class="pageTips">
			<s:text name="order.tip" />：<br/>
			♦  <s:text name="order.noticeOne" />：<br/>
			   1、<s:text name="order.noticeOne1" /><br/>
			   2、<s:text name="order.noticeOne2" /><br/>
			♦  <s:text name="order.noticeTwo" /><br/>
			♦  <s:text name="order.noticeThree" />
		</div>
		<form action="/product/product_order_submit.htm" id="orderForm" method="post">
			<input type="hidden" id="groupId" name="groupId" value="<s:property value='groupId'/>"/>
			<input type="hidden" name="pageSize" value="<s:property value='pageSize'/>"/>
			<input type="hidden" id="_oldOrder"/>
			<div class="button1">
				<input type="submit" name="buttons" style="float:left" value="<s:text name='button.saveOrder'/>" disabled />
				<input type="button" value="<s:text name='button.orderInGroup' />" onclick="location.href='/group/group_order.htm?parentId=0'" style="float:right" <s:if test="groupId > 0">disabled</s:if> />
				<input type="button" value="<s:text name='button.orderAll' />" onclick="location.href='/product/product_order.htm?pageSize=50'" style="float:right" <s:if test="0 >= groupId">disabled</s:if>/>
			</div>
				
			<div id="listTable">
				<s:include value="/page/product/product_order_inc.jsp"/>
			</div>
			
			<div class="button1">
				<input type="submit" name="buttons" style="float:left" value="<s:text name='button.saveOrder'/>" disabled />
				<div style="float: right">
					每页显示
					<input value="<s:property value='pageSize'/>" id="pageSize" style="width: 30px;"/> <input type="button" value="确定" onclick="changepageSize();"/>
				</div>
			</div>
		</form>
	</body>
</html>