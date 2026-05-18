package com.example.LibreriaMaketPaymentMS.clients;

import com.example.LibreriaMaketPaymentMS.dto.VentaResponseDTO;
import com.example.LibreriaMaketPaymentMS.dto.VentaResponseForPaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "LMSellMS", path = "/api/v1/ventas")
public interface ToAPISellFeign {

    @GetMapping("/exists-by-id/{id}")
    Boolean existsById(@PathVariable Long id);

    @GetMapping("/{id}")
    VentaResponseDTO findById(@PathVariable Long id);

    @GetMapping("/-by-id-for-payment/{id}")
    VentaResponseForPaymentDTO findByIdForPayment(@PathVariable Long id);
}