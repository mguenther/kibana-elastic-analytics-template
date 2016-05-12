package com.mgu.analytics.adapter.search.elastic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgu.analytics.adapter.search.Index;
import com.mgu.analytics.adapter.search.TypedDocument;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ElasticAdapter<T extends TypedDocument> implements Index<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticAdapter.class);

    private final ObjectMapper mapper;

    private final ClientCachingFactory factory;

    private final ElasticIndexConfig config;

    @Inject
    public ElasticAdapter(final ClientCachingFactory factory, final ElasticIndexConfig config) {
        this.factory = factory;
        this.config = config;
        this.mapper = new ObjectMapper();
    }

    @Override
    public boolean index(final T document) {
        final ClientCacheKey cacheKey = ClientCacheKey.of(config.getIndexName());
        boolean result = true;
        try {
            final Client client = factory.get(cacheKey);
            toIndexRequestBuilder(client, document).execute();
            LOGGER.info("Successfully indexed document with ID {}.", document.getId());
        } catch (Throwable t) {
            result = false;
        }
        return result;
    }

    @Override
    public boolean index(final List<T> documents) {
        final ClientCacheKey cacheKey = ClientCacheKey.of(config.getIndexName());
        boolean result = true;
        try {
            final Client client = factory.get(cacheKey);
            final BulkRequestBuilder builder = client.prepareBulk();
            for (T document : documents) {
                builder.add(toIndexRequestBuilder(client, document));
            }
            builder.execute();
            LOGGER.info("Successfully indexed batch of {} documents.", documents.size());
        } catch (Throwable t) {
            result = false;
        }
        return result;
    }

    private IndexRequestBuilder toIndexRequestBuilder(final Client client, final T document) throws JsonProcessingException {
        final String json = mapper.writeValueAsString(document);
        final IndexRequestBuilder indexBuilder = client
                .prepareIndex(config.getIndexName(), document.getType(), document.getId())
                .setContentType(XContentType.JSON)
                .setSource(json);
        return indexBuilder;
    }

    @Override
    public CompletableFuture<Boolean> indexAsync(final T document) {
        return CompletableFuture.supplyAsync(() -> index(document));
    }

    @Override
    public CompletableFuture<Boolean> indexAsync(final List<T> documents) {
        return CompletableFuture.supplyAsync(() -> index(documents));
    }
}
