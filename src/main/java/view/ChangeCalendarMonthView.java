package view;

import entity.Calendar;
import entity.Event;
import interface_adapter.change_calendar_month.ChangeCalendarMonthController;
import interface_adapter.change_calendar_month.ChangeCalendarMonthState;
import interface_adapter.change_calendar_month.ChangeCalendarMonthViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;

public class ChangeCalendarMonthView extends JPanel implements ActionListener, PropertyChangeListener {
  public final String viewName = "calendar_month";
  private final ChangeCalendarMonthViewModel changeCalendarMonthViewModel;
  private ChangeCalendarMonthController controller;

  // UI Components
  private JPanel calendarPanel = new JPanel(new GridLayout(0, 7, 2, 2));
  private final JComboBox<Month> monthSelector = new JComboBox<>(Month.values());
  private final JComboBox<Integer> yearSelector = new JComboBox<>();
  private final JButton googleButton = new JButton(ChangeCalendarMonthViewModel.GOOGLE_CALENDAR_BUTTON_LABEL);
  private final JButton notionButton = new JButton(ChangeCalendarMonthViewModel.NOTION_CALENDAR_BUTTON_LABEL);
  private final JButton outlookButton = new JButton(ChangeCalendarMonthViewModel.OUTLOOK_CALENDAR_BUTTON_LABEL);
  private final JLabel errorLabel;

  private DayViewOpener dayViewOpener;
  private static final Color ACTIVE_CALENDAR_COLOR = new Color(200, 200, 255);
  private static final int CELL_HEIGHT = 100;
  private static final int MAX_PREVIEW_EVENTS = 3;

  public interface DayViewOpener {
    void openDayView(LocalDate date);
  }

  public void setDayViewOpener(DayViewOpener opener) {
    this.dayViewOpener = opener;
  }

  public ChangeCalendarMonthView(ChangeCalendarMonthViewModel viewModel) {
    this.changeCalendarMonthViewModel = viewModel;
    this.changeCalendarMonthViewModel.addPropertyChangeListener(this);

    // Layout setup
    setLayout(new BorderLayout());

    // Create error label
    errorLabel = new JLabel();
    errorLabel.setForeground(Color.RED);
    errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

    // Create calendar selection panel
    JPanel sidePanel = createSidePanel();
    add(sidePanel, BorderLayout.WEST);

    // Create month/year selection panel
    JPanel topPanel = createTopPanel();
    add(topPanel, BorderLayout.NORTH);

    // Create calendar panel
    calendarPanel = new JPanel(new GridLayout(0, 7, 2, 2));
    calendarPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    // Create a wrapper panel that includes error label and calendar
    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(errorLabel, BorderLayout.NORTH);
    centerPanel.add(new JScrollPane(calendarPanel), BorderLayout.CENTER);
    add(centerPanel, BorderLayout.CENTER);

    updateCalendarView();
  }

  private void updateCalendarView() {
    calendarPanel.removeAll();
    Map<LocalDate, List<Event>> eventsByDate = getEventsByDate();

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

    // Add day cells with event previews
    for (int day = 1; day <= monthLength; day++) {
      final LocalDate cellDate = LocalDate.of(selectedYear, selectedMonth, day);
      JPanel dayCell = createDayCell(cellDate, eventsByDate.get(cellDate));
      calendarPanel.add(dayCell);
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

  private JPanel createDayCell(LocalDate date, List<Event> events) {
    JPanel cellPanel = new JPanel();
    cellPanel.setLayout(new BoxLayout(cellPanel, BoxLayout.Y_AXIS));
    cellPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    cellPanel.setBackground(Color.WHITE);
    cellPanel.setPreferredSize(new Dimension(0, CELL_HEIGHT));

    // Day number at the top
    JPanel dayHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
    dayHeader.setOpaque(false);
    JLabel dayLabel = new JLabel(String.valueOf(date.getDayOfMonth()));
    dayLabel.setFont(new Font("Arial", date.equals(LocalDate.now()) ? Font.BOLD : Font.PLAIN, 14));
    dayHeader.add(dayLabel);
    cellPanel.add(dayHeader);

    // Add events if they exist
    if (events != null && !events.isEmpty()) {
      cellPanel.setBackground(new Color(255, 245, 245));

      int previewCount = Math.min(events.size(), MAX_PREVIEW_EVENTS);
      for (int i = 0; i < previewCount; i++) {
        Event event = events.get(i);
        JLabel eventLabel = new JLabel("• " + truncateText(event.getEventName(), 20));
        eventLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        eventLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
        cellPanel.add(eventLabel);
      }

      if (events.size() > MAX_PREVIEW_EVENTS) {
        JLabel moreLabel = new JLabel("+" + (events.size() - MAX_PREVIEW_EVENTS) + " more");
        moreLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        moreLabel.setForeground(Color.GRAY);
        moreLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
        cellPanel.add(moreLabel);
      }
    }

    // Make the cell clickable
    cellPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (dayViewOpener != null) {
          System.out.println("Day clicked: " + date); // Debug print
          dayViewOpener.openDayView(date);
        }
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        cellPanel.setBackground(new Color(230, 230, 250));
      }

      @Override
      public void mouseExited(MouseEvent e) {
        cellPanel.setBackground(events != null && !events.isEmpty() ?
                new Color(255, 245, 245) : Color.WHITE);
      }
    });

    return cellPanel;
  }

  private String truncateText(String text, int maxLength) {
    if (text.length() <= maxLength) {
      return text;
    }
    return text.substring(0, maxLength - 3) + "...";
  }

  private Map<LocalDate, List<Event>> getEventsByDate() {
    Map<LocalDate, List<Event>> eventMap = new HashMap<>();
    ChangeCalendarMonthState state = changeCalendarMonthViewModel.getState();

    if (state != null && state.getCurrEvents() != null) {
      for (Event event : state.getCurrEvents()) {
        eventMap.computeIfAbsent(event.getDate(), k -> new ArrayList<>()).add(event);
      }
    }
    return eventMap;
  }

  private void updateButtonColors(Calendar activeCalendar) {
    googleButton.setBackground(UIManager.getColor("Button.background"));
    notionButton.setBackground(UIManager.getColor("Button.background"));
    outlookButton.setBackground(UIManager.getColor("Button.background"));

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

  private JPanel createSidePanel() {
    JPanel sidePanel = new JPanel();
    sidePanel.setLayout(new GridLayout(5, 1, 10, 10));
    sidePanel.setPreferredSize(new Dimension(200, 0));
    sidePanel.setBackground(new Color(68, 168, 167));

    // Just add action listeners to the already initialized buttons
    googleButton.addActionListener(this);
    notionButton.addActionListener(this);
    outlookButton.addActionListener(this);

    sidePanel.add(googleButton);
    sidePanel.add(notionButton);
    sidePanel.add(outlookButton);

    return sidePanel;
  }

  private JPanel createTopPanel() {
    JPanel topPanel = new JPanel(new FlowLayout());

    // Configure the already initialized selectors
    for (int year = 2020; year <= 2030; year++) {
      yearSelector.addItem(year);
    }

    // Set current month and year
    LocalDate now = LocalDate.now();
    monthSelector.setSelectedItem(now.getMonth());
    yearSelector.setSelectedItem(now.getYear());

    monthSelector.addActionListener(this);
    yearSelector.addActionListener(this);

    topPanel.add(new JLabel(ChangeCalendarMonthViewModel.MONTH_LABEL));
    topPanel.add(monthSelector);
    topPanel.add(new JLabel(ChangeCalendarMonthViewModel.YEAR_LABEL));
    topPanel.add(yearSelector);

    return topPanel;
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    if (evt.getSource() == googleButton) {
      handleCalendarClick(changeCalendarMonthViewModel.getState().getGoogleCalendar(), "Google");
    } else if (evt.getSource() == notionButton) {
      handleCalendarClick(changeCalendarMonthViewModel.getState().getNotionCalendar(), "Notion");
    } else if (evt.getSource() == outlookButton) {
      handleCalendarClick(changeCalendarMonthViewModel.getState().getOutlookCalendar(), "Outlook");
    } else if (evt.getSource() == monthSelector || evt.getSource() == yearSelector) {
      handleMonthYearChange();
    }
  }

  private void handleMonthYearChange() {
    ChangeCalendarMonthState currentState = changeCalendarMonthViewModel.getState();
    if (currentState != null && currentState.getActiveCalendar() != null) {
      Month selectedMonth = (Month) monthSelector.getSelectedItem();
      Integer selectedYear = (Integer) yearSelector.getSelectedItem();
      String monthName = selectedMonth.getDisplayName(TextStyle.FULL, Locale.getDefault());

      List<Calendar> calList = new ArrayList<>();
      calList.add(currentState.getActiveCalendar());

      currentState.setCurrMonth(monthName);
      currentState.setCurrYear(selectedYear);
      controller.execute(calList, monthName, selectedYear);
    }
  }

  private void handleCalendarClick(Calendar calendar, String calendarType) {
    if (calendar != null) {
      ChangeCalendarMonthState currentState = changeCalendarMonthViewModel.getState();
      currentState.setActiveCalendar(calendar);

      List<Calendar> calList = new ArrayList<>();
      calList.add(calendar);

      Month selectedMonth = (Month) monthSelector.getSelectedItem();
      Integer selectedYear = (Integer) yearSelector.getSelectedItem();
      String monthName = selectedMonth.getDisplayName(TextStyle.FULL, Locale.getDefault());

      currentState.setCurrMonth(monthName);
      currentState.setCurrYear(selectedYear);

      updateButtonColors(calendar);
      controller.execute(calList, monthName, selectedYear);
    } else {
      errorLabel.setText(calendarType + " Calendar not initialized");
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("state")) {
      ChangeCalendarMonthState state = (ChangeCalendarMonthState) evt.getNewValue();
      if (state != null) {
        updateButtonColors(state.getActiveCalendar());
        updateCalendarView();
        errorLabel.setText(state.getError());
      }
    }
  }

  public void setController(ChangeCalendarMonthController controller) {
    this.controller = controller;
  }

  public String getViewName() {
    return viewName;
  }
}