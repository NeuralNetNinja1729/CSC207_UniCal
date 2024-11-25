package data_access;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.EventDateTime;
import entity.GoogleCalendar;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

/**
 * Data Access Object for Google Calendar.
 */
public class GoogleCalendarDataAccessObject implements GetEventsDataAccessInterface, AddEventDataAccessInterface {

  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final String APPLICATION_NAME = "UniCal";
  private final GoogleCalendar calendar;
  private Calendar service;

  public GoogleCalendarDataAccessObject(GoogleCalendar calendar) {
    this.calendar = calendar;
    initializeService();
  }

  private void initializeService() {
    try {
      service = new Calendar.Builder(
        GoogleNetHttpTransport.newTrustedTransport(),
        JSON_FACTORY,
        calendar.getHttpRequestInitializer())
        .setApplicationName(APPLICATION_NAME)
        .build();
    } catch (GeneralSecurityException | IOException e) {
      System.err.println("Error initializing Google Calendar service: " + e.getMessage());
    }
  }

  @Override
  public ArrayList<entity.Event> fetchEventsDay(LocalDate date) {
    // Convert LocalDate to DateTime for Google Calendar API
    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

    return fetchEvents(startOfDay, endOfDay);
  }

  @Override
  public ArrayList<entity.Event> fetchEventsMonth(LocalDate date) {
    // Get start and end of the month
    LocalDateTime startOfMonth = date.withDayOfMonth(1).atStartOfDay();
    LocalDateTime endOfMonth = date.plusMonths(1).withDayOfMonth(1).atStartOfDay();

    return fetchEvents(startOfMonth, endOfMonth);
  }

  private ArrayList<entity.Event> fetchEvents(LocalDateTime start, LocalDateTime end) {
    ArrayList<entity.Event> events = new ArrayList<>();

    try {
      // Convert LocalDateTime to Date
      DateTime startDateTime = new DateTime(
        Date.from(start.atZone(ZoneId.systemDefault()).toInstant())
      );
      DateTime endDateTime = new DateTime(
        Date.from(end.atZone(ZoneId.systemDefault()).toInstant())
      );

      // Create and execute the events list request
      Events eventsList = service.events().list(calendar.getCalendarId())
        .setTimeMin(startDateTime)
        .setTimeMax(endDateTime)
        .setOrderBy("startTime")
        .setSingleEvents(true)
        .execute();

      // Parse events from the response
      parseEventsFromResponse(eventsList, events);

    } catch (IOException e) {
      System.err.println("Error fetching events: " + e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return events;
  }

  private void parseEventsFromResponse(Events eventsList, ArrayList<entity.Event> events) {
    for (Event googleEvent : eventsList.getItems()) {
      try {
        // Get event start date
        DateTime startDateTime = googleEvent.getStart().getDateTime();
        if (startDateTime == null) {
          // If dateTime is null, it's an all-day event, use date instead
          startDateTime = googleEvent.getStart().getDate();
        }

        // Convert to LocalDate
        LocalDate eventDate = LocalDate.parse(
          startDateTime.toStringRfc3339().split("T")[0]
        );

        // Create and add event
        entity.Event event = new entity.Event(
          googleEvent.getSummary(),
          eventDate,
          calendar
        );
        events.add(event);
      } catch (Exception e) {
        System.err.println("Error parsing event: " + e.getMessage());
      }
    }
  }

  @Override
  public boolean addEvent(entity.Event event) {
    try {
      // Create event
      Event googleEvent = createGoogleEvent(event);

      // Insert event
      service.events().insert(calendar.getCalendarId(), googleEvent).execute();
      return true;
    } catch (IOException e) {
      System.err.println("Error adding event: " + e.getMessage());
      return false;
    }
  }

  private Event createGoogleEvent(entity.Event event) {
    Event googleEvent = new Event()
      .setSummary(event.getEventName());

    // Set event date and time
    LocalDateTime startDateTime = event.getDate().atStartOfDay();
    LocalDateTime endDateTime = startDateTime.plusHours(1); // Default 1-hour duration

    // Convert to Google DateTime format
    DateTime start = new DateTime(
      Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant())
    );
    DateTime end = new DateTime(
      Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant())
    );

    // Create EventDateTime objects
    EventDateTime startEventDateTime = new EventDateTime().setDateTime(start);
    EventDateTime endEventDateTime = new EventDateTime().setDateTime(end);

    // Set the start and end times
    googleEvent.setStart(startEventDateTime)
      .setEnd(endEventDateTime);

    return googleEvent;
  }
}
