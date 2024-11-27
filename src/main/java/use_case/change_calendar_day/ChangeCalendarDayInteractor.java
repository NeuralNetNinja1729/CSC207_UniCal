package use_case.change_calendar_day;

import data_access.CalendarDataAccessObjectFactory;
import data_access.GetEventsDataAccessInterface;
import entity.Calendar;
import entity.Event;
import interface_adapter.change_calendar_day.ChangeCalendarDayPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * The Change Day Calendar Interactor.
 */
public class ChangeCalendarDayInteractor implements ChangeCalendarDayInputBoundary {

    private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
    private final ChangeCalendarDayPresenter changeCalendarDayPresenter;

    public ChangeCalendarDayInteractor(CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
                                         ChangeCalendarDayPresenter changeCalendarDayPresenter) {
        this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
        this.changeCalendarDayPresenter = changeCalendarDayPresenter;
    }

    @Override
    public void execute(ChangeCalendarDayInputData inputData) {

        final List<Event> events = new ArrayList<>();

        final List<Calendar> calendars = inputData.getCalendarList();
        for (int i = 0; i < calendars.size(); i++) {

            final GetEventsDataAccessInterface getEventsDataAccessObject =
                    (GetEventsDataAccessInterface) this.calendarDataAccessObjectFactory
                            .getCalendarDataAccessObject(calendars.get(i));

            events.addAll(getEventsDataAccessObject.fetchEventsDay(inputData.getDate()));
        }

        final ChangeCalendarDayOutputData changeCalendarDayOutputData =
                new ChangeCalendarDayOutputData(calendars, events);

        this.changeCalendarDayPresenter.prepareSuccessView(changeCalendarDayOutputData);
    }
}
