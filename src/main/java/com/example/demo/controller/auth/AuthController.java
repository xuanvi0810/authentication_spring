package com.example.demo.controller.auth;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.domain.User;
import com.example.demo.model.UserLoginDto;


@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private HttpSession session;
	
	@GetMapping("login")
	public ModelAndView login(ModelMap model) {
		UserLoginDto user = new UserLoginDto();
		model.addAttribute("user", user);
		return new ModelAndView("auth/login", model);
	}
	
	@PostMapping("login")
	public ModelAndView login(ModelMap model,
			@Valid @ModelAttribute("user") UserLoginDto userLoginDto,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("auth/login");
		}
		User user = new User(userLoginDto.getUsername(), userLoginDto.getPassword());
//		if(user==null) {
//			model.addAttribute("error", "Invalid username or email");
//			return new ModelAndView("auth/login", model);
//		}
		session.setAttribute("user", user);
		Object redirectUri= session.getAttribute("redirect-uri");
		if(redirectUri!=null) {
			session.removeAttribute("redirect-uri");
			return new ModelAndView("redirect:"+redirectUri);
		}
		
		return new ModelAndView("redirect:/");
	}
	
	
	@RequestMapping("logout")
	public ModelAndView logout(ModelMap model) {
		session.removeAttribute("user");
		return new ModelAndView("redirect:/");
	}
	
}
