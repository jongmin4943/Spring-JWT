package com.cos.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

@RestController
public class RestApiController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@GetMapping("/home")
	public String home() {
		return "<h1>home</h1>";
	}
	@PostMapping("/token")
	public String postHome() {
		return "<h1>token</h1>";
	}
	@PostMapping("/join")
	public String join(@RequestBody User user) {
		System.out.println(user);
		user.setRoles("ROLE_USER");
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
		return "<h1>join</h1>";
	}
	
	@GetMapping("/api/v1/user")
	public String user() {
		return "user";
	}
	@GetMapping("/api/v1/manager")
	public String manager() {
		return "manager";
	}
	@GetMapping("/api/v1/admin")
	public String admin() {
		return "admin";
	}
}
