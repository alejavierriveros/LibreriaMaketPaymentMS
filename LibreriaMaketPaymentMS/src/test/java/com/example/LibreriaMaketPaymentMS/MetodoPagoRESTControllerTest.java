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
import com.example.LibreriaMaketPaymentMS.controller.MetodoPagoRESTController;
import com.example.LibreriaMaketPaymentMS.service.MetodoPagoService;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoInputDTO;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoResponseDTO;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoUpdateDTO;

@ExtendWith(MockitoExtension.class)
public class MetodoPagoRESTControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MetodoPagoService metodoPagoService;

    @InjectMocks
    private MetodoPagoRESTController metodoPagoRESTController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(metodoPagoRESTController).build();
    }

    // ==========================================
    // TEST POST: SAVE
    // ==========================================
    @Test
    void save_DebeRetornarStatus201YObjetoCreado() throws Exception {
        MetodoPagoInputDTO inputDTO = new MetodoPagoInputDTO();
        // Se usa "Tarjeta" (7 caracteres) para cumplir con el máximo de 10 permitido
        inputDTO.setNombre("Tarjeta");
        inputDTO.setRequiereApiExterna(true);

        MetodoPagoResponseDTO responseDTO = new MetodoPagoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNombre("Tarjeta");

        when(metodoPagoService.save(any(MetodoPagoInputDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/metodos-de-pago")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Tarjeta"));
    }

    // ==========================================
    // TESTS GET: FIND ALL
    // ==========================================
    @Test
    void findAll_CuandoExistenRegistros_DebeRetornarStatus200YLista() throws Exception {
        MetodoPagoResponseDTO dto = new MetodoPagoResponseDTO();
        dto.setId(1L);
        dto.setNombre("Efectivo");
        List<MetodoPagoResponseDTO> lista = List.of(dto);

        when(metodoPagoService.findAll()).thenReturn(lista);

        mockMvc.perform(get("/api/v1/metodos-de-pago"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void findAll_CuandoNoExistenRegistros_DebeRetornarStatus204() throws Exception {
        when(metodoPagoService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/metodos-de-pago"))
                .andExpect(status().isNoContent());
    }

    // ==========================================
    // TESTS GET: EXISTS BY ID
    // ==========================================
    @Test
    void existsById_CuandoExiste_DebeRetornarTrue() throws Exception {
        when(metodoPagoService.existsById(1L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/metodos-de-pago/exists-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void existsById_CuandoNoExiste_DebeRetornarFalse() throws Exception {
        when(metodoPagoService.existsById(1L)).thenReturn(false);

        mockMvc.perform(get("/api/v1/metodos-de-pago/exists-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    // ==========================================
    // TESTS GET: FIND BY ID
    // ==========================================
    @Test
    void findById_CuandoExiste_DebeRetornarStatus200() throws Exception {
        MetodoPagoResponseDTO responseDTO = new MetodoPagoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNombre("Transferencia");

        when(metodoPagoService.findById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/metodos-de-pago/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Transferencia"));
    }

    @Test
    void findById_CuandoNoExiste_DebeRetornarStatus404() throws Exception {
        when(metodoPagoService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/metodos-de-pago/1"))
                .andExpect(status().isNotFound());
    }

    // ==========================================
    // TESTS GET: FIND BY NOMBRE
    // ==========================================
    @Test
    void findByNombre_CuandoExiste_DebeRetornarStatus200() throws Exception {
        MetodoPagoResponseDTO responseDTO = new MetodoPagoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNombre("Paypal");

        when(metodoPagoService.findByNombre("Paypal")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/metodos-de-pago/by-nombre/Paypal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Paypal"));
    }

    @Test
    void findByNombre_CuandoNoExiste_DebeRetornarStatus404() throws Exception {
        when(metodoPagoService.findByNombre("Paypal")).thenReturn(null);

        mockMvc.perform(get("/api/v1/metodos-de-pago/by-nombre/Paypal"))
                .andExpect(status().isNotFound());
    }

    // ==========================================
    // TEST PUT: UPDATE
    // ==========================================
    @Test
    void update_DebeRetornarStatus200YObjetoActualizado() throws Exception {
        MetodoPagoUpdateDTO updateDTO = new MetodoPagoUpdateDTO();
        updateDTO.setId(1L);
        updateDTO.setNombre("Nuevo Nombre");

        MetodoPagoResponseDTO responseDTO = new MetodoPagoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNombre("Nuevo Nombre");

        when(metodoPagoService.update(any(MetodoPagoUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/metodos-de-pago/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Nuevo Nombre"));
    }

    // ==========================================
    // TESTS DELETE: DELETE BY ID
    // ==========================================
    @Test
    void deleteById_CuandoExiste_DebeRetornarStatus204() throws Exception {
        when(metodoPagoService.deleteMetodoPagoById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/metodos-de-pago/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_CuandoNoExiste_DebeRetornarStatus404() throws Exception {
        when(metodoPagoService.deleteMetodoPagoById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/metodos-de-pago/1"))
                .andExpect(status().isNotFound());
    }
}