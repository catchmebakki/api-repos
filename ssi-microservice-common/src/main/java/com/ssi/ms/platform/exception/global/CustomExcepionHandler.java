package com.ssi.ms.platform.exception.global;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.FileProcessingException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class CustomExcepionHandler {

    @Autowired
    private SSIExceptionManager ssiExceptionManager;

    /**
     * This function, Exception handler for handling NotFoundException and generating an appropriate response.
     * @param exception {@link NotFoundException} The NotFoundException to be handled.
     * @param request {@link WebRequest} The WebRequest associated with the exception.
     * @return {@link ResponseEntity<String>} A ResponseEntity containing the error response as the response body.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException exception, final WebRequest request) {
        SSIExceptionManager.SSIGeneralException resStatusException;
        resStatusException = ssiExceptionManager.onError(log, request, HttpStatus.NOT_FOUND,
                "Global exception handler for the error " + exception.getMessage(),
                StringUtils.isNotBlank(exception.getErrorCode()) ? exception.getErrorCode() : "not.fount", exception);
        return resStatusException.toJsonResponseEntity();
    }

    /**
     * This method, Exception handler for handling CustomValidationException and generating an appropriate response.
     * @param exception {@link CustomValidationException} The CustomValidationException to be handled.
     * @param request {@link WebRequest} The WebRequest associated with the exception.
     * @return {@link ResponseEntity<String>} A ResponseEntity containing the error response as the response body.
     */
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<String> handleNotFoundException(CustomValidationException exception, final WebRequest request) {
        SSIExceptionManager.SSIGeneralException resStatusException;
        resStatusException = ssiExceptionManager.onError(log, request, HttpStatus.BAD_REQUEST,
                "Global exception handler for the error " + exception.getMessage(),
                "validation.error", exception.getErrorDetails(), exception);
        return resStatusException.toJsonResponseEntity();
    }
    /**
     * Handle the exception of type FileProcessingException.
     *
     * @param exception {@link FileProcessingException} The FileProcessingException to be handled.
     * @param request {@link WebRequest} The WebRequest associated with the exception.
     * @return {@link ResponseEntity<String>} A ResponseEntity containing the error response as the response body.
     */
    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<String> handleNotFoundException(FileProcessingException exception, final WebRequest request) {
    	SSIExceptionManager.SSIGeneralException resStatusException;
        resStatusException = ssiExceptionManager.onError(log, request, HttpStatus.NOT_FOUND,
                "Global exception handler for the error " + exception.getMessage(),
               "file.processing.failed", exception);
        return resStatusException.toJsonResponseEntity();
    }
}
