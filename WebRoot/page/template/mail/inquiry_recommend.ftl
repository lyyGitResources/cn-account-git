<#import "/main.ftl" as com> 
<@com.page title="推荐询盘">
	<div class="h1">
		尊敬的${inquiry.getToName()}：
	</div>
	<div>
		<br />
		此询盘来自<a href="${fromSite}" target="_blank">${fromSite}</a>，由<a href="${b2bBasePath}" target="_blank">中国海商网</a>提供。
		<br />
		<br />
	</div>
	<div class="black">此客户需要购买<strong>${catName}</strong>，如有意向，请尽快联系！<br />
		<br />
		<br />
		<strong>联系信息 </strong><br />
		公司名称：${ inquiry.getFromCompany()}<br />
		联系人：${inquiry.getFromName() }<br />
		电子邮件：<a href="mailto:${ inquiry.getFromEmail() }">${ inquiry.getFromEmail() }</a><br />
		电话号码：${inquiry.getFromTel() } <br />
		<#if inquiry.getFromFax()!="" >
		传真号码： ${inquiry.getFromFax() } <br />
		</#if>
		公司详细地址：${inquiry.getFromStreet() } <br />
		
		<#if inquiryFromWebsites ?size gt 0 >
		网址：
			 <#list inquiryFromWebsites as fromWebsite> 
				<a href="${fromWebsite}" target="_blank">${fromWebsite}</a>
			 </#list>
			 <br/>
		</#if>
	</div>
</@com.page> 