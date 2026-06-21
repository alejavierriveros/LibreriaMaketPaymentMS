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
import org.springframework.hateoas.CollectionModel;
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
import com.example.LibreriaMaketPaymentMS.controller.MetodoPagoRESTControllerV2;
import com.example.LibreriaMaketPaymentMS.service.MetodoPagoService;
import com.example.LibreriaMaketPaymentMS.assemblers.MetodoPagoMedelAssemblers;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoInputDTO;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoResponseDTO;
import com.example.LibreriaMaketPaymentMS.dto.MetodoPagoUpdateDTO;

@ExtendWith(MockitoExtension.class)
public class MetodoPagoRESTControllerV2Test {

    private MockMvc mockMvc;

    @Mock
    private MetodoPagoService metodoPagoService;

    @Mock
    private MetodoPagoMedelAssemblers assembler;

    @InjectMocks
    private MetodoPagoRESTControllerV2 metodoPagoRESTControllerV2;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // En lugar de usar módulos HAL complejos que varían según la versión,
        // usamos un convertidor JSON estándar para standaloneSetup.
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper);

        this.mockMvc = MockMvcBuilders.standaloneSetup(metodoPagoRESTControllerV2)
                .setMessageConverters(jsonConverter) 
                .build();
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v2/metodos-pago");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    // ==========================================
    // TESTS POST: SAVE
    // ==========================================
    @Test
    void save_DebeRetornarStatus201YObjetoConHipermedios() throws Exception {
        MetodoPagoInputDTO inputDTO = new MetodoPagoInputDTO();
        inputDTO.setNombre("Tarjeta");
        inputDTO.setRequiereApiExterna(true);

        MetodoPagoResponseDTO responseDTO = new MetodoPagoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNombre("Tarjeta");

        EntityModel<MetodoPagoResponseDTO> entityModel = EntityModel.of(responseDTO);
        entityModel.add(Link.of("http://localhost/api/v2/metodos-pago/1").withSelfRel());

        when(metodoPagoService.save(any(MetodoPagoInputDTO.class))).thenReturn(responseDTO);
        when(assembler.toModel(any(MetodoPagoResponseDTO.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/metodos-pago")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Tarjeta"))
                .andExpect(jsonPath("$.links").isArray()); // Standalone mapea la propiedad como 'links'
    }

    // ==========================================
    // TESTS GET: FIND ALL
    // ==========================================
    @Test
    void findAll_CuandoExistenRegistros_DebeRetornarStatus200YCollectionModel() throws Exception {
        MetodoPagoResponseDTO dto = new MetodoPagoResponseDTO();
        dto.setId(1L);
        dto.setNombre("Efectivo");

        // Eliminado el stub del assembler que provocaba UnnecessaryStubbingException
        when(metodoPagoService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v2/metodos-pago"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray()) // Standalone agrupa la colección en 'content'
                .andExpect(jsonPath("$.links").isArray());
    }

    @Test
    void findAll_CuandoNoExistenRegistros_DebeRetornarStatus204() throws Exception {
        when(metodoPagoService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v2/metodos-pago"))
                .andExpect(status().isNoContent());
    }

    // ==========================================
    // TESTS GET: EXISTS BY ID
    // ==========================================
    @Test
    void existsById_CuandoExiste_DebeRetornarTrue() throws Exception {
        when(metodoPagoService.existsById(1L)).thenReturn(true);

        mockMvc.perform(get("/api/v2/metodos-pago/exists-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void existsById_CuandoNoExiste_DebeRetornarFalse() throws Exception {
        when(metodoPagoService.existsById(1L)).thenReturn(false);

        mockMvc.perform(get("/api/v2/metodos-pago/exists-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    // ==========================================
    // TESTS GET: FIND BY ID
    // ==========================================
    @Test
    void findById_CuandoExiste_DebeRetornarStatus200ConLinks() throws Exception {
        MetodoPagoResponseDTO responseDTO = new MetodoPagoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNombre("Transf");

        EntityModel<MetodoPagoResponseDTO> entityModel = EntityModel.of(responseDTO);
        entityModel.add(Link.of("http://localhost/api/v2/metodos-pago/1").withSelfRel());

        when(metodoPagoService.findById(1L)).thenReturn(responseDTO);
        when(assembler.toModel(responseDTO)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/metodos-pago/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.links").isArray());
    }

    @Test
    void findById_CuandoNoExiste_DebeRetornarStatus404() throws Exception {
        when(metodoPagoService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/v2/metodos-pago/1"))
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

        EntityModel<MetodoPagoResponseDTO> entityModel = EntityModel.of(responseDTO);
        entityModel.add(Link.of("http://localhost/api/v2/metodos-pago/1").withSelfRel());

        when(metodoPagoService.findByNombre("Paypal")).thenReturn(responseDTO);
        when(assembler.toModel(responseDTO)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/metodos-pago/by-nombre/Paypal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Paypal"));
    }

    @Test
    void findByNombre_CuandoNoExiste_DebeRetornarStatus404() throws Exception {
        when(metodoPagoService.findByNombre("Paypal")).thenReturn(null);

        mockMvc.perform(get("/api/v2/metodos-pago/by-nombre/Paypal"))
                .andExpect(status().isNotFound());
    }

    // ==========================================
    // TEST PUT: UPDATE
    // ==========================================
    @Test
    void update_DebeRetornarStatus200YObjetoActualizado() throws Exception {
        MetodoPagoUpdateDTO updateDTO = new MetodoPagoUpdateDTO();
        updateDTO.setId(1L);
        updateDTO.setNombre("NuevoNom");

        MetodoPagoResponseDTO responseDTO = new MetodoPagoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNombre("NuevoNom");

        EntityModel<MetodoPagoResponseDTO> entityModel = EntityModel.of(responseDTO);
        entityModel.add(Link.of("http://localhost/api/v2/metodos-pago/1").withSelfRel());

        when(metodoPagoService.update(any(MetodoPagoUpdateDTO.class))).thenReturn(responseDTO);
        when(assembler.toModel(any(MetodoPagoResponseDTO.class))).thenReturn(entityModel);

        mockMvc.perform(put("/api/v2/metodos-pago/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("NuevoNom"));
    }

    // ==========================================
    // TESTS DELETE: DELETE BY ID
    // ==========================================
    @Test
    void deleteById_CuandoExiste_DebeRetornarStatus204() throws Exception {
        when(metodoPagoService.deleteMetodoPagoById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v2/metodos-pago/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_CuandoNoExiste_DebeRetornarStatus404() throws Exception {
        when(metodoPagoService.deleteMetodoPagoById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/v2/metodos-pago/1"))
                .andExpect(status().isNotFound());
    }
}