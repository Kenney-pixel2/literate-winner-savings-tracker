package cloud.cloudberry.savingstracker.ui;

import cloud.cloudberry.savingstracker.model.SavingsGoal;

import javax.swing.*;
import java.awt.*;

public class AddSavingsDialog extends JDialog {

    private final JTextField amountField = new JTextField(12);

    private AddSavingsDialog(Frame owner, SavingsGoal goal) {
        super(owner, "Add savings to " + goal.getName(), true);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 16));
        form.add(new JLabel("Amount saved"));
        form.add(amountField);
        add(form, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        saveButton.addActionListener(e -> dispose());
        cancelButton.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancelButton);
        buttons.add(saveButton);
        add(buttons, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(saveButton);

        pack();
        
        setLocationRelativeTo(owner);
    }

    public static void show(Frame owner, SavingsGoal goal) {
        new AddSavingsDialog(owner, goal).setVisible(true);
    }
}
