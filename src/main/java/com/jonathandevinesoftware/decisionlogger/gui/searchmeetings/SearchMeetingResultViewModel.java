package com.jonathandevinesoftware.decisionlogger.gui.searchmeetings;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SearchMeetingResultViewModel {
    private UUID meetingId;
    private String title;
    private LocalDateTime timestamp;
    private int numberOfDecisions;
    private List<String> attendees;
    private List<String> tags;
    private List<String> searchedAttendees;
    private List<String> searchedTags;

    public SearchMeetingResultViewModel(UUID meetingId) {
        this.meetingId = meetingId;
    }

    public UUID getMeetingId() {
        return meetingId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public SearchMeetingResultViewModel setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public int getNumberOfDecisions() {
        return numberOfDecisions;
    }

    public SearchMeetingResultViewModel setNumberOfDecisions(int numberOfDecisions) {
        this.numberOfDecisions = numberOfDecisions;
        return this;
    }

    public List<String> getAttendees() {
        return attendees;
    }

    public SearchMeetingResultViewModel setAttendees(List<String> attendees) {
        this.attendees = attendees;
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public SearchMeetingResultViewModel setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public List<String> getSearchedAttendees() {
        return searchedAttendees;
    }

    public SearchMeetingResultViewModel setSearchedAttendees(List<String> searchedAttendees) {
        this.searchedAttendees = searchedAttendees;
        return this;
    }

    public List<String> getSearchedTags() {
        return searchedTags;
    }

    public SearchMeetingResultViewModel setSearchedTags(List<String> searchedTags) {
        this.searchedTags = searchedTags;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SearchMeetingResultViewModel setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchMeetingResultViewModel{");
        sb.append("meetingId=").append(meetingId);
        sb.append(", title='").append(title).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", numberOfDecisions=").append(numberOfDecisions);
        sb.append(", attendees=").append(attendees);
        sb.append(", tags=").append(tags);
        sb.append(", searchedAttendees=").append(searchedAttendees);
        sb.append(", searchedTags=").append(searchedTags);
        sb.append('}');
        return sb.toString();
    }
}
