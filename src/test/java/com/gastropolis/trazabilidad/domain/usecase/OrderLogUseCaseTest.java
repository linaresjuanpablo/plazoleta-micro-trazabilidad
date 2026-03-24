package com.gastropolis.trazabilidad.domain.usecase;

import com.gastropolis.trazabilidad.domain.exception.OrderLogNotFoundException;
import com.gastropolis.trazabilidad.domain.model.EfficiencyResult;
import com.gastropolis.trazabilidad.domain.model.EmployeeEfficiencyModel;
import com.gastropolis.trazabilidad.domain.model.OrderEfficiencyModel;
import com.gastropolis.trazabilidad.domain.model.OrderLogModel;
import com.gastropolis.trazabilidad.domain.spi.IOrderLogPersistencePort;
import com.gastropolis.trazabilidad.domain.spi.IUserClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderLogUseCaseTest {

    @Mock
    private IOrderLogPersistencePort orderLogPersistencePort;

    @Mock
    private IUserClientPort userClientPort;

    @InjectMocks
    private OrderLogUseCase orderLogUseCase;

    private static final String TOKEN = "Bearer test-token";
    private static final Long ORDER_ID = 1L;
    private static final Long CLIENT_ID = 10L;
    private static final Long EMPLOYEE_ID = 20L;

    // -------------------------------------------------------------------------
    // saveOrderLog
    // -------------------------------------------------------------------------

    @Test
    void saveOrderLog_delegatesToPersistencePort() {
        OrderLogModel input = new OrderLogModel(null, ORDER_ID, CLIENT_ID, null, null, "PENDIENTE",
                LocalDateTime.now());
        OrderLogModel saved = new OrderLogModel(1L, ORDER_ID, CLIENT_ID, null, null, "PENDIENTE",
                LocalDateTime.now());

        when(orderLogPersistencePort.save(input)).thenReturn(saved);

        OrderLogModel result = orderLogUseCase.saveOrderLog(input);

        assertSame(saved, result);
        verify(orderLogPersistencePort).save(input);
        verifyNoInteractions(userClientPort);
    }

    // -------------------------------------------------------------------------
    // getOrderHistory – null list
    // -------------------------------------------------------------------------

    @Test
    void getOrderHistory_whenLogsNull_throwsOrderLogNotFoundException() {
        when(orderLogPersistencePort.findByOrderId(ORDER_ID)).thenReturn(null);

        assertThrows(OrderLogNotFoundException.class,
                () -> orderLogUseCase.getOrderHistory(ORDER_ID, CLIENT_ID, TOKEN));

        verifyNoInteractions(userClientPort);
    }

    // -------------------------------------------------------------------------
    // getOrderHistory – empty list
    // -------------------------------------------------------------------------

    @Test
    void getOrderHistory_whenLogsEmpty_throwsOrderLogNotFoundException() {
        when(orderLogPersistencePort.findByOrderId(ORDER_ID)).thenReturn(Collections.emptyList());

        assertThrows(OrderLogNotFoundException.class,
                () -> orderLogUseCase.getOrderHistory(ORDER_ID, CLIENT_ID, TOKEN));

        verifyNoInteractions(userClientPort);
    }

    // -------------------------------------------------------------------------
    // getOrderHistory – requester is NOT the owner
    // -------------------------------------------------------------------------

    @Test
    void getOrderHistory_whenRequestingUserIsNotOwner_throwsAccessDeniedException() {
        Long differentClientId = 99L;
        OrderLogModel log = new OrderLogModel(1L, ORDER_ID, CLIENT_ID, null, null, "PENDIENTE",
                LocalDateTime.now());

        when(orderLogPersistencePort.findByOrderId(ORDER_ID)).thenReturn(List.of(log));

        assertThrows(AccessDeniedException.class,
                () -> orderLogUseCase.getOrderHistory(ORDER_ID, differentClientId, TOKEN));

        verifyNoInteractions(userClientPort);
    }

    // -------------------------------------------------------------------------
    // getOrderHistory – owner, NO employee on any log
    // -------------------------------------------------------------------------

    @Test
    void getOrderHistory_whenOwnerAndNoEmployee_setsClientNameOnly() {
        OrderLogModel log = new OrderLogModel(1L, ORDER_ID, CLIENT_ID, null, "PENDIENTE", "EN_PREPARACION",
                LocalDateTime.now());

        when(orderLogPersistencePort.findByOrderId(ORDER_ID)).thenReturn(List.of(log));
        when(userClientPort.getUserNameById(CLIENT_ID, TOKEN)).thenReturn("Juan Cliente");

        List<OrderLogModel> result = orderLogUseCase.getOrderHistory(ORDER_ID, CLIENT_ID, TOKEN);

        assertEquals(1, result.size());
        assertEquals("Juan Cliente", result.get(0).getClientName());
        assertNull(result.get(0).getEmployeeName());

        verify(userClientPort, times(1)).getUserNameById(CLIENT_ID, TOKEN);
        verify(userClientPort, never()).getUserNameById(EMPLOYEE_ID, TOKEN);
    }

    // -------------------------------------------------------------------------
    // getOrderHistory – owner, log HAS an employee
    // -------------------------------------------------------------------------

    @Test
    void getOrderHistory_whenOwnerAndEmployeePresent_setsClientAndEmployeeName() {
        OrderLogModel log = new OrderLogModel(1L, ORDER_ID, CLIENT_ID, EMPLOYEE_ID, "PENDIENTE",
                "EN_PREPARACION", LocalDateTime.now());

        when(orderLogPersistencePort.findByOrderId(ORDER_ID)).thenReturn(List.of(log));
        when(userClientPort.getUserNameById(CLIENT_ID, TOKEN)).thenReturn("Juan Cliente");
        when(userClientPort.getUserNameById(EMPLOYEE_ID, TOKEN)).thenReturn("Maria Empleada");

        List<OrderLogModel> result = orderLogUseCase.getOrderHistory(ORDER_ID, CLIENT_ID, TOKEN);

        assertEquals(1, result.size());
        assertEquals("Juan Cliente", result.get(0).getClientName());
        assertEquals("Maria Empleada", result.get(0).getEmployeeName());

        verify(userClientPort).getUserNameById(CLIENT_ID, TOKEN);
        verify(userClientPort).getUserNameById(EMPLOYEE_ID, TOKEN);
    }

    // -------------------------------------------------------------------------
    // getOrderHistory – multiple logs, mixed employee presence
    // -------------------------------------------------------------------------

    @Test
    void getOrderHistory_withMultipleLogs_mixedEmployeePresence() {
        OrderLogModel logWithEmployee = new OrderLogModel(1L, ORDER_ID, CLIENT_ID, EMPLOYEE_ID,
                null, "EN_PREPARACION", LocalDateTime.now());
        OrderLogModel logWithoutEmployee = new OrderLogModel(2L, ORDER_ID, CLIENT_ID, null,
                "EN_PREPARACION", "LISTO", LocalDateTime.now().plusMinutes(10));

        when(orderLogPersistencePort.findByOrderId(ORDER_ID))
                .thenReturn(Arrays.asList(logWithEmployee, logWithoutEmployee));
        when(userClientPort.getUserNameById(CLIENT_ID, TOKEN)).thenReturn("Juan Cliente");
        when(userClientPort.getUserNameById(EMPLOYEE_ID, TOKEN)).thenReturn("Maria Empleada");

        List<OrderLogModel> result = orderLogUseCase.getOrderHistory(ORDER_ID, CLIENT_ID, TOKEN);

        assertEquals(2, result.size());
        result.forEach(log -> assertEquals("Juan Cliente", log.getClientName()));
        assertEquals("Maria Empleada", logWithEmployee.getEmployeeName());
        assertNull(logWithoutEmployee.getEmployeeName());
    }

    // -------------------------------------------------------------------------
    // getEfficiency – empty log list
    // -------------------------------------------------------------------------

    @Test
    void getEfficiency_whenNoLogs_returnsEmptyResult() {
        when(orderLogPersistencePort.findAll()).thenReturn(Collections.emptyList());

        EfficiencyResult result = orderLogUseCase.getEfficiency(TOKEN);

        assertNotNull(result);
        assertTrue(result.getOrders().isEmpty());
        assertTrue(result.getEmployeeRanking().isEmpty());
        verifyNoInteractions(userClientPort);
    }

    // -------------------------------------------------------------------------
    // getEfficiency – complete order (startLog + endLog present), with employee
    // -------------------------------------------------------------------------

    @Test
    void getEfficiency_withCompleteOrderAndEmployee_calculatesCorrectly() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 10, 30, 0);

        // Start log: previousStatus is null
        OrderLogModel startLog = new OrderLogModel(1L, ORDER_ID, CLIENT_ID, EMPLOYEE_ID,
                null, "EN_PREPARACION", start);
        // End log: newStatus = ENTREGADO
        OrderLogModel endLog = new OrderLogModel(2L, ORDER_ID, CLIENT_ID, EMPLOYEE_ID,
                "EN_PREPARACION", "ENTREGADO", end);

        when(orderLogPersistencePort.findAll()).thenReturn(Arrays.asList(startLog, endLog));
        when(userClientPort.getUserNameById(EMPLOYEE_ID, TOKEN)).thenReturn("Maria Empleada");

        EfficiencyResult result = orderLogUseCase.getEfficiency(TOKEN);

        assertNotNull(result);
        assertEquals(1, result.getOrders().size());
        OrderEfficiencyModel orderEfficiency = result.getOrders().get(0);
        assertEquals(ORDER_ID, orderEfficiency.getOrderId());
        assertEquals(start, orderEfficiency.getStartTime());
        assertEquals(end, orderEfficiency.getEndTime());
        assertEquals(30.0, orderEfficiency.getDurationMinutes(), 0.01);

        assertEquals(1, result.getEmployeeRanking().size());
        EmployeeEfficiencyModel empEfficiency = result.getEmployeeRanking().get(0);
        assertEquals(EMPLOYEE_ID, empEfficiency.getEmployeeId());
        assertEquals("Maria Empleada", empEfficiency.getEmployeeName());
        assertEquals(30.0, empEfficiency.getAverageDurationMinutes(), 0.01);
        assertEquals(1L, empEfficiency.getTotalOrders());

        verify(userClientPort).getUserNameById(EMPLOYEE_ID, TOKEN);
    }

    // -------------------------------------------------------------------------
    // getEfficiency – incomplete order (no endLog), must NOT be added
    // -------------------------------------------------------------------------

    @Test
    void getEfficiency_withIncompleteOrder_notAddedToResult() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0, 0);

        // Only a start log, no ENTREGADO log
        OrderLogModel startLog = new OrderLogModel(1L, ORDER_ID, CLIENT_ID, EMPLOYEE_ID,
                null, "EN_PREPARACION", start);
        OrderLogModel middleLog = new OrderLogModel(2L, ORDER_ID, CLIENT_ID, EMPLOYEE_ID,
                "EN_PREPARACION", "LISTO", start.plusMinutes(15));

        when(orderLogPersistencePort.findAll()).thenReturn(Arrays.asList(startLog, middleLog));

        EfficiencyResult result = orderLogUseCase.getEfficiency(TOKEN);

        assertNotNull(result);
        assertTrue(result.getOrders().isEmpty());
        assertTrue(result.getEmployeeRanking().isEmpty());
        verifyNoInteractions(userClientPort);
    }

    // -------------------------------------------------------------------------
    // getEfficiency – order without any employee logs
    // -------------------------------------------------------------------------

    @Test
    void getEfficiency_withCompleteOrderButNoEmployee_orderRecordedNoEmployeeRanking() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 9, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 9, 45, 0);

        OrderLogModel startLog = new OrderLogModel(1L, ORDER_ID, CLIENT_ID, null,
                null, "EN_PREPARACION", start);
        OrderLogModel endLog = new OrderLogModel(2L, ORDER_ID, CLIENT_ID, null,
                "EN_PREPARACION", "ENTREGADO", end);

        when(orderLogPersistencePort.findAll()).thenReturn(Arrays.asList(startLog, endLog));

        EfficiencyResult result = orderLogUseCase.getEfficiency(TOKEN);

        assertNotNull(result);
        assertEquals(1, result.getOrders().size());
        assertEquals(ORDER_ID, result.getOrders().get(0).getOrderId());
        assertTrue(result.getEmployeeRanking().isEmpty());
        verifyNoInteractions(userClientPort);
    }

    // -------------------------------------------------------------------------
    // getEfficiency – multiple orders, multiple employees, sorted by avg duration
    // -------------------------------------------------------------------------

    @Test
    void getEfficiency_multipleOrdersMultipleEmployees_sortedByAverageDuration() {
        Long orderId1 = 1L;
        Long orderId2 = 2L;
        Long employeeA = 100L;
        Long employeeB = 200L;

        LocalDateTime start1 = LocalDateTime.of(2025, 1, 1, 8, 0, 0);
        LocalDateTime end1 = LocalDateTime.of(2025, 1, 1, 8, 10, 0); // 10 min

        LocalDateTime start2 = LocalDateTime.of(2025, 1, 2, 8, 0, 0);
        LocalDateTime end2 = LocalDateTime.of(2025, 1, 2, 8, 50, 0); // 50 min

        // Order 1: only employeeA
        OrderLogModel s1 = new OrderLogModel(1L, orderId1, CLIENT_ID, employeeA, null, "EN_PREPARACION", start1);
        OrderLogModel e1 = new OrderLogModel(2L, orderId1, CLIENT_ID, employeeA, "EN_PREPARACION", "ENTREGADO", end1);

        // Order 2: only employeeB
        OrderLogModel s2 = new OrderLogModel(3L, orderId2, CLIENT_ID, employeeB, null, "EN_PREPARACION", start2);
        OrderLogModel e2 = new OrderLogModel(4L, orderId2, CLIENT_ID, employeeB, "EN_PREPARACION", "ENTREGADO", end2);

        when(orderLogPersistencePort.findAll()).thenReturn(Arrays.asList(s1, e1, s2, e2));
        when(userClientPort.getUserNameById(employeeA, TOKEN)).thenReturn("Empleado A");
        when(userClientPort.getUserNameById(employeeB, TOKEN)).thenReturn("Empleado B");

        EfficiencyResult result = orderLogUseCase.getEfficiency(TOKEN);

        assertEquals(2, result.getOrders().size());
        assertEquals(2, result.getEmployeeRanking().size());

        // Sorted ascending by averageDurationMinutes: A (10 min) comes before B (50 min)
        EmployeeEfficiencyModel first = result.getEmployeeRanking().get(0);
        EmployeeEfficiencyModel second = result.getEmployeeRanking().get(1);
        assertTrue(first.getAverageDurationMinutes() <= second.getAverageDurationMinutes());
        assertEquals(employeeA, first.getEmployeeId());
        assertEquals("Empleado A", first.getEmployeeName());
        assertEquals(employeeB, second.getEmployeeId());
        assertEquals("Empleado B", second.getEmployeeName());
    }

    // -------------------------------------------------------------------------
    // getEfficiency – one employee handles multiple orders (average computed)
    // -------------------------------------------------------------------------

    @Test
    void getEfficiency_sameEmployeeMultipleOrders_averageDurationComputed() {
        Long orderId1 = 1L;
        Long orderId2 = 2L;

        LocalDateTime start1 = LocalDateTime.of(2025, 3, 1, 10, 0, 0);
        LocalDateTime end1 = LocalDateTime.of(2025, 3, 1, 10, 20, 0); // 20 min

        LocalDateTime start2 = LocalDateTime.of(2025, 3, 2, 10, 0, 0);
        LocalDateTime end2 = LocalDateTime.of(2025, 3, 2, 10, 40, 0); // 40 min

        OrderLogModel s1 = new OrderLogModel(1L, orderId1, CLIENT_ID, EMPLOYEE_ID, null, "EN_PREPARACION", start1);
        OrderLogModel e1 = new OrderLogModel(2L, orderId1, CLIENT_ID, EMPLOYEE_ID, "EN_PREPARACION", "ENTREGADO", end1);

        OrderLogModel s2 = new OrderLogModel(3L, orderId2, CLIENT_ID, EMPLOYEE_ID, null, "EN_PREPARACION", start2);
        OrderLogModel e2 = new OrderLogModel(4L, orderId2, CLIENT_ID, EMPLOYEE_ID, "EN_PREPARACION", "ENTREGADO", end2);

        when(orderLogPersistencePort.findAll()).thenReturn(Arrays.asList(s1, e1, s2, e2));
        when(userClientPort.getUserNameById(EMPLOYEE_ID, TOKEN)).thenReturn("Maria Empleada");

        EfficiencyResult result = orderLogUseCase.getEfficiency(TOKEN);

        assertEquals(2, result.getOrders().size());
        assertEquals(1, result.getEmployeeRanking().size());

        EmployeeEfficiencyModel emp = result.getEmployeeRanking().get(0);
        assertEquals(EMPLOYEE_ID, emp.getEmployeeId());
        assertEquals(30.0, emp.getAverageDurationMinutes(), 0.01); // (20 + 40) / 2 = 30
        assertEquals(2L, emp.getTotalOrders());
        assertEquals("Maria Empleada", emp.getEmployeeName());
    }
}
