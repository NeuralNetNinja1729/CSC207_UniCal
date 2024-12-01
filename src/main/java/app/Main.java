package app;

import javax.swing.JFrame;

/**
 * The Main class of our application.
 */
public class Main {
    /**
     * Builds and runs the CA architecture of the application.
     * @param args unused arguments
     */
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();

        // Add views
        appBuilder.addChangeCalendarMonthView()
                .addChangeCalendarDayView();

        // Add use cases
        appBuilder.addChangeCalendarDayUseCase()  // Ensure day use case is added before month use case
                .addChangeCalendarMonthUseCase()
                .addAddEventUseCase()
                .addDeleteEventUseCase();

        // Build the application
        final JFrame application = appBuilder.build();

        application.pack();
        application.setVisible(true);
    }
}
