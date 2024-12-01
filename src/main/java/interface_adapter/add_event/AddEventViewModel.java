package interface_adapter.add_event;

import interface_adapter.ViewModel;

public class AddEventViewModel extends ViewModel<AddEventState> {
  public static final String TITLE_LABEL = "Add Event";
  public static final String EVENT_NAME_LABEL = "Event Name";
  public static final String DATE_LABEL = "Date (YYYY-MM-DD)";
  public static final String START_TIME_LABEL = "Start Time (HH:mm)";
  public static final String END_TIME_LABEL = "End Time (HH:mm)";
  public static final String CALENDAR_LABEL = "Select Calendar";
  public static final String ADD_BUTTON_LABEL = "Add";
  public static final String CANCEL_BUTTON_LABEL = "Cancel";

  public AddEventViewModel() {
    super("add event");
    setState(new AddEventState());
  }
}