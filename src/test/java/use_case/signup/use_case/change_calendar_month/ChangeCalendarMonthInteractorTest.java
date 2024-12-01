package use_case.signup.use_case.change_calendar_month;

import data_access.CalendarDataAccessObjectFactory;
import entity.Calendar;
import entity.NotionCalendar;
import interface_adapter.change_calendar_month.ChangeCalendarMonthPresenter;
import interface_adapter.change_calendar_month.ChangeCalendarMonthViewModel;
import org.junit.Before;
import org.junit.Test;
import use_case.change_calendar_month.ChangeCalendarMonthInputData;
import use_case.change_calendar_month.ChangeCalendarMonthInteractor;

import java.util.ArrayList;

public class ChangeCalendarMonthInteractorTest {

    public ChangeCalendarMonthInteractor interactor;

    @Before
    public void setUp() throws Exception {
        ChangeCalendarMonthPresenter presenter = new ChangeCalendarMonthPresenter(new ChangeCalendarMonthViewModel());
        interactor = new ChangeCalendarMonthInteractor(new CalendarDataAccessObjectFactory(), presenter);
    }

    @Test
    public void testFetchEventsMonth() throws Exception {
        ArrayList<Calendar> calendars = new ArrayList<>();
        Calendar notion = new NotionCalendar("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", "148fc62c766b80cda8edeb8afb359493", "Notion Calendar");
        calendars.add(notion);
        ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(calendars, "2024-11-24");
        interactor.execute(inputData);
    }
}
