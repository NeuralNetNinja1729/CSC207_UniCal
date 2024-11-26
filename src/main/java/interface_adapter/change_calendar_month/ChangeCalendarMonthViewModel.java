package interface_adapter.change_calendar_month;

import interface_adapter.ViewModel;

public class ChangeCalendarMonthViewModel extends ViewModel<ChangeCalendarMonthState> {
    public ChangeCalendarMonthViewModel() {
        super("calendar_month");
        setState(new ChangeCalendarMonthState());
    }
}
