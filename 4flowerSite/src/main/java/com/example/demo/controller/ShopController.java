package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShopController {
	//@RequestMapping(value = "**/shop", method = RequestMethod.GET)
	@RequestMapping(value = "/shop")
	public String shop() {
		System.out.println("shop controller start");
		return "shop";
	}
}
