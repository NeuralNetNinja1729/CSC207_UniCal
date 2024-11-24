package entity;

public class CalendarFactory {
    Calendar create(String calendarName, String credentials, String accountName) {
        switch (calendarName){
            case "Google": return new GoogleCalendar(calendarName, credentials, accountName);
            case "Notion": return new NotionCalendar(calendarName, credentials, accountName);
            case "Outlook": return new OutlookCalendar(calendarName, credentials, accountName);
            default: return null;
        }
    }
}
