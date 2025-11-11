package com.restaurant.saborgourmet.repository;

import com.restaurant.saborgourmet.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    boolean existsByNombreUsuario(String nombreUsuario);

    List<Usuario> findByRol(Usuario.Rol rol);

    List<Usuario> findByEstado(Boolean estado);

    List<Usuario> findByRolAndEstado(Usuario.Rol rol, Boolean estado);

    @Query("SELECT u FROM Usuario u WHERE u.estado = true ORDER BY u.nombreUsuario")
    List<Usuario> findAllActiveUsers();

    long countByRol(Usuario.Rol rol);

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombreUsuario) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Usuario> searchByNombreUsuario(@Param("nombre") String nombre);
}
