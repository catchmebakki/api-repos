package com.ssi.ms.platform.exception.resilience4j;

import com.ssi.ms.platform.exception.SSIExceptionManager;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.util.function.Predicate;

/**
 * @author munirathnam.surepall
 * RecordFailurePredicate provides services to Throwable should be handled based on its type.
 */
public class RecordFailurePredicate implements Predicate<Throwable> {

	/**
	 * Test if the provided Throwable matches certain criteria.
	 *
	 * @param throwable {@link Throwable} The Throwable to be tested.
	 * @return {@link boolean} True if the Throwable matches the criteria, false otherwise.
	 */
    @Override
    public boolean test(final Throwable throwable) {
        return !(throwable instanceof MethodArgumentNotValidException
                || throwable instanceof ConstraintViolationException
                || throwable instanceof HttpRequestMethodNotSupportedException
                || throwable instanceof SSIExceptionManager.SSIGeneralException
                || throwable instanceof CallNotPermittedException);
    }
}