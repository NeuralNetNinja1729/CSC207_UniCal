package entity;

public class GoogleCalendarFactory extends CalendarFactory{
    @Override
    Calendar create(String calendarName, String credentials, String accountName) {
        if (!"Google".equals(calendarName)) {
            throw new IllegalArgumentException("This factory only creates Google Calendars");
        }

        return new GoogleCalendar(calendarName, credentials, accountName);
    }
}
