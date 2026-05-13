package com.example.LibreriaMaketPaymentMS.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO {

    private Long pedidoId;
    private String metodoPago;
}