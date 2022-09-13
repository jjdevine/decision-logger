package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.model.Decision;
import com.jonathandevinesoftware.decisionlogger.model.DecisionDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

public class NewDecisionController implements NewDecisionForm.Listener {

    private NewDecisionForm form;

    public NewDecisionController() {
        form = new NewDecisionForm();
        form.addListener(this);
    }

    @Override
    public void onSave(DecisionPanel.ViewModel viewModel) {

        Decision decision = new Decision(UUID.randomUUID());
        decision.setDecisionText(viewModel.getDecision());
        decision.setTimestamp(LocalDateTime.now());
        decision.setDecisionMakers(
                viewModel.getDecisionMakers().stream().map(Person::getId).collect(Collectors.toList()));
        decision.setTags(
                viewModel.getTags().stream().map(Tag::getId).collect(Collectors.toList()));

        try {
            System.out.println("saving...");
            DecisionDAO.getInstance().saveDecision(decision);
            System.out.println("loading...");
            System.out.println(DecisionDAO.getInstance().loadDecision(decision.getId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCancel() {
        form.dispose();
        form = null;
    }
}
