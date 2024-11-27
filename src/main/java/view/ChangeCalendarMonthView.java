package view;

import entity.Calendar;
import entity.Event;
import interface_adapter.change_calendar_month.ChangeCalendarMonthController;
import interface_adapter.change_calendar_month.ChangeCalendarMonthState;
import interface_adapter.change_calendar_month.ChangeCalendarMonthViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeCalendarMonthView extends JPanel implements PropertyChangeListener {
  private final JPanel calendarPanel;
  private final JComboBox<Month> monthSelector;
  private final JComboBox<Integer> yearSelector;
  private final JButton googleButton;
  private final JButton notionButton;
  private final JButton outlookButton;
  private final ChangeCalendarMonthViewModel changeCalendarMonthViewModel;
  private ChangeCalendarMonthController changeCalendarMonthController;
  private DayViewOpener dayViewOpener;
  private static final Color ACTIVE_CALENDAR_COLOR = new Color(200, 200, 255);
  private static final Color EVENT_DAY_COLOR = new Color(255, 220, 220);

  public interface DayViewOpener {
    void openDayView(LocalDate date);
  }

  public void setDayViewOpener(DayViewOpener opener) {
    this.dayViewOpener = opener;
  }

  public ChangeCalendarMonthView(ChangeCalendarMonthViewModel viewModel) {
    this.changeCalendarMonthViewModel = viewModel;
    this.changeCalendarMonthViewModel.addPropertyChangeListener(this);

    setLayout(new BorderLayout());

    // Create side panel for calendar selection
    JPanel sidePanel = new JPanel();
    sidePanel.setLayout(new GridLayout(5, 1, 10, 10));
    sidePanel.setPreferredSize(new Dimension(200, 0));
    sidePanel.setBackground(new Color(68, 168, 167));

    googleButton = new JButton("Google Calendar");
    notionButton = new JButton("Notion Calendar");
    outlookButton = new JButton("Outlook Calendar");

    googleButton.addActionListener(e -> handleGoogleCalendarClick());
    notionButton.addActionListener(e -> handleNotionCalendarClick());
    outlookButton.addActionListener(e -> handleOutlookCalendarClick());

    sidePanel.add(googleButton);
    sidePanel.add(notionButton);
    sidePanel.add(outlookButton);
    add(sidePanel, BorderLayout.WEST);

    // Create top panel for month/year selection
    JPanel topPanel = new JPanel(new FlowLayout());
    monthSelector = new JComboBox<>(Month.values());
    yearSelector = new JComboBox<>();
    for (int year = 2020; year <= 2030; year++) {
      yearSelector.addItem(year);
    }

    // Set current month and year
    LocalDate now = LocalDate.now();
    monthSelector.setSelectedItem(now.getMonth());
    yearSelector.setSelectedItem(now.getYear());

    monthSelector.addActionListener(e -> updateCalendarView());
    yearSelector.addActionListener(e -> updateCalendarView());

    topPanel.add(new JLabel("Month:"));
    topPanel.add(monthSelector);
    topPanel.add(new JLabel("Year:"));
    topPanel.add(yearSelector);
    add(topPanel, BorderLayout.NORTH);

    // Create calendar panel
    calendarPanel = new JPanel();
    calendarPanel.setLayout(new GridLayout(0, 7, 5, 5));
    calendarPanel.setBackground(Color.WHITE);
    add(calendarPanel, BorderLayout.CENTER);

    updateCalendarView();
  }

  private void updateButtonColors(Calendar activeCalendar) {
    // Reset all buttons
    googleButton.setBackground(UIManager.getColor("Button.background"));
    notionButton.setBackground(UIManager.getColor("Button.background"));
    outlookButton.setBackground(UIManager.getColor("Button.background"));

    // Highlight active calendar button
    if (activeCalendar != null) {
      switch (activeCalendar.getCalendarApiName()) {
        case "GoogleCalendar":
          googleButton.setBackground(ACTIVE_CALENDAR_COLOR);
          break;
        case "NotionCalendar":
          notionButton.setBackground(ACTIVE_CALENDAR_COLOR);
          break;
        case "OutlookCalendar":
          outlookButton.setBackground(ACTIVE_CALENDAR_COLOR);
          break;
      }
    }
  }

  private void handleCalendarClick(Calendar calendar, String calendarType) {
    if (calendar != null) {
      ChangeCalendarMonthState currentState = changeCalendarMonthViewModel.getState();
      currentState.setActiveCalendar(calendar);
      List<Calendar> calList = new ArrayList<>();
      calList.add(calendar);
      currentState.setCurrCalendarList(calList);

      Month selectedMonth = (Month) monthSelector.getSelectedItem();
      Integer selectedYear = (Integer) yearSelector.getSelectedItem();
      String monthName = selectedMonth.getDisplayName(TextStyle.FULL, Locale.getDefault());

      currentState.setCurrMonth(monthName);
      currentState.setCurrYear(selectedYear);

      // Update UI
      updateButtonColors(calendar);
      changeCalendarMonthController.execute(calList, monthName, selectedYear);
    } else {
      JOptionPane.showMessageDialog(this,
        calendarType + " Calendar not initialized",
        "Error",
        JOptionPane.ERROR_MESSAGE);
    }
  }

  private void handleGoogleCalendarClick() {
    ChangeCalendarMonthState currentState = changeCalendarMonthViewModel.getState();
    handleCalendarClick(currentState.getGoogleCalendar(), "Google");
  }

  private void handleNotionCalendarClick() {
    ChangeCalendarMonthState currentState = changeCalendarMonthViewModel.getState();
    handleCalendarClick(currentState.getNotionCalendar(), "Notion");
  }

  private void handleOutlookCalendarClick() {
    ChangeCalendarMonthState currentState = changeCalendarMonthViewModel.getState();
    handleCalendarClick(currentState.getOutlookCalendar(), "Outlook");
  }

  private Set<LocalDate> getDatesWithEvents() {
    ChangeCalendarMonthState currentState = changeCalendarMonthViewModel.getState();
    if (currentState != null && currentState.getCurrCalendarList() != null) {
      List<Event> events = new ArrayList<>();
      // Here you would get the events from your state
      // For now, returning an empty set
      return events.stream()
        .map(Event::getDate)
        .collect(Collectors.toSet());
    }
    return new HashSet<>();
  }

  private void updateCalendarView() {
    calendarPanel.removeAll();
    Set<LocalDate> datesWithEvents = getDatesWithEvents();

    // Add day of week headers
    String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    for (String dayName : dayNames) {
      JLabel dayLabel = new JLabel(dayName, SwingConstants.CENTER);
      dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
      dayLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
      calendarPanel.add(dayLabel);
    }

    Month selectedMonth = (Month) monthSelector.getSelectedItem();
    int selectedYear = (Integer) yearSelector.getSelectedItem();

    LocalDate firstDayOfMonth = LocalDate.of(selectedYear, selectedMonth, 1);
    int monthLength = selectedMonth.length(Year.isLeap(selectedYear));
    int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7;

    // Add empty cells before first day
    for (int i = 0; i < firstDayOfWeek; i++) {
      JPanel emptyCell = new JPanel();
      emptyCell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
      calendarPanel.add(emptyCell);
    }

    // Add day cells
    for (int day = 1; day <= monthLength; day++) {
      final LocalDate cellDate = LocalDate.of(selectedYear, selectedMonth, day);
      JButton dayButton = new JButton(String.valueOf(day));
      dayButton.setOpaque(true);

      // Set background color based on conditions
      if (datesWithEvents.contains(cellDate)) {
        dayButton.setBackground(new Color(255, 220, 220));
      } else if (cellDate.equals(LocalDate.now())) {
        dayButton.setBackground(new Color(230, 230, 255));
        dayButton.setFont(dayButton.getFont().deriveFont(Font.BOLD));
      } else {
        dayButton.setBackground(Color.WHITE);
      }

      dayButton.addActionListener(e -> {
        if (dayViewOpener != null) {
          dayViewOpener.openDayView(cellDate);
        }
      });

      dayButton.setBorderPainted(true);
      dayButton.setFocusPainted(false);
      dayButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
      calendarPanel.add(dayButton);
    }

    // Add empty cells for remaining grid spaces
    int totalCells = 42;
    int remainingCells = totalCells - (monthLength + firstDayOfWeek);
    for (int i = 0; i < remainingCells; i++) {
      JPanel emptyCell = new JPanel();
      emptyCell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
      calendarPanel.add(emptyCell);
    }

    calendarPanel.revalidate();
    calendarPanel.repaint();
  }

  public void setChangeCalendarMonthController(ChangeCalendarMonthController controller) {
    this.changeCalendarMonthController = controller;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if ("state".equals(evt.getPropertyName())) {
      ChangeCalendarMonthState state = (ChangeCalendarMonthState) evt.getNewValue();
      if (state != null) {
        updateButtonColors(state.getActiveCalendar());
        updateCalendarView();
      }
    }
  }
}
