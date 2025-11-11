package com.restaurant.saborgourmet.service;

import com.restaurant.saborgourmet.model.Usuario;
import com.restaurant.saborgourmet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Intentando autenticar usuario: {}", username);

        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
            .orElseThrow(() -> {
                log.error("Usuario no encontrado: {}", username);
                return new UsernameNotFoundException("Usuario no encontrado: " + username);
            });

        // Verificar si el usuario está activo
        if (!usuario.getEstado()) {
            log.warn("Intento de acceso con usuario inactivo: {}", username);
            throw new UsernameNotFoundException("Usuario inactivo: " + username);
        }

        // Crear lista de autoridades (roles)
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));

        log.info("Usuario autenticado exitosamente: {} con rol: {}", username, usuario.getRol());

        // Retornar UserDetails de Spring Security
        return User.builder()
            .username(usuario.getNombreUsuario())
            .password(usuario.getContrasena())
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!usuario.getEstado())
            .build();
    }
}
