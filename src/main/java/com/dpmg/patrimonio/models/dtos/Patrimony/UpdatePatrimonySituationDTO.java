package com.dpmg.patrimonio.models.dtos.Patrimony;

import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import com.dpmg.patrimonio.models.enums.PatrimonySituationEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePatrimonySituationDTO extends BaseAuditDTO {
    @NotNull(message = "A situação é obrigatória")
    private PatrimonySituationEnum situacao;
}
