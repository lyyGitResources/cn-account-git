<#macro page title>
<style type="text/css">
	.cn_hisupplierWrap {width:600px;margin: 0 auto;font-family: "宋体";font-size:12px;text-align: left;}
	.cn_hisupplierLogo {width: 100%;overflow: hidden;text-align: left;}
	.cn_hisupplierLogo table {width: 100%;}
	.cn_hisupplierLogo table td {padding-right: 5px;vertical-align: top;font-size: 12px;color: #343434;}
	.cn_hisupplierLogo table th {width: 70%;text-align: left;font-size: 18px;color: #999999;font-weight: bold;line-height: 23px;vertical-align: top;padding-top: 25px;}
	.cn_hisupplierMain {overflow: hidden;background: url(${accountBasePath}/img/mail/cn_hisupplier_01.jpg) repeat-y;font-size: 12px;line-height: 20px;padding: 88px 30px 40px 26px;color: #333333;}
	.information {overflow: hidden;margin-bottom: 10px;}
	.information .h2 {font-size: 14px;font-weight: bold;background: url(${accountBasePath}/img/mail/ico.gif) no-repeat 0px 4px;padding-left: 25px;border-bottom:1px solid #5897CD;padding-bottom:5px;}
	.information .more {float:right;margin-top:0px;*margin-top:-25px !important;*margin-top:-25px;font-weight:normal;font-size:12px;}
	.information hr {display:block;width:100%;overflow:hidden; height:0px;border:0px;border-bottom:1px solid #5897CD;}
	.information table {width: 95%;margin:0 auto;margin-top: 10px;}
	.information table th {font-weight: normal;color: #D3D3D3;}
	.information ul{ padding:0px;margin:0px;list-style-type:none;overflow:hidden;width:100%;clear:none;}
	.information li{ float:left; margin:5px 11px; width:77px; overflow:hidden;}
	.information li .img75x75{width: 75px;height: 75px;border: 1px solid #ccc;display: table-cell;vertical-align: middle;text-align: center;}
	.information li .img75X75 img{vertical-align: middle;max-height: 75px;max-width: 75px;margin-top: expression(( 75 - this . height)/ 2 );}
	.information li span{display:block;text-align:center;}
	.information li .address{font-size:11px;color: #B2B2B2;}
	.information li .text{ height:40px; overflow:hidden;}
	.information .line{ width:100%; overflow:hidden; margin:0px; height:1px;}
	.cn_hisupplierMain .h1 {font-size: 14px;font-weight: bold;}
	.cn_hisupplierMain .button {font-size: 12px;padding: 0px 8px 0px 8px; * padding: 2px 2px 0px 2px !important; * padding: 2px 2px 0px 2px;height: 23px;border: 1px solid #AFC6E7;background: url(${accountBasePath}/img/mail/button.gif) repeat-x;}
	.cn_hisupplierMain div {padding-left: 10px;padding-right: 10px;}
	.cn_hisupplierMain .black {border-top: 1px dotted #969696;color: #333;margin-top: 30px;padding-top: 20px;}
	.cn_hisupplierMain .gray {border-top: 1px dotted #969696;color: #656565;margin-top: 30px;padding-top: 20px;}
	.cn_hisupplierFooter {font-size: 12px;text-align: left;line-height: 30px;padding-left: 50px;height: 52px;background: url(${accountBasePath}/img/mail/cn_hisupplier_02.jpg) no-repeat;color: #FFFFFF;}
	.cn_hisupplierFooter a {color: #FFFFFF;text-decoration: underline;}
	.cn_hisupplierFooter a:visited,.cn_hisupplierFooter a:link {color: #FFFFFF;text-decoration: underline;}
</style>
<center>
	<div class="cn_hisupplierWrap">
		<div class="cn_hisupplierLogo">
			<table>
				<tr>
					<td rowspan="2">
						<a href="${b2bBasePath}" target="_blank"><img src="${b2bBasePath}/img/logo/logo.jpg" border="0"/></a>
					</td>
					<td rowspan="2">
						<img src="${accountBasePath}/img/mail/cn_hisupplier_03.jpg" border="0" style="margin-top:20px;" />
					</td>
					<th>${title}</th>
				</tr>
				<tr>
					<td align="right">${date}</td>
				</tr>
			</table>
		</div>
		<div class="cn_hisupplierMain">
			<#nested>
		</div>
		<div class="cn_hisupplierFooter">
			版权所有：<a href="${b2bBasePath}" target="_blank">中国海商网</a>
		</div>
	</div>
</center>
</#macro>