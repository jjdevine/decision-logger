package com.jonathandevinesoftware.decisionlogger.gui.searchmeetings;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class SearchMeetingResultPanel {

    public static JPanel buildSearchDecisionResultPanel(SearchMeetingResultViewModel viewModel) {
        //TODO - this method
        return null;

    }

    public class SearchMeetingResultViewModel {
        private UUID meetingId;
        private LocalDate timestamp;
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

        public LocalDate getTimestamp() {
            return timestamp;
        }

        public SearchMeetingResultViewModel setTimestamp(LocalDate timestamp) {
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
    }



}
