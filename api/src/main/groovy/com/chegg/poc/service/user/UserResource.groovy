package com.chegg.poc.service.user

import groovy.util.logging.Slf4j
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status
import javax.ws.rs.core.UriBuilder
import org.springframework.stereotype.Controller
import javax.ws.rs.*
import static javax.ws.rs.core.MediaType.APPLICATION_JSON

/**
 * User: elberry
 * Date: 10/14/11
 */
@Path('user')
@Controller
@Slf4j
class UserResource {

	HashMap<Long, User> users = new HashMap<Long, User>()

	@DELETE
	@Path('{id}')
	@Produces(APPLICATION_JSON)
	Response deleteUser(@PathParam('id') Long id, @QueryParam('returnEntity') boolean returnEntity = false) {
		if (users[id]) {
			def user = users.remove(id)
			if (returnEntity) {
				Response.ok(user).build()
			} else {
				Response.ok().build()
			}
		} else {
			Response.noContent().status(Status.NOT_FOUND).build()
		}
	}

	@GET
	@Path('entities')
	@Produces(APPLICATION_JSON)
	Response describe() {
		User userSample = new User(dateCreated: new Date(), firstName: 'Sample', lastName: 'User', id: 1, lastUpdated: new Date())
		def entity = [
				"${User.class}": userSample
		]
		return Response.ok(entity).build()
	}

	@GET
	@Path('clear')
	@Produces(APPLICATION_JSON)
	Response clear() {
		int size = users.size()
		users.clear()
		Response.ok([count: size]).build()
	}

	@GET
	@Path('{id}')
	@Produces(APPLICATION_JSON)
	Response findById(@PathParam('id') Long id) {
		def user = users[id]
		if (user) {
			if (log.isDebugEnabled()) {
				log.debug("found user with id: ${id}")
			}
			Response.ok(user).build()
		} else {
			if (log.isDebugEnabled()) {
				log.debug("could not find user with id: ${id}")
			}
			Response.noContent().status(Status.NOT_FOUND).build()
		}
	}

	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	Response createUser(User user, @QueryParam('returnEntity') boolean returnEntity = false) {
		user.id = System.currentTimeMillis()
		user.dateCreated = new Date()
		user.lastUpdated = user.dateCreated
		if (log.isDebugEnabled()) {
			log.debug("Creating user with id: ${user.id} - wants entity: ${returnEntity}")
		}
		users[user.id] = user
		UriBuilder builder = UriBuilder.fromResource(getClass()).path('{id}')
		URI uri = builder.build(user.id)
		def response = Response.created(uri).header('X-entity-id', user.id)
		if (returnEntity) {
			response.entity(user).build()
		} else {
			response.build()
		}
	}

	@PUT
	@Path('{id}')
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	Response updateUser(@PathParam('id') Long id, User user, @QueryParam('returnEntity') boolean returnEntity = false, @QueryParam('createIfNotFound') boolean createIfNotExists = false) {
		def dbUser = users[id]
		if (dbUser) {
			dbUser.firstName = user.firstName
			dbUser.lastName = user.lastName
			dbUser.lastUpdated = new Date()
			if (returnEntity) {
				Response.ok(dbUser).build()
			} else {
				Response.ok().build()
			}
		} else if (createIfNotExists) {
			createUser(user, returnEntity)
		} else {
			Response.noContent().status(Status.NOT_FOUND).build()
		}
	}
}
