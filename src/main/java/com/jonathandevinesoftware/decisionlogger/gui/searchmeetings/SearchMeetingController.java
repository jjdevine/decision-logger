package com.jonathandevinesoftware.decisionlogger.gui.searchmeetings;

import com.jonathandevinesoftware.decisionlogger.model.Meeting;
import com.jonathandevinesoftware.decisionlogger.model.MeetingDAO;

import java.sql.SQLException;

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
        try {
            MeetingDAO.getInstance().withTest2(searchParameters);
            //MeetingDAO.getInstance().queryMeetings(searchParameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
