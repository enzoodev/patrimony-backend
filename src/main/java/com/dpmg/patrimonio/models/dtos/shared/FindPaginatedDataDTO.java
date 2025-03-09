package com.dpmg.patrimonio.models.dtos.shared;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FindPaginatedDataDTO {
    @NotNull(message = "O parâmetro 'page' é obrigatório para definir qual a página a ser retornada")
    @Min(value = 1, message = "O número da página deve ser maior que 0")
    private Integer page;

    @NotNull(message = "O parâmetro 'size' é obrigatório para definir o número de itens na página a ser retornada")
    @Min(value = 1, message = "O número de itens na página deve ser maior que 0")
    private Integer size;
}
