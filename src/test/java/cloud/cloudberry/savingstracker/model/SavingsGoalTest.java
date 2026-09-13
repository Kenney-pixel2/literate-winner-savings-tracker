package cloud.cloudberry.savingstracker.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SavingsGoalTest {

    @Test
    void constructorStoresNameDatesAndTarget() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 6, 1);

        SavingsGoal goal = new SavingsGoal("MacBook", start, end, 1200.0);

        assertEquals("MacBook", goal.getName());
        assertEquals(start, goal.getStartDate());
        assertEquals(end, goal.getEndDate());
        assertEquals(1200.0, goal.getTargetAmount());
    }
}
