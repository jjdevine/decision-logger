package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.model.DecisionDAO;

import javax.swing.*;
import java.awt.*;

public class NewDecisionForm extends BaseForm implements DecisionPanel.Listener {

    private JTextArea taDecision;
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
        System.out.println("save");#
        //TODO convert viewmodel to decision and save it, then load it to see if it's the same
        //DecisionDAO.getInstance().saveDecision();
    }

    @Override
    public void onCancel() {
        System.out.println("cancel");

    }
}
