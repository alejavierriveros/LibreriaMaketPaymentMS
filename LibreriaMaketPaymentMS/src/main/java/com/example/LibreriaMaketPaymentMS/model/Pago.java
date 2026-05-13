package com.example.LibreriaMaketPaymentMS.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pedidoId;

    private Long clienteId;

    private double monto;

    private LocalDateTime fechaPago;

    private String metodoPago;

    private String estadoPago;
}