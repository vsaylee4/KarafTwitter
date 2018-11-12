<!-- Author : Anvitha Jain -->
<!-- Author : Saylee Vyawahare -->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>

<html>
<head>
<title>Falcon Team Twitter Utility</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<style>
body {
	margin: 0px 0px 0px 1px;
}
h1 {
	font-weight: bold;
	color: #E2E2E2;
	text-align:center;
}
div {
	background-color: #E2E2E2;
}
hr {
	border: none;
	height: 1px;
	color: #4c4c4c;
	background-color: #E2E2E2;
}
</style>
</head>
<body background=https://uplogos.com.br/wp-content/uploads/2018/05/twitter1.jpg>
	<h1>Falcon Team Twitter Utility</h1>
	<br>
	<form action="runApi" method="POST">
		<input type="text" name="searchStr" value=""> <select
			name="twitteroptions">
			<option value="Home Timeline">Home Timeline</option>			
			<option value="Query String">Query String</option>			
			<option value="Post Tweet">Post Tweet</option>
			<option value="Trends Available">Trends Available</option>
			<option value="Follow User">Follow User</option>			
			<option value="Language Support">Language Support</option>
			<option value="Trends Closest">Trends Closest</option>
			<option value="Followers List">Followers List</option>
			<option value="Unfollow User">Unfollow User</option>
		</select> <input type="submit" value="Submit" />
	</form>
	<br>
	<div class="panel panel-default">
		<%
			try {
				if (session.getAttribute("option") != null) {
		%>
		<div class="panel-heading"><%=session.getAttribute("option")%></div>
		<%
			}

				if (session.getAttribute("twitterResponse") != null) {
					List<String> responses = (List<String>) session.getAttribute("twitterResponse");

					for (String resp : responses) {
		%>
		<div class="panel-body"><%=resp%></div>
		<hr>
		<%
			}
				}
			} catch (Exception e) {
			}
		%>
	</div>
</body>
</html>

