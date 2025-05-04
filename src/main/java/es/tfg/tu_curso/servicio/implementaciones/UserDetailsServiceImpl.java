package es.tfg.tu_curso.servicio.implementaciones;

import es.tfg.tu_curso.modelo.Usuario;
import es.tfg.tu_curso.repositorio.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RepositorioUsuario usuarioRepository;

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