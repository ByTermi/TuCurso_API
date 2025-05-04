package es.tfg.tu_curso.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.tfg.tu_curso.dto.PuntoDeControlDTO;
import es.tfg.tu_curso.modelo.Curso;
import es.tfg.tu_curso.modelo.PuntoDeControl;
import es.tfg.tu_curso.servicio.interfaces.PuntoDeControlServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ControladorPuntoDeControlTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PuntoDeControlServicio puntoDeControlServicio;

    @Autowired
    private ObjectMapper objectMapper;

    private PuntoDeControl puntoDeControl;
    private PuntoDeControlDTO puntoDeControlDTO;
    private Curso curso;
    private List<PuntoDeControlDTO> listPuntosDeControlDTO;

    @BeforeEach
    void setUp() {
        // Configurar curso de prueba
        curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso de Java");
        curso.setEnlace("https://example.com/curso-java");
        curso.setPrecio(19.99);
        curso.setFinalizado(false);
        curso.setAnotaciones("Notas del curso");

        // Configurar punto de control de prueba
        puntoDeControl = new PuntoDeControl();
        puntoDeControl.setId(1L);
        puntoDeControl.setDescripcion("Completar módulo de introducción");
        puntoDeControl.setFechaFinalizacionDeseada(new Date());
        puntoDeControl.setEstaCompletado(false);
        puntoDeControl.setCurso(curso);

        // Configurar DTO de punto de control
        puntoDeControlDTO = new PuntoDeControlDTO(puntoDeControl);

        // Configurar lista de DTOs de puntos de control
        listPuntosDeControlDTO = Arrays.asList(puntoDeControlDTO);
    }

    @Test
    void crearPuntoDeControlExitoso() throws Exception {
        when(puntoDeControlServicio.crear(any(PuntoDeControl.class), anyLong())).thenReturn(true);

        String requestContent = objectMapper.writeValueAsString(puntoDeControl);
        System.out.println("TEST crearPuntoDeControlExitoso - Request enviada: " + requestContent);

        String response = mockMvc.perform(post("/puntos-de-control/crear")
                        .param("cursoId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isCreated())
                .andExpect(content().string("Punto de control creado exitosamente"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST crearPuntoDeControlExitoso - Response recibida: " + response);
    }

    @Test
    void crearPuntoDeControlFallido() throws Exception {
        when(puntoDeControlServicio.crear(any(PuntoDeControl.class), anyLong())).thenReturn(false);

        String requestContent = objectMapper.writeValueAsString(puntoDeControl);
        System.out.println("TEST crearPuntoDeControlFallido - Request enviada: " + requestContent);

        String response = mockMvc.perform(post("/puntos-de-control/crear")
                        .param("cursoId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo crear el punto de control"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST crearPuntoDeControlFallido - Response recibida: " + response);
    }

    @Test
    void borrarPuntoDeControlExitoso() throws Exception {
        when(puntoDeControlServicio.borrar(anyLong())).thenReturn(true);

        System.out.println("TEST borrarPuntoDeControlExitoso - Request enviada a: /puntos-de-control/1");

        String response = mockMvc.perform(delete("/puntos-de-control/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Punto de control eliminado correctamente"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST borrarPuntoDeControlExitoso - Response recibida: " + response);
    }

    @Test
    void borrarPuntoDeControlFallido() throws Exception {
        when(puntoDeControlServicio.borrar(anyLong())).thenReturn(false);

        System.out.println("TEST borrarPuntoDeControlFallido - Request enviada a: /puntos-de-control/1");

        String response = mockMvc.perform(delete("/puntos-de-control/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se pudo eliminar el punto de control"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST borrarPuntoDeControlFallido - Response recibida: " + response);
    }

    @Test
    void modificarPuntoDeControlExitoso() throws Exception {
        when(puntoDeControlServicio.modificar(anyLong(), any(PuntoDeControl.class))).thenReturn(true);

        String requestContent = objectMapper.writeValueAsString(puntoDeControl);
        System.out.println("TEST modificarPuntoDeControlExitoso - Request enviada a: /puntos-de-control/1");
        System.out.println("TEST modificarPuntoDeControlExitoso - Contenido: " + requestContent);

        String response = mockMvc.perform(patch("/puntos-de-control/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Punto de control modificado exitosamente"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST modificarPuntoDeControlExitoso - Response recibida: " + response);
    }

    @Test
    void modificarPuntoDeControlFallido() throws Exception {
        when(puntoDeControlServicio.modificar(anyLong(), any(PuntoDeControl.class))).thenReturn(false);

        String requestContent = objectMapper.writeValueAsString(puntoDeControl);
        System.out.println("TEST modificarPuntoDeControlFallido - Request enviada a: /puntos-de-control/1");
        System.out.println("TEST modificarPuntoDeControlFallido - Contenido: " + requestContent);

        String response = mockMvc.perform(patch("/puntos-de-control/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo modificar el punto de control"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST modificarPuntoDeControlFallido - Response recibida: " + response);
    }

    @Test
    void marcarCompletadoExitoso() throws Exception {
        when(puntoDeControlServicio.marcarCompletado(anyLong(), anyBoolean())).thenReturn(true);

        System.out.println("TEST marcarCompletadoExitoso - Request enviada a: /puntos-de-control/1/completado?completado=true");

        String response = mockMvc.perform(patch("/puntos-de-control/1/completado")
                        .param("completado", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string("Punto de control marcado como completado exitosamente"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST marcarCompletadoExitoso - Response recibida: " + response);
    }

    @Test
    void marcarNoCompletadoExitoso() throws Exception {
        when(puntoDeControlServicio.marcarCompletado(anyLong(), anyBoolean())).thenReturn(true);

        System.out.println("TEST marcarNoCompletadoExitoso - Request enviada a: /puntos-de-control/1/completado?completado=false");

        String response = mockMvc.perform(patch("/puntos-de-control/1/completado")
                        .param("completado", "false"))
                .andExpect(status().isOk())
                .andExpect(content().string("Punto de control marcado como pendiente exitosamente"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST marcarNoCompletadoExitoso - Response recibida: " + response);
    }

    @Test
    void marcarCompletadoFallido() throws Exception {
        when(puntoDeControlServicio.marcarCompletado(anyLong(), anyBoolean())).thenReturn(false);

        System.out.println("TEST marcarCompletadoFallido - Request enviada a: /puntos-de-control/1/completado?completado=true");

        String response = mockMvc.perform(patch("/puntos-de-control/1/completado")
                        .param("completado", "true"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo actualizar el estado del punto de control"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST marcarCompletadoFallido - Response recibida: " + response);
    }

    @Test
    void obtenerPuntosDeControl() throws Exception {
        when(puntoDeControlServicio.obtenerPuntosDeControl()).thenReturn(listPuntosDeControlDTO);

        System.out.println("TEST obtenerPuntosDeControl - Request enviada a: /puntos-de-control");

        String response = mockMvc.perform(get("/puntos-de-control"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descripcion").value("Completar módulo de introducción"))
                .andExpect(jsonPath("$[0].estaCompletado").value(false))
                .andExpect(jsonPath("$[0].cursoId").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerPuntosDeControl - Response recibida: " + response);
    }

    @Test
    void obtenerPuntoDeControlPorIdExitoso() throws Exception {
        when(puntoDeControlServicio.obtenerPuntoDeControlPorId(anyLong())).thenReturn(Optional.of(puntoDeControlDTO));

        System.out.println("TEST obtenerPuntoDeControlPorIdExitoso - Request enviada a: /puntos-de-control/1");

        String response = mockMvc.perform(get("/puntos-de-control/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descripcion").value("Completar módulo de introducción"))
                .andExpect(jsonPath("$.estaCompletado").value(false))
                .andExpect(jsonPath("$.cursoId").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerPuntoDeControlPorIdExitoso - Response recibida: " + response);
    }

    @Test
    void obtenerPuntoDeControlPorIdNoEncontrado() throws Exception {
        when(puntoDeControlServicio.obtenerPuntoDeControlPorId(anyLong())).thenReturn(Optional.empty());

        System.out.println("TEST obtenerPuntoDeControlPorIdNoEncontrado - Request enviada a: /puntos-de-control/1");

        String response = mockMvc.perform(get("/puntos-de-control/1"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerPuntoDeControlPorIdNoEncontrado - Response recibida: " +
                (response.isEmpty() ? "[Respuesta vacía]" : response));
    }

    @Test
    void obtenerPuntosDeControlPorCurso() throws Exception {
        when(puntoDeControlServicio.obtenerPuntosDeControlPorCurso(anyLong())).thenReturn(listPuntosDeControlDTO);

        System.out.println("TEST obtenerPuntosDeControlPorCurso - Request enviada a: /puntos-de-control/curso/1");

        String response = mockMvc.perform(get("/puntos-de-control/curso/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descripcion").value("Completar módulo de introducción"))
                .andExpect(jsonPath("$[0].estaCompletado").value(false))
                .andExpect(jsonPath("$[0].cursoId").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerPuntosDeControlPorCurso - Response recibida: " + response);
    }

    @Test
    void obtenerPuntosDeControlPendientes() throws Exception {
        when(puntoDeControlServicio.obtenerPuntosDeControlPendientes()).thenReturn(listPuntosDeControlDTO);

        System.out.println("TEST obtenerPuntosDeControlPendientes - Request enviada a: /puntos-de-control/pendientes");

        String response = mockMvc.perform(get("/puntos-de-control/pendientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descripcion").value("Completar módulo de introducción"))
                .andExpect(jsonPath("$[0].estaCompletado").value(false))
                .andExpect(jsonPath("$[0].cursoId").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerPuntosDeControlPendientes - Response recibida: " + response);
    }

    @Test
    void contarPuntosDeControlPorCurso() throws Exception {
        when(puntoDeControlServicio.contarPuntosDeControlPorCurso(anyLong())).thenReturn(3L);

        System.out.println("TEST contarPuntosDeControlPorCurso - Request enviada a: /puntos-de-control/contar/curso/1");

        String response = mockMvc.perform(get("/puntos-de-control/contar/curso/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST contarPuntosDeControlPorCurso - Response recibida: " + response);
    }

    @Test
    void contarPuntosDeControlCompletadosPorCurso() throws Exception {
        when(puntoDeControlServicio.contarPuntosDeControlCompletadosPorCurso(anyLong())).thenReturn(2L);

        System.out.println("TEST contarPuntosDeControlCompletadosPorCurso - Request enviada a: /puntos-de-control/contar/completados/curso/1");

        String response = mockMvc.perform(get("/puntos-de-control/contar/completados/curso/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST contarPuntosDeControlCompletadosPorCurso - Response recibida: " + response);
    }
}