<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>违规词解禁申请（申请列表）</title>
<meta name="memoinfo" content="温馨提示：请勿随意删除审核通过的解禁违规词，删除后对应的解禁违规词将无法再公司、产品、商情等信息中继续添加" />
</head>
<body>
	<s:property value="com.hisupplier.cn.account.message" />
	<div class="tabMenu" id="tabMenu" style="margin-bottom: 20px;">
		<ul>
			<li>
				<span><a style="text-decoration: none; color: #000;"href="/patent/patentDeblocked.htm">解禁申请</a></span>
			</li>
			<li class="current">
				<span><a style="text-decoration: none; color: #000;" href="/patent/patentDeblocked_list.htm">申请列表</a></span>
			</li>
			<li>
				<span><a style="text-decoration: none; color: #000;" href="/patent/patentDeblocked_license.htm">有效凭证</a></span>
			</li>
		</ul>
	</div>
	<form id="searchForm">
		<table class="violationsSearch">
			<tbody>
				<tr>
					<td>违规词</td>
					<td><input id="queryText" type="text" name="queryText" style="width: 130px;"></td>
					<td id="stateText"><select id="state" name="state">
							<option value="-1" <s:if test="state == -1">selected</s:if>>
								<s:text name="请选择审核状态" />
							</option>
							<option value="10">
								<s:text name="auditState.reject" />
							</option>
							<option value="15">
								<s:text name="auditState.wait" />
							</option>
							<option value="20">
								<s:text name="auditState.pass" />
							</option>
					</select></td>
				</tr>
			</tbody>
		</table>
		<div class="buttonViolations"><input type="submit" value="查询" /></div>
	</form>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#searchForm").validate({
				submitHandler : function() {
					ListTable.params["queryText"] = $("#queryText").val();
					ListTable.params["state"] = $("#state").val();
					ListTable.reload();
				}
			});

			$("#licenseImgDialog").dialog({
				bgiframe : true,
				autoOpen : false,
				height : 600,
				width : 800,
				modal : true
			});
			
			$("#causeDialog").dialog({
				bgiframe : true,
				autoOpen : false,
				height : 200,
				width : 450,
				modal : true
			});

		});
		
		function licenseShow(path) { //显示营业执照图片事件
			var title = $(this).attr("data-title");
			$("#licenseImgDialog").empty()
			$("#licenseImgDialog").append("<div style='border: 1px solid #eee;text-align: center;display: table-cell;vertical-align: middle;*display: block;*font-family:Arial;*font-size: 480px;height: 550px;width: 770px;'><img style='vertical-align: middle;' src='"+ path+ "' onload='Util.setImgWH(this, 600, 500)' /></div>")
			$("#licenseImgDialog").dialog("option", "title", title)
			$("#licenseImgDialog").dialog('open');
		}
		
		function causeShow(cause) { //显示审核不通过的原因
			$("#causeDialog").empty()
			$("#causeDialog").append(cause);
			$("#causeDialog").dialog("option", "title", "审核失败的原因")
			$("#causeDialog").dialog('open');
		}
		
		function deletePatent(num,state) {
			var url = "/patent/patentDeblocked_del.htm?tableId=" + num;
			if(state == 20) {
				deleteConfirm(url, "删除后将无法在产品、商情等信息中继续添加，你确定删除吗？");
			}else {
				deleteConfirm(url, "你确定要删除吗？");
			}
		}
		
		function updatePatent(num,state) {
			var url = "/patent/patentDeblocked_edit.htm?tableId=" + num;
			if(state == 20) {
				deleteConfirm(url, "修改此违规词解禁将重新变为等待审核，等待审核状态下，相关信息不能添加，你确定修改吗？");
			}else{
				deleteConfirm(url, "你确定要修改吗？");
			}
			
		}
	
		
	</script>
	<div id="listTable">
		<s:include value="patent_deblocked_list_inc.jsp" />
	</div>
	<div id="licenseImgDialog">
	</div>
	<div id="causeDialog">
	</div>
</body>
</html>