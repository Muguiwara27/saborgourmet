package com.restaurant.saborgourmet.aspect;

import com.restaurant.saborgourmet.model.Bitacora;
import com.restaurant.saborgourmet.model.Usuario;
import com.restaurant.saborgourmet.repository.BitacoraRepository;
import com.restaurant.saborgourmet.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final BitacoraRepository bitacoraRepository;
    private final UsuarioRepository usuarioRepository;

    @AfterReturning(
        pointcut = "execution(* com.restaurant.saborgourmet.service.*Service*.save*(..)) || " +
                   "execution(* com.restaurant.saborgourmet.service.*Service*.crear*(..)) || " +
                   "execution(* com.restaurant.saborgourmet.service.*Service*.insert*(..))",
        returning = "result"
    )
    public void logCreateAction(JoinPoint joinPoint, Object result) {
        try {
            log.debug("AuditAspect: Interceptando operación de CREACIÓN");
            
            String tableName = extractTableName(joinPoint);
            Long recordId = extractRecordId(result);
            String details = buildDetails(joinPoint, "CREAR");
            
            registrarEnBitacora("CREAR", tableName, recordId, details);
            
        } catch (Exception e) {
            log.error("Error al registrar acción de CREACIÓN en bitácora", e);
        }
    }

    @AfterReturning(
        pointcut = "execution(* com.restaurant.saborgourmet.service.*Service*.update*(..)) || " +
                   "execution(* com.restaurant.saborgourmet.service.*Service*.actualizar*(..)) || " +
                   "execution(* com.restaurant.saborgourmet.service.*Service*.edit*(..)) || " +
                   "execution(* com.restaurant.saborgourmet.service.*Service*.modify*(..)) || " +
                   "execution(* com.restaurant.saborgourmet.service.*Service*.cambiar*(..))",
        returning = "result"
    )
    public void logUpdateAction(JoinPoint joinPoint, Object result) {
        try {
            log.debug("AuditAspect: Interceptando operación de ACTUALIZACIÓN");
            
            String tableName = extractTableName(joinPoint);
            Long recordId = extractRecordId(result);
            String details = buildDetails(joinPoint, "ACTUALIZAR");
            
            registrarEnBitacora("ACTUALIZAR", tableName, recordId, details);
            
        } catch (Exception e) {
            log.error("Error al registrar acción de ACTUALIZACIÓN en bitácora", e);
        }
    }

    @AfterReturning(
        pointcut = "execution(* com.restaurant.saborgourmet.service.*Service*.delete*(..)) || " +
                   "execution(* com.restaurant.saborgourmet.service.*Service*.eliminar*(..)) || " +
                   "execution(* com.restaurant.saborgourmet.service.*Service*.remove*(..))"
    )
    public void logDeleteAction(JoinPoint joinPoint) {
        try {
            log.debug("AuditAspect: Interceptando operación de ELIMINACIÓN");
            
            String tableName = extractTableName(joinPoint);
            Long recordId = extractRecordIdFromArgs(joinPoint);
            String details = buildDetails(joinPoint, "ELIMINAR");
            
            registrarEnBitacora("ELIMINAR", tableName, recordId, details);
            
        } catch (Exception e) {
            log.error("Error al registrar acción de ELIMINACIÓN en bitácora", e);
        }
    }


    private void registrarEnBitacora(String accion, String tableName, Long recordId, String details) {
        try {

            Usuario usuario = obtenerUsuarioAutenticado();

            String ipAddress = obtenerIpCliente();

            Bitacora bitacora = new Bitacora();
            bitacora.setUsuario(usuario);
            bitacora.setAccion(accion);
            bitacora.setTablaAfectada(tableName);
            bitacora.setRegistroId(recordId);
            bitacora.setIpAddress(ipAddress);
            bitacora.setDetalles(details);

            bitacoraRepository.save(bitacora);
            
            log.info("✅ Auditoría registrada: {} - {} - Tabla: {} - ID: {} - Usuario: {}", 
                     accion, tableName, recordId, 
                     usuario != null ? usuario.getNombreUsuario() : "SYSTEM",
                     ipAddress);
            
        } catch (Exception e) {
            log.error("Error crítico al guardar en bitácora", e);
        }
    }

    private Usuario obtenerUsuarioAutenticado() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.isAuthenticated() && 
                !"anonymousUser".equals(authentication.getPrincipal())) {
                
                String username = authentication.getName();
                Optional<Usuario> usuario = usuarioRepository.findByNombreUsuario(username);
                
                if (usuario.isPresent()) {
                    return usuario.get();
                }
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener usuario autenticado", e);
        }
        
        return null; // Usuario del sistema
    }

    private String obtenerIpCliente() {
        try {
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                // Intentar obtener IP real (considerando proxies)
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
                
                return ip;
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener IP del cliente", e);
        }
        
        return "0.0.0.0";
    }

    private String extractTableName(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        // Remover "Service" o "ServiceImpl" del nombre
        String tableName = className
            .replace("ServiceImpl", "")
            .replace("Service", "")
            .toLowerCase();
        
        return tableName;
    }

    private Long extractRecordId(Object result) {
        if (result == null) {
            return null;
        }
        
        try {
            // Intentar obtener el ID usando reflexión
            Method getIdMethod = null;
            
            // Buscar método getId, getIdUsuario, etc.
            for (Method method : result.getClass().getMethods()) {
                String methodName = method.getName();
                if (methodName.startsWith("getId") && method.getParameterCount() == 0) {
                    getIdMethod = method;
                    break;
                }
            }
            
            if (getIdMethod != null) {
                Object id = getIdMethod.invoke(result);
                if (id instanceof Long) {
                    return (Long) id;
                } else if (id instanceof Integer) {
                    return ((Integer) id).longValue();
                }
            }
        } catch (Exception e) {
            log.debug("No se pudo extraer ID del resultado", e);
        }
        
        return null;
    }

    private Long extractRecordIdFromArgs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        
        if (args != null && args.length > 0) {
            Object firstArg = args[0];
            
            if (firstArg instanceof Long) {
                return (Long) firstArg;
            } else if (firstArg instanceof Integer) {
                return ((Integer) firstArg).longValue();
            }
        }
        
        return null;
    }

    private String buildDetails(JoinPoint joinPoint, String accion) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        return String.format("Método: %s.%s() - Acción: %s", className, methodName, accion);
    }
}
