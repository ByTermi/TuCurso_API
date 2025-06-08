package es.tfg.tu_curso.seguridad;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Clase de utilidad para la gestión de tokens JWT.
 * Proporciona métodos para generar, validar y manipular tokens JWT utilizados
 * para la autenticación y autorización de usuarios en la aplicación.
 */
@Component
public class JwtUtil {

    /**
     * Clave secreta utilizada para firmar los tokens JWT.
     * Se inyecta desde el archivo de propiedades de la aplicación.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Tiempo de expiración del token JWT en milisegundos.
     * Se inyecta desde el archivo de propiedades de la aplicación.
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Genera un nuevo token JWT para el nombre de usuario especificado.
     *
     * @param username El nombre de usuario para el cual se generará el token
     * @return Un token JWT válido
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Genera un nuevo token JWT para el nombre de usuario y rol especificados.
     *
     * @param username El nombre de usuario para el cual se generará el token
     * @param role     El rol del usuario que se incluirá en el token
     * @return Un token JWT válido
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Obtiene la fecha de expiración de un token JWT.
     *
     * @param token El token JWT del cual se extraerá la fecha de expiración
     * @return La fecha de expiración del token
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    /**
     * Refresca un token JWT existente, manteniendo los claims originales
     * pero actualizando las fechas de emisión y expiración.
     *
     * @param token El token JWT a refrescar
     * @return Un nuevo token JWT con fechas actualizadas
     */
    public String refreshToken(String token) {
        // Extrae los claims del token existente
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        // Genera un nuevo token con los mismos claims pero con una nueva fecha de expiración
        return Jwts.builder()
                .setClaims(claims) // Usa los claims del token original
                .setIssuedAt(new Date()) // Nueva fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Nueva fecha de expiración
                .signWith(SignatureAlgorithm.HS512, secret) // Firma el token
                .compact();
    }

    /**
     * Valida un token JWT verificando su firma y estructura.
     *
     * @param token El token JWT a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            // Firma inválida
        } catch (MalformedJwtException e) {
            // Token mal formado
        } catch (ExpiredJwtException e) {
            // Token expirado
        } catch (UnsupportedJwtException e) {
            // Token no soportado
        } catch (IllegalArgumentException e) {
            // Claims vacíos
        }
        return false;
    }

    /**
     * Método auxiliar para extraer los claims del token JWT.
     *
     * @param token El token JWT del cual se extraerán los claims
     * @return Los claims contenidos en el token
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene el nombre de usuario (subject) contenido en un token JWT.
     *
     * @param token El token JWT del cual se extraerá el nombre de usuario
     * @return El nombre de usuario contenido en el token
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * Extrae el rol del usuario del token JWT.
     *
     * @param token El token JWT
     * @return El rol del usuario
     */
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }
}