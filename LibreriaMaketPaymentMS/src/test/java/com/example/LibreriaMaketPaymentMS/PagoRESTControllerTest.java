package com.example.LibreriaMaketPaymentMS;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

// Clases del Proyecto
import com.example.LibreriaMaketPaymentMS.controller.PagoRESTController;
import com.example.LibreriaMaketPaymentMS.service.PagoService;
import com.example.LibreriaMaketPaymentMS.dto.PagoInputDTO;
import com.example.LibreriaMaketPaymentMS.dto.PagoResponseDTO;
import com.example.LibreriaMaketPaymentMS.dto.PagoUpdateDTO;

@ExtendWith(MockitoExtension.class)
public class PagoRESTControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PagoService pagoService;

    @InjectMocks
    private PagoRESTController pagoRESTController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(pagoRESTController).build();
    }

    // ==========================================
    // TEST POST: SAVE
    // ==========================================
    @Test
    void save_DebeRetornarStatus201YObjetoCreado() throws Exception {
        PagoInputDTO inputDTO = new PagoInputDTO();
        // Seteamos el campo obligatorio para saltar la validación @Valid del controlador
        inputDTO.setMetodoPagoId(1L); 

        PagoResponseDTO responseDTO = new PagoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setClienteId(10L);
        responseDTO.setVentaId(20L);

        when(pagoService.save(any(PagoInputDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    // ==========================================
    // TESTS GET: FIND ALL
    // ==========================================
    @Test
    void findAll_CuandoExistenPagos_DebeRetornarStatus200YLista() throws Exception {
        PagoResponseDTO pago = new PagoResponseDTO();
        List<PagoResponseDTO> lista = List.of(pago);

        when(pagoService.findAll()).thenReturn(lista);

        mockMvc.perform(get("/api/v1/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void findAll_CuandoNoExistenPagos_DebeRetornarStatus204() throws Exception {
        when(pagoService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/pagos"))
                .andExpect(status().isNoContent());
    }

    // ==========================================
    // TESTS GET: EXISTS BY ID
    // ==========================================
    @Test
    void existsById_CuandoExiste_DebeRetornarTrue() throws Exception {
        when(pagoService.existsById(1L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/pagos/exists-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void existsById_CuandoNoExiste_DebeRetornarFalse() throws Exception {
        when(pagoService.existsById(1L)).thenReturn(false);

        mockMvc.perform(get("/api/v1/pagos/exists-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    // ==========================================
    // TESTS GET: FIND BY ID
    // ==========================================
    @Test
    void findById_CuandoExiste_DebeRetornarStatus200() throws Exception {
        PagoResponseDTO responseDTO = new PagoResponseDTO();
        responseDTO.setId(1L);

        when(pagoService.findById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void findById_CuandoNoExiste_DebeRetornarStatus404() throws Exception {
        when(pagoService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/pagos/1"))
                .andExpect(status().isNotFound());
    }

    // ==========================================
    // TEST PUT: UPDATE
    // ==========================================
    @Test
    void update_DebeRetornarStatus200YObjetoActualizado() throws Exception {
        PagoUpdateDTO updateDTO = new PagoUpdateDTO();
        PagoResponseDTO responseDTO = new PagoResponseDTO();
        responseDTO.setId(1L);

        when(pagoService.update(any(PagoUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/pagos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    // ==========================================
    // TESTS DELETE: DELETE BY ID
    // ==========================================
    @Test
    void deleteById_CuandoExiste_DebeRetornarStatus204() throws Exception {
        when(pagoService.deletePagoById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/pagos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_CuandoNoExiste_DebeRetornarStatus404() throws Exception {
        when(pagoService.deletePagoById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/pagos/1"))
                .andExpect(status().isNotFound());
    }
}