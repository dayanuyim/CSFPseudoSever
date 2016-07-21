<%@ page isErrorPage="true" import="java.io.*" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<script>
		function toErrReason(txt)
		{
			var sep = "Exception:";
			var i = txt.indexOf(sep);
			if(i != -1)
				txt = txt.substr(i + sep.length).trim();
			return txt
		}
		
		function init(){
			//cahnge title
			var msg = "${pageContext.exception.message}";
			document.title = "Oops! " + toErrReason(msg);
			
			//highlight stacktrace
			var st = document.getElementById("stack_trace");
			console.log(st.innerHTML);
			st.innerHTML = st.innerHTML
					.replace(/\(/g, "<em>(")
					.replace(/\)/g, ")</em>");
		}

		window.addEventListener('load', init)
	</script>

	<meta charset="utf-8">
	<title>Oops!</title>
	<link rel="stylesheet" type="text/css" href="/styles/main.css"/>
</head>

<body>
	<h1>Type</h1>
	<p>${pageContext.exception["class"]}</p>
	

	<h1>Message</h1>
	<c:if test="${pageContext.exception.message != null}">
		<p>The message:</p>
	</c:if>
	<p>${pageContext.exception.message}</p>

	
	<h1>StackTrace</h1>
	<p id="stack_trace">
	<%
		try(StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);)
		{
			exception.printStackTrace(printWriter);
			out.println(stringWriter);
		}
	%>
	</p>

</body>
</html>