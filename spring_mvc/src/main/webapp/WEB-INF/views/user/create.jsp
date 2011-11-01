<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page session="false" %>
<html>
	<head>
		<title>Create User</title>
		<link rel="stylesheet" href="/spring_mvc/css/main.css"/>
		<link rel="shortcut icon" href="/spring_mvc/images/favicon.ico" type="image/x-icon"/>

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="layout" content="main"/>


		<script type="text/javascript" src="/spring_mvc/js/application.js"></script>

	</head>

	<body>
		<div id="spinner" class="spinner" style="display:none;">
			<img src="/spring_mvc/images/spinner.gif"
				  alt="Loading..."/>
		</div>

		<div id="grailsLogo">
			<a href="http://grails.org">
				<img src="/spring_mvc/images/grails_logo.png"
					  alt="Grails" border="0"/>
			</a>
		</div>


		<div class="body">
			<h1>Create User</h1>

			<form action="/spring_mvc/user/save" method="post">
				<div class="dialog">
					<table>
						<tbody>
						<tr class="prop">
							<td valign="top" class="name">
								<label for="firstName">First Name</label>
							</td>
							<td valign="top" class="value ">
								<input type="text" name="firstName" value="" id="firstName"/>
							</td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name">
								<label for="lastName">Last Name</label>
							</td>
							<td valign="top" class="value ">
								<input type="text" name="lastName" value="" id="lastName"/>
							</td>
						</tr>
						</tbody>
					</table>
				</div>

				<div class="buttons">
					<span class="button">
						<input type="submit" name="create" class="save" value="Create" id="create"/>
					</span>
				</div>
			</form>
		</div>

	</body>
</html>