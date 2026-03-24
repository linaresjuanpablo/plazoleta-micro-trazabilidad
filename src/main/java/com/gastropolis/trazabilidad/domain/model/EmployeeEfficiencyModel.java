package com.gastropolis.trazabilidad.domain.model;

public class EmployeeEfficiencyModel {

    private Long employeeId;
    private String employeeName;
    private Double averageDurationMinutes;
    private Long totalOrders;

    public EmployeeEfficiencyModel() {
    }

    public EmployeeEfficiencyModel(Long employeeId, Double averageDurationMinutes, Long totalOrders) {
        this.employeeId = employeeId;
        this.averageDurationMinutes = averageDurationMinutes;
        this.totalOrders = totalOrders;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Double getAverageDurationMinutes() {
        return averageDurationMinutes;
    }

    public void setAverageDurationMinutes(Double averageDurationMinutes) {
        this.averageDurationMinutes = averageDurationMinutes;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }
}
