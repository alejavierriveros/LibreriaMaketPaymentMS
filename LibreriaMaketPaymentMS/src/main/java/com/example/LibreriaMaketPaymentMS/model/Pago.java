package com.example.LibreriaMaketPaymentMS.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    private Double totalFinal;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "metodos_pago_id", nullable = false)
    private MetodoPago metodoPago;

    private LocalDateTime fechaPago;

    private Boolean revertido;

    @PrePersist
    protected void fechaOnCreate(){
        this.fechaPago = LocalDateTime.now();
    }
}