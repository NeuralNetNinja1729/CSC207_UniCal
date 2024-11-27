package view;

import entity.Event;
import interface_adapter.change_calendar_day.ChangeCalendarDayController;
import interface_adapter.change_calendar_day.ChangeCalendarDayState;
import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;
import interface_adapter.delete_event.DeleteEventController;
import interface_adapter.add_event.AddEventController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.List;

public class ChangeCalendarDayView extends JPanel implements PropertyChangeListener {
  private final ChangeCalendarDayViewModel dayViewModel;
  private final DefaultListModel<Event> eventListModel;
  private final JList<Event> eventList;
  private final JLabel dateLabel;
  private AddEventView addEventView;
  private DeleteEventController deleteEventController;
  private ChangeCalendarDayController changeCalendarDayController;

  public ChangeCalendarDayView(ChangeCalendarDayViewModel viewModel) {
    this.dayViewModel = viewModel;
    this.dayViewModel.addPropertyChangeListener(this);

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Header with date
    dateLabel = new JLabel("", SwingConstants.CENTER);
    dateLabel.setFont(new Font("Arial", Font.BOLD, 16));
    add(dateLabel, BorderLayout.NORTH);

    // Events list
    eventListModel = new DefaultListModel<>();
    eventList = new JList<>(eventListModel);
    eventList.setCellRenderer(new EventListCellRenderer());
    JScrollPane scrollPane = new JScrollPane(eventList);
    add(scrollPane, BorderLayout.CENTER);

    // Buttons panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    JButton addButton = new JButton("Add Event");
    JButton deleteButton = new JButton("Delete Event");
    JButton backButton = new JButton("Back to Month View");

    addButton.addActionListener(e -> showAddEventDialog());
    deleteButton.addActionListener(e -> handleDeleteEvent());
    backButton.addActionListener(e -> handleBackToMonth());

    buttonPanel.add(addButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(backButton);
    add(buttonPanel, BorderLayout.SOUTH);

    updateDateLabel(LocalDate.now());
  }

  public void setAddEventView(AddEventView view) {
    this.addEventView = view;
  }

  public void setDeleteEventController(DeleteEventController controller) {
    this.deleteEventController = controller;
  }

  public void setChangeCalendarDayController(ChangeCalendarDayController controller) {
    this.changeCalendarDayController = controller;
  }

  private void showAddEventDialog() {
    if (addEventView != null) {
      // Set the selected date in the AddEventView
      addEventView.setSelectedDate(dayViewModel.getState().getDate());

      JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Event", true);
      Container oldParent = addEventView.getParent();
      if (oldParent != null) {
        oldParent.remove(addEventView);
      }

      dialog.setContentPane(addEventView);
      dialog.pack();
      dialog.setLocationRelativeTo(this);

      addEventView.setVisible(true);
      dialog.setVisible(true);
    }
  }

  private void handleDeleteEvent() {
    Event selectedEvent = eventList.getSelectedValue();
    if (selectedEvent != null) {
      int confirm = JOptionPane.showConfirmDialog(
        this,
        "Delete event: " + selectedEvent.getEventName() + "?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION
      );
      if (confirm == JOptionPane.YES_OPTION && deleteEventController != null) {
        deleteEventController.execute(selectedEvent);
      }
    } else {
      JOptionPane.showMessageDialog(
        this,
        "Please select an event to delete",
        "No Selection",
        JOptionPane.WARNING_MESSAGE
      );
    }
  }

  private void handleBackToMonth() {
    Container parent = getParent();
    if (parent instanceof JPanel) {
      CardLayout layout = (CardLayout) parent.getLayout();
      layout.show(parent, "month");
    }
  }

  private void updateDateLabel(LocalDate date) {
    dateLabel.setText("Events for " + date.toString());
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if ("state".equals(evt.getPropertyName())) {
      ChangeCalendarDayState state = (ChangeCalendarDayState) evt.getNewValue();
      updateDateLabel(state.getDate());
      updateEventsList(state.getEventList());
    }
  }

  private void updateEventsList(List<Event> events) {
    eventListModel.clear();
    for (Event event : events) {
      eventListModel.addElement(event);
    }
  }

  private static class EventListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
      JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      Event event = (Event) value;
      String displayText = String.format("%s (%s)",
        event.getEventName(),
        event.getCalendarApi().getCalendarName());
      return super.getListCellRendererComponent(list, displayText, index, isSelected, cellHasFocus);
    }
  }
}
