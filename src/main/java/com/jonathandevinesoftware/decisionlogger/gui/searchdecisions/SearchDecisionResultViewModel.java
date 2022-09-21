package com.jonathandevinesoftware.decisionlogger.gui.searchdecisions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SearchDecisionResultViewModel {

    private UUID decisionId;
    private String decisionText;
    private LocalDateTime decisionDateTime;
    private List<String> decisionMakers;
    private List<String> tags;
    private String linkedMeetingTitle;
    private Optional<UUID> linkedMeetingId;

    public UUID getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(UUID decisionId) {
        this.decisionId = decisionId;
    }

    public String getDecisionText() {
        return decisionText;
    }

    public void setDecisionText(String decisionText) {
        this.decisionText = decisionText;
    }

    public LocalDateTime getDecisionDateTime() {
        return decisionDateTime;
    }

    public void setDecisionDateTime(LocalDateTime decisionDateTime) {
        this.decisionDateTime = decisionDateTime;
    }

    public List<String> getDecisionMakers() {
        return decisionMakers;
    }

    public void setDecisionMakers(List<String> decisionMakers) {
        this.decisionMakers = decisionMakers;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getLinkedMeetingTitle() {
        return linkedMeetingTitle;
    }

    public void setLinkedMeetingTitle(String linkedMeetingTitle) {
        this.linkedMeetingTitle = linkedMeetingTitle;
    }

    public Optional<UUID> getLinkedMeetingId() {
        return linkedMeetingId;
    }

    public void setLinkedMeetingId(Optional<UUID> linkedMeetingId) {
        this.linkedMeetingId = linkedMeetingId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchDecisionResultViewModel{");
        sb.append("decisionId=").append(decisionId);
        sb.append(", decisionText='").append(decisionText).append('\'');
        sb.append(", decisionDateTime=").append(decisionDateTime);
        sb.append(", decisionMakers=").append(decisionMakers);
        sb.append(", tags=").append(tags);
        sb.append(", linkedMeetingTitle='").append(linkedMeetingTitle).append('\'');
        sb.append(", linkedMeetingId=").append(linkedMeetingId);
        sb.append('}');
        return sb.toString();
    }
}
