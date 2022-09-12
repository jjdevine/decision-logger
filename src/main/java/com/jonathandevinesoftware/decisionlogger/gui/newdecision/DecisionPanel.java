package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ValueSelectorPanel;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DecisionPanel extends JPanel implements ActionListener {

    private JTextArea taDecision;
    private JScrollPane jspDecision;
    private ValueSelectorPanel vsDecisionMakers;
    private ValueSelectorPanel vsTags;
    private JButton bSave, bCancel;

    public DecisionPanel() {

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
        bCancel = ComponentFactory.createJButton("Cancel Decision", dimButton);

        panelButtons.add(bSave);
        panelButtons.add(bCancel);

        bSave.addActionListener(this);
        bCancel.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == bSave) {
            if(validateDecision()) {
                ViewModel viewModel = buildViewModel();
                listeners.forEach(l -> l.onSave(viewModel));
            }
        } else if(e.getSource() == bCancel) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Close without saving?",
                    "Cancel Decision?",
                    JOptionPane.OK_CANCEL_OPTION);
            if(choice == JOptionPane.OK_OPTION) {
                listeners.forEach(Listener::onCancel);
            }
        }
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

    private List<Listener> listeners = new ArrayList<>();

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public interface Listener {
        void onSave(ViewModel viewModel);

        void onCancel();
    }

    public class ViewModel {
        String decision;
        java.util.List<Person> decisionMakers;
        java.util.List<Tag> tags;

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
    }
}
