package interface_adapter.change_calendar_day;

import interface_adapter.ViewModel;
import entity.Event;
import entity.Calendar;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class ChangeCalendarDayViewModel extends ViewModel<ChangeCalendarDayState> {
  public static final String TITLE_LABEL = "Day View";
  public static final String ADD_EVENT_BUTTON_LABEL = "Add Event";
  public static final String DELETE_EVENT_BUTTON_LABEL = "Delete Event";
  public static final String NEXT_DAY_BUTTON_LABEL = "Next Day";
  public static final String PREVIOUS_DAY_BUTTON_LABEL = "Previous Day";

  public ChangeCalendarDayViewModel() {
    super("calendar_day");
    setState(new ChangeCalendarDayState());
  }

  // Helper method to update the current date
  public void setCurrentDate(LocalDate date) {
    ChangeCalendarDayState state = getState();
    state.setDate(date);
    setState(state);
    firePropertyChanged();
  }

  // Helper method to update the calendar list
  public void setCalendarList(List<Calendar> calendars) {
    ChangeCalendarDayState state = getState();
    state.setCalendarList(calendars);
    setState(state);
    firePropertyChanged();
  }

  // Helper method to update the event list
  public void setEventList(List<Event> events) {
    ChangeCalendarDayState state = getState();
    state.setEventList(events);
    setState(state);
    firePropertyChanged();
  }

  // Helper method to get formatted date string
  public String getFormattedDate() {
    LocalDate date = getState().getDate();
    return date.toString(); // You can format this as needed
  }

  // Helper method to get events for display
  public List<Event> getDisplayEvents() {
    return getState().getEventList();
  }

  // Helper method to get selected calendars
  public List<Calendar> getSelectedCalendars() {
    return getState().getCalendarList();
  }

  // Helper method to advance to next day
  public void goToNextDay() {
    ChangeCalendarDayState state = getState();
    state.setDate(state.getDate().plusDays(1));
    setState(state);
    firePropertyChanged();
  }

  // Helper method to go to previous day
  public void goToPreviousDay() {
    ChangeCalendarDayState state = getState();
    state.setDate(state.getDate().minusDays(1));
    setState(state);
    firePropertyChanged();
  }

  // Helper method to check if there are any events
  public boolean hasEvents() {
    return !getState().getEventList().isEmpty();
  }

  // Helper method to get number of events
  public int getEventCount() {
    return getState().getEventList().size();
  }

  // Helper method to clear all events
  public void clearEvents() {
    ChangeCalendarDayState state = getState();
    state.setEventList(new ArrayList<>());
    setState(state);
    firePropertyChanged();
  }

  // Helper method to add a single event
  public void addEvent(Event event) {
    ChangeCalendarDayState state = getState();
    List<Event> currentEvents = new ArrayList<>(state.getEventList());
    currentEvents.add(event);
    state.setEventList(currentEvents);
    setState(state);
    firePropertyChanged();
  }

  // Helper method to remove a single event
  public void removeEvent(Event event) {
    ChangeCalendarDayState state = getState();
    List<Event> currentEvents = new ArrayList<>(state.getEventList());
    currentEvents.remove(event);
    state.setEventList(currentEvents);
    setState(state);
    firePropertyChanged();
  }
}
