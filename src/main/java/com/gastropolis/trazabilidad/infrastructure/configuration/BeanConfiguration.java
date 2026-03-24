package com.gastropolis.trazabilidad.infrastructure.configuration;

import com.gastropolis.trazabilidad.domain.api.IOrderLogServicePort;
import com.gastropolis.trazabilidad.domain.spi.IOrderLogPersistencePort;
import com.gastropolis.trazabilidad.domain.spi.IUserClientPort;
import com.gastropolis.trazabilidad.domain.usecase.OrderLogUseCase;
import com.gastropolis.trazabilidad.infrastructure.out.feign.UserFeignClient;
import com.gastropolis.trazabilidad.infrastructure.out.feign.adapter.UserFeignAdapter;
import com.gastropolis.trazabilidad.infrastructure.out.jpa.adapter.OrderLogJpaAdapter;
import com.gastropolis.trazabilidad.infrastructure.out.jpa.mapper.IOrderLogEntityMapper;
import com.gastropolis.trazabilidad.infrastructure.out.jpa.repository.IOrderLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IOrderLogRepository orderLogRepository;
    private final IOrderLogEntityMapper orderLogEntityMapper;
    private final UserFeignClient userFeignClient;

    @Bean
    public IOrderLogPersistencePort orderLogPersistencePort() {
        return new OrderLogJpaAdapter(orderLogRepository, orderLogEntityMapper);
    }

    @Bean
    public IUserClientPort userClientPort() {
        return new UserFeignAdapter(userFeignClient);
    }

    @Bean
    public IOrderLogServicePort orderLogServicePort(IOrderLogPersistencePort orderLogPersistencePort,
                                                    IUserClientPort userClientPort) {
        return new OrderLogUseCase(orderLogPersistencePort, userClientPort);
    }
}
