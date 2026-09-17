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

    @Test
    void remainingAmountIsTargetMinusSaved() {
        SavingsGoal goal = new SavingsGoal("MacBook",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 1), 1200.0);

        goal.setSavedAmount(300.0);

        assertEquals(900.0, goal.getRemainingAmount());
    }

    @Test
    void remainingDaysCountsFromTodayToEndDateInclusive() {
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(10);

        SavingsGoal goal = new SavingsGoal("MacBook", today, end, 1200.0);

        // today counts as a saving day too, so it's 11, not 10
        assertEquals(11, goal.getRemainingDays());
    }

    @Test
    void remainingDaysIsOneWhenEndDateIsToday() {
        LocalDate today = LocalDate.now();

        SavingsGoal goal = new SavingsGoal("MacBook", today, today, 1200.0);

        assertEquals(1, goal.getRemainingDays());
    }
}
