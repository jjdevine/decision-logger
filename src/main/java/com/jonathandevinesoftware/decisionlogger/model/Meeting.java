package com.jonathandevinesoftware.decisionlogger.model;

import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Meeting {

    private UUID id;


    private List<Person> attendees;

    private List<Decision> decisions;

    public Meeting(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }


    public List<Person> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Person> attendees) {
        this.attendees = attendees;
    }

    public List<Decision> getDecisions() {
        if(decisions == null) {
            decisions = new ArrayList<>();
        }
        return decisions;
    }

    public void setDecisions(List<Decision> decisions) {
        this.decisions = decisions;
    }
}
