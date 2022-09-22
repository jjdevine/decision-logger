package com.jonathandevinesoftware.decisionlogger.gui.searchdecisions;

import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;

import javax.swing.*;
import java.awt.*;

public class SearchDecisionResultPanel {

    public static JPanel buildSearchDecisionResultPanel(SearchDecisionResultViewModel viewModel) {
        JPanel panel = ComponentFactory.createJPanel();
        FlowLayout layout = new FlowLayout();
        layout.setHgap(0);
        layout.setVgap(0);
        panel.setLayout(layout);

        panel.setPreferredSize(new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 40));
        panel.setBorder(ComponentFactory.createDefaultBorder());

        int readInfoWidth = (int)(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH*0.79);
        int buttonsWidth = (int)(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH*0.19);
        //TODO: read and buttons panels

        JPanel panelReadInfo = ComponentFactory.createJPanel();
        JPanel panelButtons = ComponentFactory.createJPanel();


        //decision date
        JPanel panelDecisionDate = ComponentFactory.createJPanel();
        panelDecisionDate.setPreferredSize(
                new Dimension((int)(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH*0.19), 20));

        //decision text
        JPanel panelDecisionText = ComponentFactory.createJPanel();
        panelDecisionText.setPreferredSize(
                new Dimension((int)(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH*0.79), 20));

        panel.add(panelDecisionDate);
        panel.add(panelDecisionText);

        return panel;
    }
}
