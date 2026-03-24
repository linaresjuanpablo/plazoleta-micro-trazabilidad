package com.gastropolis.trazabilidad.domain.exception;

public class OrderLogNotFoundException extends RuntimeException {

    public OrderLogNotFoundException(String message) {
        super(message);
    }

    public OrderLogNotFoundException(Long orderId) {
        super("No se encontraron registros de trazabilidad para el pedido con ID: " + orderId);
    }
}
