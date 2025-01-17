package com.vivianabedoya.api.domain.topico;

public record DatosActualizarTopico(
        Long id,
        String titulo,
        String mensaje,
        String status) {
}
