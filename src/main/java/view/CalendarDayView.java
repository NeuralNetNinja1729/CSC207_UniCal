package view;

import interface_adapter.change_calendar_day.ChangeCalendarDayController;
import interface_adapter.change_calendar_day.ChangeCalendarDayState;
import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;
import entity.Event;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CalendarDayView extends JPanel implements PropertyChangeListener {
    private final String viewName = "calendar day";
    private final ChangeCalendarDayViewModel changeCalendarDayViewModel;
    private ChangeCalendarDayController changeCalendarDayController;

    // UI Components
    private final JPanel eventsPanel;
    private final JLabel dateLabel;
    private final JPanel headerPanel;
    private final JLabel calendarNameLabel;

    // Time slots from 12 AM to 11 PM
    private static final int START_HOUR = 0;
    private static final int END_HOUR = 23;
    private final Map<Integer, JPanel> timeSlots = new HashMap<>();

    public CalendarDayView(ChangeCalendarDayViewModel viewModel) {
        this.changeCalendarDayViewModel = viewModel;
        this.changeCalendarDayViewModel.addPropertyChangeListener(this);

        // Set up the main layout
        setLayout(new BorderLayout());

        // Create header panel
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 240, 250)); // Light blue background

        // Create calendar name label
        calendarNameLabel = new JLabel("Calendar: Not Selected");
        calendarNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        calendarNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(calendarNameLabel, BorderLayout.WEST);

        // Create date label
        dateLabel = new JLabel("Select a date");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(dateLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Create scrollable events panel
        eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));

        // Create the time slots
        createTimeSlots();

        // Add events panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createTimeSlots() {
        eventsPanel.removeAll();
        timeSlots.clear();

        for (int hour = START_HOUR; hour <= END_HOUR; hour++) {
            JPanel timeSlot = new JPanel(new BorderLayout());
            timeSlot.setPreferredSize(new Dimension(eventsPanel.getWidth(), 60));
            timeSlot.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            // Time label panel (left side)
            JPanel timeLabelPanel = new JPanel(new BorderLayout());
            timeLabelPanel.setPreferredSize(new Dimension(80, 60));
            timeLabelPanel.setBackground(new Color(245, 245, 245));

            // Format time string
            String timeText = String.format("%02d:00", hour);
            JLabel timeLabel = new JLabel(timeText);
            timeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            timeLabel.setForeground(new Color(100, 100, 100));
            timeLabelPanel.add(timeLabel, BorderLayout.CENTER);

            timeSlot.add(timeLabelPanel, BorderLayout.WEST);

            // Events area for this time slot (stretches to fill space)
            JPanel eventArea = new JPanel();
            eventArea.setLayout(new BoxLayout(eventArea, BoxLayout.X_AXIS));
            eventArea.setBackground(Color.WHITE);
            timeSlot.add(eventArea, BorderLayout.CENTER);

            eventsPanel.add(timeSlot);
            timeSlots.put(hour, timeSlot);
        }
    }

    public void displayEvents(ArrayList<Event> events) {
        // Clear all existing events
        createTimeSlots();

        if (events != null) {
            for (Event event : events) {
                // Get the hour for the event
                LocalTime eventTime = event.getDate().atStartOfDay().toLocalTime();
                int hour = eventTime.getHour();

                // Add event to the appropriate time slot
                if (timeSlots.containsKey(hour)) {
                    JPanel timeSlot = timeSlots.get(hour);
                    JPanel eventArea = (JPanel) timeSlot.getComponent(1); // Get the event area
                    eventArea.add(createEventDisplay(event));
                    eventArea.revalidate();
                }
            }
        }

        revalidate();
        repaint();
    }

    private JPanel createEventDisplay(Event event) {
        JPanel eventDisplay = new JPanel();
        eventDisplay.setLayout(new BorderLayout());

        // Set gradient-like background
        eventDisplay.setBackground(new Color(144, 238, 144)); // Light green
        eventDisplay.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 139, 87), 1), // Dark green border
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        // Event title
        JLabel titleLabel = new JLabel(event.getEventName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        eventDisplay.add(titleLabel, BorderLayout.CENTER);

        // Set preferred size for consistent event display
        eventDisplay.setPreferredSize(new Dimension(200, 50));
        eventDisplay.setMaximumSize(new Dimension(200, 50));

        return eventDisplay;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ChangeCalendarDayState state = (ChangeCalendarDayState) evt.getNewValue();

        // Update calendar name if available
        if (state.getCurrentCalendar() != null) {
            calendarNameLabel.setText("Calendar: " + state.getCurrentCalendar().getCalendarName());
        }

        // Update date display if available
        if (state.getSelectedDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
            dateLabel.setText(state.getSelectedDate().format(formatter));
        }

        // Update events
        displayEvents(state.getEvents());

        // Handle any errors
        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this,
                    state.getError(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setController(ChangeCalendarDayController controller) {
        this.changeCalendarDayController = controller;
    }

    public ChangeCalendarDayController getController() {
        return changeCalendarDayController;
    }

    // Test method to directly add events
    public void setTestEvents(ArrayList<Event> events) {
        displayEvents(events);
    }
}