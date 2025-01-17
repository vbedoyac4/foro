package com.vivianabedoya.api.domain.topico;

public record DatosRegistroTopico(
        String titulo,
        Long curso_id,
        String mensaje,
        String status,
        Long usuario_id
) {
}
