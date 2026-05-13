package com.example.LibreriaMaketPaymentMS.controller;

import com.example.LibreriaMaketPaymentMS.dto.*;
import com.example.LibreriaMaketPaymentMS.model.*;
import com.example.LibreriaMaketPaymentMS.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    @Autowired
    private PagoService service;

    @PostMapping
    public Pago crear(@RequestBody PagoDTO dto){
        return service.crear(dto);
    }

    @GetMapping
    public List<Pago> listar(){
        return service.listar();
    }

    @GetMapping("/{id}")
    public Pago obtener(@PathVariable Long id){
        return service.buscar(id);
    }
}
