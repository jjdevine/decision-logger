package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.core.ApplicationConstants;
import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.MainMenuController;
import com.jonathandevinesoftware.decisionlogger.model.Decision;
import com.jonathandevinesoftware.decisionlogger.model.DecisionDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

public class NewDecisionController {

    private NewDecisionForm form;

    public NewDecisionController() {
        form = new NewDecisionForm();
        form.setSaveCallback(this::onSave);
        form.setCancelCallback(this::onCancel);
    }

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

            if(ApplicationConstants.DEBUG) {
                System.out.println("loading...");
                System.out.println(DecisionDAO.getInstance().loadDecision(decision.getId()));
            }

            JOptionPane.showMessageDialog(form,
                    "Decision saved successfully!",
                    "Decision Saved",
                    JOptionPane.INFORMATION_MESSAGE);
            closeForm();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onCancel() {
        closeForm();
    }

    private void closeForm() {
        form.dispose();
        form = null;
        MainMenuController.getInstance().displayMainMenu();
    }
}
