-- 1. Crear tabla metodos_pago primero (Lado "Uno" de la relación)
CREATE TABLE metodos_pago (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              nombre VARCHAR(10) NOT NULL, -- Longitud máxima de 10 caracteres según tu @Length
                              requiere_api_externa BOOLEAN NOT NULL,
                              servicio_url VARCHAR(255) NULL,
                              disponible BOOLEAN DEFAULT TRUE
);

-- 2. Crear tabla pagos (Lado "Muchos" de la relación)
CREATE TABLE pagos (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       venta_id BIGINT NULL,
                       cliente_id BIGINT NULL,
                       total_final DOUBLE NULL,
                       metodos_pago_id BIGINT NOT NULL,
                       fecha_pago DATETIME NULL,
                       revertido BOOLEAN DEFAULT FALSE,

    -- Restricción de Clave Foránea hacia metodos_pago
                       CONSTRAINT fk_pagos_metodos_pago FOREIGN KEY (metodos_pago_id)
                           REFERENCES metodos_pago(id)
);