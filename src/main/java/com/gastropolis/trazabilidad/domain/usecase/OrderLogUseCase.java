package com.gastropolis.trazabilidad.domain.usecase;

import com.gastropolis.trazabilidad.domain.api.IOrderLogServicePort;
import com.gastropolis.trazabilidad.domain.exception.OrderLogNotFoundException;
import com.gastropolis.trazabilidad.domain.model.EfficiencyResult;
import com.gastropolis.trazabilidad.domain.model.EmployeeEfficiencyModel;
import com.gastropolis.trazabilidad.domain.model.OrderEfficiencyModel;
import com.gastropolis.trazabilidad.domain.model.OrderLogModel;
import com.gastropolis.trazabilidad.domain.spi.IOrderLogPersistencePort;
import com.gastropolis.trazabilidad.domain.spi.IUserClientPort;
import org.springframework.security.access.AccessDeniedException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class OrderLogUseCase implements IOrderLogServicePort {

    private final IOrderLogPersistencePort orderLogPersistencePort;
    private final IUserClientPort userClientPort;

    public OrderLogUseCase(IOrderLogPersistencePort orderLogPersistencePort, IUserClientPort userClientPort) {
        this.orderLogPersistencePort = orderLogPersistencePort;
        this.userClientPort = userClientPort;
    }

    @Override
    public OrderLogModel saveOrderLog(OrderLogModel orderLogModel) {
        return orderLogPersistencePort.save(orderLogModel);
    }

    @Override
    public List<OrderLogModel> getOrderHistory(Long orderId, Long requestingUserId, String token) {
        List<OrderLogModel> logs = orderLogPersistencePort.findByOrderId(orderId);

        if (logs == null || logs.isEmpty()) {
            throw new OrderLogNotFoundException(orderId);
        }

        boolean isOwner = logs.stream()
                .anyMatch(log -> log.getClientId() != null && log.getClientId().equals(requestingUserId));

        if (!isOwner) {
            throw new AccessDeniedException(
                    "No tiene permiso para ver el historial del pedido con ID: " + orderId);
        }

        String clientName = userClientPort.getUserNameById(requestingUserId, token);
        logs.forEach(log -> {
            log.setClientName(clientName);
            if (log.getEmployeeId() != null) {
                String employeeName = userClientPort.getUserNameById(log.getEmployeeId(), token);
                log.setEmployeeName(employeeName);
            }
        });

        return logs;
    }

    @Override
    public EfficiencyResult getEfficiency(String token) {
        List<OrderLogModel> allLogs = orderLogPersistencePort.findAll();

        Map<Long, List<OrderLogModel>> logsByOrder = allLogs.stream()
                .collect(Collectors.groupingBy(OrderLogModel::getOrderId));

        List<OrderEfficiencyModel> orderEfficiencies = new ArrayList<>();

        for (Map.Entry<Long, List<OrderLogModel>> entry : logsByOrder.entrySet()) {
            Long orderId = entry.getKey();
            List<OrderLogModel> orderLogs = entry.getValue();

            Optional<OrderLogModel> startLog = orderLogs.stream()
                    .filter(log -> log.getPreviousStatus() == null)
                    .findFirst();

            Optional<OrderLogModel> endLog = orderLogs.stream()
                    .filter(log -> "ENTREGADO".equals(log.getNewStatus()))
                    .findFirst();

            if (startLog.isPresent() && endLog.isPresent()) {
                LocalDateTime startTime = startLog.get().getTimestamp();
                LocalDateTime endTime = endLog.get().getTimestamp();
                double durationMinutes = Duration.between(startTime, endTime).toSeconds() / 60.0;

                orderEfficiencies.add(new OrderEfficiencyModel(orderId, startTime, endTime, durationMinutes));
            }
        }

        Map<Long, List<OrderEfficiencyModel>> efficienciesByEmployee = new HashMap<>();
        for (Map.Entry<Long, List<OrderLogModel>> entry : logsByOrder.entrySet()) {
            Long orderId = entry.getKey();
            List<OrderLogModel> orderLogs = entry.getValue();

            Optional<OrderEfficiencyModel> orderEff = orderEfficiencies.stream()
                    .filter(e -> e.getOrderId().equals(orderId))
                    .findFirst();

            if (orderEff.isPresent()) {
                Set<Long> employeeIds = orderLogs.stream()
                        .filter(log -> log.getEmployeeId() != null)
                        .map(OrderLogModel::getEmployeeId)
                        .collect(Collectors.toSet());

                for (Long employeeId : employeeIds) {
                    efficienciesByEmployee
                            .computeIfAbsent(employeeId, k -> new ArrayList<>())
                            .add(orderEff.get());
                }
            }
        }

        List<EmployeeEfficiencyModel> employeeEfficiencies = efficienciesByEmployee.entrySet().stream()
                .map(entry -> {
                    Long employeeId = entry.getKey();
                    List<OrderEfficiencyModel> orders = entry.getValue();
                    double avgDuration = orders.stream()
                            .mapToDouble(OrderEfficiencyModel::getDurationMinutes)
                            .average()
                            .orElse(0.0);
                    EmployeeEfficiencyModel model = new EmployeeEfficiencyModel(employeeId, avgDuration, (long) orders.size());
                    String employeeName = userClientPort.getUserNameById(employeeId, token);
                    model.setEmployeeName(employeeName);
                    return model;
                })
                .sorted(Comparator.comparingDouble(EmployeeEfficiencyModel::getAverageDurationMinutes))
                .collect(Collectors.toList());

        return new EfficiencyResult(orderEfficiencies, employeeEfficiencies);
    }
}
