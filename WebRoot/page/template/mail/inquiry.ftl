<#import "/main.ftl" as com> 
<@com.page title="询盘">
	<div class="h1">
		尊敬的${inquiry.getToName()}：
	</div>
	<div>
		<br />
		此询盘来自<a href="${fromSite}" target="_blank">${fromSite}</a>，由<a href="${b2bBasePath}" target="_blank">中国海商网</a>提供。
		<br />
		<br />
	</div>
	<div class="black">
		<div align="center">
			${inquiry.getSubject()}
		</div>
		<br />
		${inquiryContent}
		<br />
		<br />
		<#if inquiryFilePaths?size gt 0 ><#assign num = 0>
			 附件:
			 <#list inquiryFilePaths as inquiryFile> <#assign num = num+1>
				<a href="${inquiryFile}">附件${num}</a>
			 </#list>
			 <br/>
		</#if>
		<br />
	</div>
	<#if (basketItem.getProductList()?size+basketItem.getTradeList()?size) gt 0 >
		<div>
			<strong>咨询产品如下：</strong>
		</div>
		<div class="information" style="background:#FAFAFA; border:1px solid #EBEBEB; padding-top:5px;">
			<#if basketItem.getProductList()?size gt 0> <#assign total = basketItem.getProductList()?size> <#assign free = total> <#assign used = 0>
		    <strong>产品信息</strong>(${total})
			<ul>
				<#list basketItem.getProductList() as product> <#assign used=used+1 > <#assign free=free-1 >
					<li>
						<span class="img75X75">
							<a href="${product.getProUrl()}" target="_blank"><img src="${product.getImgPath()}" width="75" height="75" border="0"/></a>
						</span>
						<span class="text">
							<a href="${product.getProUrl()}" target="_blank">${product.getProName()}</a>
						</span>
						<span class="address">${product.getModel()}</span>
					</li>
					<#if used % 5 == 0 && free gt 0 >
						<li class="line"></li>
					</#if> 
				</#list>
			</ul>
			</#if>
			
			<#if basketItem.getTradeList()?size gt 0> <#assign total = basketItem.getTradeList()?size> <#assign free = total> <#assign used = 0>
		    <strong>商情信息</strong>(${total})
			<ul>
				<#list basketItem.getTradeList() as trade> <#assign used=used+1> <#assign free=free-1>
					<li>
						<span class="img75X75">
							<a href="${trade.getProUrl()}" target="_blank"><img src="${trade.getImgPath()}" width="75" height="75" border="0"/></a>
						</span>
						<span class="text">
							<a href="${trade.getProUrl()}" target="_blank">${trade.getTradeName()}</a>
						</span>
					</li>
					<#if used % 5 == 0 && free gt 0 >
						<li class="line"></li>
					</#if> 
				</#list>
			</ul>
			</#if>
		</div>
	</#if>
	<div class="black">
		<strong>联系信息</strong><br />
		<#if inquiry.getFromCompany()!="">
		公司名称：${inquiry.getFromCompany()}<br />
		</#if>
		联系人：${inquiry.getFromName()}<br />
		电子邮件：<a href="mailto:${inquiry.getFromEmail()}">${inquiry.getFromEmail()}</a><br />
		电话号码：${ inquiry.getFromTel() }<br />
		<#if inquiry.getFromFax()!="" >
		传真号码：${inquiry.getFromFax()}<br />
		</#if>
		<#if inquiry.getFromStreet()!="">
		公司详细地址：${inquiry.getFromStreet()}<br />
		</#if>
		<#if inquiryFromWebsites ?size gt 0 >
		网址：
			 <#list inquiryFromWebsites as fromWebsite> 
				<a href="${fromWebsite}" target="_blank">${fromWebsite}</a>
			 </#list>
			 <br/>
		</#if>
		<#if inquiry.getFromIPShow()!="">
		发件人IP：${inquiry.getFromIPShow()}
		</#if>
	</div>
</@com.page> 
