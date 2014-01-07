(function($){
	$.fn.checkform=function(options){
		options = $.extend({}, $.fn.checkform.defaults, options);

		//  记录按钮是否点击
		if(options.changeClick != ""){
			$(options.changeClick).each(function(){
				var $this = $(this);
				$(this).one("click", function(){
					$this.data("click", true);
				});
			});
			$("body").data("clickSize", $(options.changeClick).size());
		}
		
		// 增加忽略标签
		if(options.ignoreTag != ""){
			options.ignoreTag = ":not(" + options.ignoreTag + ")";
		}
		
		// 多选框
		$(this).find("input[type='checkbox']" + options.ignoreTag).each(function(){
			$(this).data("value", $(this).attr("checked"));
		});
		
		// 单选框
		$(this).find("input[type='radio']" + options.ignoreTag).each(function(){
			$(this).data("value", $(this).attr("checked"));
		});
		
		// 文件框
		$(this).find("input[type='file']" + options.ignoreTag).each(function(){
			$(this).data("value", $(this).val());
		});
		
		// 输入框
		$(this).find("input[type='text']" + options.ignoreTag).each(function(){
			$(this).data("value", $(this).val());
		});
		
		// 文本框
		$(this).find("textarea" + options.ignoreTag).each(function(){
			if($(this).attr("id") == options.fckArea){
				$("body").data("value", $(this).val().replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&amp;/g, "&"));
			}else{
				$(this).data("value", $(this).val());
			}
		});

		// 下拉框
		$(this).find("select" + options.ignoreTag).each(function(){
			if($(this).attr("multiple")){
				var values = new Array();
				$(this).find("option").each(function(){
					values.push($(this).val());
				});
				
				// 升序排列
				values.sort(function(a,b){  
					return a - b;  
				});
				
				$(this).data("value", values.join(","));
			} else {
				$(this).data("value", $(this).val());
			}
		});
		
		// 图片
		$(this).find("img" + options.ignoreTag).each(function(){
			$(this).data("value", $(this).attr("src"));
		});
		
		$(this).submit(function(){
			var ischange=false;
			
			if(options.changeClick != ""){
				// 按钮删除了自己
				if($("body").data("clickSize") != $(options.changeClick).size()){
					return true;
				}
				$(options.changeClick).each(function(i){
					if($(this).data("click")){
						ischange=true;
						return false;
					}
				});
			}
			
			if(ischange){
				return true;
			}
			
			$(this).find("input[type='checkbox']" + options.ignoreTag).each(function(){
				if($(this).data("value") != $(this).attr("checked")){
					ischange=true;
					return false;
				}
			});
			if(ischange){
				return true;
			}
			
			$(this).find("input[type='radio']" + options.ignoreTag).each(function(){
				if($(this).data("value") != $(this).attr("checked")){
					ischange=true;
					return false;
				}
			});
			if(ischange){
				return true;
			}
			
			$(this).find("input[type='file']" + options.ignoreTag).each(function(){
				if($(this).data("value") != $(this).val()){
					ischange=true;
					return false;
				}
			});
			if(ischange){
				return true;
			}
			
			$(this).find("input[type='text']" + options.ignoreTag).each(function(){
				if($(this).data("value") != $(this).val()){
					ischange=true;
					return false;
				}
			});
			if(ischange){
				return true;
			}
			$(this).find("textarea" + options.ignoreTag).each(function(){
				if($(this).attr("id") != options.fckArea && $(this).data("value") != $(this).val()){
					ischange=true;
					return false;
				}
			});
			if(ischange){
				return true;
			}
			$(this).find("select" + options.ignoreTag).each(function(){
				if($(this).attr("multiple")){
					var values = new Array();
					$(this).find("option").each(function(){
						values.push($(this).val());
					});
					
					// 升序排列
					values.sort(function(a,b){  
						return a - b;  
					});

					if($(this).data("value") != values.join(",")){
						ischange=true;
						return false;
					}
				} else {
					if($(this).data("value") != $(this).val()){
						ischange=true;
						return false;
					}
				}
			});
			if(ischange){
				return true;
			}
			
			$(this).find("img" + options.ignoreTag).each(function(){
				if($(this).data("value") != $(this).attr("src")){
					ischange=true;
					return false;
				}
			});
			
			if(ischange){
				return true;
			}
			
			if($("body").data("value") && typeof FCKeditorAPI != "undefined"){
				// alert(FCKeditorAPI.GetInstance(options.fckArea).GetXHTML() + " vs " + $("body").data("value"))
				if(FCKeditorAPI.GetInstance(options.fckArea).GetXHTML() != $("body").data("value")){
					return true;
				}
			}
			
			document.location.href=options.url;
			return false;
		});
	}
	$.fn.checkform.defaults={
		changeClick: "",   // "#id1,#id2", 点击一下就为表单改变的按钮，例如：增加表单标签，删除表单标签操作
		ignoreTag: "",		// "#id1,#id2" 忽略的标签
		fckArea: "description",	// fck域的名称，不需要'#'
		url:"/product/list.do?random=1000"
	}
})(jQuery)