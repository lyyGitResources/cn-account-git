<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>迎客宝</title>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#dialog1").dialog({
					autoOpen: false,
					bgiframe: true,
					height:1020,
					width:920
				});
				//$("#dialog").dialog("open");
				$("#reg").click(function(){
					$("#dialog1").dialog("open");
				});
			})
		</script>
	</head>

	<body>
		<div id="meta_memoinfo"
			style="color: #888888; line-height: 20px; margin-top: 5px; overflow: hidden; width: 100%;">
		</div>
		<style>
<!--
.tips2 {
	font-size: 14px;
	color: #CC0000;
	background: url(http://cn.jiaming.com/img/ico/ico_22.gif) no-repeat 0
		10px;
	padding-left: 35px;
	line-height: 50px;
	margin: 0px 0 10px 200px;
}
-->
</style>
		<div class="ykb_box">
			<div class="ykb_top">
				<img src="/img/ykb.jpg" border="0" />
			</div>
			<div class="ykb_about">
				<a href="http://downloads.youshang.com/ykb2/ykb.exe" target="_blank" class="button"></a>
				<input id="reg" name="" type="button" class="reg_button" />
				<span>&nbsp;&nbsp;&nbsp;&nbsp;迎客宝是一款融合了管理软件行业、互联网行业、通讯服务行业特性优势于一体的互联网营销服务平台。实现实际营销工作中业务与客服的网络信息协同管理化，提高管理效能。充分运用互联网客户数据挖掘技术，让您随时随地按需获取最适合的潜在客户名录。整合邮件、短信、传真、电话和呼叫中心等多种通讯技术服务，快速高效实现业务拓展，有效提升客户服务水平。</span>
			</div>
			<div class="ykb_i">
				<div class="ykb_list_box">
					<div class="ykb_list">
						<img src="/img/ykb_04.jpg" width="167" height="72" />
						<div>
							管理软件
						</div>
						<div>
							互联网
						</div>
						<div>
							通讯服务
						</div>
					</div>
					<div class="ykb_list">
						<img src="/img/ykb_05.jpg" width="167" height="72" />
						<div>
							管理软件
						</div>
						<div>
							互联网
						</div>
						<div>
							通讯服务
						</div>
					</div>
					<div class="ykb_list">
						<img src="/img/ykb_06.jpg" width="167" height="72" />
						<div>
							管理软件
						</div>
						<div>
							互联网
						</div>
						<div>
							通讯服务
						</div>
					</div>
					<div class="ykb_list">
						<img src="/img/ykb_07.jpg" width="167" height="72" />
						<div>
							管理软件
						</div>
						<div>
							互联网
						</div>
						<div>
							通讯服务
						</div>
					</div>
				</div>
			</div>
		</div>
	<div id="dialog1" title="注册">
   		<div class="infor_right">
			<iframe class="yingkebo_ifoame" allowTransparency="true" frameborder="no" scrolling="no" src="http://assistant.youshang.com/common/register/register_ykb.jsp"></iframe>
			<div class="service_card"><div class="box"><span>联系人：Kitty  电话：0574-27901060  邮箱：Service10@hi.cc</span><b>索取服务卡</b></div></div>
			<iframe frameborder="no" scrolling="no" class="blank_iframe"></iframe>
		</div>
   </div>
	</body>
</html>
