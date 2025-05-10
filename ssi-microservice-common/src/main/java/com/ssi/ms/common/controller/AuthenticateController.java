package com.ssi.ms.common.controller;

import static com.ssi.ms.constant.AlvCodeConstantParent.forAlvCode;
import static com.ssi.ms.constant.ErrorMessageConstant.USER_NOT_FOUND;

import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssi.ms.common.dto.JwtClaimDTO;
import com.ssi.ms.common.dto.UserDetailsDTO;
import com.ssi.ms.common.service.ScreenRolesAccessService;
import com.ssi.ms.common.service.UserService;
import com.ssi.ms.constant.AlvLogConstant.SRLAccessCd;
import com.ssi.ms.constant.SecurityConstant;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.platform.handler.JWTHandler;

import io.jsonwebtoken.Claims;

/**
 * @author Praveenraja Paramsivam AuthenticateController provides services to
 *         getUser, login apllication, login claim, getRefreshToken,
 *         getAccessToken and validate jwt token.
 */
@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthenticateController {
	@Autowired
	private JWTHandler jWTHandler;

	@Autowired
	private UserService userService;

	@Autowired
	private ScreenRolesAccessService screenRolesAccessService;

	/**
	 * This function is used for get user information.
	 * @param userId {@link Long} The ID of the user to retrieve information for.
	 * @return {@link ResponseEntity<Object>} ResponseEntity containing the user
	 *         information as the response body.
	 */
	@GetMapping(path = "/getUser")
	@ResponseBody
	public ResponseEntity<Object> getUser(final Long userId) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(userService.getUserById(userId));
	}

	/**
	 * Perform user login based on the provided HttpServletRequest.
	 *
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated
	 *                with the login request.
	 * @return {@link ResponseEntity<String>} A ResponseEntity containing the login
	 *         response as the response body.
	 */
	@GetMapping(path = "/login")
	public ResponseEntity<String> login(final HttpServletRequest request) {
		final String token = jWTHandler.getJWT(null, request);
		System.out.println(token);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
				new StringJoiner(":", "{", "}").add("\"id\"").add("\"" + jWTHandler.getJWT(request) + "\"").toString());
	}

	/**
	 * Perform user login with JWT claim information based on the provided
	 * JwtClaimDTO and HttpServletRequest.
	 *
	 * @param claimDTO {@link JwtClaimDTO} The JwtClaimDTO containing the JWT claim
	 *                 information for the login.
	 * @param request  {@link HttpServletRequest} The HttpServletRequest associated
	 *                 with the login request.
	 * @return {@link ResponseEntity<String>} A ResponseEntity containing the login
	 *         response as the response body.
	 */
	@PostMapping(path = "/login")
	public ResponseEntity<String> loginWithClaim(@Valid @RequestBody final JwtClaimDTO claimDTO,
			final HttpServletRequest request) {
		final String token = jWTHandler.getJWT(claimDTO, request);
		System.out.println(token);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new StringJoiner(":", "{", "}")
				.add("\"id\"").add("\"" + token + "\"").toString());
	}

	/**
	 * Retrieve a refresh token based on the provided HttpServletRequest.
	 *
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated
	 *                with the refresh token request.
	 * @return {@link ResponseEntity} A ResponseEntity containing the refresh token
	 *         response as the response body.
	 */
	@GetMapping(path = "/refreshToken")
	public ResponseEntity getRefreshToken(final HttpServletRequest request) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(jWTHandler.getAccessAndRefreshJWTs(request, true));
	}

	/**
	 * This method, GET request to retrieve the access token.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated
	 *                with the access token request.
	 * @return {@link ResponseEntity} A ResponseEntity containing the refresh token
	 *         response as the response body.
	 */
	@GetMapping(path = "/accessToken")
	public ResponseEntity getAccessToken(final HttpServletRequest request) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(jWTHandler.getAccessAndRefreshJWTs(request, false));
	}

	/**
	 * This function, GET request to validate a JWT token and retrieve user details.
	 * @param request {@link HttpServletRequest} The HttpServletRequest associated
	 *                with the JWT token validation request.
	 * @return {@link ResponseEntity<UserDetailsDTO>} A ResponseEntity containing
	 *         the validation response as the response body.
	 */
	@GetMapping(path = "/validateJwtToken")
	public ResponseEntity<UserDetailsDTO> validateJwtToken(final HttpServletRequest request) {
		final StringBuilder builder = new StringBuilder();
		final StringBuilder srlAccessCdValue = new StringBuilder();
		return Optional.ofNullable((Claims) request.getAttribute(SecurityConstant.CLAIMS)).map(claims -> {
			builder.append(claims.get(SecurityConstant.ClaimsBody.USER_ID.getValue(), String.class));
			final Map<Integer, String> accessRolesData = screenRolesAccessService.loadAccessRoles(
					claims.get(SecurityConstant.ClaimsBody.SCOPES.getValue(), String.class));
			final String srlAccessCode = accessRolesData
					.get(claims.get(SecurityConstant.ClaimsBody.ROLE_ID.getValue(), Integer.class));
			if (StringUtils.isNotBlank(srlAccessCode)) {
				try {
					srlAccessCdValue.append(forAlvCode(SRLAccessCd.class, Integer.valueOf(srlAccessCode)).name());
				} catch (IllegalArgumentException e) {
					srlAccessCdValue.append(SRLAccessCd.NONE.name());
				}
			} else {
				srlAccessCdValue.append(SRLAccessCd.NONE.name());
			}
			return builder.toString();
		}).map(userId -> this.userService.getUserById(Long.valueOf(userId))).map(userDao -> {
			userDao.setSrlAccessCdValue(srlAccessCdValue.toString());
			return userDao;
		}).map(userDao -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(userDao)).orElseThrow(
				() -> new NotFoundException("Requested user id not Found:" + builder.toString(), USER_NOT_FOUND));
	}

}
