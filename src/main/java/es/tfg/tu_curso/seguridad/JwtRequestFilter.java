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

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}") // Inyecta la clave secreta desde application.properties
    private String secret;

    private final UserDetailsService userDetailsService;

    // Constructor que recibe el servicio UserDetailsService
    public JwtRequestFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Obtiene el encabezado "Authorization" de la solicitud HTTP
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null; // Variable para almacenar el nombre de usuario extraído del token
        String jwt = null;      // Variable para almacenar el token JWT

        // Verifica si el encabezado "Authorization" existe y comienza con "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extrae el token JWT eliminando el prefijo "Bearer "
            jwt = authorizationHeader.substring(7);

            // Parsea el token JWT para extraer los claims (información contenida en el token)
            Claims claims = Jwts.parser()
                    .setSigningKey(secret) // Usa la clave secreta para validar el token
                    .parseClaimsJws(jwt)   // Parsea el token
                    .getBody();            // Obtiene los claims del token

            // Extrae el nombre de usuario (subject) del token
            username = claims.getSubject();
        }

        // Si se encontró un nombre de usuario y no hay una autenticación previa en el contexto de seguridad...
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carga los detalles del usuario (UserDetails) usando el nombre de usuario
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Valida el token JWT comparando el nombre de usuario del token con el del UserDetails
            if (Jwts.parser()
                    .setSigningKey(secret) // Usa la clave secreta para validar el token
                    .parseClaimsJws(jwt)    // Parsea el token
                    .getBody()              // Obtiene los claims del token
                    .getSubject()           // Obtiene el nombre de usuario del token
                    .equals(userDetails.getUsername())) { // Compara con el nombre de usuario de UserDetails

                // Crea un objeto de autenticación (UsernamePasswordAuthenticationToken) con los detalles del usuario
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Establece detalles adicionales de la autenticación (como la dirección IP del cliente)
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establece la autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // Continúa con la cadena de filtros para procesar la solicitud
        chain.doFilter(request, response);
    }
}