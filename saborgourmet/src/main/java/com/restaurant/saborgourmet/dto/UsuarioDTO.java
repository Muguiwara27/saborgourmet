package com.restaurant.saborgourmet.dto;

import com.restaurant.saborgourmet.model.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long idUsuario;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 50, message = "El nombre de usuario debe tener entre 4 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El nombre de usuario solo puede contener letras, números y guiones bajos")
    private String nombreUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
        message = "La contraseña debe contener al menos una mayúscula, una minúscula y un número"
    )
    private String contrasena;

    private String confirmarContrasena;

    @NotBlank(message = "El rol es obligatorio")
    private String rol;

    private Boolean estado;


    public UsuarioDTO(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.rol = usuario.getRol().name();
        this.estado = usuario.getEstado();
    }

    public boolean passwordsMatch() {
        return contrasena != null && contrasena.equals(confirmarContrasena);
    }


    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(this.idUsuario);
        usuario.setNombreUsuario(this.nombreUsuario);
        usuario.setContrasena(this.contrasena);
        usuario.setRol(Usuario.Rol.valueOf(this.rol));
        usuario.setEstado(this.estado != null ? this.estado : true);
        return usuario;
    }
}
