package interface_adapter.change_calendar_day;

import entity.Calendar;
import entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeCalendarDayState {
    private User currentUser;
    private Calendar googleCalendar;
    private Calendar notionCalendar;
    private Calendar outlookCalendar;
    private List<Calendar> currCalendarList = new ArrayList<>();
    private Map<String, String> eventMap = new HashMap<>();
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

    public Map<String, String> getEventMap() {
        return eventMap;
    }

    public void setEventMap(Map<String, String> eventMap) {
        this.eventMap = eventMap;
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