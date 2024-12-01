package use_case.add_event;

import data_access.AddEventDataAccessInterface;
import data_access.CalendarDataAccessObjectFactory;
import entity.Calendar;
import entity.Event;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AddEventInteractor implements AddEventInputBoundary {
    private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
    private final AddEventOutputBoundary addEventPresenter;

    public AddEventInteractor(
            CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
            AddEventOutputBoundary addEventPresenter) {
        this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
        this.addEventPresenter = addEventPresenter;
    }

    @Override
    public void execute(AddEventInputData inputData) {
        try {
            if (inputData.getEventName().trim().isEmpty()) {
                addEventPresenter.prepareFailView("Event name cannot be empty");
                return;
            }

            LocalDate date = LocalDate.parse(inputData.getDate());
            LocalTime startTime = LocalTime.parse(inputData.getStartTime());
            LocalTime endTime = LocalTime.parse(inputData.getEndTime());

            if (endTime.isBefore(startTime)) {
                addEventPresenter.prepareFailView("End time cannot be before start time");
                return;
            }

            Calendar calendar = inputData.getCalendar();
            Event event = new Event(
                    inputData.getEventName(),
                    date,
                    startTime,
                    endTime,
                    calendar
            );

            AddEventDataAccessInterface addEventDataAccessObject =
                    (AddEventDataAccessInterface) calendarDataAccessObjectFactory
                            .getCalendarDataAccessObject(calendar);

            if (!addEventDataAccessObject.addEvent(event)) {
                addEventPresenter.prepareFailView("Failed to add event to calendar");
                return;
            }

            AddEventOutputData outputData = new AddEventOutputData(event, false);
            addEventPresenter.prepareSuccessView(outputData);

        } catch (DateTimeParseException e) {
            if (e.getParsedString().contains(":")) {
                addEventPresenter.prepareFailView("Invalid time format. Use HH:mm (24-hour format)");
            } else {
                addEventPresenter.prepareFailView("Invalid date format. Use YYYY-MM-DD");
            }
        } catch (Exception e) {
            addEventPresenter.prepareFailView("Error adding event: " + e.getMessage());
        }
    }
}