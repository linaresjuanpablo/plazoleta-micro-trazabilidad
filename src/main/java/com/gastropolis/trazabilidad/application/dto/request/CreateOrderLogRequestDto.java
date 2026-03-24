package com.gastropolis.trazabilidad.application.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateOrderLogRequestDto {

    @NotNull(message = "El ID del pedido es obligatorio")
    private Long orderId;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clientId;

    private Long employeeId;

    private String previousStatus;

    @NotNull(message = "El nuevo estado es obligatorio")
    private String newStatus;

    @NotNull(message = "El timestamp es obligatorio")
    private LocalDateTime timestamp;

    public CreateOrderLogRequestDto() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
