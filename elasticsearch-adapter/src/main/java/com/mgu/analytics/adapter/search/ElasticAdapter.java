package com.mgu.analytics.adapter.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgu.analytics.util.StopWatch;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ElasticAdapter<T extends TypedDocument> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticAdapter.class);

    private final ObjectMapper mapper;

    private final ClientCachingFactory factory;

    private final ElasticIndexConfig config;

    @Inject
    public ElasticAdapter(final ClientCachingFactory factory, final ElasticIndexConfig config) {
        this.factory = factory;
        this.config = config;
        this.mapper = new ObjectMapper();
        LOGGER.info("Adapter is configured to use index '{}'.", config.getIndexName());
    }

    private void createIndexIfNonExisting() {
        final ClientCacheKey cacheKey = ClientCacheKey.of(config.getIndexName());
        final Client client = factory.get(cacheKey);
        if (!indexExists()) {
            LOGGER.info("The index '{}' does not exist. Creating it.");
            client.admin().indices().prepareCreate(config.getIndexName()).get();
            LOGGER.info("Index '{}' created.");
        }
    }

    private boolean indexExists() {
        final ClientCacheKey cacheKey = ClientCacheKey.of(config.getIndexName());
        final Client client = factory.get(cacheKey);
        return client.admin().indices().prepareExists(config.getIndexName()).get().isExists();
    }

    public boolean index(final T document) {
        return index(Collections.singletonList(document));
    }

    public boolean index(final List<T> documents) {
        final StopWatch stopWatch = StopWatch.start();
        final ClientCacheKey cacheKey = ClientCacheKey.of(config.getIndexName());
        final Client client = factory.get(cacheKey);
        boolean result = true;
        try {
            createIndexIfNonExisting();
            final BulkRequestBuilder builder = client.prepareBulk();
            for (T document : documents) {
                builder.add(toIndexRequestBuilder(client, document));
            }
            builder.execute().get();
            LOGGER.info("Successfully indexed batch of {} documents.", documents.size());
        } catch (Throwable t) {
            LOGGER.warn("Caught an unexpected exception while indexing documents.", t);
            result = false;
        }
        LOGGER.debug("Indexing {} documents took {} ms.", documents.size(), stopWatch.time().toMillis());
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

    public CompletableFuture<Boolean> indexAsync(final T document) {
        return CompletableFuture.supplyAsync(() -> index(document));
    }

    public CompletableFuture<Boolean> indexAsync(final List<T> documents) {
        return CompletableFuture.supplyAsync(() -> index(documents));
    }
}
