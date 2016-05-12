package com.mgu.analytics.util;

import java.util.Objects;

/**
 * Represents an operation that accepts a single input argument and returns no result.
 * It is expected that a {@code ThrowingConsumer} operates via side-effects. Unlike
 * {@code Consumer}, {@code Exception}s it relays exceptions to the proper callsite.
 *
 * @param <T> the type of the input operation
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
@FunctionalInterface
public interface ThrowingConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t
     *      the input argument
     * @throws Exception
     *      if the operation throws an {@code Exception}
     */
    void accept(T t) throws Exception;

    /**
     * Returns a composed {@code ThrowingConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the composed
     * operation. If performing this operation throws an exception, the {@code after}
     * will not be executed.
     *
     * @param after
     *      the operation to perform after this operation
     * @throws NullPointerException
     *      if the operation {@code after} is {@code null}
     * @throws Exception
     *      if either operation throws an exception
     * @return
     *      composed {@code ThrowingConsumer} that performs in sequence this operation
     *      followed by the {@code after} operation
     */
    default ThrowingConsumer<T> chain(final ThrowingConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }
}
