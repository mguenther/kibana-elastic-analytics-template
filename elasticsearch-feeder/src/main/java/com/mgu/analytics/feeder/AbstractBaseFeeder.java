package com.mgu.analytics.feeder;

import com.mgu.analytics.adapter.search.Index;
import com.mgu.analytics.adapter.search.TypedDocument;
import com.mgu.analytics.adapter.search.elastic.ClientCachingFactory;
import com.mgu.analytics.adapter.search.elastic.ElasticAdapter;
import com.mgu.analytics.adapter.search.elastic.ElasticIndexConfig;
import com.mgu.analytics.util.ThrowingConsumer;

import java.util.concurrent.CountDownLatch;

abstract public class AbstractBaseFeeder<T extends TypedDocument> {

    private final FeederConfig config;

    public AbstractBaseFeeder() {
        config = new FeederConfig("config.properties");
    }

    protected void withFeeder(final ThrowingConsumer<Index<T>> acceptor) {
        final ElasticIndexConfig indexConfig = new ElasticIndexConfig(config.getIndexName());
        final ElasticAdapter<T> adapter = new ElasticAdapter<>(new ClientCachingFactory(), indexConfig);
        executeAsynchronously(acceptor, adapter);
    }

    private <R> void executeAsynchronously(final ThrowingConsumer<R> acceptor, R context) {
        final CountDownLatch barrier = new CountDownLatch(1);
        try {
            final Runnable asyncExec = wrap(acceptor, context, barrier);
            asyncExec.run();
            barrier.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("Received an interrupt while executing the provided feeding logic.", e);
        }
    }

    private <R> Runnable wrap(final ThrowingConsumer<R> acceptor, final R context, final CountDownLatch barrier) {
        return () -> {
            try {
                acceptor.accept(context);
            } catch (Throwable t) {
                throw new RuntimeException("An error occurred while executing the provided feeding logic.", t);
            } finally {
                barrier.countDown();
            }
        };
    }

    abstract public void run();
}
