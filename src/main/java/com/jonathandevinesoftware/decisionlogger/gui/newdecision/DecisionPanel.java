package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ValueSelectorPanel;

import javax.swing.*;
import java.awt.*;

public class DecisionPanel extends JPanel {

    private JTextArea taDecision;
    private JScrollPane jspDecision;
    private ValueSelectorPanel vsDecisionMaker;

    public DecisionPanel() {

        setBorder(ComponentFactory.createDefaultBorder());
        setPreferredSize(new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 500));

        add(ComponentFactory.createJLabel("Decision"));

        taDecision = ComponentFactory.createJTextArea();
        jspDecision = ComponentFactory.createJScrollPane(taDecision);
        jspDecision.setPreferredSize(new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH-20, 100));

        add(jspDecision);
        add(new ValueSelectorPanel(
                GuiConstants.DEFAULT_HALF_COMPONENT_WIDTH,
                "Decision Maker",
                PersonDataSource.getInstance()));
    }
}
