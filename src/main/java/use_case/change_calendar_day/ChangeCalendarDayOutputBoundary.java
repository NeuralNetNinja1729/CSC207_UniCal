package use_case.change_calendar_day;

public interface ChangeCalendarDayOutputBoundary {
    /**
     * Prepares the success view for the Change Calendar Day use case
     * @param outputData the output data
     */
    void prepareSuccessView(ChangeCalendarDayOutputData outputData);

    /**
     * Prepares the failure view for the Change Calendar Day use case
     * @param error the error message
     */
    void prepareFailView(String error);
}