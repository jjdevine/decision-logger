package com.jonathandevinesoftware.decisionlogger.gui.meeting;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.jonathandevinesoftware.decisionlogger.gui.decision.DecisionEditorForm;
import com.jonathandevinesoftware.decisionlogger.gui.decision.DecisionPanel;
import com.jonathandevinesoftware.decisionlogger.gui.decision.PersonDataSource;
import com.jonathandevinesoftware.decisionlogger.gui.decision.TagDataSource;
import com.jonathandevinesoftware.decisionlogger.gui.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ValueSelectorPanel;
import com.jonathandevinesoftware.decisionlogger.model.Decision;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MeetingEditorForm extends BaseForm {

    private JPanel headerPanel;

    private JPanel meetingMetadataPanel;

    private JPanel meetingMetadataSummaryPanel;

    private DateTimePicker meetingDateTimePicker;
    //https://github.com/LGoodDatePicker/LGoodDatePicker

    private JTextField tfMeetingTitle;

    private ValueSelectorPanel vsAttendees;

    private ValueSelectorPanel vsTags;

    private JButton bCollapse, bFinish, bCancelDelete, bAddDecision;

    private boolean meetingMetadataCollapsed = false;

    private DecisionPanel decisionPanel;

    private JScrollPane jspDecisions;

    private Dimension dimJspLarge;
    private Dimension dimJspSmall;

    private JPanel decisionsPanel;

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
        meetingMetadataPanel.setBorder(ComponentFactory.createDefaultBorder());
        meetingMetadataPanel.setPreferredSize(new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 420));
        add(meetingMetadataPanel);

        meetingMetadataSummaryPanel = ComponentFactory.createJPanel();
        meetingMetadataSummaryPanel.setBorder(ComponentFactory.createDefaultBorder());
        meetingMetadataSummaryPanel.setPreferredSize(new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 110));
        meetingMetadataSummaryPanel.add(ComponentFactory.createJLabel(""));

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
        tfMeetingTitle.setPreferredSize(
                new Dimension((int)(GuiConstants.DEFAULT_HALF_COMPONENT_WIDTH/1.3), 30));

        col2Panel.add(ComponentFactory.createJLabel("Meeting Title: "));
        col2Panel.add(tfMeetingTitle);

        meetingMetadataPanel.add(col1Panel);
        meetingMetadataPanel.add(col2Panel);

        vsAttendees = new ValueSelectorPanel(
                GuiConstants.DEFAULT_HALF_COMPONENT_WIDTH,
                "Attendee",
                PersonDataSource.getInstance());

        vsTags = new ValueSelectorPanel(
                GuiConstants.DEFAULT_HALF_COMPONENT_WIDTH,
                "Tag",
                TagDataSource.getInstance());

        bCollapse = ComponentFactory.createJButton(
                "Collapse",
                new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH-20, 25),
                this::toggleCollapse);

        meetingMetadataPanel.add(vsAttendees);
        meetingMetadataPanel.add(vsTags);
        meetingMetadataPanel.add(bCollapse);

        Dimension dimTerminationButton = new Dimension(GuiConstants.DEFAULT_HALF_COMPONENT_WIDTH, 40);

        bFinish = ComponentFactory.createJButton(
                "Finish Meeting", dimTerminationButton, this::onFinish);
        bCancelDelete = ComponentFactory.createJButton(
                "Cancel/Delete Meeting", dimTerminationButton, this::onCancelDelete);

        add(bFinish);
        add(bCancelDelete);

        dimJspLarge = new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 660);
        dimJspSmall = new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 350);

        jspDecisions = ComponentFactory.createJScrollPane(decisionsPanel);
        jspDecisions.setPreferredSize(dimJspSmall);
        add(jspDecisions);

        bAddDecision = ComponentFactory.createJButton(
                "Add Decision",
                new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 50),
                this::onAddDecision);
        add(bAddDecision);
    }

    private void onAddDecision() {
        DecisionEditorForm decisionEditorForm = new DecisionEditorForm(null);
        decisionEditorForm.setCancelCallback(decisionEditorForm::dispose);
        decisionEditorForm.setSaveCallback(this::saveDecision);
    }

    private void saveDecision(DecisionPanel.ViewModel viewModel) {
        Decision decision;
        if(viewModel.getDecisionId() != null) {
            decision = new Decision(viewModel.getDecisionId());
        } else {
            decision = new Decision(UUID.randomUUID());
        }

        //TODO convert to decision and save to meeting view model
    }

    private void toggleCollapse() {
        if(meetingMetadataCollapsed) { //need to expand
            remove(meetingMetadataSummaryPanel);
            bCollapse.setText("Collapse");
            meetingMetadataPanel.add(bCollapse);
            jspDecisions.setPreferredSize(dimJspSmall);
            add(meetingMetadataPanel, 1);
        } else { //need to collapse
            updateMetadataSummary();
            add(meetingMetadataSummaryPanel, 1);
            bCollapse.setText("Expand");
            meetingMetadataSummaryPanel.add(bCollapse);
            jspDecisions.setPreferredSize(dimJspLarge);
            remove(meetingMetadataPanel);
        }

        //TODO - resize JScrollPane

        revalidate();
        repaint();

        meetingMetadataCollapsed = !meetingMetadataCollapsed;
    }

    private void onFinish() {

    }

    private void onCancelDelete() {

    }

    private void updateMetadataSummary() {
        MeetingViewModel viewModel = buildViewModel();
        StringBuilder summary = new StringBuilder();

        summary.append("<html>");
        summary.append("Meeting at: " + viewModel.getMeetingDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        summary.append("<br/>Meeting title: " + viewModel.getMeetingTitle());
        summary.append("<br/>Attendees: " +
                viewModel.getAttendees().stream().map(Person::getValue).collect(Collectors.joining(", ")));
        summary.append("<br/>Tags: " +
                viewModel.getTags().stream().map(Tag::getValue).collect(Collectors.joining(", ")));
        summary.append("</html>");

        JLabel summaryLabel = (JLabel) meetingMetadataSummaryPanel.getComponent(0);
        summaryLabel.setText(summary.toString());
    }

    private MeetingViewModel buildViewModel() {
        MeetingViewModel viewModel = new MeetingViewModel();

        viewModel.setMeetingDateTime(meetingDateTimePicker.getDateTimeStrict());
        viewModel.setMeetingTitle(tfMeetingTitle.getText());

        List<Person> attendees = new ArrayList<>();
        attendees.addAll(
                vsAttendees.getSelectedValues().stream().map(rd -> (Person)rd).collect(Collectors.toList()));
        viewModel.setAttendees(attendees);

        List<Tag> tags = new ArrayList<>();
        tags.addAll(
                vsTags.getSelectedValues().stream().map(rd -> (Tag)rd).collect(Collectors.toList()));
        viewModel.setTags(tags);

        System.out.println("buildViewModel() = " + viewModel);

        return viewModel;
    }

    public class MeetingViewModel {
        LocalDateTime meetingDateTime;
        String meetingTitle;
        java.util.List<Person> attendees;
        java.util.List<Tag> tags;
        java.util.List<Decision> decisions;

        public LocalDateTime getMeetingDateTime() {
            return meetingDateTime;
        }

        public MeetingViewModel setMeetingDateTime(LocalDateTime meetingDateTime) {
            this.meetingDateTime = meetingDateTime;
            return this;
        }

        public String getMeetingTitle() {
            return meetingTitle;
        }

        public MeetingViewModel setMeetingTitle(String meetingTitle) {
            this.meetingTitle = meetingTitle;
            return this;
        }

        public List<Person> getAttendees() {
            return attendees;
        }

        public MeetingViewModel setAttendees(List<Person> attendees) {
            this.attendees = attendees;
            return this;
        }

        public List<Tag> getTags() {
            return tags;
        }

        public MeetingViewModel setTags(List<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public List<Decision> getDecisions() {
            return decisions;
        }

        public MeetingViewModel setDecisions(List<Decision> decisions) {
            this.decisions = decisions;
            return this;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MeetingViewModel{");
            sb.append("meetingDateTime=").append(meetingDateTime);
            sb.append(", meetingTitle='").append(meetingTitle).append('\'');
            sb.append(", attendees=").append(attendees);
            sb.append(", tags=").append(tags);
            sb.append(", decisions=").append(decisions);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public void closeOperation() {
        System.exit(0);
    }
}
