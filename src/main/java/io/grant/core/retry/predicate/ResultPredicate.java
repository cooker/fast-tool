package io.grant.core.retry.predicate;

import io.grant.core.retry.attempt.Attempt;

import java.util.function.Predicate;

/**
 * Created on 2018/2/11
 *
 * @author lowzj
 */
public class ResultPredicate<V> implements Predicate<Attempt<V>> {

    private Predicate<V> delegate;

    public ResultPredicate(Predicate<V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean test(Attempt<V> attempt) {
        if (!attempt.hasResult()) {
            return false;
        }
        V result = attempt.getResult();
        return delegate.test(result);
    }
}
