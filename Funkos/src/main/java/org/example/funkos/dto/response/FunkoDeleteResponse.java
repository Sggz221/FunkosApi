package org.example.funkos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.funkos.models.Funko;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunkoDeleteResponse {
    private String mensaje;
    private FunkoResponse deleted;
}
