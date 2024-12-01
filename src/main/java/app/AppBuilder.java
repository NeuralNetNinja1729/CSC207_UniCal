package app;

import java.awt.CardLayout;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import data_access.CalendarDataAccessObjectFactory;
import entity.GoogleCalendar;
import entity.NotionCalendar;
import entity.OutlookCalendar;
import entity.Calendar;
import interface_adapter.ViewManagerModel;
import interface_adapter.add_event.AddEventController;
import interface_adapter.change_calendar_day.ChangeCalendarDayController;
import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;
import interface_adapter.change_calendar_month.ChangeCalendarMonthController;
import interface_adapter.change_calendar_month.ChangeCalendarMonthState;
import interface_adapter.change_calendar_month.ChangeCalendarMonthViewModel;
import interface_adapter.delete_event.DeleteEventController;
import use_case.add_event.AddEventInteractor;
import use_case.change_calendar_day.ChangeCalendarDayInteractor;
import use_case.change_calendar_month.ChangeCalendarMonthInteractor;
import use_case.delete_event.DeleteEventInteractor;
import view.ChangeCalendarDayView;
import view.ChangeCalendarMonthView;
import view.ViewManager;
import interface_adapter.add_event.AddEventPresenter;
import interface_adapter.change_calendar_day.ChangeCalendarDayPresenter;
import interface_adapter.change_calendar_month.ChangeCalendarMonthPresenter;
import interface_adapter.delete_event.DeleteEventPresenter;

/**
 * The AppBuilder class is responsible for putting together the pieces of
 * our CA architecture; piece by piece.
 * <p/>
 * This is done by adding each View and then adding related Use Cases.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    private CalendarDataAccessObjectFactory dataAccessObjectFactory = new CalendarDataAccessObjectFactory();

    // Calendars
    private Calendar googleCalendar;
    private Calendar notionCalendar;
    private Calendar outlookCalendar;

    // View Models
    private ChangeCalendarMonthViewModel monthViewModel;
    private ChangeCalendarDayViewModel dayViewModel;

    // Views
    private ChangeCalendarMonthView monthView;
    private ChangeCalendarDayView dayView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        initializeCalendars();
    }

    /**
     * Initializes the Google, Notion, and Outlook calendar instances.
     */
    private void initializeCalendars() {
        try {
            // Google Calendar Initialization
            String googleCredentials = new String(Files.readAllBytes(
                    Paths.get("src/main/resources/unical-442813-e8345d894d68.json")));

            googleCalendar = new GoogleCalendar(
                    googleCredentials,
                    "calendar-service@unical-442813.iam.gserviceaccount.com",
                    "UniCal Google Calendar",
                    "unical.207.test@gmail.com"
            );

            // Notion Calendar Initialization
            notionCalendar = new NotionCalendar(
                    "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",
                    "148fc62c766b80cda8edeb8afb359493",
                    "UniCal Notion Calendar"
            );

            // Outlook Calendar Initialization
            outlookCalendar = new OutlookCalendar(
                    "{\n" +
                            "    \"client_id\": \"4f5baecc-ea4a-42b0-96cd-1af3a9e1351b\",\n" +
                            "    \"client_secret\": \"N.98Q~CT_pHc4dBcZQyM4GG6Q5g2qHCyh13ECcKE\",\n" +
                            "    \"tenant_id\": \"consumers\",\n" +
                            "    \"redirect_uri\": \"http://localhost\"\n" +
                            "}",
                    "unical.207.test@outlook.com",
                    "UniCal Outlook Calendar",
                    "primary" // or specific calendar ID
            );

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error initializing calendars: " + e.getMessage(),
                    "Initialization Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Adds the Change Calendar Month View to the application.
     * @return this builder
     */
    public AppBuilder addChangeCalendarMonthView() {
        monthViewModel = new ChangeCalendarMonthViewModel();
        monthView = new ChangeCalendarMonthView(monthViewModel);
        cardPanel.add(monthView, monthView.getViewName());

        // Set initial calendar data to the month view model
        ChangeCalendarMonthState initialState = monthViewModel.getState();
        initialState.setGoogleCalendar(googleCalendar);
        initialState.setNotionCalendar(notionCalendar);
        initialState.setOutlookCalendar(outlookCalendar);
        initialState.setCurrCalendarList(List.of(googleCalendar, notionCalendar, outlookCalendar));
        initialState.setActiveCalendar(googleCalendar); // Default active calendar
        monthViewModel.setState(initialState);

        return this;
    }

    /**
     * Adds the Change Calendar Day View to the application.
     * @return this builder
     */
    public AppBuilder addChangeCalendarDayView() {
        dayViewModel = new ChangeCalendarDayViewModel();
        dayView = new ChangeCalendarDayView(dayViewModel);
        cardPanel.add(dayView, dayView.getViewName());
        return this;
    }

    /**
     * Adds the Change Calendar Month Use Case to the application.
     * @return this builder
     */
    public AppBuilder addChangeCalendarMonthUseCase() {
        ChangeCalendarMonthPresenter monthPresenter = new ChangeCalendarMonthPresenter(monthViewModel, viewManagerModel);
        ChangeCalendarMonthInteractor monthInteractor = new ChangeCalendarMonthInteractor(dataAccessObjectFactory, monthPresenter);
        ChangeCalendarMonthController monthController = new ChangeCalendarMonthController(monthInteractor);
        monthView.setChangeCalendarMonthController(monthController);
        return this;
    }

    /**
     * Adds the Change Calendar Day Use Case to the application.
     * @return this builder
     */
    public AppBuilder addChangeCalendarDayUseCase() {
        ChangeCalendarDayPresenter dayPresenter = new ChangeCalendarDayPresenter(dayViewModel, viewManagerModel);
        ChangeCalendarDayInteractor dayInteractor = new ChangeCalendarDayInteractor(dataAccessObjectFactory,
                dayPresenter);
        ChangeCalendarDayController dayController = new ChangeCalendarDayController(dayInteractor);
        dayView.setChangeCalendarDayController(dayController);
        monthView.setDayController(dayController);  // Ensure Month View also has a reference to day controller
        return this;
    }

    /**
     * Adds the Add Event Use Case to the application.
     * @return this builder
     */
    public AppBuilder addAddEventUseCase() {
        AddEventPresenter addEventPresenter = new AddEventPresenter(dayViewModel, viewManagerModel);
        AddEventInteractor addEventInteractor = new AddEventInteractor(dataAccessObjectFactory, addEventPresenter);
        AddEventController addEventController = new AddEventController(addEventInteractor);
        dayView.setAddEventController(addEventController);
        return this;
    }

    /**
     * Adds the Delete Event Use Case to the application.
     * @return this builder
     */
    public AppBuilder addDeleteEventUseCase() {
        DeleteEventPresenter deleteEventPresenter = new DeleteEventPresenter(dayViewModel, viewManagerModel);
        DeleteEventInteractor deleteEventInteractor = new DeleteEventInteractor(dataAccessObjectFactory,
                deleteEventPresenter);
        DeleteEventController deleteEventController = new DeleteEventController(deleteEventInteractor);
        dayView.setDeleteEventController(deleteEventController);
        return this;
    }

    /**
     * Creates the JFrame for the application and initially sets the Change Calendar Month View to be displayed.
     * @return the application
     */
    public JFrame build() {
        final JFrame application = new JFrame("UniCal - Calendar View");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setSize(1000, 700);

        // Add card panel and set the initial view to the Change Calendar Month View
        application.add(cardPanel);
        viewManagerModel.setState(monthView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
