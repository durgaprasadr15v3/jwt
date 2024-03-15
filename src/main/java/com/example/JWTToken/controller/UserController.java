package com.example.JWTToken.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.JWTToken.entity.AuthRequest;
import com.example.JWTToken.entity.UserInfo;
import com.example.JWTToken.service.JwtService;
import com.example.JWTToken.service.UserInfoService;

@RestController
@RequestMapping("/auth") 
public class UserController { 

	@Autowired
	private UserInfoService service; 

	@Autowired
	private JwtService jwtService; 

	@Autowired
	private AuthenticationManager authenticationManager; 

	@GetMapping("/welcome") 
	public String welcome() { 
		return "Welcome this endpoint is not secure"; 
	} 

	@PostMapping("/addNewUser") 
	public String addNewUser(@RequestBody UserInfo userInfo) { 
		return service.addUser(userInfo); 
	} 

	@GetMapping("/user/userProfile") 
	@PreAuthorize("hasAuthority('ROLE_USER')") 
	public String userProfile() { 
		return "Welcome to User Profile"; 
	} 

	@GetMapping("/admin/adminProfile") 
	@PreAuthorize("hasAuthority('ROLE_ADMIN')") 
	public String adminProfile() { 
		return "Welcome to Admin Profile"; 
	} 

	@PostMapping("/generateToken") 
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) { 
		System.out.println("fhknsdallfgjfskgjkfgbff");
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		System.out.println("fhknsdallfgjfskgjkfgbff");
		if (authentication.isAuthenticated()) { 
			
			System.out.println("fhknsdallfgjfskgjkfgbff");

				String	token=jwtService.generateToken(authRequest.getUsername()); 
				
				System.out.println("fhknsdallfgjfskgjkfgbff"+token);
					return token;
		} else { 
			throw new UsernameNotFoundException("invalid user request !"); 
		} 
	} 

} 
