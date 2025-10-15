package org.example.funkos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.funkos.models.Funko;

@Data
@AllArgsConstructor
public class FunkoDeleteResponse {
    private String mensaje;
    private Funko deleted;
}
