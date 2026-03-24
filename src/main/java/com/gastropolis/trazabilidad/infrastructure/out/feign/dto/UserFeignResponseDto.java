package com.gastropolis.trazabilidad.infrastructure.out.feign.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserFeignResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String phone;
    private String email;
    private String roleName;
}
