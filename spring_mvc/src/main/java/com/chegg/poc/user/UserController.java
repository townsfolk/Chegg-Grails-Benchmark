package com.chegg.poc.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * User: elberry
 * Date: 10/28/11
 */
@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping("create")
	public void create() {

	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(UserCommand userCommand) {
		//System.out.println("save");
		Map<String, Object> userInstance = userService.create(userCommand);
		Map<String, Object> model = new HashMap<String, Object>();
		if (userInstance.containsKey("error")) {
			model.put("message", userInstance.get("error"));
			return new ModelAndView("user/create", model);
		} else {
			model.put("userInstance", userInstance);
			return new ModelAndView("user/save", model);
		}
	}

	@RequestMapping("show/{id}")
	public ModelAndView show(@PathVariable("id") String id) {
		Map<String, Object> userInstance = userService.findById(id);
		Map<String, Object> model = new HashMap<String, Object>();
		if (userInstance.containsKey("error")) {
			model.put("message", userInstance.get("error"));
			return new ModelAndView("user/create", model);
		} else {
			model.put("userInstance", userInstance);
			return new ModelAndView("user/show", model);
		}
	}
}
