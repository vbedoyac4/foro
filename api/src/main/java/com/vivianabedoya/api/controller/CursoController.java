package com.vivianabedoya.api.controller;

import com.vivianabedoya.api.domain.compartido.UriUtil;
import com.vivianabedoya.api.domain.curso.Curso;
import com.vivianabedoya.api.domain.curso.CursoRepository;
import com.vivianabedoya.api.domain.curso.DatosActualizarCurso;
import com.vivianabedoya.api.domain.curso.DatosCurso;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.net.URI;

@RestController
@RequestMapping("cursos")
@SecurityRequirement(name = "bearer-key")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosActualizarCurso> agregar(@RequestBody @Valid DatosCurso datos){
        Curso cursoActual = cursoRepository.save(new Curso(datos));
        DatosActualizarCurso respuesta = new DatosActualizarCurso(cursoActual.getId(), cursoActual.getNombre(), cursoActual.getCategoria());
        
        URI url = UriUtil.generarUriConId(cursoActual.getId());
        return ResponseEntity.created(url).body(respuesta);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosActualizarCurso> actualizar(@RequestBody @Valid DatosActualizarCurso datosActualizados){
        var cursoActual = cursoRepository.getReferenceById(datosActualizados.id());
        cursoActual.actualizar(datosActualizados);

        DatosActualizarCurso respuesta = new DatosActualizarCurso(cursoActual.getId(), cursoActual.getNombre(), cursoActual.getCategoria());
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminar(@PathVariable Long id){
        cursoRepository.deleteById(id);
        
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<Curso> getCursos(@PageableDefault(size = 5, sort = "nombre")Pageable paginacion){
        return cursoRepository.findAll(paginacion);
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosActualizarCurso> getCurso(@PathVariable Long id){
        var curso = cursoRepository.getReferenceById(id);
        DatosActualizarCurso respuesta = new DatosActualizarCurso(curso.getId(), curso.getNombre(), curso.getCategoria());

        return ResponseEntity.ok(respuesta);
    }
}
