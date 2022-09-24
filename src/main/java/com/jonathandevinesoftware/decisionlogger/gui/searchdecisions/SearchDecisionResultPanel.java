package com.jonathandevinesoftware.decisionlogger.gui.searchdecisions;

import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class SearchDecisionResultPanel {

    public static JPanel buildSearchDecisionResultPanel(
            SearchDecisionResultViewModel viewModel,
            Runnable onOpenDecision,
            Runnable onOpenLinkedMeeting) {
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
        panelDecisionDate.setBackground(new Color(255,200,255));

        //decision text
        JPanel panelDecisionText = ComponentFactory.createJPanelWithMargin(1,1);
        panelDecisionText.setPreferredSize(
                new Dimension((int)(readInfoWidth*0.8), 20));
        JLabel lDecisionText = ComponentFactory.createJLabel(truncate(viewModel.getDecisionText(), 100));
        panelDecisionText.add(lDecisionText);
        panelReadInfo.add(panelDecisionText);
        panelDecisionText.setBackground(new Color(255,255,200));

        //decision maker(s)
        JPanel panelDecisionMakers = ComponentFactory.createJPanelWithMargin(1,1);
        panelDecisionMakers.setPreferredSize(
                new Dimension((int)(readInfoWidth*0.4), 20));
        JLabel lDecisionMakers = ComponentFactory.createJLabel(getDecisionMakerText(viewModel));
        panelDecisionMakers.add(lDecisionMakers);
        panelReadInfo.add(panelDecisionMakers);
        panelDecisionMakers.setBackground(new Color(200,255,255));

        //tags
        JPanel panelTags = ComponentFactory.createJPanelWithMargin(1,1);
        panelTags.setPreferredSize(
                new Dimension((int)(readInfoWidth*0.4), 20));
        JLabel lTags = ComponentFactory.createJLabel(getTagText(viewModel));
        panelTags.add(lTags);
        panelReadInfo.add(panelTags);
        panelTags.setBackground(new Color(200,200,255));


        //linked meeting
        JPanel panelLinkedMeeting = ComponentFactory.createJPanelWithMargin(1,1);
        panelLinkedMeeting.setPreferredSize(
                new Dimension((int)(readInfoWidth*0.19), 20));
        JLabel lLinkedMeeting = ComponentFactory.createJLabel("No linked Meeting");
        lLinkedMeeting.setFont(ComponentFactory.getItalicFont(10));
        panelLinkedMeeting.add(lLinkedMeeting);
        panelReadInfo.add(panelLinkedMeeting);
        panelLinkedMeeting.setBackground(new Color(255,200,200));

        Dimension dimButton = new Dimension(
                (int)(buttonsWidth*0.99),
                (int)(layoutPanelHeight*0.49));

        //buttons
        JButton bOpenDecision = ComponentFactory.createJButton("Open Decision", dimButton, onOpenDecision);
        JButton bOpenLinkedMeeting = ComponentFactory.createJButton("Open Linked Meeting", dimButton, onOpenLinkedMeeting);

        panelButtons.add(bOpenDecision);
        panelButtons.add(bOpenLinkedMeeting);

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
                    return -1;
                }

                if(!dm1WasSearched && dm2WasSearched) {
                    return 1;
                }

                return dm1.compareTo(dm2);
            }).collect(Collectors.toList());

        String text = sortedDecisionMakers.stream().collect(Collectors.joining(", "));
        return truncate(text, 50);
    }

    private static String getTagText(SearchDecisionResultViewModel viewModel) {
        //sort by which ones match the search terms and alphabetically
        List<String> sortedTags =  viewModel.getTags()
                .stream()
                .sorted((t1, t2) ->
                {
                    boolean t1WasSearched = viewModel.getTagSearchTerms().contains(t1);
                    boolean t2WasSearched = viewModel.getTagSearchTerms().contains(t2);

                    if(t1WasSearched && !t2WasSearched) {
                        return -1;
                    }

                    if(!t1WasSearched && t2WasSearched) {
                        return 1;
                    }

                    return t1.compareTo(t2);
                }).collect(Collectors.toList());

        String text = sortedTags.stream().collect(Collectors.joining(", "));
        return truncate(text, 50);
    }

    private static String truncate(String str, int maxLen) {
        if(str.length() > maxLen) {
            return str.substring(0, maxLen-3) + "...";
        }
        return str;
    }
}
