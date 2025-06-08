package es.tfg.tu_curso.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro para procesar y validar tokens JWT en las solicitudes entrantes.
 * Este filtro intercepta cada solicitud HTTP y verifica si contiene un token JWT válido
 * en el encabezado de autorización. Si el token es válido, establece la autenticación
 * del usuario en el contexto de seguridad de Spring para permitir el acceso a los recursos protegidos.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    /**
     * Clave secreta utilizada para validar la firma de los tokens JWT.
     * Se inyecta desde el archivo de propiedades de la aplicación.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Servicio utilizado para cargar los detalles del usuario basándose en su nombre de usuario.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Constructor que inicializa el filtro con el servicio de detalles de usuario necesario.
     *
     * @param userDetailsService Servicio para cargar los detalles del usuario
     */
    public JwtRequestFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Método principal del filtro que procesa cada solicitud HTTP.
     * Extrae y valida el token JWT del encabezado de autorización y establece la autenticación
     * en el contexto de seguridad si el token es válido.
     *
     * @param request La solicitud HTTP entrante
     * @param response La respuesta HTTP saliente
     * @param chain La cadena de filtros para continuar el procesamiento
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        // Log para debugging
        System.out.println("=== JWT Filter Debug ===");
        System.out.println("URL solicitada: " + request.getRequestURL());
        System.out.println("Authorization Header: " + authorizationHeader);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            System.out.println("Token JWT extraído: " + jwt);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(jwt)
                        .getBody();
                username = claims.getSubject();
                System.out.println("Username extraído del token: " + username);
            } catch (Exception e) {
                System.out.println("Error al procesar el token JWT: " + e.getMessage());
                logger.error("Error al procesar el token JWT: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró token JWT válido en el header");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            System.out.println("Autoridades del usuario: " + userDetails.getAuthorities());

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(jwt)
                        .getBody();

                if (claims.getSubject().equals(userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("Autenticación establecida correctamente");
                }
            } catch (Exception e) {
                System.out.println("Error validando el token JWT: " + e.getMessage());
                logger.error("Error validando el token JWT: " + e.getMessage());
            }
        }
        System.out.println("============================");

        chain.doFilter(request, response);
    }
}