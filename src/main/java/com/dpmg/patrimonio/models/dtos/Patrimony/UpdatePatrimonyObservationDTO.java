package com.dpmg.patrimonio.models.dtos.Patrimony;

import com.dpmg.patrimonio.models.dtos.shared.BaseAuditDTO;
import lombok.Data;

@Data
public class UpdatePatrimonyObservationDTO extends BaseAuditDTO {
    private String sala;
    private String observacao;
}
