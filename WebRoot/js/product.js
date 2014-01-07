// 扩展验证方法
jQuery.extend(jQuery.validator.methods, {
		// 是否对应，包括“”，“0”两种情况
		correspond: function(value, element, param) {
			var paramValue = $(param).val();
			// 自定义属性无法正确匹配（param得到的是最后一个框中的值）
			// 解决方案：得到事件源的ID，然后得到对应的值，只对自定义属性有效
			// 原因猜想：跟param的值在验证条件生成时是动态的有关
			if(element && element.id && element.id.indexOf("customTag") != -1){
				var correspondId = element.id.replace("Name", "Value");
				paramValue = $("#" + correspondId).val();
			}

			if(($.trim(value) != "" && $.trim(value) != "0") && ($.trim(paramValue) == "" || $.trim(paramValue) == "0")){
				return false;
			}else{
				return true;
			}
		},
		notEqual: function(value, element, param) {
			var equal = true;
			value = $.trim(value.replace(/\s*/g,""));
			//value = value.replace(/\s*/g,"");
			$(param).each(function(){
				var otherValue = $.trim($(this).val().replace(/\s*/g,""));
				//otherValue = otherValue.replace(/\s*/g,"");
				if(otherValue != "" && value != "" && otherValue == value){
					equal = false;
					return;
				}
			});
			return equal;
		},
		// 不能含有逗号
		noComma: function(value, element, param) {
			return value.indexOf(",") == -1 && value.indexOf("，") == -1;
		},
		// 是否相同，param中包含自己
		notSame: function(value, element, param) {
			var num = 0;
			value = $.trim(value);
			$(param).each(function(){
				var otherValue = $.trim($(this).val());
				if(otherValue != "" && value != "" && otherValue == value){
					num++;
				}
			});
			if(num >= 2){
				return false;
			}else{
				return true;
			}
		},
		priceNumber: function(value, element) {
			return value == "" ||　/^\d{1,8}(\.\d{1,2})?$/i.test(value);
		}
});

// 选择分组窗口点击后执行
function selectGroup(groupId, groupName, groupPath){
	$("#groupId").val(groupId);
	$("#groupName").val(groupPath);
	$("#groupList").dialog('close');
}
function toggleTrade(){
	if($("#tradeImg").attr("src").indexOf("close") != -1){
		$("#tradeImg").attr("src", "/img/ico/switch_open.gif");
	}else{
		$("#tradeImg").attr("src", "/img/ico/switch_close.gif");
	}
	$("[name='tradeDiv']").each(function(){
		$(this).toggle();
	});
}
function toggleSuppliable(){
	if($("#suppliableImg").attr("src").indexOf("close") != -1){
		$("#suppliableImg").attr("src", "/img/ico/switch_open.gif");
	}else{
		$("#suppliableImg").attr("src", "/img/ico/switch_close.gif");
	}
	$("[name='suppliableDiv']").each(function(){
		$(this).toggle();
	});
}

/**
 * 初始化窗口
 */
function initDialog(){
 	$("#productDialog").dialog({
		bgiframe: true,
		autoOpen: false,
		height: 390,
		width: 460,
		modal: true,
		close: function(){
			$("#productDialog").empty();
		}
	});
	$("#specialGroupDialog").dialog({
		bgiframe: true,
		autoOpen: false,
		height: 390,
		width: 610,
		modal: true,
		close: function(){
			$("#specialGroupDialog").empty();
		}
	});
	$("#groupList").dialog({
		bgiframe: true,
		autoOpen: false,
		height: 500,
		width: 520,
		modal: true
	});
	$("#groupAddDialog").dialog({
		bgiframe: true,
		autoOpen: false,
		height: 150,
		width: 600,
		modal: true,
		close: function(){
			$("#groupAddDialog").empty();
		}
	});
	$("#selectImgDialog").dialog({
		bgiframe: true,
		autoOpen: false,
		height: 600,
		width: 780,
		modal: true,
		close: function(){
			$("#selectImgDialog").empty();
		}		
	});
}

/**
 * 绑定查询相关关键词对应事件
 */
function bindKeyword(){
	document.onclick = hideKeywordList;
	
	var oldQueryText = $.trim($("[name='proName']").val());
	$("#autoSearch,#keyword1,#keyword2,#keyword3").click(function(){
		var newQueryText = $.trim($("[name='proName']").val());
		var $this = $(this);
		if(newQueryText != "" && $("#autoSearch").attr("checked")){
			position = $this.attr("id");
		 	
    		if(position =="keyword1"){
    			$("#keywordList").removeClass("keyword2");
    			$("#keywordList").removeClass("keyword3");
    			$("#keywordList").addClass("keyword1");
    		}else if(position =="keyword2"){
    			$("#keywordList").removeClass("keyword1");
    			$("#keywordList").removeClass("keyword3");
    			$("#keywordList").addClass("keyword2");
    		}else if(position =="keyword3"){
    			$("#keywordList").removeClass("keyword1");
    			$("#keywordList").removeClass("keyword2");
    			$("#keywordList").addClass("keyword3");
    		}
		 	
			if(newQueryText != oldQueryText || !$("#keywordList").html()){ // 第一次进入页面或产品名称改变
				oldQueryText = newQueryText;
				$.ajax({
				   	type: "post",
				    url: "/user/relate_keyword_list.do",
				    cache: false, 
				    // 查询不存在于已有关键词的列表，用于修改
				    data: "queryText=" + newQueryText + "&keyword1=" + $("#keyword1").val() + "&keyword2=" + $("#keyword2").val() + "&keyword3=" + $("#keyword3").val(),
				    dataType: "json",
				    success:function(response){
				    	if(response != null && response.result != null && response.result.keywordList != null){
				    		var keywordList = response.result.keywordList;
				    		var buffer = "";
				    		for(var i =0; i < keywordList.length; i++){
				    			buffer += "<div style='cursor:pointer' onclick=\"selectKeyword('" + keywordList[i].keyName + "')\">";
				    			buffer += keywordList[i].keyName;
				    			buffer += "</div>";
				    		}
				    		$("#keywordList").html(buffer);
				    		$("#keywordList").addClass("keyword1");
							$("#keywordList").show();
							if($.browser.msie && $.browser.version == 6.0){
								$("#origin").hide();
							}
							
							// 绑定颜色改变
							$("#keywordList div").each(function(){
								$(this).hover(function(){
									$(this).css("background-color", "rgb(51, 102, 204)");
									$(this).css("color", "rgb(255, 255, 255)");
								},function(){
									$(this).css("background-color", "rgb(255, 255, 255)");
									$(this).css("color", "rgb(0, 0, 0)");
								});
							});
				    	}else{
				    		alert("未找到相匹配的关键词");
				    		$("#keywordList").html("");
				    		$("#keywordList").hide();
							if($.browser.msie && $.browser.version == 6.0){
								$("#origin").show();
							}
				    	}
				    }
				});
			}
			if($("#keywordList").html()){
				// 去除已选择过的关键词
				var obj = $("#keywordList div");
				var keywords = "," + $("#keyword1").val() + "," + $("#keyword2").val() + "," + $("#keyword3").val() + ",";
				$("#keywordList div").each(function(){
					$(this).show();
					var keyName = "," + $(this).html() + ",";
					if(keywords.indexOf(keyName) != -1){
						$(this).hide();
					}
				});
				$("#keywordList").show();
				if($.browser.msie && $.browser.version == 6.0){
					$("#origin").hide();
				}
			}
		}
	});
}

function selectKeyword(keyName){
	if(position != "autoSearch"){
		$("#" + position).val(keyName);
	}else{
		if($("#keyword1").val() == ""){
			$("#keyword1").val(keyName);
		}else if($("#keyword2").val() == ""){
			$("#keyword2").val(keyName);
		}else if($("#keyword3").val() == ""){
			$("#keyword3").val(keyName);
		}else{
			$("#keyword1").val(keyName);
		}
	}
	
	// 当关键词都填了，才隐藏关键词列表
	if($("#keyword1").val() != "" && $("#keyword2").val() != "" && $("#keyword3").val() != ""){
		$("#keywordList").hide();
		if($.browser.msie && $.browser.version == 6.0){
			$("#origin").show();
		}
	}
}
function hideKeywordList(){
	var e = arguments[0] || window.event;
	var src = e.srcElement || e.target;
	// 点击除了关键词输入框及选中自动查询复选框时，隐藏关键词列表
	if(src.id != "keywordList" && (src.id != "autoSearch" || !src.checked) && src.id != "keyword1" && src.id != "keyword2" && src.id != "keyword3"){
		$("#keywordList").hide();
		if($.browser.msie && $.browser.version == 6.0){
			$("#origin").show();
		}
	}
}