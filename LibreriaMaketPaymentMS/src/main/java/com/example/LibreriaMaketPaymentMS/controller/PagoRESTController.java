package com.example.LibreriaMaketPaymentMS.controller;

import com.example.LibreriaMaketPaymentMS.dto.*;
import com.example.LibreriaMaketPaymentMS.service.*;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoRESTController {

    private static final Logger logger = LoggerFactory.getLogger(PagoRESTController.class.getName());

    @Autowired
    private PagoService pagoService;

    //CREATE:
    @PostMapping
    public ResponseEntity<PagoResponseDTO> save(@Valid @RequestBody PagoInputDTO dto) {
        String logMsgRequest = "Recibiendo solicitud para crear/guardar método de pago.";
        String logMsg = "Solicitud para crear/guardar método de pago.";
        logger.info(logMsgRequest);
        PagoResponseDTO created = pagoService.save(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        //de componentes de constructor URI // de la actual request //ruta de id // sacar la id del obj creado // transformar a URI.
        logger.info(logMsg + "=> creado con ID: {}, con Cliente ID: {}, con ID de Venta: {}.", created.getId(), created.getClienteId(), created.getClienteId());
        return ResponseEntity.created(location).body(created);
        //devuelve el estado y la locación //devuelve el objeto creado
    }


    //READ:
    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> findAll() {
        String logMsgRequest = "Recibiendo solicitud para buscar listado de pagos.";
        String logMsg = "Solicitud para buscar listado de pagos.";
        logger.info(logMsgRequest);
        List<PagoResponseDTO> listadoDTO = pagoService.findAll();

        if (!listadoDTO.isEmpty()) {
            logger.info(logMsg + "=> encontrado(s) y enlistado(s).");
            return ResponseEntity.ok(listadoDTO);
        }
        logger.info(logMsg + "=> sin coincidencias (vacío).");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists-by-id/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        String logMsgRequest = "Recibiendo solicitud para verificar existencia de pago con ID: " + id + ".";
        String logMsg = "Solicitud para verificar existencia de pago con ID: " + id + ".";
        logger.info(logMsgRequest);
        if (pagoService.existsById(id)) {
            logger.info(logMsg + " => encontrado.");
            return ResponseEntity.ok(true);
        }
        logger.info(logMsg + " => no encontrado.");
        return ResponseEntity.ok(false);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> findById(@PathVariable Long id) {
        String logMsgRequest = "Recibiendo solicitud para buscar pago con ID: " + id + ".";
        String logMsg = "Solicitud para buscar pago con ID: " + id + ".";
        logger.info(logMsgRequest);
        PagoResponseDTO dto = pagoService.findById(id);
        if (dto != null) {
            logger.info(logMsg + "=> encontrado.");
            return ResponseEntity.ok(dto);
        }
        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }




    //UPDATE:
    @PutMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> update(@Valid @RequestBody PagoUpdateDTO dto, @PathVariable Long id){
        String logMsgRequest = "Recibiendo solicitud para actualizar pago con ID: " + id + ".";
        String logMsg = "Solicitud para actualizar pago con ID: " + id + ".";
        logger.info(logMsgRequest);
        dto.setId(id);
        PagoResponseDTO updated = pagoService.update(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updated.getId()).toUri();
        //de componentes de constructor URI // de la actual request //ruta de id // sacar la id del obj creado // transformar a URI.
        logger.info(logMsg + " => actualizado.");
        return ResponseEntity.status(200).location(location).body(updated);
        //devuelve el estado y la locación //devuelve el objeto creado
    }


    //DELETE:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        String logMsgRequest = "Recibiendo solicitud para borrar pago con ID: " + id + ".";
        String logMsg = "Solicitud para borrar pago con ID: " + id + ".";
        logger.info(logMsgRequest);
        if(pagoService.deletePagoById(id)){
            logger.info(logMsg + " => encontrado y borrado.");
            return ResponseEntity.noContent().build();
        }
        logger.info(logMsg + " => no encontrado.");
        return ResponseEntity.notFound().build();
    }
}
