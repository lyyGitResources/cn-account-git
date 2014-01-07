<#import "/main.ftl" as com> 
<@com.page title="会员注册信息">
	<div class="h1">
		尊敬的${contact}：
	</div>
	<div>
		<br />
		
		<#if regMode == 1 || regMode == 10>
			欢迎您加入中国海商网（<a href="${b2bBasePath}">${b2bBasePath}</a>），
		</#if>
		<#if regMode == 4>
			恭喜您成功发布求购信息，并已成为中国海商网（<a href="${b2bBasePath}">${b2bBasePath}</a>）的会员，
		</#if>
		<#if regMode == 5>
			<#if tradeAlertKeyword != ''>
				恭喜您成功订阅关键词：&nbsp;<strong>${tradeAlertKeyword}</strong>&nbsp;，并已成为中国海商网（<a href="${b2bBasePath}">${b2bBasePath}</a>）的会员，
			<#else>
				恭喜您成功订阅目录：&nbsp;<strong>${tradeAlertCatName}</strong>&nbsp;，并已成为中国海商网（<a href="${b2bBasePath}">${b2bBasePath}</a>）的会员，
			</#if> 
		</#if>
		<#if regMode == 6>
			恭喜您询盘发送成功，您已成为中国海商网（<a href="${b2bBasePath}">${b2bBasePath}</a>）的会员，
		</#if>
		您的会员登录信息为： <br />
		会员帐号：
		<#if editMemberId == 'true'>
			未设置 <a href="${accountBasePath}/member/company_edit.htm">设置</a><br />
		<#else>
			${memberId}<br />
		</#if> 
		密码：${passwd}<br />
		邮箱：${email}<br />
		<a href="${accountBasePath}">立即登录</a><br /><br />
		从现在起，您可以在中国海商网推广产品，创建商业机会，网上轻松谈生意! <br />
		<strong>
	1、发布信息
		</strong><br />
		发布的产品及供求信息，将自动加入中国海商网，并放到搜索引擎上做优化，让买家轻易找到您的产品。 <br />
		<img src="${accountBasePath}/img/mail/cn_hisupplier_04.jpg" />&nbsp;&nbsp;<a href="${accountBasePath}/product/product_add.htm">发布产品信息</a> <br />
		<img src="${accountBasePath}/img/mail/cn_hisupplier_04.jpg" />&nbsp;&nbsp;<a href="${accountBasePath}/trade/trade_add.htm">发布商情信息</a><br />
		<strong>
	2、订阅商情
		</strong><br />
		免费提供最新供应/采购信息。 <br />
		<img src="${accountBasePath}/img/mail/cn_hisupplier_04.jpg" />&nbsp;&nbsp;<a href="${accountBasePath}/alert/trade_alert_add.htm">订阅商情</a><br />
		<strong>
	3、自助建站
		</strong><br />
		您可以免费建立一个公司的二级域名网站，向您的业务伙伴及客户展示您的公司资料。<br />
		<img src="${accountBasePath}/img/mail/cn_hisupplier_04.jpg" />&nbsp;&nbsp;<a href="${accountBasePath}">开始创建您的免费网站 </a><br /><br />
	</div>
	<div align="center"><br />
		<a href="${accountBasePath}"><img src="${accountBasePath}/img/mail/signinCh2.gif" width="130" height="40" border="0"></a><br /><br />
	</div>
	<div>
		<strong>想了解更多如何使用中国海商网及服务信息，请登录<a href="http://help.cn.hisupplier.com">帮助中心</a>！</strong>
	</div>
	<div class="gray"> 衷心希望中国海商网带给您无限商机！ <br />
	        如果有任何疑问，欢迎您随时 <a href="${service_contact}"	target="_blank">联系我们</a>！
	</div>
	<div> <br />
		中国海商网团队敬上 <br />
        <a href="mailto:${service_email}">${service_email}</a> 
    </div>
</@com.page> 