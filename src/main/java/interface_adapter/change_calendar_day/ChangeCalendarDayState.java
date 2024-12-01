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
  private String error = "";
  private boolean useCaseFailed = false;

  // Copy constructor for immutability
  public ChangeCalendarDayState(ChangeCalendarDayState copy) {
    this.calendarList = new ArrayList<>(copy.calendarList);
    this.eventList = new ArrayList<>(copy.eventList);
    this.date = copy.date;
    this.error = copy.error;
    this.useCaseFailed = copy.useCaseFailed;
  }

  // Default constructor
  public ChangeCalendarDayState() {}

  // Getters and setters with defensive copying for collections
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

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public boolean isUseCaseFailed() {
    return useCaseFailed;
  }

  public void setUseCaseFailed(boolean useCaseFailed) {
    this.useCaseFailed = useCaseFailed;
  }

  // Helper methods
  public void addEvent(Event event) {
    this.eventList.add(event);
  }

  public void removeEvent(Event event) {
    this.eventList.remove(event);
  }

  public boolean hasEvents() {
    return !eventList.isEmpty();
  }

  public int getEventCount() {
    return eventList.size();
  }
}
