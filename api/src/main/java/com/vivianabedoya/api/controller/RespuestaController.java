package com.vivianabedoya.api.controller;

import com.vivianabedoya.api.domain.curso.Curso;
import com.vivianabedoya.api.domain.respuesta.*;
import com.vivianabedoya.api.domain.topico.*;
import com.vivianabedoya.api.domain.usuario.Usuario;
import com.vivianabedoya.api.domain.usuario.UsuariosRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
public class RespuestaController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuesta> agregar(@RequestBody @Valid DatosRegistroRespuesta datos, UriComponentsBuilder uriBuilder) {

        Optional<Usuario> autorOptional = usuariosRepository.findById(datos.usuario_id());
        Optional<Topico> topicoOptional = topicoRepository.findById(datos.topico_id());

        Usuario autor = autorOptional.get();
        Topico topico = topicoOptional.get();
        Respuesta respuesta = new Respuesta(datos);
        respuesta.setAutor(autor);
        respuesta.setTopico(topico);
        respuestaRepository.save(respuesta);
        URI url = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        DatosRespuesta res = new DatosRespuesta(respuesta.getId(),respuesta.getSolucion(), respuesta.getFechaCreacion(),
                respuesta.getTopico().getTitulo(), respuesta.getAutor().getCorreoElectronico(), respuesta.getMensaje());

        return ResponseEntity.created(url).body(res);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuesta> actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizarRespuesta datosActualizados) {
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        respuesta.actualizar(datosActualizados);

        DatosRespuesta res = new DatosRespuesta(respuesta.getId(),respuesta.getSolucion(), respuesta.getFechaCreacion(), respuesta.getTopico().getTitulo(), respuesta.getAutor().getCorreoElectronico(), respuesta.getMensaje());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminar(@PathVariable Long id) {
        respuestaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<DatosRespuesta> getRespuestas(@PageableDefault(size = 5, sort = "fechaCreacion") Pageable paginacion) {
        Page<Respuesta> respuestas = respuestaRepository.findAll(paginacion);
        Page<DatosRespuesta> respuesta = respuestas.map(x -> new DatosRespuesta(
                x.getId(),
                x.getSolucion(),
                x.getFechaCreacion(),
                x.getTopico().getTitulo(),
                x.getAutor().getCorreoElectronico(),
                x.getMensaje()
        ));
        return respuesta;
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuesta> getRespuesta(@PathVariable Long id) {
        Optional<Respuesta> respuestaOptional = respuestaRepository.findById(id);

        Respuesta respuesta = respuestaOptional.get();
        DatosRespuesta res = new DatosRespuesta(
                respuesta.getId(),
                respuesta.getSolucion(),
                respuesta.getFechaCreacion(),
                respuesta.getTopico().getTitulo(),
                respuesta.getAutor().getCorreoElectronico(),
                respuesta.getMensaje()
        );

        return ResponseEntity.ok(res);
    }
}
