package com.restaurant.saborgourmet.service;

import com.restaurant.saborgourmet.model.Bitacora;
import com.restaurant.saborgourmet.model.Usuario;
import com.restaurant.saborgourmet.repository.BitacoraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BitacoraServiceImpl implements BitacoraService {

    private final BitacoraRepository bitacoraRepository;

    @Override
    public Bitacora registrarAccion(Usuario usuario, String accion, String tablaAfectada,
                                    Long registroId, String ipAddress, String detalles) {
        log.debug("Registrando acción en bitácora: {} - {} - Tabla: {} - ID: {}", 
                  usuario != null ? usuario.getNombreUsuario() : "SYSTEM", 
                  accion, tablaAfectada, registroId);

        Bitacora bitacora = new Bitacora(usuario, accion, tablaAfectada, registroId, ipAddress, detalles);
        return bitacoraRepository.save(bitacora);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Bitacora> listarTodos(Pageable pageable) {
        return bitacoraRepository.findAllByOrderByFechaHoraDesc(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Bitacora> buscarPorUsuario(Usuario usuario, Pageable pageable) {
        return bitacoraRepository.findByUsuario(usuario, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Bitacora> buscarPorTabla(String tabla, Pageable pageable) {
        return bitacoraRepository.findByTablaAfectada(tabla, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Bitacora> buscarPorAccion(String accion, Pageable pageable) {
        return bitacoraRepository.findByAccion(accion, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Bitacora> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable) {
        return bitacoraRepository.findByFechaHoraBetween(fechaInicio, fechaFin, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Bitacora> buscarPorCriterios(Usuario usuario, String tabla, String accion, Pageable pageable) {
        return bitacoraRepository.findByMultipleCriteria(usuario, tabla, accion, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bitacora> obtenerUltimasAcciones() {
        return bitacoraRepository.findTop10ByOrderByFechaHoraDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarPorUsuario(Usuario usuario) {
        return bitacoraRepository.countByUsuario(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarPorTabla(String tabla) {
        return bitacoraRepository.countByTablaAfectada(tabla);
    }
}
