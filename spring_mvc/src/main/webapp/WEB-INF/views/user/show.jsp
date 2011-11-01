<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Show User</title>
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
			<h1>Show User</h1>

			<div class="dialog">
				<table>
					<tbody>

					<tr class="prop">
						<td valign="top" class="name">Id</td>

						<td valign="top" class="value">1</td>

					</tr>

					<tr class="prop">
						<td valign="top" class="name">First Name</td>

						<td valign="top" class="value">${userInstance.firstName}</td>

					</tr>

					<tr class="prop">
						<td valign="top" class="name">Last Name</td>

						<td valign="top" class="value">${userInstance.lastName}</td>

					</tr>

					<tr class="prop">
						<td valign="top" class="name">Date Created</td>

						<td valign="top" class="value">${userInstance.dateCreated}</td>

					</tr>

					<tr class="prop">
						<td valign="top" class="name">Last Updated</td>

						<td valign="top" class="value">${userInstance.lastUpdated}</td>

					</tr>

					</tbody>
				</table>
			</div>

			<div class="buttons">
				<form action="/spring_mvc/user/index" method="post">
					<input type="hidden" name="id" value="1" id="id"/>
					<span class="button"><input type="submit" name="_action_edit" value="Edit" class="edit"/></span>
					<span class="button"><input type="submit" name="_action_delete" value="Delete" class="delete"
														 onclick="return confirm('Are you sure?');"/></span>
				</form>
			</div>
		</div>

	</body>
</html>