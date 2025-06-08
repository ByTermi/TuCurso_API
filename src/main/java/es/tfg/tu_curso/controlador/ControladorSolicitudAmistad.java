package es.tfg.tu_curso.controlador;

import es.tfg.tu_curso.dto.SolicitudAmistadDTO;
import es.tfg.tu_curso.servicio.interfaces.SolicitudAmistadServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar las solicitudes de amistad
 * Proporciona endpoints para enviar, aceptar, rechazar y consultar solicitudes de amistad
 */
@RestController
@RequestMapping("/solicitudes-amistad")
@Tag(name = "Solicitudes de Amistad", description = "API para la gestión de solicitudes de amistad")
public class ControladorSolicitudAmistad {

    @Autowired
    private SolicitudAmistadServicio solicitudAmistadServicio;

    /**
     * Envía una solicitud de amistad desde un usuario a otro
     *
     * @param emisorId ID del usuario que envía la solicitud
     * @param receptorId ID del usuario que recibe la solicitud
     * @return Mensaje de confirmación y código HTTP correspondiente
     */
    @PostMapping("/enviar")
    @Operation(summary = "Enviar solicitud de amistad", description = "Envía una solicitud de amistad entre dos usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud enviada exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo enviar la solicitud", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> enviarSolicitud(
            @Parameter(description = "ID del usuario emisor") @RequestParam Long emisorId,
            @Parameter(description = "ID del usuario receptor") @RequestParam Long receptorId) {

        if (solicitudAmistadServicio.enviarSolicitud(emisorId, receptorId)) {
            return new ResponseEntity<>("Solicitud de amistad enviada exitosamente", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("No se pudo enviar la solicitud de amistad", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Acepta una solicitud de amistad por su ID
     *
     * @param solicitudId ID de la solicitud a aceptar
     * @return Mensaje de confirmación y código HTTP correspondiente
     */
    @PostMapping("/{solicitudId}/aceptar")
    @Operation(summary = "Aceptar solicitud de amistad", description = "Acepta una solicitud de amistad específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud aceptada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> aceptarSolicitud(
            @Parameter(description = "ID de la solicitud") @PathVariable Long solicitudId) {

        if (solicitudAmistadServicio.aceptarSolicitud(solicitudId)) {
            return new ResponseEntity<>("Solicitud de amistad aceptada exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo aceptar la solicitud", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Rechaza una solicitud de amistad por su ID
     *
     * @param solicitudId ID de la solicitud a rechazar
     * @return Mensaje de confirmación y código HTTP correspondiente
     */
    @PostMapping("/{solicitudId}/rechazar")
    @Operation(summary = "Rechazar solicitud de amistad", description = "Rechaza una solicitud de amistad específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud rechazada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> rechazarSolicitud(
            @Parameter(description = "ID de la solicitud") @PathVariable Long solicitudId) {

        if (solicitudAmistadServicio.rechazarSolicitud(solicitudId)) {
            return new ResponseEntity<>("Solicitud de amistad rechazada exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo rechazar la solicitud", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene todas las solicitudes de amistad recibidas por un usuario
     *
     * @param receptorId ID del usuario receptor
     * @return Lista de solicitudes recibidas
     */
    @GetMapping("/recibidas/{receptorId}")
    @Operation(summary = "Obtener solicitudes recibidas", description = "Retorna todas las solicitudes de amistad recibidas por un usuario")
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida con éxito",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SolicitudAmistadDTO.class)))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SolicitudAmistadDTO>> obtenerSolicitudesRecibidas(
            @Parameter(description = "ID del usuario receptor") @PathVariable Long receptorId) {

        List<SolicitudAmistadDTO> solicitudes = solicitudAmistadServicio.obtenerSolicitudesRecibidasDTO(receptorId);
        return new ResponseEntity<>(solicitudes, HttpStatus.OK);
    }

    /**
     * Obtiene todas las solicitudes de amistad enviadas por un usuario
     *
     * @param emisorId ID del usuario emisor
     * @return Lista de solicitudes enviadas
     */
    @GetMapping("/enviadas/{emisorId}")
    @Operation(summary = "Obtener solicitudes enviadas", description = "Retorna todas las solicitudes de amistad enviadas por un usuario")
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida con éxito",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SolicitudAmistadDTO.class)))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SolicitudAmistadDTO>> obtenerSolicitudesEnviadas(
            @Parameter(description = "ID del usuario emisor") @PathVariable Long emisorId) {

        List<SolicitudAmistadDTO> solicitudes = solicitudAmistadServicio.obtenerSolicitudesEnviadasDTO(emisorId);
        return new ResponseEntity<>(solicitudes, HttpStatus.OK);
    }

    /**
     * Verifica si existe una solicitud pendiente entre dos usuarios
     *
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @return Resultado de la verificación
     */
    @GetMapping("/verificar-pendiente")
    @Operation(summary = "Verificar solicitud pendiente", description = "Verifica si existe una solicitud pendiente entre dos usuarios")
    @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"existeSolicitud\": true}")))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Map<String, Boolean>> verificarSolicitudPendiente(
            @Parameter(description = "ID del primer usuario") @RequestParam Long usuario1Id,
            @Parameter(description = "ID del segundo usuario") @RequestParam Long usuario2Id) {

        boolean existeSolicitud = solicitudAmistadServicio.existeSolicitudPendiente(usuario1Id, usuario2Id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("existeSolicitud", existeSolicitud);
        return ResponseEntity.ok(response);
    }

    /**
     * Cuenta el número de solicitudes recibidas por un usuario
     *
     * @param receptorId ID del usuario receptor
     * @return Número de solicitudes recibidas
     */
    @GetMapping("/contar-recibidas/{receptorId}")
    @Operation(summary = "Contar solicitudes recibidas", description = "Retorna el número total de solicitudes recibidas por un usuario")
    @ApiResponse(responseCode = "200", description = "Conteo exitoso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"cantidadSolicitudes\": 3}")))
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Map<String, Long>> contarSolicitudesRecibidas(
            @Parameter(description = "ID del usuario receptor") @PathVariable Long receptorId) {

        long cantidadSolicitudes = solicitudAmistadServicio.contarSolicitudesRecibidas(receptorId);
        Map<String, Long> response = new HashMap<>();
        response.put("cantidadSolicitudes", cantidadSolicitudes);
        return ResponseEntity.ok(response);
    }
}