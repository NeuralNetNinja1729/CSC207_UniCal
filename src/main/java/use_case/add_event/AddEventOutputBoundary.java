package use_case.add_event;

/**
 * The output boundary for the Login Use Case.
 */
public interface AddEventOutputBoundary {
    /**
     * Prepares the success view for the Login Use Case.
     * @param addEventOutputData the output data
     */
    void prepareSuccessView(AddEventOutputData addEventOutputData);

    /**
     * Prepares the failure view for the Login Use Case.
     * @param error the explanation of the failure
     */
    void prepareFailView(String error);
}