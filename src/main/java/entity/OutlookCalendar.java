package entity;

import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.models.Calendar;
import java.io.IOException;

import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ClientSecretCredentialBuilder;

public class OutlookCalendar implements entity.Calendar {
  private final String credentials;
  private final String accountName;
  private final String calendarName;
  private final String calendarId;
  private IAuthenticationProvider authProvider;

  public OutlookCalendar(String credentials, String accountName, String calendarName, String calendarId) {
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

    try {
      initializeAuthProvider();
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid credentials format: " + e.getMessage());
    }
  }

  @Override
  public String getCalendarApiName() {
    return "OutlookCalendar";
  }

  @Override
  public String getCalendarName() {
    return calendarName;
  }

  public String getCredentials() {
    return credentials;
  }

  public String getAccountName() {
    return accountName;
  }

  public String getCalendarId() {
    return calendarId;
  }

  public IAuthenticationProvider getAuthProvider() throws IOException {
    if (authProvider == null) {
      initializeAuthProvider();
    }
    return authProvider;
  }

  private void initializeAuthProvider() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    CredentialsConfig config = mapper.readValue(credentials, CredentialsConfig.class);

    TokenCredential credential = new ClientSecretCredentialBuilder()
      .clientId(config.client_id)
      .clientSecret(config.client_secret)
      .tenantId(config.tenant_id)
      .build();

    authProvider = new TokenCredentialAuthProvider(
      Collections.singletonList("https://graph.microsoft.com/.default"),
      credential
    );
  }

  private static class CredentialsConfig {
    public String client_id;
    public String client_secret;
    public String tenant_id;
    public String redirect_uri;
  }
}
