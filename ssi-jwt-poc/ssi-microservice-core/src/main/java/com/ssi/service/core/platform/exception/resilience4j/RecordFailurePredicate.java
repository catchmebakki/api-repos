package com.ssi.service.core.platform.exception.resilience4j;

import com.ssi.service.core.platform.exception.SSIExceptionManager;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.util.function.Predicate;

public class RecordFailurePredicate implements Predicate<Throwable> {

    @Override
    public boolean test(final Throwable throwable) {
        return !(throwable instanceof MethodArgumentNotValidException
            || throwable instanceof ConstraintViolationException
            || throwable instanceof HttpRequestMethodNotSupportedException
            || throwable instanceof SSIExceptionManager.SSIGeneralException
            || throwable instanceof CallNotPermittedException);
    }
}