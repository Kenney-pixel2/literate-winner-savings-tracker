package cloud.cloudberry.savingstracker.storage;

import cloud.cloudberry.savingstracker.model.SavingsGoal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GoalStorageTest {

    @TempDir
    Path tempDir;

    @Test
    void savingAndLoadingAGoalPreservesItsFields() throws IOException {
        Path file = tempDir.resolve("goals.dat");
        GoalStorage storage = new GoalStorage(file);

        SavingsGoal original = new SavingsGoal("MacBook",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 1), 1200.0);
        original.setSavedAmount(300.0);

        storage.save(original);
        SavingsGoal loaded = storage.load();

        assertEquals("MacBook", loaded.getName());
        assertEquals(LocalDate.of(2026, 1, 1), loaded.getStartDate());
        assertEquals(LocalDate.of(2026, 6, 1), loaded.getEndDate());
        assertEquals(1200.0, loaded.getTargetAmount());
        assertEquals(300.0, loaded.getSavedAmount());
    }
}
