package es.tfg.tu_curso.controlador;

import es.tfg.tu_curso.dto.UsuarioDTO;
import es.tfg.tu_curso.modelo.Usuario;
import es.tfg.tu_curso.seguridad.JwtUtil;
import es.tfg.tu_curso.servicio.interfaces.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.*;

/**
 * Controlador REST para la gestión de usuarios.
 * Proporciona endpoints para crear, modificar, eliminar y consultar usuarios,
 * así como para manejar la autenticación mediante JWT y la gestión de amigos.
 *
 */
@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuario", description = "API para la gestión de usuarios y amigos")
public class ControladorUsuario {

    @Autowired
    private UsuarioServicio usuarioServicio;

    // Dependencias security
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param usuario Objeto Usuario con los datos del nuevo usuario
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/crear")
    @Operation(summary = "Crear un nuevo usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para crear usuario")
    })
    public ResponseEntity<String> crearUsuario(
            @Parameter(description = "Datos del usuario a crear", required = true)
            @RequestBody Usuario usuario) {
        if (usuarioServicio.crear(usuario.getNombre(), usuario.getEmail(), usuario.getPass(), usuario.getDescripcion(), usuario.getIcono())) {
            return new ResponseEntity<>("Usuario creado exitosamente", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("No se pudo crear el usuario", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Crea un nuevo usuario administrador en el sistema.
     *
     * @param usuario Objeto Usuario con los datos del nuevo administrador
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/crear-admin")
    @Operation(summary = "Crear un nuevo administrador",
            description = "Registra un nuevo usuario con rol de administrador en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrador creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para crear administrador"),
            @ApiResponse(responseCode = "403", description = "No autorizado para crear administradores")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")  // Solo otros admins pueden crear admins
    public ResponseEntity<String> crearAdministrador(
            @Parameter(description = "Datos del administrador a crear", required = true)
            @RequestBody Usuario usuario) {
        try {
            // Asumiendo que has agregado este método en el servicio
            if (usuarioServicio.crearAdministrador(
                    usuario.getNombre(),
                    usuario.getEmail(),
                    usuario.getPass(),
                    usuario.getDescripcion(),
                    usuario.getIcono())) {
                return new ResponseEntity<>("Administrador creado exitosamente", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("No se pudo crear el administrador", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear el administrador: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * [ENDPOINT TEMPORAL - SOLO PARA DESARROLLO]
     * Crea un administrador sin requerir autenticación.
     * ADVERTENCIA: Este endpoint debe ser eliminado antes del despliegue en producción.
     *
     * @param usuario Datos del administrador a crear
     * @return ResponseEntity con el resultado de la operación
     */
    @PostMapping("/crear-admin-dev")
    @Operation(summary = "[DESARROLLO] Crear administrador sin autenticación",
            description = "ADVERTENCIA: Endpoint temporal solo para desarrollo. " +
                    "Permite crear un administrador sin autenticación. " +
                    "Este endpoint será eliminado antes del despliegue final.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrador creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<String> crearAdminDesarrollo(
            @Parameter(description = "Datos del administrador a crear", required = true)
            @RequestBody Usuario usuario) {
        try {
            boolean creado = usuarioServicio.crearAdministrador(
                    usuario.getNombre(),
                    usuario.getEmail(),
                    usuario.getPass(),
                    usuario.getDescripcion(),
                    usuario.getIcono()
            );

            if (creado) {
                return new ResponseEntity<>("Administrador creado con éxito", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("No se pudo crear el administrador", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear el administrador: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }



    /**
     * Modifica los datos de un usuario existente.
     *
     * @param id Identificador del usuario a modificar
     * @param usuario Objeto Usuario con los nuevos datos
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modificar un usuario", description = "Actualiza los datos de un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario modificado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para modificar el usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> modificarUsuario(
            @Parameter(description = "ID del usuario a modificar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del usuario", required = true)
            @RequestBody Usuario usuario) {
        if (usuarioServicio.modificar(id, usuario)) {
            return new ResponseEntity<>("Usuario modificado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo modificar el usuario", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene la lista de todos los usuarios registrados en el sistema.
     *
     * @return ResponseEntity con la lista de usuarios en formato DTO
     */
    @GetMapping
    @Operation(summary = "Obtener usuarios", description = "Recupera la lista de todos los usuarios registrados")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioDTO.class)))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuarios() {
        return new ResponseEntity<>(usuarioServicio.obtenerUsuarios(), HttpStatus.OK);
    }

    /**
     * Autentica a un usuario y genera un token JWT.
     *
     * @param loginRequest Objeto Usuario con las credenciales de acceso
     * @return ResponseEntity con el token JWT y la fecha de expiración
     */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "object", example = "{\"id\":\"1\",\"token\":\"eyJhbGciOiJIUzI1NiJ9...\",\"expiration\":\"Fri Jun 24 15:30:45 GMT 2023\"}"))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    public ResponseEntity<Map<String, String>> login(
            @Parameter(description = "Credenciales del usuario", required = true)
            @RequestBody Usuario loginRequest) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        if (passwordEncoder.matches(loginRequest.getPass(), userDetails.getPassword())) {
            String token = jwtUtil.generateToken(loginRequest.getEmail());
            Date expiration = jwtUtil.getExpirationDateFromToken(token); // Método para obtener la fecha de expiración

            // Obtener el usuario desde el servicio
            Usuario usuario = usuarioServicio.obtenerPorEmail(loginRequest.getEmail());

            Map<String, String> response = new HashMap<>();
            response.put("id", usuario.getId().toString()); // Agregar el ID del usuario
            response.put("token", token);
            response.put("expiration", expiration.toString());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Credenciales inválidas"));
    }

    /**
     * Refresca un token JWT válido antes de que expire.
     *
     * @param token Token JWT actual en el encabezado de autorización
     * @return ResponseEntity con el nuevo token JWT
     */
    @PostMapping("/refresh-token")
    @Operation(summary = "Refrescar token", description = "Genera un nuevo token JWT a partir de uno válido antes de que expire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refrescado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiJ9..."))),
            @ApiResponse(responseCode = "401", description = "Token inválido o expirado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> refreshToken(
            @Parameter(description = "Token JWT actual", required = true)
            @RequestHeader("Authorization") String token) {
        String newToken = jwtUtil.refreshToken(token);
        return ResponseEntity.ok(newToken);
    }

    // ENDPOINTS PARA GESTIÓN DE AMIGOS

    /**
     * Agrega un amigo a la lista de amigos de un usuario.
     *
     * @param usuarioId ID del usuario que quiere agregar un amigo
     * @param amigoId ID del usuario que será agregado como amigo
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{usuarioId}/amigos/{amigoId}")
    @Operation(summary = "Agregar amigo", description = "Agrega un usuario a la lista de amigos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Amigo agregado exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo agregar el amigo"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> agregarAmigo(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long usuarioId,
            @Parameter(description = "ID del amigo a agregar", required = true)
            @PathVariable Long amigoId) {
        if (usuarioServicio.agregarAmigo(usuarioId, amigoId)) {
            return new ResponseEntity<>("Amigo agregado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo agregar el amigo", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Remueve un amigo de la lista de amigos de un usuario.
     *
     * @param usuarioId ID del usuario que quiere remover un amigo
     * @param amigoId ID del usuario que será removido de la lista de amigos
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @DeleteMapping("/{usuarioId}/amigos/{amigoId}")
    @Operation(summary = "Remover amigo", description = "Remueve un usuario de la lista de amigos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Amigo removido exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo remover el amigo"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> removerAmigo(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long usuarioId,
            @Parameter(description = "ID del amigo a remover", required = true)
            @PathVariable Long amigoId) {
        if (usuarioServicio.removerAmigo(usuarioId, amigoId)) {
            return new ResponseEntity<>("Amigo removido exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo remover el amigo", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene la lista de amigos de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return ResponseEntity con la lista de amigos en formato DTO
     */
    @GetMapping("/{usuarioId}/amigos")
    @Operation(summary = "Obtener amigos", description = "Recupera la lista de amigos de un usuario")
    @ApiResponse(responseCode = "200", description = "Lista de amigos recuperada correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioDTO.class)))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<UsuarioDTO>> obtenerAmigos(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long usuarioId) {
        return new ResponseEntity<>(usuarioServicio.obtenerAmigos(usuarioId), HttpStatus.OK);
    }

    /**
     * Busca usuarios para agregar como amigos.
     *
     * @param usuarioId ID del usuario actual
     * @param nombre Nombre a buscar
     * @return ResponseEntity con lista de usuarios que coinciden con la búsqueda
     */
    @GetMapping("/{usuarioId}/buscar-amigos")
    @Operation(summary = "Buscar usuarios para agregar", description = "Busca usuarios que no son amigos y coinciden con el nombre")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioDTO.class)))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<UsuarioDTO>> buscarUsuariosParaAgregar(
            @Parameter(description = "ID del usuario actual", required = true)
            @PathVariable Long usuarioId,
            @Parameter(description = "Nombre a buscar", required = true)
            @RequestParam String nombre) {
        return new ResponseEntity<>(usuarioServicio.buscarUsuariosParaAgregar(usuarioId, nombre), HttpStatus.OK);
    }

    /**
     * Verifica si dos usuarios son amigos.
     *
     * @param usuarioId ID del primer usuario
     * @param amigoId ID del segundo usuario
     * @return ResponseEntity con resultado de la verificación
     */
    @GetMapping("/{usuarioId}/amigos/{amigoId}/verificar")
    @Operation(summary = "Verificar amistad", description = "Verifica si dos usuarios son amigos")
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"sonAmigos\": true}")))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Map<String, Boolean>> verificarAmistad(
            @Parameter(description = "ID del primer usuario", required = true)
            @PathVariable Long usuarioId,
            @Parameter(description = "ID del segundo usuario", required = true)
            @PathVariable Long amigoId) {
        boolean sonAmigos = usuarioServicio.sonAmigos(usuarioId, amigoId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("sonAmigos", sonAmigos);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene el número de amigos de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return ResponseEntity con el conteo de amigos
     */
    @GetMapping("/{usuarioId}/amigos/contar")
    @Operation(summary = "Contar amigos", description = "Obtiene el número de amigos de un usuario")
    @ApiResponse(responseCode = "200", description = "Conteo realizado correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"cantidadAmigos\": 5}")))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Map<String, Long>> contarAmigos(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long usuarioId) {
        long cantidadAmigos = usuarioServicio.contarAmigos(usuarioId);
        Map<String, Long> response = new HashMap<>();
        response.put("cantidadAmigos", cantidadAmigos);
        return ResponseEntity.ok(response);
    }
}
