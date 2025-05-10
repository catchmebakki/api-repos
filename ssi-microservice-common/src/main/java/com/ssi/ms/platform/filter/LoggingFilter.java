package com.ssi.ms.platform.filter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * @author munirathnam.surepall
 * LoggingFilter provides services to logging filters.
 */
@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

	/**
	 * Perform the filter's processing on the provided HttpServletRequest and HttpServletResponse.
	 *
	 * @param request {@link HttpServletRequest} The HttpServletRequest to be filtered.
	 * @param response {@link HttpServletResponse} The HttpServletResponse to be filtered.
	 * @param filterChain {@link FilterChain} The FilterChain to continue the filter chain.
	 * @throws ServletException If a servlet-specific error occurs during the filtering process.
	 * @throws IOException If an I/O error occurs during the filtering process.
	 */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
       final ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

        final ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

       final long startTime = System.currentTimeMillis();
        filterChain.doFilter(request, responseWrapper);
       final long timeTaken = System.currentTimeMillis() - startTime;


      final String requestBody = getStringValue(requestWrapper.getContentAsByteArray(),
                request.getCharacterEncoding());
       final String responseBody = getStringValue(responseWrapper.getContentAsByteArray(),
                response.getCharacterEncoding());

        log.info(
                "FINISHED PROCESSING : METHOD={}; REQUESTURI={}; REQUEST={}; RESPONSE CODE={}; RESPONSE={}; TIME TAKEN={}",
                request.getMethod(), request.getRequestURI(), requestBody, response.getStatus(), responseBody,
                timeTaken);
        responseWrapper.copyBodyToResponse();
    }

   /* private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        return requestBody.toString();
    }*/

    /**
     * Convert a byte array to a string using the specified character encoding.
     *
     * @param contentAsByteArray {@link byte[]} The byte array to be converted.
     * @param characterEncoding {@link String} The character encoding to use for the conversion.
     * @return {@link String} The string representation of the byte array using the specified character encoding.
     */
    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        String result = "";
        try {
            result = new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}