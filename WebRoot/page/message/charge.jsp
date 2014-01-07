<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/error/500.jsp"%>
<%@ page import="com.opensymphony.xwork2.util.*"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>短信群发</title>
	</head>
	<body>
		
				<div class="SMSbox">
				 <div class="tabMenu">
					<ul>
						<li>
							<span onclick="location.href='/message/form.htm'">发送短消息</span>
						</li>
						<li>
							<span onclick="location.href='/message/messageLog.htm'">已发短信</span>
						</li>
						<li>
							<span onclick="location.href='/message/phoneBook.htm'">电话簿</span>
						</li>
						<li>
							<span onclick="location.href='/message/template.htm'">常用短语</span>
						</li>
						<li>
							<span onclick="location.href='/message/chargeLog.htm'">充值记录</span>
						</li>
						<li class="current">
							<span onclick="location.href='/message/charge.htm'">充值</span>
						</li>
					</ul>
				</div>
				    <div class="balance"></div>
					<!-- <div class="recharge">
					<h1>方式一：网银支付</h1>
					<div class="main">
						支持国内主流银行发行的借记卡、信用卡以及VISA国际信用卡。
					            <br />
					            <br />
					            <a href="#"><img src="../images/button_01.gif" width="102" height="29" border="0" /></a></div>
					</div>
					 -->
				    <div class="recharge">
				         <h1>银行汇款或转账</h1>
				         <div class="main">公司全称：浙江海商网络科技有限公司<br />
				           账号：574903512910401<br />
				           开户行：招商银行股份有限公司宁波鄞州支行</div>
				   </div>
			      <table width="100%" border="0" cellpadding="2" cellspacing="1" class="tableRank" style="width:300px;">
			        <tr>
			          <th colspan="2" style="text-align:left; padding-left:20px;">短信价格：</th>
			          </tr>
			        <tr>
			          <td width="350" style=" line-height:20px;text-align:left; padding-left:20px;">充值1000元以下</td>
			          <td width="133">0.09元/条</td>
			          </tr>
			        <tr>
			          <td class="tdBg"  style=" line-height:20px;text-align:left; padding-left:20px;">充值1000元（含1000元）- 2000元</td>
			          <td class="tdBg">0.08元/条</td>
			          </tr>
			        <tr>
			          <td  style=" line-height:20px;text-align:left; padding-left:20px;">充值2000元以上（含2000元）</td>
			          <td>0.07元/条</td>
			          </tr>
			      </table><br />
			      	1、汇款单请注明您的企业名称和帐号名，请将回执单传真至0574-2882 2992。 <br />
					2、如果您在汇款后2个工作日仍然没有任何反馈，请与我们联系。 <br />
					3、请在打款时添加尾款，如：0.01元、0.08元……以便于我司及时辨别您的款项是否到帐。 <br />
	  			</div>
	</body>
</html>