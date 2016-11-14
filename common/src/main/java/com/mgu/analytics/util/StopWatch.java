package com.mgu.analytics.util;

import java.time.Duration;

public class StopWatch {

    private final long startedInNs;

    private StopWatch(final long startedInNs) {
        this.startedInNs = startedInNs;
    }

    public Duration time() {
        return Duration.ofNanos(System.nanoTime() - startedInNs);
    }

    public static StopWatch start() {
        return new StopWatch(System.nanoTime());
    }
}
