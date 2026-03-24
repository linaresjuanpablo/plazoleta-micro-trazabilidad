-- Script DDL para crear la base de datos bd_trazabilidad
-- Ejecutar manualmente en MySQL antes de iniciar la aplicación

CREATE DATABASE IF NOT EXISTS bd_trazabilidad
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE bd_trazabilidad;

CREATE TABLE IF NOT EXISTS order_logs (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    order_id      BIGINT       NOT NULL,
    client_id     BIGINT       NOT NULL,
    employee_id   BIGINT       NULL,
    previous_status VARCHAR(20) NULL,
    new_status    VARCHAR(20)  NOT NULL,
    timestamp     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
