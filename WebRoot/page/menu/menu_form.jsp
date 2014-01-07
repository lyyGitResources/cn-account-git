<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:if test="result.menu.menuId > 0">
	<s:set name="pageTitle" value="%{getText('menu.editTitle')}"></s:set>
</s:if>
<s:else>
	<s:set name="pageTitle" value="%{getText('menu.addTitle')}"></s:set>
</s:else>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			<s:property value="#pageTitle" />
			<span class="red">(<s:property value="result.group.groupName" />)</span>
			<span class="gray">
			<s:text name="menu.addTitleNotice">
				<s:param value="result.group.menuCount"/>
				<s:param value="result.group.itemMaxCount"/>
			</s:text>
			</span>
		</title>
		<meta name="memoinfo" content="<s:property value='memoInfo'/>"/>
		<script type="text/javascript" src="/js/fckeditor/fckeditor.extend.js" ></script>
		<script type="text/javascript" src="/js/fckeditor/fckeditor.js"></script>
		<script type="text/javascript">
			$(function (){
				<s:if test="result.menu.menuId > 0">
					$("#menuFrom").checkform({
						fckArea:"content",
						url:"/menu/menu_list.htm?groupId=<s:property value='result.menu.groupId' />"
					});
				</s:if>
				$("#menuFrom").validate({
					rules: {
						title: {required:true, maxlength:120}
					},
					messages: {
						title: "<s:text name='menu.title.required'/>"
					}	
				});
				$("#selectImgDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 600,
					width: 780,
					modal: true,
					close: function(){
						$("#selectImgDialog").empty();
					}	
				});
				
				$("#contentDiv").html($("#content").val());
				if($("#contentDiv").height() > 300){
					$("#contentDiv").css({height:"300px",overflow:"scroll"});
				}
				
				$("#fckButton").click(function(){
					$("#contentDiv").hide();
					fckeditor = new FCKeditor("content", 580, 300, "Admin") ;
					fckeditor.ReplaceTextarea();
					// TinyMCE_initEditer("content", "zh", 580, 300);
					// $("#content").show();
					$(this).hide();
				});
				
				if(<s:property value='result.menu.menuId'/> == 0){
					$("#fckButton").click();
				}
			});
			
			function checkMenuForm(){
				if(fckeditor){
					$('#content').val(FCKeditor_CleanWord(FCKeditorAPI.GetInstance('content').GetXHTML()));
				}
				return true;
			}
		</script>
	</head>
	<body>
		<s:if test="result.isFull== true">
		<div style="background: #FCF4CF url(/img/ico/warn.gif) no-repeat 15px 6px; border: 1px #EFDB7B solid;color: #EA4901; padding: 8px 5px 15px 60px; margin-bottom: 15px;">
		提示：信息数量已达到<s:property value="result.group.menuCount" />个，不能再添加<br />
		<a href="/menu/menu_list.htm?groupId=<s:property value="#groupId" />">返回菜单管理</a>
		</div>
		</s:if>
		<s:else>
		<div class="buttonLeft">
			<label>
				<input type="Button"  value="<s:text name='menu.list' />"  onclick="document.location.href='/menu/menu_list.htm?groupId=<s:property value='#groupId'/>'" />
			</label>
		</div>
		<form action="<s:property value='result.formAction'/>" name="menuFrom" id="menuFrom" method="post" enctype="multipart/form-data" onsubmit="return checkMenuForm();">
			<input type="hidden" name="videoId" id="videoId" value="<s:property value='result.menu.videoId' />"/>
			<table class="formTable">
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="menu.title" />：</th>
					<td><input type="text" name="title" value="<s:property value='result.menu.title' />" style="width:200px;"/></td>
				</tr>
				<tr>
					<th><s:text name="menu.imgPath" />：</th>
					<td>
						<% 
							int imgType = Image.MENU;
							String imgSrcTag = "imgSrc";
							String imgPathTag = "imgPath";
							String imgIdTag = "imgId";
						%>
						<s:set name="imgSrc" value="result.menu.imgPath75" />
						<s:set name="imgPath" value="result.menu.imgPath" />
						<s:set name="imgId" value="result.menu.imgId"/>
						<s:set name="imgExts" value="'*.jpg;*.jpeg;*.gif'" />
						<s:set name="imgSize" value="'100'" />
						<%@ include file="/page/inc/image_bar.jsp" %>
					</td>	
				</tr>
				<s:if test="loginUser.memberType == 2">
					<tr>
						<th><s:text name="menu.video" />：</th>
						<td>
							<s:set name="videoImgPath" value="result.menu.videoImgPath"></s:set>
							<s:set name="playPath" value="result.menu.playPath"></s:set>
							<s:set name="videoState" value="result.menu.videoState"></s:set>
							<%@ include file="/page/inc/video_bar.jsp"%>
						</td>
					</tr>
				</s:if>			
				<tr>
					<th><s:text name="menu.content"/>：</th>
					<td>
						<input id="fckButton" type="button" value="<s:text name="button.fckEdit" />" />
						<div id="contentDiv" style="width:580px;"></div>
						<textarea name="content" id="content" style="display:none;"><s:property value='result.menu.content' escape="false"/></textarea>
						<div class="fieldTips">
							<s:text name="menu.contentTip" />
						</div>
					</td>
				</tr>
				<tr>
					<th><s:text name="attachment" />：</th>
					<td class="fieldTips">
						<input type="hidden" id="filePath" name="filePath" value="<s:property value="result.menu.filePath"/>"/>
						<div id="filePathSelect" <s:if test="result.menu.filePath != null && result.menu.filePath != ''">style="display:none;"</s:if>>
							<div style="padding-left:1px;">
								<s:text name="menu.attachmentTip" />
								<input type="checkbox" id="check_3" onclick="$('#filePathUpload').toggle()" />
							</div>
							<div id="filePathUpload" style="display:none;">
								<table width="100%" border="0" align="center" cellpadding="5" cellspacing="1">
									<tr>
										<td>
											<input type="file" name="attachment" value=""/>
											<div class="gray">
												-&nbsp;<s:text name="attachment.format"><s:param>jpg、gif、txt、doc、pdf、xls</s:param><s:param>500</s:param></s:text>
											</div>
										</td>
									</tr>
								</table>
							</div>
						</div>
						<s:if test="result.menu.filePath != null && result.menu.filePath != ''">
							<a id="filePathA" href="<s:property value='result.menu.filePathUrl'/>" target="_blank" ><s:property value='result.menu.filePathUrl'/></a>&nbsp;&nbsp;
							<input type="button" value="<s:text name="button.delete" />" onclick="$('#filePathA').hide();$(this).hide();$('#filePathSelect').show();$('#filePath').val('');">	
						</s:if>
					</td>
				</tr>
			</table>
			<div class="buttonCenter">
				<input type="submit" value="<s:text name='button.submit' />"/> 
				<input type="reset" value="<s:text name='button.reset' />"/> 
			</div>
		</form>
		<div id="selectImgDialog"></div>
		<%@ include file="/page/inc/image_error.jsp" %>
		</s:else>
	</body>
</html>
