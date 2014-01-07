<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ page import="com.hisupplier.cas.CASClient"%>
<%@ page import="com.hisupplier.commons.basket.*"%>
<%@page import="com.hisupplier.commons.util.StringUtil"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/sitemesh-decorator" prefix="decorator"%>
<%@ taglib uri="/gb2big5-converter" prefix="gb2big5"%>

	<%
		String accountBase = Config.getString("account.base");
		String tradeshowBase = Config.getString("tradeshow.base");
		String trainBase = "http://train.cn."+Config.getString("sys.domain");
		String sysBase = Config.getString("sys.base");
		String sysLogo = sysBase+"/img/logo/logo-r.jpg";
		if(StringUtil.equalsIgnoreCase("true",Config.getString("isBig5"))){
			accountBase = "http://account.big5."+Config.getString("sys.domain");
			tradeshowBase = "http://tradeshow.big5."+Config.getString("sys.domain");
			sysBase = "http://big5."+Config.getString("sys.domain");
			sysLogo = sysBase+"/img/logo/big5-logo-r.jpg";
			trainBase = "http://train.big5."+Config.getString("sys.domain");
		}
	%>
<gb2big5:Gb2Big5JspWrapper>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><decorator:title /></title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=7" />
		<link href="<%=sysBase %>/css/css.css" rel="stylesheet" type="text/css" />
		<link href="<%=sysBase %>/css/other.css" rel="stylesheet" type="text/css" />
		<link href="/css/user/link.css" rel="stylesheet" type="text/css" />
		<link type="image/x-icon" rel="Shortcut icon" href="<%= Config.getString("img.base") %>/img/ico/hisupplier.ico" />
		<link type="image/x-icon" rel="Bookmark" href="<%= Config.getString("img.base") %>/img/ico/hisupplier.ico" />
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.validate.messages_zh.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.validate.extends.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/util.js"></script>
		<script type="text/javascript" src="<%=Config.getString("sys.base")%>/js/search.js"></script>
		<script type="text/javascript" src="/js/user.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.select.js"></script>
		<script type="text/javascript">
			var HI_DOMAIN = "<%=Config.getString("sys.domain")%>";
			$(function () {
				$("#topQueryText").searchSuggest({
					queryById: "topQueryBy"
				});
				$('#tipText').html('请输入您感兴趣的产品关键词');
				var tmpTopQueryText=$("#topQueryText").val();
				
				if(tmpTopQueryText != ''){
					$("#tipText").css("visibility","hidden");
				};
				$("#topQueryText").focus(function(){
					$("#tipText").css("visibility","hidden");
				})
				.blur(function(){
					if($(this).val() == ''){
						$("#tipText").css("visibility",'visible');
					}
				});
				$("a").focus(function(){this.blur();});
				$("#hiService").hover(
					function(){
				  		$("#hiService").addClass("current");	
				  		$('#hiServiceChildren').show();			
						
					},
					function(){
						$("#hiService").removeClass("current");		
				  		$('#hiServiceChildren').hide();
					}				
				);
				$("#hixun").hover(
					function(){
				  		$("#hixun").addClass("current");	
				  		$('#hixunChildren').show();			
						
					},
					function(){
						$("#hixun").removeClass("current");		
				  		$('#hixunChildren').hide();
					}				
				);
			});
		</script>
		<decorator:head />
	</head>
	<body>
		<div id="header">
		    <div class="topTips">
		        <ul class="quickLink">
		            <li><a onclick="javascript:Util.setHome(this);" class="f3" href="#position">设为主页</a></li>
		            <li><a href="javascript:Util.addBookMark();">添加收藏</a></li>
		            <li><a href="<%=Config.getString("sys.base") %>/sitemap.html">网站导航</a></li>
		            <li class="hiService" id="hiService">
		            	<a href="#">海商服务</a>
		                <div class="serviceList" id="hiServiceChildren" style="display:none;">
							<a href="http://www.hisupplier.com/exporter/index.html"  target="_blank">出口服务</a><br />
		                    <a href="<%=accountBase %>/user/post_buy_lead.htm" target="_blank">免费发布求购</a><br />
		                    <a href="<%=accountBase %>/user/trade_alert_step1.htm" target="_blank">订阅商情信息</a>
						</div>
		            </li>
		        </ul>
		        <ul class="quickMenu">
		            <li class="english"><a  href="http://www.hisupplier.com" target="_blank">国际贸易</a></li>
					<%if(CASClient.getInstance().isLogin(request)){ %>
					<li>
					您好，<%=CASClient.getInstance().getUser(request).getContact() %>！
					<a href="<%=sysBase %>/logout">退出</a>
					</li>
					<%}else{ %>
			            <li><a  href="<%=accountBase %>">登录</a></li>
			            <li><a  href="<%=accountBase %>/user/join.htm">免费注册</a></li>
					<%} %>
		            <li>
						<%
							Basket basket = BasketFactory.getBasket(request); 
							if( basket.getCompanySize() > 0 ){%>
							<a href="<%=accountBase%>/user/inquiry_basket.htm">询盘篮</a><b>(<%=basket.getCompanySize()%>)</b>
						<% }else{ %>
							询盘篮		
						<% } %>
					</li>
		            <li><a  href="http://help.cn.hisupplier.com" target="_blank">帮助中心</a></li>
		        </ul>
		    </div>
		    <div class="topToolBar">
		        <div class="topLogo">
					<a href="<%=sysBase %>"><img border="0" src="<%=sysLogo %>"/></a><img src="<%=sysBase %>/img/logo/hi_logo.png" border="0" usemap="#Map"/>
		            <map name="Map" id="Map">
		                <area shape="rect" coords="10,50,50,65" href="<%=Config.getString("sys.base") %>" />
		                <area shape="rect" coords="51,50,88,70" href=<%="http://big5."+Config.getString("sys.domain") %> />
		            </map>
		        </div>
				<table border="0" cellspacing="0" cellpadding="0" class="h1">
					<tr>
						<td valign="bottom">中国制造商，供应商，贸易进出口，工厂，贸易公司，采购商，供求信息，贸易机会</td>
					</tr>
				</table>
		        <form id="topSearchForm" name="searchForm" autocomplete="off" method="get">
					<input type="hidden" name="domain" value="<%=sysBase %>"/>
			        <div class="searchBox">
			            <div class="l"></div>
			            <div class="r"></div>
			            <div class="formBox">
			                <div id="searchDiv" class="dropdown">
			                    <select id="topQueryBy">
									<option value="product" selected>产品库</option>
									<option value="sell">供应库</option>
									<option value="buy">求购库</option>
									<option value="company">公司库</option>
									<!-- ‘展会信息’、‘培训信息’如果修改，需相应修改search.js中的changeSearchType方法 -->
									<option value="tradeshow">展会信息</option>
<!--									<option value="train">培训信息</option>-->
			                    </select>
			                </div>
			                <script type="text/javascript">
			                	$("#searchDiv").sSelect();
			                </script>
			            </div>
			            <div class="formBox formBox2">
			          		<label id="tipText" for="topQueryText"></label>
							<input type="text" id="topQueryText" name="queryText"/>
			            </div>
			            <div class="formBox">
			                <input type="submit" class="submit" value="找一下"/>
			            </div>
<!--						<div class="formBox formBox3"><a href="#">热门产品</a></div>-->
		        	</div>
		        </form>
		    </div>
		    <div class="hiMenu">
		        <div class="l"></div>
		        <div class="r"></div>
		        <ul>
		            <li><a href="<%=sysBase %>">首页</a></li>
					<li class="chinaPro"><a href="<%=sysBase %>/product/">中国产品目录</a></li>
		            <li><a href="<%=sysBase %>/company/">找公司</a></li>
		            <li><a href="<%=sysBase %>/trade-list/">找商机</a></li>
		            <li><a href="<%=tradeshowBase%>">找展会</a></li>
<%--		            <li><a href="<%=trainBase %>">找培训</a></li>--%>
		            <li class="myHi"><a href="<%=accountBase%>"><img border="0" src="<%=sysBase %>/img/bg/homeBg_16.gif"/></a></li>
		        </ul>
		    </div> 
		</div>
		
		<!-- 主体 -->
		<div class="area">
		<div class="mainNav2"><span class="T">您正在浏览：</span><a href="<%=sysBase %>">首页</a> &gt; <span><decorator:title /></span></div>
			<div class="linkLeft">
				<ul>
					<li>
						<h2><a href="<%=sysBase%>/friend_link.html">友情链接</a></h2>
					</li>
					<li <s:if test="currentMenu == 'friendLink'">class="current"</s:if>>
						<h1><a href="/user/friend_link.htm">交换链接</a></h1>
					</li>
					<li>
						<h2><a href="http://about.hisupplier.com/">关于我们</a></h2>
					</li>
					<li <s:if test="currentMenu == 'contactUs'">class="current"</s:if>>
						<h2><a href="/user/contact_us.htm">联系我们</a></h2>
					</li>
					<li <s:if test="currentMenu == 'categorySuggest'">class="current"</s:if>>
						<h2 ><a href="/user/category_suggest.htm">目录建议</a></h2>
					</li>
				</ul>
			</div>
			
			<div class="linkRight">
				<decorator:body />
			</div>
			<div style="overflow: hidden; height: 10px; width:740px; float:left;"></div>
		</div>
		
		<!-- 底部 -->
		<%@ include file="/page/inc/footer.jsp"%>
		<%-- 
		<script type="text/javascript">
			var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
			document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
		<script type="text/javascript">
			try {
				var pageTracker = _gat._getTracker("UA-5167365-6");
				pageTracker._setDomainName(".hisupplier.com");
				pageTracker._trackPageview();
			} catch(err) {}
		</script>
		--%>
	</body>
</html>
</gb2big5:Gb2Big5JspWrapper>