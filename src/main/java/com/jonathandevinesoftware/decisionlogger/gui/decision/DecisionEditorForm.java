package com.jonathandevinesoftware.decisionlogger.gui.decision;

import com.jonathandevinesoftware.decisionlogger.gui.common.Mode;
import com.jonathandevinesoftware.decisionlogger.gui.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiUtils;
import com.jonathandevinesoftware.decisionlogger.model.Decision;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.util.function.Consumer;

public class DecisionEditorForm extends BaseForm {

    private DecisionPanel decisionPanel;

    private Decision decision;

    private JPanel headerPanel;

    private Mode mode;

    public DecisionEditorForm(Decision decision, Mode mode) {
        super(mode == Mode.NEW ? "New Decision" : "Edit Decision");
        this.decision = decision;
        this.mode = mode;
        setupDecisionPanel();

        if(mode == Mode.NEW) {
            //create new decision
            setHeaderPanelText("New Decision");
        } else {
            //edit existing decision
            setHeaderPanelText("Edit Decision");
        }
    }

    private void setupDecisionPanel() {
        decisionPanel = new DecisionPanel(decision, mode);
        decisionPanel.setSaveCallback(this::onSave);
        decisionPanel.setCancelCallback(this::onCancel);
        add(decisionPanel);
    }

    @Override
    protected void init() {
        setPreferredSize(new Dimension(GuiConstants.DEFAULT_FORM_WIDTH, 660));
        headerPanel = ComponentFactory.createHeaderPanel(
                "",
                new Dimension(
                        GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH,
                        GuiConstants.DEFAULT_HEADER_HEIGHT));
        add(headerPanel);
    }

    private void setHeaderPanelText(String text) {
        ((JLabel)headerPanel.getComponent(0)).setText(text);
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

    private Optional<Runnable> discardCallBack = Optional.empty();

    public void setDiscardCallback(Runnable callback) {
        discardCallBack = Optional.of(callback);
    }

    @Override
    public void closeOperation() {
        discardCallBack.ifPresent(Runnable::run);
    }

    public boolean changesMade() {
        return decisionPanel.changesMade();
    }
}
