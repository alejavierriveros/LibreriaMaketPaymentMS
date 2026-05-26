package com.example.LibreriaMaketPaymentMS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "metodos_pago")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Debe ingresar un nombre de medio de pago")
    @Length(message = "Debe ser un nombre con largo mínimo de 2 y máximo 10 caracters.", min = 2, max = 10)
    private String nombre;
    @NotNull
    @Column(name = "requiere_api_externa")
    private Boolean requiereApiExterna;

    @Column(name = "servicio_url")
    private String servicioURL;
    private Boolean disponible;
}
