<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<link rel="stylesheet" href="/css/css.css" type="text/css"></link>
<script type="text/javascript">
	function chooseCategory(catId, catName){
		$("#catId").val(catId);
		$("#catName").val(catName);
		$("#productDialog").dialog('close');
		
		if (window.similar_return !== undefined) {
			window.similar_return(catName);
		}
		
		if(typeof loadTag == "function"){
			loadTag(catId);
		}
	}
</script>
<table>
	<tbody>
		<s:iterator value="result.catMap">
			<tr>
				<td>
					<a href="javascript:chooseCategory(<s:property value='key'/>,'<s:property value='value'/>')"><s:property value='value'/></a>
				</td>
			</tr>
		</s:iterator>
	</tbody>
</table>