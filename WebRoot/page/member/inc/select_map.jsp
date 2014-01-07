<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>电子地图 - 查询坐标</title>
		<script type="text/javascript" src="/js/jquery/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="http://map.google.com/maps/api/js?sensor=false"></script>
		<script type="text/javascript" src="/js/map.js"></script>
	</head>
	<body>
		<div style=" background: #D5DDF3; font-size: 12px; padding: 5px;">
			<label for="address">请输入您要查询的地址</label> 
			<input type="text" id="address" style="width: 300px;" />
			<input type="button" id="search" value="查询" />
			<input type="button" id="submit" value="确定" />
		</div>
	  	<div id="map" style="width:100%;height:600px;"></div>
	</body>
</html>