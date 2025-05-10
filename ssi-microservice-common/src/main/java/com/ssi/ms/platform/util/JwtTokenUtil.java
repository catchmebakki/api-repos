package com.ssi.ms.platform.util;

import com.ssi.ms.common.dto.JwtClaimDTO;
import com.ssi.ms.constant.SecurityConstant;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.context.request.WebRequest;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.function.BiFunction;

import static com.ssi.ms.constant.ErrorMessageConstant.CLAIMANT_NOT_FOUND;


/**
 * @author Praveenraja Paramsivam
 * JwtTokenUtil provides sevices to retrieve all claims, retrieve a JwtClaimDTO object and different objects.
 */
@SuppressWarnings("PMD.FieldNamingConventions")
public class JwtTokenUtil {

    private static final int TO_MILL_SECONDS = 60 * 1000;
    private static final int REQUEST_SCOPE_FOR_WEB_REQUEST = 0;

    // secretString12345678901234567890
    private static final SecretKey KEY = Keys
            .hmacShaKeyFor(Decoders.BASE64.decode("c2VjcmVjdFN0cmluZzEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MA=="));

    private static final BiFunction<Claims, SecurityConstant.ClaimsBody, Long> getLongFromClaims = (claims, claimBody) ->
            UtilFunction.stringToLong.apply(claims.get(claimBody.getValue(), String.class));

    /**
     * Retrieve all claims from the provided JWT token.
     *
     * @param jwtToken {@link String} The JWT token from which to extract claims.
     * @return {@link Jws<Claims>} A Jws containing the claims extracted from the JWT token.
     */
    public static Jws<Claims> getAllClaimsFromToken(final String jwtToken) {
        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(jwtToken);
    }

    /**
     * Retrieve a JwtClaimDTO containing claims from the provided HttpServletRequest.
     *
     * @param req {@link HttpServletRequest} The HttpServletRequest from which to extract JWT claims.
     * @return {@link JwtClaimDTO} A JwtClaimDTO containing the extracted JWT claims.
     */
    public static JwtClaimDTO getClaimFromRequest(HttpServletRequest req) {
        return createJwtClaimDTO((Claims) req.getAttribute(SecurityConstant.CLAIMS));
    }

    /**
     * Retrieve a JwtClaimDTO containing claims from the provided WebRequest.
     *
     * @param req {@link WebRequest} The WebRequest from which to extract JWT claims.
     * @return {@link JwtClaimDTO} A JwtClaimDTO containing the extracted JWT claims.
     */
    public static JwtClaimDTO getClaimFromWebRequest(WebRequest req) {
        return createJwtClaimDTO((Claims) req.getAttribute(SecurityConstant.CLAIMS, REQUEST_SCOPE_FOR_WEB_REQUEST));
    }

    /**
     * Create a JwtClaimDTO from the provided JWT claims.
     *
     * @param claims {@link Claims} The JWT claims from which to create the JwtClaimDTO.
     * @return {@link JwtClaimDTO} A JwtClaimDTO containing the information extracted from the JWT claims.
     */
    private static JwtClaimDTO createJwtClaimDTO(Claims claims) {
        return Optional.ofNullable(claims)
                .map(reqClaims -> JwtClaimDTO.builder()
                    .claimId(getLongFromClaims.apply(reqClaims, SecurityConstant.ClaimsBody.CLAIM_ID))
                    .claimantId(getLongFromClaims.apply(reqClaims, SecurityConstant.ClaimsBody.CLAIMANT_ID))
                    .scope(reqClaims.get(SecurityConstant.ClaimsBody.SCOPES.getValue(), String.class))
                    .roleId(Long.valueOf(reqClaims.get(SecurityConstant.ClaimsBody.ROLE_ID.getValue(), Integer.class)))
                    .userId(getLongFromClaims.apply(reqClaims, SecurityConstant.ClaimsBody.USER_ID))
                    .build())
                .orElseThrow(() -> new NotFoundException("Claim details not found in the request", CLAIMANT_NOT_FOUND));
    }


    /**
     * Create a JSON Web Token (JWT) using the provided JwtClaimDTO and expiration time.
     *
     * @param claimDTO {@link JwtClaimDTO} The JwtClaimDTO containing the claim information for the JWT.
     * @param jwTExpiryInMin {@link int} The expiration time of the JWT in minutes.
     * @return {@link String} The generated JWT as a String.
     */
    public static String createJWT(JwtClaimDTO claimDTO, int jwTExpiryInMin) {
        final var jwtBuilder = Jwts.builder().setHeaderParam("typ", "JWT").setSubject("Default");
         if (null != claimDTO) {
             if (null != claimDTO.getUserId()) {
                 jwtBuilder.claim(SecurityConstant.ClaimsBody.USER_ID.getValue(), claimDTO.getUserId() + "");
             }
             if (null != claimDTO.getClaimId()) {
                 jwtBuilder.claim(SecurityConstant.ClaimsBody.CLAIM_ID.getValue(), claimDTO.getClaimId() + "");
             }
             if (null != claimDTO.getClaimantId()) {
                 jwtBuilder.claim(SecurityConstant.ClaimsBody.CLAIMANT_ID.getValue(), claimDTO.getClaimantId() + "");
             }
             if (null != claimDTO.getRoleId()) {
                 jwtBuilder.claim(SecurityConstant.ClaimsBody.ROLE_ID.getValue(), claimDTO.getRoleId());
             }
             if (null != claimDTO.getScope()) {
                 jwtBuilder.claim(SecurityConstant.ClaimsBody.SCOPES.getValue(), claimDTO.getScope());
             } else {
                 jwtBuilder.claim(SecurityConstant.ClaimsBody.SCOPES.getValue(), "default");
             }
         }
        return jwtBuilder.signWith(KEY).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ((long) jwTExpiryInMin * TO_MILL_SECONDS))).compact();
    }
}