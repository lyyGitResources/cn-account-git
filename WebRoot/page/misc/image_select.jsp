<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<link href="/css/css.css" type="text/css" rel="stylesheet" />
<script type="text/javascript">
	ListTable.params["queryBy"] = "";
	ListTable.params["queryText"] = "";
	$(document).ready(function () {
		// 用于弹出窗口的重载
		ListTable.url = "/image/image_select.do?batch=<s:property value='batch' />&imgType=<s:property value='imgType' />&imgSrcTag=<s:property value='imgSrcTag' />&imgPathTag=<s:property value='imgPathTag' />&imgIdTag=<s:property value='imgIdTag' />";
		
		var queryText = $("#queryText").attr("oriValue");
		$("#queryText").blur(function(){
			if($(this).val() == ""){
				$(this).val(queryText);
			}
		}).focus(function(){
			if($(this).val() == queryText){
				$(this).val("");
			}
		});
		
		$("#selectForm").submit(function(){
			if($("#queryText").val() == queryText){
				$("#queryText").val("");
			}
		});
	
		$("#selectForm").validate({
			submitHandler: function(){ 
				ListTable.params["queryBy"] = "imgName";
				ListTable.params["queryText"] = $("#queryText").val();
				ListTable.params["pageNo"] = 1;
				ListTable.reload({}, true);
			}
		});
		$("#selectBtnId").unbind().bind('click',function(){
			if(Util.isChecked('imageCheckBox')){
				var array = $(":checkbox[name='imageCheckBox']:checked");
				var ids = "";
				for(var i=0;i<array.length;i++){
					ids += "," + array[i].value;
				};
				selectImage(0,'',ids.substring(1,ids.length),'');
			}else{
				alert("请至少选择一项");
			}
		});
		$("#batchSelectBtnId").unbind().bind('click',function(){
			if(Util.isChecked('imageCheckBox')){
				var array = $(":checkbox[name='imageCheckBox']:checked");
				var count = window.parent.$("[name='_multiDiv']").size() + array.length - window.parent.maxNum;
				if(count > 0){
					alert("您还能添加" + (window.parent.maxNum - window.parent.$("[name='_multiDiv']").size()) + "张图片");
				}else{
					var ids = "";
					for(var i=0;i<array.length;i++){
						ids += ";" + array[i].value;
					};
					selectImage(0,'',ids.substring(1,ids.length),'');
				}
			}else{
				alert("请至少选择一项");
			}
		});
	});
	
	//批量上传产品时选择单独一张图片
	function selectSingleImg(imgId,imgPath,imgName){
		if(imgId > 0){
			var imgIdArray = window.parent.$("#imgIdsStr").val().split(",");
			var hasId = false;
			for(var i=0;i<imgIdArray.length;i++){
				if(imgIdArray[i] == imgId){
					hasId = true;
					break;
				}
			}
			if(hasId){
				$("#selectImgDialog").dialog("close");
			}else{
				selectImage(imgId,'',imgPath + "," + imgName,'');
			}
		}
	}
	function selectImage(imgId, imgPath, imgPaths, imgName){
		if("<s:property value='imgSrcTag' />" == "txtUrl"){
			// 获取fck域的ID
			var fckIframeId = parent.$("iframe[id$='___Frame']").attr("id");
			var imgElements = "<img src='" + imgPath + "' align='absmiddle'>";
			if(imgPaths != ''){
				imgElements = '';
				var array = imgPaths.split(',');
				for(var i=0;i<array.length;i++){
					imgElements += "<img src='" + array[i] + "' align='absmiddle'>";
				}
			}
			parent.FCKeditorAPI.GetInstance(fckIframeId.replace("___Frame","")).InsertHtml(imgElements);
			parent.$("#selectImgDialog").dialog("close");
			// tinyMCE处理
			//var ed = parent.tinyMCE.activeEditor;
			//ed.focus();
			//ed.selection.setContent("<img src='" + imgPath + "' align='absmiddle'>");
			parent.$("#selectImgDialog").dialog("close");
		}else{
			// 是否图片多选中的'从图库选择'
			if("<s:property value='imgSrcTag' />" != ""){
				$("#<s:property value='imgSrcTag' />").attr("src","<%=Config.getString("img.link") %>" + imgPath);
				$("#<s:property value='imgIdTag' />").val(imgId);
				$("#<s:property value='imgPathTag' />").val(imgPath);

				$("#selectImgDialog").dialog("close");
				if($("#watermarkPart").attr("id")){
					//$("#watermarkPart").show();
				}
			}else{
				var index = 0;
				if(window.parent.$("[name='_multiDiv']").size() > 0){
					index = window.parent.$("[name='_multiDiv']:last").attr("id").replace("_multiDiv","");
				}
				var maxNum = 5;
				if(Util.isInt("<s:property value='imgIdTag' />")){
					maxNum = "<s:property value='imgIdTag' />" * 1;
				}
				if("<s:property value='batch' />" == "true"){//批量上传产品
					maxNum = window.parent.maxNum;
				}
				if(window.parent.$("[name='_multiDiv']").size() >= maxNum){
					alert("不能再添加了！");
				}else {
					// 是否已选过
					var have = false;
					$("[name='<s:property value='imgPathTag' />']").each(function(){
						if($(this).val() == imgPath){
							have = true;
						}
					});

					if(have){
						alert("你已经选择了该图片！");
						return;
					}else{
						if("<s:property value='batch' />" == "true"){//批量上传产品
							if(imgPaths != ''){
								var imgIdArray = window.parent.$("#imgIdsStr").val().split(",");
								var imgPathInfoArray = imgPaths.split(";");
								var tmpImgName;
								var tmpImgPath;
								var j = 0;
								var hasImgId = true;//是单张点击选择还是批量选择，如果单张选择则有imgId
								if(imgId <=0 || imgId == undefined){
									hasImgId = false;
								}
								for(var i=0;i<imgPathInfoArray.length;i++){
									tmpImgPath = imgPathInfoArray[i].split(",")[0];
									tmpImgName = imgPathInfoArray[i].split(",")[1];
									if(!hasImgId){
										imgId = imgPathInfoArray[i].split(",")[2];
									}
									var hasId = false;
									if(imgIdArray.length > 0){
										for(var k=0;k<imgIdArray.length;k++){
											if(imgIdArray[k] == imgId){
												hasId = true;
												break;
											}
										}
									}
									if(hasId){//已经有一张相同的图片就跳过，不显示到页面上
										continue;
									}
									if(index == 0 && window.parent.$("[name='_multiDiv']").size() == 0){
										window.parent.$("#_imageMulti").after(window.parent.getImgDiv(imgId,tmpImgPath, "<s:property value='imgPathTag' />", index, '', tmpImgName));
									}else{
										window.parent.$("[name='_multiDiv']:last").after(window.parent.getImgDiv(imgId,tmpImgPath, "<s:property value='imgPathTag' />", (j++)+parseInt(index,10)+1,'', tmpImgName));
									} 
								}
								$("#selectImgDialog").dialog("close");
							}
						}else{
							alert("选择成功，请继续选择图片！");
							if(index == 0 && window.parent.$("[name='_multiDiv']").size() == 0){
								window.parent.$("#_imageMulti").after(window.parent.getImgDiv(imgPath, "<s:property value='imgPathTag' />", index, '', imgId));
							}else{
								window.parent.$("[name='_multiDiv']:last").after(window.parent.getImgDiv(imgPath, "<s:property value='imgPathTag' />", parseInt(index,10)+1, '', imgId));
							} 
						}
					}
				}
			}
		}
	}
	function goBack() {
		$("#queryText").val("");
		$("#selectForm").submit();
	}
</script>
<form id="selectForm" action="/image/image_list.htm" method="post">
	<table>
		<tbody>
			<tr>
				<td>
					<input id="backBtn" type="button" value="后退" onclick="goBack();" style="display:none;"/>
				</td>
				<td>
					<s:if test="result.isFck == true"><input type="button" value="引用" id="selectBtnId" /></s:if>
					<s:elseif test="batch == true">
						<input type="checkbox" onclick="Util.checkAll(this, 'imageCheckBox')"/>&nbsp;全选
						<input type="button" value="引用" id="batchSelectBtnId" />
					</s:elseif>
				</td>
				<td><input value="<s:text name='image.queryText' />" name="imgName" id="queryText" type="text" oriValue="<s:text name='image.queryText' />"></td>
				<td><input class="searchButton" value="<s:text name="button.search" />" type="submit" /></td>
			</tr>
		</tbody>
	</table>
</form>

<div id="listTable">
	<s:include value="/page/misc/image_list_inc.jsp"/>
</div>