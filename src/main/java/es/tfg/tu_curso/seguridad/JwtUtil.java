package es.tfg.tu_curso.seguridad;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

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

    // Nuevo método para validar token
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

    // Método auxiliar para extraer los claims del token
    private Claims getClaimsFromToken(String token) { // ¡Este es el método faltante!
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    // Nuevo método para obtener username
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }
}