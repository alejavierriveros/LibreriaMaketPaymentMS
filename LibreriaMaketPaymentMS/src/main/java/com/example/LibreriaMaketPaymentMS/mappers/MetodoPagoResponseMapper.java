package com.example.LibreriaMaketPaymentMS.mappers;

import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoResponseDTO;
import com.example.LibreriaMaketPaymentMS.model.MetodoPago;
import org.springframework.stereotype.Component;

@Component
public class MetodoPagoResponseMapper {

    public MetodoPagoResponseDTO toDto(MetodoPago ent) {
        if  (ent == null) return null;
        return new MetodoPagoResponseDTO(
                ent.getId(),
                ent.getNombre(),
                ent.getRequiereApiExterna()
        );
    }
}
