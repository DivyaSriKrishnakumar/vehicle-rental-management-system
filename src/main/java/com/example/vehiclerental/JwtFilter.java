package com.example.vehiclerental;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//intercepts every http request
//extracts JWT from the header -> validate & parse it
//sets authentication in spring security context
@Component//register this as a spring bean
public class JwtFilter extends OncePerRequestFilter {

//OncePerRequestFilter basic flow	
//	Incoming request
//	   ↓
//	shouldNotFilter() → check skip?so override to not generate JWT when ->Login
//	   ↓
//	doFilterInternal() → actual filtering logic
	    private final JwtUtil jwtUtil;//used for extracting username, role

	    //constructor injection
	    public JwtFilter(JwtUtil jwtUtil) {
	        this.jwtUtil = jwtUtil;
	    }

	    //in login only token generated, no token exist -> no need to validate
	    @Override
	    protected boolean shouldNotFilter(HttpServletRequest request) {
	        return request.getServletPath().equals("/login");
	    }
//with overriding	    
//	 Authorization header → extract token
//	    ↓
//	 Validate token
//	    ↓
//	 Extract username + role
//	    ↓
//	 Set Authentication in SecurityContext

	    @Override
	    protected void doFilterInternal(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    FilterChain filterChain)
	            throws ServletException, IOException {

	        String authHeader = request.getHeader("Authorization");//get authorization header
	              
	        if (authHeader == null || !authHeader.startsWith("Bearer "))//validate header format
	        {
	        	
	            filterChain.doFilter(request, response);
	            return;
	        }
	        System.out.println("Header: " + authHeader);//Testing
	        
	        String token = authHeader.substring(7);//extract token
	        String username = jwtUtil.extractUsername(token);//extract data from jwt
	        String role = jwtUtil.extractRole(token);
	        
	        //Validate token
	        if (!jwtUtil.validateToken(token, username)) {
	            filterChain.doFilter(request, response);
	            return;
	        }
            
	        //Role based (Authorization)convert role -> authority
	        List<SimpleGrantedAuthority> authorities =
	                List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

	        //create authentication object
	        UsernamePasswordAuthenticationToken authToken =
	                new UsernamePasswordAuthenticationToken(username, null, authorities);
	        System.out.println("ROLE FROM TOKEN: " + role); //testing purpose

	        //set security context
	        SecurityContextHolder.getContext().setAuthentication(authToken);

	        //continue filterchain-> pass request to next filter/controller
	        filterChain.doFilter(request, response);
	    }
}