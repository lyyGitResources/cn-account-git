<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<title>精准关键词生成工具</title>
<style type="text/css">
.keyword_box h3 { color: #F60; font-size: 14px; margin: 12px 24px 36px; }
.keyword_box { background-image: url("/img/businesscentre/keyword_box.png"); background-repeat: repeat-x; border: 1px solid #AECBE5; border-radius: 6px 6px 6px 6px; overflow: hidden; padding: 2%; width: 95%; }
.keyword_box .kwbtn,.keyword_box .kwbox,.keyword_box .kwbox2{ background-color: #ECF1F9; border: 1px solid #C9D8EB; display: inline; float: left; margin: 0 5px; width: 168px; }
.keyword_box label { background-color: #DFF1FB; border-bottom: 1px solid #B2CAEE; color: #325DA2; display: block; font-size: 14px; font-weight: 700; height: 28px; line-height: 28px; text-align: center; }
.keyword_box textarea { border: 1px solid #98BBF6; margin: 3px; }
.keyword_box .kwbtn { background-color: transparent; border-style: none; margin-top: 130px; width: auto; }
.keyword_box .kwbtn input, .keyword_box .kwbtn span { background-image: url("/img/businesscentre/kw_btn.png"); border: 1px solid #BED2F0; border-radius: 5px 5px 5px 5px; color: #1E70BD; font-weight: 700; height: 32px; }
.keyword_box .kwbox textarea { width: 160px; }
.keyword_box .kwbox2 { width: 234px; }
.keyword_box .kwbox2 textarea { width: 225px; }
.keyword_box .kwcopy { clear: both; padding-top: 12px; text-align: right; }
.keyword_box .kwcopy .btn_copy { background: url("/img/businesscentre/kw_copy_btn.png") no-repeat; border-style: none; color: #FFF;   }
.keyword_box .kwcopy input {height: 34px; width: 60px; font-weight: 700; }
.btn_clear {background-image: url("/img/businesscentre/kw_btn.png"); border: 1px solid #BED2F0;border-radius: 5px 5px 5px 5px; color: #1E70BD; }
.kwcopy { width: 680px; }
.kwcopy td { text-align: center;}
</style>
</head>
<body>
<div class="keyword_box">
	<h3>精准关键词生成工具</h3>
	<div class="kwbox">
		<label for="word1">属性词</label>
		<textarea id="word1" name="word1" class="textarea" cols="45" rows="16"></textarea>
	</div>
	<div class="kwbtn">
		<span>+</span>
	</div>
	<div class="kwbox">
		<label for="word2">中心词</label>
		<textarea id="word2" name="word2" class="textarea" cols="45" rows="16"></textarea>
	</div>
	<div class="kwbtn">
		<input id="btnG" type="button" value="生成" />
	</div>
	<div class="kwbox2">
		<label for="keyword">海商网精准关键词</label>
		<textarea id="keyword" name="keyword" class="textarea" cols="45" rows="16"></textarea>
	</div>
	<table class="kwcopy">
		<tr>
			<td width="168px"><input class="btn_clear" data-e="#word1" id="clear_word1" type="button" value="清空" /></td>
			<td width="45px"></td>
			<td width="166px"><input class="btn_clear" data-e="#word2" id="clear_word2" type="button" value="清空" /></td>
			<td width="55px"></td>
			<td width="231px">
				<div style="position: relative;">
				<input class="btn_clear" data-e="#keyword" id="clear_keyword" type="button" value="清空" />
				<input class="btn_copy" id="btnCopy" type="button" value="复制" />
				</div>
			</td>
		</tr>
	</table>
</div>
<script src="/js/zclip/jquery.zclip.min.js"></script>
<script>
var $word1 = $("#word1"),
	$word2 = $("#word2"),
	$keyword = $("#keyword"),
	$btnG = $("#btnG"),
	$btnCopy = $("#btnCopy");
$(function() {
	if (!$.browser.msie) {
		$btnCopy.zclip({
			path: "/js/zclip/ZeroClipboard.swf",
			beforeCopy: function() {
				var keyword = $.trim($keyword.val());
				if (keyword.length == 0) {
					alert("请先生成关键词")
					return false;
				}
			},
			copy: function() {
				return $keyword.val();
			},
			afterCopy: function() {
				alert("已拷贝到剪贴板");
			}
		});
	} else {
		$btnCopy.click(function() {
			var keyword = $.trim($keyword.val());
			if (keyword.length == 0) {
				alert("请先生成关键词")
				return false;
			}
			window.clipboardData.setData("Text", $keyword.val());
			alert("已拷贝到剪贴板");
		});
	}
});

$("#btnG").click(function() {
	if ($.trim($word1.val()).length == 0) {
		alert("属性词不能为空。");
		$word1.focus();
		return false;
	}
	if ($.trim($word2.val()).length == 0) {
		alert("中心词不能为空。");
		$word2.focus();
		return false;
	}
	var word1_array = $word1.val().split("\n"),
		word2_array = $word2.val().split("\n"),
		word1_length = word1_array.length,
		word2_length = word2_array.length,
		keyword = "";
	
	for (var i = 0; i < word1_length; i++) {
		var word1 = $.trim(word1_array[i]);
		if (word1.length == 0) continue;
		for (var k = 0; k < word2_length; k++) {
			var word2 = $.trim(word2_array[k]);
			if (word2.length == 0) continue;
			keyword += word1 + " " + word2 + "\n";
		}
	}
	$keyword.val(keyword);
});

$("#clear_word1, #clear_word2, #clear_keyword").click(function() {
	$($(this).attr("data-e")).focus().val("");
});
</script>
</body>
</html>