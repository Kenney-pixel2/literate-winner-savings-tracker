package cloud.cloudberry.savingstracker.storage;

import cloud.cloudberry.savingstracker.model.SavingsGoal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GoalStorageTest {

    @TempDir
    Path tempDir;

    @Test
    void savingAndLoadingMultipleGoalsPreservesAllFields() throws IOException {
        Path file = tempDir.resolve("goals.dat");
        GoalStorage storage = new GoalStorage(file);

        SavingsGoal macbook = new SavingsGoal("MacBook",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 1), 1200.0);
        macbook.setSavedAmount(300.0);

        SavingsGoal vacation = new SavingsGoal("Vacation",
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 12, 1), 2000.0);
        vacation.setSavedAmount(500.0);

        List<SavingsGoal> goals = new ArrayList<>();
        goals.add(macbook);
        goals.add(vacation);

        storage.saveAll(goals);
        List<SavingsGoal> loaded = storage.loadAll();

        assertEquals(2, loaded.size());
        assertEquals("MacBook", loaded.get(0).getName());
        assertEquals(300.0, loaded.get(0).getSavedAmount());
        assertEquals("Vacation", loaded.get(1).getName());
        assertEquals(2000.0, loaded.get(1).getTargetAmount());
    }

    @Test
    void loadingFromMissingFileReturnsEmptyList() throws IOException {
        Path file = tempDir.resolve("does-not-exist.dat");
        GoalStorage storage = new GoalStorage(file);

        List<SavingsGoal> loaded = storage.loadAll();

        assertTrue(loaded.isEmpty());
    }

    @Test
    void loadingFromEmptyFileReturnsEmptyList() throws IOException {
        Path file = tempDir.resolve("empty.dat");
        Files.writeString(file, "");
        GoalStorage storage = new GoalStorage(file);

        List<SavingsGoal> loaded = storage.loadAll();

        assertTrue(loaded.isEmpty());
    }

    
    private SavingsGoal roundTrip(String name) throws IOException {
        Path file = tempDir.resolve("goals.dat");
        GoalStorage storage = new GoalStorage(file);

        SavingsGoal goal = new SavingsGoal(name,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 1), 1200.0);
        storage.saveAll(List.of(goal));

        return storage.loadAll().get(0);
    }

    @Test
    void nameContainingPipeSurvivesRoundTrip() throws IOException {
        assertEquals("Cash | Savings", roundTrip("Cash | Savings").getName());
    }

    @Test
    void nameContainingBackslashSurvivesRoundTrip() throws IOException {
        assertEquals("C:\\temp\\new", roundTrip("C:\\temp\\new").getName());
    }

    @Test
    void nameContainingNewlineSurvivesRoundTrip() throws IOException {
        assertEquals("Line one\nLine two", roundTrip("Line one\nLine two").getName());
    }

    @Test
    void nameWithBackslashRightBeforePipeSurvivesRoundTrip() throws IOException {
        assertEquals("a\\|b", roundTrip("a\\|b").getName());
    }
}
