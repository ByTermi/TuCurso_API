package es.tfg.tu_curso.controlador;

import es.tfg.tu_curso.dto.PomodoroDTO;
import es.tfg.tu_curso.modelo.Pomodoro;
import es.tfg.tu_curso.servicio.interfaces.PomodoroServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar los pomodoros
 * Proporciona endpoints para crear, modificar, eliminar y consultar pomodoros
 */
@RestController
@RequestMapping("/pomodoros")
@Tag(name = "Pomodoros", description = "API para la gestión de pomodoros")
public class ControladorPomodoro {

    @Autowired
    private PomodoroServicio pomodoroServicio;

    /**
     * Crea un nuevo pomodoro asociado a un usuario
     *
     * @param fechaHoraInicial Fecha y hora de inicio del pomodoro
     * @param fechaHoraDestino Fecha y hora de finalización del pomodoro
     * @param usuarioId ID del usuario propietario del pomodoro
     * @return Mensaje de confirmación y código HTTP correspondiente
     */
    @PostMapping("/crear")
    @Operation(summary = "Crear nuevo pomodoro", description = "Crea un nuevo pomodoro y lo asocia al usuario especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pomodoro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo crear el pomodoro", content = @Content)
    })
    public ResponseEntity<String> crearPomodoro(
            @Parameter(description = "Fecha y hora de inicio del pomodoro (formato ISO)", example = "2025-01-01T08:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHoraInicial,
            @Parameter(description = "Fecha y hora de finalización del pomodoro (formato ISO)", example = "2025-01-01T09:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHoraDestino,
            @Parameter(description = "ID del usuario propietario") @RequestParam Long usuarioId) {
        if (pomodoroServicio.crear(fechaHoraInicial, fechaHoraDestino, usuarioId)) {
            return new ResponseEntity<>("Pomodoro creado exitosamente", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("No se pudo crear el pomodoro", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Elimina un pomodoro por su ID
     *
     * @param id ID del pomodoro a eliminar
     * @return Mensaje de confirmación y código HTTP correspondiente
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pomodoro", description = "Elimina un pomodoro según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pomodoro eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el pomodoro", content = @Content)
    })
    public ResponseEntity<String> borrarPomodoro(@Parameter(description = "ID del pomodoro") @PathVariable Long id) {
        if (pomodoroServicio.borrar(id)) {
            return new ResponseEntity<>("Pomodoro eliminado correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo eliminar el pomodoro", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Modifica un pomodoro existente
     *
     * @param id ID del pomodoro a modificar
     * @param pomodoro Datos actualizados del pomodoro
     * @return Mensaje de confirmación y código HTTP correspondiente
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modificar pomodoro", description = "Actualiza los datos de un pomodoro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pomodoro modificado exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo modificar el pomodoro", content = @Content)
    })
    public ResponseEntity<String> modificarPomodoro(
            @Parameter(description = "ID del pomodoro") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del pomodoro") @RequestBody Pomodoro pomodoro) {
        if (pomodoroServicio.modificar(id, pomodoro)) {
            return new ResponseEntity<>("Pomodoro modificado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo modificar el pomodoro", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene todos los pomodoros disponibles
     *
     * @return Lista de pomodoros en formato DTO
     */
    @GetMapping
    @Operation(summary = "Obtener todos los pomodoros", description = "Retorna una lista con todos los pomodoros disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de pomodoros obtenida con éxito")
    public ResponseEntity<List<PomodoroDTO>> obtenerPomodoros() {
        return new ResponseEntity<>(pomodoroServicio.obtenerPomodoros(), HttpStatus.OK);
    }

    /**
     * Obtiene un pomodoro por su ID
     *
     * @param id ID del pomodoro a consultar
     * @return Pomodoro encontrado o error 404 si no existe
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener pomodoro por ID", description = "Retorna un pomodoro según el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pomodoro encontrado",
                    content = @Content(schema = @Schema(implementation = PomodoroDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pomodoro no encontrado", content = @Content)
    })
    public ResponseEntity<PomodoroDTO> obtenerPomodoroPorId(@Parameter(description = "ID del pomodoro") @PathVariable Long id) {
        Optional<PomodoroDTO> pomodoro = pomodoroServicio.obtenerPomodoroPorId(id);
        return pomodoro.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Obtiene todos los pomodoros de un usuario específico
     *
     * @param usuarioId ID del usuario
     * @return Lista de pomodoros del usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener pomodoros por usuario", description = "Retorna todos los pomodoros asociados a un usuario")
    @ApiResponse(responseCode = "200", description = "Lista de pomodoros obtenida con éxito")
    public ResponseEntity<List<PomodoroDTO>> obtenerPomodorosPorUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        List<PomodoroDTO> pomodoros = pomodoroServicio.obtenerPomodorosPorUsuario(usuarioId);
        return new ResponseEntity<>(pomodoros, HttpStatus.OK);
    }

    /**
     * Obtiene los pomodoros entre dos fechas
     *
     * @param fechaInicio Fecha de inicio para la búsqueda
     * @param fechaFin Fecha de fin para la búsqueda
     * @return Lista de pomodoros en el rango de fechas especificado
     */
    @GetMapping("/entre-fechas")
    @Operation(summary = "Obtener pomodoros entre fechas",
            description = "Retorna los pomodoros registrados entre las fechas especificadas")
    @ApiResponse(responseCode = "200", description = "Lista de pomodoros obtenida con éxito")
    public ResponseEntity<List<PomodoroDTO>> obtenerPomodorosEntreFechas(
            @Parameter(description = "Fecha de inicio (formato ISO)", example = "2025-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin (formato ISO)", example = "2025-01-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<PomodoroDTO> pomodoros = pomodoroServicio.obtenerPomodorosEntreFechas(fechaInicio, fechaFin);
        return new ResponseEntity<>(pomodoros, HttpStatus.OK);
    }

    /**
     * Cuenta el número de pomodoros de un usuario
     *
     * @param usuarioId ID del usuario
     * @return Número de pomodoros del usuario
     */
    @GetMapping("/contar/usuario/{usuarioId}")
    @Operation(summary = "Contar pomodoros por usuario", description = "Retorna el número total de pomodoros de un usuario")
    @ApiResponse(responseCode = "200", description = "Conteo exitoso")
    public ResponseEntity<Long> contarPomodorosPorUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        long count = pomodoroServicio.contarPomodorosPorUsuario(usuarioId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}