package interface_adapter.change_calendar_month;

import interface_adapter.ViewModel;

public class ChangeCalendarMonthViewModel extends ViewModel<ChangeCalendarMonthState> {
    public ChangeCalendarMonthViewModel() {
        super("calendar month");
        setState(new ChangeCalendarMonthState());
    }
}

