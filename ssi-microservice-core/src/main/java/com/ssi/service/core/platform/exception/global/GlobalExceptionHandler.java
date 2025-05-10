package com.ssi.service.core.platform.exception.global;


import com.ssi.service.core.platform.exception.SSIExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> methodArgumentNotValidException(final MethodArgumentNotValidException ex,
                                                                  final WebRequest request) {
        var mapOfError = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(fieldError -> fieldError.getObjectName() + "." + fieldError.getField(),
                        DefaultMessageSourceResolvable::getDefaultMessage));

        var resStatusException =  ssiExceptionManager
                .onError(log, HttpStatus.BAD_REQUEST, "MethodArgumentNotValidException logged from Global Handler",
                        "Validation Error", mapOfError, ex);

        return resStatusException.toJsonResponseEntity();
    }
    //
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> constraintViolationException(final ConstraintViolationException ex,
                                                               final WebRequest request) {
        var mapOfErrorMessage = Optional.ofNullable(ex.getConstraintViolations())
                .map(constraintViolationSet -> constraintViolationSet.stream()
                        .collect(Collectors.toMap(constraintViolation ->
                                    constraintViolation.getPropertyPath().toString(),
                                ConstraintViolation::getMessage)))
                        .orElseGet(() -> Map.of("Error", ex.getMessage()));

        log.error("validationException logged from Global Handler", ex);
        var resStatusException =  ssiExceptionManager
                .onError(log, HttpStatus.BAD_REQUEST, "ConstraintViolationException logged from Global Handler",
                        "Validation Error", mapOfErrorMessage, ex);

        return resStatusException.toJsonResponseEntity();
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> resourceNotFoundException(final Exception ex, final WebRequest request) {
        log.error("Error logged in Global Handler SSIExceptionManager", ex);
        SSIExceptionManager.SSIGeneralException resStatusException;
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            HttpRequestMethodNotSupportedException notSupportedException = (HttpRequestMethodNotSupportedException) ex;
            resStatusException = ssiExceptionManager.onError(log, HttpStatus.METHOD_NOT_ALLOWED,
                    "Generic error Handled - Requested HTTP method not supported",
                    notSupportedException.getMethod() + " not supported. Try for "
                            + notSupportedException.getSupportedHttpMethods(), ex);
        } else {
            resStatusException = ssiExceptionManager.onError(log, HttpStatus.INTERNAL_SERVER_ERROR,
                    "Generic error Handled at global level", "Internal Error", ex);
        }
        return resStatusException.toJsonResponseEntity();
    }


}
