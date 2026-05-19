package com.example.LibreriaMaketPaymentMS.service;

import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoInputDTO;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoResponseDTO;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoUpdateDTO;
import com.example.LibreriaMaketPaymentMS.exceptions.IdNoExisteException;
import com.example.LibreriaMaketPaymentMS.exceptions.NombreExisteException;
import com.example.LibreriaMaketPaymentMS.mappers.MetodoPagoInputMapper;
import com.example.LibreriaMaketPaymentMS.mappers.MetodoPagoResponseMapper;
import com.example.LibreriaMaketPaymentMS.mappers.MetodoPagoUpdateMapper;
import com.example.LibreriaMaketPaymentMS.model.MetodoPago;
import com.example.LibreriaMaketPaymentMS.repository.MetodoPagoRepository;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MetodoPagoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetodoPagoService.class.getName());
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    MetodoPagoResponseMapper metodoPagoResponseMapper;

    @Autowired
    MetodoPagoInputMapper metodoPagoInputMapper;

    @Autowired
    MetodoPagoUpdateMapper metodoPagoUpdateMapper;

    //CREATE:
    @Transactional
    public MetodoPagoResponseDTO save(MetodoPagoInputDTO dto) {
        //Validaciones:
        //Duplicado de nombres de método de pago.
        if (metodoPagoRepository.existsByNombre(dto.getNombre())){
            throw new NombreExisteException("Nombre de método de pago ya existe.");
        }

        //Verificación de requerimiento de url para consumir servicio de pago.
        if (dto.getRequiereApiExterna() && (dto.getServicioURL() == null || dto.getServicioURL().isEmpty())) {

            throw new IllegalArgumentException("Debe ingresar una URL para consumir servicio de pago.");

        }

        if (!dto.getRequiereApiExterna()) {
            dto.setServicioURL("NONE");
        }

        return metodoPagoResponseMapper.toDto(metodoPagoRepository.save(metodoPagoInputMapper.toEntity(dto)));
    }

    //READ:
    @Transactional(readOnly = true)
    public List<MetodoPagoResponseDTO> findAll() {
        return metodoPagoRepository.findAll().stream().map(metodoPagoResponseMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public MetodoPagoResponseDTO findById(Long id) {
        return metodoPagoResponseMapper.toDto(metodoPagoRepository.findById(id).orElseThrow(() -> new IdNoExisteException("Id de método de pago no existe.")));
    }

    @Transactional(readOnly = true)
    public Boolean existsById(Long id) {
        return metodoPagoRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public MetodoPagoResponseDTO findByNombre(String nombre) {
        return metodoPagoResponseMapper.toDto(metodoPagoRepository.findByNombre(nombre));
    }

    //UPDATE:
    @Transactional
    public MetodoPagoResponseDTO update(MetodoPagoUpdateDTO dto) {

        //TODO: Se debe arregllar: expone a la entidad MetodoPago directamente en Service cuando se podría procesar en el mapper.
        MetodoPago ent = metodoPagoRepository.findById(dto.getId()).orElseThrow(() -> new IdNoExisteException("ID de metodoPago no existe."));
        return metodoPagoResponseMapper.toDto(metodoPagoRepository.save(metodoPagoUpdateMapper.toEntity(ent, dto)));
    }


    @Transactional
    public Boolean deleteMetodoPagoById(Long id) {
        if (!metodoPagoRepository.existsById(id)) {
            throw new IdNoExisteException("Id de método de pago no existe.");
        }
        metodoPagoRepository.deleteById(id);
        return true;

    }
}
