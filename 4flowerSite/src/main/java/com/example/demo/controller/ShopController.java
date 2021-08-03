package com.example.demo.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.security.UserInfo;
import com.example.demo.security.service.MyUserDetails;

@Controller
public class ShopController {
	//@RequestMapping(value = "**/shop", method = RequestMethod.GET)
	@RequestMapping(value = "/shop")
	public ModelAndView shop(Principal principal) {

		ModelAndView modelAndView = new ModelAndView();

		try{
			Authentication authentication = (Authentication) principal;
			MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
			UserInfo userInfo = userDetails.getUserInfo();
			modelAndView.addObject("userName", userInfo.getUserName());
//			modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");

		}catch(Exception e) {
		}
		modelAndView.setViewName("shop");


		System.out.println("shop controller start");
		return modelAndView;
	}
}
