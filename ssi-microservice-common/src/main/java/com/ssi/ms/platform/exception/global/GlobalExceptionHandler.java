package com.ssi.ms.platform.exception.global;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.ssi.ms.platform.exception.SSIExceptionManager;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * @author Praveenraja Paramsivam
 * GlobalExceptionHandler provides services to global exception handler.
 * ************************************************************************************************
 *                      Modification Log                                                        *
 ************************************************************************************************
 *
 *  Date            Developer           Defect  	Description of Change
 *  ----------      ---------           ------  	---------------------
 * 02/14/2024		Seetha S			SS230332 -UD-240214-Unable to manually add Mass Layoff claimants to MSL
 ************************************************************************************************
 */
@Slf4j
@ControllerAdvice
//SS230332 Start
@Order(Ordered.LOWEST_PRECEDENCE)
//SS230332 End
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    /**
     * This method, Exception handler for methodArgumentNotValidException and generating an appropriate response.
     * @param exception {@link MethodArgumentNotValidException} The MethodArgumentNotValidException to be handled.
     * @param request {@link WebRequest} The WebRequest associated with the exception.
     * @return {@link ResponseEntity<String>} A ResponseEntity containing the error response as the response body.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> methodArgumentNotValidException(final MethodArgumentNotValidException exception,
                                                                  final WebRequest request) {
        final var mapOfError = exception.getBindingResult().getFieldErrors().stream()
                .collect(toMap(fieldError -> fieldError.getField(),
                        fieldError -> List.of(fieldError.getDefaultMessage()),
                        (first, second) -> Stream.concat(first.stream(), second.stream()).toList()));

        final var resStatusException = ssiExceptionManager
                .onError(LOG, request, HttpStatus.BAD_REQUEST, "MethodArgumentNotValidException logged from Global Handler",
                        "Validation Error", mapOfError, exception);

        return resStatusException.toJsonResponseEntity();
    }

    /**
     * This method, Exception handler for constraintViolationException and generating an appropriate response.
     * @param exception {@link ConstraintViolationException} The ConstraintViolationException to be handled.
     * @param request {@link WebRequest} The WebRequest associated with the exception.
     * @return {@link ResponseEntity<String>} A ResponseEntity containing the error response as the response body.
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> constraintViolationException(final ConstraintViolationException exception,
                                                               final WebRequest request) {
        final var mapOfErrorMessage = Optional.ofNullable(exception.getConstraintViolations())
                .map(constraintViolationSet -> constraintViolationSet.stream()
                        .collect(toMap(constraintViolation -> constraintViolation.getPropertyPath().toString(),
                                constrain -> List.of(constrain.getMessage()),
                                (first, second) -> Stream.concat(first.stream(), second.stream()).toList())))
                .orElseGet(() -> Map.of("Error", List.of(exception.getMessage())));
        final var resStatusException = ssiExceptionManager
                .onError(LOG, request, HttpStatus.BAD_REQUEST, "ConstraintViolationException logged from Global Handler",
                        "validation.error", mapOfErrorMessage, exception);

        return resStatusException.toJsonResponseEntity();
    }

    /**
     * This method, Exception handler for handleNotFoundException and generating an appropriate response.
     * @param exception {@link EmptyResultDataAccessException} The EmptyResultDataAccessException to be handled.
     * @param request {@link WebRequest} The WebRequest associated with the exception.
     * @return {@link ResponseEntity<String>} A ResponseEntity containing the error response as the response body.
     */
    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<String> handleNotFoundException(EmptyResultDataAccessException exception, final WebRequest request) {
        SSIExceptionManager.SSIGeneralException resStatusException;
        resStatusException = ssiExceptionManager.onError(log, request, HttpStatus.NOT_FOUND,
                "Global exception handler for the error " + exception.getMessage(),
                "not.found", exception);
        return resStatusException.toJsonResponseEntity();
    }
    /**
     * This method, Exception handler for handleNotFoundException and generating an appropriate response.
     * @param exception {@link DataAccessException} The DataAccessException to be handled.
     * @param request {@link WebRequest} The WebRequest associated with the exception.
     * @return {@link ResponseEntity<String>} A ResponseEntity containing the error response as the response body.
     */
    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<String> handleNotFoundException(DataAccessException exception, final WebRequest request) {
        SSIExceptionManager.SSIGeneralException resStatusException;
        resStatusException = ssiExceptionManager.onError(log, request, HttpStatus.NOT_FOUND,
                "Global exception handler for the error " + exception.getMessage(),
                "db.resource.not.available", exception);
        return resStatusException.toJsonResponseEntity();
    }


    /**
     * This method, Exception handler for resourceNotFoundException and generating an appropriate response.
     * @param exception {@link Exception} The Exception to be handled.
     * @param request {@link WebRequest} The WebRequest associated with the exception.
     * @return {@link ResponseEntity<String>} A ResponseEntity containing the error response as the response body.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> resourceNotFoundException(final Exception exception, final WebRequest request) {
        SSIExceptionManager.SSIGeneralException resStatusException;
        String messageStr;
        if (exception instanceof HttpRequestMethodNotSupportedException notSupportedException) {
            resStatusException = ssiExceptionManager.onError(LOG, request, HttpStatus.METHOD_NOT_ALLOWED,
                    "Generic error Handled - Requested HTTP method not supported" + notSupportedException.getMethod() + " not supported. Try for "
                            + notSupportedException.getSupportedHttpMethods(),
                    "method.not.supported", exception);
        } else if (exception instanceof HttpMessageNotReadableException notSupportedException) {
            if (notSupportedException.getCause() instanceof JsonMappingException jsonMappingExcep) {
                messageStr = jsonMappingExcep.getPath().stream()
                        .map(path -> path.getFieldName())
                        .collect(joining("."));
            } else {
                messageStr = notSupportedException.getMessage() + " not supported. Try for "
                        + notSupportedException.getHttpInputMessage();
            }
            resStatusException = ssiExceptionManager.onError(LOG, request, HttpStatus.BAD_REQUEST,
                    "Generic error Handled - Not able to read the HTTP Message", "parser.error",
                    Map.of(messageStr, List.of("Parser.error")), exception);
        } else {
            resStatusException = ssiExceptionManager.onError(LOG, request, HttpStatus.INTERNAL_SERVER_ERROR,
                    "Generic error Handled at global level", "internal.error", exception);
        }
        return resStatusException.toJsonResponseEntity();
    }
}
