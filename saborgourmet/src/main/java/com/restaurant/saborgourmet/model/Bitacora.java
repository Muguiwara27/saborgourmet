package com.restaurant.saborgourmet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "bitacora", indexes = {
    @Index(name = "idx_usuario", columnList = "id_usuario"),
    @Index(name = "idx_fecha_hora", columnList = "fecha_hora"),
    @Index(name = "idx_tabla", columnList = "tabla_afectada"),
    @Index(name = "idx_accion", columnList = "accion")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora")
    private Long idBitacora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", foreignKey = @ForeignKey(name = "fk_bitacora_usuario"))
    private Usuario usuario;

    @Column(name = "accion", nullable = false, length = 255)
    private String accion;

    @Column(name = "tabla_afectada", length = 100)
    private String tablaAfectada;

    @Column(name = "registro_id")
    private Long registroId;

    @CreationTimestamp
    @Column(name = "fecha_hora", nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "detalles", columnDefinition = "TEXT")
    private String detalles;


    public Bitacora(Usuario usuario, String accion, String tablaAfectada, Long registroId, String ipAddress, String detalles) {
        this.usuario = usuario;
        this.accion = accion;
        this.tablaAfectada = tablaAfectada;
        this.registroId = registroId;
        this.ipAddress = ipAddress;
        this.detalles = detalles;
    }
}
