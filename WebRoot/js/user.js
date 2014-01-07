
$(function (){
	$.fn.validateForm = function (options) {
		var settings = {
			//debug: true,
			//errorElement: "em",
			//errorContainer: $("#warning, #summary"),
			errorPlacement: function(error, element) {
				fieldName = element.attr("name");
				// 最后一位为数字，并且前面字符一样的，提示使用同一个位置
				if(fieldName.length > 2){
					var num = fieldName.substring(fieldName.length - 1, fieldName.length);
					if(Util.isInt(num)){
						fieldName = fieldName.substring(0, fieldName.length - 1);
					}
				}
				
				// 城市省份需要判断province,city，提示使用同一个位置。原因对于存在disabled属性的input不进行验证
				if(fieldName.toLowerCase().indexOf("province") != -1 || fieldName.toLowerCase().indexOf("city") != -1){
					fieldName = "city";
				}

				if (fieldName != "sex"){
					if(document.getElementById(fieldName+"Tip")){
						$("#"+fieldName+"Tip").html(error);
					}else{
						element.after(error);
					}
				}
			},
			success: function(label) {
				label.text("OK").addClass("success");
			}
		};
		$.extend(settings, options);
		return this.validate(settings);
	};
});
	