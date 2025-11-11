package com.restaurant.saborgourmet.controller;

import com.restaurant.saborgourmet.model.Bitacora;
import com.restaurant.saborgourmet.model.Usuario;
import com.restaurant.saborgourmet.service.BitacoraService;
import com.restaurant.saborgourmet.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/bitacora")
@RequiredArgsConstructor
@Slf4j
public class BitacoraController {

    private final BitacoraService bitacoraService;
    private final UsuarioService usuarioService;


    @GetMapping
    public String listarBitacora(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) String tabla,
            @RequestParam(required = false) String accion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Bitacora> bitacoraPage;
        
        // Aplicar filtros
        if (usuarioId != null || tabla != null || accion != null) {
            Usuario usuario = null;
            if (usuarioId != null) {
                usuario = usuarioService.buscarPorId(usuarioId).orElse(null);
            }
            bitacoraPage = bitacoraService.buscarPorCriterios(usuario, tabla, accion, pageable);
        } else {
            bitacoraPage = bitacoraService.listarTodos(pageable);
        }
        
        // Obtener lista de usuarios para el filtro
        List<Usuario> usuarios = usuarioService.listarTodos();
        
        model.addAttribute("bitacoraPage", bitacoraPage);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bitacoraPage.getTotalPages());
        model.addAttribute("totalItems", bitacoraPage.getTotalElements());
        
        // Mantener filtros en el modelo
        model.addAttribute("usuarioId", usuarioId);
        model.addAttribute("tabla", tabla);
        model.addAttribute("accion", accion);
        
        return "admin/bitacora-list";
    }
}
