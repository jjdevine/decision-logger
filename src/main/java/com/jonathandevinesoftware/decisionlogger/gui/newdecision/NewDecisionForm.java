package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.model.Decision;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NewDecisionForm extends BaseForm implements DecisionPanel.Listener {

    private DecisionPanel decisionPanel;

    public NewDecisionForm() {
        super("New Decision");
    }

    @Override
    protected void init() {
        setPreferredSize(new Dimension(GuiConstants.DEFAULT_FORM_WIDTH, 660));
        JPanel headerPanel = ComponentFactory.createHeaderPanel(
                "New Decision",
                new Dimension(
                        GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH,
                        GuiConstants.DEFAULT_HEADER_HEIGHT));
        add(headerPanel);

        decisionPanel = new DecisionPanel();
        decisionPanel.addListener(this);
        add(decisionPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void onSave(DecisionPanel.ViewModel viewModel) {
        listeners.forEach(l -> l.onSave(viewModel));
    }

    @Override
    public void onCancel() {
        listeners.forEach(Listener::onCancel);

    }

    private List<Listener> listeners = new ArrayList<>();

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public interface Listener {
        void onSave(DecisionPanel.ViewModel viewModel);
        void onCancel();
    }
}
