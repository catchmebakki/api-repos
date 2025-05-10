package com.ssi.ms.platform.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssi.ms.common.service.NhuisLogService;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
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
import java.util.Objects;

@Slf4j
@Component
public class SSIDynamicExceptionManager {

    @Autowired
    private NhuisLogService logService;

    /**
     * Handle and log an SSIGeneralException, providing detailed information.
     *
     * @param logger {@link Logger} The logger instance for logging.
     * @param request {@link WebRequest} The WebRequest associated with the JWT token validation request.
     * @param status {@link HttpStatus} The HTTP status associated with the error.
     * @param messageToLog {@link String} The message to log for the error.
     * @param reason {@link String} The reason for the exception.
     * @param errorDetails {@link Map<String, List< DynamicErrorDTO >>} The map of error details.
     * @param exception {@link Throwable} The Throwable exception that triggered the error.
     * @return {@link SSIGeneralException} An SSIGeneralException instance with the provided information.
     */
    public SSIGeneralException onError(final Logger logger, final WebRequest request, final HttpStatus status, final String messageToLog,
                                       final String reason, final Map<String, List<DynamicErrorDTO>> errorDetails, final Throwable exception) {
        SSIGeneralException ssiEx = null;
        try {
            ssiEx = new SSIGeneralException(logger, status, reason, errorDetails, exception);
            final var jwtClaimDTO = JwtTokenUtil.getClaimFromWebRequest(request);
            final ObjectMapper objectMapper = new ObjectMapper();
            logger.error(
                    "LOGGER : {} ; CLAIM_BODY : {} ; HTTP_STATUS : {} ; ERROR_DETAIL : {} ; REASON : {} ; FIELD_ERRORS : {} ",
                    logger.getName(), jwtClaimDTO.toString(), status, messageToLog, reason,
                    null != errorDetails ? objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                            .writeValueAsString(errorDetails) : "");
            logger.error("Exception detail :", null != exception ? exception : ssiEx);
            logService.saveErrorToNhl(null != exception ? exception : ssiEx, jwtClaimDTO.getUserId(), jwtClaimDTO.getClaimId());
        } catch (Exception logException) {
            logger.error("Original Exception--->:", exception);
            logger.error("Exception while inserting error log in the table", logException);
        }
        return ssiEx;
    }

    /**
     * Custom exception class for representing general exceptions in the SSI application.
     * Extends ResponseStatusException for providing additional context and details in the exception.
     */
    public final class SSIGeneralException extends ResponseStatusException {
        private final Logger logger;
        private final Map<String, List<DynamicErrorDTO>> errorDetails;
        /**
         * Custom exception class for representing general exceptions in the SSI application.
         *
         * @param logger {@link Logger} The Logger instance for logging purposes.
         * @param status {@link HttpStatus} The HttpStatus associated with the exception.
         * @param reason {@link String} The reason for the exception.
         * @param errorDetails {@link Map<String, List<DynamicErrorDTO>>} The error details as a map of field names to error messages.
         * @param exception {@link Throwable} The underlying Throwable that caused this exception.
         */
        private SSIGeneralException(final Logger logger, final HttpStatus status, final String reason,
                                    final Map<String, List<DynamicErrorDTO>> errorDetails, final Throwable exception) {
            super(null != status ? status : HttpStatus.INTERNAL_SERVER_ERROR, reason, exception);
            this.logger = logger;
            this.errorDetails = Objects.requireNonNullElseGet(errorDetails, Map::of);
        }

        /**
         * Convert the object to its JSON representation.
         *
         * @return {@link String} The JSON representation of the object as a String.
         */
        public String toJson() {
            String jsonResponseString = null;
            try {
                final ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                final ObjectNode jsonResponse = objectMapper.createObjectNode();
                jsonResponse.put("timestamp", String.valueOf(LocalDateTime.now()));
                jsonResponse.put("status", this.getRawStatusCode());
                jsonResponse.put("reason", getReason());
                final ArrayNode errorDetailsArray = objectMapper.createArrayNode();
                if (null != this.errorDetails && !this.errorDetails.isEmpty()) {
                    this.errorDetails.forEach((key, value) -> {
                        final ObjectNode errorFieldObject = objectMapper.createObjectNode();
                        final ObjectNode errorObject = objectMapper.createObjectNode();
                        errorObject.put("errorField", key);
                        final ArrayNode errorArray = objectMapper.valueToTree(value);
                        errorObject.putArray("error").addAll(errorArray);
                        errorFieldObject.setAll(errorObject);
                        errorDetailsArray.add(errorFieldObject);
                    });
                } else {
                    final ObjectNode errorFieldObject = objectMapper.createObjectNode();
                    errorFieldObject.put("message", "internal.error");
                    errorDetailsArray.add(errorFieldObject);
                }
                jsonResponse.set("errorDetails", errorDetailsArray);
                jsonResponseString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonResponse);
            } catch (JsonProcessingException e) {
                jsonResponseString = "{\"message\": \"internal.error\"}";
                logger.error("Error while parsing the error details map in SSIDynamicExceptionManager.SSIGeneralException.toJson()", e);
            }
            return jsonResponseString;
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
