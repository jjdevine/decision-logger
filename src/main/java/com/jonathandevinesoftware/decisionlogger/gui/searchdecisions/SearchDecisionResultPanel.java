package com.jonathandevinesoftware.decisionlogger.gui.searchdecisions;

import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;

import javax.swing.*;
import java.awt.*;

public class SearchDecisionResultPanel {


    public static JPanel buildSearchDecisionResultPanel() {
        JPanel panel = ComponentFactory.createJPanel();
        panel.setPreferredSize(new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 40));
        panel.setBorder(ComponentFactory.createDefaultBorder());
        return panel;
    }
}
