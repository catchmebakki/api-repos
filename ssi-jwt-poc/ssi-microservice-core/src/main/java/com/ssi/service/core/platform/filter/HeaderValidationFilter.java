package com.ssi.service.core.platform.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.ssi.service.core.platform.exception.SSIExceptionManager;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class HeaderValidationFilter  implements Filter {

private static final Logger LOG = LoggerFactory.getLogger(HeaderValidationFilter.class);

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        // read the excude URL form property
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            List.of("/auth/", "/actuator", "/v3/api-docs", "/swagger")
                    .stream()
                    .filter(pathToExcude -> req.getRequestURI().startsWith(pathToExcude))
                    .findAny()
                    .orElseGet(() -> Optional.of(req.getHeader(HttpHeaders.CONTENT_TYPE))
                            .filter(contentType -> contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
                            .orElseThrow());
        } catch (Exception e) {
            throw ssiExceptionManager.createError(LOG, HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    "Error happened while validating header",
                    "Only support " + MediaType.APPLICATION_JSON_VALUE +  " " + HttpHeaders.CONTENT_TYPE);
        }
        chain.doFilter(request, response);
    }
}