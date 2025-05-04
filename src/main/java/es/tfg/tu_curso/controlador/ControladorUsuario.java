package es.tfg.tu_curso.controlador;

import es.tfg.tu_curso.dto.UsuarioDTO;
import es.tfg.tu_curso.modelo.Usuario;
import es.tfg.tu_curso.seguridad.JwtUtil;
import es.tfg.tu_curso.servicio.interfaces.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/usuarios")
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



    @PostMapping("/crear")
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
        if (usuarioServicio.crear(usuario.getNombre(), usuario.getEmail(), usuario.getPass(), usuario.getDescripcion(), usuario.getIcono())) {
            return new ResponseEntity<>("Usuario creado exitosamente", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("No se pudo crear el usuario", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarUsuario(@PathVariable Long id) {
        if (usuarioServicio.borrar(id)) {
            return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo eliminar el usuario", HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> modificarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        if (usuarioServicio.modificar(id, usuario)) {
            return new ResponseEntity<>("Usuario modificado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo modificar el usuario", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuarios() {
        return new ResponseEntity<>(usuarioServicio.obtenerUsuarios(), HttpStatus.OK);
    }




    /**
     *
     *
     * Endpoints security
     *
     *
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Usuario loginRequest) {
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

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String token) {
        String newToken = jwtUtil.refreshToken(token);
        return ResponseEntity.ok(newToken);
    }

}

