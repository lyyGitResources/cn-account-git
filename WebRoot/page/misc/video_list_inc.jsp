<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$(function (){
		$("#playDialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 390,
			width: 460,
			modal: true,
			close: function(){
				$("#playDialog").empty();
			}
		});
		
		$("#adminLogDialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 200,
			width: 350,
			modal: true,
			close: function(){
				$("#adminLogDialog").empty();
			}
		});
	
		$("[name='adminLog']").each(function(){
			$(this).click(function(){
				$("#adminLogDialog").load("/basic/admin_log.do?tableName=Video&tableId=" + $(this).attr("proId"),{random: Math.random()});
				$("#adminLogDialog").dialog('option', 'title', '<s:text name="auditState.rejectRemark"/>');
				$("#adminLogDialog").dialog('open');
			});
		});
	});
	
	function playVideo(playPath) {
		window.open(playPath, "baidu", "height=400px,width=500px,top=300px,left=400px,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no, status=no");
	}
			
	function deleteVideo(videoId,pageNo){
		deleteConfirm("/video/video_delete.htm?videoId="+videoId+"&pageNo="+pageNo);
	}
</script>
<s:include value="/page/inc/image_error.jsp"/>
<s:if test="result.listResult.list.size > 0">
	<s:include value="/page/inc/pagination.jsp"/>
</s:if>
<table cellspacing="1" class="listTable">
	<tr>
		<th width="6%"><s:text name="serialNumber"/></th>
		<th width="8%"><s:text name="video.fileName"/></th>
		<th width="40%"><s:text name="video.title"/></th>
		<th width="13%"><s:text name="video.state"/></th>
		<s:if test='result.videoType == "all"' >
		<th width="15%">调用情况</th>
		</s:if>
		<th width="8%"><s:text name="video.viewCount"/></th>
		<th width="13%"><s:text name="operate"/></th>
	</tr>
	<s:iterator value="result.listResult.list" id="video" status="st">
	<tr>
		<td><s:property value="#st.count"/></td>
		<td class="img75">
			<s:if test="#video.state == 20">
				<a href="#position" onclick="javascript:playVideo('<s:property value="#video.playPath"/>');">
					<img src="<s:property value="#video.imgPath"/>" onload="Util.setImgWH(this, 75, 75)" onerror="$(this).attr('src', '/img/video_process.gif')"/>
				</a>
			</s:if>
			<s:else>
				<img src="<s:property value="#video.imgPath"/>" onload="Util.setImgWH(this, 75, 75)" onerror="$(this).attr('src', '/img/video_process.gif')"/>
			</s:else>
		</td>
		<td ><s:property value="#video.title"/></td>
		<td >
			<s:if test="state != 0">
				<s:if test="#video.state == 10">
					<a href="#position" proId="<s:property value='#video.videoId' />" name="adminLog">
						<s:property value="#video.stateName"/>
					</a>
				</s:if>
				<s:else>
					<s:property value="#video.stateName"/>
				</s:else>
			</s:if>
		</td>
		<s:if test='result.videoType == "all"' >
		<td>
			<s:if test="result.companyVideoId == #video.videoId">
				<a href="javaScript:tabMenu(3,{videoType:'Company'})">公司视频</a><br/>
			</s:if>
			<s:if test="result.productVideoIds.indexOf(#video.videoId) >=  0">
				<a href="/video/video_product_list.htm?videoId=${video.videoId }&videoType=Product">产品视频</a><br/>
			</s:if>
			<s:if test="result.menuVideoIds.indexOf(#video.videoId) >=  0">
				<a href="/video/video_menu_list.htm?videoId=${video.videoId }&videoType=Menu">菜单视频</a>
			</s:if>
		</td>
		</s:if>
		<td ><s:property value="#video.viewCount"/></td>
		<td >
			<s:if test="result.videoType == 'Company'">
				<a href="/member/company_edit.htm"><s:text name="button.modifyCompany"/></a>
			</s:if>
			<s:elseif test="result.videoType == 'Product'">
				<a href="/video/video_product_list.htm?videoId=<s:property value="#video.videoId"/>&videoType=Product"><s:text name="button.productList"/>[<s:property value="#video.videoUserCount"/>]</a>
			</s:elseif>
			<s:elseif test="result.videoType == 'Menu'">
				<a href="/video/video_menu_list.htm?videoId=<s:property value="#video.videoId"/>&videoType=Menu"><s:text name="button.menuList"/>[<s:property value="#video.videoUserCount"/>]</a>
			</s:elseif>
			<s:else>
				<a href="#position" onclick="javascript:showEditFormDialog('/video/video_edit.do?videoId=<s:property value="#video.videoId"/>')"><s:text name="button.edit"/></a><br />
				<a href="javascript:deleteVideo(<s:property value='#video.videoId'/>,<s:property value='result.listResult.page.pageNo'/>)"><s:text name="button.delete"/></a>
			</s:else>
		</td>
	</tr>
	</s:iterator>
</table>
<s:if test="result.listResult.list.size > 0">
	<s:include value="/page/inc/pagination.jsp"/>
</s:if>
<div id="adminLogDialog"></div>
