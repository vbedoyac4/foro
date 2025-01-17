package com.vivianabedoya.api.domain.respuesta;

public record DatosRegistroRespuesta(
        Boolean solucion,
        Long topico_id,
        Long usuario_id,
        String mensaje
) {
}
