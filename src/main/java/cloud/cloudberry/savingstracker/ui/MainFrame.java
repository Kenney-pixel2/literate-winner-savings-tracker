package cloud.cloudberry.savingstracker.ui;

import cloud.cloudberry.savingstracker.model.SavingsGoal;
import cloud.cloudberry.savingstracker.storage.GoalStorage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MainFrame extends JFrame {

    private final GoalStorage storage;
    private final DefaultListModel<SavingsGoal> listModel = new DefaultListModel<>();
    private final JList<SavingsGoal> goalList = new JList<>(listModel);

    public MainFrame() {
        super("Savings Tracker");

        Path file = Paths.get(System.getProperty("user.home"), ".savings-tracker", "goals.dat");
        storage = new GoalStorage(file);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        add(new JScrollPane(goalList), BorderLayout.CENTER);

        loadGoals();
    }

    private void loadGoals() {
        try {
            List<SavingsGoal> goals = storage.loadAll();
            for (SavingsGoal goal : goals) {
                listModel.addElement(goal);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not load saved goals: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
