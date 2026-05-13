package com.example.vehiclerental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	 	
	@Autowired 
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/register")
	public String register(@RequestParam String username,
	                       @RequestParam String password,
	                       @RequestParam String role) {

	    if (userRepository.findByUsername(username).isPresent()) {
	        return "User already exists";
	    }

	    AppUser user = new AppUser();
	    user.setUsername(username);

	    // encode password before saving
	    user.setPassword(passwordEncoder.encode(password));

	    user.setRole(role);

	    userRepository.save(user);

	    return "User registered successfully";
	}	 
	
	@PostMapping("/login")
	public String login(@RequestParam String username,
            @RequestParam String password) {
        //String token =  null;
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, password));

		// if authentication succeeds → generate token
		AppUser user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return jwtUtil.generateToken(user.getUsername(), user.getRole());
		
	
	}
}