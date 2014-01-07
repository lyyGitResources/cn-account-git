<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/error/500.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.hisupplier.commons.*"%>
<%@ page import="com.hisupplier.commons.basket.*"%>
<%@ page import="com.hisupplier.commons.entity.Inquiry"%>
<%@ page import="com.hisupplier.commons.util.StringUtil"%>
<%@ page import="com.hisupplier.commons.util.WebUtil"%>
<%@ page import="com.hisupplier.commons.basket.BasketItem.Product"%>
<%@ page import="com.hisupplier.commons.basket.BasketItem.Trade"%>
<%@ page import="com.hisupplier.commons.util.cn.LocaleUtil"%>
<%@ page import="com.opensymphony.xwork2.util.*" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String sysBase = Config.getString("sys.base");
	if(StringUtil.equalsIgnoreCase("true",Config.getString("isBig5"))){
		sysBase = "http://big5."+Config.getString("sys.domain");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>购物询盘篮-海商网-全国领先的B2B电子商务交易平台</title>  
		<meta name="keywords" content="" />
		<meta name="description" content="询盘篮可盛放您在浏览过程中感兴趣的公司、产品和商情信息,并对这些信息进行邮件群发和比较。" />
		<script type="text/javascript">
			function switchOpen(number) {
				$("#switch_close_" + number).hide();
				$("#switch_open_" + number).show();
				$("#item_list_" + number).show();
				$("#itemDiv_"+number).addClass("inquiryListOpen");
			}
			function switchClose(number) {
				$("#switch_close_" + number).show();
				$("#switch_open_" + number).hide();
				$("#item_list_" + number).hide();
				$("#itemDiv_"+number).removeClass("inquiryListOpen");
			}
			
			function toggleCheckbox(p_box_name,t_box_name, bool) {
				var p_box = document.getElementsByName(p_box_name);
				var t_box = document.getElementsByName(t_box_name);
				for (var i = 0; p_box[i]; i++) {	
					p_box[i].checked = bool;						
				}
				for (var i = 0;t_box[i]; i++) {
					t_box[i].checked = bool;						
				}
				if(bool) {
					var c_box = document.getElementsByName('hs_basket_company');
					for(var i = 0; c_box[i]; i++) {
						if(!c_box[i].checked) {
							document.getElementById('ckAll').checked = false;
							return false;
						}
					}
					document.getElementById('ckAll').checked = true;
				}else{
					document.getElementById('ckAll').checked = false;
				}
			}
			
			function inquiryNow() {
				var c_checked = false;
				var c_box = document.getElementsByName("<%=Basket.COMPANY%>");
				for (var i = 0; i < c_box.length; i++) {	
					if ( c_box[i].checked) {
						c_checked = true;
						break;
					}						
				}
				if( !c_checked ){
					alert("请选择一个内容！");
					return;
				}
				document.basketForm.action="/user/inquiry.htm";
				document.basketForm.submit();
			}	
					
			function deleteItems(){
				if( window.confirm("您确定要删除?") ){
					var c_checked = false; 
					var c_box = document.getElementsByName("<%=Basket.COMPANY%>");
					for (var i = 0; i < c_box.length; i++) {	
						if ( c_box[i].checked) {
							c_checked = true;
							break;
						}						
					}
					if( !c_checked ){
						alert("请选择一个内容！");
						return;
					}
					document.basketForm.action="/user/inquiry_basket_remove.htm";
					document.basketForm.submit();
				}
			}
		</script>
	</head>
	<body>
<!--产品列表部分-->
<div class="area">
    <div class="mainNav"><span class="T">您正在浏览：</span><a href="<%=sysBase %>">首页</a> &gt; <span>询盘篮</span></div>
	<div class="inquiryNotice">
		<div class="writing1">您的询盘篮中有<strong>[<s:property value="basketItemList.size"/>]</strong>条公司信息。</div>
		<div class="writing2">
		<span>注意：</span><br />1、如果您关闭了中国海商网的所有页面，询盘篮中的信息将会被清空。<br />2、您下次登录中国海商网时，询盘篮中的信息为空，所以建议您完成本次的询盘操作。
		</div>
	</div>
	<div class="inquiryTab">
		<div class="box">
			<table cellspacing="0" cellpadding="0" border="0">
				<tbody><tr>
					<td width="93">选择</td>
					<td width="461">公司名称</td>
					<td width="129">产品数量</td>
					<td width="114">商情数量</td>
					<td width="171">地址</td>
				</tr>
			</tbody></table>
		</div>
	</div>
<s:if test="basketItemList.size > 0">
	<form action="" method="post" name="basketForm">
		<input type="hidden" name="get_item_method" value="get_selected_item"/>
		<input type="hidden" name="inquiryFrom" value="basket" />
		<input type="hidden" name="fromPage" size="120" value="<s:property value="fromPage"/>" />
		<s:iterator value="basketItemList" id="item" status="st">
			<div id="itemDiv_<s:property value="#item.comId"/>" class="inquiryList" onmouseover="this.style.backgroundColor='#F0F8FB'" onmouseout="this.style.backgroundColor='#ffffff'">
				<div class="box1">
					<input type="checkbox" name="hs_basket_company" value="<s:property value="#item.comId"/>" productid='hs_basket_product-${item.comId}' tradeid='hs_basket_trade-${item.comId}'  onclick="toggleCheckbox('hs_basket_product-<s:property value="#item.comId"/>','hs_basket_trade-<s:property value="#item.comId"/>',this.checked);" />
				</div>
				<div class="box2">
					<div id="switch_close_<s:property value="#item.comId"/>" style="text-align:left;">
						<div style="height:14px"
						<s:if test="!(#item.productList.size == 0 && #item.tradeList.size == 0)">
						 class="companyName" onclick="javascript:switchOpen(<s:property value="#item.comId"/>);"
							 </s:if>
							 <s:else>class="companyName Bg"</s:else>
						><a href="#position" ><s:property value="#item.comName"/></a></div>
					</div>
					<div id="switch_open_<s:property value="#item.comId"/>" style="text-align:left;display:none">
						<div style="height:14px" class="companyName Bg" onclick="javascript:switchClose(<s:property value="#item.comId"/>);"><a href="#position" ><s:property value="#item.comName"/></a></div>
					</div>
					
				</div>
				<div class="box3"><s:property value="#item.productList.size"/></div>
				<div class="box4"><s:property value="#item.tradeList.size"/></div>
				<div class="box5"><s:property value="#item.provinceName"/></div>
				<div class="box6" id="item_list_<s:property value="#item.comId"/>" style="display:none;">
					<s:iterator value="#item.productList" id="product" status="st">
						<div class="inquiryPro">
							<input type="checkbox" name="hs_basket_product-<s:property value="#item.comId"/>" value="<s:property value="#product.proId"/>" />
							<div class="proImg">
								<table border="0" cellspacing="0" cellpadding="0" class="photo100x100">
									<tr>
										<td align="center"><a href="<s:property value="#product.proUrl"/>" target="_blank"><img src="<s:property value="#product.imgPath"/>" onload="Util.setImgWH(this, 75, 75)" border="0"/></a></td>
									</tr>
								</table>
							</div>
							<div class="text"><a href="<s:property value="#product.proUrl"/>" target="_blank"><s:property value="#product.proName"/></a><br />
						        <span><s:property value="#product.model"/></span>
						    </div>
						</div>
					    <s:if test="(#st.index + 1)%6 == 0">
							<div class="blank21"></div>
						</s:if>
					</s:iterator>
					<s:iterator value="#item.tradeList" id="trade" status="st">
						<div class="inquiryPro">
							<input type="checkbox" name="hs_basket_trade-<s:property value="#item.comId"/>" value="<s:property value="#trade.tradeId"/>" checked />
							<div class="proImg">
								<table border="0" cellspacing="0" cellpadding="0" class="photo100x100">
									<tr>
										<td align="center"><a href="<s:property value="#trade.proUrl"/>" target="_blank"><img src="<s:property value="#trade.imgPath"/>"  onload="Util.setImgWH(this, 75, 75)" border="0"/></a></td>
									</tr>
								</table>
							</div>
							<div class="text"><a href="<s:property value="#trade.proUrl"/>" target="_blank"><s:property value="#trade.tradeName"/></a></div>
						</div>
					    <s:if test="(#st.index + 1)%6 == 0">
							<div class="blank21"></div>
						</s:if>
					</s:iterator>
				</div>
			</div>
		</s:iterator>
	</form>
</s:if>
	<div class="blank2"></div>
	<table border="0" cellspacing="0" cellpadding="0" class="inquiryBottom">
		<tr>
			<td align="right" width="55">全选&nbsp;</td>
			<td width="22"><input id="ckAll" type="checkbox" onclick="Util.checkAll(this, 'hs_basket_company');"/></td>
			<td width="80"><a onclick="inquiryNow()" href="#position"><img src="<%=Config.getString("sys.base") %>/img/inquiry2.gif" border="0" /></a></td>
			<td width="48"><a onclick="deleteItems()" href="#position"/><img src="<%=Config.getString("sys.base") %>/img/inquiry.gif" border="0" /></a></td>
			<td>&nbsp;</td>
		</tr>
	</table>
</div>
<div class="blank2"></div>
	<script text="text/javascript">
		$("#ckAll").click(function() {
			var checked = this.checked;
			$(":input[name='hs_basket_company']").each(function(){
				this.checked = checked;
				$(":input[name="+$(this).attr("productid")+"]").each(function(){this.checked = checked;});
				$(":input[name="+$(this).attr("tradeid")+"]").each(function(){this.checked = checked;});
			});
		});
	</script>
	</body>
</html>