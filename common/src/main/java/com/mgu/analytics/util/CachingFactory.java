package com.mgu.analytics.util;

/**
 * Provides the means to get a cached instance of type {@code CachedInstance} that is identified
 * by a {@code CacheKey}.
 *
 * @param <CacheIdentity>
 *     parameterized type with type restriction on {@code CacheKey}
 * @param <CachedInstance>
 *     type of the cached instance
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface CachingFactory<CacheIdentity extends CacheKey, CachedInstance> {

    /**
     * Yields an instance of {@code CachedInstance} that is identified by the given {@code CacheIdentity} on
     * a cache-hit. On a cache-miss, the requested instance is constructed, stored in the cache and
     * finally returned to the caller.
     *
     * @param cacheKey
     *      identifies an instance of type {@code CachedInstance}
     * @return
     *      instance of type {@code CachedInstance}
     */
    CachedInstance get(CacheIdentity cacheKey);
}
