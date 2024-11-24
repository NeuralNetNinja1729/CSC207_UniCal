package entity;

public class NotionCalendar implements Calendar {
    private final String calendarName;
    private final String credentials;
    private final String accountName;

    public NotionCalendar(String calendarName, String credentials, String accountName) {
        this.calendarName = calendarName;
        this.credentials = credentials;
        this.accountName = accountName;
    }

    @Override
    public String getCalendarName() {
        return calendarName;
    }

    @Override
    public String getCredentials() {
        return credentials;
    }

    @Override
    public String getAccountName() {
        return accountName;
    }
}