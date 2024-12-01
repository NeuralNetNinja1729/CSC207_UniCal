package use_case.add_event;

/**
 * Input Boundary for actions which are related to Changing the Month Calendar.
 */
public interface AddEventInputBoundary {
    /**
     * Executes the login use case.
     * @param addEventInputData the input data
     */
    void execute(AddEventInputData addEventInputData);
}