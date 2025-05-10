package com.ssi.ms.logingov.apis.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ssi.ms.logingov.apis.util.JwtTokenUtil;

/**
 * JWTHandler class is responsible for handling JSON Web Tokens (JWT) in a Java application.
 * It provides methods for generating JWTs, validating them, and extracting information from them.
 * This class uses the jjwt library for JWT manipulation.
 * @Author: munirathnam.surepall
 */
@Component
public class JWTHandler {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	/**
	 * This method retrieves a JSON Web Token (JWT) used for authentication.
	 * @return The JWT string representing the authentication token.
	 * @throws Exception If there is an error generating or retrieving the JWT.
	 */
	public String getJWT() throws Exception {
		return jwtTokenUtil.createJWT();
	}
}
