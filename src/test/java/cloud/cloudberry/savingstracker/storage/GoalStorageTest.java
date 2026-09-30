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
        GoalStorage storage = newStorage("goals.dat");

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
        GoalStorage storage = newStorage("does-not-exist.dat");

        List<SavingsGoal> loaded = storage.loadAll();

        assertTrue(loaded.isEmpty());
    }

    @Test
    void loadingFromEmptyFileReturnsEmptyList() throws IOException {
        Files.writeString(tempDir.resolve("empty.dat"), "");
        GoalStorage storage = newStorage("empty.dat");

        List<SavingsGoal> loaded = storage.loadAll();

        assertTrue(loaded.isEmpty());
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

    private GoalStorage newStorage(String filename) {
        return new GoalStorage(tempDir.resolve(filename));
    }

    private SavingsGoal roundTrip(String name) throws IOException {
        GoalStorage storage = newStorage("goals.dat");

        SavingsGoal goal = new SavingsGoal(name,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 1), 1200.0);
        storage.saveAll(List.of(goal));

        return storage.loadAll().get(0);
    }
}
