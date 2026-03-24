package com.gastropolis.trazabilidad.domain.model;

import java.util.List;

public class EfficiencyResult {

    private List<OrderEfficiencyModel> orders;
    private List<EmployeeEfficiencyModel> employeeRanking;

    public EfficiencyResult() {
    }

    public EfficiencyResult(List<OrderEfficiencyModel> orders, List<EmployeeEfficiencyModel> employeeRanking) {
        this.orders = orders;
        this.employeeRanking = employeeRanking;
    }

    public List<OrderEfficiencyModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEfficiencyModel> orders) {
        this.orders = orders;
    }

    public List<EmployeeEfficiencyModel> getEmployeeRanking() {
        return employeeRanking;
    }

    public void setEmployeeRanking(List<EmployeeEfficiencyModel> employeeRanking) {
        this.employeeRanking = employeeRanking;
    }
}
