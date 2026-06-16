package com.example.LibreriaMaketPaymentMS.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.example.LibreriaMaketPaymentMS.assemblers.MetodoPagoMedelAssemblers;
import com.example.LibreriaMaketPaymentMS.dto.*;
import com.example.LibreriaMaketPaymentMS.service.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/metodos-pago")
@Tag(name = "Método de Pago", description = "Gestión de métodos de pago")
public class MetodoPagoRESTControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(MetodoPagoRESTControllerV2.class.getName());

    @Autowired
    private MetodoPagoService metodoPagoService;

    @Autowired
    private MetodoPagoMedelAssemblers assembler;

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
    public ResponseEntity<EntityModel<MetodoPagoResponseDTO>> save(@Valid @RequestBody MetodoPagoInputDTO dto) {

        String logMsgRequest = "Recibiendo solicitud para crear/guardar método de pago.";
        String logMsg = "Solicitud para crear/guardar método de pago.";

        logger.info(logMsgRequest);

        MetodoPagoResponseDTO created = metodoPagoService.save(dto);

        // Transformamos el DTO usando el assembler
        EntityModel<MetodoPagoResponseDTO> entityModel = assembler.toModel(created);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        logger.info(logMsg + "=> creado con ID: {}, Nombre: {}.",
                created.getId(),
                created.getNombre());

        return ResponseEntity.created(location).body(entityModel);
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
    public ResponseEntity<CollectionModel<EntityModel<MetodoPagoResponseDTO>>> findAll() {

        String logMsgRequest = "Recibiendo solicitud para buscar listado de métodos de pago.";
        String logMsg = "Solicitud para buscar listado de métodos de pago.";

        logger.info(logMsgRequest);

        List<MetodoPagoResponseDTO> listadoDTO = metodoPagoService.findAll();

        if (!listadoDTO.isEmpty()) {
            // Mapeamos la lista interna a EntityModels usando tu assembler
            List<EntityModel<MetodoPagoResponseDTO>> metodosModel = listadoDTO.stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            // Envolvemos todo en un CollectionModel y agregamos el link 'self' de la colección
            CollectionModel<EntityModel<MetodoPagoResponseDTO>> collectionModel = CollectionModel.of(metodosModel,
                    linkTo(methodOn(MetodoPagoRESTControllerV2.class).findAll()).withSelfRel());

            logger.info(logMsg + "=> encontrado(s) y enlistado(s).");
            return ResponseEntity.ok(collectionModel);
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
    public ResponseEntity<EntityModel<MetodoPagoResponseDTO>> findById(@PathVariable Long id) {

        String logMsgRequest = "Recibiendo solicitud para buscar método de pago por ID: " + id + ".";
        String logMsg = "Solicitud para buscar método de pago por ID: " + id + ".";

        logger.info(logMsgRequest);

        MetodoPagoResponseDTO dto = metodoPagoService.findById(id);

        if (dto != null) {
            logger.info(logMsg + "=> encontrado.");
            // Generamos la respuesta con hipermedios mediante el assembler
            return ResponseEntity.ok(assembler.toModel(dto));
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
    public ResponseEntity<EntityModel<MetodoPagoResponseDTO>> findByNombre(@PathVariable String nombre) {

        String logMsgRequest = "Recibiendo solicitud para buscar método de pago por nombre: " + nombre + ".";
        String logMsg = "Solicitud para buscar método de pago por nombre: " + nombre + ".";

        logger.info(logMsgRequest);

        MetodoPagoResponseDTO dto = metodoPagoService.findByNombre(nombre);

        if (dto != null) {
            logger.info(logMsg + "=> encontrado con ID:{}.", dto.getId());
            // También añadimos hipermedios aquí usando el mismo formato estructurado
            return ResponseEntity.ok(assembler.toModel(dto));
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
    public ResponseEntity<EntityModel<MetodoPagoResponseDTO>> update(
            @Valid @RequestBody MetodoPagoUpdateDTO dto,
            @PathVariable Long id) {

        String logMsgRequest = "Recibiendo solicitud para actualizar metodoPago con ID: " + id + ".";
        String logMsg = "Solicitud para actualizar metodoPago con ID: " + id + ".";

        logger.info(logMsgRequest);

        dto.setId(id);

        MetodoPagoResponseDTO updated = metodoPagoService.update(dto);

        // Mapeamos al modelo hipermedia
        EntityModel<MetodoPagoResponseDTO> entityModel = assembler.toModel(updated);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updated.getId())
                .toUri();

        logger.info(logMsg + " => actualizado.");

        return ResponseEntity.status(200)
                .location(location)
                .body(entityModel);
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
