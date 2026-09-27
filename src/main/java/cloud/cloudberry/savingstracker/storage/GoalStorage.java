package cloud.cloudberry.savingstracker.storage;

import cloud.cloudberry.savingstracker.model.SavingsGoal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GoalStorage {

    private final Path filePath;

    public GoalStorage(Path filePath) {
        this.filePath = filePath;
    }

    public void saveAll(List<SavingsGoal> goals) throws IOException {
        List<String> lines = new ArrayList<>();
        for (SavingsGoal goal : goals) {
            lines.add(toLine(goal));
        }
        Files.write(filePath, lines);
    }

    public List<SavingsGoal> loadAll() throws IOException {
        List<SavingsGoal> goals = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return goals;
        }

        List<String> lines = Files.readAllLines(filePath);
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            goals.add(fromLine(line));
        }
        return goals;
    }

    private String toLine(SavingsGoal goal) {
        return String.join("|",
                goal.getName(),
                goal.getStartDate().toString(),
                goal.getEndDate().toString(),
                String.valueOf(goal.getTargetAmount()),
                String.valueOf(goal.getSavedAmount()));
    }

    private SavingsGoal fromLine(String line) {
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
