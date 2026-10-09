package cloud.cloudberry.savingstracker.ui;

import cloud.cloudberry.savingstracker.model.SavingsGoal;

import java.time.LocalDate;

public class GoalForm {

    private GoalForm() {
    }

    public static SavingsGoal parse(String name, LocalDate start, LocalDate end, String targetText) {
        String trimmedName = name.trim();
        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Enter a name for your goal.");
        }

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("The end date can't be before the start date.");
        }

        double target;
        try {
            target = Double.parseDouble(targetText.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter the target amount as a number, like 1200.");
        }

        if (!(target > 0) || Double.isInfinite(target)) {
            throw new IllegalArgumentException("The target amount must be more than zero.");
        }

        return new SavingsGoal(trimmedName, start, end, target);
    }
}
