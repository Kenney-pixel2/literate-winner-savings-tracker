package cloud.cloudberry.savingstracker.model;

import java.time.LocalDate;

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
}
