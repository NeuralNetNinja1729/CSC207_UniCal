package use_case.add_event;

import entity.Event;

/**
 * Output data for adding an event.
 */
public class AddEventOutputData {

    private final Event event;

    public AddEventOutputData(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
