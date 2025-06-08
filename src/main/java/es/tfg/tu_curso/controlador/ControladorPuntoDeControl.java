package es.tfg.tu_curso.controlador;

import es.tfg.tu_curso.dto.PuntoDeControlDTO;
import es.tfg.tu_curso.modelo.PuntoDeControl;
import es.tfg.tu_curso.servicio.interfaces.PuntoDeControlServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de los puntos de control de los cursos.
 * Proporciona endpoints para crear, modificar, eliminar y consultar puntos de control,
 * así como para marcar su estado de completado y realizar diversas consultas relacionadas.
 *
 */
@RestController
@RequestMapping("/puntos-de-control")
@Tag(name = "Punto de Control", description = "API para la gestión de puntos de control de los cursos")
public class ControladorPuntoDeControl {

    @Autowired
    private PuntoDeControlServicio puntoDeControlServicio;

    /**
     * Crea un nuevo punto de control asociado a un curso.
     *
     * @param puntoDeControl Objeto PuntoDeControl con los datos del nuevo punto de control
     * @param cursoId Identificador del curso al que se asociará el punto de control
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/crear")
    @Operation(summary = "Crear un nuevo punto de control", description = "Registra un nuevo punto de control asociado a un curso específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Punto de control creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o curso no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> crearPuntoDeControl(
            @Parameter(description = "Datos del punto de control a crear", required = true)
            @RequestBody PuntoDeControl puntoDeControl,
            @Parameter(description = "ID del curso al que se asociará el punto de control", required = true)
            @RequestParam Long cursoId) {
        if (puntoDeControlServicio.crear(puntoDeControl, cursoId)) {
            return new ResponseEntity<>("Punto de control creado exitosamente", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("No se pudo crear el punto de control", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Elimina un punto de control del sistema por su ID.
     *
     * @param id Identificador del punto de control a eliminar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un punto de control", description = "Elimina un punto de control existente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Punto de control eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Punto de control no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> borrarPuntoDeControl(
            @Parameter(description = "ID del punto de control a eliminar", required = true)
            @PathVariable Long id) {
        if (puntoDeControlServicio.borrar(id)) {
            return new ResponseEntity<>("Punto de control eliminado correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo eliminar el punto de control", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Modifica los datos de un punto de control existente.
     *
     * @param id Identificador del punto de control a modificar
     * @param puntoDeControl Objeto PuntoDeControl con los nuevos datos
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modificar un punto de control", description = "Actualiza los datos de un punto de control existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Punto de control modificado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para modificar el punto de control"),
            @ApiResponse(responseCode = "404", description = "Punto de control no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> modificarPuntoDeControl(
            @Parameter(description = "ID del punto de control a modificar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del punto de control", required = true)
            @RequestBody PuntoDeControl puntoDeControl) {
        if (puntoDeControlServicio.modificar(id, puntoDeControl)) {
            return new ResponseEntity<>("Punto de control modificado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo modificar el punto de control", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Actualiza el estado de completado de un punto de control.
     *
     * @param id Identificador del punto de control a actualizar
     * @param completado Nuevo estado de completado (true = completado, false = pendiente)
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PatchMapping("/{id}/completado")
    @Operation(summary = "Marcar estado de completado", description = "Actualiza el estado de completado de un punto de control")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo actualizar el estado"),
            @ApiResponse(responseCode = "404", description = "Punto de control no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> marcarCompletado(
            @Parameter(description = "ID del punto de control", required = true)
            @PathVariable Long id,
            @Parameter(description = "Estado de completado (true = completado, false = pendiente)", required = true)
            @RequestParam boolean completado) {
        if (puntoDeControlServicio.marcarCompletado(id, completado)) {
            String estado = completado ? "completado" : "pendiente";
            return new ResponseEntity<>("Punto de control marcado como " + estado + " exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo actualizar el estado del punto de control", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene la lista de todos los puntos de control registrados en el sistema.
     *
     * @return ResponseEntity con la lista de puntos de control en formato DTO
     */
    @GetMapping
    @Operation(summary = "Obtener todos los puntos de control", description = "Recupera la lista de todos los puntos de control registrados")
    @ApiResponse(responseCode = "200", description = "Lista de puntos de control recuperada correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PuntoDeControlDTO.class)))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<PuntoDeControlDTO>> obtenerPuntosDeControl() {
        return new ResponseEntity<>(puntoDeControlServicio.obtenerPuntosDeControl(), HttpStatus.OK);
    }

    /**
     * Obtiene un punto de control específico por su identificador.
     *
     * @param id Identificador del punto de control a buscar
     * @return ResponseEntity con el punto de control en formato DTO o NOT_FOUND si no existe
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener punto de control por ID", description = "Recupera un punto de control específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Punto de control encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PuntoDeControlDTO.class))),
            @ApiResponse(responseCode = "404", description = "Punto de control no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PuntoDeControlDTO> obtenerPuntoDeControlPorId(
            @Parameter(description = "ID del punto de control a buscar", required = true)
            @PathVariable Long id) {
        Optional<PuntoDeControlDTO> puntoDeControl = puntoDeControlServicio.obtenerPuntoDeControlPorId(id);
        return puntoDeControl.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Obtiene todos los puntos de control asociados a un curso específico.
     *
     * @param cursoId Identificador del curso
     * @return ResponseEntity con la lista de puntos de control del curso en formato DTO
     */
    @GetMapping("/curso/{cursoId}")
    @Operation(summary = "Obtener puntos de control por curso", description = "Recupera todos los puntos de control asociados a un curso específico")
    @ApiResponse(responseCode = "200", description = "Lista de puntos de control del curso recuperada correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PuntoDeControlDTO.class)))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<PuntoDeControlDTO>> obtenerPuntosDeControlPorCurso(
            @Parameter(description = "ID del curso", required = true)
            @PathVariable Long cursoId) {
        List<PuntoDeControlDTO> puntosDeControl = puntoDeControlServicio.obtenerPuntosDeControlPorCurso(cursoId);
        return new ResponseEntity<>(puntosDeControl, HttpStatus.OK);
    }

    /**
     * Obtiene todos los puntos de control pendientes (no completados).
     *
     * @return ResponseEntity con la lista de puntos de control pendientes en formato DTO
     */
    @GetMapping("/pendientes")
    @Operation(summary = "Obtener puntos de control pendientes", description = "Recupera todos los puntos de control que están pendientes (no completados)")
    @ApiResponse(responseCode = "200", description = "Lista de puntos de control pendientes recuperada correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PuntoDeControlDTO.class)))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<PuntoDeControlDTO>> obtenerPuntosDeControlPendientes() {
        List<PuntoDeControlDTO> puntosDeControl = puntoDeControlServicio.obtenerPuntosDeControlPendientes();
        return new ResponseEntity<>(puntosDeControl, HttpStatus.OK);
    }

    /**
     * Cuenta el número total de puntos de control asociados a un curso específico.
     *
     * @param cursoId Identificador del curso
     * @return ResponseEntity con el número total de puntos de control
     */
    @GetMapping("/contar/curso/{cursoId}")
    @Operation(summary = "Contar puntos de control por curso", description = "Obtiene el número total de puntos de control asociados a un curso específico")
    @ApiResponse(responseCode = "200", description = "Conteo realizado correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "integer", format = "int64")))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Long> contarPuntosDeControlPorCurso(
            @Parameter(description = "ID del curso", required = true)
            @PathVariable Long cursoId) {
        long count = puntoDeControlServicio.contarPuntosDeControlPorCurso(cursoId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    /**
     * Cuenta el número de puntos de control completados asociados a un curso específico.
     *
     * @param cursoId Identificador del curso
     * @return ResponseEntity con el número de puntos de control completados
     */
    @GetMapping("/contar/completados/curso/{cursoId}")
    @Operation(summary = "Contar puntos de control completados por curso", description = "Obtiene el número de puntos de control completados asociados a un curso específico")
    @ApiResponse(responseCode = "200", description = "Conteo realizado correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "integer", format = "int64")))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Long> contarPuntosDeControlCompletadosPorCurso(
            @Parameter(description = "ID del curso", required = true)
            @PathVariable Long cursoId) {
        long count = puntoDeControlServicio.contarPuntosDeControlCompletadosPorCurso(cursoId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}