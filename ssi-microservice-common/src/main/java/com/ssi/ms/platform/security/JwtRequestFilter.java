package com.ssi.ms.platform.security;

import com.ssi.ms.constant.SecurityConstant;
import com.ssi.ms.platform.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Praveenraja Paramsivam
 * JwtRequestFilter provides services to Custom filter for handling JWT authentication in each HTTP request.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    /**
     * Perform the filter's processing on the provided HttpServletRequest and HttpServletResponse.
     *
     * @param request {@link HttpServletRequest} The HttpServletRequest to be filtered.
     * @param response {@link HttpServletResponse} The HttpServletResponse to be filtered.
     * @param chain {@link FilterChain} The FilterChain to continue the filter chain.
     * @throws ServletException If a servlet-specific error occurs during the filtering process.
     * @throws IOException If an I/O error occurs during the filtering process.
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final int bearerLength = 7;

        String jwtToken = null;
// JWT Token is in the form "Bearer token". Remove Bearer word and get
// only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith(TOKEN_PREFIX)) {
            jwtToken = requestTokenHeader.substring(bearerLength);
            try {
                if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    final Jws<Claims> jwtClaims = JwtTokenUtil.getAllClaimsFromToken(jwtToken);
                    final Authentication auth = new UsernamePasswordAuthenticationToken(
                            jwtClaims.getBody().getSubject(),
                            jwtToken,
                            List.of(new SimpleGrantedAuthority(jwtClaims.getBody()
                                    .get(SecurityConstant.ClaimsBody.SCOPES.getValue(), String.class))));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    request.setAttribute(SecurityConstant.CLAIMS, jwtClaims.getBody());
                }
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token, " + e.getMessage(), e);
                throw e;
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired," + e.getMessage(), e);
                throw e;
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        chain.doFilter(request, response);
    }

}