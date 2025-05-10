package com.ssi.service.core.platform.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

@Slf4j
@Component
public class SSIExceptionManager {

    public SSIGeneralException onError(final Logger logger, final HttpStatus status, final String messageToLog,
                                       final String reason, final Throwable e) {
        logger.error(status +  messageToLog, e);
        return new SSIGeneralException(logger, status, reason, Map.of(), e);
    }

    public SSIGeneralException onError(final Logger logger, final HttpStatus status, final String messageToLog,
                                       final String reason, final Map<String, String> errorDetails, final Throwable e) {
        logger.error(status +  messageToLog, e);
        return new SSIGeneralException(logger, status, reason, errorDetails, e);
    }

    public SSIGeneralException onError(final Logger logger, final String messageToLog, final String reason,
                                       final Throwable e) {
        logger.error(messageToLog + logger.getName(), e);
        return new SSIGeneralException(logger, reason, e);
    }

    public SSIGeneralException createError(final Logger logger, final HttpStatus status, final String messageToLog,
                                           final String reason) {
        var ssiException = new SSIGeneralException(logger, status, reason);
        logger.error(status +  messageToLog, ssiException);
        return ssiException;
    }

    public SSIGeneralException createError(final Logger logger, final HttpStatus status, final String messageToLog,
                                           final String reason, final Map<String, String> errorDetails) {
        var ssiException = new SSIGeneralException(logger, status, reason, errorDetails);
        logger.error(status +  messageToLog, ssiException);
        return ssiException;
    }

    public SSIGeneralException createError(final Logger logger) {
        var ssiException = new SSIGeneralException(logger, HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        logger.error("Error in " + logger.getName(), ssiException);
        return ssiException;
    }

    public final class SSIGeneralException extends ResponseStatusException {
        private final Logger logger;
        private final ServletRequest servletRequest;
        private final Map<String, String> errorDetails;

        private SSIGeneralException(final Logger logger, final HttpStatus status, final String reason) {
            this(logger, status, reason, Map.of());
        }

        private SSIGeneralException(final Logger logger, final String reason, final Throwable ex) {
            this(logger, HttpStatus.INTERNAL_SERVER_ERROR, reason, ex);
        }

        private SSIGeneralException(final Logger logger, final HttpStatus status, final String reason, final Throwable ex) {
            this(logger, status, reason, Map.of(), ex);
        }

        private SSIGeneralException(final Logger logger, final HttpStatus status, final String reason,
                                    final Map<String, String> errorDetails) {
            this(logger, status, reason, errorDetails, null);
        }

        private SSIGeneralException(final Logger logger, final HttpStatus status, final String reason,
                                    final Map<String, String> errorDetails, final Throwable e) {
            this(logger, status, reason, errorDetails, null, e);
        }

        private SSIGeneralException(final Logger logger, final HttpStatus status, final String reason,
                                    final Map<String, String> errorDetails, final ServletRequest servletRequest,
                                    final Throwable e) {
            super(status, reason, e);
            this.logger = logger;
            this.servletRequest = servletRequest;
            this.errorDetails = errorDetails;
        }

        public String toJson() {
            return Optional.of(new StringJoiner(", ", "{", "}"))
                    .map(strJoiner -> strJoiner.add("\"timestamp\": \"" + LocalDateTime.now() + "\"")
                            .add("\"status\": " + this.getStatus())
                            .add("\"reason\": \"" + getReason() + "\""))
                    .map(strJoiner -> {
                            try {
                                strJoiner = null != this.errorDetails && !this.errorDetails.isEmpty()
                                    ? strJoiner.add(new ObjectMapper().writeValueAsString(this.errorDetails))
                                    : strJoiner;
                            } catch (Exception e) {
                                log.error("Error while parsing the error details map in SSIGeneralException.toJson()", e);
                            }
                            return strJoiner;
                        })
                    //.map(strJoiner -> strJoiner.add("\"path\": \"" + context.get + "\""))  need to get the request path
                    .map(strJoiner -> strJoiner.toString())
                    .orElseGet(() -> "{\"message\"}: \"Internal Error\"");
         }

         public ResponseEntity<String> toJsonResponseEntity() {
            return ResponseEntity.status(this.getStatus())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(toJson());
         }
    }
}
