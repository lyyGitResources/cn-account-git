<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<link href="/css/commons.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/dtree/dtree.js"></script>
<script type="text/javascript">
	function selectChildNode(){
		alert('<s:text name="group.selectChidlGroup"/>');
	}
</script>
<div class="dTreeNode">
	<script type="text/javascript">
		var groupIDArray = new Array();
		groupTree = new dTree('groupTree','/js/dtree/');	
		group_id = 0;
		group_pid = -1;
		group_name = "<a href=\"javascript:groupTree.openAll();\"><s:text name='button.openAll'/></a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href=\"javascript:groupTree.closeAll();\"><s:text name='button.closeAll'/></a>";		
		groupTree.add(0,-1,group_name);
		<s:iterator value="result.groupList" id="group" status="st">
    		groupIDArray.push(<s:property value="#group.groupId"/>);
    		group_depth = <s:property value="#group.depth"/>;
			group_id = <s:property value="#group.groupId"/>;
			group_pid = <s:property value="#group.parentId"/>;
			group_name = '<s:property value="#group.groupName"/>&nbsp;(<s:property value="#group.productCount"/>&nbsp;/&nbsp;<s:property value="#group.tradeCount"/>)<span id="groupListNewIco<s:property value="#group.groupId"/>"><s:if test="#group.showNewIco"><img src="<%=Config.getString("img.base") %>/img/ico/group_new.gif"/></s:if></span>';
			group_url = "<s:property value='#group.operate'/>";
			group_title = '<s:property value="#group.groupName"/>';
			group_target = "";
			if(group_depth == 3){
				group_icon = "/js/dtree/img/page.gif";
				group_iconOpen = "/js/dtree/img/page.gif";
			}else{
				group_icon = "/js/dtree/img/folder.gif";
				group_iconOpen = "/js/dtree/img/folderopen.gif";
			}
			groupTree.add(group_id,group_pid,group_name,group_url,group_title,group_target,group_icon,group_iconOpen);
   		</s:iterator>	
   		document.write(groupTree);
	</script>
</div>