package com.ssi.ms.platform.handler;


import com.ssi.ms.common.dto.JwtClaimDTO;
import com.ssi.ms.constant.SecurityConstant;
import com.ssi.ms.platform.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static com.ssi.ms.constant.SecurityConstant.TokenType.ACCESS_TOKEN;
import static com.ssi.ms.constant.SecurityConstant.TokenType.REFRESH_TOKEN;

/**
 * @author Praveenraja Paramsivam
 * JWTHandler provides services to JWT claim data, jwt token and token type.
 */
@Component
public class JWTHandler {


    private final int tempMaxTokentime = 365 * 24 * 60;
    @Value("${application.expiry.in-minutes.access-token:10}")
    private int accessTokenExpiry;
    @Value("${application.expiry.in-minutes.refresh-token:45}")
    private int refreshTokenExpiry;
    @Value("${application.expiry.in-minutes.exchange-token:2}")
    private int exchangeToken;
    private final String devHeaderName = "Environment";

    private final Predicate<HttpServletRequest> isDevHeaderPresent = request -> Optional.ofNullable(request)
            .map(req -> req.getHeader(devHeaderName) != null
                    && req.getHeader(devHeaderName).equalsIgnoreCase("dev"))
            .orElseGet(() -> false);

    public String getJWT(final HttpServletRequest request) {
        return getJWT(null, request);
    }

    /**
     * Generate a JWT (JSON Web Token) using the provided JwtClaimDTO and HttpServletRequest.
     *
     * @param claimDTO {@link JwtClaimDTO} The JwtClaimDTO containing the claim information for the JWT.
     * @param request  {@link HttpServletRequest} The HttpServletRequest associated with the JWT generation.
     * @return {@link String} The generated JWT as a String.
     */
    public String getJWT(JwtClaimDTO claimDTO, final HttpServletRequest request) {
        return JwtTokenUtil.createJWT(claimDTO, isDevHeaderPresent.test(request) ? tempMaxTokentime : exchangeToken);
    }

    /**
     * Generate access and refresh JWTs (JSON Web Tokens) based on the provided HttpServletRequest.
     *
     * @param request            {@link HttpServletRequest} The HttpServletRequest associated with the JWT generation.
     * @param createRefreshToken {@link boolean} Flag indicating whether to create a refresh token or not.
     * @return {@link Map<String, String>} A Map containing the generated access and refresh JWTs as key-value pairs.
     */
    public Map<String, String> getAccessAndRefreshJWTs(HttpServletRequest request, boolean createRefreshToken) {
        final var claimDto = JwtTokenUtil.getClaimFromRequest(request);

        return createRefreshToken ? Map.of(ACCESS_TOKEN.getValue(), getJWT(claimDto, ACCESS_TOKEN),
                REFRESH_TOKEN.getValue(), getJWT(claimDto, REFRESH_TOKEN))
                : Map.of(ACCESS_TOKEN.getValue(), getJWT(claimDto, ACCESS_TOKEN));
    }

    /**
     * Generate a JWT (JSON Web Token) using the provided JwtClaimDTO and token type.
     *
     * @param jwtClaimDTO {@link JwtClaimDTO} The JwtClaimDTO containing the claim information for the JWT.
     * @param tokenType   {@link SecurityConstant.TokenType} The token type (e.g., access token, refresh token) for which the JWT is generated.
     * @return {@link String} The generated JWT as a String.
     */
    private String getJWT(final JwtClaimDTO jwtClaimDTO, SecurityConstant.TokenType tokenType) {
        return JwtTokenUtil.createJWT(jwtClaimDTO, ACCESS_TOKEN.name().equalsIgnoreCase(tokenType.name())
                ? accessTokenExpiry : refreshTokenExpiry);
    }
}
