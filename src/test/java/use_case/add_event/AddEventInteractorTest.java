package use_case.add_event;

import data_access.CalendarDataAccessObjectFactory;
import entity.Calendar;
import entity.NotionCalendar;
import interface_adapter.add_event.AddEventPresenter;
import org.junit.Before;
import org.junit.Test;

public class AddEventInteractorTest {

    public AddEventInteractor interactor;

    @Before
    public void setUp() throws Exception {
        AddEventPresenter presenter = new AddEventPresenter();
        interactor = new AddEventInteractor(new CalendarDataAccessObjectFactory(), presenter);
    }

    @Test
    public void testAddEvent() throws Exception {
        System.out.println(System.getenv("notion_api"));
        Calendar notion = new NotionCalendar("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", "148fc62c766b80cda8edeb8afb359493", "Notion Calendar");
        AddEventInputData inputData = new AddEventInputData("Crazy Event!", "2024-11-30", notion);
        interactor.execute(inputData);
    }
}
