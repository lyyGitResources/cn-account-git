<#import "/main.ftl" as com> 
<@com.page title="广告升级">
	<div class="h1">
		亲爱的海商网工作人员：
	</div>
	<div>
		<br />
		会员需要广告升级，请尽快联系！ <br />
		广告类型：
			<#if adOrder.catName !="" > 
				广告目录：${adOrder.catName} &nbsp;&nbsp;&nbsp;广告目录ID：${adOrder.catId}<br /> 
			</#if>
			<#if adOrder.keyword !=""> 
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				广告关键词：${adOrder.keyword}<br />
			</#if>
		<br />
		备注： ${adOrder.remark}
		<br />
		<br />
	</div>
	<div class="black">
		<strong>会员信息</strong><br />
		会员帐号：${adOrder.memberId}<br />
		公司名称：${adOrder.comName}<br />
		联系人：${adOrder.contact}<br />
		邮箱：<a href="mailto:${adOrder.email}">${adOrder.email}</a><br />
		电话：${adOrder.tel}<br />
		<#if adOrder.mobile !="" > 
		手机：${adOrder.mobile}<br />
		</#if>
	</div>
</@com.page> 