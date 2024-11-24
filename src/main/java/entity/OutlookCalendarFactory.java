package entity;

public class OutlookCalendarFactory extends CalendarFactory {
    @Override
    Calendar create(String calendarName, String credentials, String accountName) {
        if (!"Outlook".equals(calendarName)) {
            throw new IllegalArgumentException("This factory only creates Outlook Calendars");
        }

        return new OutlookCalendar(calendarName, credentials, accountName);
    }
}
