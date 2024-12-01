package view;

import entity.Calendar;
import interface_adapter.add_event.AddEventController;
import interface_adapter.add_event.AddEventState;
import interface_adapter.add_event.AddEventViewModel;

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
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import java.util.function.Consumer;

public class AddEventView extends JPanel implements ActionListener, PropertyChangeListener {
  private class DocumentChangeListener implements DocumentListener {
    private final Consumer<String> updateAction;

    public DocumentChangeListener(Consumer<String> updateAction) {
      this.updateAction = updateAction;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      updateField(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      updateField(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
      updateField(e);
    }

    private void updateField(DocumentEvent e) {
      try {
        String text = e.getDocument().getText(0, e.getDocument().getLength());
        updateAction.accept(text);
      } catch (BadLocationException ex) {
        ex.printStackTrace();
      }
    }
  }

  private final String viewName = "add event";
  private final AddEventViewModel addEventViewModel;
  private final AddEventController addEventController;

  // UI Components
  private final JTextField eventNameField = new JTextField(15);
  private final JTextField dateField = new JTextField(15);
  private final JTextField startTimeField = new JTextField(15);
  private final JTextField endTimeField = new JTextField(15);
  private final JComboBox<Calendar> calendarComboBox;
  private final JButton addButton;
  private final JButton cancelButton;
  private final JLabel eventNameErrorField = new JLabel();
  private final JLabel dateErrorField = new JLabel();
  private final JLabel timeErrorField = new JLabel();
  private final JLabel calendarErrorField = new JLabel();

  public AddEventView(AddEventViewModel viewModel,
                      AddEventController controller,
                      List<Calendar> availableCalendars) {
    this.addEventViewModel = viewModel;
    this.addEventController = controller;
    this.addEventViewModel.addPropertyChangeListener(this);

    // Initialize UI components
    calendarComboBox = new JComboBox<>(availableCalendars.toArray(new Calendar[0]));
    addButton = new JButton(AddEventViewModel.ADD_BUTTON_LABEL);
    cancelButton = new JButton(AddEventViewModel.CANCEL_BUTTON_LABEL);

    // Set up layout
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // Title
    JLabel title = new JLabel(AddEventViewModel.TITLE_LABEL);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(title);

    // Event name panel
    LabelTextPanel eventNamePanel = new LabelTextPanel(
            new JLabel(AddEventViewModel.EVENT_NAME_LABEL), eventNameField);
    add(eventNamePanel);
    add(eventNameErrorField);
    eventNameErrorField.setForeground(Color.RED);

    // Date panel
    LabelTextPanel datePanel = new LabelTextPanel(
            new JLabel(AddEventViewModel.DATE_LABEL), dateField);
    add(datePanel);
    add(dateErrorField);
    dateErrorField.setForeground(Color.RED);

    // Start Time panel
    LabelTextPanel startTimePanel = new LabelTextPanel(
            new JLabel("Start Time (HH:mm)"), startTimeField);
    add(startTimePanel);

    // End Time panel
    LabelTextPanel endTimePanel = new LabelTextPanel(
            new JLabel("End Time (HH:mm)"), endTimeField);
    add(endTimePanel);

    add(timeErrorField);
    timeErrorField.setForeground(Color.RED);

    // Calendar selection panel
    JPanel calendarPanel = new JPanel();
    calendarPanel.add(new JLabel(AddEventViewModel.CALENDAR_LABEL));
    calendarPanel.add(calendarComboBox);
    add(calendarPanel);
    add(calendarErrorField);
    calendarErrorField.setForeground(Color.RED);

    // Buttons panel
    JPanel buttons = new JPanel();
    buttons.add(addButton);
    buttons.add(cancelButton);
    add(buttons);

    // Action listeners
    addButton.addActionListener(this);
    cancelButton.addActionListener(e -> {
      resetFields();
      setVisible(false);
    });

    // Add document listeners
    addDocumentListeners();

    // Initialize the date field with the current date
    LocalDate currentDate = LocalDate.now();
    setSelectedDate(currentDate);
  }

  private void addDocumentListeners() {
    eventNameField.getDocument().addDocumentListener(
            new DocumentChangeListener(s -> {
              AddEventState currentState = addEventViewModel.getState();
              currentState.setEventName(s);
              addEventViewModel.setState(currentState);
            })
    );

    dateField.getDocument().addDocumentListener(
            new DocumentChangeListener(s -> {
              AddEventState currentState = addEventViewModel.getState();
              currentState.setDate(s);
              addEventViewModel.setState(currentState);
            })
    );

    startTimeField.getDocument().addDocumentListener(
            new DocumentChangeListener(s -> {
              AddEventState currentState = addEventViewModel.getState();
              currentState.setStartTime(s);
              addEventViewModel.setState(currentState);
            })
    );

    endTimeField.getDocument().addDocumentListener(
            new DocumentChangeListener(s -> {
              AddEventState currentState = addEventViewModel.getState();
              currentState.setEndTime(s);
              addEventViewModel.setState(currentState);
            })
    );
  }

  private void resetFields() {
    eventNameField.setText("");
    dateField.setText("");
    startTimeField.setText("");
    endTimeField.setText("");
    calendarComboBox.setSelectedIndex(0);
    eventNameErrorField.setText("");
    dateErrorField.setText("");
    timeErrorField.setText("");
    calendarErrorField.setText("");
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == addButton) {
      Calendar selectedCalendar = (Calendar) calendarComboBox.getSelectedItem();
      if (selectedCalendar != null) {
        try {
          String currentDate = dateField.getText();
          String startTime = startTimeField.getText();
          String endTime = endTimeField.getText();

          addEventController.execute(
                  eventNameField.getText(),
                  currentDate,
                  startTime,
                  endTime,
                  selectedCalendar
          );
        } catch (DateTimeParseException ex) {
          timeErrorField.setText("Invalid time format. Use HH:mm (24-hour format)");
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
      if (!state.isUseCaseFailed()) {
        resetFields();
        setVisible(false);
      } else {
        eventNameErrorField.setText(state.getEventNameError());
        dateErrorField.setText(state.getDateError());
        timeErrorField.setText(state.getTimeError());
        calendarErrorField.setText(state.getCalendarError());
      }
    }
  }

  public void setSelectedDate(LocalDate date) {
    dateField.setText(date.toString());

    AddEventState currentState = addEventViewModel.getState();
    currentState = new AddEventState(currentState); // Create a copy
    currentState.setDate(date.toString());
    addEventViewModel.setState(currentState);
  }

  public String getViewName() {
    return viewName;
  }
}