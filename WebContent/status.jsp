<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Web Status</title>

<style>
td:first-of-type{
	font-weight: bold;
}
</style>

</head>
<body>
	<table>
		<tr><th>Name</th><th>Value</th><th>MaxAge</th></tr>
		<c:forEach var="c" items="${cookie}">
			<tr>
				<td>${c.value.name}</td>
				<td>${c.value.value}</td>
				<td>${c.value.maxAge}</td>
			</tr>
		</c:forEach>
	</table>
	
	<p>session id: ${cookie.JSESSIONID.value}</p>

</body>
</html>