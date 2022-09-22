package com.jonathandevinesoftware.decisionlogger.gui.searchdecisions;

import com.jonathandevinesoftware.decisionlogger.core.Application;
import com.jonathandevinesoftware.decisionlogger.model.Decision;
import com.jonathandevinesoftware.decisionlogger.model.DecisionDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.PersonDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.TagDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SearchDecisionController {

    private SearchDecisionForm searchDecisionForm;

    public SearchDecisionController() {
        searchDecisionForm = new SearchDecisionForm();
        searchDecisionForm.setSearchCallback(this::onSearch);
    }

    private void onSearch(List<UUID> decisionMakers, List<UUID> tags) {
        try {
            List<Decision> decisionList = DecisionDAO.getInstance().queryDecisions(decisionMakers, tags);
            decisionList.forEach(d -> searchDecisionForm.addSearchResult(buildSearchResultViewModel(d)));
        } catch (SQLException e) {
            Application.log(e);
        }
    }

    private SearchDecisionResultViewModel buildSearchResultViewModel(Decision decision) {
        SearchDecisionResultViewModel viewModel = new SearchDecisionResultViewModel();
        viewModel.setDecisionId(decision.getId());
        viewModel.setDecisionDateTime(decision.getTimestamp());
        viewModel.setDecisionText(decision.getDecisionText());
        viewModel.setLinkedMeetingId(decision.getLinkedMeeting());

        PersonDAO personDAO;
        try {
            personDAO = PersonDAO.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<String> decisionMakers = new ArrayList<>();
        viewModel.setDecisionMakers(decisionMakers);
        for(UUID decisionMakerId: decision.getDecisionMakers()) {
            System.out.println("looking up person with id " + decisionMakerId);
            personDAO.getPersonWithId(decisionMakerId)
                    .ifPresent(person -> decisionMakers.add(person.getValue()));
        }

        TagDAO tagDAO;
        try {
            tagDAO = TagDAO.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<String> tags = new ArrayList<>();
        viewModel.setTags(tags);
        for(UUID tagId: decision.getTags()) {
            tagDAO.getTagWithId(tagId)
                    .ifPresent(tag -> tags.add(tag.getValue()));
        }

        System.out.println(viewModel);
        return viewModel;
    }
}
