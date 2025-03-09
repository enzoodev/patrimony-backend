package com.dpmg.patrimonio.models.dtos.shared;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindByInventoryIdDTO {
    @NotNull(message = "O parâmetro 'idInventario' é obrigatório")
    private Long idInventario;
}
