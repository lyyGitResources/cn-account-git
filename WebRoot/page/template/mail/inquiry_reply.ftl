<#import "/main.ftl" as com> 
<@com.page title="询盘回复">
	<div class="h1">
		Re:${inquiryReply.subject}
	</div>
	<div>
		<br />
		${inquiryReply.content}
		<br />
		<#if inquiryReplyFilePaths?size gt 0 ><#assign num = 0>
			 附件:
			 <#list inquiryReplyFilePaths as inquiryReplyFile> <#assign num = num+1>
				<a href="${inquiryReplyFile}">附件${num}</a>
			 </#list>
			 <br/>
		</#if>
	</div>	
</@com.page> 
<br />
<div style="width:600px; margin:0 auto; ">
	<strong>原询盘内容：</strong>
	<div style="border-top: 1px dotted #969696; margin-top:5px;">
	${inquiryReply.fromContent}
	</div>
</div>