package io.grant.core.retry.predicate;

import io.grant.core.retry.attempt.Attempt;

import java.util.function.Predicate;

/**
 * Created on 2018/2/11
 *
 * @author lowzj
 */
public class ExceptionClassPredicate<V> implements Predicate<Attempt<V>> {
    private Class<? extends Throwable> exceptionClass;

    public ExceptionClassPredicate(Class<? extends Throwable> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    @Override
    public boolean test(Attempt<V> attempt) {
        return attempt.hasException() && exceptionClass.isAssignableFrom(attempt.getExceptionCause().getClass());
    }
}
