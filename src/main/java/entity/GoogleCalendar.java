package entity;

public class GoogleCalendar implements Calendar {
    private String calendarName;
    private String credentials;
    private String accountName;

    public GoogleCalendar(String calendarName, String credentials, String accountName) {
        this.setCalendarName(calendarName);
        this.setCredentials(credentials);
        this.setAccountName(accountName);
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
}
