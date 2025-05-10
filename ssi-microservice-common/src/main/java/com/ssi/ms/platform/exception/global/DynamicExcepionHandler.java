package com.ssi.ms.platform.exception.global;

import com.ssi.ms.platform.exception.SSIDynamicExceptionManager;
import com.ssi.ms.platform.exception.custom.DynamicValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Praveenraja Paramsivam
 * CustomExcepionHandler provides services to global exception handler
 ************************************************************************************************
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
@Order(Ordered.HIGHEST_PRECEDENCE)
//SS230332 End
public class DynamicExcepionHandler {
    @Autowired
    private SSIDynamicExceptionManager ssiExceptionManager;

    /**
     * This method, Exception handler for handling DynamicValidationException and generating an appropriate response.
     * @param exception {@link DynamicValidationException} The DynamicValidationException to be handled.
     * @param request {@link WebRequest} The WebRequest associated with the exception.
     * @return {@link ResponseEntity<String>} A ResponseEntity containing the error response as the response body.
     */
    @ExceptionHandler(DynamicValidationException.class)
    public ResponseEntity<String> handleNotFoundException(DynamicValidationException exception, final WebRequest request) {
        SSIDynamicExceptionManager.SSIGeneralException resStatusException;
        resStatusException = ssiExceptionManager.onError(log, request, HttpStatus.BAD_REQUEST,
                "Global exception handler for the error " + exception.getMessage(),
                "validation.error", exception.getErrorDetails(), exception);
        return resStatusException.toJsonResponseEntity();
    }
}
