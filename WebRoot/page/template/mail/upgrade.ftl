<#import "/main.ftl" as com> 
<@com.page title="会员升级">
	<div class="h1">
		亲爱的海商网工作人员：
	</div>
	<div>
		<br />
		会员需要升级，请尽快联系！ <br />
		${upgradeMail.upTypeName}<br />
		备注： ${upgradeMail.remark}
		<br />
		<br />
	</div>
	<div class="black"><strong>会员信息</strong><br />
		会员帐号：${upgradeMail.memberId}<br />
		公司名称：${upgradeMail.comName}<br />
		联系人：${upgradeMail.contact}<br />
		邮箱：<a href="mailto:${upgradeMail.email}">${upgradeMail.email}</a><br />
		电话：${upgradeMail.tel}<br />
		<#if upgradeMail.mobile!="" >
		手机：${upgradeMail.mobile}<br />
		</#if>
	</div>
</@com.page> 