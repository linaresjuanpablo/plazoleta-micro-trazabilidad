package com.gastropolis.trazabilidad.infrastructure.out.jpa.mapper;

import com.gastropolis.trazabilidad.domain.model.OrderLogModel;
import com.gastropolis.trazabilidad.infrastructure.out.jpa.entity.OrderLogEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T22:51:30-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.2.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IOrderLogEntityMapperImpl implements IOrderLogEntityMapper {

    @Override
    public OrderLogEntity toEntity(OrderLogModel model) {
        if ( model == null ) {
            return null;
        }

        OrderLogEntity orderLogEntity = new OrderLogEntity();

        orderLogEntity.setId( model.getId() );
        orderLogEntity.setOrderId( model.getOrderId() );
        orderLogEntity.setClientId( model.getClientId() );
        orderLogEntity.setEmployeeId( model.getEmployeeId() );
        orderLogEntity.setPreviousStatus( model.getPreviousStatus() );
        orderLogEntity.setNewStatus( model.getNewStatus() );
        orderLogEntity.setTimestamp( model.getTimestamp() );

        return orderLogEntity;
    }

    @Override
    public OrderLogModel toModel(OrderLogEntity entity) {
        if ( entity == null ) {
            return null;
        }

        OrderLogModel orderLogModel = new OrderLogModel();

        orderLogModel.setId( entity.getId() );
        orderLogModel.setOrderId( entity.getOrderId() );
        orderLogModel.setClientId( entity.getClientId() );
        orderLogModel.setEmployeeId( entity.getEmployeeId() );
        orderLogModel.setPreviousStatus( entity.getPreviousStatus() );
        orderLogModel.setNewStatus( entity.getNewStatus() );
        orderLogModel.setTimestamp( entity.getTimestamp() );

        return orderLogModel;
    }

    @Override
    public List<OrderLogModel> toModelList(List<OrderLogEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<OrderLogModel> list = new ArrayList<OrderLogModel>( entities.size() );
        for ( OrderLogEntity orderLogEntity : entities ) {
            list.add( toModel( orderLogEntity ) );
        }

        return list;
    }
}
