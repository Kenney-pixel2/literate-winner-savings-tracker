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
                escape(goal.getName()),
                goal.getStartDate().toString(),
                goal.getEndDate().toString(),
                String.valueOf(goal.getTargetAmount()),
                String.valueOf(goal.getSavedAmount()));
    }

    private SavingsGoal fromLine(String line) {
        List<String> parts = splitFields(line);

        String name = parts.get(0);
        LocalDate startDate = LocalDate.parse(parts.get(1));
        LocalDate endDate = LocalDate.parse(parts.get(2));
        double targetAmount = Double.parseDouble(parts.get(3));
        double savedAmount = Double.parseDouble(parts.get(4));

        SavingsGoal goal = new SavingsGoal(name, startDate, endDate, targetAmount);
        goal.setSavedAmount(savedAmount);
        return goal;
    }

    private static String escape(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            switch (c) {
                case '\\':
                    result.append("\\\\");
                    break;
                case '|':
                    result.append("\\|");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                default:
                    result.append(c);
            }
        }
        return result.toString();
    }

    private static List<String> splitFields(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\\' && i + 1 < line.length()) {
                char next = line.charAt(++i);
                current.append(next == 'n' ? '\n' : next);
            } else if (c == '|') {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields;
    }
}
