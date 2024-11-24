package entity;

public class NotionCalendarFactory extends CalendarFactory{
    @Override
    Calendar create(String calendarName, String credentials, String accountName) {
        if (!"Notion".equals(calendarName)) {
            throw new IllegalArgumentException("This factory only creates Notion Calendars");
        }

        return new NotionCalendar(calendarName, credentials, accountName);
    }
}
