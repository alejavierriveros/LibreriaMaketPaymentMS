package com.example.LibreriaMaketPaymentMS.mappers;

import com.example.LibreriaMaketPaymentMS.dto.PagoInputDTO;
import com.example.LibreriaMaketPaymentMS.dto.VentaResponseDTO;
import com.example.LibreriaMaketPaymentMS.dto.VentaResponseForPaymentDTO;
import com.example.LibreriaMaketPaymentMS.model.MetodoPago;
import com.example.LibreriaMaketPaymentMS.model.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoInputMapper {

    public Pago toEntity(VentaResponseForPaymentDTO venta, MetodoPago metodoPago, PagoInputDTO dto) {
        if (dto == null) return null;

        Pago ent = new Pago();

        ent.setVentaId(venta.getId());
        ent.setClienteId(venta.getClienteId());
        ent.setTotalFinal(venta.getTotalFinal());
        ent.setMetodoPago(metodoPago);
        ent.setRevertido(false);

        return ent;
    }

}
