package com.gastropolis.trazabilidad.infrastructure.out.feign;

import com.gastropolis.trazabilidad.infrastructure.out.feign.dto.UserFeignResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-usuarios", url = "${feign.usuarios.url}")
public interface UserFeignClient {

    @GetMapping("/api/v1/user/{id}")
    UserFeignResponseDto getUserById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);
}
