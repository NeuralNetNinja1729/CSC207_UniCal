package data_access;

import entity.Calendar;
import entity.Event;
import entity.NotionCalendar;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NotionCalendarDataAccessObject implements GetEventsDataAccessInterface, AddEventDataAccessInterface, DeleteEventDataAccessInterface {
    private static final String DATE_PROPERTY_NAME = "Due Date";
    private static final String START_TIME_PROPERTY_NAME = "Start Time";
    private static final String END_TIME_PROPERTY_NAME = "End Time";
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
        final JSONObject requestData = createRequestDataDay(date);
        return fetchEvents(requestData);
    }

    @Override
    public ArrayList<Event> fetchEventsMonth(LocalDate date) {
        final JSONObject requestData = createRequestDataMonth(date);
        return fetchEvents(requestData);
    }

    private ArrayList<Event> fetchEvents(JSONObject requestData) {
        final ArrayList<Event> events = new ArrayList<>();
        try {
            final String notionApiKey = calendar.getNotionToken();
            final String dbId = calendar.getDatabaseID();
            final HttpURLConnection connection = createConnection(notionApiKey, dbId);

            sendRequest(connection, requestData);

            final int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                final String response = getResponse(connection);
                parseEventsFromResponse(response, events, calendar);
            } else {
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

    private void parseEventsFromResponse(String response, ArrayList<Event> events, Calendar calendarApi) {
        try {
            final JSONObject res = new JSONObject(response);
            final JSONArray results = res.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                try {
                    final JSONObject result = results.getJSONObject(i);
                    final JSONObject properties = result.getJSONObject("properties");

                    // Get event name from title property
                    final String eventName = properties.getJSONObject("Name")
                            .getJSONArray(TITLE)
                            .getJSONObject(0)
                            .getString("plain_text");

                    // Get date from date property
                    final JSONObject dueDateProperty = properties.getJSONObject(DATE_PROPERTY_NAME);
                    if (!dueDateProperty.has(DATE) || dueDateProperty.getJSONObject(DATE).isNull("start")) {
                        continue; // Skip events with invalid dates
                    }

                    final String dateString = dueDateProperty.getJSONObject(DATE).getString("start");
                    LocalDate date = LocalDate.parse(dateString.split("T")[0]); // Handle both date-only and datetime formats

                    // Parse start time
                    LocalTime startTime = LocalTime.of(0, 0);
                    if (properties.has(START_TIME_PROPERTY_NAME)) {
                        JSONObject startTimeProperty = properties.getJSONObject(START_TIME_PROPERTY_NAME);
                        if (startTimeProperty.has("rich_text") && !startTimeProperty.getJSONArray("rich_text").isEmpty()) {
                            String startTimeStr = startTimeProperty.getJSONArray("rich_text")
                                    .getJSONObject(0)
                                    .getJSONObject("text")
                                    .getString("content");
                            startTime = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
                        }
                    }

                    // Parse end time
                    LocalTime endTime = LocalTime.of(23, 59);
                    if (properties.has(END_TIME_PROPERTY_NAME)) {
                        JSONObject endTimeProperty = properties.getJSONObject(END_TIME_PROPERTY_NAME);
                        if (endTimeProperty.has("rich_text") && !endTimeProperty.getJSONArray("rich_text").isEmpty()) {
                            String endTimeStr = endTimeProperty.getJSONArray("rich_text")
                                    .getJSONObject(0)
                                    .getJSONObject("text")
                                    .getString("content");
                            endTime = LocalTime.parse(endTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
                        }
                    }

                    events.add(new Event(eventName, date, startTime, endTime, calendarApi));
                } catch (Exception e) {
                    System.err.println("Error parsing individual event: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing events response: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private HttpURLConnection createConnection(String notionApiKey, String dbId) throws IOException {
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

    @Override
    public boolean addEvent(Event event) {
        try {
            final String notionApiKey = calendar.getNotionToken();
            final String dbId = calendar.getDatabaseID();

            final JSONObject eventData = createAddEventRequestData(dbId, event);

            final HttpURLConnection connection = createAddEventConnection(notionApiKey);

            sendRequest(connection, eventData);

            final int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                System.out.println("Failed to add event. Response code: " + responseCode);
                return false;
            }
        } catch (Exception exception) {
            System.err.println("Error adding event: " + exception.getMessage());
            exception.printStackTrace();
            return false;
        }
    }

    private void sendRequest(HttpURLConnection connection, JSONObject requestData) throws IOException {
        try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
            out.writeBytes(requestData.toString());
            out.flush();
        }
    }

    private String getResponse(HttpURLConnection connection) throws IOException {
        final StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }

    private JSONObject createAddEventRequestData(String dbId, Event event) {
        final JSONObject eventData = new JSONObject();

        // Parent database ID
        final JSONObject parent = new JSONObject();
        parent.put(TYPE, "database_id");
        parent.put("database_id", dbId);
        eventData.put("parent", parent);

        // Properties object
        final JSONObject properties = new JSONObject();

        // Event name (Title)
        final JSONObject titleProperty = new JSONObject();
        titleProperty.put(TYPE, TITLE);
        JSONArray titleArray = new JSONArray();
        JSONObject titleText = new JSONObject();
        titleText.put(TYPE, "text");
        titleText.put("text", new JSONObject().put("content", event.getEventName()));
        titleArray.put(titleText);
        titleProperty.put(TITLE, titleArray);
        properties.put("Name", titleProperty);

        // Date property
        final JSONObject dateProperty = new JSONObject();
        dateProperty.put(TYPE, DATE);
        dateProperty.put(DATE, new JSONObject().put("start", event.getDate().toString()));
        properties.put(DATE_PROPERTY_NAME, dateProperty);

        // Start Time property
        final JSONObject startTimeProperty = new JSONObject();
        startTimeProperty.put(TYPE, "rich_text");
        JSONArray startTimeArray = new JSONArray();
        JSONObject startTimeText = new JSONObject();
        startTimeText.put(TYPE, "text");
        startTimeText.put("text", new JSONObject().put("content", event.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
        startTimeArray.put(startTimeText);
        startTimeProperty.put("rich_text", startTimeArray);
        properties.put(START_TIME_PROPERTY_NAME, startTimeProperty);

        // End Time property
        final JSONObject endTimeProperty = new JSONObject();
        endTimeProperty.put(TYPE, "rich_text");
        JSONArray endTimeArray = new JSONArray();
        JSONObject endTimeText = new JSONObject();
        endTimeText.put(TYPE, "text");
        endTimeText.put("text", new JSONObject().put("content", event.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
        endTimeArray.put(endTimeText);
        endTimeProperty.put("rich_text", endTimeArray);
        properties.put(END_TIME_PROPERTY_NAME, endTimeProperty);

        eventData.put("properties", properties);

        return eventData;
    }

    private HttpURLConnection createAddEventConnection(String notionApiKey) throws IOException {
        final URL url = new URL(NOTION_API_BASE_URL + "/pages");
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + notionApiKey);
        connection.setRequestProperty("Notion-Version", NOTION_API_VERSION);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        return connection;
    }

    @Override
    public boolean deleteEvent(Event event) {
        boolean result = false;
        try {
            // Get the Notion API key and page ID from the event object
            final String notionApiKey = calendar.getNotionToken();
            final String pageId = getEventID(event);

            // Create a connection to the Notion API
            final int responseCode = createDeleteEventConnection(notionApiKey, pageId);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                result = true;
            } else {
                System.out.println("Failed to delete event! Response code: " + responseCode);
            }
        } catch (Exception exception) {
            System.err.println("Error deleting event: " + exception.getMessage());
            exception.printStackTrace();
        }
        return result;
    }

    public String getEventID(Event event) {
        String pageId = null;
        try {
            // Prepare request details
            final String notionApiKey = calendar.getNotionToken();
            final String dbId = calendar.getDatabaseID();
            final LocalDate eventDate = event.getDate();
            final String eventName = event.getEventName();

            // Create request payload to filter events for the specific date
            final JSONObject requestData = new JSONObject();
            final JSONObject dateFilter = new JSONObject()
                    .put(PROPERTY, DATE_PROPERTY_NAME)
                    .put(DATE, new JSONObject().put("equals", DateUtils.getDateString(eventDate)));

            requestData.put("filter", new JSONObject().put("and", new JSONArray().put(dateFilter)));

            // Create a connection to the Notion API
            final HttpURLConnection connection = createConnection(notionApiKey, dbId);

            // Send the request
            sendRequest(connection, requestData);

            // Handle the response
            final int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                final String response = getResponse(connection);

                // Parse the response to find the matching page
                final JSONObject res = new JSONObject(response);
                final JSONArray results = res.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    final JSONObject result = results.getJSONObject(i);
                    final JSONObject properties = result.getJSONObject("properties");

                    final String pageName = properties.getJSONObject("Name")
                            .getJSONArray(TITLE)
                            .getJSONObject(0)
                            .getString("plain_text");

                    // Check if the event name matches
                    if (pageName.equals(eventName)) {
                        pageId = result.getString("id");
                        break;
                    }
                }
            } else {
                System.out.println("Failed to fetch pages. Response code: " + responseCode);
                String errorResponse = getErrorResponse(connection);
                System.err.println("Error response: " + errorResponse);
            }
        } catch (Exception exception) {
            System.err.println("Error fetching event ID: " + exception.getMessage());
            exception.printStackTrace();
        }
        return pageId;
    }


    private int createDeleteEventConnection(String notionApiKey, String pageId) throws Exception {
        OkHttpClient client = new OkHttpClient();

        // Construct the URL
        String url = NOTION_API_BASE_URL + "/pages/" + pageId;
        System.out.println(url);

        // Create the request body
        RequestBody body = RequestBody.create(
                "{\"archived\": true}",
                MediaType.parse("application/json")
        );

        // Build the PATCH request
        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .addHeader("Authorization", "Bearer " + notionApiKey)
                .addHeader("Notion-Version", NOTION_API_VERSION)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Failed to delete event. Response code: " + response.code());
                // Print the response body for more details
                System.out.println("Response body: " + response.body().string());
                return response.code();
            } else {
                System.out.println("Event deleted successfully!");
                System.out.println("Response body: " + response.body().string());
                return response.code();
            }
        } catch (IOException e) {
            System.out.println("Error while sending the request: " + e.getMessage());
            return 400;
        }
    }

    private String getErrorResponse(HttpURLConnection connection) {
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
        return errorResponse;
    }
}