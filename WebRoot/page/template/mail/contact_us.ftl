<#import "/main.ftl" as com> 
<@com.page title="客户服务">
	<div class="h1">
			亲爱的海商网工作人员：
	</div>
	<div>
		${serviceMail.contact} 客户需要知道 ${serviceMail.reason}，请尽快联系！ <br />
			主题：${serviceMail.subject}<br />
			内容：${serviceMail.content}<br />
	</div>
	<div class="black"><strong>客户信息</strong><br />
		会员账号：${serviceMail.memberId}<br />
		姓名：${serviceMail.contact}<br />
		电子邮箱：${serviceMail.email}<br />
	</div>
</@com.page>