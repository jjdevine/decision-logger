package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.gui.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.util.function.Consumer;

public class NewDecisionForm extends BaseForm {

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
        decisionPanel.setSaveCallback(this::onSave);
        decisionPanel.setCancelCallback(this::onCancel);
        add(decisionPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private Optional<Consumer<DecisionPanel.ViewModel>> saveCallback = Optional.empty();

    public void setSaveCallback(Consumer<DecisionPanel.ViewModel> callback) {
        saveCallback = Optional.of(callback);
    }

    public void onSave(DecisionPanel.ViewModel viewModel) {
        saveCallback.ifPresent(c -> c.accept(viewModel));
    }

    private Optional<Runnable> cancelCallback = Optional.empty();

    public void setCancelCallback(Runnable callback) {
        cancelCallback = Optional.of(callback);
    }

    public void onCancel() {
        cancelCallback.ifPresent(Runnable::run);
    }
}
