package io.grant.core.retry.predicate;

import io.grant.core.retry.attempt.Attempt;

import java.util.function.Predicate;

/**
 * Created on 2018/2/11
 *
 * @author lowzj
 */
public class ExceptionPredicate<V> implements Predicate<Attempt<V>> {
    private Predicate<Throwable> delegate;

    public ExceptionPredicate(Predicate<Throwable> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean test(Attempt<V> attempt) {
        return attempt.hasException() && delegate.test(attempt.getExceptionCause());
    }
}
