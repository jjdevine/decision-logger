package com.jonathandevinesoftware.decisionlogger.gui.meeting;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.jonathandevinesoftware.decisionlogger.gui.common.Mode;
import com.jonathandevinesoftware.decisionlogger.gui.common.SearchResultJPanel;
import com.jonathandevinesoftware.decisionlogger.gui.decision.DecisionEditorForm;
import com.jonathandevinesoftware.decisionlogger.gui.decision.DecisionPanel;
import com.jonathandevinesoftware.decisionlogger.gui.decision.PersonDataSource;
import com.jonathandevinesoftware.decisionlogger.gui.decision.TagDataSource;
import com.jonathandevinesoftware.decisionlogger.gui.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ValueSelectorPanel;
import com.jonathandevinesoftware.decisionlogger.model.Decision;
import com.jonathandevinesoftware.decisionlogger.model.DecisionDAO;
import com.jonathandevinesoftware.decisionlogger.model.Meeting;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.PersonDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.TagDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
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

    private SearchResultJPanel decisionsPanel;

    private Meeting underlyingMeeting;

    private Map<UUID, DecisionEditorForm> openDecisionEditors = new HashMap<>();

    private List<Decision> meetingDecisions;

    private Mode mode;

    public MeetingEditorForm(Meeting meeting, Mode mode) {
        super(mode == Mode.NEW ? "New Meeting" : "Edit Meeting");
        this.mode = mode;
        this.underlyingMeeting = meeting;

        if(mode != Mode.NEW) {
            try {
                meetingDecisions = DecisionDAO.getInstance().loadDecisionsByLinkedMeetingId(meeting.getId());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Could not load linked decisions - " + e.getMessage());
            }
        } else {
            meetingDecisions = new ArrayList<>();
        }

        populateMeetingMetaData();
        refreshDecisionsDisplay();
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

        decisionsPanel = ComponentFactory.getSearchResultJPanel();
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
        //prepopulate decision with decision makers/tags used by this meeting
        Decision decision = new Decision(UUID.randomUUID());
        MeetingViewModel viewModel = buildViewModel();
        decision.setDecisionMakers(viewModel.attendees.stream().map(Person::getId).collect(Collectors.toList()));
        decision.setTags(viewModel.tags.stream().map(Tag::getId).collect(Collectors.toList()));

        DecisionEditorForm decisionEditorForm = new DecisionEditorForm(decision, Mode.NEW);
        openDecisionEditors.put(decision.getId(), decisionEditorForm);
        decisionEditorForm.setCancelCallback(decisionEditorForm::dispose);
        decisionEditorForm.setSaveCallback(this::saveDecision);
    }

    private void saveDecision(DecisionPanel.ViewModel viewModel) {
        Decision decision = new Decision(viewModel.getDecisionId());

        decision.setDecisionText(viewModel.getDecision());
        decision.setTimestamp(LocalDateTime.now());
        decision.setDecisionMakers(
                viewModel.getDecisionMakers().stream().map(Person::getId).collect(Collectors.toList()));
        decision.setTags(
                viewModel.getTags().stream().map(Tag::getId).collect(Collectors.toList()));
        decision.setLinkedMeeting(Optional.of(underlyingMeeting.getId()));
        meetingDecisions.add(decision);

        refreshDecisionsDisplay();
        //TODO - the below errors when triggered from a meeting that is being edited..
        openDecisionEditors.get(viewModel.getDecisionId()).dispose();
        openDecisionEditors.remove(viewModel.getDecisionId());
    }

    private void populateMeetingMetaData() {
        meetingDateTimePicker.setDateTimeStrict(underlyingMeeting.getTimestamp());
        tfMeetingTitle.setText(underlyingMeeting.getTitle());
        underlyingMeeting.getTags().forEach(tag -> vsTags.setSelectedValue(tag));
        underlyingMeeting.getAttendees().forEach(attendee -> vsAttendees.setSelectedValue(attendee));
    }

    private void refreshDecisionsDisplay() {

        int panelWidth = GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH-10;
        int panelHeight = 55;
        int innerPanelHeight = panelHeight-4;
        decisionsPanel.clear();

        for(Decision decision: meetingDecisions) {

            JPanel decisionSummaryPanel = ComponentFactory.createJPanelWithMargin(1,1);
            decisionSummaryPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));

            StringBuilder summaryText = new StringBuilder();
            summaryText.append("<html>");
            summaryText.append("Decision: " + decision.getDecisionText());
            summaryText.append("<br/>Decision Makers: " +
                    decision.getDecisionMakers()
                            .stream()
                            .map(id -> getPersonName(id))
                            .collect(Collectors.joining(", ")));
            summaryText.append("<br/>Tags: " +
                    decision.getTags()
                            .stream()
                            .map(id -> getTagName(id))
                            .collect(Collectors.joining(", ")));
            summaryText.append("</html>");

            JPanel summaryTextPanel =
                    ComponentFactory.createHeaderPanel(
                            summaryText.toString(),
                            new Dimension(panelWidth-100, innerPanelHeight),
                            ComponentFactory.getStandardFont(12));

            JButton bEdit = ComponentFactory.createJButton(
                    "Edit",
                    new Dimension(88, innerPanelHeight),
                    () -> onEditDecision(decision.getId()));

            decisionSummaryPanel.add(summaryTextPanel);
            decisionSummaryPanel.add(bEdit);
            decisionsPanel.addSearchResult(decisionSummaryPanel);
        }
       revalidate();
    }

    private void onEditDecision(UUID decisionId) {
        System.out.println("Edit decision " + decisionId);
        DecisionEditorForm decisionEditorForm = new DecisionEditorForm(
                meetingDecisions.stream()
                        .filter(d -> d.getId().equals(decisionId)).findFirst().get(),
                Mode.EDIT);
        openDecisionEditors.put(decisionId, decisionEditorForm);
        decisionEditorForm.setCancelCallback(() -> onCancelDecision(decisionId, decisionEditorForm));
        decisionEditorForm.setSaveCallback(this::saveDecision);
    }

    private void onCancelDecision(UUID decisionId, DecisionEditorForm decisionEditorForm) {
        decisionEditorForm.dispose();
        meetingDecisions.removeIf(d -> d.getId().equals(decisionId));
        refreshDecisionsDisplay();
    }

    private String getPersonName(UUID id) {
        try {
            return PersonDAO.getInstance().getPersonWithId(id).get().getValue();
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String getTagName(UUID id) {
        try {
            return TagDAO.getInstance().getTagWithId(id).get().getValue();
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR";
        }
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

        revalidate();
        repaint();

        meetingMetadataCollapsed = !meetingMetadataCollapsed;
    }

    private Consumer<MeetingViewModel> onFinishCallback;
    private Consumer<MeetingViewModel> onCancelDeleteCallback;

    public void setOnFinishCallback(Consumer<MeetingViewModel> onFinishCallback) {
        this.onFinishCallback = onFinishCallback;
    }

    public void setOnCancelDeleteCallback(Consumer<MeetingViewModel> onCancelDeleteCallback) {
        this.onCancelDeleteCallback = onCancelDeleteCallback;
    }

    private void onFinish() {
        closeChildWindows();
        onFinishCallback.accept(buildViewModel());
    }

    private void onCancelDelete() {
        closeChildWindows();
        onFinishCallback.accept(buildViewModel());
    }

    private void closeChildWindows() {
        openDecisionEditors.keySet().forEach(uuid -> {
            DecisionEditorForm decisionEditorForm = openDecisionEditors.get(uuid);
            if(decisionEditorForm != null) {
                decisionEditorForm.dispose();
            }
        });
        openDecisionEditors.clear();
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

    public boolean validateInput() {
        List<String> errors = new ArrayList<>();
        if(meetingDateTimePicker.getDateTimePermissive() == null) {
            errors.add("Must pick a meeting time!");
        }

        if(tfMeetingTitle.getText().trim().length() == 0) {
            errors.add("Meeting needs a title!");
        }

        if(vsAttendees.getSelectedValues().size() <= 1) {
            errors.add("Meetings need at least 2 attendees!");
        }

        if(vsTags.getSelectedValues().size() == 0) {
            errors.add("Choose at least one tag for this meeting!");
        }

        if(errors.size() > 0) {
            JOptionPane.showMessageDialog(this, "Validation errors: " +
                    errors.stream().collect(Collectors.joining(" --- ")));
            return false;
        }

        return true;
    }

    private MeetingViewModel buildViewModel() {
        MeetingViewModel viewModel = new MeetingViewModel();

        viewModel.setMeetingId(underlyingMeeting.getId());
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

        viewModel.setDecisions(meetingDecisions);

        System.out.println("buildViewModel() = " + viewModel);

        return viewModel;
    }

    public class MeetingViewModel {

        private UUID meetingId;
        private LocalDateTime meetingDateTime;
        private String meetingTitle;
        private java.util.List<Person> attendees;
        private java.util.List<Tag> tags;
        private java.util.List<Decision> decisions;

        public UUID getMeetingId() {
            return meetingId;
        }

        public MeetingViewModel setMeetingId(UUID meetingId) {
            this.meetingId = meetingId;
            return this;
        }

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
            sb.append("meetingId=").append(meetingId);
            sb.append(", meetingDateTime=").append(meetingDateTime);
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
