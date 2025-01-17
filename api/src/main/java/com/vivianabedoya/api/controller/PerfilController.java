package com.vivianabedoya.api.controller;

import com.vivianabedoya.api.domain.curso.Curso;
import com.vivianabedoya.api.domain.curso.DatosActualizarCurso;
import com.vivianabedoya.api.domain.perfil.DatosRegistro;
import com.vivianabedoya.api.domain.perfil.Perfil;
import com.vivianabedoya.api.domain.perfil.PerfilRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("perfiles")
@SecurityRequirement(name = "bearer-key")
public class PerfilController {

    @Autowired
    private PerfilRepository perfilRepository;

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminar(@PathVariable Long id){
        perfilRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<Perfil> getPerfiles(@PageableDefault(size = 5, sort = "nombre") Pageable paginacion){
        return perfilRepository.findAll(paginacion);
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRegistro> getPerfil(@PathVariable Long id){
        var perfil = perfilRepository.getReferenceById(id);
        DatosRegistro respuesta = new DatosRegistro( perfil.getNombre());

        return ResponseEntity.ok(respuesta);
    }
}
