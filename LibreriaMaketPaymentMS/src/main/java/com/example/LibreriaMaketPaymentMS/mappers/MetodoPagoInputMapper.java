package com.example.LibreriaMaketPaymentMS.mappers;

import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoInputDTO;
import com.example.LibreriaMaketPaymentMS.model.MetodoPago;
import org.springframework.stereotype.Component;

@Component
public class MetodoPagoInputMapper {

    public MetodoPago toEntity(String url, MetodoPagoInputDTO dto) {
        if  (dto == null) return null;

        MetodoPago ent = new MetodoPago();

        ent.setNombre(dto.getNombre());
        /* llevar a MetodoPagoservice

        */

        ent.setRequiereApiExterna(dto.getRequiereApiExterna());
        ent.setServicioURL(url);

        return ent;
    }
}
