package entity;

/**
 * Represents a Notion Calendar.
 */
public class NotionCalendar implements Calendar {

    private final String notionToken;
    private final String databaseID;
    private final String calendarName;

    public NotionCalendar(String notionToken, String databaseID, String calendarName) {
        this.notionToken = notionToken;
        this.databaseID = databaseID;
        this.calendarName = calendarName;
    }

    @Override
    public String getCalendarApiName() {
        return "NotionCalendar";
    }

    @Override
    public String getCalendarName() {
        return calendarName;
    }

    public String getNotionToken() {
        return notionToken;
    }

    public String getDatabaseID() {
        return databaseID;
    }
}
