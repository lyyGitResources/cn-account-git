<div class="pagination" >
   	<div style="float:left;">
	   	<s:text name="pagination.info">
	   		<s:param><%=p.getResultTotal() %></s:param>
	   		<s:param><%=p.getStartRow()+1 %></s:param>
	   		<s:param><%=p.getEndIndex() %></s:param>
	   		<s:param><%=p.getPageNo() %></s:param>
	   		<s:param><%=p.getPageTotal() %></s:param>
	   	</s:text>
	</div>
	<%if(p.getPageTotal()>1){ %>
	<div style="float:left;margin-left:10px;">&laquo;	
		<%if(p.isPreviousPage()){ %>
			&nbsp;<a href="<%=p.getPagUrl(p.getPageNo()-1,true) %>"><s:text name="pagination.pre" /></a>	
			&nbsp;<a href="<%=p.getPagUrl(1,true) %>" >1</a>
		<%} %>
		<%if(p.getIndexArray()[0] >2){ %>...<%} %>
		<%
		for(int tmp : p.getIndexArray()){
			if(tmp == p.getPageNo()){
		%>
			<strong><%=tmp %></strong>
		<%
			}
			if(tmp != 0 && tmp < p.getPageTotal() && tmp > 1 && tmp != p.getPageNo()){
		%>
			<a href="<%=p.getPagUrl(tmp,true) %>"><%=tmp %></a>
		<%
			}
		}
		if(p.getIndexArray()[p.getIndexArray().length-1]<p.getPageTotal()-1){%>...<%} 
		if(p.isNextPage()){
		%>
			<a href="<%=p.getPagUrl(p.getPageTotal(),true) %>"><%=p.getPageTotal() %></a>
			&nbsp;<a href="<%=p.getPagUrl(p.getPageNo()+1,true) %>"><s:text name="pagination.next" /></a>
		<%
		}
		%>
		 &raquo;	
	</div>	
	<div style="float:right;">								
		<s:text name="pagination.goto" /> 
		<select onchange="javascript:document.location.href=this.value;" class="goToPage">
			<%
			for(int page_i = 1; page_i<=p.getPageTotal(); page_i++){
			%>
				<option value="<%=p.getPagUrl(page_i,true) %>" <%if(page_i == p.getPageNo()){%> selected <%} %>><%=page_i %></option>
			<%
			} 
			%>
		</select>
	</div>
	<%} %>
</div>