<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/sitemesh-decorator" prefix="decorator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Sortable</title>
		<link type="text/css" rel="stylesheet" href="<%=Config.getString("img.base")%>/js/lib/ui/themes/base/ui.all.css" />
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/ui/ui.core.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/ui/ui.sortable.js"></script>

		<style type="text/css">
			#sortable { list-style-type: none; margin: 0; padding: 0; width: 60%; }
			#sortable li { margin: 0 3px 3px 3px; padding: 0.4em; padding-left: 1.5em; font-size: 1.4em; height: 18px; }
			#sortable li span { position: absolute; margin-left: -1.3em; }
		</style>
		<script type="text/javascript">
			$(function() {
				$("#sortable").sortable();
				$("#sortable").disableSelection();
			});
		</script>
	</head>
	<body>
		<ul id="sortable">
	<li class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>Item 1</li>
	<li class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>Item 2</li>
	<li class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>Item 3</li>
	<li class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>Item 4</li>
	<li class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>Item 5</li>
	<li class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>Item 6</li>
	<li class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>Item 7</li>

		</ul>

	</body>
</html>
