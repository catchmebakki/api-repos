package com.ssi.ms.platform.exception;


import com.ssi.ms.common.service.NhuisLogService;
import com.ssi.ms.platform.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.ssi.ms.platform.util.UtilFunction.stringToLong;

/**
 * @author Praveenraja Paramsivam
 * SSIExceptionManager provides services to handle SSIGeneralException.
 */
@Slf4j
@Component
public class SSIExceptionManager {

    @Autowired
    private NhuisLogService logService;

    /**
     * Handle and log an SSIGeneralException, providing detailed information.
     *
     * @param logger {@link Logger} The logger instance for logging.
     * @param status {@link HttpStatus} The HTTP status associated with the error.
     * @param messageToLog {@link String} The message to log for the error.
     * @param reason {@link String} The reason for the exception.
     * @param errorDetails {@link Map<String, List<String>>} The map of error details.
     * @param exception {@link Throwable} The Throwable exception that triggered the error.
     * @return {@link SSIGeneralException} An SSIGeneralException instance with the provided information.
     */
    public SSIGeneralException onError(final Logger logger, final HttpStatus status, final String messageToLog,
                                       final String reason, final Throwable exception) {
        return onError(logger, status, messageToLog, reason, Map.of(), exception);
    }

    /**
     * Handle and log an SSIGeneralException, providing detailed information.
     *
     * @param logger {@link Logger} The logger instance for logging.
     * @param status {@link HttpStatus} The HTTP status associated with the error.
     * @param messageToLog {@link String} The message to log for the error.
     * @param reason {@link String} The reason for the exception.
     * @param errorDetails {@link Map<String, List<String>>} The map of error details.
     * @param exception {@link Throwable} The Throwable exception that triggered the error.
     * @return {@link SSIGeneralException} An SSIGeneralException instance with the provided information.
     */
    public SSIGeneralException onError(final Logger logger, final WebRequest request, final HttpStatus status, final String messageToLog,
                                       final String reason, final Throwable exception) {
        return onError(logger, request, status, messageToLog, reason, Map.of(), exception);
    }

    /**
     * Handle and log an SSIGeneralException, providing detailed information.
     *
     * @param logger {@link Logger} The logger instance for logging.
     * @param status {@link HttpStatus} The HTTP status associated with the error.
     * @param messageToLog {@link String} The message to log for the error.
     * @param reason {@link String} The reason for the exception.
     * @param errorDetails {@link Map<String, List<String>>} The map of error details.
     * @param exception {@link Throwable} The Throwable exception that triggered the error.
     * @return {@link SSIGeneralException} An SSIGeneralException instance with the provided information.
     */
    public SSIGeneralException onError(final Logger logger, final HttpStatus status, final String messageToLog,
                                       final String reason, final Map<String, List<String>> errorDetails, final Throwable exception) {
        return onError(logger, "", status, messageToLog, reason, errorDetails, exception);
    }

    /**
     * Handle and log an SSIGeneralException, providing detailed information.
     *
     * @param logger {@link Logger} The logger instance for logging.
     * @param request {@link WebRequest} The WebRequest associated with the JWT token validation request.
     * @param status {@link HttpStatus} The HTTP status associated with the error.
     * @param messageToLog {@link String} The message to log for the error.
     * @param reason {@link String} The reason for the exception.
     * @param errorDetails {@link Map<String, List<String>>} The map of error details.
     * @param exception {@link Throwable} The Throwable exception that triggered the error.
     * @return {@link SSIGeneralException} An SSIGeneralException instance with the provided information.
     */
    public SSIGeneralException onError(final Logger logger, final WebRequest request, final HttpStatus status, final String messageToLog,
                                       final String reason, final Map<String, List<String>> errorDetails, final Throwable exception) {
        SSIGeneralException ssiEx = null;
        try {
            ssiEx = new SSIGeneralException(logger, status, reason, errorDetails, exception);
            final var jwtClaimDTO = JwtTokenUtil.getClaimFromWebRequest(request);
            logger.error(
                    "LOGGER : {} ; CLAIM_BODY : {} ; HTTP_STATUS : {} ; ERROR_DETAIL : {} ; REASON : {} ; FIELD_ERRORS : {} ",
                    logger.getName(), jwtClaimDTO.toString(), status, messageToLog, reason,
                    null != errorDetails ? errorDetails.toString() : "");
            logger.error("Exception detail :", null != exception ? exception : ssiEx);
            logService.saveErrorToNhl(null != exception ? exception : ssiEx, jwtClaimDTO.getUserId(), jwtClaimDTO.getClaimId());
        } catch (Exception logException) {
            logger.error("Original Exception--->:", exception);
            logger.error("Exception while inserting error log in the table", logException);
        }
        return ssiEx;
    }
    /**
     * Handle and log an SSIGeneralException, providing detailed information.
     *
     * @param logger {@link Logger} The logger instance for logging.
     * @param userId {@link String} The ID of the user associated with the error.
     * @param status {@link HttpStatus} The HTTP status associated with the error.
     * @param messageToLog {@link String} The message to log for the error.
     * @param reason {@link String} The reason for the exception.
     * @param errorDetails {@link Map<String, List<String>>} The map of error details.
     * @param exception {@link Throwable} The Throwable exception that triggered the error.
     * @return {@link SSIGeneralException} An SSIGeneralException instance with the provided information.
     */
    public SSIGeneralException onError(final Logger logger, final String userId, final HttpStatus status, final String messageToLog,
                                       final String reason, final Map<String, List<String>> errorDetails, final Throwable exception) {
        SSIGeneralException ssiEx = null;
        try {
            ssiEx = new SSIGeneralException(logger, status, reason, errorDetails, exception);
            logger.error(
                    "LOGGER : {} ; USER_ID : {} ; HTTP_STATUS : {} ; ERROR_DETAIL : {} ; REASON : {} ; FIELD_ERRORS : {} ",
                    logger.getName(), userId, status, messageToLog, reason,
                    null != errorDetails ? errorDetails.toString() : "");
            logger.error("Exception detail :", null != exception ? exception : ssiEx);
            logService.saveErrorToNhl(null != exception ? exception : ssiEx, stringToLong.apply(userId), null);
        } catch (Exception exception1) {
            logger.error("Exception while inserting error log in the table", exception1);
        }
        return ssiEx;
    }


    /**
     * Custom exception class for representing general exceptions in the SSI application.
     * Extends ResponseStatusException for providing additional context and details in the exception.
     */
    public final class SSIGeneralException extends ResponseStatusException {
        private final Logger logger;
        private final Map<String, List<String>> errorDetails;
        /**
         * Custom exception class for representing general exceptions in the SSI application.
         *
         * @param logger {@link Logger} The Logger instance for logging purposes.
         * @param status {@link HttpStatus} The HttpStatus associated with the exception.
         * @param reason {@link String} The reason for the exception.
         * @param errorDetails {@link Map<String, List<String>>} The error details as a map of field names to error messages.
         * @param exception {@link Throwable} The underlying Throwable that caused this exception.
         */
        private SSIGeneralException(final Logger logger, final HttpStatus status, final String reason,
                                    final Map<String, List<String>> errorDetails, final Throwable exception) {
            super(null != status ? status : HttpStatus.INTERNAL_SERVER_ERROR, reason, exception);
            this.logger = logger;
            if (null != errorDetails) {
                this.errorDetails = errorDetails;
            } else {
                this.errorDetails = Map.of();
            }
        }

        /**
         * Convert the object to its JSON representation.
         *
         * @return {@link String} The JSON representation of the object as a String.
         */
        public String toJson() {
            return Optional.of(new StringJoiner(", ", "{", "}"))
                    .map(strJoiner -> strJoiner.add("\"timestamp\": \"" + LocalDateTime.now() + "\"")
                            .add("\"status\": " + this.getRawStatusCode())
                            .add("\"reason\": \"" + getReason() + "\""))
                    .map(strJoiner -> {
                        try {
                            if (null != this.errorDetails && !this.errorDetails.isEmpty()) {
                                strJoiner = strJoiner.add("\"errorDetails\": [{"
                                        + this.errorDetails.entrySet().stream()
                                        .map(entry -> "\"errorField\": \"" + entry.getKey()
                                                + "\", \"errorCode\": [\"" + entry.getValue().stream()
                                                .collect(Collectors.joining("\", \"")) + "\"]")
                                        .collect(Collectors.joining("},{ ")) + "}]");

                            }
                        } catch (Exception e) {
                            logger.error("Error while parsing the error details map in SSIGeneralException.toJson()", e);
                        }
                        return strJoiner;
                    })
                    .map(strJoiner -> strJoiner.toString())
                    .orElseGet(() -> "{\"message\"}: \"internal.error\"");
        }

        /**
         * Convert the response content to a JSON format and wrap it in a ResponseEntity.
         *
         * @return {@link ResponseEntity<String>} A ResponseEntity containing the response content in JSON format.
         */
        public ResponseEntity<String> toJsonResponseEntity() {
            return ResponseEntity.status(this.getStatus())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(toJson());
        }
    }
}
