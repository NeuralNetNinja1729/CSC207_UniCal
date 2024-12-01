package interface_adapter.change_calendar_day;

import entity.Calendar;
import entity.User;

import java.util.ArrayList;
import java.util.List;

public class ChangeCalendarDayState {
    private User currentUser;
    private Calendar googleCalendar;
    private Calendar notionCalendar;
    private Calendar outlookCalendar;
    private List<Calendar> currCalendarList = new ArrayList<>();
    private List<String> eventList = new ArrayList<>();
    private String currMonth;
    private Integer currYear;
    private Integer currDay;

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

    public List<Calendar> getCurrCalendarList() {
        return currCalendarList;
    }

    public void setCurrCalendarList(List<Calendar> currCalendarList) {
        this.currCalendarList = currCalendarList;
    }

    public List<String> getEventList() {
        return eventList;
    }

    public void setEventList(List<String> eventList) {
        this.eventList = eventList;
    }

    public void addEvent(String eventDetails) {
        eventList.add(eventDetails);
    }

    public void deleteEvent(String eventDetails) {
        eventList.remove(eventDetails);
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

    public Integer getCurrDay() {
        return currDay;
    }

    public void setCurrDay(Integer currDay) {
        this.currDay = currDay;
    }
}
