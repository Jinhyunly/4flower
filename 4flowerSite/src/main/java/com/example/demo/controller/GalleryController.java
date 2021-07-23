package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.domain.user.UserPrincipal;

@Controller
public class GalleryController {
	@RequestMapping(value = "/gallery")
	public ModelAndView gallery() {

		ModelAndView modelAndView = new ModelAndView();

		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
			System.out.println(userPrincipal.toString());

			//modelAndView.addObject("userName", "Welcome " + userPrincipal.getName() + " (" + userPrincipal.getId() + ")");
			modelAndView.addObject("userName", userPrincipal.getName());
			modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");

		}catch(Exception e) {

		}
    modelAndView.setViewName("gallery");

		System.out.println("gallery controller start");
		return modelAndView;
	}
}
