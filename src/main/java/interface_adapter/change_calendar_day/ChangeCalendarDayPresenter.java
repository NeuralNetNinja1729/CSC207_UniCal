package interface_adapter.change_calendar_day;

import entity.Event;
import interface_adapter.ViewManagerModel;
import use_case.change_calendar_day.ChangeCalendarDayOutputBoundary;
import use_case.change_calendar_day.ChangeCalendarDayOutputData;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * The Presenter for the Change Day Calendar Use Case.
 */
public class ChangeCalendarDayPresenter implements ChangeCalendarDayOutputBoundary {

    private final ChangeCalendarDayViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public ChangeCalendarDayPresenter(ChangeCalendarDayViewModel changeCalendarDayViewModel,
                                      ViewManagerModel viewManagerModel) {
        this.viewModel = changeCalendarDayViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(ChangeCalendarDayOutputData outputData) {
        Map<String, String> eventsByHour = new HashMap<>();

        // Format the events
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Event event : outputData.getEventList()) {
            String time = event.getDate().atTime(event.getStartTime()).format(timeFormatter);
            String eventName = event.getEventName();
            String calendarApiName = event.getCalendarApi().getCalendarApiName(); // Assuming Event has a method getCalendarApiName()

            // Append the event to the hour along with the calendar API name
            eventsByHour.putIfAbsent(time, "");
            eventsByHour.put(time, eventsByHour.get(time) + eventName + " (" + calendarApiName + ")\n");
        }

        // Update the view model with formatted data
        ChangeCalendarDayState state = this.viewModel.getState();
        state.setCurrCalendarList(outputData.getCalendarList());

        // Store events as a simplified map for easy UI rendering
        state.setEventMap(eventsByHour);

        // Notify the view model about the state change
        this.viewModel.setState(state);
        this.viewModel.firePropertyChanged();

        this.viewManagerModel.setState(viewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}
