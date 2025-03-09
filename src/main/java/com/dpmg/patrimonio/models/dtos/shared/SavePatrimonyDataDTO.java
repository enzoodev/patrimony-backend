package com.dpmg.patrimonio.models.dtos.shared;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SavePatrimonyDataDTO extends SaveDataDTO {
    @NotNull(message = "O parâmetro 'idInventario' é obrigatório")
    private Long idInventario;
}
