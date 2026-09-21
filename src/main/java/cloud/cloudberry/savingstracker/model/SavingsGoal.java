package cloud.cloudberry.savingstracker.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SavingsGoal {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private double targetAmount;
    private double savedAmount;
    
    public SavingsGoal(String name, LocalDate startDate, LocalDate endDate, double targetAmount) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetAmount = targetAmount;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getTargetAmount() {
        return targetAmount;
    }
    
    public void setSavedAmount(double savedAmount) {
        this.savedAmount = savedAmount;
    }

    public double getRemainingAmount() {
        return targetAmount - savedAmount;
    }    

    public long getRemainingDays() {
        LocalDate today = LocalDate.now();
        if (today.isAfter(endDate)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(today, endDate) + 1; // +1 so today counts as a saving day
    }

    public double getDailyAmountToSave() {
        double remaining = getRemainingAmount();
        if (remaining <= 0) {
            return 0.0;
        }
        long days = getRemainingDays();
        return remaining / days;
    }
}
