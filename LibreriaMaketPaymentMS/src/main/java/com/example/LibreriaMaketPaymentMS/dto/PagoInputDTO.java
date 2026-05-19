package com.example.LibreriaMaketPaymentMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoInputDTO {

    private Long ventaId;

    @NotNull(message = "Metodo de pago Obligatorio")
    private Long metodoPagoId;
}