package use_case.change_calendar_day;

import data_access.CalendarDataAccessObjectFactory;
import entity.Calendar;
import entity.Event;
import entity.NotionCalendar;
import interface_adapter.ViewManagerModel;
import interface_adapter.change_calendar_day.ChangeCalendarDayPresenter;
import interface_adapter.change_calendar_day.ChangeCalendarDayState;
import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChangeCalendarDayIntegrationTest {
    private ChangeCalendarDayViewModel viewModel;
    private ViewManagerModel viewManagerModel;
    private ChangeCalendarDayInteractor interactor;
    private Calendar notionCalendar;
    private LocalDate testDate;

    @Before
    public void setUp() {
        // Initialize date
        testDate = LocalDate.parse("2024-11-25");

        // Create ViewModel
        viewModel = new ChangeCalendarDayViewModel();
        viewManagerModel = new ViewManagerModel();

        // Create Presenter
        ChangeCalendarDayPresenter presenter = new ChangeCalendarDayPresenter(viewModel, viewManagerModel);

        // Create Calendar Factory
        CalendarDataAccessObjectFactory factory = new CalendarDataAccessObjectFactory();

        // Create Interactor
        interactor = new ChangeCalendarDayInteractor(factory, presenter);

        // Create test calendar (using Notion as it's the most complete implementation)
        notionCalendar = new NotionCalendar(
                "test_token",
                "test_database_id",
                "Test Notion Calendar"
        );
    }

    @Test
    public void testSuccessfulEventFetch() {
        // Create input data
        ArrayList<Calendar> calendars = new ArrayList<>();
        calendars.add(notionCalendar);
        ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(
                calendars,
                testDate.toString()
        );

        // Execute use case
        interactor.execute(inputData);

        // Verify ViewModel state
        ChangeCalendarDayState resultState = viewModel.getState();

        // Verify calendar is set
        assertNotNull("Calendar list should not be null", resultState.getCalendars());
        assertFalse("Calendar list should not be empty", resultState.getCalendars().isEmpty());
        assertEquals("Should be Notion calendar",
                "NotionCalendar",
                resultState.getCurrentCalendar().getCalendarApiName());

        // Verify events list exists (might be empty but should not be null)
        assertNotNull("Events list should not be null", resultState.getEvents());

        // Verify no errors
        assertNull("Should not have errors", resultState.getError());
    }

    @Test
    public void testCalendarListHandling() {
        // Test with empty calendar list
        ArrayList<Calendar> emptyCalendars = new ArrayList<>();
        ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(
                emptyCalendars,
                testDate.toString()
        );

        // Execute use case
        interactor.execute(inputData);

        // Verify ViewModel state
        ChangeCalendarDayState resultState = viewModel.getState();

        // Verify events list is empty but not null
        assertNotNull("Events list should not be null", resultState.getEvents());
        assertTrue("Events list should be empty", resultState.getEvents().isEmpty());
    }

    @Test
    public void testDateHandling() {
        // Create input data with calendars
        ArrayList<Calendar> calendars = new ArrayList<>();
        calendars.add(notionCalendar);

        // Test with a future date
        LocalDate futureDate = LocalDate.parse("2025-01-01");
        ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(
                calendars,
                futureDate.toString()
        );

        // Execute use case
        interactor.execute(inputData);

        // Verify ViewModel state
        ChangeCalendarDayState resultState = viewModel.getState();

        // Verify events list exists
        assertNotNull("Events list should not be null", resultState.getEvents());
    }

    @Test
    public void testEventProperties() {
        // Create input data
        ArrayList<Calendar> calendars = new ArrayList<>();
        calendars.add(notionCalendar);
        ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(
                calendars,
                testDate.toString()
        );

        // Execute use case
        interactor.execute(inputData);

        // Verify ViewModel state
        ChangeCalendarDayState resultState = viewModel.getState();

        // If there are any events, verify their properties
        for (Event event : resultState.getEvents()) {
            assertNotNull("Event name should not be null", event.getEventName());
            assertNotNull("Event date should not be null", event.getDate());
            assertNotNull("Event calendar should not be null", event.getCalendarApi());
            assertEquals("Event should be from Notion calendar",
                    "NotionCalendar",
                    event.getCalendarApi().getCalendarApiName());
        }
    }
}
