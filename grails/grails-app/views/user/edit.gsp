<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="layout" content="main"/>
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
		<title><g:message code="default.edit.label" args="[entityName]"/></title>
	</head>

	<body>
		<div class="body">
			<h1><g:message code="default.edit.label" args="[entityName]"/></h1>
			<g:hasErrors bean="${userInstance}">
				<div class="errors">
					<g:renderErrors bean="${userInstance}" as="list"/>
				</div>
			</g:hasErrors>
			<g:form method="post">
				<g:hiddenField name="id" value="${userInstance?.id}"/>
				<g:hiddenField name="version" value="${userInstance?.version}"/>
				<div class="dialog">
					<table>
						<tbody>

						<tr class="prop">
							<td valign="top" class="name">
								<label for="firstName"><g:message code="user.firstName.label" default="First Name"/></label>
							</td>
							<td valign="top" class="value ${hasErrors(bean: userInstance, field: 'firstName', 'errors')}">
								<g:textField name="firstName" value="${userInstance?.firstName}"/>
							</td>
						</tr>

						<tr class="prop">
							<td valign="top" class="name">
								<label for="lastName"><g:message code="user.lastName.label" default="Last Name"/></label>
							</td>
							<td valign="top" class="value ${hasErrors(bean: userInstance, field: 'lastName', 'errors')}">
								<g:textField name="lastName" value="${userInstance?.lastName}"/>
							</td>
						</tr>

						</tbody>
					</table>
				</div>

				<div class="buttons">
					<span class="button"><g:actionSubmit class="save" action="update"
																	 value="${message(code: 'default.button.update.label', default: 'Update')}"/></span>
					<span class="button"><g:actionSubmit class="delete" action="delete"
																	 value="${message(code: 'default.button.delete.label', default: 'Delete')}"
																	 onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
				</div>
			</g:form>
		</div>
	</body>
</html>
