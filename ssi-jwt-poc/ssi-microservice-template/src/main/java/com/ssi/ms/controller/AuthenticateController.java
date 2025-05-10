package com.ssi.ms.controller;

import java.util.List;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssi.ms.handler.JWTHandler;
import com.ssi.service.core.platform.util.JwtTokenUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;

@RestController
@RequestMapping("/auth")
public class AuthenticateController {

    @Autowired
    private JWTHandler jWTHandler;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping(path = "/login")
    public ResponseEntity<String> login(final HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new StringJoiner(":", "{", "}")
                .add("\"id\"").add("\"" + jWTHandler.getJWT(request) + "\"")
                .toString());
    }

    @GetMapping(path = "/validateJwtToken")
    public ResponseEntity<String> validateJwtToken(@RequestBody final String token) {
     String jwtToken = token;
     //String token_prefix = "Bearer ";
   //final int bearerLength = 7;
   String isValidToken = "true";
  // final String SCOPES = "scopes";
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
      /*  if (token.startsWith("Bearer ")) {
            jwtToken = token.substring(bearerLength);
            try {
                if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Jws<Claims> jwtClims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
                    Authentication auth = new UsernamePasswordAuthenticationToken(jwtClims.getBody().getSubject(),
                            jwtToken, List.of(new SimpleGrantedAuthority(jwtClims.getBody().get("scopes", String.class))));
                    SecurityContextHolder.getContext().setAuthentication(auth);

                }
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else { */

            //LOGGER.warn("JWT Token does not begin with Bearer String");
            try {
                if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Jws<Claims> jwtClims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
                    Authentication auth = new UsernamePasswordAuthenticationToken(jwtClims.getBody().getSubject(),
                            jwtToken, List.of(new SimpleGrantedAuthority(jwtClims.getBody().get("scopes", String.class))));
                    SecurityContextHolder.getContext().setAuthentication(auth);

                }
            } catch (IllegalArgumentException e) {
isValidToken = "false";
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
isValidToken = "false";
                System.out.println("JWT Token has expired");
            }
            //System.out.println("JWT Token has expired");
       // }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new StringJoiner(":", "{", "}")
                .add("\"res\"").add("\"" + isValidToken + "\"")
                .toString());
    }
}
