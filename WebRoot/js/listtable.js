
/**
 * 提供列表翻页、动态组合参数搜索、创建编辑区等功能
 * @author linliuwei
 */
var ListTable = new Object();
ListTable.url = _initUrl();
ListTable.pageTotal = 1;
ListTable.resultTotal = 0;
ListTable.pageNoId = "listPageNo";
ListTable.pageSizeId = "listPageSize";
ListTable.pageSizeCookieKey = "hs_account_pageSize";
ListTable.debug = false; //调式选项
ListTable.params = _initParams(); //初始化参数

/**
 * 在列表中创建一个可编辑区
 * @param obj	表单元素
 * @param url   提交地址
 * @param params  数据，格式key=value用&符号分隔
 * 实例：ListTable.edit(this,'/demo/edit.htm','comId=114&memberId=');
 */
ListTable.edit = function (obj, url, params) {
	var tagName = obj.firstChild.tagName;
	if (tagName && tagName.toLowerCase() == "input") {
		return;
	}
	var nodeName = obj.nodeName;

	obj = $(obj);
	org = $.trim(obj.html().replace(/\n/g, "")); //保存原始的内容
	
	//创建一个输入框
	text = $("<input/>");
	text.val(org);
	if (nodeName.toLowerCase() == "span") {
		text.css("width", (obj.innerWidth() + 4) + "px");
		text.css("height", obj.innerHeight() + "px");
	}else{
		text.css("width", (obj.innerWidth() - 8) + "px");
		text.css("height", (obj.innerHeight() - 10) + "px");
	}
 	
	//清空原先的内容，将输入框加入到对象中
	obj.empty();
	obj.append(text);
	text.focus();
	text.bind("keypress", function (event) {
		if (event.keyCode == 13) {
			text.blur();
			return false;
		}
		if (event.keyCode == 27) {
			obj.html(org);
		}
	});
	text.bind("blur", function (event) {
		value = $.trim(text.val());
		if (value == org) {
			obj.html(org);
			return;
		}
		if (value.length > 0) {
			$.ajax({
				type:"post", 
				url:url,
				data:params + value, 
				dataType:"json", 
				success:function (response) {
					if(response.tip == "editSuccess"){
						alert(response.msg);
						obj.html(value);
						var clazz=obj.attr("class");
						if(clazz != null && clazz != '' && clazz != undefined){
							$("."+clazz).html(value);//同时更改其它地方同样的信息
						}
					}else{
						alert(response.msg);
						obj.html(org);
					}
				}
			});
		} else {
			obj.html(org);
		}
	});
};

/**
 * 重新载入列表
 * @param options 参数
 * @param extend  是否追加原先记录的参数，默认为false
 * 实例：
 * 1) ListTable.reload({param:value})  //在当前参数列表上追加参数
 * 2) ListTable.reload({remove:param}) //在当前参数列表上删除参数
 * 3) ListTable.reload({empty:true})   //清空当前参数列表，只保留{ajax: true}
 * 4) ListTable.reload({msg:'提示信息'}) 
 */
ListTable.reload = function (options, extend) {
	if(extend == null){
		extend = false;
	}
	//追加/删除/清空参数
	if (typeof options == "object") {
		if (options.empty) {
			ListTable.params = {ajax:true};
		} else if (options.remove && ListTable.params[options.remove]) {
			ListTable.params[options.remove] = "";
			ListTable.params["pageNo"] = 1;
		} else if (extend){
			ListTable.params = $.extend(ListTable.params, options, {pageNo:1, msg:"",ajax:true});
		} else{
			ListTable.params = $.extend(options, {pageNo:1, ajax:true});		
		}
	}
 
	// 使用ajax方式加载列表
	if (ListTable.params.ajax) {
		if(options && options.msg && options.msg != ""){
			$("#dialogMessage").show().html(options.msg);
		}else{
			$("#dialogMessage").hide()
		}
		//$('#dialog').dialog('open');
		var list = $("#listTable");
		ListTable.params["random"] = Math.random();
		$.get(
			ListTable.url, 
			ListTable.params, 
			function (response) {
				list.html(response);
				// 追加升序/降序图标
				if (ListTable.params["sortBy"] && ListTable.params["sortBy"] != "") {
					var order = ListTable.params["sortOrder"] == "desc" ? "desc" : "asc";
					$("#sort_" + ListTable.params["sortBy"]).append("<img src='/img/sort_" + order.toLowerCase() + ".gif' />");
				}
				//$('#dialog').dialog('close');
				ListTable._debug();
			}
		);
	} else {
		// 普通加载方式
		var queryString = "";
		var index = 1;
		$.each(ListTable.params, function (name, value) {
			queryString += (index == 1 ? "?" : "&") + name + "=" + value;
			index++;
		});
		location.href = ListTable.url + queryString;
	}
};
/**
 * 切换排序
 * @param sortBy 排序字段
 */
ListTable.sort = function (sortBy) {
	ListTable.params["sortBy"] = sortBy;
	ListTable.params["sortOrder"] = ListTable.params["sortOrder"] == "asc" ? "desc" : "asc";
	ListTable.reload();
};
 
/**
 * 列表翻页
 * @param pageNo
 */
ListTable.gotoPage = function (pageNo) {
	if (pageNo == null) {
		ListTable.params["pageNo"] = 1;
	} else {
		ListTable.params["pageNo"] = pageNo;
	}
	if (parseInt(ListTable.params["pageNo"],10) > parseInt(ListTable.pageTotal,10)) {
		ListTable.params["pageNo"] = 1;
	}
	var pageSize = document.getElementById(ListTable.pageSizeId);
	if (pageSize) {
		pageSize = Util.isInt(pageSize.value) ? pageSize.value : 20;
		ListTable.params["pageSize"] = pageSize;
		// 保存7天
		Util.setCookie(ListTable.pageSizeCookieKey, pageSize, 60 * 24 * 7);
	}
	ListTable.reload();
};
ListTable.changePageNo = function (event) {
	if (event.keyCode == 13) {
		var pageNo = document.getElementById(ListTable.pageNoId);
		if (pageNo) {
			ListTable.gotoPage(pageNo.value);
		}
	}
};
ListTable.changePageSize = function (event) {
	if (event.keyCode == 13) {
		ListTable.gotoPage();
	}
};
ListTable._debug = function () {
	if (!ListTable.debug) {
		return;
	}
	var div = document.getElementById("listTableDebug");
	if (!div) {
		div = $("<div id='listTableDebug'/>");
	} else {
		div = $(div);
	}
	div.css("float","left");
	div.css("margin","5px");
	div.css("width","300px");
	div.css("padding","5px");
	div.css("background","#f2f2f");
	div.css("border","1px solid #8db5dc");
	div.empty();
	div.append("<strong>debug params:</strong> <br>");
	$.each(ListTable.params, function (i, n) {
		div.append(i + " = " + n + "<br>");
	});
	$("#listTable").before(div);
};

function _initUrl(){
	var url = location.href;
	url = url.lastIndexOf("?") == -1 ? url.substring((url.lastIndexOf("/")) + 1) : url.substring((url.lastIndexOf("/")) + 1, url.lastIndexOf("?"));
	if(url.lastIndexOf(".htm") != -1){
		url = url.substring(0, url.lastIndexOf("."));
		url += ".do";
	}
	//alert(url);
	return url;
}
function _initParams() {
	var startIndex = location.href.lastIndexOf("?");
	var endIndex = location.href.length;
	// 默认打开ajax选项
	var params = {ajax:true};
	if (startIndex != -1) {
		var tmps = location.href.substring(startIndex + 1, endIndex).split("&");
		$.each(tmps, function (i, n) {
			v = n.split("=");
			params[v[0]] = v[1];
		});
		if (!params.pageNo) {
			params.pageNo = 1;
		}
		if (!params.pageSize) {
			var ps = Util.getCookie(ListTable.pageSizeCookieKey);
			params.pageSize = ps == "" ? 15 : ps;
		}
	}
	return params;
}
 
$(function(){
	//在搜索框为空时清空参数
	if (document.getElementById("queryText")) {
		$("#queryText").bind("blur", function () {
			if ($.trim($("#queryText").val()) == "") {
				ListTable.params["queryText"] = "";
			}
		});
	}
	$("#listTable tr").live("mouseover", function(){
		if($(this).attr("name") && $(this).attr("name").indexOf("userSuggest") != -1){
			
		}else{
			 $(this).find("td").css("backgroundColor","#FFFAE3"); 
		}
	});
	
	$("#listTable tr").live("mouseout", function(){
		// 菜单固定项的底色不是白色
		if($(this).attr("name") && $(this).attr("name").indexOf("menuTrtrue") != -1){
			$(this).find("td").css("backgroundColor","#DDEFFF");
		}else{
			$(this).find("td").css("backgroundColor","#FFFFFF");
		}
	});
});