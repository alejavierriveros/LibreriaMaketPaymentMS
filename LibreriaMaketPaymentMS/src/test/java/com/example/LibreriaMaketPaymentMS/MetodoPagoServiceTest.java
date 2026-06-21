package com.example.LibreriaMaketPaymentMS;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import com.example.LibreriaMaketPaymentMS.service.MetodoPagoService;

@ExtendWith(MockitoExtension.class)
class MetodoPagoServiceTest {

    @Mock
    private MetodoPagoRepository mpRepository;

    @Mock
    private MetodoPagoResponseMapper mpResponseMapper;

    @Mock
    private MetodoPagoInputMapper mpInputMapper;

    @Mock
    private MetodoPagoUpdateMapper mpUpdateMapper;

    @InjectMocks
    private MetodoPagoService metodoPagoService;

    // ==========================================
    // TESTS: SAVE()
    // ==========================================

    @Test
    void save_FlujoExitoso_ConApiExterna_DebeGuardarCorrectamente() {
        MetodoPagoInputDTO inputDTO = new MetodoPagoInputDTO("Paypal", true, "https://api.paypal.com");
        MetodoPago entidadMock = new MetodoPago();
        MetodoPagoResponseDTO expectedResponse = new MetodoPagoResponseDTO();

        when(mpRepository.existsByNombre("Paypal")).thenReturn(false);
        when(mpInputMapper.toEntity(inputDTO)).thenReturn(entidadMock);
        when(mpRepository.save(entidadMock)).thenReturn(entidadMock);
        when(mpResponseMapper.toDto(entidadMock)).thenReturn(expectedResponse);

        MetodoPagoResponseDTO actualResponse = metodoPagoService.save(inputDTO);

        assertNotNull(actualResponse);
        verify(mpRepository, times(1)).save(entidadMock);
    }

    @Test
    void save_FlujoExitoso_SinApiExterna_DebeAsignarNoneAUrlYGuardar() {
        MetodoPagoInputDTO inputDTO = new MetodoPagoInputDTO("Efectivo", false, null);
        MetodoPago entidadMock = new MetodoPago();
        MetodoPagoResponseDTO expectedResponse = new MetodoPagoResponseDTO();

        when(mpRepository.existsByNombre("Efectivo")).thenReturn(false);
        when(mpInputMapper.toEntity(inputDTO)).thenReturn(entidadMock);
        when(mpRepository.save(entidadMock)).thenReturn(entidadMock);
        when(mpResponseMapper.toDto(entidadMock)).thenReturn(expectedResponse);

        MetodoPagoResponseDTO actualResponse = metodoPagoService.save(inputDTO);

        assertEquals("NONE", inputDTO.getServicioURL()); 
        assertNotNull(actualResponse);
    }

    @Test
    void save_CuandoNombreYaExiste_DebeLanzarNombreExisteException() {
        MetodoPagoInputDTO inputDTO = new MetodoPagoInputDTO("Tarjeta", false, null);
        when(mpRepository.existsByNombre("Tarjeta")).thenReturn(true);

        assertThrows(NombreExisteException.class, () -> metodoPagoService.save(inputDTO));
        verify(mpRepository, never()).save(any());
    }

    @Test
    void save_RequiereApiExternaPeroUrlEsNula_DebeLanzarIllegalArgumentException() {
        MetodoPagoInputDTO inputDTO = new MetodoPagoInputDTO("Paypal", true, null);
        when(mpRepository.existsByNombre("Paypal")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> metodoPagoService.save(inputDTO));
        verify(mpRepository, never()).save(any());
    }

    @Test
    void save_RequiereApiExternaPeroUrlEstaVacia_DebeLanzarIllegalArgumentException() {
        MetodoPagoInputDTO inputDTO = new MetodoPagoInputDTO("Paypal", true, "");
        when(mpRepository.existsByNombre("Paypal")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> metodoPagoService.save(inputDTO));
        verify(mpRepository, never()).save(any());
    }

    // ==========================================
    // TESTS: READ METHODS
    // ==========================================

    @Test
    void findAll_DebeRetornarListaDeMetodosPago() {
        MetodoPago mp = new MetodoPago();
        MetodoPagoResponseDTO dto = new MetodoPagoResponseDTO();

        when(mpRepository.findAll()).thenReturn(List.of(mp));
        when(mpResponseMapper.toDto(mp)).thenReturn(dto);

        List<MetodoPagoResponseDTO> resultado = metodoPagoService.findAll();

        assertEquals(1, resultado.size());
    }

    @Test
    void findById_CuandoExiste_DebeRetornarDto() {
        MetodoPago mp = new MetodoPago();
        MetodoPagoResponseDTO dto = new MetodoPagoResponseDTO();

        when(mpRepository.findById(1L)).thenReturn(Optional.of(mp));
        when(mpResponseMapper.toDto(mp)).thenReturn(dto);

        MetodoPagoResponseDTO resultado = metodoPagoService.findById(1L);

        assertNotNull(resultado);
    }

    @Test
    void findById_CuandoNoExiste_DebeLanzarIdNoExisteException() {
        when(mpRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IdNoExisteException.class, () -> metodoPagoService.findById(1L));
    }

    @Test
    void existsById_DebeRetornarBooleano() {
        when(mpRepository.existsById(1L)).thenReturn(true);
        assertTrue(metodoPagoService.existsById(1L));
    }

    @Test
    void findByNombre_DebeRetornarMetodoPagoPorNombre() {
        MetodoPago mp = new MetodoPago();
        MetodoPagoResponseDTO dto = new MetodoPagoResponseDTO();

        when(mpRepository.findByNombre("Paypal")).thenReturn(mp);
        when(mpResponseMapper.toDto(mp)).thenReturn(dto);

        MetodoPagoResponseDTO resultado = metodoPagoService.findByNombre("Paypal");

        assertNotNull(resultado);
    }

    // ==========================================
    // TESTS: UPDATE()
    // ==========================================

    @Test
    void update_CuandoExiste_DebeActualizarYRetornarDto() {
        // Corrección: Instanciación limpia usando setters
        MetodoPagoUpdateDTO updateDTO = new MetodoPagoUpdateDTO();
        updateDTO.setId(1L);
        updateDTO.setNombre("NuevoNombre");

        MetodoPago entExistente = new MetodoPago();
        MetodoPago entModificado = new MetodoPago();
        MetodoPagoResponseDTO responseDTO = new MetodoPagoResponseDTO();

        when(mpRepository.findById(1L)).thenReturn(Optional.of(entExistente));
        when(mpUpdateMapper.toEntity(entExistente, updateDTO)).thenReturn(entModificado);
        when(mpRepository.save(entModificado)).thenReturn(entModificado);
        when(mpResponseMapper.toDto(entModificado)).thenReturn(responseDTO);

        MetodoPagoResponseDTO resultado = metodoPagoService.update(updateDTO);

        assertNotNull(resultado);
    }

    @Test
    void update_CuandoNoExiste_DebeLanzarIdNoExisteException() {
        // Corrección: Instanciación limpia usando setters
        MetodoPagoUpdateDTO updateDTO = new MetodoPagoUpdateDTO();
        updateDTO.setId(1L);
        updateDTO.setNombre("Nombre");

        when(mpRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IdNoExisteException.class, () -> metodoPagoService.update(updateDTO));
    }

    // ==========================================
    // TESTS: DELETE()
    // ==========================================

    @Test
    void deleteMetodoPagoById_CuandoExiste_DebeRetornarTrue() {
        when(mpRepository.existsById(1L)).thenReturn(true);
        doNothing().when(mpRepository).deleteById(1L);

        Boolean resultado = metodoPagoService.deleteMetodoPagoById(1L);

        assertTrue(resultado);
        verify(mpRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMetodoPagoById_CuandoNoExiste_DebeLanzarIdNoExisteException() {
        when(mpRepository.existsById(1L)).thenReturn(false);

        assertThrows(IdNoExisteException.class, () -> metodoPagoService.deleteMetodoPagoById(1L));
        verify(mpRepository, never()).deleteById(any());
    }
}