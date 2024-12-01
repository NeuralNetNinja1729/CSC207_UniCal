package use_case.change_calendar_month;

import data_access.CalendarDataAccessObjectFactory;
import entity.Calendar;
import entity.GoogleCalendar;
import entity.NotionCalendar;
import entity.OutlookCalendar;
import interface_adapter.ViewManagerModel;
import interface_adapter.change_calendar_month.ChangeCalendarMonthPresenter;
import interface_adapter.change_calendar_month.ChangeCalendarMonthViewModel;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ChangeCalendarMonthInteractorTest {

    public ChangeCalendarMonthInteractor interactor;

    @Before
    public void setUp() throws Exception {
        ChangeCalendarMonthPresenter presenter = new ChangeCalendarMonthPresenter(new ChangeCalendarMonthViewModel());
        interactor = new ChangeCalendarMonthInteractor(new CalendarDataAccessObjectFactory(), presenter);
    }

    @Test
    public void testFetchEventsMonthNotion() throws Exception {
        ArrayList<Calendar> calendars = new ArrayList<>();
        Calendar notion = new NotionCalendar("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", "148fc62c766b80cda8edeb8afb359493", "Notion Calendar");
        calendars.add(notion);
        ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(calendars, "2024-11-24");
        interactor.execute(inputData);
    }

    @Test
    public void testFetchEventsMonthGoogle() throws Exception {
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

    @Test
    public void testFetchEventsMonthOutlook() throws Exception {
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
