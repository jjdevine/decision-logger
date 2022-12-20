package com.jonathandevinesoftware.decisionlogger.gui.meeting;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.jonathandevinesoftware.decisionlogger.gui.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class MeetingEditorForm extends BaseForm {

    private JPanel headerPanel;

    private JPanel meetingMetadataPanel;

    private DateTimePicker meetingDateTimePicker;
    //https://github.com/LGoodDatePicker/LGoodDatePicker

    private JTextField tfMeetingTitle;


    public MeetingEditorForm(String title) {
        super(title);
    }

    @Override
    protected void init() {
        setPreferredSize(new Dimension(GuiConstants.DEFAULT_FORM_WIDTH, 1000));

        headerPanel = ComponentFactory.createHeaderPanel(
                "New Meeting",
                new Dimension(
                        GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH,
                        GuiConstants.DEFAULT_HEADER_HEIGHT));
        add(headerPanel);

        meetingMetadataPanel = ComponentFactory.createJPanel();
        meetingMetadataPanel.setPreferredSize(new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 500));
        add(meetingMetadataPanel);

        DatePickerSettings datePickerSettings = new DatePickerSettings();
        datePickerSettings.setAllowEmptyDates(false);

        TimePickerSettings timePickerSettings = new TimePickerSettings();
        timePickerSettings.setAllowEmptyTimes(false);

        Dimension twoColumnPanelDimension = new Dimension(GuiConstants.DEFAULT_HALF_COMPONENT_WIDTH, 35);
        JPanel col1Panel = ComponentFactory.createJPanelWithMargin(1,1);
        col1Panel.setPreferredSize(twoColumnPanelDimension);
        JPanel col2Panel = ComponentFactory.createJPanelWithMargin(1,1);
        col2Panel.setPreferredSize(twoColumnPanelDimension);

        meetingDateTimePicker = new DateTimePicker(datePickerSettings, timePickerSettings);
        meetingDateTimePicker.setDateTimeStrict(LocalDateTime.now());

        col1Panel.add(ComponentFactory.createJLabel("Date and Time: "));
        col1Panel.add(meetingDateTimePicker);

        tfMeetingTitle = ComponentFactory.createJTextField();
        tfMeetingTitle.setPreferredSize(new Dimension(GuiConstants.DEFAULT_HALF_COMPONENT_WIDTH/2, 30));

        col2Panel.add(ComponentFactory.createJLabel("Meeting Title: "));
        col2Panel.add(tfMeetingTitle);

        meetingMetadataPanel.add(col1Panel);
        meetingMetadataPanel.add(col2Panel);

    }

    @Override
    public void closeOperation() {
        System.exit(0);
    }
}
