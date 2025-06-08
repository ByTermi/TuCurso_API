package es.tfg.tu_curso.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.tfg.tu_curso.dto.UsuarioDTO;
import es.tfg.tu_curso.modelo.Usuario;
import es.tfg.tu_curso.seguridad.JwtUtil;
import es.tfg.tu_curso.servicio.interfaces.UsuarioServicio;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControladorUsuarioTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UsuarioServicio usuarioServicio;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String TOKEN_VALIDO = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    // SECCIÓN 1: ENDPOINTS PÚBLICOS (no requieren autenticación)

    @Test
    @Order(1)
    @DisplayName("1.1 - Crear usuario exitosamente")
    public void testCrearUsuario_Exitoso() throws Exception {
        // Crear un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPass("password123");
        usuario.setDescripcion("Usuario de prueba");
        usuario.setIcono("default.png");

        // Configurar el mock del servicio para devolver true (éxito)
        when(usuarioServicio.crear(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(true);

        // Realizar la solicitud POST y verificar
        mockMvc.perform(post("/usuarios/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuario creado exitosamente"));
    }

    @Test
    @Order(2)
    @DisplayName("1.2 - Crear usuario fallido")
    public void testCrearUsuario_Fallido() throws Exception {
        // Crear un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPass("password123");
        usuario.setDescripcion("Usuario de prueba");
        usuario.setIcono("default.png");

        // Configurar el mock del servicio para devolver false (fallo)
        when(usuarioServicio.crear(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(false);

        // Realizar la solicitud POST y verificar
        mockMvc.perform(post("/usuarios/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo crear el usuario"));
    }




    @Test
    @Order(4)
    @DisplayName("1.4 - Login fallido")
    public void testLogin_Fallido() throws Exception {
        // Preparar datos de prueba
        Usuario loginRequest = new Usuario();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPass("wrongpassword");

        // Configurar mocks
        UserDetails userDetails = User.withUsername("test@example.com")
                .password("encodedPassword123")
                .authorities(Collections.emptyList())
                .build();

        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        when(passwordEncoder.matches("wrongpassword", "encodedPassword123")).thenReturn(false);

        // Ejecutar y verificar
        mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Credenciales inválidas"));
    }

    // SECCIÓN 2: ENDPOINTS PROTEGIDOS (requieren autenticación)

    @Test
    @Order(5)
    @DisplayName("2.1 - Obtener usuarios con token válido")
    public void testObtenerUsuarios_ConTokenValido() throws Exception {
        // Preparar datos de prueba
        List<UsuarioDTO> usuariosEsperados = Arrays.asList(
                new UsuarioDTO(1L, "Usuario 1", "Descripción 1", "icono1.png"),
                new UsuarioDTO(2L, "Usuario 2", "Descripción 2", "icono2.png")
        );

        // Configurar mock
        when(usuarioServicio.obtenerUsuarios()).thenReturn(usuariosEsperados);

        // Configurar validación del token
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("test@example.com");

        UserDetails userDetails = User.withUsername("test@example.com")
                .password("encodedPassword123")
                .authorities(Collections.emptyList())
                .build();
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        // Ejecutar y verificar
        mockMvc.perform(get("/usuarios")
                        .header("Authorization", TOKEN_VALIDO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre").value("Usuario 1"))
                .andExpect(jsonPath("$[0].descripcion").value("Descripción 1"))
                .andExpect(jsonPath("$[0].icono").value("icono1.png"))
                .andExpect(jsonPath("$[1].nombre").value("Usuario 2"))
                .andExpect(jsonPath("$[1].descripcion").value("Descripción 2"))
                .andExpect(jsonPath("$[1].icono").value("icono2.png"));
    }







    @Test
    @Order(10)
    @DisplayName("2.6 - Modificar usuario con token válido")
    public void testModificarUsuario_ConTokenValido() throws Exception {
        // Crear datos de prueba
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Modificado");
        usuario.setEmail("modificado@example.com");
        usuario.setPass("nuevapass123");
        usuario.setDescripcion("Descripción modificada");
        usuario.setIcono("nuevo_icono.png");

        // Configurar mock
        when(usuarioServicio.modificar(eq(1L), any(Usuario.class))).thenReturn(true);

        // Configurar validación del token
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("test@example.com");

        UserDetails userDetails = User.withUsername("test@example.com")
                .password("encodedPassword123")
                .authorities(Collections.emptyList())
                .build();
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        // Ejecutar y verificar
        mockMvc.perform(patch("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario))
                        .header("Authorization", TOKEN_VALIDO))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario modificado exitosamente"));
    }

    @Test
    @Order(11)
    @DisplayName("2.7 - Modificar usuario que no existe")
    public void testModificarUsuario_NoExiste() throws Exception {
        // Crear datos de prueba
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Modificado");
        usuario.setEmail("modificado@example.com");
        usuario.setPass("nuevapass123");
        usuario.setDescripcion("Descripción modificada");
        usuario.setIcono("nuevo_icono.png");

        // Configurar mock
        when(usuarioServicio.modificar(eq(99L), any(Usuario.class))).thenReturn(false);

        // Configurar validación del token
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("test@example.com");

        UserDetails userDetails = User.withUsername("test@example.com")
                .password("encodedPassword123")
                .authorities(Collections.emptyList())
                .build();
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        // Ejecutar y verificar
        mockMvc.perform(patch("/usuarios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario))
                        .header("Authorization", TOKEN_VALIDO))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo modificar el usuario"));
    }



    @Test
    @Order(13)
    @DisplayName("2.9 - Refrescar token con token válido")
    public void testRefreshToken_ConTokenValido() throws Exception {
        // Configurar mock
        String nuevoToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...NUEVO";
        when(jwtUtil.refreshToken(TOKEN_VALIDO)).thenReturn(nuevoToken);

        // Configurar validación del token
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("test@example.com");

        UserDetails userDetails = User.withUsername("test@example.com")
                .password("encodedPassword123")
                .authorities(Collections.emptyList())
                .build();
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        // Ejecutar y verificar
        mockMvc.perform(post("/usuarios/refresh-token")
                        .header("Authorization", TOKEN_VALIDO))
                .andExpect(status().isOk())
                .andExpect(content().string(nuevoToken));
    }



    @Test
    @Order(15)
    @DisplayName("2.11 - Verificar que no se exponen contraseñas")
    public void testNoPasswordsExpuestos() throws Exception {
        // Preparar datos de prueba - Estos DTO no deberían contener el campo pass
        List<UsuarioDTO> usuariosEsperados = Arrays.asList(
                new UsuarioDTO(1L, "Usuario 1", "Descripción 1", "icono1.png"),
                new UsuarioDTO(2L, "Usuario 2", "Descripción 2", "icono2.png")
        );

        // Configurar mock
        when(usuarioServicio.obtenerUsuarios()).thenReturn(usuariosEsperados);

        // Configurar validación del token
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("test@example.com");

        UserDetails userDetails = User.withUsername("test@example.com")
                .password("encodedPassword123")
                .authorities(Collections.emptyList())
                .build();
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        // Ejecutar y verificar que la respuesta no contiene el campo "pass"
        mockMvc.perform(get("/usuarios")
                        .header("Authorization", TOKEN_VALIDO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pass").doesNotExist())
                .andExpect(jsonPath("$[1].pass").doesNotExist());
    }
}