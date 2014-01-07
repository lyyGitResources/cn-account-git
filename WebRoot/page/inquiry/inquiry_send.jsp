<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hisupplier.cn.account.entity.Inquiry"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ page import="com.hisupplier.commons.basket.*"%>
<%@ page import="com.hisupplier.commons.util.*"%>
<%@ page import="com.hisupplier.commons.basket.BasketItem.Product"%>
<%@ page import="com.hisupplier.commons.basket.BasketItem.Trade"%>
<%@ page import="com.opensymphony.xwork2.util.*"%>
<%@ page import="com.hisupplier.cas.CASClient"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String sysBase = Config.getString("sys.base");
	String accountBase = Config.getString("account.base");
	if(StringUtil.equalsIgnoreCase("true",Config.getString("isBig5"))){
		sysBase = "http://big5."+Config.getString("sys.domain");
		accountBase = "http://account.big5."+Config.getString("sys.domain");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>询盘发送-海商网-全国领先的B2B电子商务交易平台</title>  
		<meta name="keywords" content="" />
		<meta name="description" content="您在浏览过程中感兴趣的公司、产品和商情信息,可将询盘信息发送到对方公司邮箱中。" />
		<meta http-equiv="X-UA-Compatible" content="IE=7" />
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js"></script>
		<style type="text/css">
			label.error {width:320px;}	
			label.success {width:170px;}
		</style>
		<script type="text/javascript">
		jQuery.extend(jQuery.validator.methods, {
				remoteEmail: function(value, element, param) {
					if (this.optional(element))
						return "dependency-mismatch";
					var previous = this.previousValue(element);
					if (!this.settings.messages[element.name] )
						this.settings.messages[element.name] = {};
					this.settings.messages[element.name].remote = typeof previous.message == "function" ? previous.message(value) : previous.message;
					param = typeof param == "string" && {url:param} || param; 
					if ( previous.old !== value ) {
						previous.old = value;
						var validator = this;
						this.startRequest(element);
						var data = {};
						data[element.name] = value;
						$.ajax($.extend(true, {
							url: param,
							mode: "abort",
							port: "validate" + element.name,
							dataType: "json",
							data: data,
							cache: false,
							success: function(response) {
								var valid = response.tip == "true";
								if (valid) {
									var submitted = validator.formSubmitted;
									validator.prepareElement(element);
									validator.formSubmitted = submitted;
									validator.successList.push(element);
									validator.showErrors();
								} else {
									var errors = {};
									errors[element.name] = previous.message = $.validator.messages.remote = validator.defaultMessage( element, "remoteEmail" );
									validator.showErrors(errors);
								}
								previous.valid = valid;
								validator.stopRequest(element, valid);
							}
						}, param));
						return "pending";
					} else if( this.pending[element.name] ) {
						return "pending";
					}
					return previous.valid;
				},
				remoteCompany: function(value, element, param) {
					if ( this.optional(element) )
						return "dependency-mismatch";
					var previous = this.previousValue(element);
					if (!this.settings.messages[element.name] )
						this.settings.messages[element.name] = {};
					this.settings.messages[element.name].remote = typeof previous.message == "function" ? previous.message(value) : previous.message;
					param = typeof param == "string" && {url:param} || param; 
					if ( previous.old !== value ) {
						previous.old = value;
						var validator = this;
						this.startRequest(element);
						var data = {};
						data[element.name] = value;
						$.ajax($.extend(true, {
							url: param,
							mode: "abort",
							port: "validate" + element.name,
							dataType: "json",
							data: data,
							success: function(response) {
								var valid = response.tip == "true";
								if (valid) {
									var submitted = validator.formSubmitted;
									validator.prepareElement(element);
									validator.formSubmitted = submitted;
									validator.successList.push(element);
									validator.showErrors();
								} else {
									var errors = {};
									errors[element.name] = previous.message = $.validator.messages.remote = validator.defaultMessage( element, "remoteCompany" );
									validator.showErrors(errors);
								}
								previous.valid = valid;
								validator.stopRequest(element, valid);
							}
						}, param));
						return "pending";
					} else if( this.pending[element.name] ) {
						return "pending";
					}
					return previous.valid;
				}
			});
		
		
		$(function() {
		  	loadValidateCode();
		  	var $tr_company_name = $("#tr_company_name");
			$("#c_type_company, #c_type_single").click(function() {
			  if (this.checked) {
			    if (this.value === 'company') {
			      $tr_company_name.show();
			    } else {
			      $tr_company_name.hide();
			    }
			  }
			});
			if ($("#c_type_single:checked").length) {
			  $tr_company_name.hide();
			}
			if ($("#newUserRadiobutton:checked").length) {
			  $("#newUser").show();
			}
		});
			
		$(function(){
			showUpload();
			$("#submitButton").removeAttr("disabled");
			
			$("#inquiryForm").validateForm({
				submitHandler: function(form) {
				   	$("#submitButton").attr("disabled","disabled");
				   	$("#submitButton").val("发送中..");
				   	form.submit();
				},
				rules: {
					subject: {required:true, maxlength:120},
					content: {required:true, rangelength:[20,3000]},
					validateCode: {required:true, minlength:5, maxlength:5}
				},
				messages: {
					subject: {required:'请输入询盘主题，长度120个字符内'},
					content: {required:'请输入询盘内容，长度3000个字符内'},
					validateCode: '请输入图片中的字符'
				}
			});
			
			$("#loadValidateCode").click(function (){
				loadValidateCode();
			});
			
			$("[name='fromWebsite']").blur(function(){
				if($(this).val() == "http://"){
					$("#fromWebsiteTip").html('<label class="error success" for="website" generated="true">OK</label>');
					$(this).val("");
				}
			}).focus(function(){
				if($(this).val() == ""){
					$(this).val("http://");
	
				}
			});
			var radiobutton_value = $("#radiobutton").val();
			if(radiobutton_value != "currentMember" 
			    && radiobutton_value != "newUser" 
			    && radiobutton_value != "loginUser"){
				selectFieldset("newUser");
			}else{
				selectFieldset(radiobutton_value);
			}
			if(radiobutton_value != "loginUser"){
				if(radiobutton_value == "newUser"){
					checklocal();
				}
			}
		});

		function selectFieldset(value){
			if(value == "newUser"){
				$("#radiobutton").val("newUser");
				$("#currentMember").hide();
				$("#fromEmail").val($("#email").val());
				$("#fromEmail").focus();
				$("#email").val("");
				$("#newUser").show();
				$("#newUserRadiobutton").attr("checked","checked");
				$("#currentMemberRadiobutton").removeAttr("checked");
				$("#tmp1").show();
				$("#tmp2").show();
				<%--
				getProvince("province", "city", "country", "tel1", "countryCode");
				--%>
				$("#fromEmail").rules("add", {required:true,email:true, remoteEmail:"/user/check_email.htm",
				 				   messages: {required:"请输入有效的电子邮箱，便于找回密码", email: "请输入有效的电子邮箱，便于找回密码", remoteEmail: "邮箱已存在，点击输入<a href='javascript:login()'>登录</a>密码即可发送询盘"}
				 				   });
				$("#fromCompany").rules(
				    "add", {
				      required: "#c_type_company:checked",
				      remoteCompany:"/user/check_comName.htm", 
				      messages: {
				        required:'请输入公司名称，长度120个字符内', 
				        remoteCompany:'该公司名称已注册，若忘记会员帐号和密码，请<a href="/user/contact_us.htm" target="_blank">联系我们</a>！'
				      }
				});
				$("#fromName").rules("add", {required:true, rangelength:[2,20],
				 				  messages: {required:'请输入联系人姓名，长度20个字符内'}
				 				  });
				/*
				$("#province").rules("add", {required:true, 
								  messages: {required:'请选择省份'}
								  });
				$("#city").rules("add", {required:true, 
							  messages: {required:'请选择城市'}
							  });
				*/
							  
				$("#tel1").rules("add", {required:true, telAreaCode:"#tel2",
				 			  messages: {required:'请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔', telAreaCode:'请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔'}
				 			  });
				$("#tel2").rules("add", {required:true, telNumber:"#tel1", 
							  messages: {required:'请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔', telNumber:'请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔'}
							  });
				$("#fax1").rules("add", {faxAreaCode:"#fax2",
				 			  messages: {faxAreaCode:'请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔'}
				 			  });
				$("#fax2").rules("add", {faxNumber:"#fax1", 
							  messages: {faxNumber:'请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔'}
							  });
				<%--
				$("#fromStreet").rules("add", {required:true, rangelength:[1,120],
				 			        messages: {required:'请输入街道地址'}
				 			        });				 								 								 								 								 								 				
				--%>
				$("#fromWebsite").rules("add", {url: true, rangelength:[0,70],
				 				     messages: {url:'请输入有效的公司网址，如 http://www.abc.com'}
				 				     });
				$("#email").rules("remove");
				$("#passwd").rules("remove");
			}else if(value == "currentMember"){
				$("#radiobutton").val("currentMember");
				$("#newUser").hide();
				$("#email").val($("#fromEmail").val());
				$("#email").focus();
				$("#fromEmail").val("");
				$("#currentMember").show();
				$("#currentMemberRadiobutton").attr("checked","checked");
				$("#newUserRadiobutton").removeAttr("checked");
				$("#tmp1").show();
				$("#tmp2").show();
				
				$("#email").rules("add", {required: true, messages: {required:'请输入会员帐号或邮箱'}});
				$("#passwd").rules("add", {passwd:true,messages: {passwd:"请输入登录密码"}});
				
				$("#fromEmail").rules("remove");
				$("#fromName").rules("remove");
				$("#fromCompany").rules("remove");
				<%--
				$("#province").rules("remove");
				$("#city").rules("remove");
				$("#fromStreet").rules("remove");
				--%>
				$("#tel1").rules("remove");
				$("#tel2").rules("remove");
				$("#fax1").rules("remove");
				$("#fax2").rules("remove");
				$("#fromWebsite").rules("remove");
				
			}else if(value == "loginUser"){
				$("#newUserFieldset").hide();
				$("#currentMemberFieldset").hide();
				$("#tmp1").hide();
				$("#tmp2").hide();
				$("#currentMemberRadiobutton").removeAttr("checked");
				$("#newUserRadiobutton").removeAttr("checked");

			}
		}
		
		function loadValidateCode(){
			Util.loadValidateCode(document.inquiryForm,"validateCodeImg","/validateCode/getImage");
		}
		function switchOpen(number) {
			document.getElementById("switch_close_" + number).style.display = "none";
			document.getElementById("switch_open_" + number).style.display = "block";
			document.getElementById("item_list_" + number).style.display = "block";
		}
		function switchClose(number) {
			document.getElementById("switch_close_" + number).style.display = "block";
			document.getElementById("switch_open_" + number).style.display = "none";
			document.getElementById("item_list_" + number).style.display = "none";
		}
		function showUpload() {
			if($("#uploadSwitch:checked").size() > 0){
				$("#uploadBar").show();
			}else{
				$("#uploadBar").hide();
			}
		}
		
		function forgetPasswd() {
			var email = $.trim($("#email").val());
			$("#forgetLink").attr("href","/user/forget_passwd.htm?email="+email);
			return
		}
 
		function textLimitCheck(thisArea, showArea, maxLength) {
			if (thisArea.value.length > maxLength) {
				alert(maxLength + " characters limit. \r Excessive data will be truncated.");
				thisArea.value = thisArea.value.substring(0, maxLength - 1);
				thisArea.focus();
			}
			showArea.value = thisArea.value.length;
		}
		
		function login(){
			selectFieldset("currentMember");
		}
		
		function checklocal(){
			if($("#province").val() == ""){
				$("[name='fromProvince']").rules("add", {
					required:true,
					messages: {required:'请选择省份'}
				});
			}else{
				$("[name='fromCity']").rules("add", {
					required:true,
					messages: {required:'请选择城市'}
				});
				$("[name='fromProvince']").rules("remove");
			}
		}
		</script>
	</head>
	<body>
		<%--产品列表部分--%>
		<div class="area">
			<div class="mainNav2"><span class="T">您正在浏览：</span><a href="<%=sysBase %>">首页</a> &gt; <span>联系该商家</span></div>
			<div class="blank1"></div>
			<div class="inquiryBox">
				<div class="h1">联系该商家</div>
				<div class="tips">
					<div>已经是会员？
						<input type="button" value="立即登录" class="button9" onclick="javascript:window.location.href='/'" style="cursor: pointer;"/>
						<a href="/user/forget_passwd.htm">忘记密码？</a>
					</div>
					<span class="s1">必填项</span><span class="s2"><a href="http://help.cn.hisupplier.com/supplier/accidence/info.php?bid=181&id=190" target="_blank">帮助中心</a></span>
				</div>
				<form action="/user/inquiry_send.htm" name="inquiryForm" id="inquiryForm" method="post" enctype="multipart/form-data" class="submitForm">
					<%@ include file="/page/inc/messages.jsp"%>
					<input type="hidden" id="radiobutton" value="<s:if test="inquiry.loginUser != null">loginUser</s:if><s:else><s:if test="inquiry.newUser == true">newUser</s:if><s:else>currentMember</s:else></s:else>"/>
					<input type="hidden" id="countryCode" name="countryCode" value="<s:property value="inquiry.countryCode"/>" />
					<input type="hidden" id="validateCodeKey" name="validateCodeKey" value="<s:property value="inquiry.validateCodeKey"/>" />
					<input type="hidden" name="tradeAlertInfoType" value="<s:property value="inquiry.tradeAlertInfoType"/>" />
					<input type="hidden" name="<%= Basket.GET_METHOD %>" value="<%= Basket.GET_SELECTED %>"/>
					<input type="hidden" name="fromPage" value="<s:property value="inquiry.fromPage"/>"/>										
					<input type="hidden" name="fromSite" value="<s:property value="inquiry.fromSite"/>"/>
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th><b>发送给：</b></th>
								<td>
								<table>
								<s:iterator value="inquiry.basketItemList" id="item" status="st">
								<tr><td>
									<input type="hidden" name="<%= Basket.COMPANY %>" value="<s:property value="#item.comId"/>" />
									<s:if test="#item.size >0">
										<div id="switch_close_<s:property value="#item.comId"/>" onclick="javascript:switchOpen(<s:property value="#item.comId"/>);" style="text-align:left; height:20px;">
											<div class="companyName" style="vertical-align: bottom;"><a href="#position" ><s:property value="#item.comName"/></a>(<s:property value="#item.size"/>)</div>
										</div>
										<div id="switch_open_<s:property value="#item.comId"/>" onclick="javascript:switchClose(<s:property value="#item.comId"/>);" style="text-align:left;display:none">
											<div class="companyName Bg"><a href="#position" ><s:property value="#item.comName"/></a>(<s:property value="#item.size"/>)</div>
											<div class="box1" id="item_list_<s:property value="#item.comId"/>" style="display:none; height: auto;">
											<s:iterator value="#item.productList" id="product" status="st">
												<input type="hidden" name="<%= Basket.PRODUCT %>-<s:property value="#item.comId"/>" value="<s:property value="proId"/>"/>								 
												<div class="inquiryPro">
													<div class="proImg">
														<table cellspacing="0" cellpadding="0" border="0" class="photo75">
															<tr>
																<td align="center"><a href="<s:property value="#product.proUrl"/>" target="_blank"><img src="<s:property value="#product.imgPath"/>" border="0"  onload="Util.setImgWH(this, 75, 75)" /></a></td>
															</tr>
														</table>
													</div>
													<div class="text">
														<a href="<s:property value="#product.proUrl"/>" target="_blank"><s:property value="#product.proName"/></a><br>
														<span><s:property value="#product.model"/></span>
													</div>
												</div>
												<s:if test="#st.count%6 == 0">
													<div class="blank21"></div>
												</s:if>
											</s:iterator>
											<s:iterator value="#item.tradeList" id="trade" status="st">
												<input type="hidden" name="<%= Basket.TRADE %>-<s:property value="#item.comId"/>" value="<s:property value="#trade.tradeId"/>"/>		
												<div class="inquiryPro">
													<div class="proImg">
														<table cellspacing="0" cellpadding="0" border="0" class="photo75">
															<tr>
																<td align="center"><a href="<s:property value="#trade.proUrl"/>" target="_blank"><img src="<s:property value="#trade.imgPath"/>"  border="0"  onload="Util.setImgWH(this, 75, 75)" /></a></td>
															</tr>
														</table>
													</div>
													<div class="text">
														<a href="<s:property value="#trade.proUrl"/>" target="_blank"><s:property value="#trade.tradeName"/></a><br>
													</div>
												</div>
												<s:if test="#st.count%5 == 0">
													<div class="blank21"></div>
												</s:if>
											</s:iterator>
										</div>
										</div>
									</s:if>
									<s:else>
										<div id="switch_open_<s:property value="#item.comId"/>" style="text-align:left;">
											<div class="companyName Bg"><a href="#position" ><s:property value="#item.comName"/></a>(<s:property value="#item.size"/>)</div>
										</div>
									</s:else>
									</td></tr>
									</s:iterator>
									</table>
								</td>
							</tr>
						<tr>
							<th><label><strong>主题：</strong></label></th>
							<td>
								<input name="subject" id="subject" style=" width:402px; height:17px;" maxlength="120" value="<s:property value="inquiry.subject"/>"/>
								<span id="subjectTip"></span>
							</td>
						</tr>
						<tr>
							<th style="line-height:20px;">
								<label><strong>内容：<br />字符个数：</strong></label><br />
								<input name="messageCount" id="messageCount" type="text" style=" width:30px; height:17px;" readonly/>&nbsp;&nbsp;
							</th>
							<td>
								<textarea name="content" id="content" style="width:400px;height:150px;" onKeyDown="textLimitCheck(this, messageCount,3000)" onKeyup="textLimitCheck(this, messageCount,3000)"
									 onmousedown="textLimitCheck(this, messageCount,3000)" onmouseup="textLimitCheck(this, messageCount,3000)"><s:property value="inquiry.content"/></textarea>
								<div id="contentTip" style="margin-top:5px;"></div>
							</td>
						</tr>
						<tr>
							<th>&nbsp;</th>
							<td style="padding-left:220px;">
								<span style="color:#FF6600;">如果您想发送附件请点击这里</span>
								<input type="checkbox" id="uploadSwitch" onclick="showUpload()">
							</td>
						</tr>
						<tr id="uploadBar" style="padding-top: 5px;display: none;">
							<th>附件：</th>
							<td>
								<input type="file" name="upload" /><br />
								<input type="file" name="upload" /><br />	
								<input type="file" name="upload" /><br />	
								<div class="t">								
								文件格式和大小：<br /> 
								— 文件格式支持 jpg、jpeg、gif、txt、doc、pdf <br /> 
								— 最大500KB	
								</div>						
							</td>
						</tr>
					</table>
				<s:if test="inquiry.loginUser == null">
					<%--------------New user---------------------%>
					<div id="newUserFieldset">
					<div class="h2">
						<div style="float:none; width:120px;">
							<input type="radio" id="newUserRadiobutton" name="newUser" value="true" <s:if test="%{inquiry.newUser}"> checked </s:if> onclick="selectFieldset('newUser');" />
							还不是会员？
						</div>
					</div>
					<div id="newUser">
						<table border="0" cellspacing="0" cellpadding="0" style="margin-bottom:30px;">
							<tr>
								<th><label><strong>公司类型：</strong></label></th>
								<td>
									<input value="company" checked="checked" name="fromCompanyType" id="c_type_company" type="radio" />
									<label for="c_type_company">公司</label>
									<input value="single" name="fromCompanyType" id="c_type_single" type="radio" />
									<label for="c_type_single">个体经营</label>
								</td>
							</tr>
							<tr id="tr_company_name">
								<th><label><strong>公司名称：</strong></label></th>
								<td>
									<input name="fromCompany" id="fromCompany" style=" width:305px; height:17px;"  value="<s:property value="inquiry.fromCompany"/>" />
									<div class="t">请填写工商局注册的全称，无商号的个体经营者请填写执照上的姓名，并标注个体经营，<br />如张三（个体经营）公司名一旦注册，不可随意修改，如需修改请提供相关证明。</div>
									<span id="fromCompanyTip"></span>
								</td>
							</tr>
							<tr>
								<th><label><strong>电子邮箱：</strong></label></th>
								<td>
									<input name="fromEmail" id="fromEmail" style=" width:305px; height:17px;" maxlength="80" value="<s:property value="inquiry.email"/>"/>
									<span id="fromEmailTip"></span>
								</td>
							</tr>
							<tr>
								<th style="padding-bottom:0;"><label><strong>联系人姓名：</strong></label></th>
								<td>
									<input name="fromName" id="fromName" style=" width:200px; height:17px;"  value="<s:property value="inquiry.fromName"/>" />
									<input checked="checked" type="radio" name="sex" value="1" <s:if test="inquiry.sex == 1">checked</s:if>>先生&nbsp;&nbsp;
									<input type="radio" name="sex" value="2" <s:if test="inquiry.sex == 2">checked</s:if>>女士&nbsp;&nbsp;
									<input type="radio" name="sex" value="3" <s:if test="inquiry.sex == 3">checked</s:if>>小姐
									<span id="fromNameTip"></span>
								</td>
							</tr>
							<tr>
								<th style="padding-top: 0;">&nbsp;</th>
								<td style="padding:0; color:#999; vertical-align:bottom;">区号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;电话</td>
							</tr>
							<tr>
								<th style="padding-bottom:0;"><label><strong>电话号码：</strong></label></th>
								<td style="padding-top:0;">
									<input type="text" id="tel1" name="tel1" maxlength="4" value="<s:property value="inquiry.tel1"/>" style=" width:54px; height:17px;"/>
									-
									<input type="text" id="tel2" name="tel2" maxlength="26" value="<s:property value="inquiry.tel2"/>" style=" width:131px; height:17px;"/>
									<div class="t">多个电话用","或"/"分隔，分机号码请用"-"分隔。<br />如：0574-27702770-123,27712771</div>
									<div id="telTip"></div>
								</td>
							</tr>
							<tr>
								<th style="padding:0;">&nbsp;</th>
								<td style="padding:0; color:#999; vertical-align:bottom;">区号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;电话</td>
							</tr>
							<tr>
								<th style="padding-top:0;"><strong>传真：</strong></th>
								<td style="padding-top:0;">
								<input type="text"id="fax1" name="fax1" maxlength="4" value="<s:property value="inquiry.fax1"/>" style=" width:54px; height:17px;"/>
							    -
								<input type="text" id="fax2" name="fax2" maxlength="26" value="<s:property value="inquiry.fax2"/>" style=" width:131px; height:17px;"/>
								<div class="t">多个传真用","或"/"分隔，分机号码请用"-"分隔。<br />如：0574-27702770-123,27712771</div>
								<div id="faxTip"></div>
								</td>
							</tr>
							<%--
							<tr>
								<th><strong>所在地：</strong></th>
								<td>
									<select id="province" name="fromProvince" style="width: 130px;"></select>
									<select id="city" name="fromCity"></select>
									<select id="country" name="fromTown"></select>
									<span id="cityTip"></span>
								</td>
							</tr>
							<tr>
								<th><strong>地址：</strong></th>
								<td>
									<input name="fromStreet" id="fromStreet" maxlength="120" style=" width:305px; height:17px;" value="<s:property value="inquiry.fromStreet"/>"/>
									<span id="fromStreetTip"></span>
								</td>
							</tr>
							 --%>
							<tr>
								<th><strong>公司网址：</strong></th>
								<td>
									<input name="fromWebsite" id="fromWebsite" maxlength="100" style=" width:305px; height:17px;" value=""/>
									<span id="fromWebsiteTip"></span>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<div  id="currentMemberFieldset">
					<div class="h2">
						<div style="float:none;width:120px;">
							<input type="radio" id="currentMemberRadiobutton" name="newUser" value="false" <s:if test="%{!inquiry.newUser}"> checked </s:if> onclick="selectFieldset('currentMember');"/>
							已经是会员？
						</div>
					</div>
					<div id="currentMember" style="display:none">
						<table border="0" cellspacing="0" cellpadding="0" style="margin-bottom:30px;">
							<tr>
								<th><label><strong>会员帐号或邮箱：</strong></label></th>
								<td>
									<input id="email" name="email" value="<s:property value="inquiry.fromEmail"/>" />
									<span id="emailTip"></span>
								</td>
							</tr>
							<tr>
								<th><label><strong>密码：</strong></label></th>
								<td>
									<input type="password" id="passwd" name="passwd" value="" />
									<a id="forgetLink" href="#position" onclick="forgetPasswd();" target="_blank">忘记密码？</a>
									<span id="passwdTip"></span>
								</td>
							</tr>
						</table>
					</div>
				</div>
				</s:if>	
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th><strong>希望回复天数：</strong></th>
							<td>
								<label>
							       <select name="replyDay" id="replyDay" style="width:70px;float:left" >
										<option value="5" <s:if test="inquiry.replyDay == 5">selected</s:if>>5 天</option>
										<option value="7" <s:if test="inquiry.replyDay == 7">selected</s:if>>7 天</option>
										<option value="15" <s:if test="inquiry.replyDay == 15">selected</s:if>>15 天</option>	
										<option value="30" <s:if test="inquiry.replyDay == 30">selected</s:if>>30 天</option>
									</select>
						        </label>
							</td>
						</tr>
						<tr>
							<th>&nbsp;</th>
							<td>
								<s:if test="inquiry.validateCodeKey == null">
									<img id="validateCodeImg"/>	
								</s:if>
								<s:else>
									<img id="validateCodeImg" src="/validateCode/getImage?hi_vc_key=<s:property value="inquiry.validateCodeKey"/>"/>	
								</s:else>
							</td>
						</tr>
						<tr>
							<th><label><strong>验证码：</strong></label></th>
							<td>
								<input type="text" name="validateCode" value="" autocomplete="off" maxlength="5" style=" width:100px; height:17px;"/>
								<a href="#position" id="loadValidateCode" >看不清，换一张</a>
								<span id="validateCodeTip"></span>
							</td>
						</tr>
						<s:if test="%{inquiry.tradeAlertKeyword != null}">
							<tr>
								<th>
									<input type="checkbox" name="tradeAlert" value="true" checked />
									<span class="t">是否需要订阅商机？</span><strong>关键词</strong>
								</th>
								<td>
									<input type="text" id="tradeAlertKeyword" name="tradeAlertKeyword" maxlength="30"  style=" width:305px; height:17px;" value="<s:property value="inquiry.tradeAlertKeyword"/>"/>							
									<span id="tradeAlertKeywordTip"></span>
								</td>
							</tr>
						</s:if>
						<tr>
							<th>&nbsp;</th>
							<td style="padding-top:30px;"><input type="submit" name="Submit" value="发送询盘" class="button7"/></td>
						</tr>
					</table>
				</form>	
				<div class="footerTips">
					<%--<div>需要帮助？请立即<a href="<%=accountBase%>/user/contact_us.htm" target="_blank">联系我们</a>！</div>--%>
					<div>您在搜索卖家/买家信息过程中，有任何问题和建议？<a href="<%=accountBase%>/user/contact_us.htm" target="_blank">点此反馈</a></div>
				</div>
			</div>
		</div>
		<div class="blank2"></div>
	</body>
</html>