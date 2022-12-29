package com.jonathandevinesoftware.decisionlogger.gui.searchmeetings;

import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;

import javax.swing.*;
import java.awt.*;

public class SearchMeetingResultPanel {

    public static JPanel buildSearchDecisionResultPanel(SearchMeetingResultViewModel viewModel) {
        //TODO - this method
        JPanel panel = ComponentFactory.createJPanelWithMargin(0,0);

        panel.setPreferredSize(new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 40));
        panel.setBorder(ComponentFactory.createDefaultBorder());

        panel.add(ComponentFactory.createJLabel(viewModel.getMeetingId().toString()));
        return panel;

    }

}
