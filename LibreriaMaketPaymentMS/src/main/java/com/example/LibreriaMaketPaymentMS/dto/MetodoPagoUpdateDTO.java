package com.example.LibreriaMaketPaymentMS.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPagoUpdateDTO {

    @NotNull
    @Positive
    private Long id;
    private String nombre;
    private Boolean requiereApiExterna;
    private String servicioURL;
    private Boolean disponible;
}
