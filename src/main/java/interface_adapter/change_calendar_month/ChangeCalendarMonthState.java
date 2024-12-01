package interface_adapter.change_calendar_month;

import entity.Calendar;
import entity.Event;
import entity.User;

import java.util.ArrayList;
import java.util.List;

public class ChangeCalendarMonthState {
  private User currentUser = null;
  private Calendar googleCalendar = null;
  private Calendar notionCalendar = null;
  private Calendar outlookCalendar = null;
  private Calendar activeCalendar = null;
  private List<Calendar> currCalendarList = new ArrayList<>();
  private List<Event> currEvents = new ArrayList<>();
  private String currMonth = "";
  private Integer currYear = null;
  private String error = "";

  // Copy constructor
  public ChangeCalendarMonthState(ChangeCalendarMonthState copy) {
    currentUser = copy.currentUser;
    googleCalendar = copy.googleCalendar;
    notionCalendar = copy.notionCalendar;
    outlookCalendar = copy.outlookCalendar;
    activeCalendar = copy.activeCalendar;
    currCalendarList = new ArrayList<>(copy.currCalendarList);
    currEvents = new ArrayList<>(copy.currEvents);
    currMonth = copy.currMonth;
    currYear = copy.currYear;
    error = copy.error;
  }

  // Default constructor
  public ChangeCalendarMonthState() {}

  // Getters and setters with defensive copying for collections
  public Calendar getActiveCalendar() {
    return activeCalendar;
  }

  public void setActiveCalendar(Calendar calendar) {
    this.activeCalendar = calendar;
  }

  public List<Calendar> getCurrCalendarList() {
    return new ArrayList<>(currCalendarList);
  }

  public void setCurrCalendarList(List<Calendar> calendarList) {
    this.currCalendarList = new ArrayList<>(calendarList);
  }

  public List<Event> getCurrEvents() {
    return new ArrayList<>(currEvents);
  }

  public void setCurrEvents(List<Event> events) {
    this.currEvents = new ArrayList<>(events);
  }

  public String getCurrMonth() {
    return currMonth;
  }

  public void setCurrMonth(String currMonth) {
    this.currMonth = currMonth;
  }

  public Integer getCurrYear() {
    return currYear;
  }

  public void setCurrYear(Integer currYear) {
    this.currYear = currYear;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
  }

  public Calendar getGoogleCalendar() {
    return googleCalendar;
  }

  public void setGoogleCalendar(Calendar googleCalendar) {
    this.googleCalendar = googleCalendar;
  }

  public Calendar getNotionCalendar() {
    return notionCalendar;
  }

  public void setNotionCalendar(Calendar notionCalendar) {
    this.notionCalendar = notionCalendar;
  }

  public Calendar getOutlookCalendar() {
    return outlookCalendar;
  }

  public void setOutlookCalendar(Calendar outlookCalendar) {
    this.outlookCalendar = outlookCalendar;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}