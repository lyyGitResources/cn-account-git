<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>网络电话</title>
		<script type="text/javascript">
			$(function () {
				$("#voIPForm").validate({
					submitHandler: function(form){
						$(form).ajaxSubmit({ 
						    success: function(response){
						    	alert(response.msg);
						    	if(response.tip == "addSuccess"){
									location.href= "/voIP/voIP_list.htm";
						    	}
						    },
						    dataType: 'json'
						});
					},
					rules: {
						telephone: {required:true, digits:true, rangelength: [10, 15]}
					},
					messages: {
						telephone: '请输入一个有效的电话/手机号码'
					}
				});
			});
		</script>
	</head>
	<body>
		<s:if test="result.state == 'adminNoRegister'">
			<div style="border: 1px solid #F8C540;padding-top: 20px;padding-left: 30px;padding-right: 20px;overflow: hidden;padding-bottom: 30px;">
				<div style="background: url(/img/ico/tip.jpg) no-repeat;padding-left: 80px;padding-top: 10px;line-height: 40px;font-size: 14px;">
					<span style="color: #000;font-size: 16px;font-weight: bold;">
						贵公司尚未开通网络电话功能，请通知管理员开通！
					</span>
				</div>
			</div>
		</s:if>
		<s:else>
			<s:if test="result.state == 'adminRegister'">
				<div style="border: 1px solid #9FC1E7;padding-top: 20px;padding-left: 30px;padding-right: 20px;overflow: hidden;padding-bottom: 30px;margin-bottom:20px;">
					<div style="background: url(/img/ico/suc.gif) no-repeat;padding-left: 80px;padding-top: 10px;line-height: 40px;font-size: 14px;">
						<span style="color: #3A9805;font-size: 16px;font-weight: bold;">
							贵公司已经开通网络电话功能！
						</span>
					</div>
				</div>
				<div style="border:1px solid #9AB6CD; font-size:14px;">
			</s:if>
			<s:else>
				<div style="border:1px solid #9AB6CD; font-size:14px;">
					<div style="margin:20px 50px; line-height:30px;">
						<p>尊敬的客户：</p>
			       		<p style="text-indent:25px;">
			       			海商网中文平台最新推出“网络电话服务”，如果您是黄金会员，将免费享受高达60分钟的网络电话服务，<br />
						如果您是免费会员，我们也将赠送给您5分钟的网络电话服务。
						</p>
					</div>
					
					<div style="background:url(/img/freeTel/bg02.gif) repeat-y; padding:0; margin:0 auto; width:650px;">
						<img src="/img/freeTel/bg01.gif" /><br />
						<div style="margin:44px 39px; line-height:30px; font-size:12px; color:#333333;"><span style=" color:#FF6600; font-weight:bold;">海商网络电话有以下优点：</span><br />
							1、海商网络电话将互联网与通信工具连接起来，接通双方都是被叫方，通话质量好、费用低；<br />
							2、买家需要注册海商网会员才能使用网络电话，电话质量高；<br />
							3、买家拨打电话无需付费，能够吸引更多的买家咨询产品。<br /><br />
							
							通话资费：0.25元/ 分钟
						</div>
						<img src="/img/freeTel/bg03.gif" />
					</div>
				</s:else>
				<div style="margin:20px 70px; line-height:30px; font-size:12px;">
					<form id="voIPForm" method="post" action="/voIP/voIP_binding.do">
						请输入您的固定电话或手机号码：
						<input name="telephone" maxlength="20" value="<s:property value="telephone"/>"/>
						<input type="submit" value="绑定" style="background:url(/img/freeTel/button02.gif) no-repeat; color:#FFFFFF; border:0px; width:58px; height:22px; font-size:12px;"/>
					</form>
				
					 <span style="color:#666666;">固定电话号码格式：区号+电话号码      例如：057427702770</span><br />
		         	 <span style="color:#666666;">手机号码格式：请直接输入11位手机号      例如：13827702770</span> 
				</div>
			</div>
		</s:else>
	</body>
</html>
