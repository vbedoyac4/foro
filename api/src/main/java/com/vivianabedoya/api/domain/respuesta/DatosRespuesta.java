package com.vivianabedoya.api.domain.respuesta;

import java.time.LocalDateTime;

public record DatosRespuesta(
        Long id,
        Boolean solucion,
        LocalDateTime fechaCreacion,
        String topico,
        String usuario,
        String mensaje
) {
}
