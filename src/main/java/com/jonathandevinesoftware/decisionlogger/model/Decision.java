package com.jonathandevinesoftware.decisionlogger.model;

import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;

import java.util.List;
import java.util.UUID;

public class Decision {

    private int id;

    private List<UUID> decisionMakers;

    private List<UUID> tags;

    private String decisionText;

    public Decision(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<UUID> getDecisionMakers() {
        return decisionMakers;
    }

    public void setDecisionMakers(List<UUID> decisionMakers) {
        this.decisionMakers = decisionMakers;
    }

    public List<UUID> getTags() {
        return tags;
    }

    public void setTags(List<UUID> tags) {
        this.tags = tags;
    }

    public String getDecisionText() {
        return decisionText;
    }

    public void setDecisionText(String decisionText) {
        this.decisionText = decisionText;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Decision{");
        sb.append("id=").append(id);
        sb.append(", decisionMakers=").append(decisionMakers);
        sb.append(", tags=").append(tags);
        sb.append(", decisionText='").append(decisionText).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
