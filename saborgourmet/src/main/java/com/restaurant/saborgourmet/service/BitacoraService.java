package com.restaurant.saborgourmet.service;

import com.restaurant.saborgourmet.model.Bitacora;
import com.restaurant.saborgourmet.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;


public interface BitacoraService {


    Bitacora registrarAccion(Usuario usuario, String accion, String tablaAfectada, 
                            Long registroId, String ipAddress, String detalles);


    Page<Bitacora> listarTodos(Pageable pageable);


    Page<Bitacora> buscarPorUsuario(Usuario usuario, Pageable pageable);

    Page<Bitacora> buscarPorTabla(String tabla, Pageable pageable);


    Page<Bitacora> buscarPorAccion(String accion, Pageable pageable);


    Page<Bitacora> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);


    Page<Bitacora> buscarPorCriterios(Usuario usuario, String tabla, String accion, Pageable pageable);


    List<Bitacora> obtenerUltimasAcciones();


    long contarPorUsuario(Usuario usuario);


    long contarPorTabla(String tabla);
}
