package com.mgu.analytics.adapter.search.elastic;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.mgu.analytics.util.CachingFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class ClientCachingFactory implements CachingFactory<ClientCacheKey, Client> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientCachingFactory.class);

    private final LoadingCache<ClientCacheKey, Client> cache;

    public ClientCachingFactory() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(10)
                .expireAfterAccess(60, TimeUnit.MINUTES)
                .refreshAfterWrite(60, TimeUnit.MINUTES)
                .build(this::createClient);
    }

    private Client createClient(final ClientCacheKey cacheKey) {
        Client client = null;
        try {
            final Settings settings = Settings
                    .settingsBuilder()
                    .put("client.transport.sniff", false)
                    .build();
            final InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(
                    InetAddress.getByAddress(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), 9300);
            client = TransportClient
                    .builder()
                    .settings(settings)
                    .build()
                    .addTransportAddress(transportAddress);
            LOGGER.debug("Created Elasticsearch client instance to 0.0.0.0:9300 using cache key {}", cacheKey);
            return client;
        } catch (UnknownHostException e) {
            LOGGER.warn("Unable to create Elasticsearch client due to malformed client configuration.", e);
        }
        return client;
    }

    @Override
    public Client get(final ClientCacheKey cacheKey) {
        return cache.get(cacheKey);
    }
}
