package com.vivianabedoya.api.domain.topico;

import com.vivianabedoya.api.domain.respuesta.Respuesta;
import java.util.List;

public record DatosTopico(
        Long id,
        String titulo,
        String mensaje,
        String status,
        String autor,
        List<Respuesta> respuestas
) {
}
