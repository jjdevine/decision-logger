package com.jonathandevinesoftware.decisionlogger.gui.decision;

import com.jonathandevinesoftware.decisionlogger.gui.common.Mode;
import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ValueSelectorPanel;
import com.jonathandevinesoftware.decisionlogger.model.Decision;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DecisionPanel extends JPanel implements ActionListener {

    private JTextArea taDecision;
    private JScrollPane jspDecision;
    private ValueSelectorPanel vsDecisionMakers;
    private ValueSelectorPanel vsTags;
    private JButton bSave, bCancel;
    private String cancelMessage;
    private String cancelTitle;
    private String cancelButtonText;
    private Decision decision;

    private Mode mode;

    public DecisionPanel(Decision decision, Mode mode) {
        this.decision = decision;
        this.mode = mode;

        if(mode == Mode.NEW) {
            cancelMessage = "Close without saving?";
            cancelTitle = "Cancel Decision?";
            cancelButtonText = "Cancel Decision";
        } else {
            cancelMessage = "Really delete this decision?";
            cancelTitle = "Delete Decision?";
            cancelButtonText = "Delete Decision";
        }

        setBorder(ComponentFactory.createDefaultBorder());
        setPreferredSize(new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 545));

        add(ComponentFactory.createJLabel("Decision"));

        taDecision = ComponentFactory.createJTextArea();
        jspDecision = ComponentFactory.createJScrollPane(taDecision);
        jspDecision.setPreferredSize(new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH-20, 100));
        add(jspDecision);

        vsDecisionMakers = new ValueSelectorPanel(
                GuiConstants.DEFAULT_HALF_COMPONENT_WIDTH,
                "Decision Maker",
                PersonDataSource.getInstance());
        add(vsDecisionMakers);

        vsTags = new ValueSelectorPanel(
                GuiConstants.DEFAULT_HALF_COMPONENT_WIDTH,
                "Tag",
                TagDataSource.getInstance());
        add(vsTags);

        JPanel panelButtons = ComponentFactory.createJPanel();
        panelButtons.setPreferredSize(new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH-20, 60));
        panelButtons.setBorder(new LineBorder(Color.GRAY));
        add(panelButtons);

        Dimension dimButton = new Dimension(GuiConstants.DEFAULT_HALF_COMPONENT_WIDTH-10,50);

        bSave = ComponentFactory.createJButton("Save Decision", dimButton);
        bCancel = ComponentFactory.createJButton(cancelButtonText, dimButton);
        if(mode == Mode.EDIT) {
            bCancel.setForeground(Color.RED);
        }

        panelButtons.add(bSave);
        panelButtons.add(bCancel);

        bSave.addActionListener(this);
        bCancel.addActionListener(this);

        initialiseDecisionView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == bSave) {
            if(validateDecision()) {
                ViewModel viewModel = buildViewModel();
                saveCallback.ifPresent(c -> c.accept(viewModel));
            }
        } else if(e.getSource() == bCancel) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    cancelMessage,
                    cancelTitle,
                    JOptionPane.OK_CANCEL_OPTION);
            if(choice == JOptionPane.OK_OPTION) {
                cancelCallback.ifPresent(Runnable::run);
            }
        }
    }

    private Optional<Consumer<ViewModel>> saveCallback = Optional.empty();

    public void setSaveCallback(Consumer<ViewModel> saveCallback) {
        this.saveCallback = Optional.of(saveCallback);
    }

    private Optional<Runnable> cancelCallback = Optional.empty();

    public void setCancelCallback(Runnable cancelCallback) {
        this.cancelCallback = Optional.of(cancelCallback);
    }

    private boolean validateDecision() {

        java.util.List<String> errors = new ArrayList();

        if(taDecision.getText().trim().length() == 0) {
            errors.add("Decision text must not be empty");
        }

        if(vsDecisionMakers.getSelectedValues().size() == 0) {
            errors.add("There must be at least one decision maker");
        }

        if(errors.size() > 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Errors:\n" + errors.stream().collect(Collectors.joining("\n")),
                    "Validation Errors",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }


        return true;
    }

    private ViewModel buildViewModel() {
        ViewModel viewModel = new ViewModel();
        if(decision != null) {
            viewModel.setDecisionId(decision.getId());
        }

        switch (mode) {
            case NEW -> viewModel.setTimestamp(LocalDateTime.now());
            case EDIT -> viewModel.setTimestamp(decision.getTimestamp());
        }

        viewModel.setDecision(taDecision.getText().trim());

        List<Person> decisionMakers = new ArrayList<>();
        decisionMakers.addAll(
                vsDecisionMakers.getSelectedValues().stream().map(rd -> (Person)rd).collect(Collectors.toList()));
        viewModel.setDecisionMakers(decisionMakers);

        List<Tag> tags = new ArrayList<>();
        tags.addAll(
                vsTags.getSelectedValues().stream().map(rd -> (Tag)rd).collect(Collectors.toList()));
        viewModel.setTags(tags);

        return viewModel;
    }

    private void initialiseDecisionView() {
        taDecision.setText(decision.getDecisionText());
        decision.getTags().forEach(tag -> vsTags.setSelectedValue(tag));
        decision.getDecisionMakers().forEach(tag -> vsDecisionMakers.setSelectedValue(tag));
    }


    public class ViewModel {

        UUID decisionId;
        String decision;
        java.util.List<Person> decisionMakers;
        java.util.List<Tag> tags;

        LocalDateTime timestamp;

        public UUID getDecisionId() {
            return decisionId;
        }

        public ViewModel setDecisionId(UUID decisionId) {
            this.decisionId = decisionId;
            return this;
        }

        public String getDecision() {
            return decision;
        }

        public void setDecision(String decision) {
            this.decision = decision;
        }

        public List<Person> getDecisionMakers() {
            return decisionMakers;
        }

        public void setDecisionMakers(List<Person> decisionMakers) {
            this.decisionMakers = decisionMakers;
        }

        public List<Tag> getTags() {
            return tags;
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }

}
