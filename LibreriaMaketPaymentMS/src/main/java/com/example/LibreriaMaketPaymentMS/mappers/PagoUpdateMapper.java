package com.example.LibreriaMaketPaymentMS.mappers;

import com.example.LibreriaMaketPaymentMS.dto.PagoUpdateDTO;
import com.example.LibreriaMaketPaymentMS.model.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoUpdateMapper {

    public Pago toEntity(Pago ent, PagoUpdateDTO dto) {
        ent.setRevertido(dto.getRevertido());

        return ent;
    }
}
