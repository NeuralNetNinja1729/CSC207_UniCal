package entity;

public class OutlookCalendar implements Calendar {
    private String calendarName;
    private String credentials;
    private String accountName;

    public OutlookCalendar(String calendarName, String credentials, String accountName) {
        this.calendarName = calendarName;
        this.credentials = credentials;
        this.accountName = accountName;
    }
}
