package com.example.LibreriaMaketPaymentMS.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPagoInputDTO {


    @NotBlank(message = "Debe ingresar un nombre de medio de pago")
    @Length(message = "Debe ser un nombre con largo mínimo de 2 y máximo 10 caracters.", min = 2, max = 10)
    private String nombre;

    @NotNull
    private Boolean requiereApiExterna;
    private String servicioURL;
}
