<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>	
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="home.title" /></title>
		<%--
		<script type="text/javascript">
 			$ (function(){
 				$("#newsList").attr("src","http://about.hisupplier.com/newsiframe.php");
 				$("#expIframeList").attr("src","http://about.hisupplier.com/expiframe.php");
 			});
		</script>
		 --%>
	</head>	
	<body>
		<div class="center">
			<div class="bulletin">
				<ul>
					<li>
						<s:if test="loginUser.memberType == 2">
							亲爱的海商会员：<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;为了营造一个良好的网络环境，避免不良信息在平台上展示，平台的审核机制已进行调整：“等待审核”状态的信息不再显示在平台上，只有“审核通过”和“正在审核”状态下的信息，才能在平台上显示。给您带来不便，敬请谅解！
						</s:if>
						<s:else>
							亲爱的海商会员：<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;为了营造一个良好的网络环境，避免不良信息在平台上展示，平台的审核机制已进行调整：“等待审核”状态的信息不再显示在平台上，只有“审核通过”状态下的信息，才能在平台上显示。给您带来不便，敬请谅解！
						</s:else>
						<br/>
						<span>(2013-10-18)</span>
					</li>
				</ul>
			</div>
			<s:if test="result.bulletinList && result.bulletinList.size > 0">
				<div class="bulletin">
					<ul>
						<s:iterator value="result.bulletinList" id="bulletin">
							<li>
								<s:if test="#bulletin.link != null && #bulletin.link != ''">
									<a href="<s:property value="#bulletin.link"/>"><s:property value="#bulletin.content"/></a>
									<span>(<s:property value="#bulletin.beginDate"/>)</span>
								</s:if>
								<s:else>
									<s:property value="#bulletin.content"/>
									<span>(<s:property value="#bulletin.beginDate"/>)</span>
								</s:else>
							</li>
						</s:iterator>
					</ul>
				</div>
			</s:if>
			<div class="reminded">
				<h1><s:text name="home.tip" /></h1>
				<div class="content">
					<ul>
						<s:if test="loginUser.memberType == 2">
							<li>
								<span class="span1"><s:text name="home.tip.video" /></span>
								<s:if test="result.videoCount == 0">
									<span class="span2"><a href="/video/video_list.htm"><s:text name="home.tip.videoFull"><s:param><s:property value="result.company.videoMax" /></s:param></s:text></a></span>
								</s:if>
								<s:else>
									<span class="span2"><a href="/video/video_list.htm"><s:text name="home.tip.videoAdd"><s:param><span class="digital">[<s:property value="result.videoCount" />]</span></s:param></s:text></a></span>
								</s:else>
							</li>
						</s:if>
						<li>
							<span class="span1"><s:text name="userLog.product" /></span>
							<s:if test="result.productCount == 0">
								<span class="span2"><a href="/product/product_list.htm"><s:text name="home.tip.proFull"><s:param><s:property value="result.company.productMax" /></s:param></s:text></a></span>
							</s:if>
							<s:else>
								<span class="span2"><a href="/product/product_add.htm"><s:text name="home.tip.proAdd"><s:param><span class="digital">[<s:property value="result.productCount" />]</span></s:param></s:text></a></span>
							</s:else>
						</li>
						<li>
							<span class="span1"><s:text name="home.tip.optimizePro" /></span>
							<s:if test="result.optimizeProCount == 0">
								<span class="span2"><a href="/product/product_list.htm"><s:text name="home.tip.optimizeProFull"><s:param><s:property value="result.company.optimizeProMax" /></s:param></s:text></a></span>
							</s:if>
							<s:else>
								<span class="span2"><a href="/product/product_add.htm"><s:text name="home.tip.optimizeProAdd"><s:param><span class="digital">[<s:property value="result.optimizeProCount" />]</span></s:param></s:text></a></span>
							</s:else>
						</li>
						<s:if test="result.company.memberType > 1">
							<li>
							<span class="span1"><s:text name="userLog.newProduct" /></span>
							<s:if test="result.newProCount == 0">
								<span class="span2"><a href="/newproduct/new_product_list.htm"><s:text name="home.tip.newProFull"><s:param><span class="digital"><s:property value="result.company.newProMax" /></span></s:param></s:text></a></span>
							</s:if>	
							<s:else>
								<span class="span2"><a href="/newproduct/new_product_add.htm"><s:text name="home.tip.newProAdd"><s:param><span class="digital">[<s:property value="result.newProCount" />]</span></s:param></s:text></a></span>
							</s:else>
							</li>
						</s:if>
						<li>
						<span class="span1"><s:text name="home.tip.featurePro" /></span>
						<s:if test="result.featureProCount == 0">
							<span class="span2"><a href="/product/feature_product_list.htm"><s:text name="home.tip.featureProFull"><s:param><s:property value="result.company.featureProMax" /></s:param></s:text></a></span>
						</s:if>
						<s:else>
							<span class="span2"><a href="/product/feature_product_list.htm"><s:text name="home.tip.featureProAdd"><s:param><span class="digital">[<s:property value="result.featureProCount" />]</span></s:param></s:text></a></span>
						</s:else>
						</li>
						<li>
						<span class="span1"><s:text name="userLog.trade" /></span>
						<s:if test="result.tradeCount == 0">
							<span class="span2"><a href="/trade/trade_list.htm"></a><s:text name="home.tip.tardeFull"><s:param><s:property value="result.company.tradeMax" /></s:param></s:text></a></span>
						</s:if>
						<s:else>
							<span class="span2"><a href="/trade/trade_add.htm"><s:text name="home.tip.tradeAdd"><s:param><span class="digital">[<s:property value="result.tradeCount" />]</span></s:param></s:text></a></span>
							&nbsp;&nbsp;&nbsp;
							<s:if test="result.tradeDatedCount > 0">
							<span class="span2"><a href="/trade/trade_list.htm?showReject=true&state=-2&proType=-2">过期商情<span class="digital">[<s:property value="result.tradeDatedCount" />]</span>个</a></span>
							</s:if>
						</s:else>
						</li>
						<s:if test="loginUser.showMenu == true">
						<li>
							<span class="span1"><s:text name="userLog.menu" /></span>
							<s:if test="result.menuCount == 0">
								<span class="span2"><a href="/menu/menu_group_list.htm"><s:text name="home.tip.menuFull"><s:param><s:property value="result.company.menuGroupMax" /></s:param></s:text></a></span>
							</s:if>
							<s:else>
								<span class="span2"><a href="/menu/menu_group_list.htm"><s:text name="home.tip.menuAdd"><s:param><span class="digital">[<s:property value="result.menuCount" />]</span></s:param></s:text></a></span>
							</s:else>
						</li>
						</s:if>
					</ul>
				</div>
			</div>
			<div class="member">
				<h1><s:text name="userLog.member" /></h1>
				<table>
					<tr>
						<td align="center"><s:text name="home.member.memberType" /></td>
						<td class="td1">
							<s:if test="result.memberType == 2">
								<img src="/img/ico/goldSite.gif" width="65" height="13" />
							</s:if>
							<s:else>
								<img src="/img/ico/freeSite.gif" width="65" height="13" />
							</s:else>
						</td>
						<td align="center"><s:text name="home.member.regDate" /></td>
						<td class="td1"><s:property value="result.regDate" /></td>
					</tr>
					<tr>
						<td colspan="4" class="td3"></td>
					</tr>
					<tr>
						<td align="center"><s:text name="home.member.joinYear" /></td>
						<td class="td1"><s:property value="result.jionYear" /></td>
						<td align="center"><s:text name="home.member.preLoginTime" /></td>
						<td class="td1"><s:property value="result.preLoginTime" /></td>
					</tr>
					<tr>
						<td colspan="4" class="td3"></td>
					</tr>
					<tr>
						<td align="center"><s:text name="home.member.loginTimes" /></td>
						<td class="td1"><s:property value="result.loginTimes" /></td>
						<td align="center"><s:text name="home.member.preLoginIP" /></td>
						<td class="td1"><s:property value="result.preLoginIP" /></td>
					</tr>
					<s:if test="loginUser.memberType == 2">
						<tr>
							<td colspan="4" class="td3"></td>
						</tr>
						<tr>
							<td align="center"><s:text name="home.member.availabilityDays" /></td>
							<td class="td1"><s:property value="result.availabilityDays" /><s:text name="home.member.day" /></td>
							<td align="center">&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</s:if>
				</table>
			</div>
			<div class="diary">
				<h1>
					<s:text name="userLog.title" />
					<span class="more"><a href="/basic/user_log_list.htm">&gt;&gt;<s:text name="home.more" /></a> </span>
				</h1>
				<ul>
					<s:iterator value="result.userLogList.list" id="userLog">
						<li onMouseOver="this.style.backgroundImage='url(/img/diary_table_bg.jpg)';" onMouseOut="this.style.backgroundImage='url()';">
							<span class="span1"><s:property value="#userLog.user.contact" /></span><span class="span2"><s:property value="#userLog.logTypeName" /></span><span class="span3"><s:property value="#userLog.operateName" /></span><span class="span4" title="<s:property value="#userLog.content" />"><s:property value="#userLog.contentSub" /></span><span class="span5"><s:property value="#userLog.createTime" /></span>
						</li>						
					</s:iterator>
				</ul>
			</div>
			<%--
			<div class="manual">
				<ul>
					<li><a href="http://help.hisupplier.com" target="_blank"><s:text name="home.help" /></a></li>
					<li><a href="http://help.hisupplier.com/auditing_standard/index.html" target="_blank"><s:text name="home.auditing" /></a></li>
					<li><a href="http://help.hisupplier.com/q_a/index.html" target="_blank">Q & A</a></li>
				</ul>
			</div>
			--%>
		</div>
		<%--右--%>
		<div class="right2">
			<a href="/businesscentre/index.htm"><img src="/img/bc/businesscentre_03.gif" border="0"/></a>
			<div class="news" style="margin-top: 15px;">
				<h2>
					<s:text name="home.hisNew" />
					<span class="more"><a href="http://about.hisupplier.com/hinews" target="_blank">&gt;&gt;<s:text name="home.more" /></a> </span>
				</h2>
				<div class="content">
				<s:if test="result.news">
				<ul>
					<s:iterator value="result.news" id="new">
						<li><a title="<s:property value="#new.title"/>" href="${new.getNewsDetailPath()}" target="_blank">${new.getTitleShow(13, "...");}</a></li>
					</s:iterator>
					</ul>
				</s:if>
				<%--
				<s:else>
					<iframe id="newsList" src="http://about.hisupplier.com/newsiframe.php" scrolling="no" frameborder="no" width="200px"></iframe>
				</s:else>
				 --%>
				</div>
			</div>
			<div class="news">
				<h2>
					<s:text name="home.hisExpo" />
					<span class="more"><a href="http://about.hisupplier.com/hiexhibition" target="_blank">&gt;&gt;<s:text name="home.more" /></a> </span>
				</h2>
				<div class="content2">
				<s:if test="result.exhii">
				<ul>
					<s:iterator value="result.exhii" id="exhii">
					<li><a target="_blank" title="<s:property value="#exhii.title"/>" href="${exhii.getExhiiDetailPath() }">${exhii.getTitleShow(14, "...") }</a></li>
					<span><s:property escape="false" value="#exhii.cNExhiiNoteShow"/></span>
					</s:iterator>
				</ul>
				</s:if>
				<%--
				<s:else>
					<iframe id="expIframeList" src="http://about.hisupplier.com/expiframe.php" scrolling="no" frameborder="no" width="200px" height="230px;"></iframe>
				</s:else>
				 --%>
				</div>
			</div>
		</div>
	</body>
</html>