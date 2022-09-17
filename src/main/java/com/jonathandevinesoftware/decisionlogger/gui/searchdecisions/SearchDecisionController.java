package com.jonathandevinesoftware.decisionlogger.gui.searchdecisions;

import java.util.List;
import java.util.UUID;

public class SearchDecisionController {

    private SearchDecisionForm searchDecisionForm;

    public SearchDecisionController() {
        searchDecisionForm = new SearchDecisionForm();
        searchDecisionForm.setSearchCallback(this::onSearch);
    }

    private void onSearch(List<UUID> decisionMakers, List<UUID> tags) {
        //TODO: this
    }
}
