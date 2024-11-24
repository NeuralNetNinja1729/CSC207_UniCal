package entity;

/**
 * Represents a Google Calendar.
 */
public class GoogleCalendar implements Calendar {
    private final String calendarName;
    private final String credentials;
    private final String accountName;

    public GoogleCalendar(String calendarName, String credentials, String accountName) {
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
