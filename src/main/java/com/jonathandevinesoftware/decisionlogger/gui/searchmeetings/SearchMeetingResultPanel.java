package com.jonathandevinesoftware.decisionlogger.gui.searchmeetings;

import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class SearchMeetingResultPanel {

    public static JPanel buildSearchDecisionResultPanel(SearchMeetingResultViewModel viewModel,
                Runnable onOpenMeeting) {
        //TODO - this method
        JPanel panel = ComponentFactory.createJPanelWithMargin(0,0);

        int panelWidth = SearchMeetingFormConstants.FULL_COMPONENT_WIDTH-10;

        panel.setPreferredSize(new Dimension(SearchMeetingFormConstants.FULL_COMPONENT_WIDTH-10, 40));
        panel.setBorder(ComponentFactory.createDefaultBorder());

        int readInfoWidth = (int)(panelWidth*0.8);
        int buttonsWidth = (int)(panelWidth*0.19);
        int layoutPanelHeight = 38;

        JPanel panelReadInfo = ComponentFactory.createJPanelWithMargin(0,0);
        panelReadInfo.setPreferredSize(new Dimension(readInfoWidth, layoutPanelHeight));
        JPanel panelButtons = ComponentFactory.createJPanelWithMargin(0,0);
        panelButtons.setPreferredSize(new Dimension(buttonsWidth, layoutPanelHeight));

        panel.add(panelReadInfo);
        panel.add(panelButtons);

        //meeting title
        JPanel panelMeetingTitle = ComponentFactory.createJPanelWithMargin(1,1);
        panelMeetingTitle.setPreferredSize(
                new Dimension((int)(readInfoWidth*0.69), 20));
        JLabel lMeetingText = ComponentFactory.createJLabel(GuiUtils.truncate(viewModel.getTitle(), 100));
        panelMeetingTitle.add(lMeetingText);
        panelReadInfo.add(panelMeetingTitle);
        panelMeetingTitle.setBackground(new Color(255,255,200));

        //meeting date
        JPanel panelMeetingDate = ComponentFactory.createJPanelWithMargin(1,5);
        panelMeetingDate.setPreferredSize(
                new Dimension((int)(readInfoWidth*0.15), 20));
        JLabel lDate = ComponentFactory.createJLabel(
                viewModel.getTimestamp().format(GuiUtils.getDefaultDateTimeFormatter()));
        panelMeetingDate.add(lDate);
        panelReadInfo.add(panelMeetingDate);
        panelMeetingDate.setBackground(new Color(255,200,255));

        //number of decisions
        JPanel panelDecisionCount = ComponentFactory.createJPanelWithMargin(1,5);
        panelDecisionCount.setPreferredSize(
                new Dimension((int)(readInfoWidth*0.15), 20));
        JLabel lDecisionCount = ComponentFactory.createJLabel(
                "Number of decisions: "+ viewModel.getNumberOfDecisions());
        panelDecisionCount.add(lDecisionCount);
        panelReadInfo.add(panelDecisionCount);
        panelDecisionCount.setBackground(new Color(255,227,227));

        //attendees
        JPanel panelAttendees = ComponentFactory.createJPanelWithMargin(1,1);
        panelAttendees.setPreferredSize(
                new Dimension((int)(readInfoWidth*0.6), 20));
        JLabel lDecisionMakers = ComponentFactory.createJLabel(getAttendeeText(viewModel));
        panelAttendees.add(lDecisionMakers);
        panelReadInfo.add(panelAttendees);
        panelAttendees.setBackground(new Color(200,255,255));

        //tags
        JPanel panelTags = ComponentFactory.createJPanelWithMargin(1,1);
        panelTags.setPreferredSize(
                new Dimension((int)(readInfoWidth*0.39), 20));
        JLabel lTags = ComponentFactory.createJLabel(getTagText(viewModel));
        panelTags.add(lTags);
        panelReadInfo.add(panelTags);
        panelTags.setBackground(new Color(200,200,255));

        //button
        Dimension dimButton = new Dimension(
                (int)(buttonsWidth*0.99),
                (int)(layoutPanelHeight*0.99));

        JButton bOpenMeeting = ComponentFactory.createJButton("Open  Meeting", dimButton, onOpenMeeting);
        panelButtons.add(bOpenMeeting);

        return panel;
    }

    private static String getAttendeeText(SearchMeetingResultViewModel viewModel) {

        //sort by which ones match the search terms and alphabetically
        java.util.List<String> sortedAttendees =  viewModel.getAttendees()
                .stream()
                .sorted((attendee1, attendee2) ->
            {
                boolean attendee1WasSearched = viewModel.getSearchedAttendees().contains(attendee1);
                boolean attendee2WasSearched = viewModel.getSearchedAttendees().contains(attendee2);

                if(attendee1WasSearched && !attendee2WasSearched) {
                    return -1;
                }

                if(!attendee1WasSearched && attendee2WasSearched) {
                    return 1;
                }

                return attendee1.compareTo(attendee2);
            }).collect(Collectors.toList());

        String text = sortedAttendees.stream().collect(Collectors.joining(", "));
        return GuiUtils.truncate(text, 200);
    }

    private static String getTagText(SearchMeetingResultViewModel viewModel) {
        //sort by which ones match the search terms and alphabetically
        List<String> sortedTags =  viewModel.getTags()
                .stream()
                .sorted((t1, t2) ->
                {
                    boolean t1WasSearched = viewModel.getSearchedTags().contains(t1);
                    boolean t2WasSearched = viewModel.getSearchedTags().contains(t2);

                    if(t1WasSearched && !t2WasSearched) {
                        return -1;
                    }

                    if(!t1WasSearched && t2WasSearched) {
                        return 1;
                    }

                    return t1.compareTo(t2);
                }).collect(Collectors.toList());

        String text = sortedTags.stream().collect(Collectors.joining(", "));
        return GuiUtils.truncate(text, 100);
    }
}
