package data_access;

import entity.Calendar;
import entity.Event;
import entity.NotionCalendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Data Access Object for Notion Calendar.
 */
public class NotionCalendarDataAccessObject implements GetEventsDataAccessInterface, AddEventDataAccessInterface {

    private static final String DATE_PROPERTY_NAME = "Due Date";
    private static final String DATE = "date";
    private static final String PROPERTY = "property";
    private static final String TYPE = "type";
    private static final String TITLE = "title";
    private static final String NOTION_API_VERSION = "2022-06-28";
    private static final String NOTION_API_BASE_URL = "https://api.notion.com/v1";
    private final NotionCalendar calendar;

    public NotionCalendarDataAccessObject(NotionCalendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public ArrayList<Event> fetchEventsDay(LocalDate date) {
        // Prepare request Data
        final JSONObject requestData = createRequestDataDay(date);
        return fetchEvents(requestData);
    }

    @Override
    public ArrayList<Event> fetchEventsMonth(LocalDate date) {
        // Prepare request Data
        final JSONObject requestData = createRequestDataMonth(date);
        return fetchEvents(requestData);
    }

    private ArrayList<Event> fetchEvents(JSONObject requestData) {
    final ArrayList<Event> events = new ArrayList<>();
    try {
      // Prepare request details
      final String notionApiKey = calendar.getNotionToken();
      final String dbId = calendar.getDatabaseID();
      final HttpURLConnection connection = createConnection(notionApiKey, dbId);

      sendRequest(connection, requestData);

      // Handle the response
      final int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        final String response = getResponse(connection);
        parseEventsFromResponse(response, events, calendar);
      } else {
        // Enhanced error logging
        String errorResponse = "";
        try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(connection.getErrorStream()))) {
          String line;
          StringBuilder responseBuilder = new StringBuilder();
          while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
          }
          errorResponse = responseBuilder.toString();
        } catch (Exception e) {
          errorResponse = "Could not read error response";
        }
        System.err.println("Failed to fetch events. Response code: " + responseCode);
        System.err.println("Error response: " + errorResponse);
      }
    } catch (Exception exception) {
      System.err.println("Error fetching Notion events: " + exception.getMessage());
      exception.printStackTrace();
    }
    return events;
  }

    private HttpURLConnection createConnection(String notionApiKey, String dbId) throws Exception {
        final String urlString = NOTION_API_BASE_URL + "/databases/" + dbId + "/query";
        final URL url = new URL(urlString);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + notionApiKey);
        connection.setRequestProperty("Notion-Version", NOTION_API_VERSION);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        return connection;
    }

    private JSONObject createRequestDataMonth(LocalDate date) {

        // Get the start and end of the month
        final String startOfMonth = DateUtils.getStartOfMonth(date);
        final String endOfMonth = DateUtils.getEndOfMonth(date);

        final JSONObject filter = new JSONObject();
        final JSONObject startDateFilter = new JSONObject()
                .put(PROPERTY, DATE_PROPERTY_NAME)
                .put(DATE, new JSONObject().put("on_or_after", startOfMonth));

        final JSONObject endDateFilter = new JSONObject()
                .put(PROPERTY, DATE_PROPERTY_NAME)
                .put(DATE, new JSONObject().put("on_or_before", endOfMonth));

        filter.put("and", new JSONArray().put(startDateFilter).put(endDateFilter));

        final JSONObject sorts = new JSONObject();
        sorts.put(PROPERTY, DATE_PROPERTY_NAME);
        sorts.put("direction", "ascending");

        final JSONObject requestData = new JSONObject();
        requestData.put("filter", filter);
        requestData.put("sorts", new JSONArray().put(sorts));
        return requestData;
    }

    private JSONObject createRequestDataDay(LocalDate date) {

        final String dateString = DateUtils.getDateString(date);

        final JSONObject filter = new JSONObject();
        filter.put(PROPERTY, DATE_PROPERTY_NAME)
                        .put(DATE, new JSONObject()
                                .put("equals", dateString));

        final JSONObject requestData = new JSONObject();
        requestData.put("filter", filter);
        requestData.put("sorts", new JSONArray());
        return requestData;
    }

    private void sendRequest(HttpURLConnection connection, JSONObject requestData) throws Exception {
        try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
            out.writeBytes(requestData.toString());
            out.flush();
        }
    }

    private String getResponse(HttpURLConnection connection) throws Exception {
        final StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }

    private void parseEventsFromResponse(String response, ArrayList<Event> events, Calendar calendarApi) {
        final JSONObject res = new JSONObject(response);
        final JSONArray results = res.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            final JSONObject result = results.getJSONObject(i);
            final JSONObject properties = result.getJSONObject("properties");

            final String eventName = properties.getJSONObject("Name")
                    .getJSONArray(TITLE)
                    .getJSONObject(0)
                    .getString("plain_text");

            final String dateString = properties.getJSONObject(DATE_PROPERTY_NAME)
                    .getJSONObject("date")
                    .getString("start");

            final LocalDate date = LocalDate.parse(dateString);

            events.add(new Event(eventName, date, calendarApi));
        }
    }

    @Override
    public boolean addEvent(Event event) {
        boolean result = false;
        try {
            // Get Notion API key and Database ID from the calendar object
            final String notionApiKey = calendar.getNotionToken();
            final String dbId = calendar.getDatabaseID();

            final JSONObject eventData = createAddEventRequestData(dbId, event);

            // Create a connection to the Notion API
            final HttpURLConnection connection = createAddEventConnection(notionApiKey);

            // Send the request data
            sendRequest(connection, eventData);

            // Get the response code
            final int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                result = true;
            }
            else {
                System.out.println("Failed to add event. Response code: " + responseCode);
            }
        }
        catch (Exception exception) {
            System.err.println("Error adding event: " + exception.getMessage());
            exception.printStackTrace();
        }
        return result;
    }

    private JSONObject createAddEventRequestData(String dbId, Event event) {
        // Create the event data in JSON format for the Notion API
        final JSONObject eventData = new JSONObject();

        // Specify the parent database ID
        final JSONObject parent = new JSONObject();
        parent.put(TYPE, "database_id");
        parent.put("database_id", dbId);
        eventData.put("parent", parent);

        // Specify the event properties (for example, event name and date)
        final JSONObject properties = new JSONObject();

        // Event name
        final JSONObject eventNameProperty = new JSONObject();
        eventNameProperty.put(TYPE, TITLE);
        final JSONArray titleArray = new JSONArray();
        final JSONObject titleText = new JSONObject();
        titleText.put(TYPE, "text");
        titleText.put("text", new JSONObject().put("content", event.getEventName()));
        titleArray.put(titleText);
        eventNameProperty.put(TITLE, titleArray);

        // Add event name property
        properties.put("Name", eventNameProperty);

        // Event date (assuming you want to use "Due Date" as the date)
        final JSONObject dateProperty = new JSONObject();
        dateProperty.put(TYPE, DATE);
        final JSONObject dateDetails = new JSONObject();
        final LocalDate date = event.getDate();
        dateDetails.put("start", DateUtils.getDateString(date));
        dateProperty.put(DATE, dateDetails);

        // Add date property
        properties.put(DATE_PROPERTY_NAME, dateProperty);

        // Add properties to the event data
        eventData.put("properties", properties);

        return eventData;
    }

    private HttpURLConnection createAddEventConnection(String notionApiKey) throws Exception {
        final URL url = new URL(NOTION_API_BASE_URL + "/pages");
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + notionApiKey);
        connection.setRequestProperty("Notion-Version", NOTION_API_VERSION);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        return connection;
    }

}
