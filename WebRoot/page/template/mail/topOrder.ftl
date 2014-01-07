<#import "/main.ftl" as com> 
<@com.page title="Topsite订购">
	<div class="h1">
		亲爱的海商网工作人员：
	</div>
	<div>
		<br />
		Topsite需要订购，请尽快联系！ <br />
		排名类型： 
		<#if topOrder.getTopType() == 1>
			搜索结果出现在第1页，￥5000/年
		</#if>
		<#if topOrder.getTopType() == 2> 
			搜索结果出现在第1页的第1位，￥20000/年
		</#if>
		<br />
		关键词： ${topOrder.keyword}<br />
		备注： ${topOrder.remark}
		<br />
		<br />
	</div>
	<div class="black"><strong>会员信息</strong><br />
		会员帐号：${topOrder.memberId}<br />
		公司名称：${topOrder.comName}<br />
		联系人：${topOrder.contact}<br />
		邮箱：<a href="mailto:${topOrder.email}">${topOrder.email}</a><br />
		电话：${topOrder.tel}<br />
		<#if topOrder.mobile!="" >
		手机：${topOrder.mobile}<br />
		</#if>
	</div>
</@com.page> 