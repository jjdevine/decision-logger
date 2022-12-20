package com.jonathandevinesoftware.decisionlogger.gui.meeting;

import com.jonathandevinesoftware.decisionlogger.gui.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;

import javax.swing.*;
import java.awt.*;

public class MeetingEditorForm extends BaseForm {

    private JPanel headerPanel;

    private JPanel meetingMetadataPanel;

  //  private JDatePicker meetingDatePicker;
    //https://github.com/LGoodDatePicker/LGoodDatePicker


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
    }

    @Override
    public void closeOperation() {

    }
}
