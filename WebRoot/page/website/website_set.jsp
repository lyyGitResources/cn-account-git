<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	    <title>网站设置</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
	</head>
	
	<body>
		<s:if test="result.website.domain!=null && result.website.domain!=''">
			<div class="pageTips">
				<span>提示：</span>
				<ul>
					<li>“商情展示方式设置”仅对三级域名（会员网站）有效，风格网站上无商情板块。</li>
					<li>“浮动框设置”仅对风格网站有效，三级域名（会员网站）无浮动框板块。</li>
					<li>其他设置对三级域名（会员网站）和风格网站均有效。</li>
					<li>“添加其他版本网站”仅对风格网站有效。</li>
				</ul>
			</div>
		</s:if>
		<form id="form2" name="form2" method="post" action="/website/website_set_save.htm">
			<input type="hidden" name="website.domain" value="${result.website.domain }" />
			<div class="webTitleBox">
				<div class="topBg">
				<div class="tl"></div>
				<div class="tc">
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th style="width:182px;">设置组展示方式：</th>
							<td></td>
							<th style="width:210px;">设置产品展示方式：</th>
							<td></td>
							<th style="width:180px;">设置商情展示方式：</th>
						</tr>
					</table>
				</div>
				<div class="tr"></div>
				</div>
			    
				<div class="mBg">
					<ul>
						<li>
							<div>
								<div>
									<img src="/img/web_01.jpg" />
								</div>
								<div>
									<input name="groupFold" type="radio" value="true" <s:if test="result.website.groupFold==true">checked</s:if> />
									折叠第二层<br/>和第三层
								</div>
							</div>
							<div>
								<div>
									<img src="/img/web_03.jpg" />
								</div>
								<input name="groupFold" type="radio" value="false" <s:if test="result.website.groupFold==false">checked</s:if> />
								展示所有组
							</div>
						</li>
						<li>
							<div>
								<div> 
									<img src="/img/web_02.jpg" />
								</div>
								<input name="productListStyle" type="radio" value="1" <s:if test="result.company.productListStyle==1">checked</s:if> />
								竖排
							</div>
							<div>
								<div>
									<img src="/img/web_04.jpg" />
								</div>
								<input name="productListStyle" type="radio" value="2" <s:if test="result.company.productListStyle==2">checked</s:if> />
								横排
							</div>
						</li>
						<li>
							<div>
								<div>
									<img src="/img/web_02.jpg" />
								</div>
								<input name="tradeListStyle" type="radio" value="1" <s:if test="result.company.tradeListStyle==1">checked</s:if>/>
								竖排
							</div>
							<div>
								<div>
									<img src="/img/web_04.jpg" />
								</div>
								<input name="tradeListStyle" type="radio" value="2" <s:if test="result.company.tradeListStyle==2">checked</s:if> />
								横排
							</div>
						</li>
					</ul>
				</div>
			    
				<div class="footBg">
					<div class="fl"></div>
					<div class="fr"></div>
				</div>
			</div>
			
				<div class="webTitleBox">
				<div class="topBg">
					<div class="tl"></div>
					<div class="tc2"> 聊天软件设置</div>
					<div class="tr"></div>
				</div>
				<div class="mBg2">
					<s:if test="result.website.domain!=null && result.website.domain!=''">
						<div style="float: left; width: 290px; border-right:1px dashed #BDCDCD;">
							<div class="webTitleBox">
								<span style="color:#353535; font-weight:normal; font-size:12px;">&nbsp;&nbsp;&nbsp;<strong>风格网站：</strong>浮动框
								<div class="mBg2">
									<table style="margin-left:5px;">
										<tr>
											<th>是否显示:</th>
											<td>
												<div>
													<s:radio list="#{'true':'是','false':'否'}" name="chatTip"
														theme="simple" value="%{result.website.chatTip}" disabled="%{!result.adminHasTalk}"/>
												</div>
											</td>
										</tr>
									</table>
									<div style=" clear:both;"></div>
								</div>
							</div>
						</div>
					</s:if>
					<div style="width: auto; float: left; padding-left: 20px;">
					<s:if test="result.website.domain!=null && result.website.domain!=''">
					<span style="color:#353535; font-weight:normal; font-size:12px;">
						<strong>风格网站和三级域名：</strong>聊天账号设置（可多选，显示于网站首页的<br/>在线服务栏目）
					</span>
					<br/>
					</s:if>
					<table width="100%">
					<tr>
					<s:iterator value="result.talkList" id="talk" status="st">
						<td width="50%">
						<input type="checkbox" value="<s:property value='#talk.id' />" name="talkIds" <s:if test="#talk.show">checked</s:if> />
						<s:property value="#talk.name" /> : 
						<s:property value="#talk.review" escape="false"/>
						</td>
						<s:if test="#st.index % 2"></tr><tr></s:if>
					</s:iterator>
					</tr>
					</table>
					<div style=" clear:both;"></div>
					</div>
				</div>
				<div class="footBg">
					<div class="fl"></div>
					<div class="fr"></div>
				</div>
			</div>
			
			<s:if test="(result.website.domain != null && result.website.domain != '') || 
				result.company.memberType == 2 ">
				<div class="webTitleBox">
					<div class="topBg">
						<div class="tl"></div>
						<div class="tc2">二维码设置：</div>
						<div class="tr"></div>
					</div>
					<div class="mBg2">
						<table class="formTable" style="border: 0px;">
							<tr>
								<s:if test="result.company.memberType == 2">
									<th>三级域名:</th>
									<td>
										<div>
											<s:radio list="#{'true':'是','false':'否'}" name="showroomQR" 
												theme="simple" value="%{result.company.showQR}"/>
										</div>
									</td>
								</s:if>
							
								<s:if test="result.website.domain!=null && result.website.domain!=''">
									<th>风格网站:</th>
									<td>
										<div>
											<s:radio list="#{'true':'是','false':'否'}" name="indieQR" 
												theme="simple" value="%{result.website.showQR}" />
										</div>
									</td>
								</s:if>
							</tr>
						</table>
					</div>
				</div>
			</s:if>
			
			<div class="webTitleBox">
				<div class="topBg">
					<div class="tl"></div>
					<div class="tc2"> 设置模块标题： </div>
					<div class="tr"></div>
				</div>
				<div class="mBg2">
					<table border="0" cellspacing="5" cellpadding="0" class="t2">
						<tr>
							<th align="right" valign="baseline">产品模块：</th>
							<td align="left" valign="baseline">主标题
								<input name="product_list" type="text" value="<s:property value="result.moduleTitleMap['product.list']"/>" />次标题
								<input name="product_group" type="text" value="<s:property value="result.moduleTitleMap['product.group']"/>" />
								<input name="product_special" type="text" value="<s:property value="result.moduleTitleMap['product.special']"/>" />
								<input name="product_feature" type="text" value="<s:property value="result.moduleTitleMap['product.feature']"/>" />
							</td>
						</tr>
						<tr>
							<th align="right" valign="baseline">公司模块：</th>
							<td align="left" valign="baseline">主标题
								<input name="company_introduce" type="text" value="<s:property value="result.moduleTitleMap['company.introduce']"/>" />次标题
								<input name="company_brief" type="text" value="<s:property value="result.moduleTitleMap['company.brief']"/>" />
								<input name="company_member" type="text" value="<s:property value="result.moduleTitleMap['company.member']"/>" />
							</td>
						</tr>
						<tr>
							<th align="right" valign="baseline">服务模块：</th>
							<td align="left" valign="baseline">主标题
								<input name="service_contact" type="text" value="<s:property value="result.moduleTitleMap['service.contact']"/>" />次标题
								<input name="service_online" type="text" value="<s:property value="result.moduleTitleMap['service.online']"/>" />
							</td>
						</tr>
						<tr>
							<th align="right" valign="baseline">商情模块：</th>
							<td align="left" valign="baseline">主标题
								<input name="offer_list" type="text" value="<s:property value="result.moduleTitleMap['offer.list']"/>" />
							</td>
						</tr>
						<tr>
							<th align="right" valign="baseline">视频模块：</th>
							<td align="left" valign="baseline">主标题
								<input name="video_list" type="text" value="<s:property value="result.moduleTitleMap['video.list']"/>" />
							</td>
						</tr>
						<tr>
							<th align="right" valign="baseline">询盘模块：</th>
							<td align="left" valign="baseline">主标题
								<input name="inquiry_online" type="text" value="<s:property value="result.moduleTitleMap['inquiry.online']"/>" />
							</td>
						</tr>
						<s:if test="result.website.domain!=null && result.website.domain!=''">
						<tr>
							<th align="right" valign="baseline">搜索框：</th>
							<td align="left" valign="baseline">提示语
								<input style="width: 200px" maxlength name="search_alert" type="text" value="<s:property value="result.moduleTitleMap['search.alert']"/>" />
							</td>
						</tr>
						</s:if>
					</table>
					<div style=" clear:both;"></div>
				</div>
				<div class="footBg">
					<div class="fl"></div>
					<div class="fr"></div>
				</div>
			</div>
			<s:if test="result.website.domain!=null && result.website.domain!=''">
			<div class="webTitleBox">
				<div class="topBg">
					<div class="tl"></div>
					<div class="tc2"> 添加其他版本网站（风格网站）： </div>
					<div class="tr"></div>
				</div>
				<div class="mBg2">
				<table class="formTable" style="border: 0px;">
		    		<tr><th>网站名称：</th><td><input type="text" name="siteName" maxlength=120 value="<s:property value="result.website.siteName"/>"/></td></tr>
		    		<tr><th>网站地址：</th><td><input type="text" name="siteLink" maxlength=120 value="<s:property value="result.website.siteLink"/>"/></td></tr>
		    	</table>
				</div>
                <div class="footBg">
                    <div class="fl"></div>
                    <div class="fr"></div>
                </div>
            </div>
			</s:if>
			
			<div class="buttonCenter">
				<input type="submit" value="<s:text name="button.submit" />">&nbsp;&nbsp;
			</div>
		</form>
		<%-- 
	    <script type="text/javascript" src="/cas/track.js"></script>
	    --%>
	</body>
</html>
