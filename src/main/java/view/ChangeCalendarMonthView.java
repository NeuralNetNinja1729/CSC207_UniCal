package view;

import entity.Calendar;
import interface_adapter.change_calendar_day.ChangeCalendarDayController;
import interface_adapter.change_calendar_day.ChangeCalendarDayState;
import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;
import interface_adapter.change_calendar_month.ChangeCalendarMonthController;
import interface_adapter.change_calendar_month.ChangeCalendarMonthState;
import interface_adapter.change_calendar_month.ChangeCalendarMonthViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChangeCalendarMonthView extends JPanel implements ActionListener, PropertyChangeListener {

    private JFrame frame;
    private JPanel calendarPanel;
    private JButton googleButton;
    private JButton outlookButton;
    private JButton notionButton;
    private JComboBox<Month> monthSelector;
    private JComboBox<Integer> yearSelector;

    private final String viewName = "calendar month";
    private final ChangeCalendarMonthViewModel viewModel;
    private ChangeCalendarMonthController monthController;
    private ChangeCalendarDayController dayController;

    public ChangeCalendarMonthView(ChangeCalendarMonthViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        // Create the frame
        this.frame = new JFrame("UniCal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Create the side panel for buttons
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(5, 1, 10, 10));
        sidePanel.setPreferredSize(new Dimension(200, 0));
        sidePanel.setBackground(new Color(68, 168, 167));

        googleButton = new JButton("Google");
        outlookButton = new JButton("Outlook");
        notionButton = new JButton("Notion");
        JButton addEventButton = new JButton("Add Event");
        JButton logoutButton = new JButton("Logout");

        googleButton.addActionListener(evt -> handleCalendarSelection("Google"));
        outlookButton.addActionListener(evt -> handleCalendarSelection("Outlook"));
        notionButton.addActionListener(evt -> handleCalendarSelection("Notion"));

        sidePanel.add(googleButton);
        sidePanel.add(outlookButton);
        sidePanel.add(notionButton);
        sidePanel.add(addEventButton);
        sidePanel.add(logoutButton);

        frame.add(sidePanel, BorderLayout.WEST);

        // Create the month and year selector
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        monthSelector = new JComboBox<>(Month.values());
        yearSelector = new JComboBox<>();
        for (int year = 2020; year <= 2030; year++) {
            yearSelector.addItem(year);
        }

        monthSelector.addActionListener(e -> updateCalendar());
        yearSelector.addActionListener(e -> updateCalendar());

        topPanel.add(new JLabel("Month:"));
        topPanel.add(monthSelector);
        topPanel.add(new JLabel("Year:"));
        topPanel.add(yearSelector);

        frame.add(topPanel, BorderLayout.NORTH);

        // Create the panel to display the calendar
        calendarPanel = new JPanel();
        calendarPanel.setLayout(new GridLayout(0, 7, 5, 5));
        calendarPanel.setBackground(Color.WHITE);
        frame.add(calendarPanel, BorderLayout.CENTER);

        // Initialize the calendar view
        updateCalendar();

        // Make the frame visible
        frame.setVisible(true);
    }

    public void setDayController(ChangeCalendarDayController dayController) {
        this.dayController = dayController;
    }

    private void handleCalendarSelection(String calendarType) {
        ChangeCalendarMonthState state = viewModel.getState();

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
        state.setCurrMonth(selectedMonth.getDisplayName(TextStyle.FULL, Locale.getDefault()));
        state.setCurrYear(selectedYear);

        monthController.execute(state.getCurrCalendarList(), state.getCurrMonth(), state.getCurrYear());
    }

    private void updateCalendar() {
        calendarPanel.removeAll();

        // Retrieve events from the state
        ChangeCalendarMonthState state = this.viewModel.getState();
        Map<String, String> eventsByDate = state.getEventMap();

        // List of days from Sunday to Saturday
        List<DayOfWeek> daysOfWeek = List.of(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);

        // Add day labels
        for (DayOfWeek day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day.getDisplayName(TextStyle.SHORT, Locale.getDefault()), SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
            dayLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            calendarPanel.add(dayLabel);
        }

        // Fetch the selected month and year
        Month selectedMonth = (Month) monthSelector.getSelectedItem();
        Integer selectedYear = (Integer) yearSelector.getSelectedItem();

        // Calculate days in the month and the starting day of the week
        LocalDate firstDayOfMonth = LocalDate.of(selectedYear, selectedMonth, 1);
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // Monday = 1
        int daysInMonth = selectedMonth.length(Year.isLeap(selectedYear));

        // Adjust first day for Sunday start
        int adjustedFirstDayOfWeek = (firstDayOfWeek == 7) ? 0 : firstDayOfWeek;

        // Fill empty cells before the first day
        for (int i = 0; i < adjustedFirstDayOfWeek; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // Fill in the days of the month with events
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = LocalDate.of(selectedYear, selectedMonth, day);
            String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Create a JButton for the day
            JButton dayButton = new JButton();
            dayButton.setLayout(new BorderLayout());
            dayButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // Add the day number
            JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
            dayButton.add(dayLabel, BorderLayout.NORTH);

            // Display events for the day
            JPanel eventPanel = new JPanel();
            eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
            if (eventsByDate.containsKey(formattedDate)) {
                String[] events = eventsByDate.get(formattedDate).split("\n");
                for (String event : events) {
                    JLabel eventLabel = new JLabel(event);
                    eventLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                    eventPanel.add(eventLabel);
                }
            } else {
                eventPanel.add(new JLabel("No Events"));
            }
            dayButton.add(eventPanel, BorderLayout.CENTER);

            // Add an ActionListener to handle day selection
            Integer currDay = day;
            dayButton.addActionListener(evt -> handleDaySelection(selectedYear, selectedMonth, currDay));

            calendarPanel.add(dayButton);
        }

        // Refresh the calendar panel
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void handleDaySelection(Integer year, Month month, Integer day) {
        if (dayController == null) {
            throw new NullPointerException("Day controller is not set. Make sure to initialize the controller.");
        }

        ChangeCalendarMonthState state = viewModel.getState();
        ChangeCalendarDayState dayState = new ChangeCalendarDayState();

        state.setCurrMonth(month.getDisplayName(TextStyle.FULL, Locale.getDefault()));
        state.setCurrYear(year);

        dayState.setCurrCalendarList(state.getCurrCalendarList());
        dayState.setCurrMonth(month.getDisplayName(TextStyle.FULL, Locale.getDefault()));
        dayState.setCurrYear(year);
        dayState.setCurrDay(day);

        dayController.execute(dayState.getCurrCalendarList(), dayState.getCurrMonth(), dayState.getCurrDay(),
                dayState.getCurrYear());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Click " + e.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ChangeCalendarMonthState state = (ChangeCalendarMonthState) evt.getNewValue();
        if (state.getEventMap() != null) {
            updateCalendar(); // Calls the existing method to refresh the calendar view
        }
    }

    public void setChangeCalendarMonthController(ChangeCalendarMonthController monthController) {
        this.monthController = monthController;
    }

    public ChangeCalendarDayController getDayController() {
        return this.dayController;
    }

    public String getViewName() {
        return viewName;
    }

    public void setChangeCalendarDayController(ChangeCalendarDayController dayController) {
    }
}
