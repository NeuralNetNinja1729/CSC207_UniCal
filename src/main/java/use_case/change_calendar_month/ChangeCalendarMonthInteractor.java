package use_case.change_calendar_month;

import data_access.CalendarDataAccessObjectFactory;
import data_access.GetEventsDataAccessInterface;
import entity.Calendar;
import entity.Event;
import interface_adapter.change_calendar_month.ChangeCalendarMonthPresenter;

import java.util.ArrayList;

/**
 * The Change Month Calendar Interactor.
 */
public class ChangeCalendarMonthInteractor implements ChangeCalendarMonthInputBoundary {

    private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
    private final ChangeCalendarMonthPresenter changeCalendarMonthPresenter;

    public ChangeCalendarMonthInteractor(CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
                                         ChangeCalendarMonthPresenter changeCalendarMonthPresenter) {
        this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
        this.changeCalendarMonthPresenter = changeCalendarMonthPresenter;
    }

    @Override
    public void execute(ChangeCalendarMonthInputData inputData) {

        final ArrayList<Event> events = new ArrayList<>();

        final ArrayList<Calendar> calendars = inputData.getCalendarList();
        for (int i = 0; i < calendars.size(); i++) {

            final GetEventsDataAccessInterface getEventsDataAccessObject =
                    (GetEventsDataAccessInterface) this.calendarDataAccessObjectFactory
                            .getCalendarDataAccessObject(calendars.get(i));

            events.addAll(getEventsDataAccessObject.fetchEventsMonth(inputData.getDate()));
        }

        final ChangeCalendarMonthOutputData changeCalendarMonthOutputData =
                new ChangeCalendarMonthOutputData(calendars, events);

        this.changeCalendarMonthPresenter.prepareSuccessView(changeCalendarMonthOutputData);
    }
}
