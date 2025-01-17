package com.vivianabedoya.api.infra.security;

import com.vivianabedoya.api.domain.usuario.UsuariosRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuariosRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Filtrando solicitud...");

        // Verificar si la solicitud es para el endpoint de login
        if ( request.getMethod().equals("POST") && (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/register"))) {
            System.out.println("Solicitud de autenticacion, permitiendo acceso...");
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener el token del header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println("Token encontrado en el header.");
            String token = authHeader.replace("Bearer ", "");
            try {
                System.out.println("Verificando token...");
                String nombreUsuario = tokenService.getSubject(token); // extract username
                if (nombreUsuario != null) {
                    System.out.println("Token válido.");
                    // Token valido
                    var usuario = usuarioRepository.findByCorreoElectronico(nombreUsuario);
                    if (usuario != null) {
                        System.out.println("Usuario encontrado.");
                        var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); // Forzamos un inicio de sesion
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        System.out.println("Usuario no encontrado.");
                    }
                } else {
                    System.out.println("Token inválido.");
                }
            } catch (RuntimeException e) {
                System.out.println("Error al verificar el token: " + e.getMessage());
                // Manejar el error de verificación del token
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        } else {
            System.out.println("No se encontró token en el header.");
        }
        filterChain.doFilter(request, response);
    }
}
