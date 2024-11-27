package use_case.add_event;

import entity.Calendar;
import java.time.LocalDate;

public class AddEventInputData {
  private final String eventName;
  private final String date;
  private final Calendar calendar;

  public AddEventInputData(String eventName, String date, Calendar calendar) {
    this.eventName = eventName;
    this.date = date;
    this.calendar = calendar;
  }

  public String getEventName() {
    return eventName;
  }

  public String getDate() {
    return date;
  }

  public Calendar getCalendar() {
    return calendar;
  }
}
