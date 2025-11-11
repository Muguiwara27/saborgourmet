package com.restaurant.saborgourmet.service;

import com.restaurant.saborgourmet.dto.UsuarioDTO;
import com.restaurant.saborgourmet.model.Usuario;
import com.restaurant.saborgourmet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario crearUsuario(UsuarioDTO usuarioDTO) {
        log.info("Creando nuevo usuario: {}", usuarioDTO.getNombreUsuario());

        // Validar que el nombre de usuario no exista
        if (usuarioRepository.existsByNombreUsuario(usuarioDTO.getNombreUsuario())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        // Validar que las contraseñas coincidan
        if (!usuarioDTO.passwordsMatch()) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        // Crear usuario
        Usuario usuario = usuarioDTO.toEntity();
        
        // Cifrar contraseña con BCrypt (strength 12)
        usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        log.info("Usuario creado exitosamente con ID: {}", usuarioGuardado.getIdUsuario());

        return usuarioGuardado;
    }

    @Override
    public Usuario actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        log.info("Actualizando usuario con ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar nombre de usuario único (si cambió)
        if (!usuario.getNombreUsuario().equals(usuarioDTO.getNombreUsuario()) &&
            usuarioRepository.existsByNombreUsuario(usuarioDTO.getNombreUsuario())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        // Actualizar datos
        usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
        usuario.setRol(Usuario.Rol.valueOf(usuarioDTO.getRol()));
        usuario.setEstado(usuarioDTO.getEstado());

        // Actualizar contraseña solo si se proporcionó una nueva
        if (usuarioDTO.getContrasena() != null && !usuarioDTO.getContrasena().isEmpty()) {
            if (!usuarioDTO.passwordsMatch()) {
                throw new IllegalArgumentException("Las contraseñas no coinciden");
            }
            usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        log.info("Usuario actualizado exitosamente: {}", usuarioActualizado.getNombreUsuario());

        return usuarioActualizado;
    }

    @Override
    public Usuario cambiarEstado(Long id, Boolean estado) {
        log.info("Cambiando estado del usuario ID: {} a {}", id, estado);

        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setEstado(estado);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) {
        log.info("Eliminando usuario con ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Eliminación lógica (cambiar estado a inactivo)
        usuario.setEstado(false);
        usuarioRepository.save(usuario);

        log.info("Usuario desactivado exitosamente: {}", usuario.getNombreUsuario());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarActivos() {
        return usuarioRepository.findAllActiveUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarPorRol(Usuario.Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarPorRolYEstado(Usuario.Rol rol, Boolean estado) {
        return usuarioRepository.findByRolAndEstado(rol, estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.searchByNombreUsuario(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeNombreUsuario(String nombreUsuario) {
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }

    @Override
    public void cambiarContrasena(Long id, String nuevaContrasena) {
        log.info("Cambiando contraseña del usuario ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);

        log.info("Contraseña actualizada exitosamente para: {}", usuario.getNombreUsuario());
    }
}
