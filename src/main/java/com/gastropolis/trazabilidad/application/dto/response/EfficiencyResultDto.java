package com.gastropolis.trazabilidad.application.dto.response;

import java.util.List;

public class EfficiencyResultDto {

    private List<OrderEfficiencyDto> orders;
    private List<EmployeeEfficiencyDto> employeeRanking;

    public EfficiencyResultDto() {
    }

    public EfficiencyResultDto(List<OrderEfficiencyDto> orders, List<EmployeeEfficiencyDto> employeeRanking) {
        this.orders = orders;
        this.employeeRanking = employeeRanking;
    }

    public List<OrderEfficiencyDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEfficiencyDto> orders) {
        this.orders = orders;
    }

    public List<EmployeeEfficiencyDto> getEmployeeRanking() {
        return employeeRanking;
    }

    public void setEmployeeRanking(List<EmployeeEfficiencyDto> employeeRanking) {
        this.employeeRanking = employeeRanking;
    }
}
