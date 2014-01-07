
var AJAX_LOADING_CODE = "<img src='/img/loading1.gif' /><br /><br />Loading...";
var ajaxSubmitOptions = { 
    beforeSubmit: ajaxSubmitBefore,
    success: ajaxSubmitResponse,
    type: 'post',
    dataType: 'json'
};

function ajaxSubmitBefore(formData, jqForm, options) {
	$("#dialog").dialog( "option", "buttons", { "确定": function() { $(this).dialog("close"); } } );
	$('#dialog').dialog('open');
	$("#dialogLoading").show();
	return true;
}

function ajaxSubmitResponse(response, status) {
	$("#dialogLoading").hide(); 
	$("#dialogMessage").show().html(response.msg);
}

/**
 * 标签页切换
 * @param index
 * @param params
 */
function tabMenu(index, params){
	$("#imgType").val(params.imgType);
	$(".tabMenu li").each(function(){
		$(this).removeClass("current");
	});
	$(".tabMenu li:nth-child("+index+")").eq(0).addClass("current");
	ListTable.reload(params); 
}

function commentTabMenu(index, params){
	$(".commentTabMenu li").each(function(){
		$(this).removeClass("current");
	});
	$(".commentTabMenu li:nth-child("+index+")").eq(0).addClass("current");
	ListTable.reload(params);
}

function countNum(num, obj, showObj) {
	var reg = /[^ -~]/g,
		spare = num,
		val = obj.val(),
		match;
	var length = val.replace(/\n/g, "aa").length;
	spare = spare - length;
	if(spare >= 0){
		showObj.html(spare);
	}else{
		val = substr(val, num, showObj);
		obj.html(val);
		obj.val(val);
	}
}

function substr(val, max, showObj) {
	var count = 0;
	for (var s in val) {
		if (/\n/g.test(val[s])) count += 2;
		else count++;
		if (count + 1 >= max) {
			showObj.html((c = s % 2) > 0 ? c - 1 : c);
			return val.substring(0, parseInt(s) + 2);
		}
	}
}

$(function (){
	$.ajaxSetup({
		cache: false
	});
	
	$.fn.validateForm = function (options) {
		var settings = {
			//debug: true,
			//errorElement: "em",
			//errorContainer: $("#warning, #summary"),
			errorPlacement: function(error, element) {
				fieldName = element.attr("name");

				if(document.getElementById(fieldName+"Tip")){
					$("#"+fieldName+"Tip").html(error);
/*					$("#"+fieldName+"Tip").css({
						"position":"absolute",
						"top":"290px",
						"left":"870px",
						"marginTop":"200px"
					});*/
				}else{
					element.after(error);
				}
			},
			success: function(label) {
				label.text("OK").addClass("success");
			}
		};
		$.extend(settings, options);
		return this.validate(settings);
	};
	
	// 搜索文字框自动显示
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

	$("form").submit(function(){
		if($("#queryText").val() == queryText){
			$("#queryText").val("");
		}
	});
		
	//初始化操作对话框
	$("#dialog").dialog({
		bgiframe: true,
		autoOpen: false,
		modal: true,
		close: function(){
			//$("#dialogLoading").hide();
			$("#dialogMessage").hide().empty();
		}
	});
	
	//加载未读询盘数量，产品、商情和询盘的回收站数量
	$.ajax({
		type: "get", 
		url: "/basic/inquiry_unread_count.do", 
		cache: false, 
		data: null, 
		dataType:"json", 
		success: function (response) {
			if(response.inquiryUnreadCount > 0){
				$("#newInquiry").html("<img src='/img/new_cn.gif' />");
			}
			
			$("#inquiryDelCount").html(response.inquiryDelCount);
			$("#productDelCount").html(response.productDelCount);
			$("#tradeDelCount").html(response.tradeDelCount);
		}
	});
	
	//加载左侧客服列表
	$("#serviceList").load("/basic/service_list.do");
});

/**
 * 显示灰色JS遮罩层
 * @param ct 要显示的内容Id
 */
function showBg(ct) {
	var bg = $("<div id='fullbg'></div>");
	bg.css({
		"background-color": "Gray",
		display: "none",
		"z-index": "3",
		position: "absolute",
		left: "0px",
		top: "0px",
		filter: "Alpha(Opacity = 30)",
		"-moz-opacity": "0.4",
		opacity: "0.4"
	});
	bg.appendTo($("body"));
	var bH = $("body").height();
	var bW = $("body").width() + 16;
	var objWH = getObjWh(ct);
	$("#fullbg").css({
				width : bW,
				height : bH,
				display : "block"
			});
	var tbT = objWH.split("|")[0] + "px";
	var tbL = objWH.split("|")[1] + "px";
	$("#" + ct).css({
				top : tbT,
				left : tbL,
				display : "block"
			});
	$(window).scroll(function() {
				resetBg()
			});
	$(window).resize(function() {
				resetBg()
			});
}
function getObjWh(obj) {
	var st = document.documentElement.scrollTop;// 滚动条距顶部的距离
	var sl = document.documentElement.scrollLeft;// 滚动条距左边的距离
	var ch = document.documentElement.clientHeight;// 屏幕的高度
	var cw = document.documentElement.clientWidth;// 屏幕的宽度
	var objH = $("#" + obj).height();// 浮动对象的高度
	var objW = $("#" + obj).width();// 浮动对象的宽度
	var objT = Number(st) + (Number(ch) - Number(objH)) / 2;
	var objL = Number(sl) + (Number(cw) - Number(objW)) / 2;
	return objT + "|" + objL;
}
function resetBg() {
	var fullbg = $("#fullbg").css("display");
	if (fullbg == "block") {
		var bH2 = $("body").height();
		var bW2 = $("body").width() + 16;
		$("#fullbg").css({
					width : bW2,
					height : bH2
				});
		var objV = getObjWh("dialog");
		var tbT = objV.split("|")[0] + "px";
		var tbL = objV.split("|")[1] + "px";
		$("#dialog").css({
					top : tbT,
					left : tbL
				});
	}
}

// 关闭灰色JS遮罩层和操作窗口
function closeBg() {
	$("#fullbg").css("display", "none");
}