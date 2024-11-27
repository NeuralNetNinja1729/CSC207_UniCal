package view;

import interface_adapter.add_event.AddEventController;
import interface_adapter.add_event.AddEventState;
import interface_adapter.add_event.AddEventViewModel;
import entity.Calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AddEventView extends JPanel implements ActionListener, PropertyChangeListener {
  private final AddEventViewModel addEventViewModel;
  private final AddEventController addEventController;
  private LocalDate selectedDate;

  private final JTextField eventNameField = new JTextField(15);
  private final JTextField dateField = new JTextField(15);
  private final JComboBox<Calendar> calendarComboBox;
  private final JButton addButton;
  private final JButton cancelButton;

  private final JLabel eventNameErrorField = new JLabel();
  private final JLabel dateErrorField = new JLabel();
  private final JLabel calendarErrorField = new JLabel();

  public AddEventView(AddEventViewModel addEventViewModel,
                      AddEventController controller,
                      List<Calendar> availableCalendars) {
    this.addEventViewModel = addEventViewModel;
    this.addEventController = controller;
    this.addEventViewModel.addPropertyChangeListener(this);

    // Layout setup
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // Add title
    JLabel title = new JLabel(AddEventViewModel.TITLE_LABEL);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(title);

    // Event name panel
    JPanel eventNamePanel = new JPanel();
    eventNamePanel.add(new JLabel(AddEventViewModel.EVENT_NAME_LABEL));
    eventNamePanel.add(eventNameField);
    add(eventNamePanel);
    add(eventNameErrorField);

    // Date panel
    JPanel datePanel = new JPanel();
    datePanel.add(new JLabel(AddEventViewModel.DATE_LABEL));
    dateField.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
    datePanel.add(dateField);
    add(datePanel);
    add(dateErrorField);

    // Calendar selection panel
    JPanel calendarPanel = new JPanel();
    calendarPanel.add(new JLabel(AddEventViewModel.CALENDAR_LABEL));
    calendarComboBox = new JComboBox<>(availableCalendars.toArray(new Calendar[0]));
    calendarPanel.add(calendarComboBox);
    add(calendarPanel);
    add(calendarErrorField);

    // Buttons panel
    JPanel buttons = new JPanel();
    addButton = new JButton(AddEventViewModel.ADD_BUTTON_LABEL);
    cancelButton = new JButton(AddEventViewModel.CANCEL_BUTTON_LABEL);
    buttons.add(addButton);
    buttons.add(cancelButton);
    add(buttons);

    // Action listeners
    addButton.addActionListener(this);
    cancelButton.addActionListener(e -> {
      // Reset fields and close/hide the view
      resetFields();
      setVisible(false);
    });
  }

  private void resetFields() {
    eventNameField.setText("");
    if (selectedDate != null) {
      dateField.setText(selectedDate.toString());
    } else {
      dateField.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
    calendarComboBox.setSelectedIndex(0);
    eventNameErrorField.setText("");
    dateErrorField.setText("");
    calendarErrorField.setText("");
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == addButton) {
      // Validate and add event
      Calendar selectedCalendar = (Calendar) calendarComboBox.getSelectedItem();
      if (selectedCalendar != null) {
        try {
          // Validate date format
          LocalDate.parse(dateField.getText());

          addEventController.execute(
            eventNameField.getText(),
            dateField.getText(),
            selectedCalendar
          );
        } catch (DateTimeParseException ex) {
          dateErrorField.setText("Invalid date format. Use YYYY-MM-DD");
        }
      } else {
        calendarErrorField.setText("Please select a calendar");
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("state")) {
      AddEventState state = (AddEventState) evt.getNewValue();
      if (state.getEventNameError().isEmpty() &&
        state.getDateError().isEmpty() &&
        state.getCalendarError().isEmpty()) {
        // Success - close/hide the view
        resetFields();
        setVisible(false);
      } else {
        // Show errors
        eventNameErrorField.setText(state.getEventNameError());
        dateErrorField.setText(state.getDateError());
        calendarErrorField.setText(state.getCalendarError());
      }
    }
  }
  public void setSelectedDate(LocalDate date) {
    this.selectedDate = date;
    dateField.setText(date.toString());
  }


}
