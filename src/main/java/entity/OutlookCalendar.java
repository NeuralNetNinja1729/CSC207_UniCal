package entity;

/**
 * Represents an Outlook Calendar.
 */
public class OutlookCalendar implements Calendar {

    private final String credentials;
    private final String accountName;
    private final String calendarName;

    public OutlookCalendar(String credentials, String accountName, String calendarName) {
        this.credentials = credentials;
        this.accountName = accountName;
        this.calendarName = calendarName;
    }

    @Override
    public String getCalendarApiName() {
        return "OutlookCalendar";
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
