package com.example.vehiclerental;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Autowired
	private CustomUserDetailsService customUserDetailService;
	
	@Bean//spring manages this object
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
            		  // Swagger
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**","/swagger-ui.html").permitAll()
            	.requestMatchers("/login","/register").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
               // .requestMatchers(HttpMethod.GET,"/vehicles").permitAll()
                .requestMatchers(HttpMethod.GET, "/vehicles").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/vehicles/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/vehicles").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/vehicles/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/vehicles/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/bookings").hasAnyRole("USER","ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/bookings/**").hasAnyRole("USER","ADMIN")
                .requestMatchers(HttpMethod.GET, "/bookings/my").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET,"/bookings").hasRole("ADMIN")
                .anyRequest().authenticated()
                
            )
            .exceptionHandling(exception -> exception 
            		.accessDeniedHandler(new CustomAccessDeniedHandler())
            )
			.authenticationProvider(authenticationProvider()) 
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
	}
	
	  @Bean 
	  public PasswordEncoder passwordEncoder() { 
		  return new BCryptPasswordEncoder();
	}
	 
	
	
	  @Bean 
	  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		  return config.getAuthenticationManager();
	  }
	 
	
	@Bean
	public AuthenticationProvider authenticationProvider() {

	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    
	    provider.setUserDetailsService(customUserDetailService);
	    provider.setPasswordEncoder(passwordEncoder());

	    return provider;
	}
}
