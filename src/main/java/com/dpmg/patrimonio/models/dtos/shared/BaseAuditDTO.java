package com.dpmg.patrimonio.models.dtos.shared;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaseAuditDTO {
    @NotNull(message = "O parâmetro 'sgProjetoModificador' é obrigatório para definir qual a sigla do projeto responsável pela alteração")
    private String sgProjetoModificador;

    @NotNull(message = "O parâmetro 'sgAcaoModificadora' é obrigatório para definir qual a sigla da ação responsável pela alteração")
    private String sgAcaoModificadora;
}
