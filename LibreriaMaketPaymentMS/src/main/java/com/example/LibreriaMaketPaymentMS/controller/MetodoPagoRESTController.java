package com.example.LibreriaMaketPaymentMS.controller;

import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoInputDTO;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoResponseDTO;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoUpdateDTO;
import com.example.LibreriaMaketPaymentMS.service.MetodoPagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Método de Pago", description = "Gestión de métodos de pago")
public class MetodoPagoRESTController {

    private static final Logger logger = LoggerFactory.getLogger(MetodoPagoRESTController.class.getName());

    @Autowired
    private MetodoPagoService metodoPagoService;

    // CREATE
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Método de pago creado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MetodoPagoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @Operation(
            summary = "Crear método de pago",
            description = "Registra un nuevo método de pago"
    )
    @PostMapping
    public ResponseEntity<MetodoPagoResponseDTO> save(@Valid @RequestBody MetodoPagoInputDTO dto) {

        String logMsgRequest = "Recibiendo solicitud para crear/guardar método de pago.";
        String logMsg = "Solicitud para crear/guardar método de pago.";

        logger.info(logMsgRequest);

        MetodoPagoResponseDTO created = metodoPagoService.save(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        logger.info(logMsg + "=> creado con ID: {}, Nombre: {}.",
                created.getId(),
                created.getNombre());

        return ResponseEntity.created(location).body(created);
    }

    // READ - LISTAR
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de métodos de pago",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = MetodoPagoResponseDTO.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No existen registros",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @Operation(
            summary = "Listar métodos de pago",
            description = "Obtiene todos los métodos de pago registrados"
    )
    @GetMapping
    public ResponseEntity<List<MetodoPagoResponseDTO>> findAll() {

        String logMsgRequest = "Recibiendo solicitud para buscar listado de métodos de pago.";
        String logMsg = "Solicitud para buscar listado de métodos de pago.";

        logger.info(logMsgRequest);

        List<MetodoPagoResponseDTO> listadoDTO = metodoPagoService.findAll();

        if (!listadoDTO.isEmpty()) {
            logger.info(logMsg + "=> encontrado(s) y enlistado(s).");
            return ResponseEntity.ok(listadoDTO);
        }

        logger.info(logMsg + "=> sin coincidencias (vacío).");
        return ResponseEntity.noContent().build();
    }

    // READ - EXISTE POR ID
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resultado de la validación"
            )
    })
    @Operation(
            summary = "Validar existencia",
            description = "Verifica si un método de pago existe según su ID"
    )
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

    // READ - BUSCAR POR ID
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Método de pago encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MetodoPagoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Método de pago no encontrado",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @Operation(
            summary = "Buscar por ID",
            description = "Obtiene un método de pago según su identificador"
    )
    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoResponseDTO> findById(@PathVariable Long id) {

        String logMsgRequest = "Recibiendo solicitud para buscar método de pago por ID: " + id + ".";
        String logMsg = "Solicitud para buscar método de pago por ID: " + id + ".";

        logger.info(logMsgRequest);

        MetodoPagoResponseDTO dto = metodoPagoService.findById(id);

        if (dto != null) {
            logger.info(logMsg + "=> encontrado.");
            return ResponseEntity.ok(dto);
        }

        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }

    // READ - BUSCAR POR NOMBRE
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Método de pago encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MetodoPagoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Método de pago no encontrado",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @Operation(
            summary = "Buscar por nombre",
            description = "Obtiene un método de pago según su nombre"
    )
    @GetMapping("/by-nombre/{nombre}")
    public ResponseEntity<MetodoPagoResponseDTO> findByNombre(@PathVariable String nombre) {

        String logMsgRequest = "Recibiendo solicitud para buscar método de pago por nombre: " + nombre + ".";
        String logMsg = "Solicitud para buscar método de pago por nombre: " + nombre + ".";

        logger.info(logMsgRequest);

        MetodoPagoResponseDTO dto = metodoPagoService.findByNombre(nombre);

        if (dto != null) {
            logger.info(logMsg + "=> encontrado con ID:{}.", dto.getId());
            return ResponseEntity.ok(dto);
        }

        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }

    // UPDATE
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Método de pago actualizado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MetodoPagoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Método de pago no encontrado",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @Operation(
            summary = "Actualizar método de pago",
            description = "Actualiza la información de un método de pago existente"
    )
    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoResponseDTO> update(
            @Valid @RequestBody MetodoPagoUpdateDTO dto,
            @PathVariable Long id) {

        String logMsgRequest = "Recibiendo solicitud para actualizar metodoPago con ID: " + id + ".";
        String logMsg = "Solicitud para actualizar metodoPago con ID: " + id + ".";

        logger.info(logMsgRequest);

        dto.setId(id);

        MetodoPagoResponseDTO updated = metodoPagoService.update(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updated.getId())
                .toUri();

        logger.info(logMsg + " => actualizado.");

        return ResponseEntity.status(200)
                .location(location)
                .body(updated);
    }

    // DELETE
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Método de pago eliminado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Método de pago no encontrado",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @Operation(
            summary = "Eliminar método de pago",
            description = "Elimina un método de pago según su ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {

        String logMsgRequest = "Recibiendo solicitud para borrar metodoPago con ID: " + id + ".";
        String logMsg = "Solicitud para borrar metodoPago con ID: " + id + ".";

        logger.info(logMsgRequest);

        if (metodoPagoService.deleteMetodoPagoById(id)) {
            logger.info(logMsg + " => encontrado y borrado.");
            return ResponseEntity.noContent().build();
        }

        logger.info(logMsg + " => no encontrado.");
        return ResponseEntity.notFound().build();
    }
}