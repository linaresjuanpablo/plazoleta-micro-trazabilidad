package com.gastropolis.trazabilidad.domain.spi;

public interface IUserClientPort {
    String getUserNameById(Long userId, String token);
}
