package com.mgu.analytics.adapter.search;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

abstract public class Document {

    @JsonIgnore
    private final String id;

    public Document() {
        this(UUID.randomUUID().toString());
    }

    public Document(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document that = (Document) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
