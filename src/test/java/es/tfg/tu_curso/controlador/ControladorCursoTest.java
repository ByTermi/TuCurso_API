package es.tfg.tu_curso.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.tfg.tu_curso.dto.CursoDTO;
import es.tfg.tu_curso.modelo.Curso;
import es.tfg.tu_curso.modelo.Usuario;
import es.tfg.tu_curso.servicio.interfaces.CursoServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
public class ControladorCursoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoServicio cursoServicio;

    @Autowired
    private ObjectMapper objectMapper;

    private Curso curso;
    private CursoDTO cursoDTO;
    private Usuario usuario;
    private List<CursoDTO> listCursosDTO;

    @BeforeEach
    void setUp() {
        // Configurar usuario de prueba
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Usuario Test");
        usuario.setEmail("test@example.com");
        usuario.setDescripcion("Descripción de prueba");
        usuario.setIcono("icono.png");

        // Configurar curso de prueba
        curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso de Java");
        curso.setEnlace("https://example.com/curso-java");
        curso.setPrecio(19.99);
        curso.setFinalizado(false);
        curso.setAnotaciones("Notas del curso");
        curso.setUsuario(usuario);

        // Configurar DTO de curso
        cursoDTO = new CursoDTO(curso);

        // Configurar lista de DTOs de cursos
        listCursosDTO = Arrays.asList(cursoDTO);
    }

    @Test
    void crearCursoExitoso() throws Exception {
        when(cursoServicio.crear(any(Curso.class), anyLong())).thenReturn(true);

        String requestContent = objectMapper.writeValueAsString(curso);
        System.out.println("TEST crearCursoExitoso - Request enviada: " + requestContent);

        String response = mockMvc.perform(post("/cursos/crear")
                        .param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isCreated())
                .andExpect(content().string("Curso creado exitosamente"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST crearCursoExitoso - Response recibida: " + response);
    }

    @Test
    void crearCursoFallido() throws Exception {
        when(cursoServicio.crear(any(Curso.class), anyLong())).thenReturn(false);

        String requestContent = objectMapper.writeValueAsString(curso);
        System.out.println("TEST crearCursoFallido - Request enviada: " + requestContent);

        String response = mockMvc.perform(post("/cursos/crear")
                        .param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo crear el curso"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST crearCursoFallido - Response recibida: " + response);
    }

    @Test
    void borrarCursoExitoso() throws Exception {
        when(cursoServicio.borrar(anyLong())).thenReturn(true);

        System.out.println("TEST borrarCursoExitoso - Request enviada a: /cursos/1");

        String response = mockMvc.perform(delete("/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Curso eliminado correctamente"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST borrarCursoExitoso - Response recibida: " + response);
    }

    @Test
    void borrarCursoFallido() throws Exception {
        when(cursoServicio.borrar(anyLong())).thenReturn(false);

        System.out.println("TEST borrarCursoFallido - Request enviada a: /cursos/1");

        String response = mockMvc.perform(delete("/cursos/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se pudo eliminar el curso"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST borrarCursoFallido - Response recibida: " + response);
    }

    @Test
    void modificarCursoExitoso() throws Exception {
        when(cursoServicio.modificar(anyLong(), any(Curso.class))).thenReturn(true);

        String requestContent = objectMapper.writeValueAsString(curso);
        System.out.println("TEST modificarCursoExitoso - Request enviada a: /cursos/1");
        System.out.println("TEST modificarCursoExitoso - Contenido: " + requestContent);

        String response = mockMvc.perform(patch("/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Curso modificado exitosamente"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST modificarCursoExitoso - Response recibida: " + response);
    }

    @Test
    void modificarCursoFallido() throws Exception {
        when(cursoServicio.modificar(anyLong(), any(Curso.class))).thenReturn(false);

        String requestContent = objectMapper.writeValueAsString(curso);
        System.out.println("TEST modificarCursoFallido - Request enviada a: /cursos/1");
        System.out.println("TEST modificarCursoFallido - Contenido: " + requestContent);

        String response = mockMvc.perform(patch("/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo modificar el curso"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST modificarCursoFallido - Response recibida: " + response);
    }

    @Test
    void obtenerCursos() throws Exception {
        when(cursoServicio.obtenerCursos()).thenReturn(listCursosDTO);

        System.out.println("TEST obtenerCursos - Request enviada a: /cursos");

        String response = mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Curso de Java"))
                .andExpect(jsonPath("$[0].enlace").value("https://example.com/curso-java"))
                .andExpect(jsonPath("$[0].precio").value(19.99))
                .andExpect(jsonPath("$[0].finalizado").value(false))
                .andExpect(jsonPath("$[0].anotaciones").value("Notas del curso"))
                .andExpect(jsonPath("$[0].usuarioId").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerCursos - Response recibida: " + response);
    }

    @Test
    void obtenerCursoPorIdExitoso() throws Exception {
        when(cursoServicio.obtenerCursoPorId(anyLong())).thenReturn(Optional.of(cursoDTO));

        System.out.println("TEST obtenerCursoPorIdExitoso - Request enviada a: /cursos/1");

        String response = mockMvc.perform(get("/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Curso de Java"))
                .andExpect(jsonPath("$.enlace").value("https://example.com/curso-java"))
                .andExpect(jsonPath("$.precio").value(19.99))
                .andExpect(jsonPath("$.finalizado").value(false))
                .andExpect(jsonPath("$.anotaciones").value("Notas del curso"))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerCursoPorIdExitoso - Response recibida: " + response);
    }

    @Test
    void obtenerCursoPorIdNoEncontrado() throws Exception {
        when(cursoServicio.obtenerCursoPorId(anyLong())).thenReturn(Optional.empty());

        System.out.println("TEST obtenerCursoPorIdNoEncontrado - Request enviada a: /cursos/1");

        String response = mockMvc.perform(get("/cursos/1"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerCursoPorIdNoEncontrado - Response recibida: " +
                (response.isEmpty() ? "[Respuesta vacía]" : response));
    }

    @Test
    void obtenerCursosPorUsuario() throws Exception {
        when(cursoServicio.obtenerCursosPorUsuario(anyLong())).thenReturn(listCursosDTO);

        System.out.println("TEST obtenerCursosPorUsuario - Request enviada a: /cursos/usuario/1");

        String response = mockMvc.perform(get("/cursos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Curso de Java"))
                .andExpect(jsonPath("$[0].enlace").value("https://example.com/curso-java"))
                .andExpect(jsonPath("$[0].precio").value(19.99))
                .andExpect(jsonPath("$[0].finalizado").value(false))
                .andExpect(jsonPath("$[0].anotaciones").value("Notas del curso"))
                .andExpect(jsonPath("$[0].usuarioId").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST obtenerCursosPorUsuario - Response recibida: " + response);
    }

    @Test
    void contarCursosPorUsuario() throws Exception {
        when(cursoServicio.contarCursosPorUsuario(anyLong())).thenReturn(3L);

        System.out.println("TEST contarCursosPorUsuario - Request enviada a: /cursos/contar/usuario/1");

        String response = mockMvc.perform(get("/cursos/contar/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"))
                .andReturn().getResponse().getContentAsString();

        System.out.println("TEST contarCursosPorUsuario - Response recibida: " + response);
    }
}