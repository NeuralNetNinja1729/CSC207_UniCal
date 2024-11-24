package entity;

import java.util.List;
import java.util.ArrayList;

public class NotionCalendar implements Calendar {
    private String calendarName;
    private String credentials;
    private String accountName;
    private List<Event> events;

    public NotionCalendar(String calendarName, String credentials, String accountName) {
        this.calendarName = calendarName;
        this.credentials = credentials;
        this.accountName = accountName;
        this.events = new ArrayList<>();
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public List<Event> getEvents() {
        return events;
    }

    @Override
    public void addEvent(Event event) {
        events.add(event);
    }
}