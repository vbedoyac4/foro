package com.vivianabedoya.api.controller;

import com.vivianabedoya.api.domain.perfil.Perfil;
import com.vivianabedoya.api.domain.perfil.PerfilRepository;
import com.vivianabedoya.api.domain.usuario.DatosActualizarUsuario;
import com.vivianabedoya.api.domain.usuario.Usuario;
import com.vivianabedoya.api.domain.usuario.UsuariosRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuariosRepository usuariosRepository;
    private PerfilRepository perfilRepository;

    @PutMapping
    @Transactional
    public ResponseEntity<DatosActualizarUsuario> actualizar(@RequestBody @Valid DatosActualizarUsuario datos){
        Usuario usuarioActual = usuariosRepository.getReferenceById(datos.id());
        usuarioActual.actualizar(datos);
        Perfil perfilActual = perfilRepository.getReferenceById(usuarioActual.getPerfiles());
        perfilActual.setNombre(datos.nombre());
        usuarioActual.setPerfiles(perfilActual.getId());

        DatosActualizarUsuario respuesta = new DatosActualizarUsuario(usuarioActual.getId(), usuarioActual.getNombre(), usuarioActual.getCorreoElectronico());
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    public Page<DatosActualizarUsuario> getUsuarios(@PageableDefault(size = 5, sort = "id") Pageable paginacion) {
        Page<Usuario> usuariosPage = usuariosRepository.findAll(paginacion);
        List<DatosActualizarUsuario> usuariosConvertidos = usuariosPage.getContent().stream()
                .map(Usuario::convertirADatosActualizarUsuario)
                .collect(Collectors.toList());

        return new PageImpl<>(usuariosConvertidos, paginacion, usuariosPage.getTotalElements());
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosActualizarUsuario> getUsuario(@PathVariable Long id){
        var usuario = usuariosRepository.getReferenceById(id);
        DatosActualizarUsuario respuesta = new DatosActualizarUsuario( usuario.getId(), usuario.getNombre(), usuario.getCorreoElectronico());

        return ResponseEntity.ok(respuesta);
    }

}
