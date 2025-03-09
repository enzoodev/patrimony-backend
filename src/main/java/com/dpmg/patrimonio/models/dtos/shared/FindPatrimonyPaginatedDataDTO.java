package com.dpmg.patrimonio.models.dtos.shared;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FindPatrimonyPaginatedDataDTO extends FindPaginatedDataDTO {
    @NotNull(message = "O parâmetro 'idInventario' é obrigatório")
    private Long idInventario;
}
