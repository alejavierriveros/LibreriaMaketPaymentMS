package com.example.LibreriaMaketPaymentMS.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.LibreriaMaketPaymentMS.controller.MetodoPagoRESTControllerV2;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component 
public class MetodoPagoMedelAssemblers implements RepresentationModelAssembler<MetodoPagoResponseDTO, EntityModel<MetodoPagoResponseDTO>> {

    @Override
    public EntityModel<MetodoPagoResponseDTO> toModel(MetodoPagoResponseDTO metodoPago) {
        return EntityModel.of(metodoPago,
                linkTo(methodOn(MetodoPagoRESTControllerV2.class).findById(metodoPago.getId())).withSelfRel(),
                linkTo(methodOn(MetodoPagoRESTControllerV2.class).findAll()).withRel("metodos-pago")
        );
    }

}