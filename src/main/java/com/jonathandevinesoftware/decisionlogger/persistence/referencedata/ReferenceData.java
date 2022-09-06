package com.jonathandevinesoftware.decisionlogger.persistence.referencedata;

import java.util.UUID;

public abstract class ReferenceData {

    protected UUID id;

    protected String value;

    public ReferenceData(UUID id, String value) {
        this.id = id;
        this.value = value;
    }

    public UUID getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ReferenceData{");
        sb.append("id=").append(id);
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
