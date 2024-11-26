package interface_adapter.change_calendar_month;

import entity.Event;
import interface_adapter.ViewManagerModel;
import use_case.change_calendar_month.ChangeCalendarMonthOutputBoundary;
import use_case.change_calendar_month.ChangeCalendarMonthOutputData;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * The Presenter for the Change Month Calendar Use Case.
 */
public class ChangeCalendarMonthPresenter implements ChangeCalendarMonthOutputBoundary {

    private final ChangeCalendarMonthViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public ChangeCalendarMonthPresenter(ChangeCalendarMonthViewModel changeCalendarMonthViewModel,
                                        ViewManagerModel viewManagerModel) {
        this.viewModel = changeCalendarMonthViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(ChangeCalendarMonthOutputData outputData) {
        Map<String, String> eventsByDate = new HashMap<>();

        // Format the events
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Event event : outputData.getEventList()) {
            String date = event.getDate().format(dateFormatter);
            String eventName = event.getEventName();

            // Append the event to the date
            eventsByDate.putIfAbsent(date, "");
            eventsByDate.put(date, eventsByDate.get(date) + eventName + "\n");
        }

        // Update the view model with formatted data
        ChangeCalendarMonthState state = this.viewModel.getState();
        state.setCurrCalendarList(outputData.getCalendarList());

        // Store events as a simplified map for easy UI rendering
        state.setEventMap(eventsByDate);

        // Notify the view model about the state change
        this.viewModel.setState(state);
        this.viewModel.firePropertyChanged();

        this.viewManagerModel.setState(viewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}
