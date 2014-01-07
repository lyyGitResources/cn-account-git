<%!
/*This page contains functions which would be used by other pages
*/
/**
  * escapeXML function helps you escape special characters in XML<br>
  * @param strItem - string to be escaped
  * @param forDataURL - boolean whether to escape single quotes or not
  * @return String in which special characters have been escaped
  */
public String escapeXML(String strItem, boolean forDataURL){
	//Convert ' to &apos; if dataURL
	if(forDataURL==true)
		strItem = strItem.replaceAll("'","&apos;");	
	else {
		//Else for dataXML 		
		//Convert % to %25
		strItem = strItem.replaceAll("%","%25");	
		//Convert ' to %26apos;
		strItem = strItem.replaceAll("'","%26apos;");
		//Convert & to %26
		strItem = strItem.replaceAll("&","%26");
	}
	//Common replacements
	strItem = strItem.replaceAll("<","&lt;");
	strItem = strItem.replaceAll(">","&gt;");
	//We've not considered any special characters here. 
	//You can add them as per your language and requirements.
	//Return
	return strItem;
	}


/**
 * Returns a value between 1-5 depending on which<br>
 * paletter the user wants to plot the chart with.<br> 
 * Here, we just read from session variable and show it<br>
 * In your application, you could read this configuration from your <br>
 * User Configuration Manager, database, or global application settings<br>
 * @param session - HttpSession
 * @return String representing the palette.Value is taken from the session.
 */
public String getPalette(HttpSession session){
	String palette=null;
	if(null!=session.getAttribute("palette")){
		palette=(String)session.getAttribute("palette");
	}
 	if(null==palette){
		palette = "2"; // default value
	}
	return palette;
}

/**
  * returns 0 or 1, depending on whether we've to <br>
  * animate chart. Here, we just read from session variable and show it<br>
  * In your application, you could read this configuration from your <br>
  * User Configuration Manager, database, or global application settings<br>
  * @param session - HttpSession
  * @return String representing the palette.Value is taken from the session.
  */
public String getAnimationState(HttpSession session){
	String animation=null;
	if(null!=session.getAttribute("animation")) {
		animation=(String)session.getAttribute("animation");
	}
	if(null==animation) {
		animation = "0";
	}
	else if(!animation.equals("0")){
	    animation = "1";
	}
		  
	return animation;
}

/**
  * returns a color code for caption. Basic<br>
  * idea to use this is to demonstrate how to centralize your cosmetic <br>
  * attributes for the chart<br>
  * @return String representing the palette.Value is taken from the session.
  */
public String getCaptionFontColor(){
	//Return a hex color code without #
	return "666666";
	//FFC30C - Yellow Color
}
%>
