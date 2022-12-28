package com.jonathandevinesoftware.decisionlogger.gui.searchmeetings;

import com.jonathandevinesoftware.decisionlogger.model.Meeting;
import com.jonathandevinesoftware.decisionlogger.model.MeetingDAO;

import java.sql.SQLException;
import java.util.List;

public class SearchMeetingController {

    private SearchMeetingForm form;

    public SearchMeetingController() {
        form = new SearchMeetingForm("Search Meetings");
        form.setSearchCallback(this::onSearch);
    }

    private void onSearch(SearchParameters searchParameters) {
        //TODO - implement this
        System.out.println("Searching...");
        System.out.println(searchParameters);
        List<Meeting> meetings = null;
        try {
            meetings = MeetingDAO.getInstance().queryMeetings(searchParameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        meetings.forEach(m -> {
            System.out.println(m);
            System.out.println();
        });
    }
}
