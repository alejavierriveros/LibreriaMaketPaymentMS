package com.example.LibreriaMaketPaymentMS.mappers;

import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoUpdateDTO;
import com.example.LibreriaMaketPaymentMS.model.MetodoPago;
import org.springframework.stereotype.Component;

@Component
public class MetodoPagoUpdateMapper {

    public MetodoPago toEntity(MetodoPago ent, MetodoPagoUpdateDTO dto) {
        if  (dto == null) return null;

        ent.setNombre(dto.getNombre());
        ent.setRequiereApiExterna(dto.getRequiereApiExterna());
        ent.setServicioURL(dto.getServicioURL());
        ent.setDisponible(dto.getDisponible());

        return ent;
    }

}
