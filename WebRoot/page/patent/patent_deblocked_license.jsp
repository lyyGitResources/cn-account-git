<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>
营业执照和有效凭证
</title>
<meta name="memoinfo" content="温馨提示：请勿随意删除审核通过的解禁违规词，删除后对应的解禁违规词将无法再公司、产品、商情等信息中继续添加"/>
<script type="text/javascript">

function processSuccess(file,jsonDataArray){
	if(jsonDataArray[0].msg && jsonDataArray[0].msg == "fail"){
		if(jsonDataArray[0].errorTip){
			alert(decodeURIComponent(jsonDataArray[0].errorTip));
		}else {
			alert("数据存储出错，请确定是否选择了合适的文件格式和文件大小");
		}
	}else{
		var params = {imgType:$("#imgType").val(),random:Math.random()};
		location.reload();
	}
} 

$(document).ready(function () {
	upload = new HisupplierUpload({
		//file_size_limit : "100",	// 100K
		file_types : "*.jpg;*.jpeg;*.gif",
		button_placeholder_id : "spanButtonPlaceholder1",
		file_size_limit : "500", //大小限制
		flash_url : "/img/swf/swfupload.swf",
		custom_settings : {
			onlyStoreToDisk : false,//存储到数据库中
			processData : processSuccess
		}
	});
	
	$("#imgDetailDialog").dialog({
		bgiframe: true,
		autoOpen: false,
		height: 350,
		width: 525,
		modal: true,
		close: function(){
			$("#selectImgDialog").empty();
		}		
	});
	
	$("#searchForm").validate({
		submitHandler: function(){ 
			ListTable.reload({
				queryBy: "imgName",
				queryText: $("#queryText").val(),
				pageNo:1
			}, true);
		}
	});
	
});	
</script>
</head>
<body>
	<s:property value="com.hisupplier.cn.account.message" />
	<div class="tabMenu" id="tabMenu" style="margin-bottom: 20px;">
		<ul>
			<li>
				<span><a style="text-decoration: none; color: #000;" href="/patent/patentDeblocked.htm">解禁申请</a></span>
			</li>
			<li>
				<span><a style="text-decoration: none; color: #000;" href="/patent/patentDeblocked_list.htm">申请列表</a></span>
			</li>
			
			<li class="current">
				<span><a style="text-decoration: none; color: #000;" href="/patent/patentDeblocked_license.htm">有效凭证[<s:property value='result.licenseCount' />]</a></span>
			</li>
		</ul>
	</div>
	<form id="searchForm" class="searchForm" method="post">
		<input type="hidden" id="comId" name="comId" value="<s:property value="loginUser.comId"/>"/>
		<input type="hidden" id="imgType" name="imgType" value="<s:property value="result.licenseType"/>"/>
		<table style="width: 725px;">
			<tbody>
				<tr>
					<td style="text-align: left;">
							<s:if test="result.isImgFull">
								<div style="display:none;float:left;padding-right:2px;"><span id="spanButtonPlaceholder1"></span></div>
								<div style="display:block;float:left;padding-right:2px;">
									<img src="/img/button_disable.gif" onmousemove="javascript:$(this).next().show();" onmouseout="javascript:$(this).next().hide();" />
									<div style="display:none;position: absolute;margin-top:-48px;*margin-top:-25px;*margin-left:-72px;padding:0px 4px;width:140px;height:22px;border:1px solid #C4D8E6;background: #E2EBF7;line-height: 22px;">已达到图片上传数量限制</div>
								</div>
							</s:if>
							<s:else>
								<div style="display:block;float:left;padding-right:2px;">
									<span id="spanButtonPlaceholder1"></span>
								</div>
							</s:else>
					</td>
					<td style="text-align: right;">
						<input value="请输入图片名" oriValue="请输入图片名" name="imgName" id="queryText" type="text"> 
						<input class="searchButton" value="<s:text name="button.search" />" type="submit">
					</td>
				</tr>
			</tbody>
		</table>
	</form>
	<div class="pageTips">
			<div style="float: left; width: 60%" >
				温馨提示：<br/>
				♦  双击图片名称，可进行图片名称修改<br/>
				♦  每个用户可以上传15张
			</div>
			<div>图片格式<br/>
				1.	jpg、jpeg、gif格式<br/>
				2.	超过500K的图片禁止上传<br/>
			</div>
	</div>
	<div id="listTable" style="padding:10px 0 0 0;">
		<%int imgType = Image.LICENSE;%>
		<s:include value="/page/misc/image_list_inc.jsp"/>
	</div>
	<div id="imgDetailDialog"></div>
</body>
</html>