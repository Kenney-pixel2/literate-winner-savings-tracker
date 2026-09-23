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

    @Test
    void dailyAmountSplitsRemainingAcrossRemainingDays() {
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(9); // 10 remaining days, counting today

        SavingsGoal goal = new SavingsGoal("MacBook", today, end, 1000.0);
        goal.setSavedAmount(0.0);

        assertEquals(100.0, goal.getDailyAmountToSave());
    }

    @Test
    void dailyAmountIsWholeRemainingWhenEndDateIsToday() {
        LocalDate today = LocalDate.now();

        SavingsGoal goal = new SavingsGoal("MacBook", today, today, 500.0);
        goal.setSavedAmount(200.0);

        // only 1 day left, so the whole remaining balance is due today
        assertEquals(300.0, goal.getDailyAmountToSave());
    }

    @Test
    void dailyAmountIsZeroWhenTargetAlreadyReached() {
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(10);

        SavingsGoal goal = new SavingsGoal("MacBook", today, end, 1000.0);
        goal.setSavedAmount(1000.0);

        assertEquals(0.0, goal.getDailyAmountToSave());
    }

    @Test
    void progressPercentIsSavedOverTarget() {
        SavingsGoal goal = new SavingsGoal("MacBook",
                LocalDate.now(), LocalDate.now().plusDays(10), 1000.0);
        goal.setSavedAmount(250.0);

        assertEquals(25.0, goal.getProgressPercent());
    }

    @Test
    void progressPercentIsCappedAtOneHundred() {
        SavingsGoal goal = new SavingsGoal("MacBook",
                LocalDate.now(), LocalDate.now().plusDays(10), 1000.0);
        goal.setSavedAmount(1500.0); // saved more than the target

        assertEquals(100.0, goal.getProgressPercent());
    }

    @Test
    void isCompleteWhenSavedMeetsOrExceedsTarget() {
        SavingsGoal goal = new SavingsGoal("MacBook",
                LocalDate.now(), LocalDate.now().plusDays(10), 1000.0);
        goal.setSavedAmount(1000.0);

        assertTrue(goal.isComplete());
    }

    @Test
    void isNotCompleteWhenSavedIsBelowTarget() {
        SavingsGoal goal = new SavingsGoal("MacBook",
                LocalDate.now(), LocalDate.now().plusDays(10), 1000.0);
        goal.setSavedAmount(999.0);

        assertFalse(goal.isComplete());
    }

    @Test
    void isOverdueWhenEndDateIsPastAndNotComplete() {
        SavingsGoal goal = new SavingsGoal("MacBook",
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(1), 1000.0);
        goal.setSavedAmount(500.0); // not complete, and end date already passed

        assertTrue(goal.isOverdue());
    }
}
