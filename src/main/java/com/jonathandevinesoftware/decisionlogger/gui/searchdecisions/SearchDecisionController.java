package com.jonathandevinesoftware.decisionlogger.gui.searchdecisions;

import com.jonathandevinesoftware.decisionlogger.core.Application;
import com.jonathandevinesoftware.decisionlogger.gui.decision.DecisionEditorController;
import com.jonathandevinesoftware.decisionlogger.gui.meeting.MeetingEditorController;
import com.jonathandevinesoftware.decisionlogger.model.Decision;
import com.jonathandevinesoftware.decisionlogger.model.DecisionDAO;
import com.jonathandevinesoftware.decisionlogger.model.Meeting;
import com.jonathandevinesoftware.decisionlogger.model.MeetingDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.PersonDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceDataException;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceDataUtils;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.TagDAO;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SearchDecisionController {

    private SearchDecisionForm searchDecisionForm;

    public SearchDecisionController() {
        searchDecisionForm = new SearchDecisionForm();
        searchDecisionForm.setSearchCallback(this::onSearch);
        searchDecisionForm.setOpenDecisionConsumer(this::openDecision);
        searchDecisionForm.setOpenMeetingConsumer(this::openMeeting);
    }

    private void onSearch(List<UUID> decisionMakers, List<UUID> tags) {
        try {
            List<DecisionQuerySearchResult> decisionList = DecisionDAO.getInstance().queryDecisions(decisionMakers, tags);

            List<SearchDecisionResultViewModel> viewModels = new ArrayList<>();
            boolean firstException = true;
            for(Decision decision: decisionList) {
                SearchDecisionResultViewModel viewModel = null;
                try {
                    viewModel = buildSearchResultViewModel(decision, decisionMakers, tags);
                } catch (ReferenceDataException e) {
                    if(firstException) {
                        firstException = false;
                        Application.log(e);
                        JOptionPane.showMessageDialog(searchDecisionForm,
                                "Unable to show all search results: " + e.getMessage());
                    }
                }
                viewModels.add(viewModel);
            }

            searchDecisionForm.setSearchResults(viewModels);
        } catch (SQLException e) {
            Application.log(e);
        }
    }

    private SearchDecisionResultViewModel buildSearchResultViewModel(
            Decision decision,
            List<UUID> decisionMakerSearchIds,
            List<UUID> tagSearchIds) throws ReferenceDataException {

        SearchDecisionResultViewModel viewModel = new SearchDecisionResultViewModel();
        viewModel.setDecisionId(decision.getId());
        viewModel.setDecisionDateTime(decision.getTimestamp());
        viewModel.setDecisionText(decision.getDecisionText());
        viewModel.setLinkedMeetingId(decision.getLinkedMeeting());

        List<Person> searchedDecisionMakers = ReferenceDataUtils.convertIdsToPersonList(decisionMakerSearchIds);
        List<String> searchedDecisionMakerNames = searchedDecisionMakers.stream().map(Person::getValue).collect(Collectors.toList());
        viewModel.setDecisionMakerSearchTerms(searchedDecisionMakerNames);

        List<Person> decisionMakers = ReferenceDataUtils.convertIdsToPersonList(decision.getDecisionMakers());
        List<String> decisionMakerNames = decisionMakers.stream().map(Person::getValue).collect(Collectors.toList());
        viewModel.setDecisionMakers(decisionMakerNames);

        List<Tag> searchedTags = ReferenceDataUtils.convertIdsToTagList(tagSearchIds);
        List<String> searchedTagNames = searchedTags.stream().map(Tag::getValue).collect(Collectors.toList());
        viewModel.setTagSearchTerms(searchedTagNames);

        List<Tag> tags = ReferenceDataUtils.convertIdsToTagList(decision.getTags());
        List<String> tagNames = tags.stream().map(Tag::getValue).collect(Collectors.toList());
        viewModel.setTags(tagNames);

        return viewModel;
    }

    private void openDecision(UUID decisionId) {
        System.out.println("Open decision " + decisionId);
        try {
            Decision decision = DecisionDAO.getInstance().loadDecision(decisionId);
            new DecisionEditorController(decision).setOpenMainMenuOnClose(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void openMeeting(UUID meetingId) {
        System.out.println("Open meeting " + meetingId);
        try {
            Meeting meeting = MeetingDAO.getInstance().loadMeeting(meetingId);
            new MeetingEditorController(meeting);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
