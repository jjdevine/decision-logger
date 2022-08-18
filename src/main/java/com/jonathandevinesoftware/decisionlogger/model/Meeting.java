package com.jonathandevinesoftware.decisionlogger.model;

import java.util.List;

public class Meeting {

    private int id;

    private List<Tag> tags;

    private List<Person> attendees;

    private List<Decision> decisions;

    public int getId() {
        return id;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Person> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Person> attendees) {
        this.attendees = attendees;
    }

    public List<Decision> getDecisions() {
        return decisions;
    }

    public void setDecisions(List<Decision> decisions) {
        this.decisions = decisions;
    }
}
