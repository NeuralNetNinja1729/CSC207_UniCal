package use_case.add_event;

import entity.Calendar;

public class AddEventInputData {
  private final String eventName;
  private final String date;
  private final String startTime;
  private final String endTime;
  private final Calendar calendar;

  public AddEventInputData(String eventName, String date, String startTime, String endTime, Calendar calendar) {
    this.eventName = eventName;
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.calendar = calendar;
  }

  public String getEventName() {
    return eventName;
  }

  public String getDate() {
    return date;
  }

  public String getStartTime() {
    return startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public Calendar getCalendar() {
    return calendar;
  }
}