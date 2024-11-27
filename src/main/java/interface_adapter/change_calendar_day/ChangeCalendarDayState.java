package interface_adapter.change_calendar_day;

import entity.Calendar;
import entity.Event;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChangeCalendarDayState {
  private List<Calendar> calendarList = new ArrayList<>();
  private List<Event> eventList = new ArrayList<>();
  private LocalDate date = LocalDate.now();
  private String errorMessage = "";

  // Copy constructor
  public ChangeCalendarDayState(ChangeCalendarDayState copy) {
    this.calendarList = new ArrayList<>(copy.calendarList);
    this.eventList = new ArrayList<>(copy.eventList);
    this.date = copy.date;
    this.errorMessage = copy.errorMessage;
  }

  // Default constructor
  public ChangeCalendarDayState() {}

  // Getters and setters
  public List<Calendar> getCalendarList() {
    return new ArrayList<>(calendarList);
  }

  public void setCalendarList(List<Calendar> calendarList) {
    this.calendarList = new ArrayList<>(calendarList);
  }

  public List<Event> getEventList() {
    return new ArrayList<>(eventList);
  }

  public void setEventList(List<Event> eventList) {
    this.eventList = new ArrayList<>(eventList);
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
