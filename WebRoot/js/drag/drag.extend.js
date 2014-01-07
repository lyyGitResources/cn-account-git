var dragsort;
var junkdrawer;
ToolMan._extend = {
	isChange: function(){
		var order = junkdrawer.serializeList(document.getElementById(param.listId)).split('|');
		for(var i=0; i < order.length-1; i++){
			if(order[i] * 1 > order[i + 1] * 1){
				return true;
			}
		}
		return false;
	},
	showButton: function(){
		if(ToolMan._extend.isChange()){
			$(":submit[name='" + param.buttonName + "']").each(function(){
				$(this).removeAttr("disabled");
			});
		}else{
			$(":submit[name='" + param.buttonName + "']").each(function(){
				$(this).attr("disabled", "disabled");
			});
		}
	},
	restoreOrder: function(beginValue){
		var items = document.getElementById(param.listId).getElementsByTagName("li");
		$(param.numberName).each(function(i){
			$(this).text((param.isList ? "" : "序号：") + (beginValue+ i + 1));
		});

		// 兼容IE8，原因IE8无法触发OK按钮事件
		if($.browser.msie && $.browser.version == 8.0){
			if(typeof arguments[1] == "object"){
				var src = arguments[1].srcElement || arguments[1].target;
				if(src.type == "button"){
					ToolMan._extend.setSortValue($('#_newOrder' + src.parentNode.parentNode.id).val());
				}
			}
		} 
		ToolMan._extend.showButton();
		ToolMan._extend.hideNewOrderDiv();
	},
	setSortValue: function(newOrder){
		if($("#_oldOrder").val() == newOrder){
			return;
		}
		if(isNaN(newOrder * 1) || newOrder.indexOf(".") != -1 || newOrder * 1 <= 0 || newOrder * 1 > param.total){
			alert("请输入一个正确的排序值！");
			return;
		}
		var pageNo = param.pageNo <= 0 ? 1 : param.pageNo;
		var pageSize = param.pageSize;
		var minNo = 1;
		// 取得对象在页面中的排序位置
		var oldOrder = $("#_oldOrder").val() - (pageNo - 1) * pageSize;
		
		if(param.url != null && param.url != ""){
			minNo = (pageNo - 1) * pageSize + 1;
			var maxNo = pageNo * pageSize;
			// 超出页面范围
			if(newOrder < minNo || newOrder > maxNo){
				var proId = 0;
				$("[name='proId']").each(function(i){
					if(i == (oldOrder - 1)){
						proId = $(this).val();
						return;
					}
				});
				if(proId > 0){
					document.location.href = param.url + "proId="+ proId + "&listOrder=" + newOrder + "&pageSize=" + pageSize;
				}
			}
		}
		newOrder -= (pageNo - 1) * pageSize;

		var helpers = ToolMan.helpers();
		var coordinates = ToolMan.coordinates();
		var moveTo = null;
		
		var items = document.getElementById(param.listId).getElementsByTagName("li");
		var item = items[oldOrder - 1]; // 原位置上的元素
		var newItem = items[newOrder - 1]; // 新的位置上的元素
		var offset = coordinates.topLeftOffset(newItem); // 计算新位置元素的坐标
		
		// 向前移动
		var previous = helpers.previousItem(item, item.nodeName);
		while (previous != null) {
			var bottomRight = coordinates.bottomRightOffset(previous);
			if (offset.y < bottomRight.y && offset.x < bottomRight.x) {
				moveTo = previous;
			}
			previous = helpers.previousItem(previous, item.nodeName);
		}
		if (moveTo != null) {
			helpers.moveBefore(item, moveTo);
			ToolMan._extend.restoreOrder(--minNo);
			return;
		}
		
		// 向后移动
		var next = helpers.nextItem(item, item.nodeName);
		while (next != null) {
			var topLeft = coordinates.topLeftOffset(next);
			if (topLeft.y <= offset.y && topLeft.x <= offset.x) {
				moveTo = next;
			}
			next = helpers.nextItem(next, item.nodeName);
		}
		if (moveTo != null) {
			helpers.moveBefore(item, helpers.nextItem(moveTo, item.nodeName));
			ToolMan._extend.restoreOrder(--minNo);
			return;
		}
	},
	leavePage: function(){
		var e = arguments[0] || window.event;
		var src = e.srcElement || e.target; // src就是事件的触发源
		var isLeave = false;

		// 不是输入序号的连接或不是完成排序的按钮，判断为离开了页面
		if((src.tagName =="A"|| src.type == "button") && param.buttonName != src.name){
			isLeave = true;
		}

		if (isLeave) {
			if (ToolMan._extend.isChange()) {
				if (confirm("您尚未保存排序结果，现在就保存吗？")) {
					$(param.form).submit();
				}
			}
		}
	},
	// 隐藏所有
	hideNewOrderDiv: function(){
		$("[name='_newOrderDiv']").each(function(){
			$(this).hide();
		});
	}
} 
$(function(){
	dragsort = ToolMan.dragsort();
	junkdrawer = ToolMan.junkdrawer();

	dragsort.makeListSortable(document.getElementById(param.listId));

 	ToolMan._extend.restoreOrder(param.pageNo < 1 ? 0 : ((param.pageNo - 1)* param.pageSize));
 	document.onclick = ToolMan._extend.leavePage;

	$("#" + param.listId + " li").each(function(){
		var $this = $(this).find("[name='serialDiv']");
		
		$(this).hover(
			function(){
				$(this).css("background-color", "#FFFAE3");
				
				ToolMan._extend.hideNewOrderDiv();
				var order = $.trim($("#_number"+ $this.attr("id")).text());
				if(!param.isList){
					order = order.substring(3);
				}
			
				$("#_newOrder" + $this.attr("id")).val(order);
				$("#_oldOrder").val(order);
				$("#_newOrderDiv" + $this.attr("id")).show();
				$("#_newOrder" + $this.attr("id")).select();
				$("#_number"+ $this.attr("id")).text("");
				
				if($.browser.msie && $.browser.version == 6.0){
					$this.css({height:"40px","padding-top":"0px"});
				}
			},
			function(){
				var background = param.background;
				if($(this).attr("name") && $(this).attr("name").indexOf("true") != -1){
					 background = "#DDEFFF";
				}
				$(this).css("background-color", background);
				
				ToolMan._extend.restoreOrder(param.pageNo < 1 ? 0 : ((param.pageNo - 1)* param.pageSize));
				if($.browser.msie && $.browser.version == 6.0){
					$this.css("height","40px").css("padding-top","0");
				}
			}
		);
	});
 });