package use_case.change_calendar_day;

import data_access.CalendarDataAccessObjectFactory;
import data_access.GetEventsDataAccessInterface;
import entity.Calendar;
import entity.Event;
import java.time.LocalDate;
import java.util.ArrayList;

public class ChangeCalendarDayInteractor implements ChangeCalendarDayInputBoundary {
    private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
    private final ChangeCalendarDayOutputBoundary changeCalendarDayPresenter;

    public ChangeCalendarDayInteractor(
            CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
            ChangeCalendarDayOutputBoundary changeCalendarDayPresenter) {
        this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
        this.changeCalendarDayPresenter = changeCalendarDayPresenter;
    }

    @Override
    public void execute(ChangeCalendarDayInputData inputData) {
        try {
            ArrayList<Event> events = new ArrayList<>();
            ArrayList<Calendar> calendars = inputData.getCalendarList();

            for (Calendar calendar : calendars) {
                GetEventsDataAccessInterface getEventsDataAccessObject =
                        (GetEventsDataAccessInterface) calendarDataAccessObjectFactory
                                .getCalendarDataAccessObject(calendar);

                events.addAll(getEventsDataAccessObject.fetchEventsDay(LocalDate.parse(inputData.getDate())));
            }

            ChangeCalendarDayOutputData outputData = new ChangeCalendarDayOutputData(
                    calendars, events, false);
            changeCalendarDayPresenter.prepareSuccessView(outputData);
        } catch (Exception e) {
            changeCalendarDayPresenter.prepareFailView("Error fetching calendar data: " + e.getMessage());
        }
    }
}