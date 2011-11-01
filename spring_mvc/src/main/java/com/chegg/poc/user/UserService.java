package com.chegg.poc.user;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * User: elberry
 * Date: 10/28/11
 */
@Service
public class UserService {
	String userApiHost = "http://localhost:8091/user-api";
	Client userApiClient;
	ObjectMapper mapper;

	public UserService() {
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		userApiClient = Client.create(config);
		mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public Map<String, Object> findById(String id) {
		URI uri = UriBuilder.fromUri(userApiHost).path("user/{id}").build(id);

		WebResource resource = userApiClient.resource(uri);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		ClientResponse.Status status = response.getClientResponseStatus();
		switch (status) {
			case OK:
				return response.getEntity(Map.class);
			case BAD_REQUEST:
				Map<String, Object> error = new HashMap<String, Object>();
				error.put("error", "Unable to find user by id: " + id);
				return error;
		}
		return new HashMap<String, Object>();
	}

	Map<String, Object> create(Object user) {
		Map<String, Object> entity = new HashMap<String, Object>();
		URI uri = UriBuilder.fromUri(userApiHost).path("user").build();

		WebResource resource = userApiClient.resource(uri);
		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, user);
		ClientResponse.Status status = response.getClientResponseStatus();
		switch (status) {
			case CREATED:
				MultivaluedMap<String, String> headers = response.getHeaders();
				String userId = headers.get("X-entity-id").get(0);
				entity.put("id", userId);
				break;
			case BAD_REQUEST:
				entity.put("error", "Unable to create user");
				break;
		}
		return entity;
	}
}
