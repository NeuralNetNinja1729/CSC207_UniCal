package use_case.add_event;

import entity.Event;

/**
 * Output data for adding an event.
 */
public class AddEventOutputData {
    private final Event event;
    private final boolean useCaseFailed;

    public AddEventOutputData(Event event, boolean useCaseFailed) {
        this.event = event;
        this.useCaseFailed = useCaseFailed;
    }

    public Event getEvent() {
        return event;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
