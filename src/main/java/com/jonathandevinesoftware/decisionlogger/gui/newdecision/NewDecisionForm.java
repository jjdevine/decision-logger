package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;

import javax.swing.*;
import java.awt.*;

public class NewDecisionForm extends BaseForm {

    private JTextArea taDecision;
    private DecisionPanel decisionPanel;

    public NewDecisionForm() {
        super("New Decision");
    }

    @Override
    protected void init() {
        setPreferredSize(new Dimension(GuiConstants.DEFAULT_FORM_WIDTH, 1000));
        JPanel headerPanel = ComponentFactory.createHeaderPanel(
                "New Decision",
                new Dimension(
                        GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH,
                        GuiConstants.DEFAULT_HEADER_HEIGHT));
        add(headerPanel);
        add(new DecisionPanel());

    }
}
