package com.example.LibreriaMaketPaymentMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoResponseDTO {

    private Long id;
    private Long pedidoId;
    private Long clienteId;
    private Double totalFinal;
    private String metodoPago;
    private LocalDateTime fechaPago;
}
