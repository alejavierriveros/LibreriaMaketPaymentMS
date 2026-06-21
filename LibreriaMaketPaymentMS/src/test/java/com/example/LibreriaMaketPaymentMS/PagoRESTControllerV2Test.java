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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

// Clases del Proyecto
import com.example.LibreriaMaketPaymentMS.controller.PagoRESTControllerV2;
import com.example.LibreriaMaketPaymentMS.service.PagoService;
import com.example.LibreriaMaketPaymentMS.assemblers.PagoModelAssemblers;
import com.example.LibreriaMaketPaymentMS.dto.PagoInputDTO;
import com.example.LibreriaMaketPaymentMS.dto.PagoResponseDTO;
import com.example.LibreriaMaketPaymentMS.dto.PagoUpdateDTO;

@ExtendWith(MockitoExtension.class)
public class PagoRESTControllerV2Test {

    private MockMvc mockMvc;

    @Mock
    private PagoService pagoService;

    @Mock
    private PagoModelAssemblers assembler;

    @InjectMocks
    private PagoRESTControllerV2 pagoRESTControllerV2;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper);

        this.mockMvc = MockMvcBuilders.standaloneSetup(pagoRESTControllerV2)
                .setMessageConverters(jsonConverter)
                .build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v2/pagos");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    // ==========================================
    // TESTS POST: SAVE
    // ==========================================
    @Test
    void save_DebeRetornarStatus201YObjetoConHipermedios() throws Exception {
        // Estructura REAL de PagoInputDTO: ventaId y metodoPagoId (este último @NotNull)
        PagoInputDTO inputDTO = new PagoInputDTO();
        inputDTO.setVentaId(20L);
        inputDTO.setMetodoPagoId(2L);

        PagoResponseDTO responseDTO = new PagoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setVentaId(20L);
        responseDTO.setClienteId(10L); // El response sí puede mapear más datos del negocio

        EntityModel<PagoResponseDTO> entityModel = EntityModel.of(responseDTO);
        entityModel.add(Link.of("http://localhost/api/v2/pagos/1").withSelfRel());

        when(pagoService.save(any(PagoInputDTO.class))).thenReturn(responseDTO);
        when(assembler.toModel(any(PagoResponseDTO.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.ventaId").value(20L))
                .andExpect(jsonPath("$.links").isArray());
    }

    // ==========================================
    // TESTS GET: FIND ALL
    // ==========================================
    @Test
    void findAll_CuandoExistenRegistros_DebeRetornarStatus200YCollectionModel() throws Exception {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setId(1L);

        when(pagoService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v2/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.links").isArray());
    }

    @Test
    void findAll_CuandoNoExistenRegistros_DebeRetornarStatus204() throws Exception {
        when(pagoService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v2/pagos"))
                .andExpect(status().isNoContent());
    }

    // ==========================================
    // TESTS GET: EXISTS BY ID
    // ==========================================
    @Test
    void existsById_CuandoExiste_DebeRetornarTrue() throws Exception {
        when(pagoService.existsById(1L)).thenReturn(true);

        mockMvc.perform(get("/api/v2/pagos/exists-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void existsById_CuandoNoExiste_DebeRetornarFalse() throws Exception {
        when(pagoService.existsById(1L)).thenReturn(false);

        mockMvc.perform(get("/api/v2/pagos/exists-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    // ==========================================
    // TESTS GET: FIND BY ID
    // ==========================================
    @Test
    void findById_CuandoExiste_DebeRetornarStatus200ConLinks() throws Exception {
        PagoResponseDTO responseDTO = new PagoResponseDTO();
        responseDTO.setId(1L);

        EntityModel<PagoResponseDTO> entityModel = EntityModel.of(responseDTO);
        entityModel.add(Link.of("http://localhost/api/v2/pagos/1").withSelfRel());

        when(pagoService.findById(1L)).thenReturn(responseDTO);
        when(assembler.toModel(responseDTO)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.links").isArray());
    }

    @Test
    void findById_CuandoNoExiste_DebeRetornarStatus404() throws Exception {
        when(pagoService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/v2/pagos/1"))
                .andExpect(status().isNotFound());
    }

    // ==========================================
    // TEST PUT: UPDATE
    // ==========================================
    @Test
    void update_DebeRetornarStatus200YObjetoActualizado() throws Exception {
        // Estructura REAL de PagoUpdateDTO: id y revertido
        PagoUpdateDTO updateDTO = new PagoUpdateDTO();
        updateDTO.setId(1L);
        updateDTO.setRevertido(true);

        PagoResponseDTO responseDTO = new PagoResponseDTO();
        responseDTO.setId(1L);

        EntityModel<PagoResponseDTO> entityModel = EntityModel.of(responseDTO);
        entityModel.add(Link.of("http://localhost/api/v2/pagos/1").withSelfRel());

        when(pagoService.update(any(PagoUpdateDTO.class))).thenReturn(responseDTO);
        when(assembler.toModel(any(PagoResponseDTO.class))).thenReturn(entityModel);

        mockMvc.perform(put("/api/v2/pagos/1")
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

        mockMvc.perform(delete("/api/v2/pagos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_CuandoNoExiste_DebeRetornarStatus404() throws Exception {
        when(pagoService.deletePagoById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/v2/pagos/1"))
                .andExpect(status().isNotFound());
    }
}