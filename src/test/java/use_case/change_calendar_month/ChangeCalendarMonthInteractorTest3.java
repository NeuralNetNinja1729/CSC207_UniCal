package use_case.change_calendar_month;

import data_access.CalendarDataAccessObjectFactory;
import entity.Calendar;
import entity.OutlookCalendar;
import interface_adapter.ViewManagerModel;
import interface_adapter.change_calendar_month.ChangeCalendarMonthPresenter;
import interface_adapter.change_calendar_month.ChangeCalendarMonthViewModel;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ChangeCalendarMonthInteractorTest3 {

  public ChangeCalendarMonthInteractor interactor;

  @Before
  public void setUp() {
    ChangeCalendarMonthPresenter presenter = new ChangeCalendarMonthPresenter(new ChangeCalendarMonthViewModel());
    interactor = new ChangeCalendarMonthInteractor(new CalendarDataAccessObjectFactory(), presenter);
  }

  @Test
  public void testFetchEventsMonth() throws Exception {
    ArrayList<Calendar> calendars = new ArrayList<>();
    String credentials = new String(Files.readAllBytes(Paths.get("src/main/resources/outlook-credentials.json")));
    Calendar outlook = new OutlookCalendar(
      credentials,
      "unical.207.test@outlook.com",
      "UniCal",
      "primary"
    );
    calendars.add(outlook);
    ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(calendars, "2024-11-24");
    interactor.execute(inputData);
  }
}
