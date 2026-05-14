package com.example.LibreriaMaketPaymentMS.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDTO {

    private Long clienteId;
    private List<Long> productosIds;
    private double total;
}