package com.jonathandevinesoftware.decisionlogger.gui.searchdecisions;

import com.jonathandevinesoftware.decisionlogger.core.Application;
import com.jonathandevinesoftware.decisionlogger.gui.newdecision.DecisionEditorController;
import com.jonathandevinesoftware.decisionlogger.model.Decision;
import com.jonathandevinesoftware.decisionlogger.model.DecisionDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.PersonDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.TagDAO;

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
    }

    private void onSearch(List<UUID> decisionMakers, List<UUID> tags) {
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
    }

    private SearchDecisionResultViewModel buildSearchResultViewModel(
            Decision decision,
            List<UUID> decisionMakerSearchIds,
            List<UUID> tagSearchIds) {
        SearchDecisionResultViewModel viewModel = new SearchDecisionResultViewModel();
        viewModel.setDecisionId(decision.getId());
        viewModel.setDecisionDateTime(decision.getTimestamp());
        viewModel.setDecisionText(decision.getDecisionText());
        viewModel.setLinkedMeetingId(decision.getLinkedMeeting());

        try {
            final PersonDAO personDAO = PersonDAO.getInstance();
            List<String> decisionMakerSearchTerms = decisionMakerSearchIds
                    .stream()
                    .map(id -> personDAO.getPersonWithId(id).get().getValue())
                    .collect(Collectors.toList());
            viewModel.setDecisionMakerSearchTerms(decisionMakerSearchTerms);

            List<String> decisionMakers = new ArrayList<>();
            viewModel.setDecisionMakers(decisionMakers);
            for(UUID decisionMakerId: decision.getDecisionMakers()) {
                System.out.println("looking up person with id " + decisionMakerId);
                personDAO.getPersonWithId(decisionMakerId)
                        .ifPresent(person -> decisionMakers.add(person.getValue()));
            }

            final TagDAO tagDAO = TagDAO.getInstance();
            List<String> tagSearchTerms = tagSearchIds
                    .stream()
                    .map(id -> tagDAO.getTagWithId(id).get().getValue())
                    .collect(Collectors.toList());
            viewModel.setTagSearchTerms(tagSearchTerms);

            List<String> tags = new ArrayList<>();
            viewModel.setTags(tags);
            for(UUID tagId: decision.getTags()) {
                tagDAO.getTagWithId(tagId)
                        .ifPresent(tag -> tags.add(tag.getValue()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println(viewModel);

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
        //TODO this
    }
}
