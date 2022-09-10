package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ValueSelectorPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DecisionPanel extends JPanel {

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
    }
}
