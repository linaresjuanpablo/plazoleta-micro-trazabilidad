package com.gastropolis.trazabilidad.infrastructure.out.jpa.adapter;

import com.gastropolis.trazabilidad.domain.model.OrderLogModel;
import com.gastropolis.trazabilidad.infrastructure.out.jpa.entity.OrderLogEntity;
import com.gastropolis.trazabilidad.infrastructure.out.jpa.mapper.IOrderLogEntityMapper;
import com.gastropolis.trazabilidad.infrastructure.out.jpa.repository.IOrderLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderLogJpaAdapterTest {

    @Mock
    private IOrderLogRepository orderLogRepository;

    @Mock
    private IOrderLogEntityMapper orderLogEntityMapper;

    private OrderLogJpaAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new OrderLogJpaAdapter(orderLogRepository, orderLogEntityMapper);
    }

    @Test
    void save_persistsModelAndReturnsResult() {
        OrderLogModel model = new OrderLogModel(null, 1L, 5L, 7L, "PENDIENTE", "EN_PREPARACION", LocalDateTime.now());
        OrderLogEntity entity = new OrderLogEntity();
        OrderLogEntity savedEntity = new OrderLogEntity();
        savedEntity.setId(1L);
        OrderLogModel savedModel = new OrderLogModel(1L, 1L, 5L, 7L, "PENDIENTE", "EN_PREPARACION", LocalDateTime.now());

        when(orderLogEntityMapper.toEntity(model)).thenReturn(entity);
        when(orderLogRepository.save(entity)).thenReturn(savedEntity);
        when(orderLogEntityMapper.toModel(savedEntity)).thenReturn(savedModel);

        OrderLogModel result = adapter.save(model);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderLogRepository).save(entity);
        verify(orderLogEntityMapper).toModel(savedEntity);
    }

    @Test
    void findByOrderId_returnsModelList() {
        Long orderId = 1L;
        OrderLogEntity entity = new OrderLogEntity();
        List<OrderLogEntity> entities = List.of(entity);
        OrderLogModel model = new OrderLogModel();
        List<OrderLogModel> models = List.of(model);

        when(orderLogRepository.findByOrderId(orderId)).thenReturn(entities);
        when(orderLogEntityMapper.toModelList(entities)).thenReturn(models);

        List<OrderLogModel> result = adapter.findByOrderId(orderId);

        assertEquals(1, result.size());
        verify(orderLogRepository).findByOrderId(orderId);
        verify(orderLogEntityMapper).toModelList(entities);
    }

    @Test
    void findAll_returnsAllModels() {
        List<OrderLogEntity> entities = List.of(new OrderLogEntity(), new OrderLogEntity());
        List<OrderLogModel> models = List.of(new OrderLogModel(), new OrderLogModel());

        when(orderLogRepository.findAll()).thenReturn(entities);
        when(orderLogEntityMapper.toModelList(entities)).thenReturn(models);

        List<OrderLogModel> result = adapter.findAll();

        assertEquals(2, result.size());
        verify(orderLogRepository).findAll();
    }

    @Test
    void findByOrderId_emptyList_returnsEmpty() {
        when(orderLogRepository.findByOrderId(99L)).thenReturn(List.of());
        when(orderLogEntityMapper.toModelList(List.of())).thenReturn(List.of());

        List<OrderLogModel> result = adapter.findByOrderId(99L);

        assertTrue(result.isEmpty());
    }
}
