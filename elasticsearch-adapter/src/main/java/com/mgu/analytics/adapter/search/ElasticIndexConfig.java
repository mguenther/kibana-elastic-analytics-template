package com.mgu.analytics.adapter.search;

public class ElasticIndexConfig {

    private final String indexName;

    public ElasticIndexConfig(final String indexName) {
        this.indexName = indexName;
    }

    public String getIndexName() {
        return indexName;
    }
}
