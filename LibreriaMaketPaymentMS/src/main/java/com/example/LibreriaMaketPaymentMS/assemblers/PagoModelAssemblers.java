package com.example.LibreriaMaketPaymentMS.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.LibreriaMaketPaymentMS.controller.PagoRESTControllerV2;
import com.example.LibreriaMaketPaymentMS.dto.PagoResponseDTO;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagoModelAssemblers implements RepresentationModelAssembler<PagoResponseDTO, EntityModel<PagoResponseDTO>> {

    @Override
    public EntityModel<PagoResponseDTO> toModel(PagoResponseDTO pago) {
        return EntityModel.of(pago,
                linkTo(methodOn(PagoRESTControllerV2.class).findById(pago.getId())).withSelfRel(),
                linkTo(methodOn(PagoRESTControllerV2.class).findAll()).withRel("pagos")
        );
    }
}