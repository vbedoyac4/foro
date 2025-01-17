package com.vivianabedoya.api.domain.respuesta;

import com.vivianabedoya.api.domain.topico.Topico;
import com.vivianabedoya.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Table(name = "respuesta")
@Entity(name = "Respuesta")
@EqualsAndHashCode(of = "id")
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensaje;
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    private Boolean solucion;

    public Respuesta() {
    }

    public Respuesta(Long id, Boolean solucion, Topico topico, Usuario autor, LocalDateTime fechaCreacion, String mensaje) {
        this.id = id;
        this.solucion = solucion;
        this.topico = topico;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
        this.mensaje = mensaje;
    }

    public Respuesta( DatosRegistroRespuesta datos) {
        this.solucion = datos.solucion();
        this.topico = new Topico();
        this.autor = new Usuario();
        this.fechaCreacion = LocalDateTime.now();
        this.mensaje = datos.mensaje();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Topico getTopico() {
        return topico;
    }

    public void setTopico(Topico topico) {
        this.topico = topico;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Boolean getSolucion() {
        return solucion;
    }

    public void setSolucion(Boolean solucion) {
        this.solucion = solucion;
    }

    public void actualizar( DatosActualizarRespuesta datosActualizados) {
        this.solucion = datosActualizados.solucion();
        this.mensaje = datosActualizados.mensaje();
    }
}