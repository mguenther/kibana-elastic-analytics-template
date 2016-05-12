package com.mgu.analytics.feeder;

import java.util.Properties;

public class FeederConfig {

    private final String indexName;

    public FeederConfig(final String configFile) {
        try {
            final Properties properties = new Properties();
            properties.load(FeederConfig.class.getClassLoader().getResourceAsStream(configFile));
            this.indexName = properties.getProperty("elastic.indexname");
        } catch (Throwable t) {
            throw new RuntimeException("Unable to load configuration from file " + configFile + ".", t);
        }
    }

    public String getIndexName() {
        return indexName;
    }
}
