package es.tfg.tu_curso.servicio.implementaciones;

import es.tfg.tu_curso.modelo.Usuario;
import es.tfg.tu_curso.repositorio.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio UserDetailsService de Spring Security.
 * Esta clase es responsable de cargar información específica del usuario
 * para el proceso de autenticación y autorización en la aplicación.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * Repositorio para acceder a los datos de usuarios.
     */
    @Autowired
    private RepositorioUsuario usuarioRepository;

    /**
     * Carga los detalles del usuario identificado por su email para autenticación.
     * Este método es utilizado por Spring Security durante el proceso de autenticación.
     *
     * @param email El correo electrónico del usuario, utilizado como nombre de usuario
     * @return Un objeto UserDetails que contiene la información de autenticación del usuario
     * @throws UsernameNotFoundException Si no se encuentra ningún usuario con el email proporcionado
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return User.withUsername(usuario.getEmail())
                .password(usuario.getPass()) // <-- La contraseña ya debe estar encriptada en la base de datos
                .roles(usuario.getRol())
                .build();
    }
}