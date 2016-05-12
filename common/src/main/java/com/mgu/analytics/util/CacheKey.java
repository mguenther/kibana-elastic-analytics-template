package com.mgu.analytics.util;

/**
 * A {@code CacheKey} is an object that implements some concept of identity based on an implementation
 * of {@code CacheKey#hashCode} that may close over a set of instance attributes of the implementing class.
 * This interface makes the dependency between {@code hashCode} and {@code equals} abundantly clear,
 * since {@code equals} is also part of this interface.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface CacheKey {
    /**
     * @see {@code Object#equals(Object obj)}
     */
    boolean equals(Object obj);

    /**
     * @see {@code Object#hashCode()}
     */
    int hashCode();
}