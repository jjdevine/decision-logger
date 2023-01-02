package com.jonathandevinesoftware.decisionlogger.gui.searchmeetings;

import com.jonathandevinesoftware.decisionlogger.core.Application;
import com.jonathandevinesoftware.decisionlogger.model.Meeting;
import com.jonathandevinesoftware.decisionlogger.model.MeetingDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceDataException;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceDataUtils;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SearchMeetingController {

    private SearchMeetingForm form;

    //TODO: meeting button behaviour when opened from a search

    public SearchMeetingController() {
        form = new SearchMeetingForm("Search Meetings");
        form.setSearchCallback(this::onSearch);
    }

    private void onSearch(SearchParameters searchParameters) {
        System.out.println("Searching...");
        System.out.println(searchParameters);
        List<Meeting> meetings = null;
        try {
            meetings = MeetingDAO.getInstance().queryMeetings(searchParameters);
            List<SearchMeetingResultViewModel> viewModels =
                    meetings.stream()
                            .map(m -> buildSearchMeetingResultViewModel(m, searchParameters))
                            .sorted(Comparator.comparing(SearchMeetingResultViewModel::getTimestamp).reversed())
                            .collect(Collectors.toList());
            form.setSearchResults(viewModels);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        meetings.forEach(m -> {
            System.out.println(m);
            System.out.println();
        });

        /*
                try {
            List<Decision> decisionList = DecisionDAO.getInstance().queryDecisions(decisionMakers, tags);
            List<SearchDecisionResultViewModel> viewModels =
                    decisionList.stream()
                            .map(d -> buildSearchResultViewModel(d, decisionMakers, tags))
                            .collect(Collectors.toList());
            searchDecisionForm.setSearchResults(viewModels);
        } catch (SQLException e) {
            Application.log(e);
        }
         */
    }

    private SearchMeetingResultViewModel buildSearchMeetingResultViewModel(
            Meeting m, SearchParameters searchParameters) {

        try {
            List<Person> attendees = ReferenceDataUtils.convertIdsToPersonList(m.getAttendees());
            List<String> attendeeNames = attendees.stream().map(Person::getValue).collect(Collectors.toList());

            List<Tag> tags = ReferenceDataUtils.convertIdsToTagList(m.getTags());
            List<String> tagNames = tags.stream().map(Tag::getValue).collect(Collectors.toList());

            List<Person> searchedAttendees = ReferenceDataUtils.convertIdsToPersonList(searchParameters.getAttendeeIds());
            List<String> searchedAttendeeNames = searchedAttendees.stream().map(Person::getValue).collect(Collectors.toList());

            List<Tag> searchedTags = ReferenceDataUtils.convertIdsToTagList(searchParameters.getTagIds());
            List<String> searchedTagNames = searchedTags.stream().map(Tag::getValue).collect(Collectors.toList());

            SearchMeetingResultViewModel viewModel = new SearchMeetingResultViewModel(m.getId())
                    .setTitle(m.getTitle())
                    .setAttendees(attendeeNames)
                    .setTags(tagNames)
                    .setNumberOfDecisions(m.getDecisions().size())
                    .setTimestamp(m.getTimestamp())
                    .setSearchedAttendees(searchedAttendeeNames) //TODO: fix this
                    .setSearchedTags(searchedTagNames);

            return viewModel;

        } catch (ReferenceDataException e) {
            Application.log(e);
            return null;
        }
    }
}
