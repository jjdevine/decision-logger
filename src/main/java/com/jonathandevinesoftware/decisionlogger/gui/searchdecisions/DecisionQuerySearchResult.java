package com.jonathandevinesoftware.decisionlogger.gui.searchdecisions;

import com.jonathandevinesoftware.decisionlogger.model.Decision;

import java.util.UUID;

public class DecisionQuerySearchResult extends Decision {

    private String meetingTitle;

    public DecisionQuerySearchResult(UUID id) {
        super(id);
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DecisionQuerySearchResult{");
        sb.append("meetingTitle='").append(meetingTitle).append('\'');
        sb.append('}');
        sb.append(super.toString());
        return sb.toString();
    }
}
