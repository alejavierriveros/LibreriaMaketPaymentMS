package com.example.LibreriaMaketPaymentMS.mappers;

import com.example.LibreriaMaketPaymentMS.dto.PagoResponseDTO;
import com.example.LibreriaMaketPaymentMS.model.MetodoPago;
import com.example.LibreriaMaketPaymentMS.model.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoResponseMapper {

    public PagoResponseDTO toDto(Pago ent){

        if (ent == null) return null;

        return new PagoResponseDTO(
                ent.getId(),
                ent.getVentaId(),
                ent.getClienteId(),
                ent.getTotalFinal(),
                ent.getMetodoPago().getNombre(),
                ent.getFechaPago()
        );


    }
}
