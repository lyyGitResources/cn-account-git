<#import "/main.ftl" as com> 
<@com.page title="密码取回">
	<div class="h1">
		尊敬的${contact}：
	</div>
	<div>
		<br />
		您好！感谢您使用中国海商网。您提交了忘记密码的申请，以下是您的会员登录信息：<br />
		会员帐号：
		<#if editMemberId == 'true'>
			未设置 <a href="${accountBasePath}/member/company_edit.htm">设置</a><br />
		<#else>
			${memberId}<br />
		</#if> 
		密码：${passwd}<br />
		邮箱：${email}<br />
		你也可以登录<a href="${accountBasePath}/member/passwd_edit.htm">后台</a>重置密码！<br /><br />
		如果您未提交这样的申请或者怀疑有人未经允许擅自登录您的后台，请联系我们的<a href="${service_contact}">客服专员</a>。
		<br />
		<br />
	</div>
	<div class="black">
		中国海商网团队敬上<br />
        <a href="mailto:${service_email}">${service_email}</a> 
	</div>	
</@com.page> 