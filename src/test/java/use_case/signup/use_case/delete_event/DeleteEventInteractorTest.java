package use_case.signup.use_case.delete_event;

import data_access.CalendarDataAccessObjectFactory;
import data_access.NotionCalendarDataAccessObject;
import entity.Calendar;
import entity.Event;
import entity.NotionCalendar;
import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;
import interface_adapter.delete_event.DeleteEventPresenter;
import interface_adapter.delete_event.DeleteEventViewModel;
import org.junit.Before;
import org.junit.Test;
import use_case.delete_event.DeleteEventInputData;
import use_case.delete_event.DeleteEventInteractor;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class DeleteEventInteractorTest {
    public DeleteEventInteractor interactor;

    @Before
    public void setUp() throws Exception {
        DeleteEventPresenter presenter = new DeleteEventPresenter(new DeleteEventViewModel(), new ChangeCalendarDayViewModel());
        interactor = new DeleteEventInteractor(new CalendarDataAccessObjectFactory(), presenter);
    }

    @Test
    public void testDeleteEventT() throws Exception {
        System.out.println(System.getenv("notion_api"));
        Calendar notion = new NotionCalendar("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", "148fc62c766b80cda8edeb8afb359493", "Notion Calendar");
        DeleteEventInputData inputData = new DeleteEventInputData(new Event("Crazy Event!",
                LocalDate.of(2024, 11, 30), LocalTime.of(13,0),
                LocalTime.of(14,30), notion));
        interactor.execute(inputData);
    }

    @Test
    public void testGetEventID() throws Exception {
        NotionCalendar notionCalendar = new NotionCalendar("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", "148fc62c766b80cda8edeb8afb359493", "Notion Calendar");
        Event mockEvent = new Event("Crazy Event!", LocalDate.of(2024, 11, 30), LocalTime.of(13,0), LocalTime.of(14,30),notionCalendar);

        NotionCalendarDataAccessObject dao = new NotionCalendarDataAccessObject(notionCalendar);
        // Execution: Fetch the page ID using the getEventID method
        String actualPageId = dao.getEventID(mockEvent);
        System.out.println(actualPageId);

        // Assertion: Check if the actual page ID matches the expected value
        assertEquals("The page ID should match the expected value", 36, actualPageId.length());
    }
}
