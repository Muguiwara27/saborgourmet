package com.restaurant.saborgourmet.repository;

import com.restaurant.saborgourmet.model.Bitacora;
import com.restaurant.saborgourmet.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {


    Page<Bitacora> findByUsuario(Usuario usuario, Pageable pageable);


    Page<Bitacora> findByTablaAfectada(String tablaAfectada, Pageable pageable);


    Page<Bitacora> findByAccion(String accion, Pageable pageable);


    @Query("SELECT b FROM Bitacora b WHERE b.fechaHora BETWEEN :fechaInicio AND :fechaFin ORDER BY b.fechaHora DESC")
    Page<Bitacora> findByFechaHoraBetween(
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin,
        Pageable pageable
    );


    Page<Bitacora> findAllByOrderByFechaHoraDesc(Pageable pageable);


    @Query("SELECT b FROM Bitacora b WHERE " +
           "(:usuario IS NULL OR b.usuario = :usuario) AND " +
           "(:tabla IS NULL OR b.tablaAfectada = :tabla) AND " +
           "(:accion IS NULL OR b.accion = :accion) " +
           "ORDER BY b.fechaHora DESC")
    Page<Bitacora> findByMultipleCriteria(
        @Param("usuario") Usuario usuario,
        @Param("tabla") String tabla,
        @Param("accion") String accion,
        Pageable pageable
    );


    List<Bitacora> findTop10ByOrderByFechaHoraDesc();


    long countByUsuario(Usuario usuario);


    long countByTablaAfectada(String tablaAfectada);
}
