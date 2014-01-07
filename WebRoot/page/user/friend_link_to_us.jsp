<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>交换链接</title>
		<style type="text/css">
			label.error {
				width:200px;
			}	
			label.success {
				width:200px;
			}
			.operateTips {
				width:620px;
			}
			.operateTips ul {
				width:600px;
			}
			.operateTips ul li{
				width:560px;
			}
		</style>
		<script type="text/javascript">
			$(function (){
				selectShow();
				$("#friendForm").validateForm({
					rules: {
						contact: {required: true, maxlength:40},
						email: {required: true, maxlength:80, email: true},
						title: {required: true, maxlength:120},
						link: {required: true, maxlength:120, url:true},
						linkus: {maxlength:120, url:true},
						linkType: {required: true, min: 1, max:7},
						brief: {maxlength:500},
						validateCode: {required: true, rangelength:[5,5]},
						imgPath:{required: true, maxlength:120, url:true},
						catId:{required: true, min: 1, max:24},
						area:{required: true}
					},
					messages: {
						contact: '<s:text name="friend.contact.required" />',
						email: '<s:text name="email.required" />',
						title: '<s:text name="friend.title.required" />',
						link: '<s:text name="url.required" />',
						linkus: '<s:text name="url.required" />',
						linkType: '<s:text name="friend.linkType.required" />',
						brief: '<s:text name="friend.brief.maxlength" />',
						validateCode: '<s:text name="validateCode.required" />',
						imgPath:'<s:text name="url.required" />'
					}
				}); 
			
				$("#loadValidateCode").click(function (){
					loadValidateCode();
				});	

				$("#area").change(function(){
					selectShow();
				});
			});
			
			function selectShow(){
					var con = $("#area option:selected").val();
					if(con == 1){
						//隐藏部分
						$("#catTr").hide();
						$("#logoTr").hide();
						$("#catTr select").attr("disabled","disabled");
						$("#logoTr input").attr("disabled","disabled");
						//显示部分
						$("#linkTypeTr select").children().remove();
						$("#linkTypeTr select").html("<option value='7'>媒体合作</option><option value='5'>行业网站</option>");
						$("#linkTypeTr").show();
					}
					if(con == 2){
						//隐藏部分
						$("#catTr").hide();
						$("#logoTr").hide();
						$("#catTr select").attr("disabled","disabled");
						$("#logoTr input").attr("disabled","disabled");
						//显示部分
						$("#linkTypeTr select").children().remove();
						$("#linkTypeTr select").html("<option value='1'>媒体合作</option><option value='2'>行业网站</option><option value='3'>其他网站</option>");
						$("#linkTypeTr").show();
					}
					if(con == 3){
						//隐藏部分
						$("#linkTypeTr").hide();
						$("#linkTypeTr select").children().remove();
						$("#linkTypeTr select").html("<option value='6'>行业目录合作伙伴</option>");
						//显示部分
						$("#catTr select").removeAttr("disabled");
						$("#logoTr input").removeAttr("disabled");
						$("#logoTr").show();
						$("#catTr").show();
					}
			}
			
			function loadValidateCode(){
				Util.loadValidateCode(document.friendForm,"validateCodeImg","/validateCode/getImage");
			}	
		</script>
	</head>
	<body>
		<div class="main">
			<span style="color: rgb(254, 0, 0); font-size: 14px; font-weight: bold;">链接要求：</span><br /><br />
			1、本着公平、互惠的原则，欢迎各优秀网站与本站建立友情链接，共同提高双方网站竞争力<br/>
			2、网站不得包含反动、色情等违法内容，百度、Google收录正常<br/>
			3、只做内页文字链接，PR不低于4<br/>
			4、本站会定期整理友情链接，若PR低于4，将取消链接，恕不另行通知<br/>
			<div style="color: rgb(255, 103, 2); font-size: 14px; margin-top: 10px; margin-bottom: 10px;">
				有意合作者请先在您的网站相关页面做好本站链接，非常感谢您的支持！
			</div>
			<b>本站名称：</b>
			海商网<br/>
			<b>链接地址：</b>
			http://cn.hisupplier.com<br/>
			<b>网站简介：</b>
			海商网（cn.hisupplier.com）是中国领先的电子商务品牌，是全球领先的网上贸易平台，已经成为全球商人销售产品、拓展市场及网络推广的首选网站。
			它分为机械、五金工具、农业食品、服装饰件、工艺品、玩具、汽摩及配件、电气电子等24个大类，一千多个之类，为客商提供产品搜索、供应商搜索、采购商搜索、供应信息搜索、采购信息搜索、目录搜索等多种高级搜索方式。
			海商网积聚了丰富的采购商和供应商信息，大量的采购、供求信息，已经形成一个巨大的网上贸易市场，为企业拓展销售渠道，建立自主品牌提供了良好的平台支持，是商人创造商机的最佳选择。
			<div class="line"></div>
			<img border="0" alt="海商网" title="海商网" src="http://cn.<%=Config.getString("sys.domain") %>/img/logo/hi_logo2.jpg"/>
			<textarea style="border: 1px solid rgb(130, 157, 184); width: 690px; font-size: 12px; height: 60px; margin-top: 5px;"><a href="http://cn.<%=Config.getString("sys.domain") %>/" target="_blank"><img src="http://cn.<%=Config.getString("sys.domain") %>/img/logo/hi_logo2.jpg" width="88" height="31" border="0" alt="海商网" title="海商网"/></a></textarea>
			<div class="line2"></div>
			<img border="0" alt="海商网" title="海商网" src="http://cn.<%=Config.getString("sys.domain") %>/img/logo/Global-B2B-E-Marketplace.jpg"/>
			<textarea style="border: 1px solid rgb(130, 157, 184); width: 690px; font-size: 12px; height: 60px; margin-top: 5px;"><a href="http://cn.<%=Config.getString("sys.domain") %>/" target="_blank"><img src="http://cn.<%=Config.getString("sys.domain") %>/img/logo/Global-B2B-E-Marketplace.jpg" width="175" height="47" border="0" alt="海商网" title="海商网"/></a></textarea>
			<div class="line2"></div>
			<img border="0" alt="海商网" title="海商网" src="http://cn.<%=Config.getString("sys.domain") %>/img/logo/hi_cn.gif"/>
			<textarea style="border: 1px solid rgb(130, 157, 184); width: 690px; font-size: 12px; height: 60px; margin-top: 5px;"><a href="http://cn.<%=Config.getString("sys.domain") %>/" target="_blank"><img src="http://cn.<%=Config.getString("sys.domain") %>/img/logo/hi_cn.gif" width="460" height="60" border="0" alt="海商网" title="海商网"/></a></textarea>
			<div class="line"></div>
			<a name="friend_link_form" id="friend_link_form"></a>
			<%@ include file="/page/inc/messages.jsp"%>
			<form id="friendForm" name="friendForm" method="post" action="/user/friend_link_to_us.htm#friendForm" >
				<input name="brief" type="hidden"/>
				<input type="hidden" name="validateCodeKey" value="<s:property value="validateCodeKey"/>"/>
				<table cellspacing="0">
					<tbody>
						<tr>
							<th colspan="3"></th>
						</tr>
						<tr>
							<th width="15%"><span>*</span> 网站名称：</th>
							<td>
								<input type="text" style="width:250px;float:left;" name="title" value="<s:property value='title' />"/>
								<br/><span>请填写贵站的中文全称</span>
							</td>
							<td id="titleTip"></td>
						</tr>
						<tr>
	                        <th><span>*</span> 链接地址：</th>
						    <td>
								<input type="text" style="width: 250px;float:left;" name="link" value="<s:property value='defaultLink' />"/><br/>
	                   			<span>请填写贵站需要的链接的地址</span>
	                        </td>
	                        <td id="linkTip"></td>
						</tr>
						<tr>
	                    	<th><span>*</span> 互换链接地址：</th>
							<td>
								<input type="text" style="width: 250px;float:left;" name="linkus" value="<s:property value='defaultLinkus' />"/><br/>
	                            <span>请填写贵站为海商网做链接的地址</span>
	                        </td>
	                         <td id="linkusTip"></td>
						</tr>
						<tr>
	                        <th><span>*</span> 联系人：</th>
							<td>
								<input type="text" style="width: 250px;float:left;" name="contact" value="<s:property value='contact' />"/><br/>
	                            <span>请填写贵站相关负责人的全名</span>
	                        </td>
	                        <td id="contactTip"></td>
						</tr>
						<tr>
	                        <th><span>*</span> E-mail：</th>
							<td>
								<input type="text" style="width: 250px;float:left;" name="email" value="<s:property value='email' />"/><br/>
	                            <span>请仔细核对您的电子邮箱地址，以便日后我们能与您取得联系</span>
	                      	</td>
	                      	<td id="emailTip"></td>
						</tr>
						<tr>
						<th><span>*</span> 交换区域：</th>
							<td>
								<select id="area" name="area" style="width: 250px;float:left;">
									<option value="1">首页</option>
									<option value="2">友情链接页</option>
									<option value="3">目录页</option>
								</select><br/>
								<span>请选择显示的链接页面</span>
							</td>
							<td id="areaTip"></td>
						</tr>
						<tr id="linkTypeTr">
							<th><span>*</span> 网站类别：</th>
							<td>
								<select style="width: 250px;float:left;" name="linkType">
								<option value="7">媒体合作</option>
								<option value="5">行业网站</option>
								</select><br/>
								<span>请如实选择网站类别，以便能顺利通过审核</span>
							</td>
							<td id="linkTypeTip"></td>
						</tr>
						<tr id="catTr" style="display:none;">
							<th><span>*</span> 选择目录：</th>
							<td>
								<select style="width: 250px;float:left;" name="catId">
									<option value='1'>农业食品</option>
									<option value='2'>服装饰件</option>
									<option value='3'>工艺品</option>
									<option value='4'>汽摩及配件</option>
									<option value='5'>箱包和礼盒</option>
									<option value='6'>化工</option>
 									<option value='7'>计算机产品</option>
									<option value='8'>建筑和装饰材料</option>
									<option value='9'>消费电子</option>
									<option value='10'>电气电子</option>
									<option value='11'>家具摆设</option>
									<option value='12'>医药卫生</option>
									<option value='13'>轻工日用品</option>
									<option value='14'>照明</option>
									<option value='15'>机械</option>
									<option value='16'>冶金矿产和能源</option>
									<option value='17'>办公文教</option>
									<option value='18'>安全和防护</option>
									<option value='19'>服务</option>
									<option value='20'>运动健身和休闲娱乐</option>
									<option value='21'>纺织</option>
									<option value='22'>五金工具</option>
									<option value='23'>玩具</option>
									<option value='24'>交通运输</option>
								</select></br>
								<span>请选择要合作的行业目录</span>
							</td>
							<td id="catIdTip"></td>
						</tr>
						<tr id="logoTr" style="display:none;">
							<th><span>*</span> logo地址：</th>
							<td>
								<input type="text" style="width: 250px;float:left;" name="imgPath" value="<s:property value='defaultLinkus' />"/><br/>
								<span>举例：http://cn.hisupplier.com/img/logo/hi_logo2.jpg</span><br/>
								<span>图片尺寸：长88px，宽31px</span>
							</td>
							<td id="imgPathTip"></td>
						</tr>
						<tr>
	                       	<th><span>*</span> 验证码：</th>
							<td>
								<input id="validateCode" type="text" style="width: 50px;float:left;margin-right:5px;" name="validateCode" value="<s:property value="validateCode"/>" autocomplete="off" maxlength="5"/>
								<a href="#" id="loadValidateCode" style="float:left;">看不清，换一张</a>
							</td>
							<td id="validateCodeTip"></td>
						</tr>
						<tr>
							<th></th>
							<td>
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
							<td></td>
						</tr>
						<tr>
	                        <th></th>
							<td style="padding-top: 20px;">
								<input type="submit" class="submit3" value="提交审请" name="submit"/>
							</td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</form>
			
			<div class="tips">
				<b style="color: rgb(255, 102, 0);">温馨提示：</b><br/>
				<div style="text-indent: 25px;">
					我们会在三个工作日内给您答复或通过您的链接，如果五个工作日内未收到我们任何方式的回复，您有权删除您网站上我们的链接;也可发邮件到link@cn.hisupplier.com,我们会及时处理您的邮件，衷心感谢您对海商网的关注与支持！
				</div>					
			</div>
		</div>
	</body>
</html>