package com.dpmg.patrimonio.models.dtos.InventoryControl;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FindInventoryByYearDTO {
    @NotNull(message = "O parâmetro 'ano' é obrigatório.")
    private Integer ano;
}
