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
      // Create view models first
      ChangeCalendarMonthViewModel monthViewModel = new ChangeCalendarMonthViewModel();
      ChangeCalendarDayViewModel dayViewModel = new ChangeCalendarDayViewModel();
      AddEventViewModel addEventViewModel = new AddEventViewModel();
      DeleteEventViewModel deleteEventViewModel = new DeleteEventViewModel();

      // Create calendar factory and calendars
      CalendarDataAccessObjectFactory calendarDAOFactory = new CalendarDataAccessObjectFactory();

      // Load credentials from files
      String googleCredentials = new String(Files.readAllBytes(
              Paths.get("src/main/resources/unical-442813-e8345d894d68.json")));

      // Initialize calendars
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
              "primary"
      );

      // Create presenters
      ChangeCalendarMonthPresenter monthPresenter = new ChangeCalendarMonthPresenter(monthViewModel);
      ChangeCalendarDayPresenter dayPresenter = new ChangeCalendarDayPresenter(dayViewModel);
      AddEventPresenter addEventPresenter = new AddEventPresenter(addEventViewModel, dayViewModel);
      DeleteEventPresenter deleteEventPresenter = new DeleteEventPresenter(deleteEventViewModel, dayViewModel);

      // Create interactors
      ChangeCalendarMonthInteractor monthInteractor = new ChangeCalendarMonthInteractor(calendarDAOFactory, monthPresenter);
      ChangeCalendarDayInteractor dayInteractor = new ChangeCalendarDayInteractor(calendarDAOFactory, dayPresenter);
      AddEventInteractor addEventInteractor = new AddEventInteractor(calendarDAOFactory, addEventPresenter);
      DeleteEventInteractor deleteEventInteractor = new DeleteEventInteractor(calendarDAOFactory, deleteEventPresenter);

      // Create controllers
      ChangeCalendarMonthController monthController = new ChangeCalendarMonthController(monthInteractor);
      ChangeCalendarDayController dayController = new ChangeCalendarDayController(dayInteractor);
      AddEventController addEventController = new AddEventController(addEventInteractor);
      DeleteEventController deleteEventController = new DeleteEventController(deleteEventInteractor);

      // Create list of available calendars
      ArrayList<Calendar> availableCalendars = new ArrayList<>();
      availableCalendars.add(googleCalendar);
      availableCalendars.add(notionCalendar);
      availableCalendars.add(outlookCalendar);

      // Create views
      ChangeCalendarMonthView monthView = new ChangeCalendarMonthView(monthViewModel);
      monthView.setController(monthController);

      ChangeCalendarDayView dayView = new ChangeCalendarDayView(dayViewModel);
      dayView.setChangeCalendarDayController(dayController);
      dayView.setDeleteEventController(deleteEventController);

      AddEventView addEventView = new AddEventView(addEventViewModel, addEventController, availableCalendars);
      dayView.setAddEventView(addEventView);

      // Set initial state for month view
      ChangeCalendarMonthState initialMonthState = new ChangeCalendarMonthState();
      initialMonthState.setGoogleCalendar(googleCalendar);
      initialMonthState.setNotionCalendar(notionCalendar);
      initialMonthState.setOutlookCalendar(outlookCalendar);
      initialMonthState.setCurrCalendarList(availableCalendars);
      initialMonthState.setActiveCalendar(googleCalendar);
      monthViewModel.setState(initialMonthState);

      // Create and setup main frame
      JFrame frame = new JFrame("UniCal");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1000, 700);

      // Setup card layout for view switching
      CardLayout cardLayout = new CardLayout();
      JPanel mainPanel = new JPanel(cardLayout);

      mainPanel.add(monthView, "month");
      mainPanel.add(dayView, "day");

      // Setup day view opener
      monthView.setDayViewOpener(date -> {
        System.out.println("Opening day view for: " + date); // Debug print
        dayViewModel.setCurrentDate(date);

        ArrayList<Calendar> activeCalendarList = new ArrayList<>();
        Calendar activeCalendar = monthViewModel.getState().getActiveCalendar();
        if (activeCalendar != null) {
          activeCalendarList.add(activeCalendar);
        }

        dayController.execute(activeCalendarList, date.toString());
        cardLayout.show(mainPanel, "day");
      });

      frame.add(mainPanel);
      frame.setLocationRelativeTo(null);
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