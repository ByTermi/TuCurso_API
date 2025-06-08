package es.tfg.tu_curso.controlador;

import es.tfg.tu_curso.seguridad.JwtUtil;
import es.tfg.tu_curso.seguridad.LoginRequest;
import es.tfg.tu_curso.servicio.interfaces.CursoServicio;
import es.tfg.tu_curso.servicio.interfaces.UsuarioServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@Tag(name = "Administración", description = "API para la gestión del panel de administración")
public class ControladorAdmin {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private CursoServicio cursoServicio;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Muestra la página de inicio de sesión para administradores
     *
     * @return Vista del formulario de login
     */
    @GetMapping("/login")
    @Operation(summary = "Mostrar login admin", description = "Muestra el formulario de inicio de sesión para administradores")
    @ApiResponse(responseCode = "200", description = "Página de login mostrada correctamente")
    public String mostrarLogin() {
        return "admin/login";
    }

    /**
     * Muestra el panel principal de administración con estadísticas
     *
     * @param model Modelo para pasar datos a la vista
     * @return Vista del dashboard con estadísticas
     */
    @GetMapping("/dashboard")
    @Operation(summary = "Panel de administración", description = "Muestra el dashboard con estadísticas generales del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard mostrado correctamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public String mostrarDashboard(Model model) {
        model.addAttribute("totalUsuarios", usuarioServicio.contarUsuarios());
        model.addAttribute("totalCursos", cursoServicio.contarCursos());
        return "admin/dashboard";
    }

    /**
     * Muestra la lista de usuarios del sistema
     *
     * @param model Modelo para pasar datos a la vista
     * @return Vista con la lista de usuarios
     */
    @GetMapping("/usuarios")
    @Operation(summary = "Listar usuarios", description = "Muestra la lista completa de usuarios del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios mostrada correctamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioServicio.obtenerUsuarios());
        return "admin/usuarios";
    }

    /**
     * Muestra el formulario para cambiar la contraseña de un usuario
     *
     * @param id ID del usuario a modificar
     * @param model Modelo para pasar datos a la vista
     * @return Vista del formulario de cambio de contraseña
     */
    @GetMapping("/usuario/{id}/password")
    @Operation(summary = "Formulario cambio contraseña",
            description = "Muestra el formulario para cambiar la contraseña de un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formulario mostrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public String mostrarCambioPassword(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            Model model) {
        model.addAttribute("usuarioId", id);
        return "admin/cambiar-password";
    }

    /**
     * Procesa la eliminación de un usuario
     *
     * @param id ID del usuario a eliminar
     * @return ResponseEntity con el resultado de la operación
     */
    @PostMapping("/usuarios/{id}/eliminar")
    @ResponseBody
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioServicio.borrar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar el usuario: " + e.getMessage()));
        }
    }

    /**
     * Procesa el cambio de contraseña de un usuario
     *
     * @param id ID del usuario
     * @param request Objeto con la nueva contraseña
     * @return ResponseEntity con el resultado de la operación
     */
    @PostMapping("/usuarios/{id}/cambiar-password")
    @ResponseBody
    @Operation(summary = "Cambiar contraseña", description = "Cambia la contraseña de un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña cambiada correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> cambiarContrasena(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String nuevaContrasena = request.get("nuevaContrasena");
            if (nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "La contraseña no puede estar vacía"));
            }

            usuarioServicio.cambiarContrasena(id, nuevaContrasena);
            return ResponseEntity.ok(Map.of("mensaje", "Contraseña cambiada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al cambiar la contraseña: " + e.getMessage()));
        }
    }

    /**
     * [DESARROLLO] Muestra el formulario para crear un administrador de prueba
     * ADVERTENCIA: Este endpoint debe ser eliminado antes del despliegue en producción.
     *
     * @return Vista del formulario de registro de admin
     */
    @GetMapping("/registro-admin-dev")
    @Operation(summary = "[DESARROLLO] Formulario creación admin",
            description = "Muestra el formulario para crear un administrador de prueba. SOLO DESARROLLO.")
    public String mostrarRegistroAdmin() {
        return "admin/registro-admin";
    }


}