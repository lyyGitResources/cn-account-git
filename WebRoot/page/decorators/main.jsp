<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/sitemesh-decorator" prefix="decorator"%>
<%@ taglib uri="/gb2big5-converter" prefix="gb2big5"%>
<%
	String sysBase = Config.getString("sys.base");
	String tradeshowBase = Config.getString("tradeshow.base");
	String sysLogo = sysBase+"/img/logo/logo-r.jpg";
	if(StringUtil.equalsIgnoreCase("true",Config.getString("isBig5"))){
		tradeshowBase = "http://tradeshow.big5."+Config.getString("sys.domain");
		sysLogo = sysBase+"/img/logo/big5-logo-r.jpg";
		sysBase = "http://big5."+Config.getString("sys.domain");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<gb2big5:Gb2Big5JspWrapper>
	<head>
		<title>海商网中文站用户后台管理系统</title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=7" />
		<link href="/css/css.css" type="text/css" rel="stylesheet" />
		<link type="image/x-icon" rel="Shortcut icon" href="<%= Config.getString("img.base") %>/img/ico/hisupplier.ico" />
		<link type="image/x-icon" rel="Bookmark" href="<%= Config.getString("img.base") %>/img/ico/hisupplier.ico" />
		<link type="text/css" rel="stylesheet" href="<%=Config.getString("img.base")%>/js/lib/ui/themes/base/ui.all.css" />
		<link type="text/css" rel="stylesheet" href="<%=Config.getString("img.base")%>/css/jquery.tooltip.css" />
		<script type="text/javascript">
			var HI_DOMAIN = "<%=Config.getString("sys.domain")%>";
		</script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/??lib/jquery.js,lib/jquery.cookie.js,lib/jquery.form.js,lib/jquery.bgiframe.js,lib/jquery.validate.all.js,lib/ui/ui.core.js,lib/ui/ui.tabs.js,lib/ui/ui.dialog.js,util.js,chat.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.checkform.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/swfupload/swfupload.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/swfupload/handlers.js"></script>
		
		<script type="text/javascript" src="/js/listtable.js?v=20120424"></script>
		<script type="text/javascript" src="/js/commons.js"></script>
		<decorator:head />
		<script type="text/javascript">
			$(function () {
				$("a").focus(function(){this.blur();});
				//解决风格网站顶部空的太多
				if($("meta[name='memoinfo']").attr("content") == undefined){
					$("#meta_memoinfo").hide();
				}
			});
		</script>
	</head>
	<body>
		<noscript>
			对不起，您的浏览器不支持JavaScript，请开启此功能或更换其他版本的浏览器！
			<style type="text/css">
				#page_{display:none;}
			</style>
		</noscript>
		<div id="page_">
			<%@ include file="/page/inc/header.jsp"%>
			<div class="page">
				<div class="left">
					<div class="menu">
						<ul>
							<li><a href="/product/product_add.htm">添加产品</a></li>
							<s:if test="loginUser.memberType == 2">
								<li><a href="/product/product_batch_add.htm">批量上传产品</a></li>
							</s:if>
							<li><a href="/trade/trade_add.htm">发布商情</a></li>
							
							<s:if test="loginUser.showGroup == true">
								<li><a href="/group/group_list.htm">分组</a></li>
							</s:if>
							
							<s:if test="loginUser.showFeatureProduct == true">
								<li><a href="/product/feature_product_list.htm">首页展台产品设置</a></li>
							</s:if>
							
							<li><a href="/image/image_list.htm">图库管理</a></li>
							
							<s:if test="loginUser.memberType == 2">
								<li><a href="/video/video_list.htm">视频管理</a></li>
							</s:if>
							
							<s:if test="loginUser.memberType == 2 && loginUser.admin">
								<li><a href="/member/user_list.htm">子帐号管理</a></li>
							</s:if>
							<li><a href="/message/form.htm">短信群发</a></li>
							<li class="new"><a href="/businesscentre/index.htm">商务中心</a></li>
						</ul>
					</div>
					<div class="serviceList" id="serviceList">
						<img src="/img/loading1.gif" />
						<br />
						<br />
						Loading...
					</div>
					<div style=" margin:0;padding:30px 10px 10px 10px;">
						为确保您的信息不被低水平浏览器拦截窃取，请使用以下浏览器
						<br /><br />
						<a target="_blank" href="http://windows.microsoft.com/zh-CN/internet-explorer/downloads/ie"><img src="/img/browser/browser-1.png" border="0"/></a>
						<a target="_blank" href="http://firefox.com.cn/"><img src="/img/browser/browser-2.png" border="0"/></a>
						<a target="_blank" href="http://www.apple.com.cn/safari/"><img src="/img/browser/browser-3.png" border="0"/></a>
						<a target="_blank" href="http://chrome.google.com"><img src="/img/browser/browser-4.png" border="0"/></a>
					</div>
				</div>
				<div class="right">
					<s:if test="currentMenu != 'home'">
						<h1><span id="titleText"><decorator:title /></span><span class="red" id="redText"></span></h1>
					</s:if>
					<s:if test="showTitle">
						<h1><span id="titleText"><decorator:title /></span></h1>
					</s:if>
					<s:if test="menuType==1">
					<h1><span id="titleText"><decorator:title /></span><span class="red" id="redText"></span></h1>
					</s:if>
					<s:if test="patentDeblocked">
						<h1><span id="titleText"><decorator:title /></span><span class="red" id="redText"></span></h1>
					</s:if>
					<div id="meta_memoinfo" style="color:#888888;line-height:20px;margin-top:5px;overflow:hidden;width:100%;">
						<decorator:getProperty property="meta.memoinfo"/>
					</div>
					<%@ include file="/page/inc/messages.jsp"%>
					<decorator:body />
				</div>
				<div style="clear: both;"></div>
			</div>
			<%@ include file="/page/inc/footer.jsp"%>
			<%@ include file="/page/inc/dialog.jsp"%>
			<%= com.hisupplier.cas.CASServlet.getTrackJS(request) %>		 
		</div>
 	</body>
 	</gb2big5:Gb2Big5JspWrapper>
</html>

