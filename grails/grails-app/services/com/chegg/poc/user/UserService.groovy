package com.chegg.poc.user

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.api.client.config.ClientConfig
import com.sun.jersey.api.client.config.DefaultClientConfig
import com.sun.jersey.api.json.JSONConfiguration
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.UriBuilder
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.jackson.map.DeserializationConfig
import org.codehaus.jackson.map.ObjectMapper

class UserService {

	static transactional = false
	String userApiHost = ConfigurationHolder.config.userapi.host ?: "http://localhost:8091/user-api"
	Client userApiClient
	ObjectMapper mapper

	UserService() {
		ClientConfig config = new DefaultClientConfig()
		config.features[JSONConfiguration.FEATURE_POJO_MAPPING] = true
		userApiClient = Client.create(config)
		mapper = new ObjectMapper()
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
	}

	def findById(String id) {
		if (log.isDebugEnabled()) {
			log.debug("Attempting to find user by ID: ${id}")
		}
		def user
		URI uri = UriBuilder.fromUri(userApiHost).path("user/{id}").build(id)

		WebResource resource = userApiClient.resource(uri)
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class)
		ClientResponse.Status status = response.getClientResponseStatus()
		switch (status) {
			case ClientResponse.Status.OK:
				def entity = response.getEntity(Map.class)
				user = entity
				break
			case ClientResponse.Status.BAD_REQUEST:
				user = [error: "Unable to find user by id: ${id}"]
				break
		}
		user ?: [:]
	}

	def create(def user) {

		def created

		URI uri = UriBuilder.fromUri(userApiHost).path("user").build()

		WebResource resource = userApiClient.resource(uri)
		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, user)
		ClientResponse.Status status = response.getClientResponseStatus()
		switch (status) {
			case ClientResponse.Status.CREATED:
				MultivaluedMap headers = response.headers
				def userId = headers.'X-entity-id'[0]
				created = [id: userId]
				break
			case ClientResponse.Status.BAD_REQUEST:
				created = [error: "Unable to create user"]
				break
		}
		created ?: [:]
	}
}