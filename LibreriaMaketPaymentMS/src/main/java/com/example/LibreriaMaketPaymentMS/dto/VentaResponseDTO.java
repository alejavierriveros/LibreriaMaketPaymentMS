package com.example.LibreriaMaketPaymentMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaResponseDTO {

    private Long Id;
    private Long clienteId;
    private String clienteNombre;
    private String clienteRun;
    private Double total;
    private String descuento;
    private Double totalFinal;
    private String direccionEnvio;
    private Boolean realizado;
}
