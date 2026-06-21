package com.example.LibreriaMaketPaymentMS;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.LibreriaMaketPaymentMS.clients.ToAPISellFeign;
import com.example.LibreriaMaketPaymentMS.dto.PagoInputDTO;
import com.example.LibreriaMaketPaymentMS.dto.PagoResponseDTO;
import com.example.LibreriaMaketPaymentMS.dto.PagoUpdateDTO;
import com.example.LibreriaMaketPaymentMS.dto.VentaResponseForPaymentDTO;
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
import com.example.LibreriaMaketPaymentMS.service.PagoService;

import feign.FeignException;
import feign.Request;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pRepository;

    @Mock
    private MetodoPagoRepository mpRepository;

    @Mock
    private ToAPISellFeign feignClient;

    @Mock
    private PagoResponseMapper pResponseMapper;

    @Mock
    private PagoInputMapper pInputMapper;

    @Mock
    private PagoUpdateMapper pUpdateMapper;

    @InjectMocks
    private PagoService pagoService;

    // ==========================================
    // TESTS: SAVE()
    // ==========================================

    @Test
    void save_FlujoExitoso_DebeRetornarPagoResponseDTO() {
        PagoInputDTO inputDTO = new PagoInputDTO(100L, 2L);
        VentaResponseForPaymentDTO ventaDTO = new VentaResponseForPaymentDTO();
        MetodoPago metodoPago = new MetodoPago();
        Pago pagoGuardado = new Pago();
        PagoResponseDTO expectedResponse = new PagoResponseDTO();

        when(feignClient.findByIdForPayment(100L)).thenReturn(ventaDTO);
        when(mpRepository.findById(2L)).thenReturn(Optional.of(metodoPago));
        when(pInputMapper.toEntity(ventaDTO, metodoPago, inputDTO)).thenReturn(pagoGuardado);
        when(pRepository.save(pagoGuardado)).thenReturn(pagoGuardado);
        when(pResponseMapper.toDto(pagoGuardado)).thenReturn(expectedResponse);

        PagoResponseDTO actualResponse = pagoService.save(inputDTO);

        assertNotNull(actualResponse);
        verify(pRepository, times(1)).save(pagoGuardado);
    }

    @Test
    void save_CuandoVentaNoExisteEnFeign_DebeLanzarIdNoExisteException() {
        PagoInputDTO inputDTO = new PagoInputDTO(100L, 2L);

        // Mockear la excepción FeignException.NotFound (404)
        Request request = Request.create(Request.HttpMethod.GET, "/api/ventas/100", Collections.emptyMap(), Request.Body.empty(), null);
        FeignException.NotFound feignException = new FeignException.NotFound("Venta no encontrada", request, null, null);

        when(feignClient.findByIdForPayment(100L)).thenThrow(feignException);

        assertThrows(IdNoExisteException.class, () -> pagoService.save(inputDTO));
        verify(pRepository, never()).save(any());
    }

    @Test
    void save_CuandoApiExternaFalla_DebeLanzarFailedAPICallResponseExeption() {
        PagoInputDTO inputDTO = new PagoInputDTO(100L, 2L);
        
        when(feignClient.findByIdForPayment(100L)).thenThrow(new RuntimeException("Timeout de red"));

        assertThrows(FailedAPICallResponseExeption.class, () -> pagoService.save(inputDTO));
        verify(pRepository, never()).save(any());
    }

    @Test
    void save_CuandoMetodoPagoNoExiste_DebeLanzarIdNoExisteException() {
        PagoInputDTO inputDTO = new PagoInputDTO(100L, 2L);
        VentaResponseForPaymentDTO ventaDTO = new VentaResponseForPaymentDTO();

        when(feignClient.findByIdForPayment(100L)).thenReturn(ventaDTO);
        when(mpRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IdNoExisteException.class, () -> pagoService.save(inputDTO));
        verify(pRepository, never()).save(any());
    }

    // ==========================================
    // TESTS: FINDALL()
    // ==========================================

    @Test
    void findAll_DebeRetornarListaDePagos() {
        Pago pago = new Pago();
        PagoResponseDTO dto = new PagoResponseDTO();

        when(pRepository.findAll()).thenReturn(List.of(pago));
        when(pResponseMapper.toDto(pago)).thenReturn(dto);

        List<PagoResponseDTO> resultado = pagoService.findAll();

        assertEquals(1, resultado.size());
    }

    // ==========================================
    // TESTS: FINDBYID()
    // ==========================================

    @Test
    void findById_CuandoExiste_DebeRetornarPago() {
        Pago pago = new Pago();
        PagoResponseDTO dto = new PagoResponseDTO();

        when(pRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pResponseMapper.toDto(pago)).thenReturn(dto);

        PagoResponseDTO resultado = pagoService.findById(1L);

        assertNotNull(resultado);
    }

    @Test
    void findById_CuandoNoExiste_DebeLanzarIdNoExisteException() {
        when(pRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IdNoExisteException.class, () -> pagoService.findById(1L));
    }

    // ==========================================
    // TESTS: EXISTSBYID()
    // ==========================================

    @Test
    void existsById_DebeRetornarBooleano() {
        when(pRepository.existsById(1L)).thenReturn(true);
        assertTrue(pagoService.existsById(1L));
    }

    // ==========================================
    // TESTS: UPDATE()
    // ==========================================

    @Test
    void update_CuandoPagoExiste_DebeRetornarPagoActualizado() {
        PagoUpdateDTO updateDTO = new PagoUpdateDTO(1L, true);
        Pago pagoExistente = new Pago();
        Pago pagoModificado = new Pago();
        PagoResponseDTO responseDTO = new PagoResponseDTO();

        when(pRepository.findById(1L)).thenReturn(Optional.of(pagoExistente));
        when(pUpdateMapper.toEntity(pagoExistente, updateDTO)).thenReturn(pagoModificado);
        when(pRepository.save(pagoModificado)).thenReturn(pagoModificado);
        when(pResponseMapper.toDto(pagoModificado)).thenReturn(responseDTO);

        PagoResponseDTO resultado = pagoService.update(updateDTO);

        assertNotNull(resultado);
    }

    @Test
    void update_CuandoPagoNoExiste_DebeLanzarResourceNotFoundException() {
        PagoUpdateDTO updateDTO = new PagoUpdateDTO(1L, true);
        when(pRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pagoService.update(updateDTO));
    }

    // ==========================================
    // TESTS: DELETEPAGOBYID()
    // ==========================================

    @Test
    void deletePagoById_CuandoExiste_DebeRetornarTrue() {
        when(pRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pRepository).deleteById(1L);

        Boolean resultado = pagoService.deletePagoById(1L);

        assertTrue(resultado);
        verify(pRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePagoById_CuandoNoExiste_DebeLanzarResourceNotFoundException() {
        when(pRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> pagoService.deletePagoById(1L));
        verify(pRepository, never()).deleteById(any());
    }
}