package com.jonathandevinesoftware.decisionlogger.gui.searchmeetings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SearchParameters {

    private List<UUID> attendeeIds;
    private List<UUID> tagIds;
    private List<UUID> decisionMakerIds;

    public List<UUID> getAttendeeIds() {
        if(attendeeIds == null) {
            attendeeIds = new ArrayList<>();
        }
        return attendeeIds;
    }

    public void setAttendeeIds(List<UUID> attendeeIds) {
        this.attendeeIds = attendeeIds;
    }

    public List<UUID> getTagIds() {
        if(tagIds == null) {
            tagIds = new ArrayList<>();
        }
        return tagIds;
    }

    public void setTagIds(List<UUID> tagIds) {
        this.tagIds = tagIds;
    }

    public List<UUID> getDecisionMakerIds() {
        if(decisionMakerIds == null) {
            decisionMakerIds = new ArrayList<>();
        }
        return decisionMakerIds;
    }

    public void setDecisionMakerIds(List<UUID> decisionMakerIds) {
        this.decisionMakerIds = decisionMakerIds;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchParameters{");
        sb.append("attendeeIds=").append(attendeeIds);
        sb.append(", TagIds=").append(tagIds);
        sb.append(", DecisionMakerIds=").append(decisionMakerIds);
        sb.append('}');
        return sb.toString();
    }
}
