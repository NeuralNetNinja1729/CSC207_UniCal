package interface_adapter.add_event;

import entity.Event;
import interface_adapter.ViewManagerModel;
import interface_adapter.change_calendar_day.ChangeCalendarDayState;
import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;
import use_case.add_event.AddEventOutputBoundary;
import use_case.add_event.AddEventOutputData;

import java.time.format.DateTimeFormatter;

/**
 * The Presenter for the Login Use Case.
 */
public class AddEventPresenter implements AddEventOutputBoundary {
    private final ChangeCalendarDayViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public AddEventPresenter(ChangeCalendarDayViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(AddEventOutputData outputData) {
        System.out.println("Successfully added event");
        Event event = outputData.getEvent();
        String eventName = event.getEventName();
        String calendarApi = event.getCalendarApi().getCalendarApiName();
        String eventDetails = eventName + " (" + calendarApi + ")";
        ChangeCalendarDayState state = this.viewModel.getState();
        state.addEvent(eventDetails);
        this.viewModel.setState(state);
        this.viewModel.firePropertyChanged();

        this.viewManagerModel.setState(viewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();

    }

    @Override
    public void prepareFailView(String error) {
        // TODO
    }
}
