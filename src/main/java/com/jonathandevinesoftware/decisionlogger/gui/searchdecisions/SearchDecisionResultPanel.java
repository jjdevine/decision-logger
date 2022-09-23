package com.jonathandevinesoftware.decisionlogger.gui.searchdecisions;

import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchDecisionResultPanel {

    public static JPanel buildSearchDecisionResultPanel(SearchDecisionResultViewModel viewModel) {
        JPanel panel = ComponentFactory.createJPanelWithMargin(0,0);

        panel.setPreferredSize(new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 40));
        panel.setBorder(ComponentFactory.createDefaultBorder());

        int readInfoWidth = (int)(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH*0.8);
        int buttonsWidth = (int)(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH*0.19);
        int layoutPanelHeight = 38;
        //TODO: read and buttons panels

        JPanel panelReadInfo = ComponentFactory.createJPanelWithMargin(0,0);
        panelReadInfo.setPreferredSize(new Dimension(readInfoWidth, layoutPanelHeight));
        JPanel panelButtons = ComponentFactory.createJPanelWithMargin(0,0);
        panelButtons.setPreferredSize(new Dimension(buttonsWidth, layoutPanelHeight));

        panel.add(panelReadInfo);
        panel.add(panelButtons);


        //decision date
        JPanel panelDecisionDate = ComponentFactory.createJPanelWithMargin(1,5);
        panelDecisionDate.setPreferredSize(
                new Dimension((int)(readInfoWidth*0.19), 20));
        JLabel lDate = ComponentFactory.createJLabel(
                viewModel.getDecisionDateTime().format(GuiUtils.getDefaultDateTimeFormatter()));
        panelDecisionDate.add(lDate);
        panelReadInfo.add(panelDecisionDate);

        //decision text
        JPanel panelDecisionText = ComponentFactory.createJPanelWithMargin(1,1);
        panelDecisionText.setPreferredSize(
                new Dimension((int)(readInfoWidth*0.8), 20));
        JLabel lDecisionText = ComponentFactory.createJLabel(truncate(viewModel.getDecisionText(), 100));
        panelDecisionText.add(lDecisionText);
        panelReadInfo.add(panelDecisionText);

        //decision maker(s)
        JPanel panelDecisionMakers = ComponentFactory.createJPanelWithMargin(1,1);
        panelDecisionMakers.setPreferredSize(
                new Dimension((int)(readInfoWidth*0.4), 20));
        //TODO - put searched for decision makers first in the list
        JLabel lDecisionMakers = ComponentFactory.createJLabel(getDecisionMakerText(viewModel));
        panelDecisionMakers.add(lDecisionMakers);
        panelReadInfo.add(panelDecisionMakers);

        return panel;
    }

    private static String getDecisionMakerText(SearchDecisionResultViewModel viewModel) {
        //sort by which ones match the search terms and alphabetically
        List<String> sortedDecisionMakers =  viewModel.getDecisionMakers()
                .stream()
                .sorted((dm1, dm2) ->
            {
                boolean dm1WasSearched = viewModel.getDecisionMakerSearchTerms().contains(dm1);
                boolean dm2WasSearched = viewModel.getDecisionMakerSearchTerms().contains(dm2);

                if(dm1WasSearched && !dm2WasSearched) {
                    System.out.println(dm1 + " is first");
                    return -1;
                }

                if(!dm1WasSearched && dm2WasSearched) {
                    System.out.println(dm2 + " is first");
                    return 1;
                }

                return dm1.compareTo(dm2);
            }).collect(Collectors.toList());

        String text = sortedDecisionMakers.stream().collect(Collectors.joining(", "));
        return truncate(text, 50);
    }

    private static String truncate(String str, int maxLen) {
        if(str.length() > maxLen) {
            return str.substring(0, maxLen-3) + "...";
        }
        return str;
    }
}
