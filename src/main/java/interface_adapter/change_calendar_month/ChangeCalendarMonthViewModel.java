package interface_adapter.change_calendar_month;

import interface_adapter.ViewModel;

public class ChangeCalendarMonthViewModel extends ViewModel<ChangeCalendarMonthState> {
  public static final String TITLE_LABEL = "Calendar View";
  public static final String MONTH_LABEL = "Month:";
  public static final String YEAR_LABEL = "Year:";
  public static final String GOOGLE_CALENDAR_BUTTON_LABEL = "Google Calendar";
  public static final String NOTION_CALENDAR_BUTTON_LABEL = "Notion Calendar";
  public static final String OUTLOOK_CALENDAR_BUTTON_LABEL = "Outlook Calendar";

  public ChangeCalendarMonthViewModel() {
    super("calendar_month");
    setState(new ChangeCalendarMonthState());
  }
}
