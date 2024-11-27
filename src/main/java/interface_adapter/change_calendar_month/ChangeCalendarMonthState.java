package interface_adapter.change_calendar_month;

import entity.Calendar;
import entity.Event;
import entity.User;
import java.util.List;
import java.util.ArrayList;

public class ChangeCalendarMonthState {
  private User currentUser;
  private Calendar googleCalendar;
  private Calendar notionCalendar;
  private Calendar outlookCalendar;
  private Calendar activeCalendar; // Add this field
  private List<Calendar> currCalendarList = new ArrayList<>();
  private String currMonth;
  private Integer currYear;
  private ArrayList<Event> currEvents = new ArrayList<>();;

  // Add getter and setter for activeCalendar
  public Calendar getActiveCalendar() {
    return activeCalendar;
  }

  public void setActiveCalendar(Calendar calendar) {
    this.activeCalendar = calendar;
  }


//  private List<Calendar> currCalendarList = new ArrayList<>();

  public void setCurrCalendarList(List<Calendar> calendarList) {
    // Add validation and proper copying
    this.currCalendarList = new ArrayList<>(calendarList);
  }

  public List<Calendar> getCurrCalendarList() {
    // Return defensive copy
    return new ArrayList<>(currCalendarList);
  }

    public void addCalendar(Calendar calendar) {
        this.currCalendarList.add(calendar);
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

    public List<Event> getCurrEvents() {
      return new ArrayList<>(currEvents);
    }
  
    public void setCurrEvents(List<Event> events) {
      this.currEvents = new ArrayList<>(events);
    }
}

