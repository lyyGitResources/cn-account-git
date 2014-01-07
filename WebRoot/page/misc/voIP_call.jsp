<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>中国海商网-免费电话回呼</title>
		<link href="/css/freeTel.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/util.js"></script>
		<script type="text/javascript">
			$(function () {
				var tel = '<s:property value="result.loginUser.tel" />';
				var tels = tel.split("-");
				if(tels.length >= 2){
					if($("#tel1").val() == "" || $("#tel2").val() == ""){
						$("#tel1").val(tels[0]);
						var tel2 = tels[1];
						if(tel2.indexOf(',') != -1){
							tel2 = tel2.substr(0, tel2.indexOf(','));
						}
						$("#tel2").val(tel2);
					}
				}

				var mobile =  '<s:property value="result.loginUser.mobile" />';
				if($("#mobile").val() == ""){
					$("#mobile").val(mobile.replace(/(\+?)(\d{2,3})-/i, ""));
				}
				
				$("#submit").click(function(){
					$("#message").html("");
	
					if(validateForm()){
						$.ajax({
							type: "post", 
							url: "/voIP/voIP_call_submit.do", 
							cache: false, 
							data: "comId=<s:property value='comId'/>&userId=<s:property value='userId'/>" 
																+ "&tel1=" + $("#tel1").val() 
																+ "&tel2=" + $("#tel2").val()
																+ "&validateCodeKey=" + $("#validateCodeKey").val()
																+ "&validateCode=" + $("#validateCode").val()
																+ "&mobile=" + $("#mobile").val(), 
							dataType:"json", 
							success: function (response) {
								$("#message").html(response.msg);
								if(response.msg == "呼叫失败..."){
									loadValidateCode();
								}
							}
						});	
					}
				});
				
				modelHidden();
				$("[name='chooseTel']").each(function(){
					$(this).click(function(){
						modelHidden();
					});
				});
			
				window.resizeTo(820, 700);
				window.moveTo(200, 100);
				
				$("#loadValidateCode").click(function (){
					loadValidateCode();
				});
			});
			
			function modelHidden(){
				if($("[name='chooseTel']:checked").val() == "true"){
					$("#model1").show();
					$("#model2").hide();
				}else{
					$("#model1").hide();
					$("#model2").show();
				}
			}
			
			function validateForm(){
				var error = "";
				if($("#model2").attr("display") == "none"){
					var tel1 = $.trim($("#tel1").val());
					var tel2 = $.trim($("#tel2").val());
					
					if(tel1.length < 3 || tel1.length > 4 || !Util.isInt(tel1)){
						error += "区号应该为3-4位的数字！\r\n";
					}
					if(tel2.length < 7 || tel2.length > 8 || !Util.isInt(tel2)){
						error += "电话号码应该为7-8位的数字！\r\n";
					}
				}else {
					var mobile = $.trim($("#mobile").val());
					
					if(mobile.length != 11 || !Util.isInt(mobile)){
						error += "手机号码应该为11位的数字！\r\n";
					}
				}
				
				var validateCode = $("#validateCode").val();
				if(validateCode.length != 5){
					error += "请输入图片中的字符！\r\n";
				}

				if(error.length == 0){
					return true;
				}else{
					alert(error);
					loadValidateCode();
					return false;
				}
			}
			
			function loadValidateCode(){
				Util.loadValidateCode(document.VoIPForm, "validateCodeImg", "/validateCode/getImage");
			}
		</script>
	</head>
	<body>
		<table width="800" border="0" align="center" cellpadding="0" cellspacing="0">
		  <tr>
		    <td align="center" valign="bottom">
		    	<table width="95%" border="0" cellspacing="0" cellpadding="5">
			      <tr>
			        <td width="30%" align="left" valign="bottom"><img src="/img/freeTel/logo.jpg" width="215" height="77" /></td>
			      </tr>
    			</table>
    		</td>
  		</tr>
	  	<tr>
	    	<td align="center" valign="bottom"><img src="/img/freeTel/signBg01.gif" width="800" height="10" /></td>
	  	</tr>
	  	<tr>
    		<td align="center" valign="top" background="/img/freeTel/signBg02.gif"><br />
      			<table width="95%" border="0" cellspacing="0" cellpadding="0">
      				<tr>
        				<td width="48%" valign="top">
        					<table width="364" border="0" cellspacing="0" cellpadding="0">
         	 					<tr>
            						<td align="center" valign="bottom"><img src="/img/freeTel/sign01.gif" width="364" height="9" /></td>
         	 					</tr>
          						<tr>
            						<td align="center" valign="top" background="/img/freeTel/sign02.gif">
            							<table width="93%" border="0" cellspacing="0" cellpadding="3">
              								<tr>
								                <td width="21%" align="right" class="txt02">输入号码：</td>
                								<td align="left" class="txt02">
											      	<input type="radio" checked="checked" value="true" name="chooseTel"/> 固定电话
											      	<input type="radio" value="false" name="chooseTel"/> 手机
											    </td>
              								</tr>
							              	<tr>
												<td>&nbsp;</td>
												<td align="left" class="txt02" id="model1">
													区号             电话号码<br/>
													<input type="text" style="width: 40px;" maxlength="4" id="tel1"/> － 
													<input type="text" style="width: 90px;" maxlength="8" id="tel2"/>
												</td>
												<td align="left" class="txt02" style="display: none;" id="model2">
													<input type="text" style="width: 155px;" maxlength="11" id="mobile"/>
												</td>
											</tr>
											<tr>
								                <td class="txt02" align="right">验证码：</td>
								                <td align="left">
								                     <input id="validateCode" type="text" value="<s:property value="validateCode"/>">
												</td>
								                <td>&nbsp;</td>
              								</tr>
											<form name="VoIPForm">
												<input type="hidden" id="validateCodeKey" value="<s:property value="validateCodeKey"/>"/>
												<tr>			
									                <td>&nbsp;</td>
									                <td align="left">
														<s:if test="validateCodeKey == null">
															<script type="text/javascript">
																$(function(){
																	loadValidateCode();
																});
															</script>
															<img id="validateCodeImg" height="50" align="absmiddle" width="160" />	
														</s:if>
														<s:else>
															<img id="validateCodeImg" height="50" align="absmiddle" width="160" src="/validateCode/getImage?hi_vc_key=<s:property value="validateCodeKey"/>"/>	
														</s:else>
													</td>
									                <td class="txtLink01" align="left">
									                	<a href="#position" class="txtLink01" id="loadValidateCode">看不清，换一张</a>
									                </td>
									            </tr>
								            </form>
											<tr>
							                	<td>&nbsp;</td>
							                	<td align="left" valign="bottom">
							                		<a id="submit" href="#position" style="color:#FFFFFF;">
							                			<img src="/img/freeTel/button04.gif" width="108" height="36" border="0"/>
							                		</a>
							                	</td>
							                	<td>&nbsp;</td>
							              	</tr>
            							</table>
              							<table width="80%" border="0" cellspacing="0" cellpadding="5">
							                <tr>
							                  <td align="left" class="biaoti01" id="message"></td>
							                </tr>
              							</table>
              						</td>
          						</tr>
					          	<tr>
					            	<td align="center" valign="top"><img src="/img/freeTel/sign03.gif" width="364" height="9" /></td>
					          	</tr>
        					</table><br />
          					<table width="364" border="0" cellspacing="0" cellpadding="0">
            					<tr>
              						<td align="center" valign="bottom"><img src="/img/freeTel/sign01.gif" width="364" height="9" /></td>
            					</tr>
            					<tr>
              						<td align="center" valign="top" background="/img/freeTel/sign02.gif">
              							<table width="95%" border="0" cellspacing="3" cellpadding="3">
                							<tr>
                  								<td width="7%" align="center" valign="top"><img src="/img/freeTel/ico01.gif" width="22" height="28" /></td>
                  								<td width="93%" align="left" valign="top" class="txt02">
                  									输入您的接听电话号码，固定电话请加拨区号（057427702770）<br />
                   								 	手机请直接输入号码（13827702770）。
                   								 </td>
                							</tr>
                							<tr>
							                  	<td align="center" valign="top"><img src="/img/freeTel/ico02.gif" width="22" height="28" /></td>
							                  	<td align="left" valign="top" class="txt02">输入无误后，点击“马上接通”按钮，立即以接听的方式免费通话。</td>
                							</tr>
              							</table>
              						</td>
            					</tr>
					            <tr>
					            	<td align="center" valign="top"><img src="/img/freeTel/sign03.gif" width="364" height="9"/></td>
					            </tr>
          					</table>
          				</td>
        				<td width="52%" height="404" align="center" valign="top" background="/img/freeTel/tu01.gif"><br />
          					<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
          						<tr>
            						<td height="35" align="center" valign="middle" class="biaoti"><s:property value="result.company.comName" /></td>
          						</tr>
        					</table>
          					<br />
          					<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
	            				<tr>
	              					<td align="left" valign="top" class="txt02">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	              						<s:property value="result.company.description" />
	              					</td>
	            				</tr>
          					</table>
          				</td>
      				</tr>
    			</table>
    			<br />
    		</td>
	  	</tr>
	  	<tr>
	  		<td align="center" valign="top"><img src="/img/freeTel/signBg03.gif" width="800" height="10" /></td>
	  	</tr>
	</table>
	</body>
</html>