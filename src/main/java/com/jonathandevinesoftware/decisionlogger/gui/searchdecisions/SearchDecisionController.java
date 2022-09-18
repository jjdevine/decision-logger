package com.jonathandevinesoftware.decisionlogger.gui.searchdecisions;

import com.jonathandevinesoftware.decisionlogger.model.Decision;
import com.jonathandevinesoftware.decisionlogger.model.DecisionDAO;

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
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
