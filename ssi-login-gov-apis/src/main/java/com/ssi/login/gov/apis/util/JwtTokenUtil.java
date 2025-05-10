package com.ssi.login.gov.apis.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.sound.sampled.AudioFormat.Encoding;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil implements Serializable {
	private final long jwTExpiryInMin = 20 * 60 * 1000;

	// secrectString123456789012345678901234567890
	// private final SecretKey key =
	// Keys.hmacShaKeyFor(Decoders.BASE64.decode("c2VjcmVjdFN0cmluZzEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MA=="));
	private final SecretKey privateKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(
			//"-----BEGIN PRIVATE KEY----- MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgww1F6X70A9ofoO9d 0MlBVgT2US6c19bAxFHgZKHkzLShRANCAATVp7GYieCG/sw5ZCeHpsMUN79yP3t5 tC04ZJxEdoLVtGLbg9hTwJhEZ8VpAV6OvDdpLvxudtAOdBpp/ZEuT0F4 -----END PRIVATE KEY-----"));
	"LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tIE1JR0hBZ0VBTUJNR0J5cUdTTTQ5QWdFR0NDcUdTTTQ5QXdFSEJHMHdhd0lCQVFRZ3d3MUY2WDcwQTlvZm9POWQgME1sQlZnVDJVUzZjMTliQXhGSGdaS0hrekxTaFJBTkNBQVRWcDdHWWllQ0cvc3c1WkNlSHBzTVVONzl5UDN0NSB0QzA0Wkp4RWRvTFZ0R0xiZzloVHdKaEVaOFZwQVY2T3ZEZHBMdnh1ZHRBT2RCcHAvWkV1VDBGNCAtLS0tLUVORCBQUklWQVRFIEtFWS0tLS0t"));

	public String createJWT() {
		Map<String, Object> payLoad = new HashMap<String, Object>();
		payLoad.put("iat", "1706555412");
		payLoad.put("iss", "NH");
		payLoad.put("nonce", "eaa7e1d2c5e5312a");
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("alg", "ES256");
		headers.put("kid", "aLqiNec2SzQ_UKr50pyjvalfrg4Sd-7vfVp9jv-E8tI");
		 //byte[] headerBytes = Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(headers, Formatting.None));
		return Jwts.builder()//.setHeaderParam("typ", "JWT")
				//.setSubject("Default")//.claim("scopes", "profile")
				.signWith(privateKey).addClaims(payLoad).setHeaderParams(headers)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				//.setExpiration(new Date(System.currentTimeMillis() + jwTExpiryInMin))
				.compact();
	}

}