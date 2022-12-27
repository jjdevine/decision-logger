package com.jonathandevinesoftware.decisionlogger.gui.searchmeetings;

public class SearchMeetingController {

    private SearchMeetingForm form;

    public SearchMeetingController() {
        form = new SearchMeetingForm("Search Meetings");
        form.setSearchCallback(this::onSearch);
    }

    private void onSearch(SearchParameters searchParameters) {
        //TODO - implement this
    }
}
