package com.example.LibreriaMaketPaymentMS.clients;

import com.example.LibreriaMaketPaymentMS.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "LMOrderMS", path = "/api/v1/pedidos")
public interface ToAPIPedidoFeign {

    @GetMapping("/{id}")
    PedidoResponseDTO obtener(@PathVariable Long id);
}