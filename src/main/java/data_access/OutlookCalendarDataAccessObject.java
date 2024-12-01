package data_access;

import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.Event;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.GraphServiceClient;
import entity.OutlookCalendar;
import okhttp3.Request;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OutlookCalendarDataAccessObject implements GetEventsDataAccessInterface, AddEventDataAccessInterface {
  private static final String APPLICATION_NAME = "UniCal";
  private final OutlookCalendar calendar;
  private GraphServiceClient<Request> graphClient;

  public OutlookCalendarDataAccessObject(OutlookCalendar calendar) {
    this.calendar = calendar;
    initializeClient();
  }

  private void initializeClient() {
    try {
      graphClient = GraphServiceClient.builder()
              .authenticationProvider(calendar.getAuthProvider())
              .buildClient();
    } catch (IOException e) {
      System.err.println("Error initializing Outlook Calendar client: " + e.getMessage());
    }
  }

  @Override
  public ArrayList<entity.Event> fetchEventsDay(LocalDate date) {
    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
    return fetchEvents(startOfDay, endOfDay);
  }

  @Override
  public ArrayList<entity.Event> fetchEventsMonth(LocalDate date) {
    LocalDateTime startOfMonth = date.withDayOfMonth(1).atStartOfDay();
    LocalDateTime endOfMonth = date.plusMonths(1).withDayOfMonth(1).atStartOfDay();
    return fetchEvents(startOfMonth, endOfMonth);
  }

  private ArrayList<entity.Event> fetchEvents(LocalDateTime start, LocalDateTime end) {
    ArrayList<entity.Event> events = new ArrayList<>();

    try {
      DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
      String startTime = start.format(formatter);
      String endTime = end.format(formatter);

      List<QueryOption> requestOptions = new LinkedList<>();
      requestOptions.add(new QueryOption("startDateTime", startTime));
      requestOptions.add(new QueryOption("endDateTime", endTime));

      graphClient.users(calendar.getAccountName())
              .calendar()
              .calendarView()
              .buildRequest(requestOptions)
              .select("subject,start,end")
              .get()
              .getCurrentPage()
              .forEach(event -> parseEvent(event, events));

    } catch (ClientException e) {
      System.err.println("Error fetching events: " + e.getMessage());
      e.printStackTrace();
    }

    return events;
  }

  private void parseEvent(Event outlookEvent, ArrayList<entity.Event> events) {
    try {
      // Parse start time
      ZonedDateTime startZdt = ZonedDateTime.parse(outlookEvent.start.dateTime,
              DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of(outlookEvent.start.timeZone)));

      // Parse end time
      ZonedDateTime endZdt = ZonedDateTime.parse(outlookEvent.end.dateTime,
              DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of(outlookEvent.end.timeZone)));

      // Convert to local date and time
      LocalDate eventDate = startZdt.toLocalDate();
      LocalTime startTime = startZdt.toLocalTime();
      LocalTime endTime = endZdt.toLocalTime();

      entity.Event event = new entity.Event(
              outlookEvent.subject != null ? outlookEvent.subject : "Untitled Event",
              eventDate,
              startTime,
              endTime,
              calendar
      );
      events.add(event);
    } catch (Exception e) {
      System.err.println("Error parsing event: " + e.getMessage());
    }
  }

  @Override
  public boolean addEvent(entity.Event event) {
    try {
      Event outlookEvent = new Event();
      outlookEvent.subject = event.getEventName();

      // Create start time
      com.microsoft.graph.models.DateTimeTimeZone start = new com.microsoft.graph.models.DateTimeTimeZone();
      LocalDateTime startDateTime = LocalDateTime.of(event.getDate(), event.getStartTime());
      start.dateTime = startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      start.timeZone = ZoneId.systemDefault().toString();
      outlookEvent.start = start;

      // Create end time
      com.microsoft.graph.models.DateTimeTimeZone end = new com.microsoft.graph.models.DateTimeTimeZone();
      LocalDateTime endDateTime = LocalDateTime.of(event.getDate(), event.getEndTime());
      end.dateTime = endDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      end.timeZone = ZoneId.systemDefault().toString();
      outlookEvent.end = end;

      graphClient.users(calendar.getAccountName())
              .calendar()
              .events()
              .buildRequest()
              .post(outlookEvent);

      return true;
    } catch (ClientException e) {
      System.err.println("Error adding event: " + e.getMessage());
      return false;
    }
  }
}