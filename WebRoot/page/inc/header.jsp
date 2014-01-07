<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ page import="com.hisupplier.commons.Config" %>
<script type="text/javascript">
<!--
	$(function(){
		$(".topSubmenu > div > li").hover(
			function () {
				$(this).addClass("mousehover");
			},
			function () {
				$(this).removeClass("mousehover");
			}
		);
		
		$("#menuGroupFormDialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 260,
			width: 560,
			modal: true,
			close: function(){
				$("#menuGroupFormDialog").empty();
			}
		});
		
		// 子帐号登录，二级菜单位置自动生成
		<s:if test="loginUser.admin == false">
			// 左边空白位置长度
			var positionBlank = $(".topMenu").position(); 
			var position = $(".topMenu > ul > li.current").position();
			$(".topSubmenu > div").css("margin-left", (position.left - positionBlank.left));
		</s:if>
	});
	
	function showFormDialog (url,title) {
		$("#menuGroupFormDialog").html(AJAX_LOADING_CODE);
		$("#menuGroupFormDialog").load(url,{random:Math.random()});
		$("#menuGroupFormDialog").dialog('option', 'title', title);
		$("#menuGroupFormDialog").dialog('open');
	}
	
	<%-- 
	//调用CRM
	function gotoCRM(){
		$.ajax({
			  type: "GET",
			  url: "/plugin/gotoCRM.do",
			  dataType: "json",
			  success: function(response){
	     			if(response.tip == "success"){
	     				window.open(response.result.loginUrl);
	     			}else{
	     				alert(response.msg);
	     			}
	  			}
		});
	}
	--%>
//-->
</script>
<div class="top">
	<div class="topTips">
		<span>
			<s:if test="loginUser.domainEN != ''">
				<a href="http://account.<%=Config.getString("sys.domain") %>">英文版后台</a>&nbsp;|&nbsp;
			</s:if>
			<a href="/logout">退出</a>&nbsp;|&nbsp;
			<a href="http://about.hisupplier.com" target="_blank">关于海商</a>&nbsp;|&nbsp;
			<a href="http://help.cn.hisupplier.com" target="_blank">帮助中心</a>
			<s:if test="loginUser.statSite != null && loginUser.statSite != ''">
			|&nbsp;<a href="<s:property value="loginUser.statSite"/>" target="_blank">流量统计</a>
			</s:if>
		</span>
		<img src="/img/gb2Big5.jpg" border="0" usemap="#Map" style="margin-bottom:-2px; !important;margin-top:0px !important;*margin-top:5px;" />	
		您好：<s:property value="loginUser.contact"/>，欢迎来到海商网中文站用户后台管理系统！
		<map name="Map" id="Map">
    		<area shape="rect" coords="0,0,26,11" href="<%=Config.getString("account.base") %>" />
    		<area shape="rect" coords="41,0,65,11" href=<%="http://account.big5."+Config.getString("sys.domain") %> />
		</map>			
	</div>
</div>
<div class="header">
	<div class="logo">
		<div class="left">
			<a href="<%=sysBase%>"><img src="<%=sysLogo %>"/></a>
		</div>
		<div class="right">
			<ul>
				<li><a href="<%=sysBase%>">首页</a></li>
				<li><a href="<%=sysBase%>/product/">找供应</a></li>
				<li><a href="<%=sysBase%>/buy/">找求购</a></li>
				<li><a href="<%=tradeshowBase%>">找展会</a></li>
			</ul>
		</div>
	</div>
	<div class="topMenu">
		<ul> 
			<li <s:if test="currentMenu == 'home' || currentMenu == 'patentDeblocked' ">class="current"</s:if> ><a href="/">后台首页</a></li>
			<li <s:if test="currentMenu == 'member'">class="current"</s:if> ><a href="/member/company_edit.htm">会员信息</a></li>
			<s:if test="loginUser.showGroup == true">
				<li <s:if test="currentMenu == 'group'">class="current"</s:if> ><a href="/group/group_list.htm">分组</a></li>
			</s:if>
			<li <s:if test="currentMenu == 'product'">class="current"</s:if> ><a href="/product/product_list.htm?showReject=true">产品</a></li>
			<li <s:if test="currentMenu == 'trade'">class="current"</s:if> ><a href="/trade/trade_list.htm?showReject=true">商情</a></li>
			<s:if test="loginUser.showMenu == true">
				<li <s:if test="currentMenu == 'menu' ">class="current"</s:if> ><a href="/menu/menu_group_list.htm">自定义菜单</a></li>
			</s:if>
			<s:if test="loginUser.showAd == true">
				<li <s:if test="currentMenu == 'ad'">class="current"</s:if> ><a href="<s:if test="loginUser.memberType == 1">/ad/upgrade.htm</s:if><s:else>/ad/ad_order.htm</s:else>">增值服务</a></li>
			</s:if>
			<s:if test="loginUser.showWebsite == true">
				<s:if test="loginUser.domain != null && loginUser.domain != ''">
					<li <s:if test="currentMenu == 'website'">class="current"</s:if> ><a href="/website/design.htm">网站设计</a></li>
				</s:if>
				<s:else>
					<li <s:if test="currentMenu == 'website'">class="current"</s:if> ><a href="/website/website_set.htm">网站设计</a></li>
				</s:else>
			</s:if>
			<li <s:if test="currentMenu == 'inquiry'">class="current"</s:if> >
				<a href="/inquiry/inquiry_list.htm?recommend=0" id="inqueryHref">询盘</a>
				<div id="newInquiry" style="margin-top:-40px;margin-left:45px;position: absolute;"></div>
			</li>
			<s:if test="loginUser.showAlert ==true">
				<li <s:if test="currentMenu == 'alert'">class="current"</s:if> ><a href="/alert/trade_alert_list.htm">订阅</a></li>
			</s:if>
			
			<s:set name="previewUrl" value="previewUrl" />
			<s:if test="#previewUrl.size > 1">
				<li onmouseover="$('#previewUrl').show()" onmouseout="$('#previewUrl').hide()">
					<a href="<s:property value='#previewUrl.showroomAllUrl' escape='false'/>" target="_blank"><s:text name="home.preview" /></a>
					<div id="previewUrl" class="previewDown">
						<a href="<s:property value='#previewUrl.showroomAllUrl' escape='false'/>" target="_blank"><s:property value='#previewUrl.showroomUrl' escape='false'/></a>
						<a href="<s:property value='#previewUrl.websiteAllUrl' escape='false'/>" target="_blank"><s:property value='#previewUrl.websiteUrl' escape='false'/></a>
					</div>
				</li>		
			</s:if>
			<s:elseif test="#previewUrl.size > 0 && #previewUrl.showroomUrl != null">
				<li>
					<a href="<s:property value='#previewUrl.showroomAllUrl' escape='false'/>" target="_blank"><s:text name="home.preview" /></a>
				</li>
			</s:elseif>
		</ul>
	</div>
	<div class="topSubmenu">
		<s:if test="currentMenu == 'home' || currentMenu == 'patentDeblocked'">
			<div style="margin-left:40px;">
				<li><a href="/basic/service_mail.htm">客服热线</a></li>
				<li><a href="/basic/user_suggest.htm">意见建议</a></li>
				<li><a href="/image/image_list.htm">图库管理</a></li>
				<%--
				<li><a href="javascript:gotoCRM();" id="crm" style="color: #FFF114;">在线客户管理</a></li>
				 --%>
				<li><a href="http://about.hisupplier.com" target="_blank">关于海商</a></li>
				<li><a href="/patent/patentDeblocked.htm" style="color: #FFF114;">违规词解禁申请</a></li>
				<s:if test="commentCount > 0">
				<li><a href="/comment/comment_list.htm">评论</a></li>
				</s:if>
				<%--
				<s:if test="result.user.admin">
				<s:if test="result.containsKey('vote') &&  result.vote > 0"><li><a href="/vote/vote_list.htm">投票</a></li></s:if>
			    <s:if test="result.containsKey('comment') && result.comment > 0">
			    <li><a href="/comment/comment_list.htm">评论</a></li>
			    </s:if>
				</s:if>
				 --%>
			</div>
		</s:if>
		<s:elseif test="currentMenu == 'member'">
			<div style="margin-left:40px;">
				<li><a href="/member/company_edit.htm">公司信息</a></li>
				<li><a href="/member/contact_view.htm"><s:if test="loginUser.admin">公司联系人</s:if><s:else>我的帐号信息</s:else> </a></li>
				<s:if test="loginUser.memberType == 2 && loginUser.admin">
					<li><a href="/member/user_list.htm">子帐号管理</a></li>
				</s:if>
				<li><a href="/member/passwd_edit.htm">修改密码</a></li>
				<li><a href="/member/profile.htm">企业身份认证</a></li>
			</div>
		</s:elseif>
		<s:elseif test="currentMenu == 'group' && loginUser.showGroup == true">
			<div style="margin-left:200px;">
				<li><a href="/group/group_list.htm">普通分组</a></li>
				<li><a href="/specialGroup/group_list.htm">特殊分组</a></li>
			</div>
		</s:elseif>
		<s:elseif test="currentMenu == 'product'">
			<div style="margin-left:90px;">
				<li><a href="/product/product_add.htm">添加产品</a></li>
				<li><a href="/product/product_list.htm?showReject=true">产品管理</a></li>
				<s:if test="loginUser.showProductOrder">
					<li><a href="/product/product_order.htm?pageSize=50">产品排序</a></li>
				</s:if>
				<s:if test="loginUser.showNewProduct">
					<s:if test="loginUser.memberType > 1">
						<li>
							<a href="/newproduct/new_product_list.htm">加密产品</a>
							<ul>
								<li><a href="/newproduct/new_product_add.htm">添加加密产品</a></li>
								<li><a href="/newproduct/new_product_list.htm">加密产品管理</a></li>
								<li><a href="/newproduct/new_product_set.htm">加密产品设置</a></li>
							</ul>
						</li>
					</s:if>
				</s:if>
				<s:if test="loginUser.showFeatureProduct">
				<li><a href="/product/feature_product_list.htm">首页展台产品设置</a></li>
				</s:if>
				<li><a href="/product/product_recycle_list.htm">产品回收站<s:if test="loginUser.admin == true">(<span id="productDelCount"></span>)</s:if></a></li>
			</div>
		
		</s:elseif>
		<s:elseif test="currentMenu == 'trade'">
			<div style="margin-left:280px;">
				<li><a href="/trade/trade_add.htm">发布商情</a></li>
				<li><a href="/trade/trade_list.htm?showReject=true">商情管理</a></li>
				<li><a href="/trade/trade_recycle_list.htm">商情回收站<s:if test="loginUser.admin == true">(<span id="tradeDelCount"></span>)</s:if></a></li>
			</div>
		</s:elseif>
		<s:elseif test="currentMenu == 'ad' && loginUser.showAd == true">
			<div style="margin-left:500px;">
				<s:if test="loginUser.memberType == 1">
					<li><a href="/ad/upgrade.htm">会员升级</a></li>
				</s:if>
				<li>
					<a href="/ad/ad_order.htm" >广告服务</a>
					<ul>
						<li><a href="/ad/ad_order.htm">广告申请</a></li>
						<li><a href="/ad/ad_order_list.htm">广告申请列表</a></li>
					</ul>
		 		</li>
				<li>
					<a href="/ad/top_list.htm" >TopSite服务</a>
					<ul>
						<li><a href="/ad/top_list.htm">TopSite服务列表</a></li>
						<li><a href="/ad/top_order.htm">TopSite订购</a></li>
						<li><a href="/ad/top_order_list.htm">TopSite订购列表</a></li>
					</ul>
				</li>
			</div>
		</s:elseif>
		<s:elseif test="currentMenu == 'inquiry'">
			<div style="margin-left:580px;">
				<li><a href="/inquiry/inquiry_list.htm?recommend=0">询盘管理</a></li>
				<li><a href="/inquiry/inquiry_chart.htm">询盘报表</a></li>
				<li><a href="/inquiry/inquiry_download.htm">询盘下载</a></li>
				<s:if test="loginUser.admin && loginUser.memberType == 2">
				<li><a href="/inquiry/inquiry_set.htm">询盘设置</a></li>
				</s:if>
				<s:if test="loginUser.admin">
				<li><a href="/inquiry/inquiry_recycle_list.htm">询盘回收站(<span id="inquiryDelCount"></span>)</a></li>
				</s:if>
			</div>
		</s:elseif>
		
		<s:elseif test="currentMenu == 'alert' && loginUser.showAlert == true">
			<div style="margin-left:740px;">
				<li><a href="/alert/trade_alert_list.htm">订阅管理</a></li>
				<li><a href="/alert/trade_alert_add.htm">订阅商情</a></li>
			</div>
		</s:elseif>	
		
		<s:elseif test="currentMenu == 'menu' && loginUser.showMenu == true">
			<div style="margin-left:380px;">
				<li><a href="javascript:showFormDialog('/menu/menu_group_add.do','添加菜单')">添加菜单</a></li>
				<li><a href="/menu/menu_group_list.htm">菜单管理</a></li>
				<li><a href="/menu/menu_group_order.htm">菜单排序</a></li>
			</div>		
		</s:elseif>	
		
		<s:elseif test="currentMenu == 'website'&& loginUser.showWebsite == true">
			<s:if test="loginUser.domain != null && loginUser.domain != ''">
				<div style="margin-left:600px;">
					<li><a href="/website/design.htm">网站设计</a></li>
					<li><a href="/website/website_set.htm">网站设置</a></li>
<!-- 					<li><a href="/website/add_other_form.htm">添加其他版本的网站</a></li> -->
				</div>	
			</s:if>
			<s:else>
				<div style="margin-left:628px;">
					<li><a href="/website/website_set.htm">网站设置</a></li>
				</div>	
			</s:else>
		</s:elseif>	
	</div>
	<div id="menuGroupFormDialog"></div>
</div>
