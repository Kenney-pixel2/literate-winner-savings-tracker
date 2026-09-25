package cloud.cloudberry.savingstracker.storage;

import cloud.cloudberry.savingstracker.model.SavingsGoal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class GoalStorage {

    private final Path filePath;

    public GoalStorage(Path filePath) {
        this.filePath = filePath;
    }

    public void save(SavingsGoal goal) throws IOException {
        String line = String.join("|",
                goal.getName(),
                goal.getStartDate().toString(),
                goal.getEndDate().toString(),
                String.valueOf(goal.getTargetAmount()),
                String.valueOf(goal.getSavedAmount()));
        Files.writeString(filePath, line);
    }

    public SavingsGoal load() throws IOException {
        String line = Files.readString(filePath);
        String[] parts = line.split("\\|");

        String name = parts[0];
        LocalDate startDate = LocalDate.parse(parts[1]);
        LocalDate endDate = LocalDate.parse(parts[2]);
        double targetAmount = Double.parseDouble(parts[3]);
        double savedAmount = Double.parseDouble(parts[4]);

        SavingsGoal goal = new SavingsGoal(name, startDate, endDate, targetAmount);
        goal.setSavedAmount(savedAmount);
        return goal;
    }
}
