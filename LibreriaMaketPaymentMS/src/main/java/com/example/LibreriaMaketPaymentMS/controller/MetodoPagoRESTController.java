package com.example.LibreriaMaketPaymentMS.controller;

import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoInputDTO;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoResponseDTO;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoUpdateDTO;
import com.example.LibreriaMaketPaymentMS.service.MetodoPagoService;
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
@RequestMapping("/api/v1/metodos-de-pago")
public class MetodoPagoRESTController {

    private static final Logger logger = LoggerFactory.getLogger(MetodoPagoRESTController.class.getName());

    @Autowired
    private MetodoPagoService metodoPagoService;

    //CREATE:
    @PostMapping
    public ResponseEntity<MetodoPagoResponseDTO> save(@Valid @RequestBody MetodoPagoInputDTO dto){
        String logMsgRequest = "Recibiendo solicitud para crear/guardar método de pago.";
        String logMsg = "Solicitud para crear/guardar método de pago.";
        logger.info(logMsgRequest);
        MetodoPagoResponseDTO created = metodoPagoService.save(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        //de componentes de constructor URI // de la actual request //ruta de id // sacar la id del obj creado // transformar a URI.
        logger.info(logMsg + "=> creado con ID: {}, Nombre: {}.", created.getId(), created.getNombre());
        return ResponseEntity.created(location).body(created);
        //devuelve el estado y la locación //devuelve el objeto creado
    }


    //READ:
    @GetMapping
    public ResponseEntity<List<MetodoPagoResponseDTO>> findAll(){
        String logMsgRequest = "Recibiendo solicitud para buscar listado de métodos de pago.";
        String logMsg = "Solicitud para buscar listado de métodos de pago.";
        logger.info(logMsgRequest);
        List<MetodoPagoResponseDTO> listadoDTO = metodoPagoService.findAll();

        if (!listadoDTO.isEmpty()){
            logger.info(logMsg + "=> encontrado(s) y enlistado(s).");
            return ResponseEntity.ok(listadoDTO);
        }
        logger.info(logMsg + "=> sin coincidencias (vacío).");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists-by-id/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        String logMsgRequest = "Recibiendo solicitud para verificar existencia de método de pago con ID: " + id + ".";
        String logMsg = "Solicitud para verificar existencia de método de pago con ID: " + id + ".";
        logger.info(logMsgRequest);
        if (metodoPagoService.existsById(id)) {
            logger.info(logMsg + " => encontrado.");
            return ResponseEntity.ok(true);
        }
        logger.info(logMsg + " => no encontrado.");
        return ResponseEntity.ok(false);
    }




    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoResponseDTO> findById(@PathVariable Long id){
        String logMsgRequest = "Recibiendo solicitud para buscar método de pago por ID: " + id + ".";
        String logMsg = "Solicitud para buscar método de pago por ID: " + id + ".";
        logger.info(logMsgRequest);
        MetodoPagoResponseDTO dto = metodoPagoService.findById(id);
        if (dto != null){
            logger.info(logMsg + "=> encontrado.");
            return ResponseEntity.ok(dto);
        }
        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/by-nombre/{nombre}")
    public ResponseEntity<MetodoPagoResponseDTO> findByNombre(@PathVariable String nombre){
        String logMsgRequest = "Recibiendo solicitud para buscar método de pago por nombre: " + nombre + ".";
        String logMsg = "Solicitud para buscar método de pago por nombre: " + nombre + ".";
        logger.info(logMsgRequest);
        MetodoPagoResponseDTO dto = metodoPagoService.findByNombre(nombre);
        if (dto != null){
            logger.info(logMsg + "=> encontrado con ID:{}", dto.getId() + ".");
            return ResponseEntity.ok(dto);
        }
        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }






    //UPDATE:
    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoResponseDTO> update(@Valid @RequestBody MetodoPagoUpdateDTO dto, @PathVariable Long id){
        String logMsgRequest = "Recibiendo solicitud para actualizar metodoPago con ID: " + id + ".";
        String logMsg = "Solicitud para actualizar metodoPago con ID: " + id + ".";
        logger.info(logMsgRequest);
        dto.setId(id);
        MetodoPagoResponseDTO updated = metodoPagoService.update(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updated.getId()).toUri();
        //de componentes de constructor URI // de la actual request //ruta de id // sacar la id del obj creado // transformar a URI.
        logger.info(logMsg + " => actualizado.");
        return ResponseEntity.status(200).location(location).body(updated);
        //devuelve el estado y la locación //devuelve el objeto creado
    }


    //DELETE:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        String logMsgRequest = "Recibiendo solicitud para borrar metodoPago con ID: " + id + ".";
        String logMsg = "Solicitud para borrar metodoPago con ID: " + id + ".";
        logger.info(logMsgRequest);
        if(metodoPagoService.deleteMetodoPagoById(id)){
            logger.info(logMsg + " => encontrado y borrado.");
            return ResponseEntity.noContent().build();
        }
        logger.info(logMsg + " => no encontrado.");
        return ResponseEntity.notFound().build();
    }
    
}
