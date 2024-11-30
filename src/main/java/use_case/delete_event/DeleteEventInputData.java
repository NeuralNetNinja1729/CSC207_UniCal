// Now let's create the Input Data class
package use_case.delete_event;

import entity.Calendar;
import entity.Event;

public class DeleteEventInputData {
  private final Event event;
  private Calendar calendarApi;

  public DeleteEventInputData(Event event, Calendar calendarApi) {
    this.event = event;
    this.calendarApi = calendarApi;
  }

  public Event getEvent() {
    return event;
  }

  public Calendar getCalendarApi() {
    return calendarApi;
  }

  public void setCalendarApi(Calendar calendarApi) {
    this.calendarApi = calendarApi;
  }
}
