package com.mgu.analytics.feeder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mgu.analytics.adapter.search.TypedDocument;

public class TimeSeriesItem extends TypedDocument {

    private static final String DOCUMENT_TYPE = "time-series-item";

    @JsonProperty("date")
    private final String date;

    @JsonProperty("count")
    private final int count;

    public TimeSeriesItem(final String date, final int count) {
        super(DOCUMENT_TYPE);
        this.date = date;
        this.count = count;
    }
}
