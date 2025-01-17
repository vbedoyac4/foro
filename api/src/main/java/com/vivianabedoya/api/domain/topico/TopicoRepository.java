package com.vivianabedoya.api.domain.topico;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<Topico, Long>{

    boolean existsByTitulo(String titulo);

    boolean existsByMensaje(String mensaje);
}
