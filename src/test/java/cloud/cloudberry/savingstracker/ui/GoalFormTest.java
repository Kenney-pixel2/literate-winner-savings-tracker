package cloud.cloudberry.savingstracker.ui;

import cloud.cloudberry.savingstracker.model.SavingsGoal;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GoalFormTest {

    private static final LocalDate START = LocalDate.of(2026, 1, 1);
    private static final LocalDate END = LocalDate.of(2026, 6, 1);

    @Test
    void validInputProducesAGoal() {
        SavingsGoal goal = GoalForm.parse("MacBook", START, END, "1200");

        assertEquals("MacBook", goal.getName());
        assertEquals(START, goal.getStartDate());
        assertEquals(END, goal.getEndDate());
        assertEquals(1200.0, goal.getTargetAmount());
    }

    @Test
    void nameIsTrimmed() {
        SavingsGoal goal = GoalForm.parse("  MacBook  ", START, END, "1200");

        assertEquals("MacBook", goal.getName());
    }

    @Test
    void blankNameIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> GoalForm.parse("   ", START, END, "1200"));

        assertEquals("Enter a name for your goal.", e.getMessage());
    }

    @Test
    void endDateBeforeStartDateIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> GoalForm.parse("MacBook", END, START, "1200"));

        assertEquals("The end date can't be before the start date.", e.getMessage());
    }

    @Test
    void nonNumericTargetIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> GoalForm.parse("MacBook", START, END, "abc"));

        assertEquals("Enter the target amount as a number, like 1200.", e.getMessage());
    }

    @Test
    void zeroTargetIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> GoalForm.parse("MacBook", START, END, "0"));

        assertEquals("The target amount must be more than zero.", e.getMessage());
    }

    @Test
    void negativeTargetIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> GoalForm.parse("MacBook", START, END, "-50"));

        assertEquals("The target amount must be more than zero.", e.getMessage());
    }

    @Test
    void infinityTargetIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> GoalForm.parse("MacBook", START, END, "Infinity"));

        assertEquals("The target amount must be more than zero.", e.getMessage());
    }
}
