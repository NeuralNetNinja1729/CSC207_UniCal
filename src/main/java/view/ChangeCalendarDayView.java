package view;

import interface_adapter.add_event.AddEventController;
import interface_adapter.add_event.AddEventState;
import interface_adapter.change_calendar_day.ChangeCalendarDayController;
import interface_adapter.change_calendar_day.ChangeCalendarDayState;
import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import entity.Event;

public class ChangeCalendarDayView extends JPanel implements ActionListener, PropertyChangeListener {

    private JFrame frame;
    private JPanel eventsPanel;
    private JButton googleButton;
    private JButton outlookButton;
    private JButton notionButton;
    private JButton addEventButton;
    private JButton deleteEventButton;
    private JComboBox<Month> monthSelector;
    private JComboBox<Integer> yearSelector;
    private JComboBox<Integer> daySelector;
    private JList<String> eventsList;

    private final String viewName = "calendar day";
    private final ChangeCalendarDayViewModel viewModel;
    private ChangeCalendarDayController controller;
    private AddEventController addEventController;

    public ChangeCalendarDayView(ChangeCalendarDayViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        // Create the frame
        this.frame = new JFrame("UniCal - Day View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Create the side panel for buttons
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(6, 1, 10, 10));
        sidePanel.setPreferredSize(new Dimension(200, 0));
        sidePanel.setBackground(new Color(68, 168, 167));

        googleButton = new JButton("Google");
        outlookButton = new JButton("Outlook");
        notionButton = new JButton("Notion");
        addEventButton = new JButton("Add Event");
        deleteEventButton = new JButton("Delete Event");
        JButton logoutButton = new JButton("Logout");

        googleButton.addActionListener(evt -> handleCalendarSelection("Google"));
        outlookButton.addActionListener(evt -> handleCalendarSelection("Outlook"));
        notionButton.addActionListener(evt -> handleCalendarSelection("Notion"));
        addEventButton.addActionListener(evt -> openAddEventPopup());
        deleteEventButton.addActionListener(evt -> deleteSelectedEvent());

        sidePanel.add(googleButton);
        sidePanel.add(outlookButton);
        sidePanel.add(notionButton);
        sidePanel.add(addEventButton);
        sidePanel.add(deleteEventButton);
        sidePanel.add(logoutButton);

        frame.add(sidePanel, BorderLayout.WEST);

        // Create the month, year, and day selector
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        monthSelector = new JComboBox<>(Month.values());
        yearSelector = new JComboBox<>();
        for (int year = 2020; year <= 2030; year++) {
            yearSelector.addItem(year);
        }
        daySelector = new JComboBox<>();
        populateDaySelector();

        monthSelector.addActionListener(e -> populateDaySelector());
        yearSelector.addActionListener(e -> populateDaySelector());
        daySelector.addActionListener(e -> updateDayEvents());

        topPanel.add(new JLabel("Month:"));
        topPanel.add(monthSelector);
        topPanel.add(new JLabel("Year:"));
        topPanel.add(yearSelector);
        topPanel.add(new JLabel("Day:"));
        topPanel.add(daySelector);

        frame.add(topPanel, BorderLayout.NORTH);

        // Create the panel to display events for the selected day
        eventsPanel = new JPanel();
        eventsPanel.setLayout(new BorderLayout());
        eventsPanel.setBackground(Color.WHITE);

        eventsList = new JList<>();
        eventsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(eventsList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        eventsPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(eventsPanel, BorderLayout.CENTER);

        // Initialize the day view
        updateDayEvents();

        // Make the frame visible
        frame.setVisible(true);
    }

    public void setController(ChangeCalendarDayController controller) {
        this.controller = controller;
    }

    private void handleCalendarSelection(String calendarType) {
        ChangeCalendarDayState state = this.viewModel.getState();

        switch (calendarType) {
            case "Google":
                state.setCurrCalendarList(List.of(state.getGoogleCalendar()));
                break;
            case "Outlook":
                state.setCurrCalendarList(List.of(state.getOutlookCalendar()));
                break;
            case "Notion":
                state.setCurrCalendarList(List.of(state.getNotionCalendar()));
                break;
        }

        Month selectedMonth = (Month) monthSelector.getSelectedItem();
        Integer selectedYear = (Integer) yearSelector.getSelectedItem();
        Integer selectedDay = (Integer) daySelector.getSelectedItem();
        state.setCurrMonth(selectedMonth.getDisplayName(TextStyle.FULL, Locale.getDefault()));
        state.setCurrYear(selectedYear);
        state.setCurrDay(selectedDay);
        controller.execute(state.getCurrCalendarList(), state.getCurrMonth(),
                state.getCurrDay(), state.getCurrYear());
    }

    private void populateDaySelector() {
        Month selectedMonth = (Month) monthSelector.getSelectedItem();
        Integer selectedYear = (Integer) yearSelector.getSelectedItem();
        if (selectedMonth == null || selectedYear == null) return;

        int daysInMonth = selectedMonth.length(java.time.Year.isLeap(selectedYear));
        daySelector.removeAllItems();
        for (int day = 1; day <= daysInMonth; day++) {
            daySelector.addItem(day);
        }

        updateDayEvents();
    }

    private void updateDayEvents() {
        // Retrieve events from the state
        ChangeCalendarDayState state = viewModel.getState();
        Map<String, String> eventsByHour = state.getEventMap();

        DefaultListModel<String> listModel = new DefaultListModel<>();
        if (eventsByHour != null) {
            for (Map.Entry<String, String> entry : eventsByHour.entrySet()) {
                String time = entry.getKey();
                String eventDetails = entry.getValue();
                listModel.addElement(time + " - " + eventDetails.trim());
            }
        } else {
            listModel.addElement("No events for the selected day");
        }
        eventsList.setModel(listModel);
    }

    private void deleteSelectedEvent() {
        String selectedEvent = eventsList.getSelectedValue();
        if (selectedEvent == null || selectedEvent.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please select an event to delete.", "No Event Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Extract event details from the selected event string
        String[] parts = selectedEvent.split(" - ", 2);
        if (parts.length < 2) {
            JOptionPane.showMessageDialog(frame, "Unable to extract event details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String time = parts[0];
        String eventDetails = parts[1];

        // Extract event name and calendar API name from event details
        String[] detailsParts = eventDetails.split("\n");
        if (detailsParts.length < 2) {
            JOptionPane.showMessageDialog(frame, "Unable to extract event details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String eventName = detailsParts[0].trim();
        String calendarApiName = detailsParts[1].replaceAll("[()]", "").trim();

        // Create an Event object from the extracted details (assuming Event has the necessary constructor)
        LocalDate selectedDate = LocalDate.of((Integer) yearSelector.getSelectedItem(), (Month) monthSelector.getSelectedItem(), (Integer) daySelector.getSelectedItem());
        Event eventToDelete;
        switch (calendarApiName) {
            case "Google":
                eventToDelete = new Event(eventName, selectedDate, this.viewModel.getState().getGoogleCalendar());
                break;
            case "Notion":
                eventToDelete = new Event(eventName, selectedDate, this.viewModel.getState().getNotionCalendar());
                break;
            case "Outlook":
                eventToDelete = new Event(eventName, selectedDate, this.viewModel.getState().getOutlookCalendar());
                break;
        }

        // TODO: Call the use case to delete the event using the eventToDelete object

    }

    private void openAddEventPopup() {
        // Create a pop-up dialog for adding an event
        JDialog dialog = new JDialog(frame, "Add Event", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));

        // Event name
        JLabel nameLabel = new JLabel("Event Name:");
        JTextField nameField = new JTextField();

        // Year dropdown
        JLabel yearLabel = new JLabel("Year:");
        JComboBox<Integer> yearDropdown = new JComboBox<>();
        for (int year = 2020; year <= 2030; year++) {
            yearDropdown.addItem(year);
        }

        // Month dropdown
        JLabel monthLabel = new JLabel("Month:");
        JComboBox<Month> monthDropdown = new JComboBox<>(Month.values());

        // Day dropdown
        JLabel dayLabel = new JLabel("Day:");
        JComboBox<Integer> dayDropdown = new JComboBox<>();
        populateDayDropdown(yearDropdown, monthDropdown, dayDropdown);

        // Add action listeners to update days when year or month changes
        yearDropdown.addActionListener(e -> populateDayDropdown(yearDropdown, monthDropdown, dayDropdown));
        monthDropdown.addActionListener(e -> populateDayDropdown(yearDropdown, monthDropdown, dayDropdown));

        // Description field
        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField();

        // Calendar selector dropdown
        JLabel calendarLabel = new JLabel("Calendar:");
        JComboBox<String> calendarDropdown = new JComboBox<>(new String[]{"Google", "Notion", "Outlook"});

        // Save and Cancel buttons
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        // Save button action
        saveButton.addActionListener(e -> {
            String eventName = nameField.getText();
            Integer selectedYear = (Integer) yearDropdown.getSelectedItem();
            Month selectedMonth = (Month) monthDropdown.getSelectedItem();
            Integer selectedDay = (Integer) dayDropdown.getSelectedItem();
            // String description = descriptionField.getText();
            String selectedCalendar = (String) calendarDropdown.getSelectedItem();

            if (!eventName.isEmpty() && selectedYear != null && selectedMonth != null && selectedDay != null) {
                // Add the event using the controller
                AddEventState state = new AddEventState();
                state.setCurrMonth(selectedMonth.getDisplayName(TextStyle.FULL, Locale.getDefault()));
                state.setCurrYear(selectedYear);
                state.setCurrDay(selectedDay);
                switch (selectedCalendar) {
                    case "Google":
                        state.setCurrCalendar(this.viewModel.getState().getGoogleCalendar());
                        break;
                    case "Notion":
                        state.setCurrCalendar(this.viewModel.getState().getNotionCalendar());
                        break;
                    case "Outlook":
                        state.setCurrCalendar(this.viewModel.getState().getOutlookCalendar());
                        break;
                    default:
                        break;
                }
                LocalDate localDate = LocalDate.of(selectedYear, selectedMonth, selectedDay);
                state.setEvent(new Event(eventName, localDate, state.getCurrCalendar()));

                // Send data to the controller
                addEventController.execute(state.getEvent().getEventName(), state.getCurrMonth(), state.getCurrYear(),
                        state.getCurrDay(), state.getCurrCalendar());

                dialog.dispose();
                updateDayEvents();
            } else {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel button action
        cancelButton.addActionListener(e -> dialog.dispose());

        // Add components to the dialog
        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(yearLabel);
        dialog.add(yearDropdown);
        dialog.add(monthLabel);
        dialog.add(monthDropdown);
        dialog.add(dayLabel);
        dialog.add(dayDropdown);
        dialog.add(descriptionLabel);
        dialog.add(descriptionField);
        dialog.add(calendarLabel);
        dialog.add(calendarDropdown);
        dialog.add(saveButton);
        dialog.add(cancelButton);

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void populateDayDropdown(JComboBox<Integer> yearDropdown, JComboBox<Month> monthDropdown, JComboBox<Integer> dayDropdown) {
        Month selectedMonth = (Month) monthDropdown.getSelectedItem();
        Integer selectedYear = (Integer) yearDropdown.getSelectedItem();

        if (selectedMonth == null || selectedYear == null) return;

        int daysInMonth = selectedMonth.length(java.time.Year.isLeap(selectedYear));
        dayDropdown.removeAllItems();
        for (int day = 1; day <= daysInMonth; day++) {
            dayDropdown.addItem(day);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Click " + e.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ChangeCalendarDayState state = (ChangeCalendarDayState) evt.getNewValue();
        if (state.getEventMap() != null) {
            updateDayEvents();
        }
    }

    public String getViewName() {
        return viewName;
    }

    public AddEventController getAddEventController() {
        return addEventController;
    }

    public void setAddEventController(AddEventController addEventController) {
        this.addEventController = addEventController;
    }
}
