package com.dpmg.patrimonio.models.dtos.Patrimony;

import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyIfPatrimonyIsReadToBeLocalizedDTO extends BaseAuditDTO {
    @NotNull(message = "O parâmetro 'idInventario' é obrigatório")
    private Long idInventario;

    @NotNull(message = "O parâmetro 'codigoUnidadeResponsavel' é obrigatório")
    private Long codigoUnidadeResponsavel;

    @NotNull(message = "O parâmetro 'numeroPatrimonio' é obrigatório")
    private Long numeroPatrimonio;
}
