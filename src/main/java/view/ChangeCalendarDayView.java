package view;

import interface_adapter.change_calendar_day.ChangeCalendarDayController;
import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;
import entity.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeCalendarDayView extends JPanel implements ActionListener, PropertyChangeListener {

    private final JFrame frame;
    private final JPanel eventsPanel;
    private final JLabel dateLabel;
    private final JLabel calendarNameLabel;

    // Time slots from 12 AM to 11 PM
    private static final int START_HOUR = 0;
    private static final int END_HOUR = 23;
    private final Map<Integer, JPanel> timeSlots = new HashMap<>();

    private final ChangeCalendarDayViewModel viewModel;
    private ChangeCalendarDayController controller;

    public ChangeCalendarDayView(ChangeCalendarDayViewModel viewModel) {
        this.viewModel = viewModel;

        // Create the frame
        frame = new JFrame("UniCal - Day View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Create the header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 240, 250)); // Light blue background

        // Calendar name label
        calendarNameLabel = new JLabel("Calendar: Not Selected");
        calendarNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        calendarNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(calendarNameLabel, BorderLayout.WEST);

        // Date label
        dateLabel = new JLabel("Select a date");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(dateLabel, BorderLayout.CENTER);

        frame.add(headerPanel, BorderLayout.NORTH);

        // Create the scrollable events panel
        eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));

        // Create the time slots
        createTimeSlots();

        // Add events panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
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

            // Events area for this time slot
            JPanel eventArea = new JPanel();
            eventArea.setLayout(new BoxLayout(eventArea, BoxLayout.Y_AXIS));
            eventArea.setBackground(Color.WHITE);
            timeSlot.add(eventArea, BorderLayout.CENTER);

            eventsPanel.add(timeSlot);
            timeSlots.put(hour, timeSlot);
        }
    }

    public void displayEvents(List<Event> events) {
        createTimeSlots();

        if (events != null) {
            for (Event event : events) {
                LocalTime eventStartTime = event.getStartTime();
                int hour = eventStartTime.getHour();

                if (timeSlots.containsKey(hour)) {
                    JPanel timeSlot = timeSlots.get(hour);
                    JPanel eventArea = (JPanel) timeSlot.getComponent(1); // Get the event area
                    eventArea.add(createEventDisplay(event));
                    eventArea.revalidate();
                }
            }
        }

        eventsPanel.revalidate();
        eventsPanel.repaint();
    }

    private JPanel createEventDisplay(Event event) {
        JPanel eventDisplay = new JPanel(new BorderLayout());

        // Set gradient-like background
        eventDisplay.setBackground(new Color(144, 238, 144)); // Light green
        eventDisplay.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 139, 87), 1), // Dark green border
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        // Event title
        JLabel titleLabel = new JLabel(event.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        eventDisplay.add(titleLabel, BorderLayout.NORTH);

        // Event description
        JLabel descriptionLabel = new JLabel("<html>" + event.getDescription() + "</html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        eventDisplay.add(descriptionLabel, BorderLayout.CENTER);

        eventDisplay.setPreferredSize(new Dimension(200, 50));
        eventDisplay.setMaximumSize(new Dimension(200, 50));

        return eventDisplay;
    }

    public void setController(ChangeCalendarDayController controller) {
        this.controller = controller;
    }

    public void updateView(LocalDate selectedDate, List<Event> events, String calendarName) {
        // Update the date label
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        dateLabel.setText(selectedDate.format(formatter));

        // Update the calendar name
        calendarNameLabel.setText("Calendar: " + calendarName);

        // Display events
        displayEvents(events);
    }

    public ChangeCalendarDayViewModel getViewModel() {
        return viewModel;
    }

    public ChangeCalendarDayController getController() {
        return controller;
    }
}
