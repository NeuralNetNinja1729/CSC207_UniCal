package interface_adapter.add_event;

public class AddEventState {
  private String eventName = "";
  private String date = "";
  private String eventNameError = "";
  private String dateError = "";
  private String calendarError = "";
  private entity.Calendar selectedCalendar = null;

  // Copy constructor
  public AddEventState(AddEventState copy) {
    eventName = copy.eventName;
    date = copy.date;
    eventNameError = copy.eventNameError;
    dateError = copy.dateError;
    calendarError = copy.calendarError;
    selectedCalendar = copy.selectedCalendar;
  }

  // Default constructor
  public AddEventState() {}

  // Getters and setters
  public String getEventName() { return eventName; }
  public void setEventName(String eventName) { this.eventName = eventName; }

  public String getDate() { return date; }
  public void setDate(String date) { this.date = date; }

  public String getEventNameError() { return eventNameError; }
  public void setEventNameError(String error) { this.eventNameError = error; }

  public String getDateError() { return dateError; }
  public void setDateError(String error) { this.dateError = error; }

  public String getCalendarError() { return calendarError; }
  public void setCalendarError(String error) { this.calendarError = error; }

  public entity.Calendar getSelectedCalendar() { return selectedCalendar; }
  public void setSelectedCalendar(entity.Calendar calendar) { this.selectedCalendar = calendar; }
}
