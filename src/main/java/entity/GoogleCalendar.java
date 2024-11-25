package entity;

/**
 * Represents a Google Calendar.
 */
public class GoogleCalendar implements Calendar {
    private final String credentials;
    private final String accountName;
    private final String calendarName;

    public GoogleCalendar(String credentials, String accountName, String calendarName) {
        this.credentials = credentials;
        this.accountName = accountName;
        this.calendarName = calendarName;
    }

    @Override
    public String getCalendarApiName() {
        return "GoogleCalendar";
    }

    @Override
    public String getCalendarName() {
        return calendarName;
    }

    public String getCredentials() {
        return credentials;
    }

    public String getAccountName() {
        return accountName;
    }
}
