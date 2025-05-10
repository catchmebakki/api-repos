package com.ssi.ms.logingov.apis.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ssi.ms.logingov.apis.constant.ApplicationConstants;
import com.ssi.ms.logingov.apis.constant.ErrorMessageConstant;
import com.ssi.ms.logingov.apis.exception.custom.FileProcessingException;
import com.ssi.ms.logingov.apis.exception.custom.NotFoundException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * This class is a utility component for handling JSON Web Tokens (JWT) issued
 * by Login.gov. It provides methods for parsing, validating, and extracting
 * information from JWT tokens.
 * 
 * @Author: munirathnam.surepall
 */
@Component
public class JwtTokenUtil {
	private final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
	private PKCS8EncodedKeySpec keySpec;
	// Reads the contents of a PEM file specified by the private key path.
	@Value("${privatekey-path}")
	private String privateKeyPath;

	// Injects the value of the property 'kid' from your application.yml file
	@Value("${kid}")
	private String kidValue;

	/**
	 * Creates a JSON Web Token (JWT) for authentication or authorization purposes.
	 * This method generates a JWT with the required claims and signs it using the configured
	 * cryptographic algorithm and key.
	 *
	 * @return The generated JSON Web Token (JWT) as a String.
	 */
	public String createJWT() {
		// Construct the JWT payload
		final Map<String, Object> payload = new HashMap<>();
		payload.put(ApplicationConstants.IAT, Math.floor(System.currentTimeMillis() / 1000));
		payload.put(ApplicationConstants.ISS, ApplicationConstants.SWA_OR_ISSUER);
		payload.put(ApplicationConstants.NONCE, getHexString());

		// Build the JWT
		return Jwts.builder().setHeaderParam(ApplicationConstants.KID, kidValue).setClaims(payload)
				.signWith(generatePrivateKey(), SignatureAlgorithm.ES256).compact();
	}

	/**
	 * Generates a private key using the specified algorithm.
	 * 
	 * @return The generated private key.
	 * @throws NoSuchAlgorithmException If the specified algorithm for key
	 *                                  generation is not available.
	 */
	public PrivateKey generatePrivateKey() {
		KeyFactory keyFactory;
		PrivateKey privateKey = null;
		try {
			final String pemPrivateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
			final String privateKeyPEM = pemPrivateKey.replace("-----BEGIN PRIVATE KEY-----", "")
					.replace("-----END PRIVATE KEY-----", "").replace("\n", "").replaceAll("\\s", "");
			final byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
			keyFactory = KeyFactory.getInstance(ApplicationConstants.ALG_EC);
			keySpec = new PKCS8EncodedKeySpec(keyBytes);
			privateKey = keyFactory.generatePrivate(keySpec);

		} catch (IOException e) {
			e.printStackTrace();
			logger.error(ErrorMessageConstant.IOEXCEPTION_MSG);
			throw new FileProcessingException("Unable to read the file for the path: " + privateKeyPath,
					ErrorMessageConstant.FILE_READ_FAILED, e);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error(ErrorMessageConstant.NOSUCHALGORITHMEXCEPTION_MSG);
			throw new NotFoundException("Unable to find the specified algorithm: " + ApplicationConstants.ALG_EC,
					ErrorMessageConstant.NO_SUCH_ALGORITHM_FOUND);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			logger.error(ErrorMessageConstant.INVALIDKEYSPECEXCEPTION_MSG);
			throw new NotFoundException("Unable to find the specified keySpec: " + keySpec,
					ErrorMessageConstant.KEYSPEC_INVALID);
		}
		return privateKey;
	}

	/**
	 * Returns the hexadecimal representation of a string. This method converts each
	 * character of the input string into its corresponding hexadecimal
	 * representation and concatenates them to form a single string.
	 *
	 * @return Tthe hexadecimal representation of the input string.
	 */
	public String getHexString() {
		final Random random = new Random();
		final StringBuffer strBuffer = new StringBuffer();
		while (strBuffer.length() < 17) {
			strBuffer.append(Integer.toHexString(random.nextInt()));
		}
		return strBuffer.toString().substring(0, 15);
	}

}
