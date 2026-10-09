package cloud.cloudberry.savingstracker.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        SavingsGoal goal = goalWith(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 1), 1200.0, 300.0);

        assertEquals(900.0, goal.getRemainingAmount());
    }

    @Test
    void remainingDaysCountsFromTodayToEndDateInclusive() {
        LocalDate today = LocalDate.now();
        SavingsGoal goal = goalWith(today, today.plusDays(10), 1200.0, 0.0);

        assertEquals(11, goal.getRemainingDays());
    }

    @Test
    void remainingDaysIsOneWhenEndDateIsToday() {
        LocalDate today = LocalDate.now();
        SavingsGoal goal = goalWith(today, today, 1200.0, 0.0);

        assertEquals(1, goal.getRemainingDays());
    }

    @Test
    void dailyAmountSplitsRemainingAcrossRemainingDays() {
        LocalDate today = LocalDate.now();
        SavingsGoal goal = goalWith(today, today.plusDays(9), 1000.0, 0.0);

        assertEquals(100.0, goal.getDailyAmountToSave());
    }

    @Test
    void dailyAmountIsWholeRemainingWhenEndDateIsToday() {
        LocalDate today = LocalDate.now();
        SavingsGoal goal = goalWith(today, today, 500.0, 200.0);

        assertEquals(300.0, goal.getDailyAmountToSave());
    }

    @Test
    void dailyAmountIsZeroWhenTargetAlreadyReached() {
        LocalDate today = LocalDate.now();
        SavingsGoal goal = goalWith(today, today.plusDays(10), 1000.0, 1000.0);

        assertEquals(0.0, goal.getDailyAmountToSave());
    }

    @Test
    void progressPercentIsSavedOverTarget() {
        SavingsGoal goal = goalWith(LocalDate.now(), LocalDate.now().plusDays(10), 1000.0, 250.0);

        assertEquals(25.0, goal.getProgressPercent());
    }

    @Test
    void progressPercentIsCappedAtOneHundred() {
        SavingsGoal goal = goalWith(LocalDate.now(), LocalDate.now().plusDays(10), 1000.0, 1500.0);

        assertEquals(100.0, goal.getProgressPercent());
    }

    @Test
    void isCompleteWhenSavedMeetsOrExceedsTarget() {
        SavingsGoal goal = goalWith(LocalDate.now(), LocalDate.now().plusDays(10), 1000.0, 1000.0);

        assertTrue(goal.isComplete());
    }

    @Test
    void isNotCompleteWhenSavedIsBelowTarget() {
        SavingsGoal goal = goalWith(LocalDate.now(), LocalDate.now().plusDays(10), 1000.0, 999.0);

        assertFalse(goal.isComplete());
    }

    @Test
    void isOverdueWhenEndDateIsPastAndNotComplete() {
        SavingsGoal goal = goalWith(LocalDate.now().minusDays(10), LocalDate.now().minusDays(1), 1000.0, 500.0);

        assertTrue(goal.isOverdue());
    }

    private SavingsGoal goalWith(LocalDate start, LocalDate end, double target, double saved) {
        SavingsGoal goal = new SavingsGoal("MacBook", start, end, target);
        goal.setSavedAmount(saved);
        return goal;
    }


    @Test
    void toStringIsTheGoalName() {
        SavingsGoal goal = goalWith(LocalDate.now(), LocalDate.now().plusDays(10), 1000.0, 0.0);

        assertEquals("MacBook", goal.toString());
    }
}
