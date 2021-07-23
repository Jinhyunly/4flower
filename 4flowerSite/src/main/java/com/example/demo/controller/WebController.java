package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.domain.user.UserPrincipal;

@Controller
public class WebController {
	//@RequestMapping(value = "/", method = RequestMethod.GET)

	@RequestMapping("/")
	public ModelAndView home(HttpServletRequest request, HttpSession session) {
//	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String logoutFlg = request.getParameter("logout");
    String logoutMessage = "";

    if (logoutFlg != null && !logoutFlg.isEmpty()) {
    	logoutMessage = "logout success";
      modelAndView.addObject("logoutMessage", logoutMessage);
      session.setAttribute("userPrincipal", null);
    }



		try{
			UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
			System.out.println(userPrincipal.toString());

			//modelAndView.addObject("userName", "Welcome " + userPrincipal.getName() + " (" + userPrincipal.getId() + ")");
			modelAndView.addObject("userName", userPrincipal.getName());
			modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");

		}catch(Exception e) {

		}

    modelAndView.setViewName("index");

		System.out.println("home controller start");
		//return "index";
		return modelAndView;
	}

}