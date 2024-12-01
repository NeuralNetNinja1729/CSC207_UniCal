package use_case.signup.use_case.change_calendar_month;

import data_access.CalendarDataAccessObjectFactory;
import entity.Calendar;
import entity.GoogleCalendar;
import interface_adapter.change_calendar_month.ChangeCalendarMonthPresenter;
import interface_adapter.change_calendar_month.ChangeCalendarMonthViewModel;
import org.junit.Before;
import org.junit.Test;
import use_case.change_calendar_month.ChangeCalendarMonthInputData;
import use_case.change_calendar_month.ChangeCalendarMonthInteractor;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ChangeCalendarMonthInteractorTest2 {

  public ChangeCalendarMonthInteractor interactor;

  @Before
  public void setUp() throws Exception {
    ChangeCalendarMonthPresenter presenter = new ChangeCalendarMonthPresenter(new ChangeCalendarMonthViewModel());
    interactor = new ChangeCalendarMonthInteractor(new CalendarDataAccessObjectFactory(), presenter);
  }

  @Test
  public void testFetchEventsMonth() throws Exception {
    ArrayList<Calendar> calendars = new ArrayList<>();
    String credentials = new String(Files.readAllBytes(Paths.get("src/main/resources/unical-442813-e8345d894d68.json")));
    Calendar google = new GoogleCalendar(
      credentials,
      "calendar-service@unical-442813.iam.gserviceaccount.com",
      "UniCal",
      "primary"
    );
    calendars.add(google);
    ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(calendars, "2024-11-24");
    interactor.execute(inputData);
  }
}
