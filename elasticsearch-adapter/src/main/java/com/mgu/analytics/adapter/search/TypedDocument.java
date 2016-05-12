package com.mgu.analytics.adapter.search;

import com.fasterxml.jackson.annotation.JsonIgnore;

abstract public class TypedDocument extends Document {

    @JsonIgnore
    private String type;

    public TypedDocument(final String type) {
        super();
        this.type = type;
    }

    public TypedDocument(final String id, final String type) {
        super(id);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
