package entity;

public class NotionCalendar implements Calendar {
    private String calendarName;
    private String credentials;
    private String accountName;

    public NotionCalendar(String calendarName, String credentials, String accountName) {
        this.calendarName = calendarName;
        this.credentials = credentials;
        this.accountName = accountName;
    }
}