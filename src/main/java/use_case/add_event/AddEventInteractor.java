package use_case.add_event;

import data_access.AddEventDataAccessInterface;
import data_access.CalendarDataAccessObjectFactory;
import entity.Calendar;
import interface_adapter.add_event.AddEventPresenter;

/**
 * The Change Day Calendar Interactor.
 */
public class AddEventInteractor implements AddEventInputBoundary {

    private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
    private final AddEventPresenter addEventPresenter;

    public AddEventInteractor(CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
                              AddEventPresenter addEventPresenter) {
        this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
        this.addEventPresenter = addEventPresenter;
    }

    @Override
    public void execute(AddEventInputData inputData) {

        final Calendar calendar = inputData.getCalendarApi();

        final AddEventDataAccessInterface addEventDataAccessObject =
                (AddEventDataAccessInterface) this.calendarDataAccessObjectFactory
                        .getCalendarDataAccessObject(calendar);

        if (!addEventDataAccessObject.addEvent(inputData.getEvent())) {
            this.addEventPresenter.prepareFailView("Unable to Add Event!");
        }
        else {
            final AddEventOutputData addEventOutputData =
                    new AddEventOutputData(inputData.getCalendarApi(), inputData.getEvent());

            this.addEventPresenter.prepareSuccessView(addEventOutputData);
        }
    }
}

