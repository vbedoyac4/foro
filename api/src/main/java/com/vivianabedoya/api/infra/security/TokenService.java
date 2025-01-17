package com.vivianabedoya.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vivianabedoya.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    public String generarToken(Usuario usuario) {
        System.out.println("Generando token para el usuario: " + usuario.getCorreoElectronico());
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("clinica")
                    .withSubject(usuario.getCorreoElectronico())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            System.out.println("Error al crear el token JWT: " + exception.getMessage());
            throw new RuntimeException("Error al crear el token JWT", exception);
        }
    }

    public String getSubject(String token) {
        System.out.println("Verificando token...");
        if (token == null) {
            System.out.println("Token no puede ser nulo.");
            throw new RuntimeException("Token no puede ser nulo");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            DecodedJWT verifier = JWT.require(algorithm)
                    .withIssuer("clinica")
                    .build()
                    .verify(token);
            System.out.println("Token verificado correctamente.");
            return verifier.getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println("Error al verificar el token JWT: " + exception.getMessage());
            throw new RuntimeException("Token JWT inválido", exception);
        }
    }

    private Instant generarFechaExpiracion() {
        return LocalDateTime.now().plusHours(4).toInstant(ZoneOffset.of("-06:00"));
    }
}
