package com.jonathandevinesoftware.decisionlogger.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Meeting {

    private UUID id;

    private LocalDateTime timestamp;

    private String title;

    private List<UUID> tags;

    private List<UUID> attendees;

    private List<UUID> decisions;

    public Meeting(UUID id) {
        this.id = id;
        timestamp = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        if(title == null) {
            return "";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UUID> getTags() {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags;
    }

    public void setTags(List<UUID> tags) {

        this.tags = tags;
    }

    public List<UUID> getAttendees() {
        if (attendees == null) {
            attendees = new ArrayList<>();
        }
        return attendees;
    }

    public void setAttendees(List<UUID> attendees) {
        this.attendees = attendees;
    }

    public List<UUID> getDecisions() {
        if (decisions == null) {
            decisions = new ArrayList<>();
        }
        return decisions;
    }

    public void setDecisions(List<UUID> decisions) {
        this.decisions = decisions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Meeting{");
        sb.append("id=").append(id);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", title='").append(title).append('\'');
        sb.append(", tags=").append(tags);
        sb.append(", attendees=").append(attendees);
        sb.append(", decisions=").append(decisions);
        sb.append('}');
        return sb.toString();
    }
}
