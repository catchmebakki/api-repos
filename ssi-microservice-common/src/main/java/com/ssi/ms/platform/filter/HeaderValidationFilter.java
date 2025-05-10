package com.ssi.ms.platform.filter;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * @author munirathnam.surepall
 * HeaderValidationFilter provides service to header validation filters.
 * */
@Slf4j
@Component
public class HeaderValidationFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(HeaderValidationFilter.class);

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    private final BiPredicate<HttpServletRequest, String> uriAndMethodPredicate = (req, pathToExcude) ->
            req.getRequestURI().contains(pathToExcude)
                    || !"PUT".equalsIgnoreCase(req.getMethod()) && !"POST".equalsIgnoreCase(req.getMethod());

    /**
     * Perform the filter's processing on the provided request and response.
     *
     * @param request {@link } The ServletRequest to be filtered.
     * @param response {@link} The ServletResponse to be filtered.
     * @param chain {@link} The FilterChain to continue the filter chain.
     * @throws IOException If an I/O error occurs during the filtering process.
     * @throws ServletException If a servlet-specific error occurs during the filtering process.
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        // read the excude URL form property
        try {
          final HttpServletRequest req = (HttpServletRequest) request;
            List.of("/auth/", "/actuator", "/v3/api-docs", "/swagger")
                    .stream()
                    .filter(pathToExcude -> uriAndMethodPredicate.test(req, pathToExcude))
                    .findAny()
                    .orElseGet(() ->
                            Optional.of(req.getHeader(HttpHeaders.CONTENT_TYPE))
                            .filter(contentType -> contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)
                                || contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE))
                            .orElseThrow());
        } catch (Exception e) {
            throw ssiExceptionManager.onError(LOG, HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    "Error happened while validating header",
                    "Only support " + MediaType.APPLICATION_JSON_VALUE + " " + HttpHeaders.CONTENT_TYPE, null);
        }
        chain.doFilter(request, response);
    }
}