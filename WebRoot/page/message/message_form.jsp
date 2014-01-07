<%@ page language="java" contentType="text/html;charset=UTF-8" errorPage="/page/error/500.jsp" pageEncoding="UTF-8"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>短信群发</title>
		<link rel="stylesheet" href="<%=Config.getString("img.base")%>/js/lib/treeview/jquery.treeview.css" />
		<link href="<%=Config.getString("img.base")%>/js/lib/ui/themes/base/ui.all.css" type="text/css" rel="stylesheet" />
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/ui/ui.core.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/ui/ui.dialog.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.cookie.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/treeview/jquery.treeview.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/util.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				<s:if test="result.gift!=-1">
				$("#tipDialog").dialog({
						bgiframe: true,
						autoOpen: true,
						height: 230,
						width: 362,
						dialogClass: "Pop",
						modal: true,
						closeOnEscape: true
					});
				</s:if>
				$("#phoneBook,#template").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 430,
					width: 762,
					modal: true,
					closeOnEscape: true
				});
				
				$("#form2").submit(function(){
					if($("#phoneStr").val() == "" || $("#content").val() == ""){
						alert("请输入电话号码和短信内容！");
						return false;
					}
				});
			});
			function countNum(num, obj, showObj) {
				var spare = num - obj.val().length;
				if(spare >= 0){
					showObj.html(spare);
				}else{
					obj.val(obj.val().substring(0, num));
					showObj.html(0);
				}
			}
			function getDialog(id,url,title){
				$(id).html("Loading...");
				$.ajax({
				  	url: url,
				  	cache: false,
				  	success: function(html){
				    	$(id).html(html);
				  	}
				}); 
				$(id).dialog("option", "title", title);
				$(id).dialog("open");
			}

		</script>

		<style type="text/css">
.Pop {
	background: url(/img/SMS_15.jpg) no-repeat;
	width: 366px;
	height: 234px;
	border: 8px solid #FFFFFF;
}

.Pop .text {
	float: right;
	display: block;
	margin-top: 20px;
	line-height: 30px;
	font-size: 16px;
	color: #333332;
	text-align: center;
	width: 100%;
}
</style>
</head>

	<body>
	<div id="tipDialog" style="display:none;"> 
		<span class="text"> <span style="color:#FF0000; font-weight:bold; padding-left:10px;">恭喜!您已开通短信群发服务！ </span><br />
		<s:if test="result.gift>0">
		我们已免费赠送给您<span style="color:#FF0000;"><s:property value="result.gift" />条</span>短信<br /><br />
		<img src="/img/SMS_17.jpg" onclick="$('#tipDialog').dialog('close');"/>
		</s:if><s:else>
			<br /><br />
		<img src="/img/SMS_18.jpg" onclick="$('#tipDialog').dialog('close');"/>
		</s:else>
		</span>
	</div>
			<div class="SMSbox">
				<div class="tabMenu">
					<ul>
						<li class="current">
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
						<li>
							<span onclick="location.href='/message/charge.htm'">充值</span>
						</li>
					</ul>
				</div>
				<div class="balance">
					剩余短信（条数）：<strong><s:property value='result.number'/></strong>
				</div>
				<form id="form2" name="form2" method="post" enctype="multipart/form-data" action="/message/send.htm">
					<s:token />
					<div class="tab">
						发送给：&nbsp;&nbsp;&nbsp;
						<input id="phoneStr" name="phoneStr" value="<s:property value='phoneStr' />" type="text" />
						<span class="button5Span">
						<input type="button" value="从电话簿选取" onclick="getDialog('#phoneBook','/message/ajaxPhoneBook.do','电话薄');" class="button5" /></span>
						<br />
						<span>多个号码请用逗号隔开，支持中国移动、中国联通、中国电信、小灵通所有手机用户。</span>
					</div>
					<div class="form">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<th>
									短信内容：
								</th>
								<td>
									<textarea id="content"
										onkeyup="countNum(60,$(this),$('#count'));" name="content"
										style="width: 394px; height: 83px;"><s:property value="content" /></textarea>
									<a
										href="javascript:getDialog('#template','/message/ajaxTemplate.do','常用短语');">常用短语</a>
									<div class="gray">
										(剩余
										<span class="star" id="count">60</span>个字符)
									</div>
								</td>
							</tr>
							<tr>
								<th></th>
								<td>
									<input type="checkbox" name="save" value="true"
										<s:if test="save==true">checked</s:if> />
									保存到我的短语
								</td>
							</tr>
							<tr>
								<th>
									个性签名：
								</th>
								<td>
									<label>
										<input type="text" name="signature" value="<s:property value='signature' />" maxlength=5 />
										&nbsp;&nbsp; 最多5个字符
									</label>
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
								<td>
									<label>
										<input type="submit" value="发送" class="button" />
									</label>
								</td>
							</tr>
						</table>
					</div>
				</form>
				<div class="send">
					<span>发送需知</span>
					<textarea readonly=true
						style="width: 722px; * width: 720px !important; * width: 720px; height: 154px; padding-left: 10px; font-size: 12px; line-height: 20px; background: #fff;">根据中国移动及中国联通公司对短信内容中禁止的规定，以下内容不能出现在短消息中，请您认真核对，避免出现无法发送短信的情况！

第一类 法轮功
洪志/法轮/宏志/真善忍/大法/fa lun/falun/发轮/发伦/发抡/发沦/发囵/发仑/发纶/法纶/法仑/法囵/法沦/法抡/法伦/功友/弟子/师傅/师父/法论/发论/法.轮.功/法 轮 功/自焚/自焚/玄机/现身

第二类 国家政治安全
江独裁/江八点/江泽民/李鹏/朱容基/胡锦涛/温家宝/锦涛/十六大/共产党/政治风波/疆独/民猪/民运/ 古怪歌/推翻/示威/政变/静坐/分裂/台*湾/吕秀莲/独立/西藏/中华民国/造反/新华内情/达赖/镇压/东突/开放/游行/上访/罢课/罢工/集会/广闻/ 打倒/压迫/反革命/疆独/无能/教徒/人权/迫害/共产党/吕秀莲/正法/预约/日本/反日/抗日/小泉/靖国神社/日货/钓鱼岛/涉日/香港总部/主席/暴乱/窃听器/弹药/枪支/现身

第三类 黄赌毒
六合彩/色情/嫖娼/三陪/他妈的/龙卷风/淫秽/黄色/非典/包赢/日他/Soccer01.com/中奖/大奖/一等奖/特等奖/黑庄/13423205670/畜生/蠢猪/婊子/王八蛋/迷药/九码/六码/三码/干你娘/妓女/ fuck/强奸/ dafa/小鸡鸡/操你/鸡巴/日你妈/傻B/ SIM卡抽奖/操你娘/av

第四类 新闻
人民大众/时事参考/人民内情真相/新华举报/鸡毛信文汇/人民真实报道/大参考/大纪元/杂志/联总之声/传单/舆论/美国之音/人民报讯/E周刊/博讯/人民报/奥运/中俄边界新约/国研新闻邮件/简鸿章/新闻封锁/人民大众时事参考/鸡毛信文汇/联总之声传单/九、评/九.评/九评/九-评/猛料/cnd/Yugoslavia/中国（大陆）

第五类 民族
突厥斯坦/印尼伊斯兰祈祷团/东突厥斯坦/伊斯兰运动/拉登/拉丹/自由运动/回民/天葬

第六类 不正当竞争
中国移动通信/发财诗/发财/小灵通/CDMA/绿色环保手机/IP17908/语音/拨打/合约/广告/群发/贷款/7.310/9.635/兆赫/灵动卡/中国银联/刷卡消费/银行联合管理局/</textarea>
				</div>
			</div>
			<div id="phoneBook"></div>
			<div id="template"></div>
	</body>
</html>
