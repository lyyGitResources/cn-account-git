<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page pageEncoding="UTF-8" %> 
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    
    <title></title>
	<script type="text/javascript">
			$(document).ready(function(){
				var history = Util.getCookie("hs_contact_selected");
				if(history!=""){
					ids = history.split(",");
					for (i = 0; i < ids.length; i++) {
						$("#"+ids[i]).attr("checked",true);
					}
				}
				$("#phoneBook_").treeview({persist:'cookie'});
			});
			function selectAll(phoneBook){
				var flag=true;
				if(!phoneBook.checked){
					flag=false;
				}
				$("#phoneBook_ li").find("input[type='checkbox']").each(function(i) {
					this.checked = flag;
	   			});
			}
			function selectGroup(group){
				var flag = true;
				if(!group.checked){
					flag = false;
				}
				$(group).siblings("ul").find("li>span input").each(function(i){
					this.checked = flag;
				});
			}
			function getContactInfo(){
				var contactInfo = new Array();
				var history = new Array();
				$("input[type='checkbox']:checked").each(function(i) {
					history.push(this.id);
	   			});
	   			$("input[name='contactInfo']:checked").each(function(i) {
					contactInfo.push(this.value);
	   			});
	   			$("#phoneStr").val(contactInfo.join(","));
	   			Util.setCookie("hs_contact_selected", history.join(","), false, "/");
	   			$("#phoneBook").dialog('close');
			}
		</script>
  </head>
  
  <body>
   <div>
		<s:if test="result.groupList != null && result.groupList.size > 0">
		<ul id="phoneBook_" class="filetree">
			<li>
				<span class="folder" style="float:left;">电话薄（<s:property value="result.contactnum" />）</span><input id="g_-1" type="checkbox" onclick="selectAll(this);"/>
				<ul>
					<s:iterator value="result.groupList" id="group">
					<li>
						<span class="folder" style="float:left;"><s:property value="#group.groupName"/>（<s:if test="#group.contactList!=null"><s:property value="#group.contactList.size"/></s:if><s:if test="#group.contactList==null">0</s:if>）</span><input id="g_<s:property value='#group.groupId' />" type="checkbox" onclick="selectGroup(this);"/>
						<s:if test="#group.contactList!=null">
						<ul>
							<s:iterator value="#group.contactList" id="contact" >
								<li><span class="file"><s:property value="#contact.contactName" />(<s:property value="#contact.mobile" />)<input type="checkbox" id="c_<s:property value="#contact.id" />" name="contactInfo" value="(<s:property value='#contact.contactName' />)<s:property value='#contact.mobile' />" /></span></li>
							</s:iterator>
						</ul>
						</s:if>
					</li>
					</s:iterator>
				</ul>
			</li>
		</ul>
		<input type="button" value="确定" onclick="getContactInfo();"/>
		</s:if>
		<s:if test="result.groupList == null || result.groupList.size = 0">
			<div>无内容</div>
		</s:if>
	</div>
  </body>
</html>
