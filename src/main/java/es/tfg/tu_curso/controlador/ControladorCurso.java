package es.tfg.tu_curso.controlador;

import es.tfg.tu_curso.dto.CursoDTO;
import es.tfg.tu_curso.modelo.Curso;
import es.tfg.tu_curso.servicio.interfaces.CursoServicio;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar los cursos
 * Proporciona endpoints para crear, modificar, eliminar y consultar cursos
 */
@RestController
@RequestMapping("/cursos")
@Tag(name = "Cursos", description = "API para la gestión de cursos")
public class ControladorCurso {

    @Autowired
    private CursoServicio cursoServicio;

    /**
     * Crea un nuevo curso asociado a un usuario
     *
     * @param curso Datos del curso a crear
     * @param usuarioId ID del usuario propietario del curso
     * @return Mensaje de confirmación y código HTTP correspondiente
     */
    @PostMapping("/crear")
    @Operation(summary = "Crear nuevo curso", description = "Crea un nuevo curso y lo asocia al usuario especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo crear el curso", content = @Content)
    })
    public ResponseEntity<String> crearCurso(
            @Parameter(description = "Datos del curso a crear") @RequestBody Curso curso,
            @Parameter(description = "ID del usuario propietario") @RequestParam Long usuarioId) {

        if (cursoServicio.crear(curso, usuarioId)) {
            return new ResponseEntity<>("Curso creado exitosamente", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("No se pudo crear el curso", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Elimina un curso por su ID
     *
     * @param id ID del curso a eliminar
     * @return Mensaje de confirmación y código HTTP correspondiente
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar curso", description = "Elimina un curso según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el curso", content = @Content)
    })
    public ResponseEntity<String> borrarCurso(@Parameter(description = "ID del curso") @PathVariable Long id) {
        if (cursoServicio.borrar(id)) {
            return new ResponseEntity<>("Curso eliminado correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo eliminar el curso", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Modifica un curso existente
     *
     * @param id ID del curso a modificar
     * @param curso Datos actualizados del curso
     * @return Mensaje de confirmación y código HTTP correspondiente
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modificar curso", description = "Actualiza los datos de un curso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso modificado exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo modificar el curso", content = @Content)
    })
    public ResponseEntity<String> modificarCurso(
            @Parameter(description = "ID del curso") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del curso") @RequestBody Curso curso) {
        if (cursoServicio.modificar(id, curso)) {
            return new ResponseEntity<>("Curso modificado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo modificar el curso", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene todos los cursos disponibles
     *
     * @return Lista de cursos en formato DTO
     */
    @GetMapping
    @Operation(summary = "Obtener todos los cursos", description = "Retorna una lista con todos los cursos disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de cursos obtenida con éxito")
    public ResponseEntity<List<CursoDTO>> obtenerCursos() {
        return new ResponseEntity<>(cursoServicio.obtenerCursos(), HttpStatus.OK);
    }

    /**
     * Obtiene un curso por su ID
     *
     * @param id ID del curso a consultar
     * @return Curso encontrado o error 404 si no existe
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener curso por ID", description = "Retorna un curso según el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado",
                    content = @Content(schema = @Schema(implementation = CursoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado", content = @Content)
    })
    public ResponseEntity<CursoDTO> obtenerCursoPorId(@Parameter(description = "ID del curso") @PathVariable Long id) {
        Optional<CursoDTO> curso = cursoServicio.obtenerCursoPorId(id);
        return curso.map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Obtiene todos los cursos de un usuario específico
     *
     * @param usuarioId ID del usuario
     * @return Lista de cursos del usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener cursos por usuario", description = "Retorna todos los cursos asociados a un usuario")
    @ApiResponse(responseCode = "200", description = "Lista de cursos obtenida con éxito")
    public ResponseEntity<List<CursoDTO>> obtenerCursosPorUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        List<CursoDTO> cursos = cursoServicio.obtenerCursosPorUsuario(usuarioId);
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }

    /**
     * Cuenta el número de cursos de un usuario
     *
     * @param usuarioId ID del usuario
     * @return Número de cursos del usuario
     */
    @GetMapping("/contar/usuario/{usuarioId}")
    @Operation(summary = "Contar cursos por usuario", description = "Retorna el número total de cursos de un usuario")
    @ApiResponse(responseCode = "200", description = "Conteo exitoso")
    public ResponseEntity<Long> contarCursosPorUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        long count = cursoServicio.contarCursosPorUsuario(usuarioId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}