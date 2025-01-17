package com.vivianabedoya.api.controller;

import com.vivianabedoya.api.domain.curso.Curso;
import com.vivianabedoya.api.domain.curso.CursoRepository;
import com.vivianabedoya.api.domain.respuesta.Respuesta;
import com.vivianabedoya.api.domain.respuesta.RespuestaRepository;
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
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosTopico> agregar(@RequestBody @Valid DatosRegistroTopico datos, UriComponentsBuilder uriBuilder) {

        Optional<Usuario> autorOptional = usuariosRepository.findById(datos.usuario_id());
        Optional<Curso> cursoOptional = cursoRepository.findById(datos.curso_id());

        if (autorOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        if (cursoOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        if (topicoRepository.existsByTitulo(datos.titulo()) || topicoRepository.existsByMensaje(datos.mensaje())) {
            return ResponseEntity.badRequest().body(null);
        }

        Usuario autor = autorOptional.get();
        Curso curso = cursoOptional.get();
        Topico topico = new Topico(datos);
        topico.setAutor(autor);
        topico.setCurso(curso);
        topicoRepository.save(topico);
        URI url = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        DatosTopico respuesta = new DatosTopico(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getStatus(), topico.getAutor().getCorreoElectronico(), topico.getRespuestas());

        return ResponseEntity.created(url).body(respuesta);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosTopico> actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizarTopico datosActualizados) {
        Topico topico = topicoRepository.getReferenceById(id);
        topico.actualizar(datosActualizados);
        List<Respuesta> respuestas = respuestaRepository.findByTopicoId(topico.getId());
        if(respuestas != null && respuestas.size() > 0){
            topico.setRespuestas(respuestas);
        }
        Usuario autor = usuariosRepository.getReferenceById(topico.getAutor().getId());
        DatosTopico respuesta = new DatosTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getStatus(), autor.getCorreoElectronico(), respuestas);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminar(@PathVariable Long id) {
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<DatosTopico> getTopicos(@PageableDefault(size = 5, sort = "fechaCreacion") Pageable paginacion) {
        Page<Topico> topicos = topicoRepository.findAll(paginacion);
        Page<DatosTopico> respuesta = topicos.map(x -> new DatosTopico(
                x.getId(),
                x.getTitulo(),
                x.getMensaje(),
                x.getStatus(),
                x.getAutor().getCorreoElectronico(),
                x.getRespuestas()
        ));
        return respuesta;
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosTopico> getTopico(@PathVariable Long id) {
        Optional<Topico> topicoOptional = topicoRepository.findById(id);
        if (topicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Topico topico = topicoOptional.get();
        Usuario autor = usuariosRepository.getReferenceById(topico.getAutor().getId());
        DatosTopico respuesta = new DatosTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getStatus(),
                autor.getCorreoElectronico(),
                topico.getRespuestas()
        );

        return ResponseEntity.ok(respuesta);
    }
}
