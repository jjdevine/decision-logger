package com.jonathandevinesoftware.decisionlogger.model;

import java.util.List;

public class Decision {

    private int id;

    private List<Tag> tags;

    private List<Person> decisionMakers;

    private String decisionText;

    public Decision(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Person> getDecisionMakers() {
        return decisionMakers;
    }

    public void setDecisionMakers(List<Person> decisionMakers) {
        this.decisionMakers = decisionMakers;
    }

    public String getDecisionText() {
        return decisionText;
    }

    public void setDecisionText(String decisionText) {
        this.decisionText = decisionText;
    }
}
