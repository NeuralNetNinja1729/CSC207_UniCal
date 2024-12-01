package view;

import interface_adapter.delete_event.DeleteEventController;
import interface_adapter.delete_event.DeleteEventState;
import interface_adapter.delete_event.DeleteEventViewModel;
import entity.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DeleteEventView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "delete event";
    private final DeleteEventViewModel deleteEventViewModel;
    private final DeleteEventController deleteEventController;

    // UI Components
    private final JLabel titleLabel;
    private final JLabel eventInfoLabel;
    private final JLabel errorLabel;
    private final JButton deleteButton;
    private final JButton cancelButton;

    public DeleteEventView(DeleteEventViewModel viewModel, DeleteEventController controller) {
        this.deleteEventViewModel = viewModel;
        this.deleteEventController = controller;
        this.deleteEventViewModel.addPropertyChangeListener(this);

        // Initialize components
        titleLabel = new JLabel(DeleteEventViewModel.TITLE_LABEL);
        eventInfoLabel = new JLabel();
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        deleteButton = new JButton(DeleteEventViewModel.DELETE_BUTTON_LABEL);
        cancelButton = new JButton(DeleteEventViewModel.CANCEL_BUTTON_LABEL);

        // Layout setup
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setupUI();
        addListeners();
    }

    private void setupUI() {
        // Title panel
        JPanel titlePanel = new JPanel();
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);
        add(titlePanel);

        // Event info panel
        JPanel eventPanel = new JPanel();
        eventInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        eventPanel.add(eventInfoLabel);
        add(eventPanel);

        // Error panel
        JPanel errorPanel = new JPanel();
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorPanel.add(errorLabel);
        add(errorPanel);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel);
    }

    private void addListeners() {
        deleteButton.addActionListener(this);
        cancelButton.addActionListener(e -> setVisible(false));
    }

    public void setSelectedEvent(Event event) {
        DeleteEventState currentState = deleteEventViewModel.getState();
        currentState.setSelectedEvent(event);
        deleteEventViewModel.setState(currentState);
        updateEventInfo(event);
    }

    private void updateEventInfo(Event event) {
        if (event != null) {
            eventInfoLabel.setText(String.format("Event: %s (Date: %s)",
                    event.getEventName(),
                    event.getDate().toString()));
        } else {
            eventInfoLabel.setText("No event selected");
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == deleteButton) {
            DeleteEventState state = deleteEventViewModel.getState();
            Event selectedEvent = state.getSelectedEvent();

            if (selectedEvent != null) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        DeleteEventViewModel.CONFIRM_DELETE_MESSAGE,
                        DeleteEventViewModel.TITLE_LABEL,
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    deleteEventController.execute(selectedEvent);
                }
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            DeleteEventState state = (DeleteEventState) evt.getNewValue();
            if (state.isUseCaseFailed()) {
                errorLabel.setText(state.getError());
            } else {
                errorLabel.setText("");
                if (state.getSelectedEvent() == null) {
                    setVisible(false);
                } else {
                    updateEventInfo(state.getSelectedEvent());
                }
            }
        }
    }

    public String getViewName() {
        return viewName;
    }
}