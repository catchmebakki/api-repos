package com.ssi.ms.logingov.apis.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.ssi.ms.logingov.apis.builder.JwtBuilder;
import com.ssi.ms.logingov.apis.constant.ApplicationConstants;
import com.ssi.ms.logingov.apis.constant.ErrorMessageConstant;
import com.ssi.ms.logingov.apis.dto.ClaimsResDTO;
import com.ssi.ms.logingov.apis.dto.SwaRecordFetchedDTO;
import com.ssi.ms.logingov.apis.exception.custom.CustomJoseException;
import com.ssi.ms.logingov.apis.exception.custom.FileProcessingException;
import com.ssi.ms.logingov.apis.exception.custom.NotFoundException;
import com.ssi.ms.logingov.apis.handler.JWTHandler;

import lombok.NoArgsConstructor;

/**
 * The LoginGovService class provides functionality related to login with Gov
 * services. This service handles authentication, authorization, and other
 * operations related to Gov login.
 * 
 * @Author: munirathnam.surepall
 */
@Service
@NoArgsConstructor
public class LoginGovService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginGovService.class);
	private static String jwtToken;
	private static RestTemplate restTemplate = new RestTemplate();
	private static HttpHeaders headers = new HttpHeaders();
	private String url;
	// Define a field to hold the value of the property "webservice_path"
	@Value("${webservice-path}")
	private String loginGovPrefix;

	// Reads the contents of a PEM file specified by the private key path.
	@Value("${privatekey-path}")
	private String privateKeyPath;

	// This annotation injects the value of the property 'kid' from your
	// application.yml file
	@Value("${kid}")
	private String kidValue;

	@Autowired
	private JWTHandler jWTHandler;

	/**
	 * Retrieves SWA records based on the provided SWA XID.
	 * 
	 * @param swaXid The SWA XID (identifier) used to fetch corresponding records.
	 * @return ResponseEntity<String> A ResponseEntity containing SWA records in the
	 *         body.
	 * @throws Exception If there's an error occurred during the retrieval process.
	 */
	public ResponseEntity<String> getRecordsBySwaXidOrUuid(String swaXid) throws Exception {
		url = loginGovPrefix + ApplicationConstants.LOGIN_GOV_URL_PATH + swaXid;
		jwtToken = "JWT " + jWTHandler.getJWT();
		final HttpEntity<String> jwtEntity = new HttpEntity<>(getHeaders(headers, jwtToken));
		return restTemplate.exchange(url, HttpMethod.GET, jwtEntity, String.class);
	}

	/**
	 * Retrieves all claims from the system and returns a list of decrypted claim
	 * data transfer objects.
	 * 
	 * @return List of GetClaimDecryptedResDTO objects containing decrypted claim
	 *         information.
	 * @throws Exception if there's an error while retrieving or decrypting the
	 *                   claims.
	 */
	public ResponseEntity<List<ClaimsResDTO>> getAllClaims() throws Exception {
		ResponseEntity<List<ClaimsResDTO>> resEntity = null;
		url = loginGovPrefix + ApplicationConstants.LOGIN_GOV_URL_PATH;
		jwtToken = "JWT " + jWTHandler.getJWT();
		final HttpEntity<String> jwtEntity = new HttpEntity<>(getHeaders(headers, jwtToken));
		try {
			resEntity = (ResponseEntity<List<ClaimsResDTO>>) getDecrypt(new ObjectMapper()
					.readTree(restTemplate.exchange(url, HttpMethod.GET, jwtEntity, String.class).getBody())
					.get("claims"));
		} catch (HttpClientErrorException e) {
			resEntity = new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessageConstant.CLAIMS_READ_FAILED, e);
		}
		return resEntity;
	}

	/**
	 * Retrieves and decrypts a list of claim results. This method takes a JSON node
	 * containing claim results and returns a ResponseEntity containing a list of
	 * decrypted claim results. The decryption process is performed based on the
	 * provided JSON node.
	 *
	 * @param results The JSON node containing the encrypted claim results.
	 * @return A ResponseEntity containing a list of decrypted claim results.
	 */
	public ResponseEntity<List<ClaimsResDTO>> getDecrypt(JsonNode results) {
		final List<ClaimsResDTO> claimsResDTOList = new ArrayList<>();
		ResponseEntity<List<ClaimsResDTO>> claimDecryptedList = null;
		final ObjectMapper objectMapper = new ObjectMapper();
		results.forEach(encrypted -> {
			String privateKey;
			try {
				privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
				final JwtBuilder jwtBuilder = new JwtBuilder(privateKey, kidValue, ApplicationConstants.SWA_OR_ISSUER,
						ApplicationConstants.ALGORITHM_ES256);
				final String decryptedResults = jwtBuilder.decrypt(encrypted.get("claim").get("ciphertext").asText(),
						encrypted.get("claim").get("header").get("epk").get("x").asText(),
						encrypted.get("claim").get("header").get("epk").get("y").asText(),
						encrypted.get("claim").get("iv").asText(), encrypted.get("claim").get("tag").asText(),
						encrypted.get("claim").get("encrypted_key").asText(),
						encrypted.get("claim").get("protected").asText());
				final JsonNode jsonNode = objectMapper.readTree(decryptedResults);
				final ClaimsResDTO claimsResDTO = objectMapper.treeToValue(jsonNode, ClaimsResDTO.class);
				claimsResDTOList.add(claimsResDTO);
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.error(ErrorMessageConstant.IOEXCEPTION_MSG);
				claimsResDTOList.removeAll(claimsResDTOList);
				throw new FileProcessingException("Unable to read the file for the path: " + privateKeyPath,
						ErrorMessageConstant.FILE_READ_FAILED, e);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				LOGGER.error(ErrorMessageConstant.NOSUCHALGORITHMEXCEPTION_MSG);
				claimsResDTOList.removeAll(claimsResDTOList);
				throw new NotFoundException(
						"Unable to find the specified algorithm: " + ApplicationConstants.ALGORITHM_ES256,
						ErrorMessageConstant.NO_SUCH_ALGORITHM_FOUND);

			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
				LOGGER.error(ErrorMessageConstant.INVALIDKEYSPECEXCEPTION_MSG);
				claimsResDTOList.removeAll(claimsResDTOList);
				throw new NotFoundException("Invalid keyspec :  " + e.getMessage(),
						ErrorMessageConstant.KEYSPEC_INVALID);
			} catch (JOSEException e) {
				e.printStackTrace();
				LOGGER.error(ErrorMessageConstant.JOSEEXCEPTION_MSG);
				claimsResDTOList.removeAll(claimsResDTOList);
				throw new CustomJoseException("JOSE Exception :  " + e.getMessage(),
						ErrorMessageConstant.CLAIM_READ_FAILED);
			}

		});
		if (claimsResDTOList.isEmpty()) {
			claimDecryptedList = new ResponseEntity<>(claimsResDTOList, HttpStatus.NOT_FOUND);
		} else {
			claimDecryptedList = new ResponseEntity<>(claimsResDTOList, HttpStatus.OK);
		}
		return claimDecryptedList;
	}

	/**
	 * Marks a claim as fetched.
	 *
	 * @param swaRecordFetchedDTO The data transfer object containing information
	 *                            about the SWA record being marked as fetched.
	 * @param swaXid              The unique identifier of the SWA record.
	 * @return A ResponseEntity indicating the status of the operation.
	 * @throws Exception If an error occurs during the operation.
	 */
	public ResponseEntity<String> markClaimAsFetched(SwaRecordFetchedDTO swaRecordFetchedDTO, String swaXid)
			throws Exception {
		jwtToken = "JWT " + jWTHandler.getJWT();
		url = loginGovPrefix + ApplicationConstants.LOGIN_GOV_URL_PATH + swaXid + ApplicationConstants.FSLASH;
		final HttpEntity<SwaRecordFetchedDTO> entity = new HttpEntity<>(swaRecordFetchedDTO,
				getHeaders(headers, jwtToken));
		final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		restTemplate = new RestTemplate(requestFactory);

		return new ResponseEntity<>(restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class).getStatusCode());
	}

	/**
	 * Retrieves the HttpHeaders with the specified JWT token added to the existing
	 * headers.
	 * 
	 * @param headers  The HttpHeaders object representing the existing headers.
	 * @param jwtToken The JWT token to be added to the headers.
	 * @return The modified HttpHeaders object with the JWT token added.
	 */
	public HttpHeaders getHeaders(HttpHeaders headers, String jwtToken) {
		headers.set(ApplicationConstants.AUTHORIZATION, jwtToken);
		headers.set(ApplicationConstants.USER_AGENT,
				ApplicationConstants.SWA_OR_ISSUER + ApplicationConstants.SWACLIENT + ApplicationConstants.VERSION);
		return headers;
	}

}