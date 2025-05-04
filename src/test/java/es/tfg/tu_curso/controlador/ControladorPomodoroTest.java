package es.tfg.tu_curso.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.tfg.tu_curso.dto.PomodoroDTO;
import es.tfg.tu_curso.modelo.Pomodoro;
import es.tfg.tu_curso.modelo.Usuario;
import es.tfg.tu_curso.servicio.interfaces.PomodoroServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControladorPomodoroTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PomodoroServicio pomodoroServicio;

    @Autowired
    private ObjectMapper objectMapper;

    private Pomodoro pomodoro;
    private PomodoroDTO pomodoroDTO;
    private Usuario usuario;
    private List<PomodoroDTO> listPomodorosDTO;
    private LocalDateTime fechaHoraInicial;
    private LocalDateTime fechaHoraDestino;

    @BeforeEach
    void setUp() {
        // Configurar fechas de prueba
        fechaHoraInicial = LocalDateTime.now();
        fechaHoraDestino = fechaHoraInicial.plusMinutes(25); // Típica duración de un pomodoro

        // Configurar usuario de prueba
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Usuario Test");
        usuario.setEmail("test@example.com");
        usuario.setDescripcion("Descripción de prueba");
        usuario.setIcono("icono.png");

        // Configurar pomodoro de prueba
        pomodoro = new Pomodoro();
        pomodoro.setId(1L);
        pomodoro.setFechaHoraInicial(fechaHoraInicial);
        pomodoro.setFechaHoraDestino(fechaHoraDestino);
        pomodoro.setUsuario(usuario);

        // Configurar DTO de pomodoro
        pomodoroDTO = new PomodoroDTO(pomodoro);

        // Configurar lista de DTOs de pomodoros
        listPomodorosDTO = Arrays.asList(pomodoroDTO);
    }

    @Test
    @Order(1)
    @DisplayName("1.1 - Crear pomodoro exitoso")
    void crearPomodoroExitoso() throws Exception {
        when(pomodoroServicio.crear(any(Pomodoro.class), anyLong())).thenReturn(true);

        String requestContent = objectMapper.writeValueAsString(pomodoro);
        System.out.println("TEST crearPomodoroExitoso - Request enviada: " + requestContent);

        String response = mockMvc.perform(post("/pomodoros/crear")
                        .param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isCreated())
                .andExpect(content().string("Pomodoro creado exitosamente"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST crearPomodoroExitoso - Response recibida: " + response);
    }

    @Test
    @Order(2)
    @DisplayName("1.2 - Crear pomodoro fallido")
    void crearPomodoroFallido() throws Exception {
        when(pomodoroServicio.crear(any(Pomodoro.class), anyLong())).thenReturn(false);

        String requestContent = objectMapper.writeValueAsString(pomodoro);
        System.out.println("TEST crearPomodoroFallido - Request enviada: " + requestContent);

        String response = mockMvc.perform(post("/pomodoros/crear")
                        .param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo crear el pomodoro"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST crearPomodoroFallido - Response recibida: " + response);
    }

    @Test
    @Order(3)
    @DisplayName("1.3 - Borrar pomodoro exitoso")
    void borrarPomodoroExitoso() throws Exception {
        when(pomodoroServicio.borrar(anyLong())).thenReturn(true);

        System.out.println("TEST borrarPomodoroExitoso - Request enviada a: /pomodoros/1");

        String response = mockMvc.perform(delete("/pomodoros/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Pomodoro eliminado correctamente"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST borrarPomodoroExitoso - Response recibida: " + response);
    }

    @Test
    @Order(4)
    @DisplayName("1.4 - Borrar pomodoro fallido")
    void borrarPomodoroFallido() throws Exception {
        when(pomodoroServicio.borrar(anyLong())).thenReturn(false);

        System.out.println("TEST borrarPomodoroFallido - Request enviada a: /pomodoros/99");

        String response = mockMvc.perform(delete("/pomodoros/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se pudo eliminar el pomodoro"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST borrarPomodoroFallido - Response recibida: " + response);
    }

    @Test
    @Order(5)
    @DisplayName("1.5 - Modificar pomodoro exitoso")
    void modificarPomodoroExitoso() throws Exception {
        when(pomodoroServicio.modificar(anyLong(), any(Pomodoro.class))).thenReturn(true);

        String requestContent = objectMapper.writeValueAsString(pomodoro);
        System.out.println("TEST modificarPomodoroExitoso - Request enviada a: /pomodoros/1");
        System.out.println("TEST modificarPomodoroExitoso - Contenido: " + requestContent);

        String response = mockMvc.perform(patch("/pomodoros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Pomodoro modificado exitosamente"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST modificarPomodoroExitoso - Response recibida: " + response);
    }

    @Test
    @Order(6)
    @DisplayName("1.6 - Modificar pomodoro fallido")
    void modificarPomodoroFallido() throws Exception {
        when(pomodoroServicio.modificar(anyLong(), any(Pomodoro.class))).thenReturn(false);

        String requestContent = objectMapper.writeValueAsString(pomodoro);
        System.out.println("TEST modificarPomodoroFallido - Request enviada a: /pomodoros/99");
        System.out.println("TEST modificarPomodoroFallido - Contenido: " + requestContent);

        String response = mockMvc.perform(patch("/pomodoros/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo modificar el pomodoro"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST modificarPomodoroFallido - Response recibida: " + response);
    }

    @Test
    @Order(7)
    @DisplayName("1.7 - Obtener todos los pomodoros")
    void obtenerPomodoros() throws Exception {
        when(pomodoroServicio.obtenerPomodoros()).thenReturn(listPomodorosDTO);

        System.out.println("TEST obtenerPomodoros - Request enviada a: /pomodoros");

        String response = mockMvc.perform(get("/pomodoros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fechaHoraInicial").exists())
                .andExpect(jsonPath("$[0].fechaHoraDestino").exists())
                .andExpect(jsonPath("$[0].usuarioId").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerPomodoros - Response recibida: " + response);
    }

    @Test
    @Order(8)
    @DisplayName("1.8 - Obtener pomodoro por ID exitoso")
    void obtenerPomodoroPorIdExitoso() throws Exception {
        when(pomodoroServicio.obtenerPomodoroPorId(anyLong())).thenReturn(Optional.of(pomodoroDTO));

        System.out.println("TEST obtenerPomodoroPorIdExitoso - Request enviada a: /pomodoros/1");

        String response = mockMvc.perform(get("/pomodoros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fechaHoraInicial").exists())
                .andExpect(jsonPath("$.fechaHoraDestino").exists())
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerPomodoroPorIdExitoso - Response recibida: " + response);
    }

    @Test
    @Order(9)
    @DisplayName("1.9 - Obtener pomodoro por ID no encontrado")
    void obtenerPomodoroPorIdNoEncontrado() throws Exception {
        when(pomodoroServicio.obtenerPomodoroPorId(anyLong())).thenReturn(Optional.empty());

        System.out.println("TEST obtenerPomodoroPorIdNoEncontrado - Request enviada a: /pomodoros/99");

        String response = mockMvc.perform(get("/pomodoros/99"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerPomodoroPorIdNoEncontrado - Response recibida: " +
                (response.isEmpty() ? "[Respuesta vacía]" : response));
    }

    @Test
    @Order(10)
    @DisplayName("1.10 - Obtener pomodoros por usuario")
    void obtenerPomodorosPorUsuario() throws Exception {
        when(pomodoroServicio.obtenerPomodorosPorUsuario(anyLong())).thenReturn(listPomodorosDTO);

        System.out.println("TEST obtenerPomodorosPorUsuario - Request enviada a: /pomodoros/usuario/1");

        String response = mockMvc.perform(get("/pomodoros/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fechaHoraInicial").exists())
                .andExpect(jsonPath("$[0].fechaHoraDestino").exists())
                .andExpect(jsonPath("$[0].usuarioId").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerPomodorosPorUsuario - Response recibida: " + response);
    }

    @Test
    @Order(11)
    @DisplayName("1.11 - Obtener pomodoros entre fechas")
    void obtenerPomodorosEntreFechas() throws Exception {
        when(pomodoroServicio.obtenerPomodorosEntreFechas(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(listPomodorosDTO);

        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now().plusDays(1);

        System.out.println("TEST obtenerPomodorosEntreFechas - Request enviada a: /pomodoros/entre-fechas");

        String response = mockMvc.perform(get("/pomodoros/entre-fechas")
                        .param("fechaInicio", inicio.toString())
                        .param("fechaFin", fin.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fechaHoraInicial").exists())
                .andExpect(jsonPath("$[0].fechaHoraDestino").exists())
                .andExpect(jsonPath("$[0].usuarioId").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerPomodorosEntreFechas - Response recibida: " + response);
    }

    @Test
    @Order(12)
    @DisplayName("1.12 - Contar pomodoros por usuario")
    void contarPomodorosPorUsuario() throws Exception {
        when(pomodoroServicio.contarPomodorosPorUsuario(anyLong())).thenReturn(5L);

        System.out.println("TEST contarPomodorosPorUsuario - Request enviada a: /pomodoros/contar/usuario/1");

        String response = mockMvc.perform(get("/pomodoros/contar/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST contarPomodorosPorUsuario - Response recibida: " + response);
    }
}