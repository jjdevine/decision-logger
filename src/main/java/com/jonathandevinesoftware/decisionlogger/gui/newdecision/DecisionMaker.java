package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import java.util.UUID;

public class DecisionMaker {

    private UUID id;

    private String name;

    public DecisionMaker(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DecisionMaker{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}