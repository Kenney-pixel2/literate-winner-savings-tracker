package cloud.cloudberry.savingstracker.ui;

import cloud.cloudberry.savingstracker.model.SavingsGoal;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class GoalDialog extends JDialog {

    private final JTextField nameField = new JTextField(20);
    private final JSpinner startSpinner = newDateSpinner();
    private final JSpinner endSpinner = newDateSpinner();
    private final JTextField targetField = new JTextField(20);

    private SavingsGoal result;

    private GoalDialog(Frame owner, SavingsGoal existing) {
        super(owner, existing == null ? "Add goal" : "Edit goal", true);

        if (existing != null) {
            nameField.setText(existing.getName());
            startSpinner.setValue(toDate(existing.getStartDate()));
            endSpinner.setValue(toDate(existing.getEndDate()));
            targetField.setText(String.valueOf(existing.getTargetAmount()));
        } else {
            endSpinner.setValue(toDate(LocalDate.now().plusMonths(1)));
        }

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        addRow(form, 0, "What are you saving for?", nameField);
        addRow(form, 1, "Start date", startSpinner);
        addRow(form, 2, "End date", endSpinner);
        addRow(form, 3, "Target amount", targetField);

        JButton saveButton = new JButton("Save goal");
        JButton cancelButton = new JButton("Cancel");
        saveButton.addActionListener(e -> onSave());
        cancelButton.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancelButton);
        buttons.add(saveButton);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(saveButton);

        pack();
        setLocationRelativeTo(owner);
    }

    /** Shows the dialog. Returns the new goal, or null if the user cancelled. */
    public static SavingsGoal show(Frame owner, SavingsGoal existing) {
        GoalDialog dialog = new GoalDialog(owner, existing);
        dialog.setVisible(true);
        return dialog.result;
    }

    private void onSave() {
        try {
            result = GoalForm.parse(
                    nameField.getText(),
                    toLocalDate((Date) startSpinner.getValue()),
                    toLocalDate((Date) endSpinner.getValue()),
                    targetField.getText());
            dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Check your goal", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void addRow(JPanel form, int row, String label, JComponent field) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = row;
        c.insets = new Insets(4, 4, 4, 4);

        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        form.add(new JLabel(label), c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        form.add(field, c);
    }

    private static JSpinner newDateSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd"));
        return spinner;
    }

    private static Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
