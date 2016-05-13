package com.mgu.analytics.adapter.search;

import com.mgu.analytics.util.CacheKey;

public class ClientCacheKey implements CacheKey {

    private final String identity;

    private ClientCacheKey(final String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientCacheKey that = (ClientCacheKey) o;

        return !(identity != null ? !identity.equals(that.identity) : that.identity != null);

    }

    @Override
    public int hashCode() {
        return identity != null ? identity.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClientCacheKey{" +
                "identity='" + identity + '\'' +
                '}';
    }

    public static ClientCacheKey of(final String identity) {
        return new ClientCacheKey(identity);
    }
}
