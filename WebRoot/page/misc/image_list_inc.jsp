<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<link rel="stylesheet" href="/css/commons.css" type="text/css"></link>
<script type="text/javascript">
	$(function(){
		$("[name='imageDelete']").each(function(){
			$(this).click(function(){
				var url = "/image/image_delete.htm?imgId=" + $(this).attr("imgId")
																		+ "&pageNo=" + $(this).attr("pageNo")
																		+ "&imgType=" + $(this).attr("imgType");
				deleteConfirm(url);
			});
		});
		if($("#queryText").val() == '' || $("#queryText").val().indexOf('请输入') != -1){
			$("#backBtn").hide();
		}else{
			$("#backBtn").show();
		}
	});
	function detailShow(index){
		$("#imgDetailDialog").html($("#detail"+index).html());
		$("#imgDetailDialog").dialog('open');
	}
</script>
<%@ include file="/page/inc/image_error.jsp" %>
	<s:iterator value="result.listResult.list" id="image" status="st">
		<div style="float: left;text-align: center;padding-bottom:10px;margin-right: 5px;">
			<div id="detail<s:property value='#st.index + 1'/>" style="display:none; position:absolute; clean: both;padding:20px;text-align: center;z-index: 4;background-color: #fff; width:455px;">
				<div style="float:left;">
					<div class="listImgBox240">
						<span><img onclick="javascript:$('#imgDetailDialog').dialog('close');"  src="<s:property value='#image.imgPathS' />" class="imgLoadError" onload="Util.setImgWH(this, 240, 240)"/></span>
					</div>
				</div>
				<div style="text-align:left;padding:0 0 10px 10px;float:left; width:200px;word-wrap:break-word;word-break:break-all;">
					图片名称：<span class="imgNameSpan<s:property value='#st.index + 1'/>" ondblclick="ListTable.edit(this,'/image/image_name_edit.htm','imgId=<s:property value='#image.imgId' />&imgName=');"><s:property value='#image.imgName' /></span>
					<br /><br />
					图片大小：<s:property value='#image.imageSizeText' />
					<br /><br />
					图片格式：<s:property value='#image.format' />
				</div>
				<div style="clear:both;color:red;padding-top:20px;text-align:center; overflow: hidden;">温馨提示：双击图片名称，进行图片名称修改，单击图片返回列表</div>
			</div>
			<div style="margin-bottom: 5px;width:130px;overflow: hidden; padding: 10px 5px;border:1px solid #ccc;">
				<s:if test="result.isFck == true">
					<div style="height:20px;text-align: left;padding-bottom:5px;">
						<input id="checkbox<s:property value='#st.index + 1'/>" type="checkbox" name="imageCheckBox" value="<s:property value='#image.imgPathS' />"/>
						<select style="width:100px;" onchange="$('#img<s:property value='#st.index + 1'/>').attr('src',this.value);$('#checkbox<s:property value='#st.index + 1'/>').attr('checked','checked').val(this.value);">
							<s:iterator value="#image.scaleList" id="scale" status="st">
								<option value="<s:property value='#scale.split("=")[1]' />" <s:if test="(#st.index+1) == #image.scaleList.size">selected="selected"</s:if>><s:property value='#scale.split("=")[0]' /></option>
							</s:iterator>
						</select>
					</div>
				</s:if>
				<div class="inputleft">
					<s:if test="batch == true">
						<img src="/img/checkmark.jpg" title="选择" onclick="javascript:selectSingleImg(<s:property value='#image.imgId' />,'<s:property value='#image.imgPath' />','<s:property value='#image.imgName' />');" style="cursor:pointer;"/>
					</s:if>
					<br />
					<br />
					<span>
						<s:if test="result.isFck == true">
							<img src="/img/checkmark.jpg" title="选择" onclick="javascript:selectImage(0,$('#img<s:property value='#st.index + 1'/>').attr('src'),'','');" style="cursor:pointer;"/>
						</s:if>
						<s:else>
							<s:if test="batch == true">
								<input id="checkbox<s:property value='#st.index + 1'/>" type="checkbox" name="imageCheckBox" value="<s:property value='#image.imgPath' />,<s:property value='#image.imgName'/>,<s:property value='#image.imgId' />"/>
							</s:if>
							<s:else>
								<s:property value='#image.operate' escape="false"/>
							</s:else>
						</s:else>
					</span>
					<br />
					<br />
					<s:if test="result.isSelect == false">
						<img src="/img/detail.jpg" onclick="detailShow(<s:property value='#st.index + 1'/>);" title="详情" style="cursor:pointer;"/>
					</s:if>
				</div>
				<div style="float:right">
					<div class="listImgBox100">
					<s:if test="result.isFck == true">
						<img id="img<s:property value="#st.index + 1"/>" src="<s:property value='#image.imgPathS' />" class="imgLoadError" onload="Util.setImgWH(this, 100, 100)" title="<s:property value='#image.imgName'/>"/>
					</s:if>
					<s:elseif test="result.isSelect == true">
						<img id="img<s:property value="#st.index + 1"/>" src="<s:property value='#image.imgPath100' />" class="imgLoadError" onload="Util.setImgWH(this, 100, 100)" title="<s:property value='#image.imgName'/>"/>
					</s:elseif>
					<s:else>
						<img id="img<s:property value="#st.index + 1"/>" src="<s:property value='#image.imgPath100' />" class="imgLoadError" onload="Util.setImgWH(this, 100, 100)" onclick="detailShow(<s:property value='#st.index + 1'/>);" title="<s:property value='#image.imgName'/>"/>
					</s:else>
					</div>
				</div>
			</div>
			<span class="imgNameSpan<s:property value='#st.index + 1'/>" style="display:block; width:130px;word-wrap:break-word;word-break:break-all;" ondblclick="ListTable.edit(this,'/image/image_name_edit.htm','imgId=<s:property value='#image.imgId' />&imgName=');"><s:property value='#image.imgName'/></span>
		</div>
		<s:if test="(#st.index +1)%5==0"><div style="clear: both;width: 100%;height: 5px;line-height: normal;font-size: 0;overflow: hidden;"></div></s:if>
	</s:iterator>
<s:if test="imgType != 9">
	<%@ include file="/page/inc/pagination.jsp"%>
</s:if>