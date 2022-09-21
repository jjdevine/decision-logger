package com.jonathandevinesoftware.decisionlogger.gui.searchdecisions;

import com.jonathandevinesoftware.decisionlogger.model.Decision;
import com.jonathandevinesoftware.decisionlogger.model.DecisionDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.PersonDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class SearchDecisionController {

    private SearchDecisionForm searchDecisionForm;

    public SearchDecisionController() {
        searchDecisionForm = new SearchDecisionForm();
        searchDecisionForm.setSearchCallback(this::onSearch);
    }

    private void onSearch(List<UUID> decisionMakers, List<UUID> tags) {
        System.out.println("Decision Makers:");
        decisionMakers.forEach(System.out::println);
        System.out.println("Tags:");
        tags.forEach(System.out::println);

        System.out.println("search results...");
        try {
            List<Decision> decisionList = DecisionDAO.getInstance().queryDecisions(decisionMakers, tags);
            decisionList.forEach(System.out::println);
            decisionList.forEach(d -> searchDecisionForm.addSearchResult());
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private SearchDecisionResultViewModel buildSearchResultViewModel(Decision decision) {
        SearchDecisionResultViewModel viewModel = new SearchDecisionResultViewModel();
        viewModel.setDecisionId(decision.getId());
        viewModel.setDecisionDateTime(decision.getTimestamp());
        viewModel.setDecisionText(decision.getDecisionText());
        viewModel.setLinkedMeetingId(decision.getLinkedMeeting());

        //TODO: lookup decision maker and tag names

        //PersonDAO.getInstance().getPersonWithName("");
    }
}
