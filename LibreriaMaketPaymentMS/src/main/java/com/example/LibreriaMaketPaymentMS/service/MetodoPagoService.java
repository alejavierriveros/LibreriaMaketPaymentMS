package com.example.LibreriaMaketPaymentMS.service;

import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoInputDTO;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoResponseDTO;
import com.example.LibreriaMaketPaymentMS.mappers.MetodoPagoInputMapper;
import com.example.LibreriaMaketPaymentMS.mappers.MetodoPagoResponseMapper;
import com.example.LibreriaMaketPaymentMS.model.MetodoPago;
import com.example.LibreriaMaketPaymentMS.repository.MetodoPagoRepository;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    MetodoPagoResponseMapper metodoPagoResponseMapper;

    @Autowired
    MetodoPagoInputMapper metodoPagoInputMapper;

    @Transactional
    public MetodoPagoResponseDTO save(MetodoPagoInputDTO dto) {
        //Validaciones:
        String url = null;
        if (dto.getRequiereApiExterna()) {
            if (dto.getServicioURL() != null || !dto.getServicioURL().isEmpty()) {
                url = dto.getNombre();
            } else {
                throw new IllegalArgumentException("Debe ingresar una URL para consumir servicio de pago.");
            }
        } else {
            url = "NONE";
        }

        return metodoPagoResponseMapper.toDto(metodoPagoRepository.save(metodoPagoInputMapper.toEntity(url, dto)));


    }
}
