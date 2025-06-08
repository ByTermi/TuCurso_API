package es.tfg.tu_curso.seguridad;

/**
 * Clase que representa una solicitud de inicio de sesión.
 * Contiene los datos necesarios para autenticar a un usuario en el sistema,
 * específicamente su dirección de correo electrónico y contraseña.
 */
public class LoginRequest {
    /**
     * Dirección de correo electrónico del usuario que intenta iniciar sesión.
     * Se utiliza como identificador único del usuario en el sistema.
     */
    private String email;

    /**
     * Contraseña del usuario para validar su identidad.
     * Esta contraseña se verificará contra la almacenada en la base de datos.
     */
    private String pass;

    /**
     * Obtiene la dirección de correo electrónico del usuario.
     *
     * @return La dirección de correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece la dirección de correo electrónico del usuario.
     *
     * @param email La dirección de correo electrónico a establecer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return La contraseña del usuario
     */
    public String getPass() {
        return pass;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param pass La contraseña a establecer
     */
    public void setPass(String pass) {
        this.pass = pass;
    }
}