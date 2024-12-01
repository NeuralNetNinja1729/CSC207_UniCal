package use_case.change_calendar_month;

/**
 * The output boundary for the Login Use Case.
 */
public interface ChangeCalendarMonthOutputBoundary {
    /**
     * Prepares the success view for the Login Use Case.
     * @param changeCalendarMonthOutputData the output data
     */
    void prepareSuccessView(ChangeCalendarMonthOutputData changeCalendarMonthOutputData);
    void prepareFailView(String error);
}
