package com.chegg.poc.service.user

import org.codehaus.jackson.annotate.JsonIgnoreProperties

/**
 * User: elberry
 * Date: 10/14/11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class User {
	long id
	String firstName
	String lastName
	Date dateCreated
	Date lastUpdated
}