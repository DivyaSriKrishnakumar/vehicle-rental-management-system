package com.example.vehiclerental;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	 private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymysecretkey";

	 //every time it runs -> generate and parse the token
	 //converts string into cryptographic key
	 private Key getSignKey() {
	        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}
    //used to extract username
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)//extractUsername() in JwtFilter,verify signature decodes payload
                .getBody()//and get body
                .getSubject();//get subject
    }
    //verify identity(username) and validity
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
    //used by extractAllClaims
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
   //used by extractAllClaims
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    //reusualble method to extract any field, eg: token, Claims::methodname
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    //token creation
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();//create claims

        claims.put("role", role.toUpperCase());//JWT will contain ROLE

        return Jwts.builder() //builder from JWT library, build token
                .setClaims(claims)// add extra data to token (eg:{'role':'USER'}),add claims
                .setSubject(username)// (eg:{'username':'divya'})
                .setIssuedAt(new Date())//token was created (eg:{'iat':ddmmyyyy}),set timestamps
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))//token valid for 1hour
                .signWith(getSignKey())//creates digital sign
                .compact();//converts->HEADER.PAYLOAD.SIGNATURE->payload { 'sub':'divya','iat':ddmmyyyy,'exp':1hour}
    }
    
    //when you want to extract role directly from JWT(Optional)
    //used by extractAllClaims
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
    

}
