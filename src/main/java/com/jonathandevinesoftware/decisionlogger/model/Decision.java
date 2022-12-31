package com.jonathandevinesoftware.decisionlogger.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Decision {

    private UUID id;

    private List<UUID> decisionMakers = new ArrayList<>();

    private List<UUID> tags = new ArrayList<>();

    private String decisionText;

    private LocalDateTime timestamp;

    private Optional<UUID> linkedMeeting = Optional.empty();

    public Decision(UUID id) {
        this.id = id;
        this.timestamp = LocalDateTime.now();
        this.decisionText = "";
    }

    public UUID getId() {
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Optional<UUID> getLinkedMeeting() {
        return linkedMeeting;
    }

    public void setLinkedMeeting(Optional<UUID> linkedMeeting) {
        this.linkedMeeting = linkedMeeting;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Decision{");
        sb.append("id=").append(id);
        sb.append(", decisionMakers=").append(decisionMakers);
        sb.append(", tags=").append(tags);
        sb.append(", decisionText='").append(decisionText).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", linkedMeeting=").append(linkedMeeting);
        sb.append('}');
        return sb.toString();
    }
}
