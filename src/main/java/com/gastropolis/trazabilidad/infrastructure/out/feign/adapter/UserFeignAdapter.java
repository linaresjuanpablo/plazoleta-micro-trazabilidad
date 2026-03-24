package com.gastropolis.trazabilidad.infrastructure.out.feign.adapter;

import com.gastropolis.trazabilidad.domain.spi.IUserClientPort;
import com.gastropolis.trazabilidad.infrastructure.out.feign.UserFeignClient;
import com.gastropolis.trazabilidad.infrastructure.out.feign.dto.UserFeignResponseDto;

public class UserFeignAdapter implements IUserClientPort {

    private final UserFeignClient userFeignClient;

    public UserFeignAdapter(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    @Override
    public String getUserNameById(Long userId, String token) {
        UserFeignResponseDto user = userFeignClient.getUserById(userId, token);
        return user.getName() + " " + user.getLastName();
    }
}
