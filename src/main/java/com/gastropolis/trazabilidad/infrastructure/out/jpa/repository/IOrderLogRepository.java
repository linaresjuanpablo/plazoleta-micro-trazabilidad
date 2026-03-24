package com.gastropolis.trazabilidad.infrastructure.out.jpa.repository;

import com.gastropolis.trazabilidad.infrastructure.out.jpa.entity.OrderLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderLogRepository extends JpaRepository<OrderLogEntity, Long> {

    List<OrderLogEntity> findByOrderId(Long orderId);
}
