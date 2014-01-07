/**
 * 内容过滤：火狐下过滤符合条件的多余代码
 */
function FCKeditor_filterIF(html) {
	var fristIndex = html.indexOf("<!--[if");
	var lastIndex = html.indexOf("endif]-->");
	if (fristIndex != -1 && lastIndex != -1) {
		var tmp1 = html.substring(0, fristIndex);
		var tmp2 = html.substring(lastIndex + 9, html.length);
		return FCKeditor_filterIF(tmp1 + tmp2);
	} else {
		return html;
	}
}
/**
 * 内容过滤：火狐下过滤符合条件的多余代码
 */
function FCKeditor_filterStyle(html) {
	var fristIndex = html.indexOf("<style");
	var lastIndex = html.indexOf("</style>");
	if (fristIndex != -1 && lastIndex != -1) {
		var tmp1 = html.substring(0, fristIndex);
		var tmp2 = html.substring(lastIndex + 8, html.length);
		return FCKeditor_filterStyle(tmp1 + tmp2);
	} else {
		return html;
	}
}
/**
 * 内容过滤：火狐下过滤符合条件的多余代码
 */
function FCKeditor_filterScript(html) {
	var fristIndex = html.indexOf("<script");
	var lastIndex = html.indexOf("</script>");
	if (fristIndex != -1 && lastIndex != -1) {
		var tmp1 = html.substring(0, fristIndex);
		var tmp2 = html.substring(lastIndex + 9, html.length);
		return FCKeditor_filterScript(tmp1 + tmp2);
	} else {
		return html;
	}
}
/**
 * 内容过滤
 */
function FCKeditor_CleanWord(html) {
	// Remove href
	html = html.replace(/<a(.*?)href="(.*?)"(.*?)>(.*?)<\/a>/gi, "$4");
	// add table border attr
	// html = html.replace(/<table/gi,"<table border=1");
	// Remove image
	//html = html.replace(/<img \b((?!images\.hisupplier)\w)+\b[^>]+>/gi,"");
	html = html.replace(/<img\s*((\w*)="[^<>]*"\s*)*src="http:\/\/(?!images\.hisupplier\.com)(.*?)"(.*?)(\/)?>/gi,"");
	html = html.replace(/\s*bgcolor="[^"]*"/gi, "");
	
    // Remove <!--[if ... endif]-->
	if ($.browser.mozilla) {
		html = FCKeditor_filterIF(html);
	} 
	
	// Remove <link /> <script />
	html = html.replace(/<\s*meta.*\/\s*>/gi, "");
	html = html.replace(/<\s*link.*\/\s*>/gi, "");
	html = html.replace(/<\s*script.*\/\s*>/gi, "");
	html = html.replace(/<o:p>\s*<\/o:p>/g, "");
	html = html.replace(/<o:p>.*?<\/o:p>/g, "&nbsp;");
	
	// Remove mso-xxx styles.
	html = html.replace(/\s*mso-[^:]+:[^;"]+;?/gi, "");

	// Remove margin styles.
	html = html.replace(/\s*MARGIN: 0cm 0cm 0pt\s*;/gi, "");
	html = html.replace(/\s*MARGIN: 0cm 0cm 0pt\s*"/gi, "\"");
	html = html.replace(/\s*TEXT-INDENT: 0cm\s*;/gi, "");
	html = html.replace(/\s*TEXT-INDENT: 0cm\s*"/gi, "\"");
	html = html.replace(/\s*TEXT-ALIGN: [^\s;]+;?"/gi, "\"");
	html = html.replace(/\s*PAGE-BREAK-BEFORE: [^\s;]+;?"/gi, "\"");
	html = html.replace(/\s*FONT-VARIANT: [^\s;]+;?"/gi, "\"");
	html = html.replace(/\s*tab-stops:[^;"]*;?/gi, "");
	html = html.replace(/\s*tab-stops:[^"]*/gi, "");

	// Remove FONT face attributes.
	html = html.replace(/\s*face="[^"]*"/gi, "");
	html = html.replace(/\s*face=[^ >]*/gi, "");
	html = html.replace(/\s*FONT-FAMILY:[^;"]*;?/gi, "");
	// 用户后台不能修改文字大小
	html = html.replace(/\s*size="[^"]*"/gi, "");
	html = html.replace(/\s*size='[^']*'/gi, "");
	
	// Remove Class attributes
	html = html.replace(/<(\w[^>]*) class=([^ |>]*)([^>]*)/gi, "<$1$3");			
	// Remove styles.
	html = html.replace(/<(\w[^>]*) style="([^\"]*)"([^>]*)/gi, "<$1$3");	
			
	// Remove empty styles.
	html = html.replace(/\s*style="\s*"/gi, "");
	html = html.replace(/<SPAN\s*[^>]*>\s*&nbsp;\s*<\/SPAN>/gi, "&nbsp;");
	html = html.replace(/<SPAN\s*[^>]*><\/SPAN>/gi, "");
	
	// Remove Lang attributes
	html = html.replace(/<(\w[^>]*) lang=([^ |>]*)([^>]*)/gi, "<$1$3");
	html = html.replace(/<SPAN\s*>(.*?)<\/SPAN>/gi, "$1");
	html = html.replace(/<span\s*>(.*?)<\/span>/gi, "$1");
	html = html.replace(/<FONT\s*>(.*?)<\/FONT>/gi, "$1");
	html = html.replace(/<font\s*>(.*?)<\/font>/gi, "$1");
	// Remove XML elements and declarations
	html = html.replace(/<\\?\?xml[^>]*>/gi, "");
	
	// Remove Tags with XML namespace declarations: <o:p></o:p>
	html = html.replace(/<\/?\w+:[^>]*>/gi, "");
	
	// Remove comments [SF BUG-1481861].
	html = html.replace(/<\!--.*-->/gi, "");
	html = html.replace(/<H\d>\s*<\/H\d>/gi, "");
	html = html.replace(/<H1([^>]*)>/gi, "<div$1><b><font size=\"6\">");
	html = html.replace(/<H2([^>]*)>/gi, "<div$1><b><font size=\"5\">");
	html = html.replace(/<H3([^>]*)>/gi, "<div$1><b><font size=\"4\">");
	html = html.replace(/<H4([^>]*)>/gi, "<div$1><b><font size=\"3\">");
	html = html.replace(/<H5([^>]*)>/gi, "<div$1><b><font size=\"2\">");
	html = html.replace(/<H6([^>]*)>/gi, "<div$1><b><font size=\"1\">");
	html = html.replace(/<\/H\d>/gi, "</font></b></div>");
	html = html.replace(/<(U|I|STRIKE)>&nbsp;<\/\1>/g, "&nbsp;");

	// Remove empty tags (three times, just to be sure).
	html = html.replace(/<([^\s>]+)(\s[^>]*)?>\s*<\/\1>/g, "");
	html = html.replace(/<([^\s>]+)(\s[^>]*)?>\s*<\/\1>/g, "");
	html = html.replace(/<([^\s>]+)(\s[^>]*)?>\s*<\/\1>/g, "");

	// Transform <P> to <DIV>
	var re = new RegExp("(<P)([^>]*>.*?)(</P>)", "gi");	// Different because of a IE 5.0 error
	html = html.replace(re, "<div$2</div>");
	
	// Fix relative anchor URLs (IE automatically adds the current page URL).
	re = new RegExp(window.location + "#", "g");
	html = html.replace(re, "#");
	html = html.replace(/<span>(&nbsp;|\s)*<\/span>/gi, "");
	html = html.replace(/<strong>(&nbsp;|\s)*<\/strong>/gi, "");
	html = html.replace(/<div>(&nbsp;|\s)*<\/div>/gi, "");
	html = html.replace(/<p>(&nbsp;|\s)*<\/p>/gi, "");
	html = html.replace(/\s{2,}/gi, "");
	
	//  Remove <style ... </style> <script ... </script>
	if ($.browser.mozilla) {
		html = FCKeditor_filterStyle(html);
		html = FCKeditor_filterScript(html);
	}
	
	return html;
}

function FCKeditor_OnComplete(editorInstance) {
	editorInstance.Events.AttachEvent("OnPaste", FCKeditor_OnPaste);
}
function FCKeditor_OnPaste(editorInstance) {
	setTimeout(function () {
		var tmp = FCKeditor_CleanWord(editorInstance.GetXHTML());
		editorInstance.SetHTML(tmp);
		editorInstance.ResetIsDirty();
	}, 500);
	editorInstance.Focus();
	return true;
}