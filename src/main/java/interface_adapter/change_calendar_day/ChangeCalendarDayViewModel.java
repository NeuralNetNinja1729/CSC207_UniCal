package interface_adapter.change_calendar_day;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;

public class ChangeCalendarDayViewModel extends ViewModel<ChangeCalendarDayState> {

    public static final String TITLE_LABEL = "Daily Calendar View";
    public static final String NO_EVENTS_MESSAGE = "No events scheduled for this day.";
    public static final String ERROR_TITLE = "Error";

    // Property change identifiers
    public static final String EVENTS_UPDATED = "events";
    public static final String ERROR_OCCURRED = "error";

    public ChangeCalendarDayViewModel() {
        super("calendar day");
        setState(new ChangeCalendarDayState());
    }

    @Override
    public void setState(ChangeCalendarDayState state) {
        super.setState(state);
        firePropertyChanged();
    }

    // Method to update only events
    public void updateEvents() {
        firePropertyChanged(EVENTS_UPDATED);
    }

    // Method to update error state
    public void updateError() {
        firePropertyChanged(ERROR_OCCURRED);
    }
}
