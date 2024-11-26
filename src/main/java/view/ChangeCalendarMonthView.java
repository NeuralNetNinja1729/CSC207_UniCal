package view;

import entity.Calendar;
import interface_adapter.change_calendar_month.ChangeCalendarMonthController;
import interface_adapter.change_calendar_month.ChangeCalendarMonthState;
import interface_adapter.change_calendar_month.ChangeCalendarMonthViewModel;
import interface_adapter.login.LoginState;

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
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChangeCalendarMonthView extends JPanel implements ActionListener, PropertyChangeListener {
    private JFrame frame;
    private JPanel calendarPanel;
    private JButton googleButton;
    private JButton outlookButton;
    private JButton notionButton;
    private JComboBox<Month> monthSelector;
    private JComboBox<Integer> yearSelector;

    private final String viewName = "calendar month";
    private ChangeCalendarMonthController changeCalendarMonthController;
    private ChangeCalendarMonthState changeCalendarMonthState;
    private ChangeCalendarMonthViewModel changeCalendarMonthViewModel;


    public ChangeCalendarMonthView(ChangeCalendarMonthViewModel changeCalendarMonthViewModel) {
        this.changeCalendarMonthViewModel = changeCalendarMonthViewModel;
        this.changeCalendarMonthViewModel.addPropertyChangeListener(this);

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

        googleButton.addActionListener(
                // This creates an anonymous subclass of ActionListener and instantiates it.
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(googleButton)) {
                            final ChangeCalendarMonthState currentState = new ChangeCalendarMonthState();
                            List<Calendar> calList = new ArrayList<>();
                            calList.add(currentState.getGoogleCalendar());
                            currentState.setCurrCalendarList(calList);
                            Month selectedMonth = (Month) monthSelector.getSelectedItem();
                            Integer selectedYear = (Integer) yearSelector.getSelectedItem();
                            String monthName = selectedMonth.getDisplayName(TextStyle.FULL, Locale.getDefault());
                            currentState.setCurrMonth(monthName);
                            currentState.setCurrYear(selectedYear);
                            changeCalendarMonthController.execute(currentState.getCurrCalendarList(),
                                    currentState.getCurrMonth(), currentState.getCurrYear());
                        }
                    }
                }
        );



        outlookButton.addActionListener(
                // This creates an anonymous subclass of ActionListener and instantiates it.
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(outlookButton)) {
                            final ChangeCalendarMonthState currentState = new ChangeCalendarMonthState();
                            List<Calendar> calList = new ArrayList<>();
                            calList.add(currentState.getOutlookCalendar());
                            currentState.setCurrCalendarList(calList);
                            Month selectedMonth = (Month) monthSelector.getSelectedItem();
                            Integer selectedYear = (Integer) yearSelector.getSelectedItem();
                            String monthName = selectedMonth.getDisplayName(TextStyle.FULL, Locale.getDefault());
                            currentState.setCurrMonth(monthName);
                            currentState.setCurrYear(selectedYear);
                            changeCalendarMonthController.execute(currentState.getCurrCalendarList(),
                                    currentState.getCurrMonth(), currentState.getCurrYear());
                        }
                    }
                }
        );


        notionButton.addActionListener(
                // This creates an anonymous subclass of ActionListener and instantiates it.
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(notionButton)) {
                            final ChangeCalendarMonthState currentState = new ChangeCalendarMonthState();
                            List<Calendar> calList = new ArrayList<>();
                            calList.add(currentState.getNotionCalendar());
                            currentState.setCurrCalendarList(calList);
                            Month selectedMonth = (Month) monthSelector.getSelectedItem();
                            Integer selectedYear = (Integer) yearSelector.getSelectedItem();
                            String monthName = selectedMonth.getDisplayName(TextStyle.FULL, Locale.getDefault());
                            currentState.setCurrMonth(monthName);
                            currentState.setCurrYear(selectedYear);
                            changeCalendarMonthController.execute(currentState.getCurrCalendarList(),
                                    currentState.getCurrMonth(), currentState.getCurrYear());
                        }
                    }
                }
        );

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

        monthSelector.addActionListener(e -> updateCalendarView());
        yearSelector.addActionListener(e -> updateCalendarView());

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
        updateCalendarView();

        // Make the frame visible
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }


    // Update the calendar view based on the selected month and year
    private void updateCalendarView() {
        calendarPanel.removeAll();

        // List to ensure days are ordered from Sunday to Saturday
        java.util.List<DayOfWeek> daysOfWeek = List.of(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);

        // Add day labels to the first row of the calendar panel
        for (DayOfWeek day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day.getDisplayName(TextStyle.SHORT, Locale.getDefault()), SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
            dayLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            calendarPanel.add(dayLabel);
        }

        Month selectedMonth = (Month) monthSelector.getSelectedItem();
        Integer selectedYear = (Integer) yearSelector.getSelectedItem();

        LocalDate firstDayOfMonth = LocalDate.of(selectedYear, selectedMonth, 1);
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();
        int daysInMonth = selectedMonth.length(Year.isLeap(selectedYear));

        // Adjust for Sunday being the first day of the week
        int adjustedFirstDayOfWeek = (firstDayOfWeek == 7) ? 0 : firstDayOfWeek;

        // Fill in the empty cells before the first day of the month
        for (int i = 0; i < adjustedFirstDayOfWeek; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // Fill in the days of the month
        for (int day = 1; day <= daysInMonth; day++) {
            JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            dayLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            calendarPanel.add(dayLabel);
        }

        // Refresh the calendar panel
        calendarPanel.revalidate();
        calendarPanel.repaint();
        // at this point, we could again call the use case so that changing the month and year
        // automatically refreshes the events
    }

    public ChangeCalendarMonthController getChangeCalendarMonthController() {
        return changeCalendarMonthController;
    }

    public void setChangeCalendarMonthController(ChangeCalendarMonthController changeCalendarMonthController) {
        this.changeCalendarMonthController = changeCalendarMonthController;
    }

    public String getViewName() {
        return viewName;
    }


        public String getName() {
            return this.viewName;
        }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final ChangeCalendarMonthState currentState = (ChangeCalendarMonthState) evt.getSource();
        setFields(currentState);
    }

    private void setFields(ChangeCalendarMonthState state) {
    }
}
