<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    
    <title></title>
    <script type="text/javascript">
			function setType(typeId){
				ajaxLoad(typeId,-1);
			}
			function setPageNo(pageNo){
				if(typeof(pageNo)=='undefined')pageNo=1;
				ajaxLoad(<s:property value="result.type" />,pageNo);
			}
			function ajaxLoad(typeId,pageNo){
				$("#template").html("Loading...");
				$.ajax({
				  	url: "/message/ajaxTemplate.do?type="+typeId+"&pageNo="+pageNo,
				  	cache: false,
				  	success: function(html){
				    	$("#template").html(html);
				  	}
				}); 
				$("#template").dialog('option', 'title', "常用短语");
				$("#template").dialog('open');
			}
			function setMessageContent(id){
				var content = $("#template_"+id).attr("content");
				$("#content").val(content);
				countNum(60,$("#content"),$('#count'));
				$("#template").dialog("close");
			}
	</script>
	<style type="text/css">
			.b{ font-weight:bold;}
	</style>
  </head>
  
  <body>
     	<div class="buttonGroup">
  			<input type="button"  class="searchButton <s:if test="result.type==-1">b</s:if>" value="&nbsp;全部&nbsp;" onclick="setType(-1);"/>&nbsp;
  			<input type="button"  class="searchButton <s:if test="result.type==1">b</s:if>" value="&nbsp;春节祝福&nbsp;" onclick="setType(1);"/>&nbsp;
  			<input type="button"  class="searchButton <s:if test="result.type==2">b</s:if>" value="&nbsp;中秋祝福&nbsp;" onclick="setType(2);"/>&nbsp;
  			<input type="button"  class="searchButton <s:if test="result.type==3">b</s:if>" value="&nbsp;生日祝福&nbsp;" onclick="setType(3);"/>&nbsp;
  			<input type="button"  class="searchButton <s:if test="result.type==4">b</s:if>" value="&nbsp;健康问候&nbsp;" onclick="setType(4);"/>
  			<input type="button"  class="searchButton <s:if test="result.type==10">b</s:if>" value="&nbsp;我的短语&nbsp;" onclick="setType(10);"/>
			<table width="100%" border="0" cellpadding="2" cellspacing="1" class="tableRank" style="margin-top:10px;">
		        <tr>
		          <th width="20%">分类</th>
		          <th>内容</th>
		          <th width="20%">选项</th>
		        </tr>
		        <s:iterator value="result.listResult.list" id="template" status="st" >
		        	<tr>
			          <td <s:if test="#st.even">class="td1"</s:if><s:if test="#st.odd">class="td2 tdBg"</s:if>><s:property value="#template.fullType" /></td>
			          <td <s:if test="#st.even">class="tdText"</s:if><s:if test="#st.odd">class="tdBg tdText"</s:if>><s:property value="#template.content" /></td>
			          <td <s:if test="#st.odd">class="tdBg"</s:if>><a href="javascript:setMessageContent(<s:property value="#template.id" />);">使用</a></td>
			          <input type="hidden" id="template_<s:property value='#template.id' />" content="<s:property value='#template.content' />">
			        </tr>
		        </s:iterator>
		    </table>
  		</div>
		<%@ include file="/page/message/ajaxPagebar.jsp"%>
  </body>
</html>
