package com.jonathandevinesoftware.decisionlogger.gui.searchmeetings;

import java.util.List;
import java.util.UUID;

public class SearchParameters {

    private List<UUID> attendeeIds;
    private List<UUID> TagIds;
    private List<UUID> DecisionMakerIds;

    public List<UUID> getAttendeeIds() {
        return attendeeIds;
    }

    public void setAttendeeIds(List<UUID> attendeeIds) {
        this.attendeeIds = attendeeIds;
    }

    public List<UUID> getTagIds() {
        return TagIds;
    }

    public void setTagIds(List<UUID> tagIds) {
        TagIds = tagIds;
    }

    public List<UUID> getDecisionMakerIds() {
        return DecisionMakerIds;
    }

    public void setDecisionMakerIds(List<UUID> decisionMakerIds) {
        DecisionMakerIds = decisionMakerIds;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchParameters{");
        sb.append("attendeeIds=").append(attendeeIds);
        sb.append(", TagIds=").append(TagIds);
        sb.append(", DecisionMakerIds=").append(DecisionMakerIds);
        sb.append('}');
        return sb.toString();
    }
}
