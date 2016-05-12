package com.mgu.analytics.adapter.search;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Index<T extends TypedDocument> {

    boolean index(T document);

    boolean index(List<T> documents);

    CompletableFuture<Boolean> indexAsync(T document);

    CompletableFuture<Boolean> indexAsync(List<T> documents);
}
