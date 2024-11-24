package entity;

public class GoogleCalendar implements Calendar {
    private String calendarName;
    private String credentials;
    private String accountName;

    public GoogleCalendar(String calendarName, String credentials, String accountName) {
        this.calendarName = calendarName;
        this.credentials = credentials;
        this.accountName = accountName;
    }
}
