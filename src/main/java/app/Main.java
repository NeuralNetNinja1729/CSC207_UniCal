package app;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

import data_access.*;
import entity.*;
import interface_adapter.add_event.*;
import interface_adapter.change_calendar_day.*;
import interface_adapter.change_calendar_month.*;
import interface_adapter.delete_event.*;
import use_case.add_event.AddEventInteractor;
import use_case.change_calendar_day.ChangeCalendarDayInteractor;
import use_case.change_calendar_month.ChangeCalendarMonthInteractor;
import use_case.delete_event.DeleteEventInteractor;
import view.*;

public class Main {
  public static void main(String[] args) {
    try {
      // Create factories and DAOs
      CalendarDataAccessObjectFactory calendarDAOFactory = new CalendarDataAccessObjectFactory();

      // Create calendars with credentials
      String googleCredentials = new String(Files.readAllBytes(
        Paths.get("src/main/resources/unical-442813-e8345d894d68.json")));

      Calendar googleCalendar = new GoogleCalendar(
        googleCredentials,
        "calendar-service@unical-442813.iam.gserviceaccount.com",
        "UniCal Google Calendar",
        "unical.207.test@gmail.com"
      );

      Calendar notionCalendar = new NotionCalendar(
        "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",
        "148fc62c766b80cda8edeb8afb359493",
        "UniCal Notion Calendar"
      );

      Calendar outlookCalendar = new OutlookCalendar(
        "{\n" +
          "    \"client_id\": \"4f5baecc-ea4a-42b0-96cd-1af3a9e1351b\",\n" +
          "    \"client_secret\": \"N.98Q~CT_pHc4dBcZQyM4GG6Q5g2qHCyh13ECcKE\",\n" +
          "    \"tenant_id\": \"consumers\",\n" +
          "    \"redirect_uri\": \"http://localhost\"\n" +
          "}",
        "unical.207.test@outlook.com",
        "UniCal Outlook Calendar",
        "primary"  // or specific calendar ID
      );

      // Initialize ViewModels
      ChangeCalendarMonthViewModel monthViewModel = new ChangeCalendarMonthViewModel();
      ChangeCalendarDayViewModel dayViewModel = new ChangeCalendarDayViewModel();
      AddEventViewModel addEventViewModel = new AddEventViewModel();
      DeleteEventViewModel deleteEventViewModel = new DeleteEventViewModel();

      // Initialize Presenters
      ChangeCalendarMonthPresenter monthPresenter = new ChangeCalendarMonthPresenter(monthViewModel);
      ChangeCalendarDayPresenter dayPresenter = new ChangeCalendarDayPresenter(dayViewModel);
      AddEventPresenter addEventPresenter = new AddEventPresenter(addEventViewModel, dayViewModel);
      DeleteEventPresenter deleteEventPresenter = new DeleteEventPresenter(deleteEventViewModel, dayViewModel);

      // Initialize Interactors
      ChangeCalendarMonthInteractor monthInteractor = new ChangeCalendarMonthInteractor(calendarDAOFactory, monthPresenter);
      ChangeCalendarDayInteractor dayInteractor = new ChangeCalendarDayInteractor(calendarDAOFactory, dayPresenter);
      AddEventInteractor addEventInteractor = new AddEventInteractor(calendarDAOFactory, addEventPresenter);
      DeleteEventInteractor deleteEventInteractor = new DeleteEventInteractor(calendarDAOFactory, deleteEventPresenter);

      // Initialize Controllers
      ChangeCalendarMonthController monthController = new ChangeCalendarMonthController(monthInteractor);
      ChangeCalendarDayController dayController = new ChangeCalendarDayController(dayInteractor);
      AddEventController addEventController = new AddEventController(addEventInteractor);
      DeleteEventController deleteEventController = new DeleteEventController(deleteEventInteractor);

      // Create list of available calendars
      ArrayList<Calendar> availableCalendars = new ArrayList<>();
      availableCalendars.add(googleCalendar);
      availableCalendars.add(notionCalendar);
      availableCalendars.add(outlookCalendar);

      // Initialize Views
      ChangeCalendarMonthView monthView = new ChangeCalendarMonthView(monthViewModel);
      monthView.setChangeCalendarMonthController(monthController);

      ChangeCalendarDayView dayView = new ChangeCalendarDayView(dayViewModel);
      dayView.setChangeCalendarDayController(dayController);
      dayView.setDeleteEventController(deleteEventController);

      AddEventView addEventView = new AddEventView(addEventViewModel, addEventController, availableCalendars);
      dayView.setAddEventView(addEventView);

      // Set initial state
      ChangeCalendarMonthState initialMonthState = monthViewModel.getState();
      initialMonthState.setGoogleCalendar(googleCalendar);
      initialMonthState.setNotionCalendar(notionCalendar);
      initialMonthState.setOutlookCalendar(outlookCalendar);
      initialMonthState.setCurrCalendarList(availableCalendars);
      initialMonthState.setActiveCalendar(googleCalendar); // Set default calendar
      monthViewModel.setState(initialMonthState);

      // Create main frame and panel
      JFrame frame = new JFrame("UniCal");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1000, 700);

      CardLayout cardLayout = new CardLayout();
      JPanel mainPanel = new JPanel(cardLayout);

      // Add views to main panel
      mainPanel.add(monthView, "month");
      mainPanel.add(dayView, "day");

      // Set up day view opener
      monthView.setDayViewOpener(date -> {
        dayViewModel.setCurrentDate(date);
        // Update events for the selected date
        ArrayList<Calendar> activeCalendarList = new ArrayList<>();
        activeCalendarList.add(monthViewModel.getState().getActiveCalendar());
        dayController.execute(activeCalendarList, date.toString());
        cardLayout.show(mainPanel, "day");
      });

      frame.add(mainPanel);
      frame.setLocationRelativeTo(null);  // Center the window
      frame.setVisible(true);

    } catch (IOException e) {
      JOptionPane.showMessageDialog(null,
        "Error initializing calendars: " + e.getMessage(),
        "Initialization Error",
        JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
  }
}
