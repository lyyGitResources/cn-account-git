<#import "/main.ftl" as com> 
<@com.page title="客户服务">
	<div class="h1">
			亲爱的海商网工作人员：
	</div>
	<div>
		${serviceMail.contact}会员${serviceMail.subject}，请尽快联系！ <br />
			服务项目：${serviceMail.subject}<br />
			详细内容：${serviceMail.content}<br />
			<#if serviceMail.filePath?exists && serviceMail.filePath!="">
			附件：	<a href="${serviceMail.filePath}" >${serviceMail.filePath}</a>
			<br />
			<br />
			</#if>		
	</div>
	<div class="black"><strong>会员信息</strong><br />
		会员帐号：${serviceMail.memberId}<br />
		公司名称：${serviceMail.comName}<br />
		联系人：${serviceMail.contact}<br />
		邮箱：${serviceMail.email}<br />
		电话：${serviceMail.tel}<br />
		<#if serviceMail.fax!="" >
		传真：${serviceMail.fax}<br />
		</#if>
		方便联系方式：${serviceMail.contactMode} 
	</div>
</@com.page>