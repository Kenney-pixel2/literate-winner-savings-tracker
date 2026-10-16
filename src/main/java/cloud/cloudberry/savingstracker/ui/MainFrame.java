package cloud.cloudberry.savingstracker.ui;

import cloud.cloudberry.savingstracker.model.SavingsGoal;
import cloud.cloudberry.savingstracker.storage.GoalStorage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class MainFrame extends JFrame {

    private final GoalStorage storage;
    private final DefaultListModel<SavingsGoal> listModel = new DefaultListModel<>();
    private final JList<SavingsGoal> goalList = new JList<>(listModel);
    private final JButton editButton = new JButton("Edit goal");
    private final JButton savingsButton = new JButton("Add savings");

    public MainFrame() {
        super("Savings Tracker");

        Path file = Paths.get(System.getProperty("user.home"), ".savings-tracker", "goals.dat");
        storage = new GoalStorage(file);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JButton addButton = new JButton("Add goal");
        addButton.addActionListener(e -> onAdd());
        editButton.addActionListener(e -> onEdit());
        editButton.setEnabled(false);
        
        savingsButton.addActionListener(e -> onAddSavings());
        savingsButton.setEnabled(false);
        goalList.addListSelectionListener(e -> {
            boolean selected = goalList.getSelectedIndex() >= 0;
            editButton.setEnabled(selected);
            savingsButton.setEnabled(selected);
        });


        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(savingsButton);

        add(new JScrollPane(goalList), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        loadGoals();
    }

    private void onAdd() {
        SavingsGoal goal = GoalDialog.show(this, null);
        if (goal != null) {
            listModel.addElement(goal);
            goalList.setSelectedIndex(listModel.size() - 1);
            saveGoals();
        }
    }

    private void onAddSavings() {
        int index = goalList.getSelectedIndex();
        if (index < 0) {
            return;
        }
        AddSavingsDialog.show(this, listModel.get(index));
    }

    private void onEdit() {
        int index = goalList.getSelectedIndex();
        if (index < 0) {
            return;
        }
        SavingsGoal existing = listModel.get(index);
        SavingsGoal edited = GoalDialog.show(this, existing);
        if (edited != null) {
            edited.setSavedAmount(existing.getSavedAmount());
            listModel.set(index, edited);
            saveGoals();
        }
    }

    private void loadGoals() {
        try {
            for (SavingsGoal goal : storage.loadAll()) {
                listModel.addElement(goal);
            }
        } catch (IOException e) {
            showError("Could not load saved goals: " + e.getMessage());
        }
    }

    private void saveGoals() {
        try {
            Files_createParent();
            storage.saveAll(Collections.list(listModel.elements()));
        } catch (IOException e) {
            showError("Could not save your goals: " + e.getMessage());
        }
    }

    private void Files_createParent() throws IOException {
        java.nio.file.Files.createDirectories(
                Paths.get(System.getProperty("user.home"), ".savings-tracker"));
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
