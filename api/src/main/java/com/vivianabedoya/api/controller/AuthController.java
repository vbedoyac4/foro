package com.vivianabedoya.api.controller;

import com.vivianabedoya.api.domain.perfil.Perfil;
import com.vivianabedoya.api.domain.perfil.PerfilRepository;
import com.vivianabedoya.api.domain.usuario.DatosAuth;
import com.vivianabedoya.api.domain.usuario.DatosRegistro;
import com.vivianabedoya.api.domain.usuario.Usuario;
import com.vivianabedoya.api.domain.usuario.UsuariosRepository;
import com.vivianabedoya.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuariosRepository usuariosRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PerfilRepository perfilRepository;

    @PostMapping("/login")
    public String login(@RequestBody DatosAuth usuario) throws AuthenticationException {
        System.out.println("Intentando iniciar sesión para el usuario: " + usuario.usuario());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.usuario(), usuario.contrasena())
        );
        System.out.println("Autenticación exitosa para el usuario: " + usuario.usuario());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenService.generarToken((Usuario) authentication.getPrincipal());
        System.out.println("Token generado: " + token);
        return token;
    }

    @PostMapping("/register")
    public String register(@RequestBody DatosRegistro datosRegistro) {

        if (usuariosRepository.existsByCorreoElectronico(datosRegistro.usuario())) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }

        String encodedPassword = passwordEncoder.encode(datosRegistro.contrasena());
        Perfil perfil = new Perfil();
        perfil.setNombre(datosRegistro.nombre());
        Perfil savedPerfil = perfilRepository.save(perfil);

        Usuario usuario = new Usuario();
        usuario.setNombre(datosRegistro.nombre());
        usuario.setCorreoElectronico(datosRegistro.usuario());
        usuario.setContrasena(encodedPassword);
        usuario.setPerfiles(savedPerfil.getId());

        Usuario savedUsuario = usuariosRepository.save(usuario);

        return "Usuario registrado con éxito: " + savedUsuario.getCorreoElectronico();
    }

}
