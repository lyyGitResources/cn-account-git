<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>海商网-全国领先的B2B电子商务交易平台</title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=7" />
		<meta name="google-site-verification" content="cYTzERtXLNordUPWVefZcp3Imrf6wt1yVy0y_vvlw8I" />
		<meta name="keywords" content="海商网，网上贸易，B2B，B2B网站，B2B平台，电子商务，内贸，外贸，批发，网上交易，在线交易，买卖信息，贸易机会，商业信息，供求信息，采购，求购信息，供应信息，加工合作，商务服务，商务网站" />
		<meta name="description" content="海商网（cn.hisupplier.com）是中国（B2B）电子商务平台的知名网络品牌，汇集海量的产品供求信息，是中国商人销售产品、拓展市场及网络推广的首选网站。" />
		<link href="<%=Config.getString("sys.base") %>/css/pro_services.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div class="area">
			<div class="mainNav"><span class="T">您正在浏览：</span><a href="<%=Config.getString("sys.base") %>">首页</a>&nbsp;&gt;&nbsp;<span>立即申请</span></div>
			<div class="pro_serv_left_box">
				<div class="h1">立即申请</div>
				<ul>
					<li><a href="<%=Config.getString("sys.base") %>/productService.htm">产品服务</a></li>
					<li><a href="<%=Config.getString("sys.base") %>/payment.htm">付款方式</a></li>
					<li><a href="<%=Config.getString("sys.base") %>/webCreditManager.htm">网站信用管理制度</a></li>
					<li class="current"><a href="#">立即申请</a></li>
				</ul>
				<div class="left_banner">
					<img src = "<%=Config.getString("sys.base") %>/img/banner/leftBanner.gif" border="0"/>
				</div>
			</div>
			<div class="pro_serv_right_box">
				<div class="h2">立即申请</div>
				<div class="apply_now">
					公司名称：浙江海商网络科技有限公司（海商网）<br>
					电话：86-574-27702770<br>
					传真：86-574-28822992<br>
					地址：浙江省宁波市鄞州区鄞县大道中段1357号广博国贸中心11层<br>
					邮编：315100
				</div>
				<div class="h3"><strong>在线申请</strong> (<span class="xing">*</span>为必填项)</div>
				<div style="color:red;margin:10px auto;">
					<s:actionerror/>
					<s:actionmessage/>
				</div>
				<form id="applyForm" name="applyForm" method="post" action="/user/product_apply_submit.htm">
					<table cellspacing="0" cellpadding="0" border="0" class="apply_now_table">
						<tr>
							<td colspan="2"><div class="tips">您也可以填写以下表单申请海商网国际站服务，我们会尽快与您联系！</div></td>
						</tr>
						<tr>
							<th><span class="xing">*</span>您的姓名：</th>
							<td>
								<label>
									<input name="contact" id="contact" style="border: 1px solid rgb(195, 209, 222);" value="<s:property value="contact"/>"/>
									<input type="radio" name="sex" value="1" <s:if test="sex == 1">checked="checked"</s:if> />&nbsp;先生
									<input type="radio" name="sex" value="2" <s:if test="sex == 2">checked="checked"</s:if>/>&nbsp;女士
									<input type="radio" name="sex" value="3" <s:if test="sex == 3">checked="checked"</s:if>/>&nbsp;小姐
								</label>
							</td>
						</tr>
						<tr>
							<th><span class="xing">*</span>公司名称：</th>
							<td><input type="text" name="comName" id="comName" value="<s:property value="comName"/>" style="width: 240px; border: 1px solid rgb(195, 209, 222);"></td>
						</tr>
						<tr>
							<th><span class="xing">*</span>详细地址：</th>
							<td><input type="text" name="address" id="address" value="<s:property value="address"/>" style="width: 240px; border: 1px solid rgb(195, 209, 222);"></td>
						</tr>
						<tr>
							<th><span class="xing">*</span>联系电话：</th>
							<td><input type="text"  name="tel" id="tel" value="<s:property value="tel"/>" style="width: 240px; border: 1px solid rgb(195, 209, 222);"></td>
						</tr>
						<tr>
							<th><span class="xing">*</span>电子邮箱：</th>
							<td><input name="email" id="email" value="<s:property value="email"/>" style="width: 240px; border: 1px solid rgb(195, 209, 222);"></td>
						</tr>
						<tr>
							<th>公司网站：</th>
							<td><input type="text" name="website" id="website"  value="<s:property value="website"/>" style="width: 240px; border: 1px solid rgb(195, 209, 222);"></td>
						</tr>
						<tr>
							<th>补充说明：</th>
							<td>
								<label>
							        <textarea id="supplement" name="supplement" style="width: 240px; height: 150px; border: 1px solid rgb(195, 209, 222);"><s:property value="supplement"/></textarea>
						        </label>
							</td>
						</tr>
						<tr>
							<th>&nbsp;</th>
							<td>
								<label>
							        <input type="submit" class="submit" value="" name="Submit">
								</label>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</body>
</html>