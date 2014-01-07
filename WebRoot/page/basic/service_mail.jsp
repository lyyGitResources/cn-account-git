<%@ page language="java" contentType="text/html; charset=UTF-8"%>	
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="serviceMail.title" /></title>
		<script type="text/javascript">
			$(document).ready(function (){	
				showContactDate();
				
				$("#serviceForm").validateForm({
					rules: {
						subject: {required:true},
						content: {required:true}
					},
					messages: {
						subject: {required:"<s:text name='serviceMail.subject.required'/>"},
						content: "<s:text name='serviceMail.content.required'/>"
					}
				});
				
				showOtherSubject();
			});
			function setService(obj){ 
				values = obj.value.split("-");
				$("#toName").val(values[0]);
			   	$("#toEmail").val(values[1]);
		    }
		    
			function showContactDate(){
				if( $("#contactMode").val() == '<s:text name="serviceMail.tel" />' ){
					$("#contact_time").css({display:""});
				}else{
					$("#contact_time").css({display:"none"});
				}
			}

			function selectSubject(){
				$("#subject").val("");
				$("[name='otherSubject']").val("");
				$("#subject").show();
				$("#otherSubject").hide();
				$("[name='otherSubject']").rules("remove");
				$("#subject").rules("add", {required: true,messages: {required:'<s:text name="serviceMail.subject.required"/>'}});
			}
			
			function showOtherSubject(){
				if( $("#subject").val() == '<s:text name="serviceMail.subject7" />' || $("[name='otherSubject']").val() == '关于商务中心的功能推荐'){
					$("#subject").rules("remove");
					$("#subject").val('<s:text name="serviceMail.subject7" />').hide();
					$("#otherSubject").show();
					$("[name='otherSubject']").rules("add", {required: true,messages: {required:'<s:text name="serviceMail.otherSubject.required" />'}});
				}
			}

		</script>
	</head>
	<body>
		<div class="serviceTips">
			<span><s:text name="serviceMail.tip1" /></span>
			<br /><br />
			<s:text name="serviceMail.tip2" />
		</div>
		<div style=" float: left;">
			<s:iterator value="result.serviceList" id="service" status="st">
				<input type="radio" onclick="setService(this);" name="service" value="<s:property value="#service.contact"/>-<s:property value="#service.email"/>" <s:if test="#st.index == 0">checked</s:if>/> 
				<s:if test="#service.sex == 2">
					<img src="/img/ico/service05.gif" />
				</s:if>
				<s:else>
					<img src="/img/ico/service04.gif" />
				</s:else>
				<span class="span"><s:property value="#service.contact"/></span>			
			</s:iterator>
		</div>
		<span class="sapn1">&quot;<font class="red">*</font>&quot; <s:text name="serviceMail.tip3" /></span>
		
		<form id="serviceForm" name="serviceForm" method="post" action="/basic/service_mail_send.htm" enctype="multipart/form-data">	
			<s:token />
			<input type="hidden" name="comName" value="<s:property value="result.serviceMail.comName"/>"/>
			<input type="hidden" name="contact" value="<s:property value="result.serviceMail.contact"/>"/>
			<input type="hidden" name="email" value="<s:property value="result.serviceMail.email"/>"/>
			<input type="hidden" name="tel" value="<s:property value="result.serviceMail.tel"/>"/>
			<input type="hidden" name="fax" value="<s:property value="result.serviceMail.fax"/>"/>
			<input type="hidden" name="memberId" value="<s:property value="result.serviceMail.memberId"/>"/>
			<input type="hidden" name="mobile" value="<s:property value="result.serviceMail.mobile"/>"/>
			<input type="hidden" name="toName" id="toName" value="<s:property value="result.serviceMail.toName"/>">
			<input type="hidden" name="toEmail" id="toEmail" value="<s:property value="result.serviceMail.toEmail"/>">
			<table class="formTable">
				<tr>
					<th>
						<span class="red">*</span>&nbsp;<s:text name="serviceMail.subject" />:
					</th>
					<td>
						<select name="subject" id="subject" style="width:200px;" onchange="javascript:showOtherSubject();">
							<option value="">-<s:text name="choose" />-</option>
							<option value='<s:text name="serviceMail.subject1"/>'><s:text name="serviceMail.subject1"/></option>
							<option value='<s:text name="serviceMail.subject2"/>'><s:text name="serviceMail.subject2"/></option>
							<option value='<s:text name="serviceMail.subject3"/>'><s:text name="serviceMail.subject3"/></option>
							<option value='<s:text name="serviceMail.subject4"/>'><s:text name="serviceMail.subject4"/></option>
							<option value='<s:text name="serviceMail.subject5"/>'><s:text name="serviceMail.subject5"/></option>
							<option value='<s:text name="serviceMail.subject6"/>'><s:text name="serviceMail.subject6"/></option>
							<option value='<s:text name="serviceMail.subject7"/>'><s:text name="serviceMail.subject7"/></option>
						</select>
						<div id="otherSubject" style="display:none;">
							<a href="javascript:selectSubject();" style="float:left;"><s:text name="button.select" /></a>
							<input type="text" name="otherSubject" style="float:left;width:250px;" value="<s:property value="queryBy"/>" >
							<div id="otherSubjectTip" style="float:left;"></div>
						</div>
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="serviceMail.content" />：</th>
					<td>
						<textarea name="content" style="width:350px;height:100px;"><s:property value="result.serviceMail.content" /></textarea>
						<div id="contentTip"></div>
					</td>
				</tr>
				<tr>
					<th><s:text name="comName" />：</th>
					<td><s:property value="result.serviceMail.comName" /></td>
				</tr>
				<tr>
					<th><s:text name="serviceMail.contact" />：</th>
					<td><s:property value="result.serviceMail.contact" /></td>
				</tr>
				<tr>
					<th><s:text name="email" />：</th>
					<td>
						<s:property value="result.serviceMail.email" />
					</td>
				</tr>
				<tr>
					<th><s:text name="serviceMail.tel" />：</th>
					<td><s:property value="result.serviceMail.tel" /></td>
				</tr>
				<tr>
					<th><s:text name="serviceMail.fax" />：</th>
					<td><s:property value="result.serviceMail.fax" /></td>
				</tr>
				<tr>
					<th><s:text name="serviceMail.mobile" />：</th>
					<td><s:property value="result.serviceMail.mobile" /></td>
				</tr>
				<tr>
					<th><s:text name="serviceMail.contactMode" />：</th>
					<td>
						<select name="contactMode" style="width:100px;" id="contactMode" onchange="showContactDate();">
							<option value='<s:text name="email" />'><s:text name="email" /></option>
							<option value='<s:text name="serviceMail.tel" />'><s:text name="serviceMail.tel" /></option>
							<option value='<s:text name="serviceMail.fax" />'><s:text name="serviceMail.fax" /></option>
							<option value='<s:text name="serviceMail.mobile" />'><s:text name="serviceMail.mobile" /></option>
						</select>
					</td>
				</tr>
				<tr id="contact_time">
					<th ><s:text name="serviceMail.contactDate" />:</th>
					<td >
						<select id="contactDate" name="contactDate" style="float:left;">
							<option value='<s:text name="serviceMail.contactTime.monday"/>'><s:text name="serviceMail.contactTime.monday"/></option>
							<option value='<s:text name="serviceMail.contactTime.tuesday"/>'><s:text name="serviceMail.contactTime.tuesday"/></option>
							<option value='<s:text name="serviceMail.contactTime.wednesday"/>'><s:text name="serviceMail.contactTime.wednesday"/></option>
							<option value='<s:text name="serviceMail.contactTime.thursday"/>'><s:text name="serviceMail.contactTime.thursday"/></option>
							<option value='<s:text name="serviceMail.contactTime.friday"/>'><s:text name="serviceMail.contactTime.friday"/></option>
						</select>
						<select id="contactTime" name="contactTime" style="float:left; margin-left:5px;">
							<option value="08:30 - 10:30">08:30 - 10:30</option>									
							<option value="10:30 - 12:00">10:30 - 12:00</option>
							<option value="13:00 - 15:00">13:00 - 15:00</option>
							<option value="15:00 - 17:00">15:00 - 17:00</option>
						</select>
						<div style="clear:both;"></div>
						<div class="gray" style="float:left; width:550px; margin-top:5px;"><s:text name="serviceMail.contactTime.notice"/></div>		
					</td>
				</tr>
				<tr>
					<th><s:text name="attachment" />：</th>
					<td>
						<input type="file" name="upload" value=""/>
						<div class="fieldTips">
							<s:text name="attachment.format" >
								<s:param>jpg、gif、txt、doc、pdf、xls</s:param>
								<s:param>500</s:param>
							</s:text>
						</div>
					</td>
				</tr>
			</table>
			<div class="buttonCenter">
				<input type="submit" value="<s:text name='button.submit' />"/> 
				<input type="reset" value="<s:text name='button.reset' />"/> 
			</div>
		</form>
		
 	</body>
</html>