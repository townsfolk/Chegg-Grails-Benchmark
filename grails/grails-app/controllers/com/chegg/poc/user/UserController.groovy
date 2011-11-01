package com.chegg.poc.user

class UserController {

	UserService userService

	def index = {
		redirect(action: "create", params: params)
	}

	def create = {
		return [userInstance: new UserCommand()]
	}

	def save = { UserCommand userCommand ->
		if (!userCommand.hasErrors()) {
			def userInstance = userService.create(userCommand)
			if (userInstance.error) {
				flash.messageCode = 'default.not.created.message'
				flash.messageArgs = ['User']
				redirect(view: 'create', model: [userInstance: userInstance])
			} else {
				flash.messageCode = 'default.created.message'
				flash.messageArgs = ['User', userInstance.id]
				render(userInstance.id)
			}
		} else {
			render(view: 'create', model: [userInstance: userCommand])
		}
	}

	def show = {
		def userInstance = userService.findById(params.id)
		if (userInstance.error) {
			flash.messageCode = 'default.not.found.message'
			flash.messageArgs = ['User', params.id]
			redirect(action: "create")
		} else {
			[userInstance: userInstance]
		}
	}

	def edit = {
		def userInstance = userService.findById(params.id)
		if (userInstance.error) {
			flash.messageCode = 'default.not.found.message'
			flash.messageArgs = ['User', params.id]
			redirect(action: "create")
		} else {
			return [userInstance: userInstance]
		}
	}

	def update = { UserCommand userCommand ->
		def userInstance = userService.update(userCommand)
		if (userInstance.error) {
			flash.messageCode = 'default.not.found.message'
			flash.messageArgs = ['User', params.id]
			render(view: "edit", model: [userInstance: userInstance])
		} else {
			flash.messageCode = 'default.updated.message'
			flash.messageArgs = ['User', userInstance.id]
			redirect(action: "show", id: userInstance.id)
		}
	}

	def delete = {
		def deleted = userService.delete(params.id)
		if (deleted.error) {
			flash.messageCode = 'default.not.found.message'
			flash.messageArgs = ['User', params.id]
		} else {
			flash.messageCode = 'default.deleted.message'
			flash.messageArgs = ['User', params.id]
		}
		redirect(action: "create")
	}
}

class UserCommand {
	int id
	String firstName
	String lastName
	Date dateCreated
	Date lastUpdated

	static constraints = {
		firstName(blank: false, nullable: false)
		lastName(blank: false, nullable: false)
		dateCreated(nullable: true)
		lastUpdated(nullable: true)
	}
}
