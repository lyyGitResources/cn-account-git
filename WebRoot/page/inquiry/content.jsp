<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.hisupplier.commons.util.*"%>
<%@ page import="com.hisupplier.commons.jdbc.*"%>
<body style="margin: 0; padding:0;">
<%
	String content = "";
	String inqId = request.getParameter("inqId");
	if(inqId != null){
		JdbcUtil jd = JdbcUtilFactory.getInstance().getJdbcUtil();
		try {
			jd.query("select content from InquiryExtra where inqId ="+ inqId);
			if (jd.resultNext()) {
				content = jd.getString("content");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtilFactory.getInstance().closeJdbcUtil(jd);
		}
	}
%>
<%= content %>
</body>
