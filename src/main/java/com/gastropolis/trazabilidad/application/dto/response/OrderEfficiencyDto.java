package com.gastropolis.trazabilidad.application.dto.response;

import java.time.LocalDateTime;

public class OrderEfficiencyDto {

    private Long orderId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double durationMinutes;

    public OrderEfficiencyDto() {
    }

    public OrderEfficiencyDto(Long orderId, LocalDateTime startTime, LocalDateTime endTime, Double durationMinutes) {
        this.orderId = orderId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Double getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Double durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
