package entity;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;




/**
 * Represents a Google Calendar.
 */
public class GoogleCalendar implements Calendar {
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final String CALENDAR_SCOPE = "https://www.googleapis.com/auth/calendar";

  private final String credentials;
  private final String accountName;
  private final String calendarName;
  private final String calendarId;
  private GoogleCredential googleCredential;

  /**
   * Creates a new Google Calendar instance.
   * @param credentials The JSON credentials string from Google Cloud Console
   * @param accountName The account name (email) associated with the calendar
   * @param calendarName The display name for the calendar
   * @param calendarId The Google Calendar ID (usually email or "primary")
   * @throws IllegalArgumentException if any required parameter is null or empty
   */
  public GoogleCalendar(String credentials, String accountName, String calendarName, String calendarId) {
    if (credentials == null || credentials.trim().isEmpty()) {
      throw new IllegalArgumentException("Credentials cannot be null or empty");
    }
    if (accountName == null || accountName.trim().isEmpty()) {
      throw new IllegalArgumentException("Account name cannot be null or empty");
    }
    if (calendarName == null || calendarName.trim().isEmpty()) {
      throw new IllegalArgumentException("Calendar name cannot be null or empty");
    }
    if (calendarId == null || calendarId.trim().isEmpty()) {
      throw new IllegalArgumentException("Calendar ID cannot be null or empty");
    }

    this.credentials = credentials;
    this.accountName = accountName;
    this.calendarName = calendarName;
    this.calendarId = calendarId;

    // Validate credentials format
    try {
      initializeCredential();
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid credentials format: " + e.getMessage());
    }
  }

  @Override
  public String getCalendarApiName() {
    return "GoogleCalendar";
  }

  @Override
  public String getCalendarName() {
    return calendarName;
  }

  /**
   * Gets the raw credentials string.
   * @return the credentials JSON string
   */
  public String getCredentials() {
    return credentials;
  }

  public String getAccountName() {
    return accountName;
  }

  /**
   * Gets the calendar ID used by Google Calendar API.
   * Usually in the format "primary" or "example@gmail.com"
   * @return the calendar ID
   */
  public String getCalendarId() {
    return calendarId;
  }

  /**
   * Gets an initialized HttpRequestInitializer for Google Calendar API.
   * @return HttpRequestInitializer for Google Calendar API
   * @throws IOException if credentials cannot be parsed
   */
  public HttpRequestInitializer getHttpRequestInitializer() throws IOException {
    if (googleCredential == null) {
      initializeCredential();
    }
    return googleCredential;
  }

  /**
   * Initializes the Google credential from the stored credentials JSON.
   * @throws IOException if credentials cannot be parsed
   */
  private void initializeCredential() throws IOException {
    // Create GoogleCredential from JSON string
    googleCredential = GoogleCredential.fromStream(
      new ByteArrayInputStream(credentials.getBytes(StandardCharsets.UTF_8))
    );

    // If using service account, specify the scope
    if (googleCredential.createScopedRequired()) {
      googleCredential = googleCredential.createScoped(
        Collections.singletonList(CALENDAR_SCOPE)
      );
    }
  }
}
