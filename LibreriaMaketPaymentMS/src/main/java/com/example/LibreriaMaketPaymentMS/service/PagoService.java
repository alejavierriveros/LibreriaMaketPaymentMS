package com.example.LibreriaMaketPaymentMS.service;

import com.example.LibreriaMaketPaymentMS.clients.ToAPISellFeign;
import com.example.LibreriaMaketPaymentMS.dto.PagoInputDTO;
import com.example.LibreriaMaketPaymentMS.dto.PagoResponseDTO;
import com.example.LibreriaMaketPaymentMS.dto.PagoUpdateDTO;
import com.example.LibreriaMaketPaymentMS.dto.VentaResponseForPaymentDTO;
import com.example.LibreriaMaketPaymentMS.exceptions.BadRequestException;
import com.example.LibreriaMaketPaymentMS.exceptions.FailedAPICallResponseExeption;
import com.example.LibreriaMaketPaymentMS.exceptions.IdNoExisteException;
import com.example.LibreriaMaketPaymentMS.exceptions.ResourceNotFoundException;
import com.example.LibreriaMaketPaymentMS.mappers.PagoInputMapper;
import com.example.LibreriaMaketPaymentMS.mappers.PagoResponseMapper;
import com.example.LibreriaMaketPaymentMS.mappers.PagoUpdateMapper;
import com.example.LibreriaMaketPaymentMS.model.MetodoPago;
import com.example.LibreriaMaketPaymentMS.model.Pago;
import com.example.LibreriaMaketPaymentMS.repository.MetodoPagoRepository;
import com.example.LibreriaMaketPaymentMS.repository.PagoRepository;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoService {

    private static final Logger logger = LoggerFactory.getLogger(PagoService.class.getName());
    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private ToAPISellFeign toAPISellFeign;
    @Autowired
    private PagoResponseMapper pagoResponseMapper;
    @Autowired
    private PagoInputMapper pagoInputMapper;
    @Autowired
    private PagoUpdateMapper pagoUpdateMapper;


    public PagoResponseDTO save(PagoInputDTO dto) {


        // Solicitar detalles venta. Llamada a API REST LMSellMS:

        VentaResponseForPaymentDTO venta = null;
        try{
            venta = toAPISellFeign.findByIdForPayment(dto.getVentaId());

        } catch (FeignException.NotFound e){
            logger.info(e.getMessage());
            throw new IdNoExisteException(e.getMessage());
        } catch (Exception e){
            String errorMessage = "Error al intentar conectar con servicio al buscar la venta con id: " + dto.getVentaId() + ". Error: " + e.getMessage();
            logger.error(errorMessage);
            throw new FailedAPICallResponseExeption(errorMessage);
        }

        // Validar método de pago
        MetodoPago metodoPago = metodoPagoRepository.findById(dto.getMetodoPagoId()).orElseThrow(()->new IdNoExisteException("Metodo de pago no existe"));

        return pagoResponseMapper.toDto(pagoRepository.save(pagoInputMapper.toEntity(venta, metodoPago, dto)));
    }


    public List<PagoResponseDTO> findAll() {
        return pagoRepository.findAll().stream().map(pagoResponseMapper::toDto).toList();
    }


    public PagoResponseDTO findById(Long id) {
        return pagoResponseMapper.toDto(pagoRepository.findById(id)
                .orElseThrow(() ->
                        new IdNoExisteException("Id de Pago no encontrado.")));
    }

    public Boolean existsById(Long id) {
        return  pagoRepository.existsById(id);
    }

    public PagoResponseDTO update(PagoUpdateDTO dto) {
        Pago pago = pagoRepository.findById(dto.getId()).orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
        return pagoResponseMapper.toDto(pagoRepository.save(pagoUpdateMapper.toEntity(pago, dto)));
    }

    public Boolean deletePagoById(Long id) {

        if (!pagoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pago no encontrado");
        }

        pagoRepository.deleteById(id);
        return true;
    }

}