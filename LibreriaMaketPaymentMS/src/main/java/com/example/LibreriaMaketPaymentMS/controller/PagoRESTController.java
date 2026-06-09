package com.example.LibreriaMaketPaymentMS.controller;

import com.example.LibreriaMaketPaymentMS.dto.*;
import com.example.LibreriaMaketPaymentMS.service.*;

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
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pago", description = "Gestión de pagos")
public class PagoRESTController {

    private static final Logger logger = LoggerFactory.getLogger(PagoRESTController.class.getName());

    @Autowired
    private PagoService pagoService;

    // CREATE
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pago creado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @Operation(
            summary = "Crear pago",
            description = "Registra un nuevo pago en el sistema"
    )
    @PostMapping
    public ResponseEntity<PagoResponseDTO> save(@Valid @RequestBody PagoInputDTO dto) {

        String logMsgRequest = "Recibiendo solicitud para crear/guardar método de pago.";
        String logMsg = "Solicitud para crear/guardar método de pago.";

        logger.info(logMsgRequest);

        PagoResponseDTO created = pagoService.save(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        logger.info(logMsg + "=> creado con ID: {}, con Cliente ID: {}, con ID de Venta: {}.",
                created.getId(),
                created.getClienteId(),
                created.getVentaId());

        return ResponseEntity.created(location).body(created);
    }

    // READ - LISTAR
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de pagos encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = PagoResponseDTO.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No existen pagos registrados",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @Operation(
            summary = "Listar pagos",
            description = "Obtiene todos los pagos registrados"
    )
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

    // READ - EXISTS BY ID
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resultado de la validación"
            )
    })
    @Operation(
            summary = "Validar existencia",
            description = "Verifica si un pago existe según su ID"
    )
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

    // READ - BUSCAR POR ID
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pago no encontrado",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @Operation(
            summary = "Buscar pago por ID",
            description = "Obtiene un pago específico según su identificador"
    )
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

    // UPDATE
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pago no encontrado",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @Operation(
            summary = "Actualizar pago",
            description = "Actualiza la información de un pago existente"
    )
    @PutMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> update(
            @Valid @RequestBody PagoUpdateDTO dto,
            @PathVariable Long id) {

        String logMsgRequest = "Recibiendo solicitud para actualizar pago con ID: " + id + ".";
        String logMsg = "Solicitud para actualizar pago con ID: " + id + ".";

        logger.info(logMsgRequest);

        dto.setId(id);

        PagoResponseDTO updated = pagoService.update(dto);

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
                    description = "Pago eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pago no encontrado",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @Operation(
            summary = "Eliminar pago",
            description = "Elimina un pago según su ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {

        String logMsgRequest = "Recibiendo solicitud para borrar pago con ID: " + id + ".";
        String logMsg = "Solicitud para borrar pago con ID: " + id + ".";

        logger.info(logMsgRequest);

        if (pagoService.deletePagoById(id)) {
            logger.info(logMsg + " => encontrado y borrado.");
            return ResponseEntity.noContent().build();
        }

        logger.info(logMsg + " => no encontrado.");
        return ResponseEntity.notFound().build();
    }
}