package use_case.change_calendar_month;

import data_access.CalendarDataAccessObjectFactory;
import entity.Calendar;
import entity.GoogleCalendar;
import interface_adapter.change_calendar_month.ChangeCalendarMonthPresenter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ChangeCalendarMonthInteractorTest2 {

  public ChangeCalendarMonthInteractor interactor;

  @Before
  public void setUp() throws Exception {
    ChangeCalendarMonthPresenter presenter = new ChangeCalendarMonthPresenter();
    interactor = new ChangeCalendarMonthInteractor(new CalendarDataAccessObjectFactory(), presenter);
  }

  @Test
  public void testFetchEventsMonth() throws Exception {
    ArrayList<Calendar> calendars = new ArrayList<>();
    String credentials = new String(Files.readAllBytes(Paths.get("src/main/resources/credentials.json")));
    Calendar google = new GoogleCalendar(
      credentials,
      "unical.207.test@gmail.com",
      "UniCal",
      "primary"
    );
    calendars.add(google);
    ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(calendars, "2024-11-24");
    interactor.execute(inputData);
  }
}
