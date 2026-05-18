package com.example.LibreriaMaketPaymentMS.mappers;

import com.example.LibreriaMaketPaymentMS.dto.PagoInputDTO;
import com.example.LibreriaMaketPaymentMS.model.MetodoPago;
import com.example.LibreriaMaketPaymentMS.model.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoInputMapper {

    public Pago toEntity(Long clienteId, Double totalFinal, MetodoPago metodoPago, PagoInputDTO dto) {
        if (dto == null) return null;

        Pago ent = new Pago();

        ent.setPedidoId(dto.getPedidoId());
        ent.setClienteId(clienteId);
        ent.setTotalFinal(totalFinal);
        ent.setMetodoPago(metodoPago);
        ent.setRevertido(false);

        return ent;
    }

}
