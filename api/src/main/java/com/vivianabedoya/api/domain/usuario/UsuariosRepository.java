package com.vivianabedoya.api.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuariosRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByCorreoElectronico(String username);

    boolean existsByCorreoElectronico(String username);
}
