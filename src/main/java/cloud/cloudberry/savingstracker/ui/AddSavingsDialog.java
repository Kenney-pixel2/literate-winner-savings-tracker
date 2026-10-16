package cloud.cloudberry.savingstracker.ui;

import cloud.cloudberry.savingstracker.model.SavingsGoal;

import javax.swing.*;
import java.awt.*;

public class AddSavingsDialog extends JDialog {

    private AddSavingsDialog(Frame owner, SavingsGoal goal) {
        super(owner, "Add savings to " + goal.getName(), true);
        setSize(320, 160);
        setLocationRelativeTo(owner);
    }

    public static void show(Frame owner, SavingsGoal goal) {
        new AddSavingsDialog(owner, goal).setVisible(true);
    }
}
