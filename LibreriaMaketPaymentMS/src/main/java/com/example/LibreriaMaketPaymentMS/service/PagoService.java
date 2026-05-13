package com.example.LibreriaMaketPaymentMS.service;

import com.example.LibreriaMaketPaymentMS.clients.ToAPIPedidoFeign;
import com.example.LibreriaMaketPaymentMS.dto.PagoDTO;
import com.example.LibreriaMaketPaymentMS.dto.PedidoResponseDTO;
import com.example.LibreriaMaketPaymentMS.exceptions.BadRequestException;
import com.example.LibreriaMaketPaymentMS.exceptions.ResourceNotFoundException;
import com.example.LibreriaMaketPaymentMS.model.Pago;
import com.example.LibreriaMaketPaymentMS.repository.PagoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository repo;

    @Autowired
    private ToAPIPedidoFeign pedidoFeign;


    public Pago crear(PagoDTO dto) {

        // Validar método de pago
        if (dto.getMetodoPago() == null || dto.getMetodoPago().isBlank()) {
            throw new BadRequestException("Método de pago obligatorio");
        }

        // Buscar pedido
        PedidoResponseDTO pedido = pedidoFeign.obtener(dto.getPedidoId());

        if (pedido == null) {
            throw new ResourceNotFoundException("Pedido no encontrado");
        }

        // Validar total
        if (pedido.getTotal() <= 0) {
            throw new BadRequestException("Monto inválido");
        }

        // Crear pago
        Pago pago = new Pago();

        pago.setPedidoId(dto.getPedidoId());
        pago.setClienteId(pedido.getClienteId());
        pago.setMonto(pedido.getTotal());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstadoPago("APROBADO");

        return repo.save(pago);
    }


    public List<Pago> listar() {
        return repo.findAll();
    }


    public Pago buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pago no encontrado"));
    }


    public void eliminar(Long id) {

        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Pago no encontrado");
        }

        repo.deleteById(id);
    }
}