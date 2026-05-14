package com.example.LibreriaMaketPaymentMS.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO {

    private Long pedidoId;

    @NotBlank(message = "Metodo de pago Obligatorio")
    private String metodoPago;
}